package com.liguanqiao.grow.mq.rocket;

import cn.hutool.core.util.StrUtil;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.mq.MqSender;
import com.liguanqiao.grow.mq.model.MqWrap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

import java.util.Optional;

/**
 * @author liguanqiao
 * @since 2023/6/25
 **/
@Slf4j
@AllArgsConstructor
public class MqSenderRocketImpl implements MqSender {

    private final RocketMQTemplate template;
    private final TracerContext tracerContext;

    @Override
    public <T> void send(String topic, String key, T data) {
        String destination = Optional.ofNullable(key)
                .filter(StrUtil::isNotBlank)
                .map(topic.concat(StrUtil.COLON)::concat)
                .orElse(topic);
        MqWrap payload = MqWrap.convert(tracerContext, data);
        template.convertAndSend(destination, payload);
    }

    @Override
    public <T> void send(String topic, T data) {
        send(topic, null, data);
    }

}
