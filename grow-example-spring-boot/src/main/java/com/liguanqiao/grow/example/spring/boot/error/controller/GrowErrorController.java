package com.liguanqiao.grow.example.spring.boot.error.controller;

import com.liguanqiao.grow.web.common.error.BizException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试异常捕捉
 *
 * @author liguanqiao
 * @since 2023/2/1
 **/
@Slf4j
@RestController
@RequestMapping("/growError")
@AllArgsConstructor
public class GrowErrorController {

    @GetMapping("/test")
    public void test() {
        throw new BizException(GrowErrorCode.TEST);
    }

}
