package com.liguanqiao.grow.lock.error;

import com.liguanqiao.grow.core.error.AbsGrowException;

/**
 * Exception thrown on failure to acquire a lock during an update, for example during a "select for update" statement.
 *
 * @author liguanqiao
 * @since 2023/1/13
 **/
public class CannotAcquireLockException extends AbsGrowException {
    public CannotAcquireLockException(Throwable e) {
        super(e);
    }

    public CannotAcquireLockException(String message) {
        super(message);
    }

    public CannotAcquireLockException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public CannotAcquireLockException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CannotAcquireLockException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public CannotAcquireLockException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }
}
