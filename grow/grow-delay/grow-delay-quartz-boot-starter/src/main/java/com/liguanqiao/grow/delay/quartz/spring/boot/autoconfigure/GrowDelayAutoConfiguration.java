package com.liguanqiao.grow.delay.quartz.spring.boot.autoconfigure;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.delay.quartz.DelayTaskOpsQuartzImpl;
import com.liguanqiao.grow.delay.quartz.config.GrowSpringJobFactory;
import com.liguanqiao.grow.delay.quartz.job.DelayTaskQuartzJobBean;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.util.TracerContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;

/**
 * 延迟任务自动装配
 *
 * @author liguanqiao
 * @since 2023/4/19
 **/
@Slf4j
@Configuration
public class GrowDelayAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DelayTaskOps delayTaskOps(Scheduler scheduler, @Autowired(required = false) TracerContext tracerContext) {
        log.info(">>>>>>>>>>> Grow DelayTask Quartz Config Init.");
        return new DelayTaskOpsQuartzImpl(scheduler, TracerContextUtil.getOrDefault(tracerContext));
    }

    @Bean
    public Job delayTaskQuartzJob(List<DelayTaskHandler<?>> handlers, DelayTaskOps delayTaskOps, @Autowired(required = false) TracerContext tracerContext) {
        return new DelayTaskQuartzJobBean(handlers, delayTaskOps, TracerContextUtil.getOrDefault(tracerContext));
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(ApplicationContext applicationContext) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(new GrowSpringJobFactory(applicationContext));
        return schedulerFactoryBean;
    }

}
