package com.liguanqiao.grow.delay.rabbitmq.spring.boot.autoconfigure;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.delay.rabbitmq.DelayTaskOpsRabbitmqImpl;
import com.liguanqiao.grow.delay.rabbitmq.config.DelayTaskHandlerEndpointRegister;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.context.TracerContextDefaultImpl;
import com.liguanqiao.grow.mq.rabbit.RabbitMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Optional;

;

/**
 * @author liguanqiao
 * @since 2023/4/14
 **/
@Slf4j
@Configuration
public class GrowDelayAutoConfiguration {

    @Order(1)
    @ConditionalOnMissingBean
    @Bean
    public DelayTaskOps delayTaskOps(RabbitTemplate template, @Autowired(required = false) TracerContext tracerContext) {
        log.info(">>>>>>>>>>> Grow DelayTask RabbitMQ Config Init.");
        return new DelayTaskOpsRabbitmqImpl(template, Optional.ofNullable(tracerContext).orElseGet(TracerContextDefaultImpl::new));
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageConverter rabbitMessageConverter(@Autowired(required = false) TracerContext tracerContext) {
        return new RabbitMessageConverter(Optional.ofNullable(tracerContext).orElseGet(TracerContextDefaultImpl::new));
    }

    @Bean
    @ConditionalOnMissingBean
    public DelayTaskHandlerEndpointRegister delayTaskHandlerEndpointRegister(List<DelayTaskHandler<?>> handlers,
                                                                             ConnectionFactory connectionFactory,
                                                                             DelayTaskOps delayTaskOps,
                                                                             MessageConverter messageConverter) {
        DelayTaskHandlerEndpointRegister register = new DelayTaskHandlerEndpointRegister(handlers);
        register.registerAllEndpoints(connectionFactory, delayTaskOps, messageConverter);
        return register;
    }

}
