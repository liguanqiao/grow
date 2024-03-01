package com.liguanqiao.grow.lock.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Lock Zookeeper 配置
 *
 * @author liguanqiao
 * @since 2023/1/13
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.coordinate.zookeeper")
public class LockZookeeperProperties {

    private String zkServers;

    private int sessionTimeout = 30000;

    private int connectionTimeout = 5000;

    private int baseSleepTimeMs = 1000;

    private int maxRetries = 3;
}
