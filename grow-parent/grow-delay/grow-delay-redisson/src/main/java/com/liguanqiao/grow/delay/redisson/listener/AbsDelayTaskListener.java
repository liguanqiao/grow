package com.liguanqiao.grow.delay.redisson.listener;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.redisson.config.DelayQueueRegistryInfo;
import com.liguanqiao.grow.delay.redisson.support.ThreadFactoryCreator;
import com.liguanqiao.grow.log.context.TracerContext;
import lombok.Getter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class AbsDelayTaskListener {

    private final Object lifecycleMonitor = new Object();

    @Getter
    private final Executor taskExecutor = Executors.newSingleThreadExecutor(ThreadFactoryCreator.create("RedissonConsumeThread"));
    @Getter
    private final DelayTaskOps delayTaskOps;
    @Getter
    private final TracerContext tracerContext;
    @Getter
    private final DelayQueueRegistryInfo registryInfo;

    private volatile boolean running = false;

    public AbsDelayTaskListener(DelayTaskOps delayTaskOps, TracerContext tracerContext, DelayQueueRegistryInfo registryInfo) {
        this.delayTaskOps = delayTaskOps;
        this.tracerContext = tracerContext;
        this.registryInfo = registryInfo;
    }

    public void start() {
        if (isRunning()) {
            return;
        }
        synchronized (this.lifecycleMonitor) {
            this.doStart();
            this.running = true;
        }
    }

    public void stop() {
        if (!isRunning()) {
            return;
        }
        synchronized (this.lifecycleMonitor) {
            this.doStop();
            this.running = false;
        }
    }

    public boolean isRunning() {
        return this.running;
    }

    /**
     * do start
     */
    protected abstract void doStart();

    /**
     * do stop
     */
    protected abstract void doStop();


    protected enum ConsumerStatus {
        /**
         * created
         */
        CREATED,
        /**
         * running
         */
        RUNNING,
        /**
         * stopped
         */
        STOPPED
    }

}
