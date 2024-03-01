package com.liguanqiao.grow.example.core.mq.listeners;

import com.liguanqiao.grow.example.core.mq.model.dto.GrowMqValueDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liguanqiao
 * @since 2023/6/30
 **/
@Slf4j
@Component
public class GrowKafkaListener {
//    @KafkaListener(topics = GrowMqConstant.TOPIC)
    public void onMessage(GrowMqValueDTO dto) {
        log.info("grow mq listener, data: [{}]", dto);
    }
}
