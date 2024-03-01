package com.liguanqiao.grow.delay.redisson;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.TaskInfo;
import com.liguanqiao.grow.delay.error.DelayTaskException;
import com.liguanqiao.grow.delay.redisson.config.DelayQueueRegistryInfo;
import com.liguanqiao.grow.delay.redisson.config.RedissonDelayQueueRegistry;
import com.liguanqiao.grow.log.context.TracerContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author liguanqiao
 * @since 2023/3/30
 **/
@Slf4j
@AllArgsConstructor
public class DelayTaskOpsRedissonImpl implements DelayTaskOps {

    private final RedissonDelayQueueRegistry delayQueueRegistry;
    private final TracerContext tracerContext;

    @Override
    public <T> void add(String topic, TaskInfo<T> task, long time, TimeUnit timeUnit) {
        log.debug("delay task add, topic: [{}], taskInfo: [{}]", topic, task);
        DelayQueueRegistryInfo registryInfo = checkAndGetRegistryInfo(topic);
        registryInfo.setTaskInfo(task, tracerContext.nextSpan());
        registryInfo.getDelayedQueue().offer(task.getId(), time, timeUnit);
    }

    @Override
    public boolean del(String topic, String taskId) {
        log.debug("delay task delete, topic: [{}], taskId: [{}]", topic, taskId);
        DelayQueueRegistryInfo registryInfo = checkAndGetRegistryInfo(topic);
        boolean result = registryInfo.getDelayedQueue().remove(taskId);
        registryInfo.removeTaskInfo(taskId);
        return result;
    }

    private DelayQueueRegistryInfo checkAndGetRegistryInfo(String topic) {
        return Optional.ofNullable(delayQueueRegistry.getRegistryInfo(topic))
                .orElseThrow(() -> new DelayTaskException("queue not registered"));
    }

}
