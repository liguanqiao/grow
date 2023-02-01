package com.liguanqiao.grow.mq.rabbit;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.mq.MqSender;
import com.liguanqiao.grow.mq.model.MqWrap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;

/**
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Slf4j
@AllArgsConstructor
public class MqSenderRabbitImpl implements MqSender {

    private final AmqpTemplate template;
    private final TracerContext tracerContext;

    @Override
    public <T> void send(String topic, String key, T data) {
        MqWrap value = MqWrap.convert(tracerContext, data);
        template.convertAndSend(topic, key, value);
    }

    @Override
    public <T> void send(String topic, T data) {
        MqWrap value = MqWrap.convert(tracerContext, data);
        template.convertAndSend(topic, value);
    }

}
