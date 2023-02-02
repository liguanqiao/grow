package com.liguanqiao.grow.log.context;

import com.liguanqiao.grow.log.span.TracerSpan;

/**
 * Tracer 上下文
 *
 * @author liguanqiao
 * @since 2023/1/10
 **/
public interface TracerContext {

    /**
     * 获取当前线程标签
     *
     * @return 标签
     */
    TracerSpan currentSpan();

    /**
     * 生成下一个标签
     *
     * @return 标签
     */
    TracerSpan nextSpan();

    /**
     * 覆盖当前线程标签
     *
     * @param tracerSpan 标签
     */
    void joinSpan(TracerSpan tracerSpan);

    /**
     * 清除当前线程标签
     */
    void cleanSpan();
}
