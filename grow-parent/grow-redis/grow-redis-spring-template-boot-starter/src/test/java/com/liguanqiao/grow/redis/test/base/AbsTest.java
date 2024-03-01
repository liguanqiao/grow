package com.liguanqiao.grow.redis.test.base;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liguanqiao
 * @since 2023/8/15
 **/
@Slf4j
public class AbsTest {

    protected void printLog(String method, String key, Object value) {
        log.info("Redis Test, Method: [{}], Key: [{}], Value:[{}]", method, key, value);
    }

}
