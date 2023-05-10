package com.liguanqiao.grow.example.spring.cloud.orders.conifg;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.web.common.error.ErrorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * 单体 Web 服务自动装配
 *
 * @author liguanqiao
 * @since 2023/1/14
 **/
@Configuration
public class GrowWebCloudConfiguration {
    @Bean
    public ErrorController errorController(@Value("${spring.application.name}") String appName,
                                           @Autowired(required = false) TracerContext tracerContext) {
        return Optional.ofNullable(tracerContext)
                .map(context -> new ErrorController(appName, context))
                .orElseGet(() -> new ErrorController(appName));
    }

}
