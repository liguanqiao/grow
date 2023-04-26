package com.liguanqiao.grow.delay.rabbitmq.config;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.delay.rabbitmq.listener.DelayTaskRabbitMqListener;
import com.liguanqiao.grow.delay.rabbitmq.util.DelayTaskRabbitMqUtil;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.List;

/**
 * @author liguanqiao
 * @since 2023/4/14
 **/
public class DelayTaskHandlerEndpointRegister {


    public final List<DelayTaskHandler<?>> handlers;

    public DelayTaskHandlerEndpointRegister(List<DelayTaskHandler<?>> handlers) {
        this.handlers = handlers;
    }

    public void registerAllEndpoints(ConnectionFactory factory, DelayTaskOps ops, MessageConverter converter) {
        for (DelayTaskHandler<?> handler : handlers) {
            createQueue(factory, handler);
            createMessageListener(factory, handler, ops, converter);
        }
    }

    private void createQueue(ConnectionFactory factory, DelayTaskHandler<?> handler) {
        Queue queue = DelayTaskRabbitMqUtil.createQueue(handler.getTopic());
        CustomExchange exchange = DelayTaskRabbitMqUtil.createExchange(handler.getTopic());
        Binding binding = DelayTaskRabbitMqUtil.binding(queue, exchange);
        RabbitAdmin rabbitAdmin = new RabbitAdmin(factory);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareBinding(binding);
    }

    private void createMessageListener(ConnectionFactory factory, DelayTaskHandler<?> handler, DelayTaskOps ops, MessageConverter converter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factory);
        container.setQueueNames(DelayTaskRabbitMqUtil.getQueueName(handler.getTopic()));
        container.setMessageListener(new DelayTaskRabbitMqListener(handler, ops, converter));
        container.start();
    }

}
