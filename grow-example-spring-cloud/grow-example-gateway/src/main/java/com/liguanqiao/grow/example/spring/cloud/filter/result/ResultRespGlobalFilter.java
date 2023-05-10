package com.liguanqiao.grow.example.spring.cloud.filter.result;

import com.liguanqiao.grow.json.JsonUtil;
import com.liguanqiao.grow.web.common.payload.BizMessage;
import com.liguanqiao.grow.web.common.payload.RespRes;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.xml.ws.ResponseWrapper;
import java.nio.charset.StandardCharsets;

/**
 * @author liguanqiao
 * @since 2023/5/5
 **/
//@Component
public class ResultRespGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//            ServerHttpResponse response = exchange.getResponse();
//            HttpHeaders headers = response.getHeaders();
//            MediaType mediaType = headers.getContentType();
//
//            if (mediaType != null && mediaType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
//                DataBufferFactory bufferFactory = response.bufferFactory();
//                DataBuffer dataBuffer = bufferFactory.allocateBuffer();
//                byte[] content = new byte[dataBuffer.readableByteCount()];
//                dataBuffer.read(content);
//                DataBufferUtils.release(dataBuffer);
//                String originalBody = new String(content, StandardCharsets.UTF_8);
//                Object data = JsonUtil.toBean(originalBody, Object.class);
//                // 将原始响应体转换为统一响应实体
//                RespRes<Object> wrapper = RespRes.of(BizMessage.ok(null,null),data);
//                byte[] newBody = JsonUtil.toJson(wrapper).getBytes(StandardCharsets.UTF_8);
//                response.getHeaders().setContentLength(newBody.length);
//                response.writeWith(Mono.just(bufferFactory.wrap(newBody)));
//            }
//        }));
//        exchange.getResponse().writeWith(dataBuf ->{
//            dataBuf.
//        })
        return chain.filter(exchange).then();
    }

}
