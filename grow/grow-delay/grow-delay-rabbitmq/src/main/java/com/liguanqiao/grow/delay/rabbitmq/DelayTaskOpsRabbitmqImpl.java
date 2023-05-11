package com.liguanqiao.grow.delay.rabbitmq;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.TaskInfo;
import com.liguanqiao.grow.delay.error.DelayTaskException;
import com.liguanqiao.grow.delay.rabbitmq.util.DelayTaskMessagePropertiesUtil;
import com.liguanqiao.grow.delay.rabbitmq.util.DelayTaskRabbitMqUtil;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.mq.model.MqWrap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 分布式延迟任务操作（RabbitMQ实现）
 *
 * @author liguanqiao
 * @since 2023/3/30
 **/
@Slf4j
@AllArgsConstructor
public class DelayTaskOpsRabbitmqImpl implements DelayTaskOps {

    private final RabbitTemplate rabbitTemplate;
    private final TracerContext tracerContext;

    @Override
    public <T> void add(String topic, TaskInfo<T> task, long time, TimeUnit timeUnit) {
        String messageId = task.getId();
        MqWrap value = MqWrap.convert(tracerContext, task.getData());
        log.debug("Delay Task Add, Topic: [{}], TaskInfo: [{}]", topic, task);
        rabbitTemplate.convertAndSend(DelayTaskRabbitMqUtil.getExchangeName(topic), DelayTaskRabbitMqUtil.ROUTING_KEY, value, message -> {
            message.getMessageProperties().setMessageId(messageId);
            message.getMessageProperties().setDelay((int) timeUnit.toMillis(time));
            DelayTaskMessagePropertiesUtil.setRetry(message.getMessageProperties(), task.getRetry());
            return message;
        });
    }

    /**
     * RabbitMQ [delayed-message-exchange] plugin does not support the removal of delayed messages.
     * <a href="https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/issues/16">github#issues#16</a>
     **/
    @Override
    public boolean del(String topic, String taskId) {
        throw new DelayTaskException("RabbitMQ [delayed-message-exchange] plugin does not support the removal of delayed messages.");
    }

}
