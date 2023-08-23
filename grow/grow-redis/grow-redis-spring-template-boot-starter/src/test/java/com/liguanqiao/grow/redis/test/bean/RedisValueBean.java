package com.liguanqiao.grow.redis.test.bean;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author liguanqiao
 * @since 2023/8/15
 **/
@ToString(callSuper = true)
@Data
@Accessors(chain = true)
public class RedisValueBean {
    private String str;
    private Short _short;
    private Integer _int;
    private Long _long;
    private Float _float;
    private Double _double;
    private BigDecimal bigDecimal;
    private LocalDateTime localDateTime;
    private LocalDate localDate;
    private LocalTime localTime;
    private Date date;

    public static RedisValueBean create() {
        return new RedisValueBean()
                .setStr(RandomUtil.randomString(16))
                .set_short((short) RandomUtil.randomInt(Short.MIN_VALUE, Short.MAX_VALUE))
                .set_int(RandomUtil.randomInt())
                .set_long(RandomUtil.randomLong())
                .set_float(RandomUtil.randomBigDecimal().floatValue())
                .set_double(RandomUtil.randomDouble())
                .setBigDecimal(RandomUtil.randomBigDecimal())
                .setLocalDateTime(LocalDateTime.now())
                .setLocalDate(LocalDate.now())
                .setLocalTime(LocalTime.now())
                .setDate(new Date());
    }

}
