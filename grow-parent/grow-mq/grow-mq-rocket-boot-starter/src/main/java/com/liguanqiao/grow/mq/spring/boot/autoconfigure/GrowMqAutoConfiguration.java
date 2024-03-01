package com.liguanqiao.grow.mq.spring.boot.autoconfigure;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.util.TracerContextUtil;
import com.liguanqiao.grow.mq.MqSender;
import com.liguanqiao.grow.mq.rocket.GrowRocketMQMessageConverter;
import com.liguanqiao.grow.mq.rocket.MqSenderRocketImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
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

    @ConditionalOnMissingBean
    @Bean
    public MqSender mqSender(RocketMQTemplate template, @Autowired(required = false) TracerContext tracerContext) {
        log.info(">>>>>>>>>>> Grow MqSender Rocket Config Init.");
        return new MqSenderRocketImpl(template, TracerContextUtil.getOrDefault(tracerContext));
    }

//    @Order(-99)
//    @Bean
//    @ConditionalOnMissingBean
//    public RocketMQMessageConverter rocketMessageConverter(@Autowired(required = false) TracerContext tracerContext) {
//        GrowRocketMessageConverter messageConverter = new GrowRocketMessageConverter(TracerContextUtil.getOrDefault(tracerContext));
//        return new RocketMQMessageConverter() {
//            @Override
//            public MessageConverter getMessageConverter() {
//                return messageConverter;
//            }
//        };
//    }

    @Order(-99)
    @Bean
    @ConditionalOnMissingBean
    public RocketMQMessageConverter rocketMQMessageConverter(@Autowired(required = false) TracerContext tracerContext) {
        return new GrowRocketMQMessageConverter(TracerContextUtil.getOrDefault(tracerContext));
    }

}
