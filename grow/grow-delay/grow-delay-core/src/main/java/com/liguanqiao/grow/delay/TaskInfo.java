package com.liguanqiao.grow.delay;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 延迟任务信息
 *
 * @author liguanqiao
 * @since 2023/3/30
 **/
@ToString
@Data
@Accessors(chain = true)
public class TaskInfo<T> implements Serializable {
    /**
     * 任务ID
     **/
    private String id;
    /**
     * 已经重试次数
     **/
    private int retry = 0;
    /**
     * 数据
     **/
    private T data;
}
