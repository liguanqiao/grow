package com.liguanqiao.grow.example.spring.boot.log.task;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.liguanqiao.grow.example.spring.boot.mq.service.GrowMqService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 日志 Spring Schedule 测试
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
@Slf4j
@Component
@AllArgsConstructor
public class GrowLogSpringScheduledTask {

    private final GrowMqService growMqService;

    @Scheduled(cron = "0/5 * * * * ? ")
    public void handle() {
        String str = StrUtil.format("日志 Spring Schedule 测试, random string: [{}]", RandomUtil.randomString(10));
        log.info(str);
        growMqService.send(str);
        growMqService.send(str);
    }

}
