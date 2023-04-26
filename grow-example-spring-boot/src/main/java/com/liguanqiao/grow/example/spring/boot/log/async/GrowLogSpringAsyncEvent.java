package com.liguanqiao.grow.example.spring.boot.log.async;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liguanqiao
 * @since 2023/4/13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class GrowLogSpringAsyncEvent {
    private String str;
}
