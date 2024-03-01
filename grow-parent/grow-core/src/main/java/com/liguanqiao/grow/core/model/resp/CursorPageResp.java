package com.liguanqiao.grow.core.model.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 游标分页响应对象
 *
 * @author liguanqiao
 * @since 2023/6/25
 **/
@Data
@Accessors(chain = true)
public class CursorPageResp<C, T> {
    /**
     * 游标
     **/
    private C cursor;
    /**
     * 是否还有下一页
     **/
    private Boolean hasNext;
    /**
     * 结果集
     **/
    private List<T> list;
}
