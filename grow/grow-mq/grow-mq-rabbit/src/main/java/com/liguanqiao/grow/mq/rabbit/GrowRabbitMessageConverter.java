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
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * GrowRabbitMessageConverter
 *
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Getter
@AllArgsConstructor
@SuppressWarnings("NullableProblems")
public class GrowRabbitMessageConverter extends AbstractMessageConverter implements ReceiversMessageConverter {

    private final TracerContext tracerContext;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Map<String, Type> fromTypeCache = new HashMap<>();

    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties) {
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        return new Message(MqSerializeUtil.valueToString()
                .apply(Optional.of(object)
                        .filter(MqWrap.class::isInstance)
                        .orElseGet(() -> MqWrap.convert(tracerContext, object)))
                .getBytes(DEFAULT_CHARSET), messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String body = new String(message.getBody(), DEFAULT_CHARSET);
        MqWrap entity = MqSerializeUtil.castType(MqWrap.class).apply(body);
        trace(entity.getTracerSpan());
        return convertContent(entity.getContent(), message.getMessageProperties());
    }

    private Object convertContent(String content, MessageProperties properties) {
        return getFromType(properties)
                .map(MqSerializeUtil::castType)
                .map(func -> func.apply(content))
                .orElseGet(() -> new Message(content.getBytes(StandardCharsets.UTF_8), properties));
    }

    private Optional<Type> getFromType(MessageProperties properties) {
        String cacheKey = properties.getConsumerQueue();
        if (fromTypeCache.containsKey(cacheKey)) {
            return Optional.of(fromTypeCache.get(cacheKey));
        }
        if (Objects.nonNull(properties.getTargetMethod())) {
            Class<?> type = properties.getTargetMethod().getParameterTypes()[0];
            fromTypeCache.put(cacheKey, type);
            return Optional.of(type);
        }
        if (Objects.nonNull(properties.getTargetBean())) {
            Method[] methods = ReflectUtil.getPublicMethods(properties.getTargetBean().getClass());
            if (Objects.nonNull(methods)) {
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RabbitHandler.class)) {
                        Class<?> type = method.getParameterTypes()[0];
                        fromTypeCache.put(cacheKey, type);
                        return Optional.of(type);
                    }
                }
            }
        }
        return Optional.empty();
    }

}
