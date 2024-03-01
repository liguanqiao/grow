package com.liguanqiao.grow.core.model.req;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 游标分页请求对象
 *
 * @author liguanqiao
 * @since 2023/6/25
 **/
@Data
@Accessors(chain = true)
public class CursorPageReq<C> {
    /**
     * 游标
     * 第一页不用传入，后续请求附带上次翻页的游标
     **/
    private C cursor;
    /**
     * 页数
     * 默认值 = 10, 传入-1获取全部
     **/
    private Integer pageSize = 10;
}
