package com.liguanqiao.grow.json.engine.hutool;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.liguanqiao.grow.json.JsonDatePattern;
import com.liguanqiao.grow.json.JsonEngine;

import java.lang.reflect.Type;

/**
 * Hutool-Json引擎
 *
 * @author liguanqiao
 **/
public class HutoolEngine implements JsonEngine, JsonDatePattern {

    private final JSONConfig jsonConfig;

    public HutoolEngine() {
        JSONConfig jsonConfig = JSONConfig.create()
                .setIgnoreError(Boolean.TRUE);
//        jsonConfig.setDateFormat(DatePattern.NORM_DATETIME_MS_PATTERN);
        this.jsonConfig = jsonConfig;
    }

    @Override
    public <T> T toBean(String json, Type type) {
        return JSONUtil.parse(json, jsonConfig).toBean(type);
    }

    @Override
    public <T> T toBean(String json, Class<T> clazz) {
        return JSONUtil.toBean(json, jsonConfig, clazz);
    }

    @Override
    public String toJson(Object object) {
        return JSONUtil.toJsonStr(object, jsonConfig);
    }
}
