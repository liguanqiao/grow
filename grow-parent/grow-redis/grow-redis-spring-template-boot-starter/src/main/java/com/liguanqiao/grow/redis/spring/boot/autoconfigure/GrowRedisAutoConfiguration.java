package com.liguanqiao.grow.redis.spring.boot.autoconfigure;

import com.liguanqiao.grow.redis.RedisOps;
import com.liguanqiao.grow.redis.serializer.DefaultRedisOpsSerializer;
import com.liguanqiao.grow.redis.serializer.RedisOpsSerializer;
import com.liguanqiao.grow.redis.spring.RedisOpsSpringImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author liguanqiao
 * @since 2023/1/5
 **/
@Slf4j
@Configuration
public class GrowRedisAutoConfiguration {

    @Order(1)
    @ConditionalOnMissingBean
    @Bean
    public RedisOps redisOpsSpring(StringRedisTemplate template, RedisOpsSerializer serializer) {
        log.info(">>>>>>>>>>> Grow RedisOps SpringTemplate Config Init.");
        return new RedisOpsSpringImpl(template, serializer);
    }

    @ConditionalOnMissingBean
    @Bean
    public RedisOpsSerializer redisOpsSerializer() {
        return new DefaultRedisOpsSerializer();
    }

}
