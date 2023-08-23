package com.liguanqiao.grow.json.test.bean;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author liguanqiao
 * @since 2023/1/9
 **/
@Data
@Accessors(chain = true)
public class JsonBean {
    private String str;
    private Long lon;
    private Double dou;
    private BigDecimal bigDecimal;
    private LocalDateTime localDateTime;
    private LocalDate localDate;
    private LocalTime localTime;
    private Date date;
    private String empty;

    public static JsonBean create() {
        return new JsonBean()
                .setStr(RandomUtil.randomString(16))
                .setLon(RandomUtil.randomLong())
                .setDou(RandomUtil.randomDouble())
                .setBigDecimal(RandomUtil.randomBigDecimal())
                .setLocalDateTime(LocalDateTime.now())
                .setLocalDate(LocalDate.now())
                .setLocalTime(LocalTime.now())
                .setDate(new Date())
                .setEmpty(null);
    }

}
