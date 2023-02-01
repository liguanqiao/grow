package com.liguanqiao.grow.web.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liguanqiao
 * @since 2022/12/4
 **/
@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    SERVER_EXCEPTION(5000, "服务器异常"),
    FORBIDDEN(5001, "鉴权失败"),
    WRONG_PARAMS(5002, "参数有误"),
    AUTH_DENY(5003, "没有权限"),
    NOT_FOUND(5004, "资源未找到"),
    METHOD_NOT_ALLOW(5005, "HTTP方法使用错误"),
    HYSTRIX_FAIL(5006, "网络连接失败，请稍后重试"),
    ;

    private final int code;
    private final String note;
}
