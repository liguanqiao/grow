package com.liguanqiao.grow.example.spring.boot.delay.controller;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.TaskInfo;
import com.liguanqiao.grow.example.spring.boot.delay.handler.GrowDelayTaskHandler;
import com.liguanqiao.grow.example.spring.boot.delay.model.req.GrowDelayTaskAddReq;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author liguanqiao
 * @since 2023/4/14
 **/
@Slf4j
@RestController
@RequestMapping("/growDelay")
@AllArgsConstructor
public class GrowDelayTaskController {

    private final DelayTaskOps delayTaskOps;

    @PostMapping("/add")
    public String add(@RequestBody GrowDelayTaskAddReq param) {
        if (param.getTaskId() == null) {
            return delayTaskOps.add(
                    GrowDelayTaskHandler.TOPIC,
                    GrowDelayTaskHandler.TaskData.builder()
                            .str(param.getData())
                            .build(),
                    param.getTime(),
                    param.getTimeUnit()
            );
        } else {
            delayTaskOps.add(
                    GrowDelayTaskHandler.TOPIC,
                    new TaskInfo<>()
                            .setId(param.getTaskId())
                            .setData(GrowDelayTaskHandler.TaskData.builder()
                                    .str(param.getData())
                                    .build()),
                    param.getTime(),
                    param.getTimeUnit()
            );
            return param.getTaskId();
        }
    }

    @PostMapping("/del")
    public void del(@RequestParam String taskId) {
        delayTaskOps.del(GrowDelayTaskHandler.TOPIC, taskId);
    }
}
