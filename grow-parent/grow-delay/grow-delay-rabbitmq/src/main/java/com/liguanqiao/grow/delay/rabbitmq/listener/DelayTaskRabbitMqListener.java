package com.liguanqiao.grow.delay.rabbitmq.listener;

import cn.hutool.core.util.TypeUtil;
import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.TaskInfo;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.delay.rabbitmq.util.DelayTaskMessagePropertiesUtil;
import com.liguanqiao.grow.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * 延迟任务RabbitMQ执行器
 *
 * @author liguanqiao
 * @since 2023/3/30
 **/
@Slf4j
@SuppressWarnings({"rawtypes", "unchecked"})
public class DelayTaskRabbitMqListener extends MessageListenerAdapter {

    private final DelayTaskHandler<?> handler;
    private final DelayTaskOps ops;
    private final Type dataType;

    public DelayTaskRabbitMqListener(DelayTaskHandler<?> handler, DelayTaskOps ops, MessageConverter converter) {
        this.handler = handler;
        this.ops = ops;
        this.dataType = TypeUtil.getTypeArgument(handler.getClass());
        super.setMessageConverter(converter);
        super.setDefaultListenerMethod("onMessage");
    }

    @Override
    public void onMessage(Message message) {
        TaskInfo taskInfo = doTaskInfo(message);
        log.debug("Delay Task Handle, Topic: [{}], TaskData: [{}]", handler.getTopic(), taskInfo);
        doExecute(taskInfo);
    }

    private TaskInfo doTaskInfo(Message message) {
        String body = new String(message.getBody(), Charset.defaultCharset());
        Object data = JsonUtil.toBean(body, this.dataType);
        TaskInfo taskInfo = new TaskInfo<>();
        taskInfo.setId(message.getMessageProperties().getMessageId());
        taskInfo.setRetry(DelayTaskMessagePropertiesUtil.getRetry(message.getMessageProperties()) + 1);
        taskInfo.setData(data);
        return taskInfo;
    }

    private void doExecute(TaskInfo taskInfo) {
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
}
