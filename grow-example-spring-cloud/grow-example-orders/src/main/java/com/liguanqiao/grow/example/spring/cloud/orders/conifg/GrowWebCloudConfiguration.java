package com.liguanqiao.grow.example.spring.cloud.orders.conifg;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.web.common.error.ErrorController;
import com.yomahub.tlog.springboot.TLogWebAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.servlet.LogbookFilter;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import java.util.Optional;

/**
 * 单体 Web 服务自动装配
 *
 * @author liguanqiao
 * @since 2023/1/14
 **/
@Configuration
@EnableAutoConfiguration(exclude = {TLogWebAutoConfiguration.class})
public class GrowWebCloudConfiguration {
    @Bean
    public ErrorController errorController(@Value("${spring.application.name}") String appName,
                                           @Autowired(required = false) TracerContext tracerContext) {
        return Optional.ofNullable(tracerContext)
                .map(context -> new ErrorController(appName, context))
                .orElseGet(() -> new ErrorController(appName));
    }


//    @Configuration(proxyBeanMethods = false)
//    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//    @ConditionalOnClass({
//            Servlet.class,
//            LogbookFilter.class
//    })
//    @EnableAutoConfiguration(exclude = {TLogWebAutoConfiguration.class})
//    static class ServletFilterConfiguration {
//
//        private static final String FILTER_NAME = "tLogFilter";
//
//        @Bean
//        @ConditionalOnProperty(name = "logbook.filter.enabled", havingValue = "true", matchIfMissing = true)
//        @ConditionalOnMissingBean(name = FILTER_NAME)
//        public FilterRegistrationBean<?> tLogFilter(final Logbook logbook) {
//            final TLogServletFilter2 filter = new TLogServletFilter2();
//            return newFilter(filter, FILTER_NAME, Ordered.HIGHEST_PRECEDENCE);
//        }
//
//        static FilterRegistrationBean<?> newFilter(final Filter filter, final String filterName, final int order) {
//            @SuppressWarnings("unchecked") // as of Spring Boot 2.x
//            final FilterRegistrationBean<?> registration = new FilterRegistrationBean<>(filter);
//            registration.setName(filterName);
//            registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
//            registration.setOrder(order);
//            return registration;
//        }
//
//    }

}
