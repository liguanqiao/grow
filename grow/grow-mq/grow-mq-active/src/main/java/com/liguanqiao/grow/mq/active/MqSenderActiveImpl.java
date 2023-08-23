package com.liguanqiao.grow.mq.active;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.mq.MqSender;
import com.liguanqiao.grow.mq.model.MqWrap;
import lombok.AllArgsConstructor;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author liguanqiao
 * @since 2023/6/25
 **/
@AllArgsConstructor
public class MqSenderActiveImpl implements MqSender {

    public static final String SELECTOR_KEY_HEADER = "selectorKey";
    private final JmsMessagingTemplate template;
    private final TracerContext tracerContext;

    @Override
    public <T> void send(String topic, String key, T data) {
        MqWrap payload = MqWrap.convert(tracerContext, data);
        Map<String, Object> headers = new HashMap<>();
        headers.put(JmsMessagingTemplate.CONVERSION_HINT_HEADER, data.getClass().getName());
        Optional.ofNullable(key).ifPresent(val -> headers.put(SELECTOR_KEY_HEADER, val));
        template.convertAndSend(topic, payload, headers);
    }

    @Override
    public <T> void send(String topic, T data) {
        send(topic, null, data);
    }

}
