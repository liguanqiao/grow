package com.liguanqiao.grow.example.spring.cloud.orders.controller;

import com.liguanqiao.grow.example.spring.cloud.orders.error.GrowOrdersBizCode;
import com.liguanqiao.grow.example.spring.cloud.orders.model.resp.GrowOrdersResp;
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
@RequestMapping("/orders")
public class GrowOrdersController {

    @GetMapping("/get")
    public GrowOrdersResp get() {
        log.info("get");
        return GrowOrdersResp.create();
    }

    @GetMapping("/error")
    public GrowOrdersResp error() {
        log.info("error");
        throw new BizException(GrowOrdersBizCode.TEST);
    }

}
