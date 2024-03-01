package com.liguanqiao.grow.lock.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author liguanqiao
 * @since 2023/1/13
 **/
@UtilityClass
public class ReflectionUtils {

    public static void rethrowRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        } else if (ex instanceof Error) {
            throw (Error) ex;
        } else {
            throw new UndeclaredThrowableException(ex);
        }
    }

}
