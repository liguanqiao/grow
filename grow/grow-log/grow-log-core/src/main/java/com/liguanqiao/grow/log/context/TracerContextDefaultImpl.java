package com.liguanqiao.grow.log.context;

import com.liguanqiao.grow.log.span.TracerSpan;

/**
 * Tracer 上下文默认实现
 *
 * @author liguanqiao
 * @since 2023/1/10
 **/
public class TracerContextDefaultImpl implements TracerContext {

    @Override
    public TracerSpan currentSpan() {
        return null;
    }

    @Override
    public TracerSpan nextSpan() {
        return null;
    }

    @Override
    public void joinSpan(TracerSpan tracerSpan) {
    }

    @Override
    public void cleanSpan() {

    }

}
