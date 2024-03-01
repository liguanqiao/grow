package com.liguanqiao.grow.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author liguanqiao
 * @since 2023/1/5
 **/
@EnableScheduling
@SpringBootApplication
public class GrowExampleSpringBoot2_3_XApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrowExampleSpringBoot2_3_XApplication.class);
    }

}
