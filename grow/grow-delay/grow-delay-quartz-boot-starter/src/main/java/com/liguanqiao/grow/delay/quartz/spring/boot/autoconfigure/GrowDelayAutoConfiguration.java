package com.liguanqiao.grow.delay.quartz.spring.boot.autoconfigure;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.delay.quartz.DelayTaskOpsQuartzImpl;
import com.liguanqiao.grow.delay.quartz.job.DelayTaskQuartzJobBean;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.context.TracerContextDefaultImpl;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

/**
 * @author liguanqiao
 * @since 2023/4/19
 **/
@Slf4j
@Configuration
public class GrowDelayAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public DelayTaskOps delayTaskOps(Scheduler scheduler, @Autowired(required = false) TracerContext tracerContext) {
        log.info(">>>>>>>>>>> Grow DelayTask Quartz Config Init.");
        return new DelayTaskOpsQuartzImpl(scheduler, Optional.ofNullable(tracerContext).orElseGet(TracerContextDefaultImpl::new));
    }

    @Bean
    @ConditionalOnMissingBean
    public Job delayTaskQuartzJob(List<DelayTaskHandler<?>> handlers, DelayTaskOps delayTaskOps, @Autowired(required = false) TracerContext tracerContext) {
        return new DelayTaskQuartzJobBean(handlers, delayTaskOps, Optional.ofNullable(tracerContext).orElseGet(TracerContextDefaultImpl::new));
    }

}
