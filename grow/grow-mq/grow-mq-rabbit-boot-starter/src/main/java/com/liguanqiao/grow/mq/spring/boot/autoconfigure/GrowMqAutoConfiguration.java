package com.liguanqiao.grow.mq.spring.boot.autoconfigure;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.util.TracerContextUtil;
import com.liguanqiao.grow.mq.MqSender;
import com.liguanqiao.grow.mq.rabbit.GrowRabbitMessageConverter;
import com.liguanqiao.grow.mq.rabbit.MqSenderRabbitImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Slf4j
@Configuration
public class GrowMqAutoConfiguration {

    @Order(2)
    @ConditionalOnMissingBean
    @Bean
    public MqSender mqSender(AmqpTemplate template, @Autowired(required = false) TracerContext tracerContext) {
        log.info(">>>>>>>>>>> Grow MqSender Rabbit Config Init.");
        return new MqSenderRabbitImpl(template, TracerContextUtil.getOrDefault(tracerContext));
    }

    @Bean
    @ConditionalOnMissingBean
    public MessageConverter rabbitMessageConverter(@Autowired(required = false) TracerContext tracerContext) {
        return new GrowRabbitMessageConverter(TracerContextUtil.getOrDefault(tracerContext));
    }
}
