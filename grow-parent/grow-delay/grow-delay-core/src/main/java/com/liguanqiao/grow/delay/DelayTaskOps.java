package com.liguanqiao.grow.delay;


import cn.hutool.core.util.IdUtil;
import com.liguanqiao.grow.delay.error.DelayTaskException;

import java.util.concurrent.TimeUnit;

/**
 * 分布式延迟任务操作
 *
 * @author liguanqiao
 * @since 2023/3/30
 **/
public interface DelayTaskOps {

    /**
     * 添加任务
     *
     * @param topic    主题
     * @param task     任务
     * @param time     时间
     * @param timeUnit 时间单位
     **/
    <T> void add(String topic, TaskInfo<T> task, long time, TimeUnit timeUnit);

    /**
     * 添加任务
     *
     * @param topic    主题
     * @param taskData 任务数据
     * @param time     时间
     * @param timeUnit 时间单位
     * @return 任务ID
     **/
    default <T> String add(String topic, T taskData, long time, TimeUnit timeUnit) {
        String taskId = IdUtil.nanoId();
        add(topic, new TaskInfo<>().setId(taskId).setData(taskData), time, timeUnit);
        return taskId;
    }

    /**
     * 添加任务
     *
     * @param topic     主题
     * @param task      任务
     * @param timestamp 运行时间时间戳(毫秒)
     **/
    default <T> void add(String topic, TaskInfo<T> task, long timestamp) {
        long delay = timestamp - System.currentTimeMillis();
        if (delay < 0)
            throw new DelayTaskException("current time to run time > 0");

        add(topic, task, delay, TimeUnit.MILLISECONDS);
    }


    /**
     * 添加任务
     *
     * @param topic     主题
     * @param taskData  任务数据
     * @param timestamp 运行时间时间戳(毫秒)
     * @return 任务ID
     **/
    default <T> String add(String topic, T taskData, long timestamp) {
        String taskId = IdUtil.nanoId();
        add(topic, new TaskInfo<>().setId(taskId).setData(taskData), timestamp);
        return taskId;
    }

    /**
     * 删除任务
     *
     * @param topic  主题
     * @param taskId 任务ID
     **/
    boolean del(String topic, String taskId);

}
