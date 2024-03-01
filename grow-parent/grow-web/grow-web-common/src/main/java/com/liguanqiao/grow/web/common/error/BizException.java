package com.liguanqiao.grow.web.common.error;

import lombok.Getter;

/**
 * @author liguanqiao
 * @since 2022/12/4
 **/
@Getter
public class BizException extends RuntimeException implements ErrorCode {
    private final int code;
    private final String note;

    public BizException(int code, String note) {
        super(note);
        this.code = code;
        this.note = note;
    }

    public BizException(int code, String note, Object... params) {
        super(String.format(note, params));
        this.code = code;
        this.note = String.format(note, params);
    }

    public BizException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getNote());
    }

    public BizException(ErrorCode errorCode, Object... params) {
        this(errorCode.getCode(), errorCode.getNote(), params);
    }

}
