package com.liguanqiao.grow.lock.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.liguanqiao.grow.lock.error.CannotAcquireLockException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Zookeeper Lock 注册
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
@Slf4j
public class ZookeeperLockRegistry {

    private static final String NODE_PATH_FORMAT = "/curator/%s/%s";

    private static final long DEFAULT_EXPIRE_AFTER = 60000L;

    private final String registryKey;

    private final CuratorFramework curatorFramework;

    private final long expireAfter;


    /**
     * An {@link ExecutorService} to call {@link InterProcessMutex#release} in
     * the separate thread when the current one is interrupted.
     */
    private Executor executor =
            Executors.newCachedThreadPool(ThreadFactoryBuilder.create().setNamePrefix("zk-lock-registry-").build());

    /**
     * Constructs a lock registry with the default (60 second) lock expiration.
     *
     * @param curatorFramework The connection factory.
     * @param registryKey      The key prefix for locks.
     */
    public ZookeeperLockRegistry(CuratorFramework curatorFramework, String registryKey) {
        this(curatorFramework, registryKey, DEFAULT_EXPIRE_AFTER);
    }

    /**
     * Constructs a lock registry with the supplied lock expiration.
     *
     * @param curatorFramework The connection factory.
     * @param registryKey      The key prefix for locks.
     * @param expireAfter      The expiration in milliseconds.
     */
    public ZookeeperLockRegistry(CuratorFramework curatorFramework, String registryKey, long expireAfter) {
        Assert.notNull(curatorFramework, "'curatorFramework' cannot be null");
        Assert.notNull(registryKey, "'registryKey' cannot be null");
        this.curatorFramework = curatorFramework;
        this.registryKey = registryKey;
        this.expireAfter = expireAfter;
    }

    /**
     * Set the {@link Executor}, where is not provided then a default of
     * cached thread pool Executor will be used.
     *
     * @param executor the executor service
     * @since 5.0.5
     */
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public Lock obtain(String lockKey) {
        return new ZookeeperLock(lockKey);
    }

    private final class ZookeeperLock implements Lock {

        private final String lockKey;

        private final ReentrantLock localLock = new ReentrantLock();

        private final InterProcessMutex mutex;

        private volatile long lockedAt;

        private ZookeeperLock(String path) {
            this.lockKey = constructLockKey(path);
            this.mutex = new InterProcessMutex(ZookeeperLockRegistry.this.curatorFramework, this.lockKey);
        }

        private String constructLockKey(String path) {
            return String.format(ZookeeperLockRegistry.NODE_PATH_FORMAT, ZookeeperLockRegistry.this.registryKey, path);
        }

        public long getLockedAt() {
            return this.lockedAt;
        }

        @Override
        public void lock() {
            this.localLock.lock();
            while (true) {
                try {
                    while (!obtainLock()) {
                        Thread.sleep(100); //NOSONAR
                    }
                    break;
                } catch (InterruptedException e) {
                    /*
                     * This method must be uninterruptible so catch and ignore
                     * interrupts and only break out of the while loop when
                     * we get the lock.
                     */
                } catch (Exception e) {
                    this.localLock.unlock();
                    rethrowAsLockException(e);
                }
            }
        }

        private void rethrowAsLockException(Exception e) {
            throw new CannotAcquireLockException("Failed to lock mutex at " + this.lockKey, e);
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            this.localLock.lockInterruptibly();
            try {
                while (!obtainLock()) {
                    Thread.sleep(100); //NOSONAR
                }
            } catch (InterruptedException ie) {
                this.localLock.unlock();
                Thread.currentThread().interrupt();
                throw ie;
            } catch (Exception e) {
                this.localLock.unlock();
                rethrowAsLockException(e);
            }
        }

        @Override
        public boolean tryLock() {
            try {
                return tryLock(0, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        @Override
        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            long now = System.currentTimeMillis();
            if (!this.localLock.tryLock(time, unit)) {
                return false;
            }
            try {
                long expire = now + TimeUnit.MILLISECONDS.convert(time, unit);
                boolean acquired;
                while (!(acquired = obtainLock()) && System.currentTimeMillis() < expire) { //NOSONAR
                    Thread.sleep(100); //NOSONAR
                }
                if (!acquired) {
                    this.localLock.unlock();
                }
                return acquired;
            } catch (Exception e) {
                this.localLock.unlock();
                rethrowAsLockException(e);
            }
            return false;
        }

        private boolean obtainLock() throws Exception {
            boolean result = mutex.acquire(ZookeeperLockRegistry.this.expireAfter, TimeUnit.MILLISECONDS);

            if (result) {
                this.lockedAt = System.currentTimeMillis();
            }

            return result;
        }

        @Override
        public void unlock() {
            if (!this.localLock.isHeldByCurrentThread()) {
                throw new IllegalStateException("You do not own lock at " + this.lockKey);
            }
            if (this.localLock.getHoldCount() > 1) {
                this.localLock.unlock();
                return;
            }
            try {
                if (Thread.currentThread().isInterrupted()) {
                    ZookeeperLockRegistry.this.executor.execute(this::removeLockKey);
                } else {
                    removeLockKey();
                }

                if (log.isDebugEnabled()) {
                    log.debug("Released lock; " + this);
                }
            } catch (Exception e) {
                ReflectionUtils.rethrowRuntimeException(e);
            } finally {
                this.localLock.unlock();
            }
        }

        @SneakyThrows
        private void removeLockKey() {
            mutex.release();
        }

        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException("Conditions are not supported");
        }
    }
}
