package com.liguanqiao.grow.log.spring.boot.autoconfigure;

import brave.Tracer;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.sleuth.TracerContextSleuthImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Grow Log 自动装配
 *
 * @author liguanqiao
 * @since 2023/1/11
 **/
@Slf4j
@Configuration
public class LogAutoConfiguration {

    @Order(2)
    @ConditionalOnMissingBean
    @Bean
    public TracerContext tracerContext(Tracer tracer) {
        log.info(">>>>>>>>>>> Grow TracerContext Sleuth Config Init.");
        return new TracerContextSleuthImpl(tracer);
    }

}
