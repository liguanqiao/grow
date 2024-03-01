## 简介

Grow Mq 是消息队列封装模块，其提供了多种不同的支持以满足不同性能和环境的需求。

目前支持实现方式：

- [Kafka](https://github.com/apache/kafka)
- [RabbitMQ](https://github.com/rabbitmq/rabbitmq-server)
- [RocketMQ](https://github.com/apache/rocketmq)
- [ActiveMQ](https://github.com/apache/activemq)

-------------------------------------------------------------------------------

## 如何使用

1. 引入相关依赖(根据需求选择)。

```xml
<dependencies>
    <!--若使用Kafka作为消息队列，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-mq-kafka-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
    <!--若使用RabbitMQ作为消息队列，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-mq-rabbit-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
    <!--若使用RocketMQ作为消息队列，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-mq-rocket-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
    <!--若使用ActiveMQ作为消息队列，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-mq-active-boot-starter</artifactId>
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

3.  消费者
- 3.1 RabbitMQ消费者
```java
@Slf4j
@Component
@RabbitListener(queuesToDeclare = @Queue(GrowMqConstant.TOPIC))
public class GrowRabbitMqListener {
    @RabbitHandler
    public void onMessage(GrowMqValueDTO dto) {
        log.info("grow mq listener, data: [{}]", dto);
    }
}
```

- 3.2  Kafka消费者

```java
@Slf4j
@Component
public class GrowKafkaListener {
    @KafkaListener(topics = GrowMqConstant.TOPIC)
    public void onMessage(GrowMqValueDTO dto) {
        log.info("grow mq listener, data: [{}]", dto);
    }
}
```

- 3.3  RocketMQ消费者

```java
@Slf4j
@Component
@RocketMQMessageListener(topic = GrowMqConstant.TOPIC, consumerGroup = "grow-example-spring-boot", selectorExpression = GrowMqConstant.KEY)
public class GrowRocketMqListener implements RocketMQListener<GrowMqValueDTO> {
    @Override
    public void onMessage(GrowMqValueDTO message) {
        log.info("grow mq listener, data: [{}]", message);
    }
}
```

- 3.4  ActiveMQ消费者
> selector 用法：https://blog.csdn.net/xtj332/article/details/17784671
```java
@Slf4j
@Component
public class GrowActiveMqListener {
    @JmsListener(destination = GrowMqConstant.TOPIC, selector = MqSenderActiveImpl.SELECTOR_KEY_HEADER + " = '" + GrowMqConstant.KEY + "'")
    public void onMessage(GrowMqValueDTO message) {
        log.info("grow mq listener, data: [{}]", message);
    }
}
```