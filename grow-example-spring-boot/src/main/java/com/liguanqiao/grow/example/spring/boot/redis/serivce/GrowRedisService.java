package com.liguanqiao.grow.example.spring.boot.redis.serivce;

import com.liguanqiao.grow.example.spring.boot.redis.model.dto.GrowRedisValueDTO;
import com.liguanqiao.grow.example.spring.boot.redis.model.resp.GrowRedisResp;

/**
 * @author liguanqiao
 * @since 2023/1/5
 **/
public interface GrowRedisService {

    GrowRedisResp getValue(String key);

    void setValue(String key, GrowRedisValueDTO value);
}
