package com.liguanqiao.grow.json.engine;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ServiceLoaderUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.liguanqiao.grow.json.JsonEngine;
import com.liguanqiao.grow.json.JsonException;

/**
 * Json引擎工厂
 *
 * @author liguanqiao
 **/
public class JsonFactory {

    /**
     * 根据用户引入的json引擎jar，自动创建对应的Json引擎对象<br>
     * 获得的是单例的JsonEngine
     *
     * @return 单例的JsonEngine
     */
    public static JsonEngine get() {
        return Singleton.get(JsonEngine.class.getName(), JsonFactory::create);
    }

    /**
     * 根据用户引入的json引擎jar，自动创建对应的json引擎对象
     *
     * @return {@link JsonEngine}
     */
    public static JsonEngine create() {
        final JsonEngine engine = doCreate();
        StaticLog.debug("Use [{}] Json Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
        return engine;
    }

    /**
     * 根据用户引入的json引擎jar，自动创建对应的json引擎对象
     *
     * @return {@link JsonEngine}
     */
    private static JsonEngine doCreate() {
        final JsonEngine engine = ServiceLoaderUtil.loadFirstAvailable(JsonEngine.class);
        if (null != engine) {
            return engine;
        }

        throw new JsonException("No json found ! Please add some json jar to your project !");
    }
}
