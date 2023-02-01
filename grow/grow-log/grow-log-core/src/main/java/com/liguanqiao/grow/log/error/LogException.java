package com.liguanqiao.grow.log.error;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 分布式日志异常
 *
 * @author liguanqiao
 * @since 2023/1/11
 **/
public class LogException extends RuntimeException {

    public LogException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public LogException(String message) {
        super(message);
    }

    public LogException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public LogException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public LogException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public LogException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}