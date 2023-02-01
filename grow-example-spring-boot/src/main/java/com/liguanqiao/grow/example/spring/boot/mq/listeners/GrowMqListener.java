package com.liguanqiao.grow.example.spring.boot.mq.listeners;

import com.liguanqiao.grow.example.spring.boot.mq.constant.GrowMqConstant;
import com.liguanqiao.grow.example.spring.boot.mq.model.dto.GrowMqValueDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Slf4j
@Component
@RabbitListener(queuesToDeclare = @Queue(GrowMqConstant.TOPIC))
public class GrowMqListener {

    /**
     * 处理设备报警
     */
    @RabbitHandler
    public void handle(GrowMqValueDTO dto) {
        log.info("grow mq listener, data: [{}]", dto);
    }

    /**
     * 处理设备报警
     */
//    @RabbitListener(queuesToDeclare = @Queue(GrowMqConstant.TOPIC))
//    @RabbitHandler
//    public void handle(String dto) {
//        log.info("grow mq listener, data: [{}]", dto);
//    }

}
