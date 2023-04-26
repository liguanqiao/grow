package com.liguanqiao.grow.delay.quartz;

import cn.hutool.core.date.DateUtil;
import com.liguanqiao.grow.delay.DelayTaskOps;
import com.liguanqiao.grow.delay.TaskInfo;
import com.liguanqiao.grow.delay.error.DelayTaskException;
import com.liguanqiao.grow.delay.quartz.job.DelayTaskQuartzJobBean;
import com.liguanqiao.grow.json.JsonUtil;
import com.liguanqiao.grow.log.context.TracerContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author liguanqiao
 * @since 2023/3/30
 **/
@Slf4j
@AllArgsConstructor
public class DelayTaskOpsQuartzImpl implements DelayTaskOps {

    private final Scheduler scheduler;
    private final TracerContext tracerContext;

    @Override
    public <T> void add(String topic, TaskInfo<T> task, long time, TimeUnit timeUnit) {
        try {
            log.debug("Delay Task Add, Topic: [{}], TaskInfo: [{}]", topic, task);
            String messageId = task.getId();
            JobDetail jobDetail = JobBuilder.newJob(DelayTaskQuartzJobBean.class)
                    .withIdentity(messageId, topic)
                    .usingJobData(DelayTaskQuartzJobBean.DATA_KEY, JsonUtil.toJson(task.getData()))
                    .usingJobData(DelayTaskQuartzJobBean.RETRY_KEY, task.getRetry())
                    .usingJobData(DelayTaskQuartzJobBean.TRACER_KEY, Optional.ofNullable(tracerContext.nextSpan()).map(JsonUtil::toJson).orElse(null))
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(messageId, topic)
                    .startAt(DateUtil.offsetMillisecond(DateUtil.date(), (int) timeUnit.toMillis(time)))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException ex) {
            log.error("delay task add error", ex);
            throw new DelayTaskException("delay task add error", ex);
        }
    }

    @Override
    public boolean del(String topic, String taskId) {
        try {
            JobKey jobKey = JobKey.jobKey(taskId, topic);
            return scheduler.deleteJob(jobKey);
        } catch (SchedulerException ex) {
            log.error("delay task delete error", ex);
            throw new DelayTaskException("delay task delete error", ex);
        }
    }

}
