package com.liguanqiao.grow.mq.kafka;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.mq.MqSender;
import com.liguanqiao.grow.mq.model.MqWrap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Slf4j
@AllArgsConstructor
public class MqSenderKafkaImpl implements MqSender {

    private final KafkaTemplate<String, String> template;
    private final TracerContext tracerContext;

    @Override
    public <T> void send(String topic, String key, T data) {
        String value = MqWrap.genMsgValue(tracerContext, data);
        template.send(topic, key, value)
                .addCallback(
                        successCallback -> log.debug("Message [topic:{}, key:{}, value:{}] sent successfully :)", topic, key, data),
                        failureCallback -> {
                            log.warn("Message [topic:{}, key:{}, value:{}] sending failed :(", topic, key, data);
                            log.warn(failureCallback.getMessage(), failureCallback);
                        });
    }

    @Override
    public <T> void send(String topic, T data) {
        String value = MqWrap.genMsgValue(tracerContext, data);
        template.send(topic, value)
                .addCallback(
                        successCallback -> log.debug("Message [topic:{}, value:{}] sent successfully :)", topic, data),
                        failureCallback -> {
                            log.warn("Message [topic:{}, value:{}] sending failed :(", topic, data);
                            log.warn(failureCallback.getMessage(), failureCallback);
                        });
    }
}
