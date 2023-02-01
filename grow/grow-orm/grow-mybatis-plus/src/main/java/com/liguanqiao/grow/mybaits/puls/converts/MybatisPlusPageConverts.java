package com.liguanqiao.grow.mybaits.puls.converts;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.liguanqiao.grow.core.model.req.PageReq;
import com.liguanqiao.grow.core.model.resp.PageResp;

import java.util.List;

/**
 * @author liguanqiao
 * @since 2022/12/20
 **/
public interface MybatisPlusPageConverts {

    static <E> IPage<E> convert(PageReq req) {
        return convert(req, Boolean.TRUE);
    }

    static <E> IPage<E> convert(PageReq req, boolean searchCount) {
        return PageDTO.of(req.getPageCur(), req.getPageSize(), searchCount);
    }

    static <T, E> PageResp<T> convert(IPage<E> page, List<T> records) {
        PageResp<T> result = new PageResp<>();
        result.setPageCur((int) page.getCurrent());
        result.setPageSize((int) page.getSize());
        result.setPages((int) page.getPages());
        result.setTotal(page.getTotal());
        result.setList(records);
        return result;
    }

    static <T> PageResp<T> convert(IPage<T> page) {
        PageResp<T> result = new PageResp<>();
        result.setPageCur((int) page.getCurrent());
        result.setPageSize((int) page.getSize());
        result.setPages((int) page.getPages());
        result.setTotal(page.getTotal());
        result.setList(page.getRecords());
        return result;
    }

}
