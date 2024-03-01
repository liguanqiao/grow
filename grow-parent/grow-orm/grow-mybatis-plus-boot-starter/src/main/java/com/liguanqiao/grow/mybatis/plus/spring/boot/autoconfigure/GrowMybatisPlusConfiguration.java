package com.liguanqiao.grow.mybatis.plus.spring.boot.autoconfigure;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.liguanqiao.grow.mybaits.puls.handlers.ModelMetaObjectHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis Plus 自动装配
 *
 * @author liguanqiao
 * @since 2022/12/14
 **/
@Configuration
public class GrowMybatisPlusConfiguration {

    /**
     * 分页插件
     **/
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 公共字段自动填充
     */
    @Bean
    @ConditionalOnMissingBean(ModelMetaObjectHandler.class)
    public ModelMetaObjectHandler modelMetaObjectHandler() {
        return new ModelMetaObjectHandler();
    }

}
