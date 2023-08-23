package com.liguanqiao.grow.mq.active;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.mq.ReceiversMessageConverter;
import com.liguanqiao.grow.mq.model.MqWrap;
import com.liguanqiao.grow.mq.util.MqSerializeUtil;
import lombok.Getter;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.SmartMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * GrowActiveMessageConverter
 *
 * @author liguanqiao
 * @since 2023/1/10
 **/
@SuppressWarnings("NullableProblems")
public class GrowActiveMessageConverter implements SmartMessageConverter, BeanClassLoaderAware, ReceiversMessageConverter {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Map<String, Class<?>> fromTypeCache = new HashMap<>();
    @Getter
    private final TracerContext tracerContext;
    @Nullable
    private ClassLoader beanClassLoader;

    public GrowActiveMessageConverter(TracerContext tracerContext) {
        this.tracerContext = tracerContext;
    }

    @Override
    public Message toMessage(Object object, Session session, Object conversionHint) throws JMSException, MessageConversionException {
        return toMessage(object, session);
    }

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        BytesMessage bytesMessage = session.createBytesMessage();

        byte[] bytes = MqSerializeUtil.valueToString()
                .apply(Optional.of(object)
                        .filter(MqWrap.class::isInstance)
                        .orElseGet(() -> MqWrap.convert(tracerContext, object)))
                .getBytes(DEFAULT_CHARSET);
        bytesMessage.writeBytes(bytes);

        return bytesMessage;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        if (message instanceof BytesMessage) {
            return convertFromBytesMessage((BytesMessage) message);
        } else {
            return convertFromMessage(message);
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    protected Object convertFromBytesMessage(BytesMessage message)
            throws JMSException {
        byte[] bytes = new byte[(int) message.getBodyLength()];
        message.readBytes(bytes);

        String body = new String(bytes, DEFAULT_CHARSET);
        MqWrap entity = MqSerializeUtil.castType(MqWrap.class).apply(body);
        trace(entity.getTracerSpan());
        return MqSerializeUtil.castType(getTypeForMessage(message.getStringProperty(JmsMessagingTemplate.CONVERSION_HINT_HEADER))).apply(entity.getContent());
    }

    protected Type getTypeForMessage(String conversionHint) {
        Class<?> mappedClass = fromTypeCache.get(conversionHint);
        if (mappedClass != null) {
            return mappedClass;
        }
        try {
            Class<?> typeClass = ClassUtils.forName(conversionHint, beanClassLoader);
            fromTypeCache.put(conversionHint, typeClass);
            return typeClass;
        } catch (Throwable ex) {
            throw new MessageConversionException("Failed to resolve contentType id [" + conversionHint + "]", ex);
        }
    }

    protected Object convertFromMessage(Message message) {
        throw new IllegalArgumentException("Unsupported message type [" + message.getClass() +
                "]. MappingJacksonMessageConverter by default only supports BytesMessages.");
    }

}
