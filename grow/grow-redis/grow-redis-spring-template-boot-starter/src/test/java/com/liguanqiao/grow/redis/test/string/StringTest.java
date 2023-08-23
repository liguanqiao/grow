package com.liguanqiao.grow.redis.test.string;

import com.liguanqiao.grow.redis.RedisOps;
import com.liguanqiao.grow.redis.test.GrowRedisTestApplication;
import com.liguanqiao.grow.redis.test.base.AbsTest;
import com.liguanqiao.grow.redis.test.bean.RedisValueBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author liguanqiao
 * @since 2023/8/15
 **/
@Slf4j
@DisplayName("Redis String 测试用例")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = GrowRedisTestApplication.class)
public class StringTest extends AbsTest {
    @Resource
    private RedisOps redisOps;

    private final static String key = "GROW_REDIS_STRING_TEST_KEY";
    private final static Class<RedisValueBean> type = RedisValueBean.class;

    @Test
    @Order(2)
    public void testSet() {
        RedisValueBean value = RedisValueBean.create();
        redisOps.set(key, value);
        printLog("string set", key, value);
    }

    @Test
    @Order(2)
    public void testGet() {
        redisOps.get(key, type)
                .ifPresent(value -> printLog("string get", key, value));
    }

}
