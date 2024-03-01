package com.liguanqiao.grow.core.error;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 抽象错误类
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
public abstract class AbsGrowException extends RuntimeException {

    public AbsGrowException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public AbsGrowException(String message) {
        super(message);
    }

    public AbsGrowException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public AbsGrowException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AbsGrowException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public AbsGrowException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}