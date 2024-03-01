package com.liguanqiao.grow.example.core.redis.controller;

import com.liguanqiao.grow.example.core.redis.model.dto.GrowRedisValueDTO;
import com.liguanqiao.grow.example.core.redis.model.resp.GrowRedisResp;
import com.liguanqiao.grow.example.core.redis.serivce.GrowRedisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author liguanqiao
 * @since 2023/1/5
 **/
@Slf4j
@RestController
@RequestMapping("/growRedis")
@AllArgsConstructor
public class GrowRedisController {

    private final GrowRedisService growRedisService;

    @GetMapping("/getValue")
    public GrowRedisResp getValue(@RequestParam String key) {
        log.info("getValue");
        return growRedisService.getValue(key);
    }

    @PostMapping("/setValue")
    public void setValue(@RequestParam String key) {
        log.info("setValue");
        growRedisService.setValue(key, GrowRedisValueDTO.create());
    }

}
