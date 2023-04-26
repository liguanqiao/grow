package com.liguanqiao.grow.delay.redisson.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.TypeUtil;
import com.liguanqiao.grow.delay.TaskInfo;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.json.JsonUtil;
import com.liguanqiao.grow.log.span.TracerSpan;
import lombok.Getter;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.lang.reflect.Type;
import java.util.*;

@Getter
@SuppressWarnings({"rawtypes"})
public class DelayQueueRegistryInfo {

    private final static String GROW_DELAY_TASK_KEY = "GROW_DELAY_TASK:";
    private final static String TASK_RETRY_KEY = "RETRY_KEY:";
    private final static String TASK_DATA_KEY = "DATA_KEY:";
    private final static String TASK_TRACER_KEY = "TRACER_KEY:";

    private final DelayTaskHandler<?> delayTaskHandler;
    private final Type delayTaskDataType;
    private final RBlockingQueue<String> blockingQueue;
    private final RDelayedQueue<String> delayedQueue;
    private final RMap<String, String> taskInfoMap;

    public DelayQueueRegistryInfo(RedissonClient redissonClient, DelayTaskHandler<?> delayTaskHandler) {
        this.delayTaskHandler = delayTaskHandler;
        this.delayTaskDataType = TypeUtil.getTypeArgument(delayTaskHandler.getClass());
        this.blockingQueue = redissonClient.getBlockingQueue(delayTaskHandler.getTopic(), StringCodec.INSTANCE);
        this.delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
        this.taskInfoMap = redissonClient.getMap(GROW_DELAY_TASK_KEY.concat(delayTaskHandler.getTopic()), StringCodec.INSTANCE);
    }

    public TaskInfo getTaskInfo(String taskId) {
        Integer retry = Optional.ofNullable(taskId)
                .map(TASK_RETRY_KEY::concat)
                .map(taskInfoMap::get)
                .map(Integer::valueOf)
                .orElse(0);
        Object data = Optional.ofNullable(taskId)
                .map(TASK_DATA_KEY::concat)
                .map(taskInfoMap::get)
                .map(json -> JsonUtil.toBean(json, delayTaskDataType))
                .orElse(null);
        return new TaskInfo<>().setId(taskId).setRetry(retry).setData(data);
    }

    public void setTaskInfo(TaskInfo<?> taskInfo, TracerSpan span) {
        Map<String, String> dataMap = new HashMap<>();
        Optional.of(taskInfo.getRetry())
                .map(String::valueOf)
                .ifPresent(val -> dataMap.put(TASK_RETRY_KEY.concat(taskInfo.getId()), val));
        Optional.ofNullable(taskInfo.getData())
                .map(JsonUtil::toJson)
                .ifPresent(val -> dataMap.put(TASK_DATA_KEY.concat(taskInfo.getId()), val));
        Optional.ofNullable(span)
                .map(JsonUtil::toJson)
                .ifPresent(val -> dataMap.put(TASK_TRACER_KEY.concat(taskInfo.getId()), val));
        Optional.of(dataMap)
                .filter(CollUtil::isNotEmpty)
                .ifPresent(taskInfoMap::putAll);
    }

    public void removeTaskInfo(String taskId) {
        List<String> keys = new ArrayList<>();
        Optional.ofNullable(taskId).map(TASK_RETRY_KEY::concat).ifPresent(keys::add);
        Optional.ofNullable(taskId).map(TASK_DATA_KEY::concat).ifPresent(keys::add);
        Optional.ofNullable(taskId).map(TASK_TRACER_KEY::concat).ifPresent(keys::add);
        Optional.of(keys)
                .filter(CollUtil::isNotEmpty)
                .map(list -> list.toArray(new String[0]))
                .ifPresent(taskInfoMap::fastRemove);
    }

    public TracerSpan getTracer(String taskId) {
        return Optional.ofNullable(taskId)
                .map(TASK_TRACER_KEY::concat)
                .map(taskInfoMap::get)
                .map(json -> JsonUtil.toBean(json, TracerSpan.class))
                .orElse(null);
    }

}
