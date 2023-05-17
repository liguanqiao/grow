package com.liguanqiao.grow.web.common.payload;

import com.liguanqiao.grow.web.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 业务信息
 *
 * @author liguanqiao
 * @since 2023/1/13
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class BizMessage {
    private Integer code;
    private String note;
    private String origin;
    private String traceId;

    public static BizMessage ok(String origin, String traceId) {
        return BizMessage.of(0, "success", origin, traceId);
    }

    public static BizMessage of(ErrorCode errorCode, String origin, String traceId) {
        return BizMessage.of(errorCode.getCode(), errorCode.getNote(), origin, traceId);
    }

}
