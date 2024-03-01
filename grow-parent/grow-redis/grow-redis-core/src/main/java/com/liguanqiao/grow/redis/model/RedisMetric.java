package com.liguanqiao.grow.redis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Redis 距离单位
 *
 * @author liguanqiao
 **/
@Getter
@AllArgsConstructor
public enum RedisMetric {
    METERS(6378137, "m"),
    KILOMETERS(6378.137, "km"),
    MILES(3963.191, "mi"),
    FEET(20925646.325, "ft");

    private final double multiplier;
    private final String abbreviation;
}
