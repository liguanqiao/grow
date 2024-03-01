package com.liguanqiao.grow.mq.model;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.span.TracerSpan;
import com.liguanqiao.grow.mq.util.MqSerializeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Mq 包装类
 *
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MqWrap {
    private TracerSpan tracerSpan;
    private String content;

    public static MqWrap convert(TracerContext tracerContext, Object data) {
        return new MqWrap()
                .setTracerSpan(tracerContext.nextSpan())
                .setContent(MqSerializeUtil.valueToString().apply(data));
    }

    public static String genMsgValue(TracerContext tracerContext, Object data) {
        return MqSerializeUtil.valueToString().apply(convert(tracerContext, data));
    }
}
