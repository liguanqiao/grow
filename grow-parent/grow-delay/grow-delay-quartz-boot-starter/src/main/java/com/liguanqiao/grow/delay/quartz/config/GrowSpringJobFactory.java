package com.liguanqiao.grow.delay.quartz.config;

import lombok.AllArgsConstructor;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**
 * 通过实现JobFactory接口来实现让Quartz框架优先去取Spring beans，而不是每次都new一个新的实例。
 *
 * @author liguanqiao
 * @since 2023/5/11
 **/
@AllArgsConstructor
@SuppressWarnings({"NullableProblems"})
public class GrowSpringJobFactory extends AdaptableJobFactory {

    private final ApplicationContext applicationContext;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = null;
        try {
            jobInstance = applicationContext.getBean(bundle.getJobDetail().getJobClass());
        } catch (BeansException e) {
            jobInstance = super.createJobInstance(bundle);
        }
        return jobInstance;
    }
}