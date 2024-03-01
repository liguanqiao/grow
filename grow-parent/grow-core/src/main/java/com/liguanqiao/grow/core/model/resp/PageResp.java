package com.liguanqiao.grow.core.model.resp;

import cn.hutool.core.collection.ListUtil;
import com.liguanqiao.grow.core.model.req.PageReq;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页响应对象
 *
 * @author liguanqiao
 * @since 2022/12/20
 **/
@Data
@Accessors(chain = true)
public class PageResp<T> {
    /**
     * 当前页
     **/
    private int pageCur;
    /**
     * 每页的数量
     **/
    private int pageSize;
    /**
     * 总页数
     **/
    private int pages;
    /**
     * 总记录数
     **/
    private long total;
    /**
     * 结果集
     **/
    private List<T> list;

    /**
     * 转换
     *
     * @param <T>     数据类型
     * @param pageReq 分页请求
     * @param total   总条数
     * @param datas   数据
     * @return 分页响应
     **/
    public static <T> PageResp<T> convert(PageReq pageReq, long total, List<T> datas) {
        PageResp<T> result = new PageResp<>();
        result.setPageCur(pageReq.getPageCur());
        result.setPageSize(pageReq.getPageSize());
        result.setTotal(total);
        if (result.getTotal() % result.getPageSize() > 0) {
            result.setPages((int) ((result.getTotal() / result.getPageSize()) + 1));
        } else {
            result.setPages((int) (result.getTotal() / result.getPageSize()));
        }
        result.setList(datas);
        return result;
    }

    /**
     * 分页
     *
     * @param <T>     数据类型
     * @param pageReq 分页请求
     * @param datas   数据
     * @return 分页响应
     **/
    public static <T> PageResp<T> page(PageReq pageReq, List<T> datas) {
        //因为subd的end不包含，需+1;
        return convert(pageReq, datas.size(), ListUtil.sub(datas, pageReq.getStart(), pageReq.getEnd() + 1));
    }

    /**
     * 转换
     *
     * @param <R>    结果类型
     * @param mapper 动作
     * @return this
     **/
    public <R> PageResp<R> map(Function<T, R> mapper) {
        return convert(getList().stream().map(mapper).collect(Collectors.toList()));
    }

    /**
     * 循环
     *
     * @param action 动作
     * @return this
     **/
    public PageResp<T> forEach(Consumer<T> action) {
        getList().forEach(action);
        return this;
    }

    /**
     * 转换
     *
     * @param <R>  结果类型
     * @param list 数据
     * @return 分页响应
     **/
    public <R> PageResp<R> convert(List<R> list) {
        PageResp<R> result = new PageResp<>();
        result.setPageCur(getPageCur());
        result.setPageSize(getPageSize());
        result.setPages(getPages());
        result.setTotal(getTotal());
        result.setList(list);
        return result;
    }

    /**
     * 空
     *
     * @param <T>     数据类型
     * @param pageReq 分页请求
     * @return 分页响应
     **/
    public static <T> PageResp<T> empty(PageReq pageReq) {
        return empty(pageReq, 0L);
    }

    /**
     * 空
     *
     * @param <T>     数据类型
     * @param pageReq 分页请求
     * @param total   总条数
     * @return 分页响应
     **/
    public static <T> PageResp<T> empty(PageReq pageReq, long total) {
        return convert(pageReq, total, Collections.emptyList());
    }
}
