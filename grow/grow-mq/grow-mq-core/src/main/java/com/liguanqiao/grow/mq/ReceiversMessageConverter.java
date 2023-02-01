package com.liguanqiao.grow.mq;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.span.TracerSpan;

/**
 * @author liguanqiao
 * @since 2023/1/10
 **/
public interface ReceiversMessageConverter {

    TracerContext getTracerContext();

    default void trace(TracerSpan tracerSpan) {
        getTracerContext().joinSpan(tracerSpan);
    }

}
