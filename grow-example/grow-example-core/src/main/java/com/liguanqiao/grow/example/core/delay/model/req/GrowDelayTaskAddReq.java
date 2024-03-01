package com.liguanqiao.grow.example.core.delay.model.req;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

/**
 * @author liguanqiao
 * @since 2023/4/14
 **/
@Data
@Accessors(chain = true)
public class GrowDelayTaskAddReq {
    private String taskId;
    private String data = RandomUtil.randomString(10);
    private Long time;
    private TimeUnit timeUnit;
}
