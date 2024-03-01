package com.liguanqiao.grow.redis.serializer;


/**
 * RedisOps序列化器
 *
 * @author liguanqiao
 * @since 2023/8/8
 **/
public interface RedisOpsSerializer {
    /**
     * 序列化
     *
     * @param <T>   数据类型
     * @param value 值
     * @return 序列化后的值
     **/
    <T> String serialize(T value);

    /**
     * 反序列化
     *
     * @param <T>   数据类型
     * @param value 值
     * @param type  类型
     * @return 反序列化后的值
     **/
    <T> T deserialize(String value, Class<T> type);
}
