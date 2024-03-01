package com.liguanqiao.grow.lock;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.locks.LockRegistry;

import java.util.concurrent.locks.Lock;

/**
 * LockOps Spring 实现
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
@Slf4j
@AllArgsConstructor
public class LockOpsIntegrationImpl implements LockOps {

    private final LockRegistry lockRegistry;

    @Override
    public Lock obtain(String lockKey) {
        return lockRegistry.obtain(lockKey);
    }

}
