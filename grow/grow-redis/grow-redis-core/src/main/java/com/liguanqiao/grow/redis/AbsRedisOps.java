package com.liguanqiao.grow.redis;

import com.liguanqiao.grow.redis.serializer.RedisOpsSerializer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基础 Redis 操作接口
 *
 * @author liguanqiao
 * @since 2023/8/8
 **/
public abstract class AbsRedisOps {

    protected abstract RedisOpsSerializer getSerializer();

    protected <T> String serialize(T value) {
        return getSerializer().serialize(value);
    }

    protected <T> List<String> serialize2List(Collection<T> values) {
        return values.stream().map(getSerializer()::serialize).collect(Collectors.toList());
    }

    protected <T> List<String> serialize2List(T[] values) {
        return Arrays.stream(values).map(getSerializer()::serialize).collect(Collectors.toList());
    }

    protected <T> String[] serialize2Array(Collection<T> values) {
        return values.stream().map(getSerializer()::serialize).toArray(String[]::new);
    }

    protected <T> String[] serialize2Array(T[] values) {
        return Arrays.stream(values).map(getSerializer()::serialize).toArray(String[]::new);
    }

    protected <T> T deserialize(String value, Class<T> type) {
        return getSerializer().deserialize(value, type);
    }

    protected <T> List<T> deserialize2List(Collection<String> values, Class<T> type) {
        return values.stream().map(val -> getSerializer().deserialize(val, type)).collect(Collectors.toList());
    }

    protected <T> List<T> deserialize2List(String[] values, Class<T> type) {
        return Arrays.stream(values).map(val -> getSerializer().deserialize(val, type)).collect(Collectors.toList());
    }

    protected <T> List<T> deserialize2Array(Collection<String> values, Class<T> type) {
        return values.stream().map(val -> getSerializer().deserialize(val, type)).collect(Collectors.toList());
    }

    protected <T> List<T> deserialize2Array(String[] values, Class<T> type) {
        return Arrays.stream(values).map(val -> getSerializer().deserialize(val, type)).collect(Collectors.toList());
    }

    protected <T> Set<T> deserialize2Set(Collection<String> values, Class<T> type) {
        return values.stream().map(val -> getSerializer().deserialize(val, type)).collect(Collectors.toSet());
    }

    protected <T> Set<T> deserialize2Set(String[] values, Class<T> type) {
        return Arrays.stream(values).map(val -> getSerializer().deserialize(val, type)).collect(Collectors.toSet());
    }

}
