package com.liguanqiao.grow.delay.quartz.spring.boot.autoconfigure;

import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.handler.DelayTaskHandler;
import com.liguanqiao.grow.delay.quartz.DelayTaskOpsQuartzImpl;
import com.liguanqiao.grow.delay.quartz.config.GrowSpringJobFactory;
import com.liguanqiao.grow.delay.quartz.job.DelayTaskQuartzJobBean;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.util.TracerContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;
import java.util.Map;
import java.util.Properties;

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

    /**
     * 复制于{@link QuartzAutoConfiguration#quartzScheduler}，主要更换JobFactory
     **/
    @Bean
    public SchedulerFactoryBean quartzScheduler(QuartzProperties properties, ObjectProvider<SchedulerFactoryBeanCustomizer> customizers, ObjectProvider<JobDetail> jobDetails,
                                                Map<String, Calendar> calendars, ObjectProvider<Trigger> triggers, ApplicationContext applicationContext) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        GrowSpringJobFactory jobFactory = new GrowSpringJobFactory(applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);
        if (properties.getSchedulerName() != null) {
            schedulerFactoryBean.setSchedulerName(properties.getSchedulerName());
        }

        schedulerFactoryBean.setAutoStartup(properties.isAutoStartup());
        schedulerFactoryBean.setStartupDelay((int) properties.getStartupDelay().getSeconds());
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(properties.isWaitForJobsToCompleteOnShutdown());
        schedulerFactoryBean.setOverwriteExistingJobs(properties.isOverwriteExistingJobs());
        if (!properties.getProperties().isEmpty()) {
            schedulerFactoryBean.setQuartzProperties(this.asProperties(properties.getProperties()));
        }

        schedulerFactoryBean.setJobDetails(jobDetails.orderedStream().toArray(JobDetail[]::new));
        schedulerFactoryBean.setCalendars(calendars);
        schedulerFactoryBean.setTriggers(triggers.orderedStream().toArray(Trigger[]::new));
        customizers.orderedStream().forEach((customizer) -> customizer.customize(schedulerFactoryBean));
        return schedulerFactoryBean;
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }

}
