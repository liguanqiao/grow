package com.liguanqiao.grow.lock.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 分布式锁配置
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
@Getter
@Setter
public class LockProperties {
    /**
     * 锁key前缀
     */
    private String lockKeyPrefix = "GROW-LOCK";
}
