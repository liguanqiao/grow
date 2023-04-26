package com.liguanqiao.grow.delay.rabbitmq.util;

import cn.hutool.core.util.StrUtil;
import org.springframework.amqp.core.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liguanqiao
 * @since 2023/3/30
 **/
public class DelayTaskRabbitMqUtil {

    private static final String QUEUE_NAME = "grow.delay.task.{}.queue";
    private static final String EXCHANGE_NAME = "grow.delay.task.{}.exchange";
    public static final String ROUTING_KEY = "grow.delay.task.routing.key";

    /**
     * 获取队列名称
     *
     * @param topic 主题
     * @return 队列名称
     **/
    public static String getQueueName(String topic) {
        return StrUtil.format(QUEUE_NAME, topic);
    }

    /**
     * 创建队列
     *
     * @param topic 主题
     * @return 队列
     */
    public static Queue createQueue(String topic) {
        return new Queue(getQueueName(topic), Boolean.TRUE);
    }

    /**
     * 获取交换机名称
     *
     * @param topic 主题
     * @return 交换机
     **/
    public static String getExchangeName(String topic) {
        return StrUtil.format(EXCHANGE_NAME, topic);
    }

    /**
     * 创建交换机
     *
     * @param topic 主题
     * @return 交换机
     **/
    public static CustomExchange createExchange(String topic) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(getExchangeName(topic), "x-delayed-message", Boolean.TRUE, Boolean.FALSE, args);
    }

    /**
     * 绑定延迟任务队列和与之对应的交换机
     *
     * @param queue    队列
     * @param exchange 交换机
     */
    public static Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(ROUTING_KEY)
                .noargs();
    }


}
