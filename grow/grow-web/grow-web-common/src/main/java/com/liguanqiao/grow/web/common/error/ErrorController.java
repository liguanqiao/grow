package com.liguanqiao.grow.web.common.error;

import cn.hutool.core.util.StrUtil;
import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.context.TracerContextDefaultImpl;
import com.liguanqiao.grow.log.span.TracerSpan;
import com.liguanqiao.grow.web.common.payload.BizMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.naming.ServiceUnavailableException;
import javax.xml.bind.ValidationException;
import java.util.Optional;

/**
 * @author liguanqiao
 * @since 2022/12/4
 **/
@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class ErrorController {

    //    @Value("${spring.application.name}")
    private final String appName;

    //    @Autowired
    private final TracerContext tracerContext;

    public ErrorController(String appName) {
        this.appName = appName;
        this.tracerContext = new TracerContextDefaultImpl();
    }

    /**
     * 503异常
     */
    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public BizMessage handleAuthenticationException(ServiceUnavailableException ex) {
        return BizMessage.of(CommonErrorCode.SERVER_EXCEPTION, appName, getTraceId());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BizMessage mediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        return BizMessage.of(CommonErrorCode.SERVER_EXCEPTION, appName, getTraceId());
    }

    /**
     * 404错误
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BizMessage handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return BizMessage.of(CommonErrorCode.NOT_FOUND, appName, getTraceId());
    }

    /**
     * 其他系统非预期异常，发送消息
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BizMessage handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return BizMessage.of(CommonErrorCode.SERVER_EXCEPTION, appName, getTraceId());
    }

    /**
     * 405错误
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public BizMessage handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("405错误", ex);
        return BizMessage.of(CommonErrorCode.METHOD_NOT_ALLOW, appName, getTraceId());
    }


    /**
     * 业务异常
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BizMessage handleBusinessException(BizException ex) {
        return BizMessage.of(ex, appName, getTraceId());
    }

    /**
     * 参数异常
     */
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            ValidationException.class,
            TypeMismatchException.class,
            HttpMessageConversionException.class,
            ServletRequestBindingException.class,
            InvalidPropertyException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BizMessage handleWrongParamsException(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return BizMessage.of(CommonErrorCode.WRONG_PARAMS, appName, getTraceId());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BizMessage handleBindException(BindException ex) {
        log.warn(ex.getMessage(), ex);
        String note = Optional.of(ex)
                .map(BindException::getFieldError)
                .map(error -> String.format("%s%s", error.getField(), error.getDefaultMessage()))
                .orElseGet(CommonErrorCode.WRONG_PARAMS::getNote);
        return BizMessage.of(CommonErrorCode.WRONG_PARAMS.getCode(), note, appName, getTraceId());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BizMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn(ex.getMessage(), ex);
        String note = Optional.of(ex)
                .map(MethodArgumentNotValidException::getBindingResult)
                .map(BindingResult::getFieldError)
                .map(error -> String.format("%s%s", error.getField(), error.getDefaultMessage()))
                .orElseGet(CommonErrorCode.WRONG_PARAMS::getNote);
        return BizMessage.of(CommonErrorCode.WRONG_PARAMS.getCode(), note, appName, getTraceId());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BizMessage handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.warn(ex.getMessage(), ex);
        return BizMessage.of(CommonErrorCode.WRONG_PARAMS, appName, getTraceId());
    }


    private String getTraceId() {
        return Optional.of(tracerContext)
                .map(TracerContext::currentSpan)
                .map(TracerSpan::getTraceId)
                .orElse(StrUtil.EMPTY);
    }

}
