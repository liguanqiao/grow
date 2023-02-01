package com.liguanqiao.grow.example.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author liguanqiao
 * @since 2023/1/5
 **/
@EnableScheduling
@SpringBootApplication
public class GrowExampleSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrowExampleSpringBootApplication.class);
    }

}
