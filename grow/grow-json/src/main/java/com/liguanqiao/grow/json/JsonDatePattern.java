package com.liguanqiao.grow.json;

import cn.hutool.core.date.DatePattern;

import java.time.format.DateTimeFormatter;

/**
 * @author liguanqiao
 * @since 2023/1/6
 **/
public interface JsonDatePattern {
    String DATETIME_PATTERN = DatePattern.NORM_DATETIME_MS_PATTERN;
    DateTimeFormatter DATETIME_FORMATTER = DatePattern.NORM_DATETIME_MS_FORMATTER;

    String DATE_PATTERN = DatePattern.NORM_DATE_PATTERN;
    DateTimeFormatter DATE_FORMATTER = DatePattern.NORM_DATE_FORMATTER;

//    String TIME_PATTERN = "HH:mm:ss.SSS";
    String TIME_PATTERN = "HH:mm:ss";
    DateTimeFormatter TIME_FORMATTER = DatePattern.createFormatter(TIME_PATTERN);
}
