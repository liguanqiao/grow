package com.liguanqiao.grow.lock.spring.boot.autoconfigure;

import com.liguanqiao.grow.lock.LockOps;
import com.liguanqiao.grow.lock.LockOpsZookeeperImpl;
import com.liguanqiao.grow.lock.properties.LockProperties;
import com.liguanqiao.grow.lock.properties.LockZookeeperProperties;
import com.liguanqiao.grow.lock.util.ZookeeperLockRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Grow Log 自动装配
 *
 * @author liguanqiao
 * @since 2023/1/5
 **/
@Slf4j
@EnableConfigurationProperties(LockZookeeperProperties.class)
@Configuration
public class GrowLockAutoConfiguration {

    @ConditionalOnMissingBean
    @ConfigurationProperties("grow.lock")
    @Bean
    public LockProperties lockProperties() {
        return new LockProperties();
    }

    @ConditionalOnMissingBean(CuratorFramework.class)
    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curatorFramework(LockZookeeperProperties properties) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(), properties.getMaxRetries());
        return CuratorFrameworkFactory.builder()
                .connectString(properties.getZkServers())
                .sessionTimeoutMs(properties.getSessionTimeout())
                .connectionTimeoutMs(properties.getConnectionTimeout())
                .retryPolicy(retryPolicy)
                .build();
    }

    @ConditionalOnMissingBean
    @Bean
    public ZookeeperLockRegistry lockRegistry(CuratorFramework curatorFramework, LockProperties lockProperties) {
        return new ZookeeperLockRegistry(curatorFramework, lockProperties.getLockKeyPrefix());
    }

    @Order(3)
    @ConditionalOnMissingBean
    @Bean
    public LockOps lockOps(ZookeeperLockRegistry lockRegistry) {
        log.info(">>>>>>>>>>> Grow LockOps Zookeeper Config Init.");
        return new LockOpsZookeeperImpl(lockRegistry);
    }

}
