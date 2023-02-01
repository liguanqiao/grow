package com.liguanqiao.grow.lock.error;

import com.liguanqiao.grow.core.error.AbsGrowException;

/**
 * 分布式锁错误类
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
public class LockException extends AbsGrowException {

    public LockException(Throwable e) {
        super(e);
    }

    public LockException(String message) {
        super(message);
    }

    public LockException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public LockException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public LockException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public LockException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }
}
