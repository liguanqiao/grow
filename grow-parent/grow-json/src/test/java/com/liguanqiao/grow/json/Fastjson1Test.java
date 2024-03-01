package com.liguanqiao.grow.json;

import com.liguanqiao.grow.json.engine.fastjson1.Fastjson1Engine;
import com.liguanqiao.grow.json.test.bean.JsonBean;

import java.util.List;

/**
 * @author liguanqiao
 * @since 2023/1/9
 **/
public class Fastjson1Test extends AbsJsonTest {

    public final JsonEngine jsonEngine = new Fastjson1Engine();

    @Override
    protected String bean2Json(Object bean) {
        return jsonEngine.toJson(bean);
    }

    @Override
    protected JsonBean json2Bean(String json) {
        return jsonEngine.toBean(json, JsonBean.class);
    }

    @Override
    protected List<JsonBean> json2GenericsBean(String json) {
        return jsonEngine.toBean(json, new JsonTypeReference<List<JsonBean>>() {
        });
    }

    @Override
    public void bean2JsonTest() {
        super.bean2JsonTest();
    }

    @Override
    public void json2BeanTest() {
        super.json2BeanTest();
    }

    @Override
    public void json2GenericsBeanTest() {
        super.json2GenericsBeanTest();
    }
}
