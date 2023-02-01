package com.liguanqiao.grow.redis.util;

import com.liguanqiao.grow.json.JsonUtil;

import java.util.Optional;
import java.util.function.Function;

/**
 * redis serialize util
 *
 * @author liguanqiao
 **/
public class RedisSerializeUtil {

//    static <T> Optional<T> toType(String value, Class<T> type) {
//        return Optional.ofNullable(value)
//                .map(castType(type));
//    }

    public static <T> Function<String, T> castType(Class<T> type) {
        return value -> JsonUtil.toBean(value, type);
    }

    public static <T> Function<T, String> valueToString() {
        return val -> Optional.of(val)
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .orElseGet(() -> JsonUtil.toJson(val));
    }

}
