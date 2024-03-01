package com.liguanqiao.grow.lock;

import com.liguanqiao.grow.lock.properties.LockProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;

import java.util.concurrent.locks.Lock;

/**
 * LockOps Redisson 实现
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
@Slf4j
@AllArgsConstructor
public class LockOpsRedissonImpl implements LockOps {

    private final RedissonClient redissonClient;
    private final LockProperties lockProperties;

    @Override
    public Lock obtain(String lockKey) {
        return redissonClient.getLock(constructLockKey(lockKey));
    }

    private String constructLockKey(String path) {
        return lockProperties.getLockKeyPrefix() + ":" + path;
    }

}
