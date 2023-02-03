## 简介

Grow ORM 是ORM框架脚手架。

目前支持实现方式：

- [Mybatis-Plus](https://github.com/baomidou/mybatis-plus)

-------------------------------------------------------------------------------

## 如何使用

1. 引入相关依赖(根据需求选择)。

```xml
<dependencies>
    <!--若使用Mybatis-Plus作为ORM框架，则需要引入-->
    <dependency>
        <groupId>com.liguanqiao</groupId>
        <artifactId>grow-mybatis-plus-boot-starter</artifactId>
        <version>${latest.version}</version>
    </dependency>
</dependencies>
```

2. 通用实体

[BaseEntity.java](grow-mybatis-plus/src/main/java/com/liguanqiao/grow/mybaits/puls/entity/BaseEntity.java)

3. 分页转换

[MybatisPlusPageConverts.java](grow-mybatis-plus/src/main/java/com/liguanqiao/grow/mybaits/puls/converts/MybatisPlusPageConverts.java)
