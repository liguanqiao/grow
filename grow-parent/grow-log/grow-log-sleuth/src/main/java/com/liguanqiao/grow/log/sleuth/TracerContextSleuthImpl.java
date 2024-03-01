package com.liguanqiao.grow.log.sleuth;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.span.TracerSpan;
import lombok.AllArgsConstructor;
import zipkin2.internal.HexCodec;

import java.util.Optional;

/**
 * Tracer 上下文Sleuth实现
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
@AllArgsConstructor
public class TracerContextSleuthImpl implements TracerContext {

    private final Tracer tracer;

    @Override
    public TracerSpan currentSpan() {
        return Optional.ofNullable(tracer.currentSpan())
                .map(Span::context)
                .map(context -> TracerSpan.of(context.traceIdString(), context.spanIdString()))
                .orElse(null);
    }

    @Override
    public TracerSpan nextSpan() {
        return Optional.ofNullable(tracer.nextSpan())
                .map(Span::context)
                .map(context -> TracerSpan.of(context.traceIdString(), context.spanIdString()))
                .orElse(null);
    }

    @Override
    public void joinSpan(TracerSpan tracerSpan) {
        TraceContext traceContext = Optional.ofNullable(tracerSpan)
                .map(span -> TraceContext.newBuilder()
                        .traceId(HexCodec.lowerHexToUnsignedLong(span.getTraceId()))
                        .spanId(HexCodec.lowerHexToUnsignedLong(span.getSpanId()))
                        .build())
                .orElseGet(tracer.newTrace()::context);
        tracer.newChild(traceContext);
    }

    @Override
    public void cleanSpan() {
        //不需要清除
    }
}
