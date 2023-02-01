package com.liguanqiao.grow.web.single.servlet;

import cn.hutool.core.util.StrUtil;
import com.liguanqiao.grow.json.JsonUtil;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.context.TracerContextDefaultImpl;
import com.liguanqiao.grow.log.span.TracerSpan;
import com.liguanqiao.grow.web.common.payload.BizMessage;
import com.liguanqiao.grow.web.common.payload.RespRes;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Optional;

/**
 * Web 单服务统一返回结果
 *
 * @author liguanqiao
 * @since 2023/1/13
 **/
@RestControllerAdvice
@AllArgsConstructor
public class GrowWebSingleResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final String appName;
    private final TracerContext tracerContext;

    public GrowWebSingleResponseBodyAdvice(String appName) {
        this.appName = appName;
        this.tracerContext = new TracerContextDefaultImpl();
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Boolean.TRUE;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if(body instanceof String){
            return JsonUtil.toJson(RespRes.of(BizMessage.ok(this.appName, getTraceId()), body));
        }
        if (body instanceof BizMessage) {
            return RespRes.of((BizMessage) body);
        }
        return RespRes.of(BizMessage.ok(this.appName, getTraceId()), body);
    }

    private String getTraceId() {
        return Optional.of(tracerContext)
                .map(TracerContext::currentSpan)
                .map(TracerSpan::getTraceId)
                .orElse(StrUtil.EMPTY);
    }

}
