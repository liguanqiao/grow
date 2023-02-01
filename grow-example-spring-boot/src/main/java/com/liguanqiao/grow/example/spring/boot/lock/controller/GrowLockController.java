package com.liguanqiao.grow.example.spring.boot.lock.controller;

import com.liguanqiao.grow.example.spring.boot.mq.service.GrowMqService;
import com.liguanqiao.grow.lock.LockOps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liguanqiao
 * @since 2023/1/5
 **/
@Slf4j
@RestController
@RequestMapping("/growLock")
@AllArgsConstructor
public class GrowLockController {

    private final LockOps lockOps;
    private final GrowMqService growMqService;

    @GetMapping("/tryLock")
    public void lock(@RequestParam String key) {
        lockOps.tryLockWithoutResult(key, () -> growMqService.send(key));
    }

}
