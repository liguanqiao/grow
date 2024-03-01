package com.liguanqiao.grow.mq.spring.boot.autoconfigure;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.util.TracerContextUtil;
import com.liguanqiao.grow.mq.MqSender;
import com.liguanqiao.grow.mq.kafka.KafkaMessageConverter;
import com.liguanqiao.grow.mq.kafka.MqSenderKafkaImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Slf4j
@Configuration
public class GrowMqAutoConfiguration {

    @Order(1)
    @ConditionalOnMissingBean
    @Bean
    public MqSender mqSender(KafkaTemplate<String, String> template, @Autowired(required = false) TracerContext tracerContext) {
        log.info(">>>>>>>>>>> Grow MqSender Kafka Config Init.");
        return new MqSenderKafkaImpl(template, TracerContextUtil.getOrDefault(tracerContext));
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaMessageConverter kafkaMessageConverter(@Autowired(required = false) TracerContext tracerContext) {
        return new KafkaMessageConverter(TracerContextUtil.getOrDefault(tracerContext));
    }

}
