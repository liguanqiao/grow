package com.liguanqiao.grow.log.span;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Log 标签包装类
 *
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TracerSpan {
    private String traceId;
    private String spanId;
}
