package com.liguanqiao.grow.delay.rabbitmq.util;

import lombok.experimental.UtilityClass;
import org.springframework.amqp.core.MessageProperties;

/**
 * MessagePropertiesUtil
 *
 * @author liguanqiao
 * @since 2023/4/14
 **/
@UtilityClass
public class DelayTaskMessagePropertiesUtil {

    private static final String HEADER_RETRY = "grow-retry";

    public static void setRetry(MessageProperties properties, int retry) {
        properties.setHeader(HEADER_RETRY, retry);
    }

    public static Integer getRetry(MessageProperties properties) {
        return properties.<Integer>getHeader(HEADER_RETRY);
    }

}
