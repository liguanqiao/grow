package com.liguanqiao.grow.mq;

/**
 * Mq 操作接口
 *
 * @author liguanqiao
 **/
public interface MqSender {

    /**
     * message send
     *
     * @param <T>   data type
     * @param topic topic
     * @param key   key
     * @param data  data
     **/
    <T> void send(String topic, String key, T data);

    /**
     * message send
     *
     * @param <T>   data type
     * @param topic topic
     * @param data  data
     **/
    <T> void send(String topic, T data);

}
