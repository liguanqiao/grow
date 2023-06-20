package com.liguanqiao.grow.example.spring.cloud.orders.rpc;

import com.liguanqiao.grow.example.spring.cloud.orders.model.resp.GrowOrdersResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liguanqiao
 * @since 2023/5/31
 **/
@FeignClient(
        value = "grow-example-user",
        fallback = GrowUserApiError.class
)
public interface GrowUserApi {

    @GetMapping("/user/get")
    GrowOrdersResp get();

}
