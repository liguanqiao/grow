package com.liguanqiao.grow.example.spring.cloud.handler;

import cn.hutool.core.util.StrUtil;
import com.liguanqiao.grow.json.JsonUtil;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.span.TracerSpan;
import com.liguanqiao.grow.web.common.error.CommonErrorCode;
import com.liguanqiao.grow.web.common.payload.BizMessage;
import com.liguanqiao.grow.web.common.payload.RespRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

/**
 * 网关统一异常处理
 *
 * @author ruoyi
 */
@Slf4j
@Configuration
@SuppressWarnings({"NullableProblems"})
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
    @Value("${spring.application.name}")
    public String appName;
    @Autowired(required = false)
    public TracerContext tracerContext;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }

        BizMessage bizMsg;

        String tracerId = Optional.ofNullable(tracerContext)
                .map(TracerContext::currentSpan)
                .map(TracerSpan::getTraceId)
                .orElse(StrUtil.EMPTY);

        if (ex instanceof NotFoundException) {
            bizMsg = BizMessage.of(CommonErrorCode.NOT_FOUND, appName, tracerId);
            response.setStatusCode(HttpStatus.NOT_FOUND);
        } else {
            bizMsg = BizMessage.of(CommonErrorCode.SERVER_EXCEPTION, appName, tracerId);
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.error("[网关异常处理]请求路径:{},异常信息:{}", exchange.getRequest().getPath(), ex.getMessage(), ex);
        byte[] bytes = JsonUtil.toJson(RespRes.of(bizMsg)).getBytes(StandardCharsets.UTF_8);
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().setDate(System.currentTimeMillis());
        response.getHeaders().put("transfer-encoding", Collections.singletonList("chunked"));
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}
