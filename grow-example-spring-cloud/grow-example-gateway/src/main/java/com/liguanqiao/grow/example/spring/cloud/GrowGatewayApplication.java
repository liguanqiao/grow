package com.liguanqiao.grow.example.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.zalando.logbook.servlet.LogbookFilter;

/**
 * @author liguanqiao
 * @since 2023/5/5
 **/
@EnableDiscoveryClient
@SpringBootApplication
public class GrowGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrowGatewayApplication.class);
    }

}
