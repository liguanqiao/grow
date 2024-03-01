package com.liguanqiao.grow.redis.redisson.model;

import com.liguanqiao.grow.redis.RedisException;
import com.liguanqiao.grow.redis.model.RedisMetric;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.redisson.api.GeoUnit;

import java.util.Arrays;

/**
 * Redisson 距离单位关联
 *
 * @author liguanqiao
 **/
@Getter
@AllArgsConstructor
public enum RedisRedissonMetricRelevance {

    METERS(RedisMetric.METERS, GeoUnit.METERS),
    KILOMETERS(RedisMetric.KILOMETERS, GeoUnit.KILOMETERS),
    MILES(RedisMetric.MILES, GeoUnit.MILES),
    FEET(RedisMetric.FEET, GeoUnit.FEET);;

    private final RedisMetric redisMetric;
    private final GeoUnit redissonMetric;

    public static GeoUnit convert(RedisMetric metric) {
        return Arrays.stream(RedisRedissonMetricRelevance.values())
                .filter(e -> e.getRedisMetric().equals(metric))
                .map(RedisRedissonMetricRelevance::getRedissonMetric)
                .findFirst()
                .orElseThrow(() -> new RedisException("redis redisson metric convert error, metric: [{}]", metric));
    }

    public static RedisMetric convert(GeoUnit metric) {
        return Arrays.stream(RedisRedissonMetricRelevance.values())
                .filter(e -> e.getRedissonMetric().equals(metric))
                .map(RedisRedissonMetricRelevance::getRedisMetric)
                .findFirst()
                .orElseThrow(() -> new RedisException("redis redisson metric convert error, metric: [{}]", metric));
    }

}
