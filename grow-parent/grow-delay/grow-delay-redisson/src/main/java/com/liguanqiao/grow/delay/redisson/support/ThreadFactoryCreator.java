package com.liguanqiao.grow.delay.redisson.support;

import cn.hutool.core.util.StrUtil;
import com.liguanqiao.grow.delay.error.DelayTaskException;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
@SuppressWarnings("NullableProblems")
public final class ThreadFactoryCreator {

    public static ThreadFactory create(String threadName) {
        if (StrUtil.isBlank(threadName)) {
            throw new DelayTaskException("argument [threadName] must not be blank");
        }
        return new NamedWithIdThreadFactory(threadName);
    }

    private static final class NamedWithIdThreadFactory implements ThreadFactory {

        private final AtomicInteger threadId = new AtomicInteger(1);

        private final String namePrefix;

        private NamedWithIdThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable command) {
            Thread thread = new Thread(command);
            thread.setName(this.namePrefix + "-" + this.threadId.getAndAdd(1));
            return thread;
        }
    }

}
