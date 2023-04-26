package com.liguanqiao.grow.delay.error;

import com.liguanqiao.grow.core.error.AbsGrowException;

/**
 * 分布式延迟任务错误类
 *
 * @author liguanqiao
 * @since 2023/3/30
 **/
public class DelayTaskException extends AbsGrowException {

    public DelayTaskException(Throwable e) {
        super(e);
    }

    public DelayTaskException(String message) {
        super(message);
    }

    public DelayTaskException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public DelayTaskException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DelayTaskException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    public DelayTaskException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }
}
