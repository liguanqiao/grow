package com.liguanqiao.grow.core.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 2参数转换
 *
 * @param <T>  目标   类型
 * @param <S1> 参数一 类型
 * @param <S2> 参数二 类型
 * @author liguanqiao
 * @since 2023/2/15
 **/
@FunctionalInterface
public interface Converter2<S1, S2, T> {

    T convert(S1 s1, S2 s2);

    default Optional<T> safeConvert(S1 s1, S2 s2) {
        return Optional.ofNullable(convert(s1, s2));
    }

    default List<T> convertList(Collection<S1> s1List, S2 s2) {
        if (s1List == null) {
            return Collections.emptyList();
        }
        return s1List.stream().map(s1 -> convert(s1, s2)).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    default List<T> convertList(S1 s1, Collection<S2> s2List) {
        if (s2List == null) {
            return Collections.emptyList();
        }
        return s2List.stream().map(s2 -> convert(s1, s2)).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
