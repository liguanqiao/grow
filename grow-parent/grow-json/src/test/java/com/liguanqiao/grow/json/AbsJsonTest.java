package com.liguanqiao.grow.json;

import cn.hutool.core.lang.Console;
import com.liguanqiao.grow.json.test.bean.JsonBean;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liguanqiao
 * @since 2023/1/9
 **/
public abstract class AbsJsonTest {

    protected abstract String bean2Json(Object bean);

    protected abstract JsonBean json2Bean(String json);

    protected abstract List<JsonBean> json2GenericsBean(String json);

    /**
     * bean to json test
     */
    @Test
    public void bean2JsonTest() {
        Console.log(bean2Json(JsonBean.create()));
    }

    /**
     * json to bean test
     */
    @Test
    public void json2BeanTest() {
        String json = bean2Json(JsonBean.create());
        Console.log(json);
        JsonBean bean = json2Bean(json);
        Console.log(bean);
    }

    /**
     * json to generics bean test
     */
    @Test
    public void json2GenericsBeanTest() {
        List<JsonBean> list = new ArrayList<>();
        list.add(JsonBean.create());
        list.add(JsonBean.create());
        String json = bean2Json(list);
        Console.log(json);
        List<JsonBean> bean = json2GenericsBean(json);
        Console.log(bean);
    }
}
