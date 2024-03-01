package com.liguanqiao.grow.example.core.mq.service;

/**
 * @author liguanqiao
 * @since 2023/1/10
 **/
public interface GrowMqService {

    void send();

    void send(String str);

}
