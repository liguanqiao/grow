package com.liguanqiao.grow.example.core.mq.listeners;

import com.liguanqiao.grow.example.core.mq.model.dto.GrowMqValueDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Slf4j
//@Component
public class GrowActiveMqListener {

    /**
     * selector 用法：https://blog.csdn.net/xtj332/article/details/17784671
     **/
//    @JmsListener(destination = GrowMqConstant.TOPIC, selector = MqSenderActiveImpl.SELECTOR_KEY_HEADER + " = '" + GrowMqConstant.KEY + "'")
    public void onMessage(GrowMqValueDTO message) {
        log.info("grow mq listener, data: [{}]", message);
    }

}
