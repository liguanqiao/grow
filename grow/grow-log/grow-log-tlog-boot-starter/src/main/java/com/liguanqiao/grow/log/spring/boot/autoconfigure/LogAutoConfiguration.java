package com.liguanqiao.grow.log.spring.boot.autoconfigure;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.tlog.TracerContextTLogImpl;
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

    @Order(1)
    @ConditionalOnMissingBean
    @Bean
    public TracerContext tracerContext() {
        log.info(">>>>>>>>>>> Grow TracerContext TLog Config Init.");
        return new TracerContextTLogImpl();
    }

}
