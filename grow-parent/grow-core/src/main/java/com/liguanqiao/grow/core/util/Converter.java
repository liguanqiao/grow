package com.liguanqiao.grow.core.util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 类型转换
 *
 * @param <S> 源对象
 * @param <T> 目标对象
 */
@FunctionalInterface
public interface Converter<S, T> {

    T convert(S s);

    default Optional<T> safeConvert(S s) {
        return Optional.ofNullable(convert(s));
    }

    default List<T> convertList(Collection<S> s, Predicate<T> filter) {
        if (s == null) {
            return Collections.emptyList();
        }
        return s.stream().map(this::convert).filter(Objects::nonNull)
                .filter(filter)
                .collect(Collectors.toList());
    }

    default List<T> convertList(Collection<S> s, Predicate<T> filter, Comparator<T> comparator) {
        if (s == null) {
            return Collections.emptyList();
        }
        return s.stream().map(this::convert).filter(Objects::nonNull)
                .filter(filter)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    default List<T> convertList(Collection<S> s, Comparator<T> comparator) {
        if (s == null) {
            return Collections.emptyList();
        }
        return s.stream().map(this::convert).filter(Objects::nonNull)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    default List<T> convertList(Collection<S> s) {
        if (s == null) {
            return Collections.emptyList();
        }
        return s.stream().map(this::convert).filter(Objects::nonNull).collect(Collectors.toList());
    }

    default Set<T> convertSet(Collection<S> s) {
        if (s == null) {
            return Collections.emptySet();
        }
        return s.stream().map(this::convert).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
