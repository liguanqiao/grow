package com.liguanqiao.grow.example.spring.boot.mq.controller;

import cn.hutool.core.util.RandomUtil;
import com.liguanqiao.grow.example.spring.boot.mq.service.GrowMqService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liguanqiao
 * @since 2023/1/5
 **/
@Slf4j
@RestController
@RequestMapping("/growMq")
@AllArgsConstructor
public class GrowMqController {

    private final GrowMqService growMqService;

    @PostMapping("/send")
    public String send() {
        growMqService.send();
        return RandomUtil.randomString(5);
    }

}
