package com.liguanqiao.grow.json.engine.gson;

import com.google.gson.*;
import com.liguanqiao.grow.json.JsonDatePattern;
import com.liguanqiao.grow.json.JsonEngine;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Gson引擎
 *
 * @author liguanqiao
 **/
public class GsonEngine implements JsonEngine, JsonDatePattern {

    private final Gson gson;

    public GsonEngine() {
        gson = new GsonBuilder()
                //时间格式化
                .setDateFormat(DATETIME_PATTERN)
                //Java8 时间格式化
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DATETIME_FORMATTER)))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DATE_FORMATTER)))
                .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(TIME_FORMATTER)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DATETIME_FORMATTER))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> LocalDate.parse(json.getAsJsonPrimitive().getAsString(), DATE_FORMATTER))
                .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, typeOfT, context) -> LocalTime.parse(json.getAsJsonPrimitive().getAsString(), TIME_FORMATTER))
                .create();
    }

    @Override
    public <T> T toBean(String json, Type type) {
        return gson.fromJson(json, type);
    }

    @Override
    public <T> T toBean(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    @Override
    public String toJson(Object object) {
        return gson.toJson(object);
    }
}
