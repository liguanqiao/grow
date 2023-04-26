package com.liguanqiao.grow.delay.redisson.spring.boot.autoconfigure;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.delay.redisson.DelayTaskOpsRedissonImpl;
import com.liguanqiao.grow.delay.redisson.config.RedissonDelayQueueRegistry;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.context.TracerContextDefaultImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

;

/**
 * @author liguanqiao
 * @since 2023/4/14
 **/
@Slf4j
@Configuration
public class GrowDelayAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DelayTaskOps delayTaskOps(RedissonClient redissonClient, @Autowired(required = false) TracerContext tracerContext, List<DelayTaskHandler<?>> handlers) {
        log.info(">>>>>>>>>>> Grow DelayTask Redisson Config Init.");
        TracerContext tracerContext1 = Optional.ofNullable(tracerContext).orElseGet(TracerContextDefaultImpl::new);
        RedissonDelayQueueRegistry registry = new RedissonDelayQueueRegistry(redissonClient, handlers);
        DelayTaskOpsRedissonImpl delayTaskOps = new DelayTaskOpsRedissonImpl(registry, tracerContext1);
        registry.registerAllListener(delayTaskOps, tracerContext1);
        return delayTaskOps;
    }

}
