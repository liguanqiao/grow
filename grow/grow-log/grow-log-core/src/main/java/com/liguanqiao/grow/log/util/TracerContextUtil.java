package com.liguanqiao.grow.log.util;

import com.liguanqiao.grow.log.context.TracerContext;
import com.liguanqiao.grow.log.context.TracerContextDefaultImpl;
import lombok.experimental.UtilityClass;

import java.util.Optional;

/**
 * @author liguanqiao
 * @since 2023/5/11
 **/
@UtilityClass
public class TracerContextUtil {

    public static TracerContext getOrDefault(TracerContext tracerContext) {
        return Optional.ofNullable(tracerContext)
                .orElseGet(TracerContextDefaultImpl::new);
    }

}
