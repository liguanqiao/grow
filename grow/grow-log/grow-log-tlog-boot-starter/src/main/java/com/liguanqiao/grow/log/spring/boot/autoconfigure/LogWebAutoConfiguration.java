package com.liguanqiao.grow.log.spring.boot.autoconfigure;

import com.yomahub.tlog.web.filter.TLogServletFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * TLog Filter 自动装配
 *
 * @author liguanqiao
 * @since 2023/6/25
 **/
@Configuration
@ConditionalOnClass(name = {"org.springframework.boot.web.servlet.FilterRegistrationBean", "javax.servlet.Filter"})
public class LogWebAutoConfiguration {

    @Bean
    public FilterRegistrationBean<TLogServletFilter> filterRegistration() {
        FilterRegistrationBean<TLogServletFilter> registration = new FilterRegistrationBean<>();
        // 设置自定义拦截器
        registration.setFilter(new TLogServletFilter());
        // 设置拦截路径
        registration.addUrlPatterns("/*");
        // 设置优先级（保证tlog过滤器最先执行）
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

}
