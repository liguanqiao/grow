package com.liguanqiao.grow.example.core.log.async;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author liguanqiao
 * @since 2023/4/13
 **/
@Slf4j
@Component
@AllArgsConstructor
public class GrowLogSpringAsync {
    @Async("chatGptAsyncTaskExecutor")
    @EventListener
    public void handle(GrowLogSpringAsyncEvent e) {
        log.info("日志 Spring Async 测试, random string: [{}]", e.getStr());
    }

}
