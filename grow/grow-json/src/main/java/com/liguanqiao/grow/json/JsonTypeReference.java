package com.liguanqiao.grow.json;

import cn.hutool.core.util.TypeUtil;

import java.lang.reflect.Type;

/**
 * @author liguanqiao
 * @since 2023/1/6
 **/
public abstract class JsonTypeReference<T> {
    protected final Type type;
    private final int hashCode;

    public JsonTypeReference() {
        this.type = TypeUtil.getTypeArgument(getClass());
        this.hashCode = type.hashCode();
    }

    public JsonTypeReference(Type type) {
        this.type = type;
        this.hashCode = type.hashCode();
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
