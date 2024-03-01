package com.liguanqiao.grow.redis.spring.boot.autoconfigure;

import com.liguanqiao.grow.redis.RedisOps;
import com.liguanqiao.grow.redis.redisson.RedisOpsRedissonImpl;
import com.liguanqiao.grow.redis.serializer.DefaultRedisOpsSerializer;
import com.liguanqiao.grow.redis.serializer.RedisOpsSerializer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author liguanqiao
 * @since 2023/1/5
 **/
@Slf4j
@Configuration
public class GrowRedisAutoConfiguration {

    @Order(2)
    @ConditionalOnMissingBean
    @Bean
    public RedisOps redisOpsSpring(RedissonClient redissonClient, RedisOpsSerializer serializer) {
        log.info(">>>>>>>>>>> Grow RedisOps Redisson Config Init.");
        return new RedisOpsRedissonImpl(redissonClient, serializer);
    }

    @ConditionalOnMissingBean
    @Bean
    public RedisOpsSerializer redisOpsSerializer() {
        return new DefaultRedisOpsSerializer();
    }

}
