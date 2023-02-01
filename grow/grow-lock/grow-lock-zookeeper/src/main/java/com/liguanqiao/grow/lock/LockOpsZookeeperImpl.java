package com.liguanqiao.grow.lock;

import com.liguanqiao.grow.lock.util.ZookeeperLockRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;

/**
 * LockOps Redisson 实现
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
@Slf4j
@AllArgsConstructor
public class LockOpsZookeeperImpl implements LockOps {

    private final ZookeeperLockRegistry lockRegistry;

    @Override
    public Lock obtain(String lockKey) {
        return lockRegistry.obtain(lockKey);
    }

}
