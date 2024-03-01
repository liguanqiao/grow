package com.liguanqiao.grow.example.core.log.task;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.liguanqiao.grow.example.core.log.async.GrowLogSpringAsyncEvent;
import com.liguanqiao.grow.example.core.mq.service.GrowMqService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 日志 Spring Schedule 测试
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
@Slf4j
//@Component
@AllArgsConstructor
public class GrowLogSpringScheduledTask {

    private final GrowMqService growMqService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(cron = "0/5 * * * * ? ")
    public void handle() {
        String str = StrUtil.format("日志 Spring Schedule 测试, random string: [{}]", RandomUtil.randomString(10));
        log.info(str);
        growMqService.send(str);
        growMqService.send(str);
        applicationEventPublisher.publishEvent(GrowLogSpringAsyncEvent.of(str));
    }

}
