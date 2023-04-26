package com.liguanqiao.grow.delay.handler;


import com.liguanqiao.grow.delay.TaskInfo;

/**
 * 延迟任务执行器
 *
 * @author liguanqiao
 * @since 2023/4/14
 **/
public interface DelayTaskHandler<T> {

    /**
     * 执行
     *
     * @param task 任务
     **/
    void execute(TaskInfo<T> task);

    /**
     * 获取主题
     **/
    String getTopic();

    /**
     * 重试次数
     **/
    default int getRetry() {
        return 0;
    }

    /**
     * 失败通知接口;
     * 重试n次仍然失败;
     * 可以在这个接口写自己的通知逻辑;
     * 比如发送邮件或者钉钉消息;
     *
     * @param task 任务
     * @param ex   错误
     */
    default void error(TaskInfo<T> task, Exception ex) {

    }

}
