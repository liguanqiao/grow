package com.liguanqiao.grow.mq.spring.boot.autoconfigure;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.util.TracerContextUtil;
import com.liguanqiao.grow.mq.MqSender;
import com.liguanqiao.grow.mq.active.GrowActiveMessageConverter;
import com.liguanqiao.grow.mq.active.MqSenderActiveImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * @author liguanqiao
 * @since 2023/6/25
 **/
@Slf4j
@Configuration
public class GrowMqAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public MqSender mqSender(JmsMessagingTemplate template, @Autowired(required = false) TracerContext tracerContext) {
        log.info(">>>>>>>>>>> Grow MqSender Active Config Init.");
        return new MqSenderActiveImpl(template, TracerContextUtil.getOrDefault(tracerContext));
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public MessageConverter activeMessageConverter(@Autowired(required = false) TracerContext tracerContext) {
//        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
//        messageConverter.setTargetType(MessageType.BYTES);
//        messageConverter.setTypeIdPropertyName("_type");
//        return messageConverter;
//    }

    @Bean
//    @ConditionalOnMissingBean
    public MessageConverter activeMessageConverter(@Autowired(required = false) TracerContext tracerContext) {
        return new GrowActiveMessageConverter(TracerContextUtil.getOrDefault(tracerContext));
    }

}
