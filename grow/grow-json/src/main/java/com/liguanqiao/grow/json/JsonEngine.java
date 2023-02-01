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
     * @param json    json
     * @param typeOfT type reference
     **/
    default <T> T toBean(String json, JsonTypeReference<T> typeOfT){
        return toBean(json,typeOfT.getType());
    }

    /**
     * json to bean
     *
     * @param json json
     * @param type type
     **/
    <T> T toBean(String json, Type type);

    /**
     * json to bean
     *
     * @param json  json
     * @param clazz class
     **/
    <T> T toBean(String json, Class<T> clazz);

    /**
     * bean to json
     *
     * @param object bean
     **/
    String toJson(Object object);
}
