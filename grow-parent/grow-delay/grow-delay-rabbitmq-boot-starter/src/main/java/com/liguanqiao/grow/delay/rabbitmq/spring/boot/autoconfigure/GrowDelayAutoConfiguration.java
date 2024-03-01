package com.liguanqiao.grow.delay.rabbitmq.spring.boot.autoconfigure;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.delay.rabbitmq.DelayTaskOpsRabbitmqImpl;
import com.liguanqiao.grow.delay.rabbitmq.config.DelayTaskHandlerEndpointRegister;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.context.TracerContextDefaultImpl;
import com.liguanqiao.grow.log.util.TracerContextUtil;
import com.liguanqiao.grow.mq.rabbit.GrowRabbitMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

/**
 * @author liguanqiao
 * @since 2023/4/14
 **/
@Slf4j
@Configuration
public class GrowDelayAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DelayTaskOps delayTaskOps(RabbitTemplate rabbitTemplate, @Autowired(required = false) TracerContext tracerContext) {
        log.info(">>>>>>>>>>> Grow DelayTask RabbitMQ Config Init.");
        TracerContext tc = TracerContextUtil.getOrDefault(tracerContext);
        return new DelayTaskOpsRabbitmqImpl(
                Optional.of(rabbitTemplate)
                        .filter(template -> GrowRabbitMessageConverter.class.isAssignableFrom(template.getMessageConverter().getClass()))
                        .orElseGet(() -> createRabbitTemplate(rabbitTemplate.getConnectionFactory(), createRabbitMessageConverter(tc))),
                tc
        );
    }

    @Bean
    public DelayTaskHandlerEndpointRegister delayTaskHandlerEndpointRegister(List<DelayTaskHandler<?>> handlers,
                                                                             ConnectionFactory connectionFactory,
                                                                             DelayTaskOps delayTaskOps,
                                                                             @Autowired(required = false) GrowRabbitMessageConverter messageConverter,
                                                                             @Autowired(required = false) TracerContext tracerContext) {
        DelayTaskHandlerEndpointRegister register = new DelayTaskHandlerEndpointRegister(handlers);
        register.registerAllEndpoints(connectionFactory, delayTaskOps, Optional.ofNullable(messageConverter)
                .orElseGet(() -> createRabbitMessageConverter(tracerContext)));
        return register;
    }

    private RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory, GrowRabbitMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    private GrowRabbitMessageConverter createRabbitMessageConverter(TracerContext tracerContext) {
        return new GrowRabbitMessageConverter(TracerContextUtil.getOrDefault(tracerContext));
    }

}
