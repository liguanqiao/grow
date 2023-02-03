## 简介

Grow Redis 是Redis Client封装模块，其提供了多种不同的支持以满足不同性能和环境的需求。

目前支持实现方式：

- [redis-template](https://github.com/spring-projects/spring-data-redis)
- [redisson](https://github.com/redisson/redisson)

-------------------------------------------------------------------------------

## 如何使用

1. 引入相关依赖(根据需求选择)。

```xml
<dependencies>
    <!--若使用RedisTemplate作为客户端底层，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-redis-spring-template-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
    <!--若使用Redisson作为客户端底层，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-redis-redisson-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
</dependencies>
```

2.  简单使用。

```java
@AllArgsConstructor
public class GrowRedisService {

    private final RedisOps redisOps;

    @Override
    public GrowRedisResp getValue(String key) {
        return redisOps.get(key, GrowRedisResp.class)
                .orElse(null);
    }

    @Override
    public void setValue(String key, GrowRedisValueDTO value) {
        redisOps.set(key, value);
    }
}
```

3.  更多使用方式

[RedisOps.java](grow-redis-core/src/main/java/com/liguanqiao/grow/redis/RedisOps.java)