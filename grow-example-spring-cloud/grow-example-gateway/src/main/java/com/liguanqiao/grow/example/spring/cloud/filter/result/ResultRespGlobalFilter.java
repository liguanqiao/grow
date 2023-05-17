package com.liguanqiao.grow.example.spring.cloud.filter.result;

import cn.hutool.core.util.StrUtil;
import com.liguanqiao.grow.log.context.TracerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

/**
 * @author liguanqiao
 * @since 2023/5/5
 **/
@Slf4j
@Component
public class ResultRespGlobalFilter implements GlobalFilter, Ordered {

    @Autowired(required = false)
    public TracerContext tracerContext;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Optional.of(exchange.getResponse())
                .map(response -> new ResultServerHttpResponseDecorator(response, tracerContext, getAppName(exchange)))
                .map(response -> exchange.mutate().response(response).build())
                .map(chain::filter)
                .orElseGet(() -> chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return -2;
    }

    private String getAppName(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR))
                .filter(Route.class::isInstance)
                .map(Route.class::cast)
                .map(Route::getUri)
                .map(URI::getHost)
                .orElse(StrUtil.EMPTY);
    }
}
