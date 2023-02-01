package com.liguanqiao.grow.redis.spring.model;

import com.liguanqiao.grow.redis.RedisException;
import com.liguanqiao.grow.redis.model.RedisMetric;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.Arrays;

/**
 * Redisson 距离单位关联
 *
 * @author liguanqiao
 **/
@Getter
@AllArgsConstructor
public enum RedisSpringMetricRelevance {

    METERS(RedisMetric.METERS, RedisGeoCommands.DistanceUnit.METERS),
    KILOMETERS(RedisMetric.KILOMETERS, RedisGeoCommands.DistanceUnit.KILOMETERS),
    MILES(RedisMetric.MILES, RedisGeoCommands.DistanceUnit.MILES),
    FEET(RedisMetric.FEET, RedisGeoCommands.DistanceUnit.FEET);;

    private final RedisMetric redisMetric;
    private final RedisGeoCommands.DistanceUnit springMetric;

    public static RedisGeoCommands.DistanceUnit convert(RedisMetric metric) {
        return Arrays.stream(RedisSpringMetricRelevance.values())
                .filter(e -> e.getRedisMetric().equals(metric))
                .map(RedisSpringMetricRelevance::getSpringMetric)
                .findFirst()
                .orElseThrow(() -> new RedisException("redis spring metric convert error, metric: [{}]", metric));
    }

    public static RedisMetric convert(RedisGeoCommands.DistanceUnit metric) {
        return Arrays.stream(RedisSpringMetricRelevance.values())
                .filter(e -> e.getSpringMetric().equals(metric))
                .map(RedisSpringMetricRelevance::getRedisMetric)
                .findFirst()
                .orElseThrow(() -> new RedisException("redis spring metric convert error, metric: [{}]", metric));
    }

}
