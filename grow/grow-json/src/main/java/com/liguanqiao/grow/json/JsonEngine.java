package com.liguanqiao.grow.json;

import java.lang.reflect.Type;

/**
 * Json引擎
 *
 * @author liguanqiao
 **/
public interface JsonEngine {

    /**
     * json to bean
     *
     * @param <T>     bean type
     * @param json    json
     * @param typeOfT type reference
     * @return bean
     **/
    default <T> T toBean(String json, JsonTypeReference<T> typeOfT) {
        return toBean(json, typeOfT.getType());
    }

    /**
     * json to bean
     *
     * @param <T>  bean type
     * @param json json
     * @param type type
     * @return bean
     **/
    <T> T toBean(String json, Type type);

    /**
     * json to bean
     *
     * @param <T>   bean type
     * @param json  json
     * @param clazz class
     * @return bean
     **/
    <T> T toBean(String json, Class<T> clazz);

    /**
     * bean to json
     *
     * @param object bean
     * @return json string
     **/
    String toJson(Object object);
}
