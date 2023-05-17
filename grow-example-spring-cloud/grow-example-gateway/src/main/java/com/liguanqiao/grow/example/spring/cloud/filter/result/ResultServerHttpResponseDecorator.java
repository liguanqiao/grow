package com.liguanqiao.grow.example.spring.cloud.filter.result;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.liguanqiao.grow.example.spring.cloud.util.DataBufferUtil;
import com.liguanqiao.grow.json.JsonUtil;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.util.TracerContextUtil;
import com.liguanqiao.grow.web.common.error.BizException;
import com.liguanqiao.grow.web.common.error.CommonErrorCode;
import com.liguanqiao.grow.web.common.payload.BizMessage;
import com.liguanqiao.grow.web.common.payload.RespRes;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author liguanqiao
 * @since 2023/5/11
 **/
@Slf4j
@SuppressWarnings({"NullableProblems"})
public class ResultServerHttpResponseDecorator extends ServerHttpResponseDecorator {

    private final TracerContext tracerContext;
    private final String appName;

    public ResultServerHttpResponseDecorator(ServerHttpResponse delegate, TracerContext tracerContext, String appName) {
        super(delegate);
        this.tracerContext = TracerContextUtil.getOrDefault(tracerContext);
        this.appName = appName;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        return Optional.of(getHeaderContentType())
                .filter(contentType -> contentType.contains(MediaType.APPLICATION_JSON_VALUE))
                .filter(__ -> shouldFilter())
                .map(__ -> Flux.from(body))
                .map(fluxBody -> fluxBody.buffer()
                        .map(this::peepOriginData)
                        .map(data -> isError() ? resolveError(data) : resolveResult(data))
                        .map(responseData -> bufferFactory().wrap(responseData.getBytes(StandardCharsets.UTF_8))))
                .map(super::writeWith)
                .orElseGet(() -> super.writeWith(body));
    }

    private String getHeaderContentType() {
        return Optional.ofNullable(getHeaders().getContentType())
                .map(MediaType::toString)
                .orElse(StrUtil.EMPTY);
    }

    private String peepOriginData(List<? extends DataBuffer> dataBuffers) {
        return dataBuffers.stream().map(this::readAllToString).collect(Collectors.joining());
    }

    private String readAllToString(DataBuffer dataBuffer) {
        try {
            return DataBufferUtil.readAllBytesToString(dataBuffer, StandardCharsets.UTF_8);
        } finally {
            DataBufferUtils.release(dataBuffer);
        }
    }

    private String resolveResult(String responseData) {
        return JsonUtil.toJson(Optional.ofNullable(responseData)
                .filter(StrUtil::isNotBlank)
                .map(data -> Opt.ofTry(() -> JsonUtil.toBean(data, Object.class)).orElse(data))
                .map(data -> RespRes.of(getBizMessage(), data))
                .orElseGet(() -> RespRes.of(getBizMessage(), null)));
    }

    private String resolveError(String responseData) {
        return JsonUtil.toJson(Optional.ofNullable(responseData)
                .filter(StrUtil::isNotBlank)
                .map(data -> JsonUtil.toBean(data, BizMessage.class))
                .map(bizMsg -> RespRes.of(bizMsg, null))
                .orElseThrow(() -> new BizException(CommonErrorCode.SERVER_EXCEPTION)));
    }

    private BizMessage getBizMessage() {
        return BizMessage.ok(appName, TracerContextUtil.getTracerId(tracerContext).orElse(StrUtil.EMPTY));
    }

    private boolean shouldFilter() {
        return Pattern.matches("[2|4|5]\\d{2}", String.valueOf(getRawStatusCode()));
    }

    private boolean isError() {
        return Pattern.matches("[4|5]\\d{2}", String.valueOf(getRawStatusCode()));
    }

}
