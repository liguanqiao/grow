package com.liguanqiao.grow.delay.redisson.config;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.delay.redisson.listener.DelayTaskRedissonListener;
import com.liguanqiao.grow.log.context.TracerContext;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liguanqiao
 * @since 2023/4/25
 **/
public class RedissonDelayQueueRegistry {

    private final Map<String, DelayQueueRegistryInfo> registryInfoContainer = new ConcurrentHashMap<>();

    public RedissonDelayQueueRegistry(RedissonClient redissonClient, List<DelayTaskHandler<?>> handlers) {
        handlers.forEach(handler -> registryInfoContainer.put(handler.getTopic(), new DelayQueueRegistryInfo(redissonClient, handler)));
    }

    public DelayQueueRegistryInfo getRegistryInfo(String topic) {
        return registryInfoContainer.get(topic);
    }

    public void registerAllListener(DelayTaskOps delayTaskOps, TracerContext tracerContext) {
        registryInfoContainer.forEach((topic, registryInfo) -> createMessageListener(delayTaskOps, tracerContext, registryInfo));
    }

    private void createMessageListener(DelayTaskOps delayTaskOps, TracerContext tracerContext, DelayQueueRegistryInfo registryInfo) {
        DelayTaskRedissonListener container = new DelayTaskRedissonListener(delayTaskOps, tracerContext, registryInfo);
        container.start();
    }

}
