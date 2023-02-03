## 简介

Grow Json，针对常用JSON库做了统一接口封装，既定义一套规范，隔离各个库的差异，做到一段代码，随意更换。

采用SPI服务发现机制实现，根据用户引入的Json库的jar来自动选择用哪个库实现。

现在封装的引擎有(权重按以下排序)：

- [Jackson](https://github.com/FasterXML/jackson)
- [Gson](https://github.com/google/gson)
- [Fastjson](https://github.com/alibaba/fastjson)
- [Hutool-Json](https://github.com/dromara/hutool)

-------------------------------------------------------------------------------

## 支持JSON库版本范围

| Json库    | 支持版本      |
|----------|-----------|
| Jackson  | [2.11.0,) |
| Gson     | [2.10,)   |
| Fastjson | [1.2.83,) |
| Gson     | [5.8.11,) |

-------------------------------------------------------------------------------

## 如何使用

1. 引入相关依赖。

```xml
<dependencies>
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-json</artifactId>
        <version>${latest.version}</version>
    </dependency>
</dependencies>
```

2.  简单使用。

```java
//bean to json
Bean bean = new Bean();
String json = JsonUtil.toJson(bean);

//json to bean
String json = "...";
Bean bean = JsonUtil.toBean(json, JsonBean.class);

//json to generics bean
String json = "...";
List<Bean> genericsBean = JsonUtil.toBean(json, new JsonTypeReference<List<Bean>>() {});
```