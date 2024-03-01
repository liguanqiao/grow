package com.liguanqiao.grow.lock.spring.boot.autoconfigure;

import com.liguanqiao.grow.lock.LockOps;
import com.liguanqiao.grow.lock.LockOpsRedissonImpl;
import com.liguanqiao.grow.lock.properties.LockProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Grow Log 自动装配
 *
 * @author liguanqiao
 * @since 2023/1/5
 **/
@Slf4j
@Configuration
public class GrowLockAutoConfiguration {

    @ConditionalOnMissingBean
    @ConfigurationProperties("grow.lock")
    @Bean
    public LockProperties lockProperties() {
        return new LockProperties();
    }

    @Order(2)
    @ConditionalOnMissingBean
    @Bean
    public LockOps lockOps(RedissonClient redissonClient, LockProperties lockProperties) {
        log.info(">>>>>>>>>>> Grow LockOps Redisson Config Init.");
        return new LockOpsRedissonImpl(redissonClient, lockProperties);
    }
}
