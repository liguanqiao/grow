package com.liguanqiao.grow.example.spring.cloud.config;

import com.liguanqiao.grow.example.spring.cloud.logbook.LogbookWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;
import org.zalando.logbook.Logbook;

/**
 * @author liguanqiao
 * @since 2023/5/23
 **/
@Configuration
public class LogbookConfiguration {

    @Bean
    public WebFilter logbookServerFilter(final Logbook logbook) {
        return new LogbookWebFilter(logbook);
    }
}
