package com.liguanqiao.grow.json.engine.fastjson1;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.liguanqiao.grow.json.JsonDatePattern;
import com.liguanqiao.grow.json.JsonEngine;

import java.lang.reflect.Type;

/**
 * Fastjson1引擎
 *
 * @author liguanqiao
 **/
public class Fastjson1Engine implements JsonEngine, JsonDatePattern {

    private final SerializerFeature[] writerFeatures;
    private final Feature[] readerFeatures;

    public Fastjson1Engine() {
        JSON.DEFFAULT_DATE_FORMAT = DATETIME_PATTERN;
        writerFeatures = new SerializerFeature[]{
                SerializerFeature.WriteDateUseDateFormat
        };
        readerFeatures = new Feature[]{
        };
    }

    @Override
    public <T> T toBean(String json, Type type) {
        return JSON.parseObject(json, type, readerFeatures);
    }

    @Override
    public <T> T toBean(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz, readerFeatures);
    }

    @Override
    public String toJson(Object object) {
        return JSON.toJSONString(object, writerFeatures);
    }
}
