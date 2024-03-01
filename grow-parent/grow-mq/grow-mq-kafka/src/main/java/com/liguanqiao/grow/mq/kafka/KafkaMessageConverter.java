package com.liguanqiao.grow.mq.kafka;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.mq.ReceiversMessageConverter;
import com.liguanqiao.grow.mq.model.MqWrap;
import com.liguanqiao.grow.mq.util.MqSerializeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.lang.reflect.Type;

/**
 * KafkaMessageConverter
 *
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Getter
@AllArgsConstructor
public class KafkaMessageConverter extends StringJsonMessageConverter implements ReceiversMessageConverter {

    private final TracerContext tracerContext;

    @SneakyThrows
    @Override
    protected Object extractAndConvertValue(ConsumerRecord<?, ?> record, Type type) {
        Object value = record.value();
        MqWrap entity = MqSerializeUtil.castType(MqWrap.class).apply(value.toString());
        trace(entity.getTracerSpan());
        return MqSerializeUtil.castType(type).apply(entity.getContent());
    }

}
