package com.liguanqiao.grow.log.sleuth.task;

import brave.Tracer;
import com.liguanqiao.grow.log.sleuth.utils.SleuthTracerUtil;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * XXL-Job 的Handler切面
 *
 * @author liguanqiao
 * @since 2023/1/12
 **/
@Aspect
@AllArgsConstructor
public class LogTaskXxlJobAop {

    private final Tracer tracer;

    @Pointcut("@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
    public void cut() {
    }

    @Around("cut()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        SleuthTracerUtil.join(tracer);
        return jp.proceed();
    }

}
