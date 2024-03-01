package com.liguanqiao.grow.log.sleuth.utils;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import lombok.experimental.UtilityClass;

import java.util.Optional;

/**
 * @author liguanqiao
 * @since 2023/1/12
 **/
@UtilityClass
public class SleuthTracerUtil {

    public static TraceContext join(Tracer tracer){
        TraceContext traceContext = Optional.ofNullable(tracer.currentSpan())
                .map(Span::context)
                .orElseGet(tracer.newTrace()::context);
        tracer.newChild(traceContext);
        return traceContext;
    }

}
