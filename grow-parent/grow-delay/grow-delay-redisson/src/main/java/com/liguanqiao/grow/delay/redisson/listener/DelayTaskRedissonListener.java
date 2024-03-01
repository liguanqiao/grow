package com.liguanqiao.grow.delay.redisson.listener;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.TaskInfo;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.delay.redisson.config.DelayQueueRegistryInfo;
import com.liguanqiao.grow.log.context.TracerContext;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.client.RedisException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@SuppressWarnings({"rawtypes", "unchecked"})
public class DelayTaskRedissonListener extends AbsDelayTaskListener {

    public DelayTaskRedissonListener(DelayTaskOps delayTaskOps, TracerContext tracerContext, DelayQueueRegistryInfo registryInfo) {
        super(delayTaskOps, tracerContext, registryInfo);
    }

    private AsyncMessageProcessingConsumer takeMessageTask;

    @Override
    protected void doStart() {
        this.takeMessageTask = new AsyncMessageProcessingConsumer();
        this.getTaskExecutor().execute(this.takeMessageTask);
    }

    @Override
    protected void doStop() {
        this.takeMessageTask.stop();
    }

    private final class AsyncMessageProcessingConsumer implements Runnable {

        private volatile Thread currentThread = null;

        private volatile ConsumerStatus status = ConsumerStatus.CREATED;

        @Override
        public void run() {
            if (this.status != ConsumerStatus.CREATED) {
                log.info("consumer currentThread [{}] will exit, because consumer status is {},expected is CREATED", this.currentThread.getName(), this.status);
                return;
            }

            final RBlockingQueue<String> blockingQueue = DelayTaskRedissonListener.this.getRegistryInfo().getBlockingQueue();
            if (blockingQueue == null) {
                log.error("error occurred while create blockingQueue for queue [{}]", DelayTaskRedissonListener.this.getRegistryInfo().getDelayTaskHandler().getTopic());
                return;
            }
            this.currentThread = Thread.currentThread();
            this.status = ConsumerStatus.RUNNING;

            for (; ; ) {
                TaskInfo taskInfo;
                try {
                    String taskId = blockingQueue.take();
                    taskInfo = DelayTaskRedissonListener.this.getRegistryInfo().getTaskInfo(taskId);
                } catch (Exception e) {
                    log.error("error occurred while take message from redisson", e);
                    continue;
                }
                doTracer(taskInfo.getId());
                doExecute(taskInfo);
                if (this.status == ConsumerStatus.STOPPED) {
                    log.info("consumer currentThread [{}] will exit, because of STOPPED status", this.currentThread.getName());
                    break;
                }
            }
            this.currentThread = null;
        }

        private void stop() {
            if (this.currentThread != null) {
                this.status = ConsumerStatus.STOPPED;
                this.currentThread.interrupt();
            }
        }

        private void doTracer(String taskId) {
            Optional.ofNullable(DelayTaskRedissonListener.this.getRegistryInfo().getTracer(taskId))
                    .ifPresent(DelayTaskRedissonListener.this.getTracerContext()::joinSpan);
        }

        private void doExecute(TaskInfo taskInfo) {
            DelayTaskHandler<?> handler = DelayTaskRedissonListener.this.getRegistryInfo().getDelayTaskHandler();
            try {
                handler.execute(taskInfo);
            } catch (Exception ex) {
                if (taskInfo.getRetry() <= handler.getRetry()) {
                    log.error("retry delay task, topic:{}, task:{}", handler.getTopic(), taskInfo, ex);
                    //任务延迟10秒重试
                    DelayTaskRedissonListener.this.getDelayTaskOps().add(handler.getTopic(), taskInfo, 10, TimeUnit.SECONDS);
                } else {
                    log.error("delay task process element error, topic:{}, task:{}", handler.getTopic(), taskInfo, ex);
                    handler.error(taskInfo, ex);
                }
            } finally {
                DelayTaskRedissonListener.this.getRegistryInfo().removeTaskInfo(taskInfo.getId());
            }
        }
    }

}
