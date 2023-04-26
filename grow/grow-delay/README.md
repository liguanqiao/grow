## 简介

Grow Delay 是一个分布式延迟任务模块，其提供了多种不同的支持以满足不同性能和环境的需求。

目前支持实现方式：

- [quartz](https://github.com/quartz-scheduler/quartz)
- [rabbitmq-delayed-message-exchange](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange): [教程](https://liguanqiao.com/archives/1682477876522)
- [redission](https://github.com/redisson/redisson)

-------------------------------------------------------------------------------

## 如何使用

1. 引入相关依赖(根据需求选择)。

```xml
<dependencies>
    <!--若使用quartz作为分布式延迟任务底层，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-delay-quartz-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
    <!--若使用rabbitmq-delayed-message-exchange作为分布式延迟任务底层，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-delay-rabbitmq-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
    <!--若使用redisson作为分布式延迟任务底层，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-delay-redisson-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
</dependencies>
```

2.  延迟任务触发处理。

```java
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
```

3.  创建/删除延迟任务

```java
@Component
@AllArgsConstructor
public class GrowDelayTaskDemo {

    private final DelayTaskOps delayTaskOps;
     
    public String add(String str) {
        //创建延迟10秒后执行的任务
        String taskId = delayTaskOps.add(
                GrowDelayTaskHandler.TOPIC,
                GrowDelayTaskHandler.TaskData.builder()
                        .str(str)
                        .build(),
                10L,
                TimeUnit.SECONDS
            );
    }

    public void del(String taskId) {
        //删除延迟任务
        delayTaskOps.del(GrowDelayTaskHandler.TOPIC, taskId);
    }
}
```