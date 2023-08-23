package com.liguanqiao.grow.example.spring.boot.mq.listeners;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Slf4j
//@Component
//@RocketMQMessageListener(topic = GrowMqConstant.TOPIC, consumerGroup = "grow-example-spring-boot", selectorExpression = GrowMqConstant.KEY)
public class GrowRocketMqListener {
}
//        implements RocketMQListener<GrowMqValueDTO> {
//
//    @Override
//    public void onMessage(GrowMqValueDTO message) {
//        log.info("grow mq listener, data: [{}]", message);
//    }
//
//}
