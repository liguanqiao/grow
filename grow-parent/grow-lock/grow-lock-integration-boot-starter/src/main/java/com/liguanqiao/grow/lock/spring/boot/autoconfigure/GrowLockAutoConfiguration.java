package com.liguanqiao.grow.lock.spring.boot.autoconfigure;

import com.liguanqiao.grow.lock.LockOps;
import com.liguanqiao.grow.lock.LockOpsIntegrationImpl;
import com.liguanqiao.grow.lock.properties.LockProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

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
    public LockProperties lockProperties(){
        return new LockProperties();
    }

    @ConditionalOnMissingBean
    @Bean
    public LockRegistry lockRegistry(RedisConnectionFactory connectionFactory,LockProperties lockProperties) {
        return new RedisLockRegistry(connectionFactory, lockProperties.getLockKeyPrefix());
    }

    @Order(1)
    @ConditionalOnMissingBean
    @Bean
    public LockOps lockOps(LockRegistry lockRegistry) {
        log.info(">>>>>>>>>>> Grow LockOps Integration Config Init.");
        return new LockOpsIntegrationImpl(lockRegistry);
    }

}
