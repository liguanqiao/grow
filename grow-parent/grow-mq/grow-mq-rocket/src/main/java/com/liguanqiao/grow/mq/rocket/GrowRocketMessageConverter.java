package com.liguanqiao.grow.mq.rocket;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.mq.ReceiversMessageConverter;
import com.liguanqiao.grow.mq.model.MqWrap;
import com.liguanqiao.grow.mq.util.MqSerializeUtil;
import lombok.Getter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * GrowRocketMessageConverter
 *
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Getter
@SuppressWarnings("NullableProblems")
public class GrowRocketMessageConverter extends MappingJackson2MessageConverter implements ReceiversMessageConverter {

    private final TracerContext tracerContext;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public GrowRocketMessageConverter(TracerContext tracerContext) {
        super();
        this.tracerContext = tracerContext;
    }

    @Override
    protected Object convertFromInternal(Message<?> message, Class<?> targetClass, Object conversionHint) {
        MqWrap entity = Optional.of(message.getPayload())
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(MqSerializeUtil.castType(MqWrap.class))
                .orElseGet(() -> MqWrap.convert(tracerContext, message.getPayload()));
        trace(entity.getTracerSpan());
        return MqSerializeUtil.castType(targetClass).apply(entity.getContent());
    }

    @Override
    protected Object convertToInternal(Object payload, MessageHeaders messageHeaders, Object conversionHint) {
        return MqSerializeUtil.valueToString()
                .apply(Optional.of(payload)
                        .filter(MqWrap.class::isInstance)
                        .orElseGet(() -> MqWrap.convert(tracerContext, payload)))
                .getBytes(DEFAULT_CHARSET);
    }

}
