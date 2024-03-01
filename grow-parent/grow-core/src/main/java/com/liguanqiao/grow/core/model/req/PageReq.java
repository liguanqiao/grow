package com.liguanqiao.grow.core.model.req;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分页请求对象
 *
 * @author liguanqiao
 * @since 2022/12/20
 **/
@Data
@Accessors(chain = true)
public class PageReq {
    /**
     * 当前页
     * 默认值 = 1
     **/
    private Integer pageCur = 1;
    /**
     * 页数
     * 默认值 = 10, 传入-1获取全部
     **/
    private Integer pageSize = 10;

    public Integer getStart() {
        if (getPageCur() == 1) {
            return 0;
        }
        return ((getPageCur() - 1) * getPageSize());
    }

    public Integer getEnd() {
        if (getPageCur() <= 0 || getPageSize() <= 0) {
            return 0;
        }
        return (getPageCur() * getPageSize()) - 1;
    }

}
