package com.liguanqiao.grow.mq.rocket;

import com.liguanqiao.grow.log.context.TracerContext;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.messaging.converter.CompositeMessageConverter;

/**
 * @author liguanqiao
 * @since 2023/6/27
 **/
public class GrowRocketMQMessageConverter extends RocketMQMessageConverter {

    public GrowRocketMQMessageConverter(TracerContext tracerContext) {
        super();
        ((CompositeMessageConverter) getMessageConverter()).getConverters().add(2, new GrowRocketMessageConverter(tracerContext));
    }

}
