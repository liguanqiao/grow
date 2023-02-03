package com.liguanqiao.grow.example.spring.boot.error.controller;

import com.liguanqiao.grow.web.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liguanqiao
 * @since 2023/2/1
 **/
@Getter
@AllArgsConstructor
public enum GrowErrorCode implements ErrorCode {

    TEST(100000, "测试异常"),
    ;

    private final int code;
    private final String note;
}
