package com.liguanqiao.grow.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * RedisGeo经纬度
 *
 * @author liguanqiao
 **/
@Data
@AllArgsConstructor
public class RedisGeoPoint {
    /**
     * 经度
     */
    private double lon;
    /**
     * 纬度
     */
    private double lat;
}
