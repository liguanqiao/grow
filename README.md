<p align="center">
	<a href="https://github.com/liguanqiao/grow"><img src="" width="45%"></a>
</p>
<p align="center">
	<strong>🍬A module library that can improve the efficiency of Java development.</strong>
</p>
<p align="center">
	👉 <a href="https://github.com/liguanqiao/grow">https://github.com/liguanqiao/grow</a> 👈
</p>

<p align="center">
	<a target="_blank" href="https://search.maven.org/artifact/com.liguanqiao/grow">
		<img src="https://img.shields.io/maven-central/v/com.liguanqiao/grow.svg?label=Maven%20Central" />
	</a>
	<a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" />
	</a>
    <a>
        <img src="https://img.shields.io/badge/springBoot-2.3+-green.svg" >
    </a>
</p>

-------------------------------------------------------------------------------

## 简介

Grow是一个可提高Java开发效率的模块库。

目前支持SpringBoot2.3.0~

-------------------------------------------------------------------------------

## 包含模块

| 模块                       | 介绍                                                     | 文档 |
|--------------------------|--------------------------------------------------------|--|
| grow-core                | 核心，包括Bean操作、日期、各种Util等                                 | [使用文档]((/grow/grow-core/README.md)) |
| grow-json                | JSON库(jackson、gson、fastjson等)封装，采用SPI服务发现机制            | [使用文档]((/grow/grow-json/README.md)) |
| grow-lock                | 分布式锁库封装，支持redission、spring-integration-redis、zookeeper | [使用文档]((/grow/grow-lock/README.md)) |
| grow-log                 | 分布式链路Log库封装，支持spring-cloud-sleuth、tlog                 | [使用文档]((/grow/grow-log/README.md)) |
| grow-mq                  | MqClient封装，支持kafka、rabbitmq                            | [使用文档]((/grow/grow-mq/README.md)) |
| grow-redis               | RedisClient封装，支持redisTemplate、redisson                 | [使用文档]((/grow/grow-orm/README.md)) |
| grow-orm                 | ORM框架封装，支持mybatis-plus                                 | [使用文档]((/grow/grow-redis/README.md)) |
| grow-web                 | Web脚手架，支持spring-boot、spring-cloud                      | [使用文档]((/grow/grow-web/README.md)) |
| grow-example-spring-boot | Spring Boot Web 使用范例                                   | 无 |

-------------------------------------------------------------------------------