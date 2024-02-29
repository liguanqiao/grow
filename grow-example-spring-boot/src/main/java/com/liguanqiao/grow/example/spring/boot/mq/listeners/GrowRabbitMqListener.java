package com.liguanqiao.grow.example.spring.boot.mq.listeners;

import com.liguanqiao.grow.example.spring.boot.mq.constant.GrowMqConstant;
import com.liguanqiao.grow.example.spring.boot.mq.model.dto.GrowMqValueDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Slf4j
@Component
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(GrowMqConstant.TOPIC),
        exchange = @Exchange(GrowMqConstant.TOPIC),
        key = GrowMqConstant.KEY
))
public class GrowRabbitMqListener {

    @RabbitHandler
    public void onMessage(GrowMqValueDTO dto) {
        log.info("grow mq listener, data: [{}]", dto);
    }

}
