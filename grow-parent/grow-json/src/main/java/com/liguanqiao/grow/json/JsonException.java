package com.liguanqiao.grow.json;

import com.liguanqiao.grow.core.error.AbsGrowException;

/**
 * Json异常
 *
 * @author liguanqiao
 **/
public class JsonException extends AbsGrowException {

    public JsonException(Throwable e) {
        super(e);
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public JsonException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public JsonException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public JsonException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

}