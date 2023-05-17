package com.liguanqiao.grow.example.spring.cloud.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liguanqiao
 * @since 2023/5/5
 **/
@EnableDiscoveryClient
@SpringBootApplication
public class GrowUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrowUserApplication.class);
    }

}
