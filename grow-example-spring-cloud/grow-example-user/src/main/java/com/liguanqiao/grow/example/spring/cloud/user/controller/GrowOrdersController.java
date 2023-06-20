package com.liguanqiao.grow.example.spring.cloud.user.controller;

import com.liguanqiao.grow.example.spring.cloud.user.error.GrowOrdersBizCode;
import com.liguanqiao.grow.example.spring.cloud.user.resp.GrowOrdersResp;
import com.liguanqiao.grow.web.common.error.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liguanqiao
 * @since 2023/5/5
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class GrowOrdersController {

    @GetMapping("/get")
    public GrowOrdersResp get() {
        log.info("get");
        return GrowOrdersResp.create();
    }

    @GetMapping("/get2")
    public void get2() {
        log.info("get2");
    }

    @GetMapping("/error")
    public GrowOrdersResp error() {
        log.info("error");
        throw new BizException(GrowOrdersBizCode.TEST);
    }

    @GetMapping("/error2")
    public GrowOrdersResp error2() {
        log.info("error2");
        throw new RuntimeException("测试未知错误");
    }
}
