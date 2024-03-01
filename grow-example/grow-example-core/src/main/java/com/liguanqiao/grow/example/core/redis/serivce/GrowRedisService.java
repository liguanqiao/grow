package com.liguanqiao.grow.example.core.redis.serivce;


import com.liguanqiao.grow.example.core.redis.model.dto.GrowRedisValueDTO;
import com.liguanqiao.grow.example.core.redis.model.resp.GrowRedisResp;

/**
 * @author liguanqiao
 * @since 2023/1/5
 **/
public interface GrowRedisService {

    GrowRedisResp getValue(String key);

    void setValue(String key, GrowRedisValueDTO value);
}
