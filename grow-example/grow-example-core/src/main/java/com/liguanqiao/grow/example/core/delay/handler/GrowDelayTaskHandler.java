package com.liguanqiao.grow.example.core.delay.handler;

import com.liguanqiao.grow.delay.TaskInfo;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author liguanqiao
 * @since 2023/4/14
 **/
@Slf4j
@Component
public class GrowDelayTaskHandler implements DelayTaskHandler<GrowDelayTaskHandler.TaskData> {

    public final static String TOPIC = "TEST_DELAY_TASK";

    @Override
    public void execute(TaskInfo<TaskData> task) {
        log.info("TEST_DELAY_TASK handle, task: [{}]", task);
    }

    @Override
    public String getTopic() {
        return TOPIC;
    }

    @Override
    public int getRetry() {
        return 0;
    }

    @Override
    public void error(TaskInfo<TaskData> task, Exception ex) {
        log.error("TEST_DELAY_TASK handle error, task: [{}]", task, ex);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TaskData implements Serializable {
        private String str;
    }

}
