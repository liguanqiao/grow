package com.liguanqiao.grow.mq.rabbit;

import cn.hutool.core.util.ReflectUtil;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.mq.ReceiversMessageConverter;
import com.liguanqiao.grow.mq.model.MqWrap;
import com.liguanqiao.grow.mq.util.MqSerializeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * RabbitMessageConverter
 *
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Getter
@AllArgsConstructor
@SuppressWarnings("NullableProblems")
public class RabbitMessageConverter extends AbstractMessageConverter implements ReceiversMessageConverter {

    private final TracerContext tracerContext;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties) {
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        return new Message(MqSerializeUtil.valueToString().apply(object).getBytes(DEFAULT_CHARSET), messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String body = new String(message.getBody(), DEFAULT_CHARSET);
        MqWrap entity = MqSerializeUtil.castType(MqWrap.class).apply(body);
        trace(entity.getTracerSpan());
        return convertContent(entity.getContent(), message.getMessageProperties());
    }

    private Object convertContent(String content, MessageProperties properties) {
        if (Objects.nonNull(properties.getTargetMethod())) {
            return MqSerializeUtil.castType(properties.getTargetMethod().getParameterTypes()[0]).apply(content);
        }
        if (Objects.nonNull(properties.getTargetBean())) {
            Method[] methods = ReflectUtil.getPublicMethods(properties.getTargetBean().getClass());
            if (Objects.nonNull(methods)) {
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RabbitHandler.class)) {
                        return MqSerializeUtil.castType(method.getParameterTypes()[0]).apply(content);
                    }
                }
            }
        }
        return new Message(content.getBytes(StandardCharsets.UTF_8), properties);
    }

}
