package com.liguanqiao.grow.redis.serializer;

import com.sun.istack.internal.Nullable;

/**
 * RedisOps序列化器
 *
 * @author liguanqiao
 * @since 2023/8/8
 **/
public interface RedisOpsSerializer {
    /**
     * 序列化
     **/
    <T> String serialize(@Nullable T value);

    /**
     * 反序列化
     **/
    <T> T deserialize(@Nullable String value, @Nullable Class<T> type);
}
