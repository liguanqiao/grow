## 简介

Grow Lock 是一个分布式锁模块，其提供了多种不同的支持以满足不同性能和环境的需求。

目前支持实现方式：

- [spring-integration-redis](https://github.com/spring-projects/spring-integration/tree/main/spring-integration-redis)
- [redission](https://github.com/redisson/redisson)
- [zookeeper](https://github.com/apache/zookeeper)

-------------------------------------------------------------------------------

## 如何使用

1. 引入相关依赖(根据需求选择)。

```xml
<dependencies>
    <!--若使用spring-integration-redis作为分布式锁底层，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-lock-integration-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
    <!--若使用redisson作为分布式锁底层，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-lock-redisson-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
    <!--若使用zookeeper作为分布式锁底层，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-lock-zookeeper-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
</dependencies>
```

2.  普通使用。

```java
@AllArgsConstructor
public class LockDemoService {

    private final LockOps lockOps;

    public String lock(String key) {
        Lock lock = obtain(lockKey);
        try {
            if (lock.tryLock(time, unit)) {
                //TODO 业务处理
                return "";
            } else {
                throw new LockException("try lock fail");
            }
        } catch (InterruptedException e) {
            throw new LockException(e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
```

3.  编程式使用

```java
@AllArgsConstructor
public class LockDemoService {

    private final LockOps lockOps;

    public String tryLock(String key) {
        return lockOps.tryLock(key, 3L, TimeUnit.SECONDS, () -> {
            //TODO 业务处理
            return "";
        });
    }

    public void tryLockWithoutResult(String key) {
        lockOps.tryLockWithoutResult(key, 3L, TimeUnit.SECONDS, () -> {
            //TODO 业务处理
        });
    }
}
```

##  高级使用

1.  全局配置

```yml
grow:
  lock:
    lock-key-prefix: GROW-LOCK
```
