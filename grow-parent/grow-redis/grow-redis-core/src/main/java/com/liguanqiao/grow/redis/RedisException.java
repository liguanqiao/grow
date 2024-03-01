package com.liguanqiao.grow.redis;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Redis异常
 * @author liguanqiao
 **/
public class RedisException  extends RuntimeException {

    public RedisException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public RedisException(String message) {
        super(message);
    }

    public RedisException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public RedisException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public RedisException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public RedisException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}