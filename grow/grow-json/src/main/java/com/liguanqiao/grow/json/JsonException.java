package com.liguanqiao.grow.json;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Json异常
 *
 * @author liguanqiao
 **/
public class JsonException extends RuntimeException {

    public JsonException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public JsonException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public JsonException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public JsonException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}