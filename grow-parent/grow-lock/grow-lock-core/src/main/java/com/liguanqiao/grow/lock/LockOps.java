package com.liguanqiao.grow.lock;

import com.liguanqiao.grow.lock.error.LockException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

/**
 * 分布式锁操作
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
public interface LockOps {

    Lock obtain(String lockKey);

    default <T> T tryLock(String lockKey, long time, TimeUnit unit, Supplier<T> action) {
        Lock lock = obtain(lockKey);
        try {
            if (lock.tryLock(time, unit)) {
                return action.get();
            } else {
                throw new LockException("try lock fail");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new LockException(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    default void tryLockWithoutResult(String lockKey, long time, TimeUnit unit, LockAction action) {
        tryLock(lockKey, time, unit, () -> {
            action.handle();
            return null;
        });
    }

    default <T> T tryLock(String lockKey, Supplier<T> action) {
        return tryLock(lockKey, 3, TimeUnit.SECONDS, action);
    }

    default void tryLockWithoutResult(String lockKey, LockAction action) {
        tryLockWithoutResult(lockKey, 3, TimeUnit.SECONDS, action);
    }

}
