package com.liguanqiao.grow.example.core.redis.model.resp;

import com.liguanqiao.grow.example.core.redis.model.dto.GrowRedisValueDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author liguanqiao
 * @since 2023/1/5
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class GrowRedisResp extends GrowRedisValueDTO {
    public static GrowRedisResp convert(GrowRedisValueDTO dto) {
        return (GrowRedisResp) new GrowRedisResp()
                .setStr(dto.getStr())
                .setLon(dto.getLon())
                .setDou(dto.getDou())
                .setBigDecimal(dto.getBigDecimal())
                .setLocalDateTime(dto.getLocalDateTime())
                .setDate(dto.getDate());
    }
}
