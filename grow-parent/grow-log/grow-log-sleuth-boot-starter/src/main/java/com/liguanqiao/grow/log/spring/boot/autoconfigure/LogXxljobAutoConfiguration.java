package com.liguanqiao.grow.log.spring.boot.autoconfigure;

import brave.Tracer;
import com.liguanqiao.grow.log.sleuth.task.LogTaskXxlJobAop;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXL-Job 自动装配
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
@Configuration
@ConditionalOnClass(name = {"com.xxl.job.core.server.EmbedServer"})
public class LogXxljobAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LogTaskXxlJobAop logTaskXxlJobAop(Tracer tracer) {
        return new LogTaskXxlJobAop(tracer);
    }

}
