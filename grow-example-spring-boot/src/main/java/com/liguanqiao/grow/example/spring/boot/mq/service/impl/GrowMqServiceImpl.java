package com.liguanqiao.grow.example.spring.boot.mq.service.impl;

import com.liguanqiao.grow.example.spring.boot.mq.constant.GrowMqConstant;
import com.liguanqiao.grow.example.spring.boot.mq.model.dto.GrowMqValueDTO;
import com.liguanqiao.grow.example.spring.boot.mq.service.GrowMqService;
import com.liguanqiao.grow.mq.MqSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author liguanqiao
 * @since 2023/1/10
 **/
@Slf4j
@Service
@AllArgsConstructor
public class GrowMqServiceImpl implements GrowMqService {

    private final MqSender mqSender;

    @Override
    public void send() {
        mqSender.send(GrowMqConstant.TOPIC, GrowMqValueDTO.create());
    }

    @Override
    public void send(String str) {
        mqSender.send(GrowMqConstant.TOPIC, GrowMqValueDTO.create().setStr(str));
    }

}
