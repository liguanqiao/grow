package com.liguanqiao.grow.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RedisGeo返回的封装结构
 *
 * @author liguanqiao
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class RedisGeoResult {
    /**
     * 距离
     */
    private double dis;
    /**
     * 距离单位
     */
    private RedisMetric disUnit;
    /**
     * 经度
     */
    private double lon;
    /**
     * 纬度
     */
    private double lat;
}
