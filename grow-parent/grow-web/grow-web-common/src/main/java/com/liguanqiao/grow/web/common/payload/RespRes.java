package com.liguanqiao.grow.web.common.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liguanqiao
 * @since 2022/12/4
 **/
@Getter
@AllArgsConstructor(staticName = "of")
public class RespRes<T> {
    private BizMessage bizMsg;
    private T dataMsg;

    public RespRes(BizMessage bizMsg) {
        this.bizMsg = bizMsg;
    }

    public static RespRes<Void> of(BizMessage bizMsg) {
        return new RespRes<>(bizMsg);
    }
}
