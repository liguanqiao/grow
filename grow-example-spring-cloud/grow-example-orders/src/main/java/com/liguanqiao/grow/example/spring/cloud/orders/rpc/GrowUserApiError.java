package com.liguanqiao.grow.example.spring.cloud.orders.rpc;

import com.liguanqiao.grow.example.spring.cloud.orders.model.resp.GrowOrdersResp;
import com.liguanqiao.grow.web.common.error.BizException;
import com.liguanqiao.grow.web.common.error.CommonErrorCode;
import org.springframework.stereotype.Component;

/**
 * @author liguanqiao
 * @since 2023/5/31
 **/
@Component
public class GrowUserApiError implements GrowUserApi {
    @Override
    public GrowOrdersResp get() {
        throw new BizException(CommonErrorCode.HYSTRIX_FAIL);
    }
}
