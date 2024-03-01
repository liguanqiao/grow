package com.liguanqiao.grow.delay.redisson.spring.boot.autoconfigure;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.delay.redisson.DelayTaskOpsRedissonImpl;
import com.liguanqiao.grow.delay.redisson.config.RedissonDelayQueueRegistry;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.util.TracerContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author liguanqiao
 * @since 2023/4/14
 **/
@Slf4j
@Configuration
public class GrowDelayAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DelayTaskOps delayTaskOps(RedissonClient redissonClient, List<DelayTaskHandler<?>> handlers, @Autowired(required = false) TracerContext tracerContext) {
        log.info(">>>>>>>>>>> Grow DelayTask Redisson Config Init.");
        TracerContext tc = TracerContextUtil.getOrDefault(tracerContext);
        RedissonDelayQueueRegistry registry = new RedissonDelayQueueRegistry(redissonClient, handlers);
        DelayTaskOpsRedissonImpl delayTaskOps = new DelayTaskOpsRedissonImpl(registry, tc);
        registry.registerAllListener(delayTaskOps, tc);
        return delayTaskOps;
    }

}
