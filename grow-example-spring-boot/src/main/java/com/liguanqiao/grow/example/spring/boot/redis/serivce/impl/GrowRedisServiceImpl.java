package com.liguanqiao.grow.example.spring.boot.redis.serivce.impl;

import com.liguanqiao.grow.example.spring.boot.redis.model.dto.GrowRedisValueDTO;
import com.liguanqiao.grow.example.spring.boot.redis.model.resp.GrowRedisResp;
import com.liguanqiao.grow.example.spring.boot.redis.serivce.GrowRedisService;
import com.liguanqiao.grow.redis.RedisOps;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author liguanqiao
 * @since 2023/1/5
 **/
@Service
@AllArgsConstructor
public class GrowRedisServiceImpl implements GrowRedisService {

    private final RedisOps redisOps;

    @Override
    public GrowRedisResp getValue(String key) {
        return redisOps.get(key, GrowRedisResp.class)
                .orElse(null);
    }

    @Override
    public void setValue(String key, GrowRedisValueDTO value) {
        redisOps.set(key, value);
    }
}
