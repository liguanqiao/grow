package com.liguanqiao.grow.mq.util;

import com.liguanqiao.grow.json.JsonTypeReference;
import com.liguanqiao.grow.json.JsonUtil;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Function;

/**
 * mq serialize util
 *
 * @author liguanqiao
 **/
public class MqSerializeUtil {

    public static <T> Function<String, T> castType(Class<T> type) {
        return value -> JsonUtil.toBean(value, type);
    }

    public static <T> Function<String, T> castType(Type type) {
        return value -> JsonUtil.toBean(value, type);
    }

    public static <T> Function<T, String> valueToString() {
        return val -> Optional.of(val)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElseGet(() -> JsonUtil.toJson(val));
    }

}
