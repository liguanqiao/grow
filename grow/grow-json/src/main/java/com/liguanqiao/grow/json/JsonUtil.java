package com.liguanqiao.grow.json;

import com.liguanqiao.grow.json.engine.JsonFactory;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * JsonUtil
 *
 * @author liguanqiao
 **/
public final class JsonUtil {

    public static JsonEngine jsonEngine;

    /**
     * json to bean
     *
     * @param json    json
     * @param typeOfT type reference
     **/
    public static <T> T toBean(String json, JsonTypeReference<T> typeOfT) {
        lazyLoading();
        return jsonEngine.toBean(json, typeOfT);
    }

    /**
     * json to bean
     *
     * @param json json
     * @param type type
     **/
    public static <T> T toBean(String json, Type type) {
        lazyLoading();
        return jsonEngine.toBean(json, type);
    }

    /**
     * json to bean
     *
     * @param json  json
     * @param clazz class
     **/
    public static <T> T toBean(String json, Class<T> clazz) {
        lazyLoading();
        return jsonEngine.toBean(json, clazz);
    }

    /**
     * bean to json
     *
     * @param object bean
     **/
    public static String toJson(Object object) {
        lazyLoading();
        return jsonEngine.toJson(object);
    }

    /**
     * lazy loading
     **/
    private static void lazyLoading() {
        if (Objects.isNull(jsonEngine)) {
            jsonEngine = JsonFactory.create();
        }
    }
}
