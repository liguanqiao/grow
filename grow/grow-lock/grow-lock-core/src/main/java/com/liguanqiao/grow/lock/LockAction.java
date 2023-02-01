package com.liguanqiao.grow.lock;

/**
 * 分布式锁动作
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
@FunctionalInterface
public interface LockAction {
    void handle();
}
