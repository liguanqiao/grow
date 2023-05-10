package com.liguanqiao.grow.example.spring.cloud.orders.error;

import com.liguanqiao.grow.web.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liguanqiao
 * @since 2023/5/5
 **/
@Getter
@AllArgsConstructor
public enum GrowOrdersBizCode implements ErrorCode {

    TEST(100000, "测试异常"),
    ;

    private final int code;
    private final String note;
}
