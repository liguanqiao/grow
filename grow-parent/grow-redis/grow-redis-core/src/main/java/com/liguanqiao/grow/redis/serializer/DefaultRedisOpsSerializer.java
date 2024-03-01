package com.liguanqiao.grow.redis.serializer;

import com.liguanqiao.grow.json.JsonUtil;

/**
 * RedisOps默认序列化器
 * <p>默认使用json方式</p>
 *
 * @author liguanqiao
 * @since 2023/8/8
 **/
public class DefaultRedisOpsSerializer implements RedisOpsSerializer {

    @Override
    public <T> String serialize(T value) {
        if (value instanceof String) {
            return (String) value;
        }
        return JsonUtil.toJson(value);
    }

    @Override
    public <T> T deserialize(String value, Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return (T) value;
        }
        return JsonUtil.toBean(value, type);
    }

}
