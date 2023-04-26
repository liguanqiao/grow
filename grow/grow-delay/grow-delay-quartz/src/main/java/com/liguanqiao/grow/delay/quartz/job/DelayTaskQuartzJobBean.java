package com.liguanqiao.grow.delay.quartz.job;

import cn.hutool.core.util.TypeUtil;
import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.TaskInfo;
import com.liguanqiao.grow.delay.error.DelayTaskException;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.json.JsonUtil;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.span.TracerSpan;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author liguanqiao
 * @since 2023/4/19
 **/
@Slf4j
@SuppressWarnings({"rawtypes", "unchecked"})
public class DelayTaskQuartzJobBean implements Job {

    public static final String DATA_KEY = "GROW_DELAY_TASK_DATA_KEY";
    public static final String RETRY_KEY = "GROW_DELAY_TASK_RETRY_KEY";
    public static final String TRACER_KEY = "GROW_DELAY_TASK_TRACER_KEY";

    private final Map<String, DelayTaskHandler<?>> handlerMap = new HashMap<>();
    private final Map<String, Type> dataTypeMap = new HashMap<>();
    private final DelayTaskOps ops;
    private final TracerContext tracerContext;

    public DelayTaskQuartzJobBean(List<DelayTaskHandler<?>> handlers, DelayTaskOps ops, TracerContext tracerContext) {
        this.ops = ops;
        this.tracerContext = tracerContext;
        for (DelayTaskHandler<?> handler : handlers) {
            handlerMap.put(handler.getTopic(), handler);
            dataTypeMap.put(handler.getTopic(), TypeUtil.getTypeArgument(handler.getClass()));
        }
    }

    @Override
    public void execute(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        JobDataMap jobData = context.getJobDetail().getJobDataMap();

        doTracer(jobData);
        TaskInfo taskInfo = doTaskInfo(jobKey, jobData);
        log.debug("Delay Task Handle, Topic: [{}], TaskData: [{}]", jobKey.getGroup(), taskInfo);
        doExecute(jobKey, taskInfo);
    }

    private void doTracer(JobDataMap jobData) {
        Optional.ofNullable(jobData.getString(TRACER_KEY))
                .map(json -> JsonUtil.toBean(json, TracerSpan.class))
                .ifPresent(tracerContext::joinSpan);
    }

    private TaskInfo doTaskInfo(JobKey jobKey, JobDataMap jobData) {
        Object data = Optional.ofNullable(jobData.getString(DATA_KEY))
                .map(json -> JsonUtil.toBean(json, getDataType(jobKey.getGroup())))
                .orElse(null);
        TaskInfo taskInfo = new TaskInfo<>();
        taskInfo.setId(jobKey.getName());
        taskInfo.setRetry(jobData.getInt(RETRY_KEY) + 1);
        taskInfo.setData(data);
        return taskInfo;
    }

    private void doExecute(JobKey jobKey, TaskInfo taskInfo) {
        DelayTaskHandler<?> handler = getHandle(jobKey.getGroup());
        try {
            handler.execute(taskInfo);
        } catch (Exception ex) {
            if (taskInfo.getRetry() <= handler.getRetry()) {
                log.error("retry delay task, topic:{}, task:{}", handler.getTopic(), taskInfo, ex);
                //任务延迟10秒重试
                ops.add(handler.getTopic(), taskInfo, 10, TimeUnit.SECONDS);
            } else {
                log.error("delay task process element error, topic:{}, task:{}", handler.getTopic(), taskInfo, ex);
                handler.error(taskInfo, ex);
            }
        }
    }

    private DelayTaskHandler<?> getHandle(String topic) {
        return Optional.ofNullable(topic)
                .map(handlerMap::get)
                .orElseThrow(() -> new DelayTaskException("delay task handle is null, topic: [{}]", topic));
    }

    private Type getDataType(String topic) {
        return Optional.ofNullable(topic)
                .map(dataTypeMap::get)
                .orElseThrow(() -> new DelayTaskException("delay task data type is null, topic: [{}]", topic));
    }

}
