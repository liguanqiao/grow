# 🚀Changelog

# 0.2.0 (2023-0X-XX)

### 🐣新特性
* 【mq    】      增加对RocketMQ支持
* 【mq    】      增加对ActiveMQ支持
* 【redis 】      重构序列化代码，提供可自定义序列化方式接口

### 🐞Bug修复
* 【delay 】      delay-quartz修复增加自定义JobFactory实现导致Quartz的SpringBoot配置自动装配失效问题

# 0.1.2 (2023-05-17)

### 🐞Bug修复
* 【delay 】      delay-quartz修复增加自定义JobFactory实现导致Quartz的SpringBoot配置自动装配失效问题

# 0.1.1 (2023-05-11)

### 🐣新特性
* 【mq    】      mq-rabbit消息转换MessageConverter.class增加缓存机制

### 🐞Bug修复
* 【delay 】      delay-quartz修复没引入grow-log-boot-starter情况下报错问题
* 【delay 】      delay-quartz通过实现JobFactory接口来实现让Quartz框架优先去取Spring beans，而不是每次都new一个新的实例
* 【delay 】      delay-rabbitmq修复项目中自定义MessageConverter情况下报错问题

# 0.1.0 (2023-04-26)

### 🐣新特性
* 【delay 】      新增分布式延迟任务封装，支持quartz、rabbitmq、redisson
* 【redis 】      RedisOps增加Set的randomMember方法
* 【orm   】      entity.BaseEntity增加链式
* 【core  】      util.Converter增加2~5个入参支持

### 🐞Bug修复
* 【log   】      修复文件日志打包文件夹名称未定义问题
