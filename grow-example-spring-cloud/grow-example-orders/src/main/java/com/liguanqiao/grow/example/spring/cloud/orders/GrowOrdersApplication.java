package com.liguanqiao.grow.example.spring.cloud.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author liguanqiao
 * @since 2023/5/5
 **/
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class GrowOrdersApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrowOrdersApplication.class);
    }

}
