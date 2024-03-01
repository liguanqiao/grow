package com.liguanqiao.grow.log.tlog;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.span.TracerSpan;
import com.yomahub.tlog.context.SpanIdGenerator;
import com.yomahub.tlog.context.TLogContext;
import com.yomahub.tlog.core.rpc.TLogLabelBean;
import com.yomahub.tlog.core.rpc.TLogRPCHandler;

import java.util.Optional;
import java.util.function.Function;

/**
 * Tracer上下文TLog实现
 *
 * @author liguanqiao
 * @since 2023/1/11
 **/
public class TracerContextTLogImpl implements TracerContext {

    private final TLogRPCHandler tLogRPCHandler;

    public TracerContextTLogImpl() {
        this.tLogRPCHandler = new TLogRPCHandler();
    }

    @Override
    public TracerSpan currentSpan() {
        return Optional.ofNullable(TLogContext.getTraceId())
                .map(traceId -> TracerSpan.of(traceId, TLogContext.getSpanId()))
                .orElse(null);
    }

    @Override
    public TracerSpan nextSpan() {
        return Optional.ofNullable(TLogContext.getTraceId())
                .map(traceId -> TracerSpan.of(traceId, SpanIdGenerator.generateNextSpanId()))
                .orElse(null);
    }

    @Override
    public void joinSpan(TracerSpan tracerSpan) {
        TLogLabelBean logLabelBean = Optional.ofNullable(tracerSpan)
                .map(span2Label)
                .orElse(new TLogLabelBean());
        tLogRPCHandler.processProviderSide(logLabelBean);
    }

    @Override
    public void cleanSpan() {
        tLogRPCHandler.cleanThreadLocal();
    }

    private static final Function<TracerSpan, TLogLabelBean> span2Label = (span) -> {
        TLogLabelBean labelBean = new TLogLabelBean();
        labelBean.setTraceId(span.getTraceId());
        labelBean.setSpanId(span.getSpanId());
        return labelBean;
    };

}
