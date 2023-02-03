## 简介

Grow Mq 是消息队列封装模块，其提供了多种不同的支持以满足不同性能和环境的需求。

目前支持实现方式：

- [kafka](https://github.com/apache/kafka)
- [rabbitmq](https://github.com/rabbitmq/rabbitmq-server)

-------------------------------------------------------------------------------

## 如何使用

1. 引入相关依赖(根据需求选择)。

```xml
<dependencies>
    <!--若使用kafka作为消息队列，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-mq-kafka-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
    <!--若使用rabbitmq作为消息队列，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-mq-rabbit-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
</dependencies>
```

2.  统一消息生产者。

```java
@AllArgsConstructor
public class GrowMqService {

    private final MqSender mqSender;

    @Override
    public void send() {
        mqSender.send(GrowMqConstant.TOPIC, GrowMqValueDTO.create());
    }

    @Override
    public void send(String str) {
        mqSender.send(GrowMqConstant.TOPIC, GrowMqValueDTO.create().setStr(str));
    }

}
```

3.  RabbitMQ消费者

```java
@Component
@RabbitListener(queuesToDeclare = @Queue(GrowMqConstant.TOPIC))
public class GrowMqListener {
    @RabbitHandler
    public void handle(GrowMqValueDTO dto) {
        log.info("grow mq listener, data: [{}]", dto);
    }
}
```

4.  Kafka消费者

```java
@Component
public class GrowMqListener {
    @EventListener
    public void handle(GrowMqValueDTO dto) {
        log.info("grow mq listener, data: [{}]", dto);
    }
}
```