## 简介

Grow ORM 是WEB框架脚手架。

目前支持实现方式：

- [Spring-Boot](https://github.com/spring-projects/spring-boot)

-------------------------------------------------------------------------------

## 单服务使用

1. 引入相关依赖。

```xml
<dependencies>
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-mybatis-plus-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
</dependencies>
```

2. 业务异常码

[ErrorCode.java](grow-web-common/src/main/java/com/liguanqiao/grow/web/common/error/ErrorCode.java)

```java
@Getter
@AllArgsConstructor
public enum GrowErrorCode implements ErrorCode {

    TEST(100000, "测试异常"),
    ;

    private final int code;
    private final String note;
}
```

3. 业务异常类

[BizException.java](grow-web-common/src/main/java/com/liguanqiao/grow/web/common/error/BizException.java)

```java
@RestController
@RequestMapping("/growError")
@AllArgsConstructor
public class GrowErrorController {

    @GetMapping("/test")
    public void test() {
        throw new BizException(GrowErrorCode.TEST);
    }

}
```

4. 统一响应格式

```json
{
  "bizMsg": {
    //业务信息
    "code": 0,
    //错误码
    "note": "success",
    //描述
    "origin": "grow-example-spring-boot",
    //源头服务
    "traceId": "47b7c6d5ed8cf7c0"
    //traceId
  },
  "dataMsg": {
    //数据信息
    "str": "pe4677qnu6yy3eo9",
    "lon": -3967645963676843182,
    "dou": 0.6584294946317961,
    "bigDecimal": 0.5037359737632943,
    "localDateTime": "2023-01-15T14:21:38.36",
    "localDate": "2023-01-15",
    "localTime": "14:21:38",
    "date": "2023-01-15 14:21:38.360"
  }
}
```

5. Controller Demo

```java
@RestController
@RequestMapping("/growRedis")
@AllArgsConstructor
public class GrowRedisController {

    private final GrowRedisService growRedisService;

    @GetMapping("/getValue")
    public GrowRedisResp getValue(@RequestParam String key) {
        log.info("getValue");
        return growRedisService.getValue(key);
    }
}
```