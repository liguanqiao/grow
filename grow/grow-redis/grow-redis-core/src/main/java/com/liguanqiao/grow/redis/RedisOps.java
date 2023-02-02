package com.liguanqiao.grow.redis;

import com.liguanqiao.grow.redis.model.RedisGeoPoint;
import com.liguanqiao.grow.redis.model.RedisGeoResult;
import com.liguanqiao.grow.redis.model.RedisMetric;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Redis 操作接口
 *
 * @author liguanqiao
 **/
public interface RedisOps {

    //~ String
    <T> Optional<T> get(String key, Class<T> type);

    String get(String key);

    <T> void set(String key, T value);

    <T> Optional<T> getAndSet(String key, T value, Class<T> type);

    String getAndSet(String key, String value);

    <T> Optional<T> getRange(String key, long start, long end, Class<T> type);

    String getRange(String key, long start, long end);

    <T> void setRange(String key, T value, long offset);

    <T> void setEx(String key, T value, long timeout, TimeUnit timeunit);

    <T> Boolean setNx(String key, T value);

    <T> Boolean setNx(String key, T value, long timeout, TimeUnit timeunit);

    <T> List<T> mGet(Class<T> type, String... keys);

    List<String> mGet(String... keys);

    <T> void mSet(Map<String, T> map);

    Long incr(String key);

    Long incrBy(String key, long amount);

    Long decr(String key);

    Long decrBy(String key, long amount);

    Boolean setBit(String key, long offset, boolean value);

    Boolean getBit(String key, long offset);

    //~ Lists
    <T> Optional<T> lPop(String key, Class<T> type);

    String lPop(String key);

    <T> Optional<T> rPop(String key, Class<T> type);

    String rPop(String key);

    Long lPush(String key, Object... values);

    List<String> lRange(String key, long start, long end);

    <T> List<T> lRange(String key, long start, long end, Class<T> type);

    Long rPush(String key, Object... values);

    Long lLen(String key);

    //~ Hashes
    Long hDel(String key, String... fields);

    <T> Optional<T> hGet(String key, String field, Class<T> type);

    String hGet(String key, String field);

    <T> List<T> hMGet(String key, Class<T> type, String... fields);

    List<String> hMGet(String key, String... fields);

    <T, K, V> Map<K, V> hMGet(String key, Collection<String> fields, Class<T> type, Function<T, K> keyMapper, Function<T, V> valueMapper);

    <T> void hSet(String key, String field, T value);

    <T> void hMSet(String key, Map<String, T> fieldValues);

    Long hLen(String key);

    Boolean hExists(String key, String field);

    <T> Map<String, T> hGetAll(String key, Class<T> type);

    Map<String, String> hGetAll(String key);

    Set<String> hKeys(String key);

    Long hIncr(String key, String field, long amount);

    Long hDecr(String key, String field, long amount);

    Double hIncr(String key, String field, double amount);

    Double hDecr(String key, String field, double amount);

    //~ Sets
    Long sAdd(String key, Object... members);

    Long sLen(String key);

    String sPop(String key);

    <T> Optional<T> sPop(String key, Class<T> type);

    <T> Boolean sIsMember(String key, T member);

    Set<String> sMembers(String key);

    //~ ZSets
    <T> Boolean zAdd(String key, T value, double score);

    Long zCount(String key, double min, double max);

    <T> Double zScore(String key, T member);

    Boolean zRemove(String key, Object... members);

    <T> Long zRank(String key, T member);

    <T> Long zRevRank(String key, T member);

    Long zLen(String key);

    Set<String> zRange(String key, long start, long end);

    <T> Set<T> zRange(String key, long start, long end, Class<T> type);

    Set<String> zRangeByScore(String key, double min, double max);

    <T> Set<T> zRangeByScore(String key, double min, double max, Class<T> type);

    Set<String> zRevRangeByScore(String key, double min, double max);

    <T> Set<T> zRevRangeByScore(String key, double min, double max, Class<T> type);

    //~ Geos

    /**
     * 经纬度校验，由EPSG:900913 / EPSG:3785 / OSGEO:41001 规定如下：
     * 有效的经度从-180度到180度
     * 有效的纬度从-85.05112878度到85.05112878度 与正常地理上的-90度～90度有差别
     *
     * @param lon 经度
     * @param lat 纬度
     * @return 校验结果
     */
    default Boolean geoCheck(double lon, double lat) {
        double lonRange = 180.00;
        double latRange = 85.05112878;
        //大于 1  ，等于 0,  小于 -1
        //校验经度范围
        int lonL = BigDecimal.valueOf(lon).compareTo(BigDecimal.valueOf(lonRange));
        int lonG = BigDecimal.valueOf(lon).compareTo(BigDecimal.valueOf(-lonRange));
        //校验纬度范围
        int latL = BigDecimal.valueOf(lat).compareTo(BigDecimal.valueOf(latRange));
        int latG = BigDecimal.valueOf(lat).compareTo(BigDecimal.valueOf(-latRange));

        if (lonL > 0 || lonG < 0 || latL > 0 || latG < 0) {
            throw new RedisException("Redis Geo 有效的经度从-180度到180度,有效的纬度从-85.05112878度到85.05112878度");
        }

        String lonStr = String.valueOf(lon < 0 ? -lon : lon);
        String latStr = String.valueOf(lat < 0 ? -lat : lat);
        String reglon = "((?:[0-9]|[1-9][0-9]|1[0-7][0-9])\\.([0-9]{0,6}))|((?:180)\\.([0]{0,6}))";
        String reglat = "((?:[0-9]|[1-8][0-9])\\.([0-9]{0,6}))|((?:90)\\.([0]{0,6}))";

        return lonStr.trim().matches(reglon) && latStr.trim().matches(reglat);
    }


    /**
     * 单个经纬点添加方法
     *
     * @param key       redis key名
     * @param pointName 成员名
     * @param lat       经度
     * @param lon       纬度
     * @return 添加成功的成员数量
     */
    Long geoAdd(String key, String pointName, double lon, double lat);

    /**
     * 多个经纬点批量添加方法
     *
     * @param key      redis key名
     * @param pointMap Map结构的多个RedisOpsGeoPoint对象
     * @return 添加成功的成员数量
     */
    Long geoAdd(String key, Map<String, RedisGeoPoint> pointMap);

    /**
     * 计算指定名字的两个点之间的距离
     *
     * @param key          key名
     * @param pointOneName 第一个点的名字
     * @param pointTwoName 第二个点的名字
     * @param metric       距离单位
     * @return Double类型的以公里为单位的距离值, 如果第一个点或第二个点已被删除, 则返回 {@link Optional#empty()}
     */
    Optional<Double> geoDist(String key, String pointOneName, String pointTwoName, RedisMetric metric);

    /**
     * 取指定点的hash值
     *
     * @param key        redis key名
     * @param pointNames 一个或多个在经纬集合中的名字
     * @return hash串列表
     */
    List<String> geoHash(String key, String... pointNames);

    /**
     * 获取一个或多个成员的位置的点经纬度信息
     *
     * @param key        redis key
     * @param pointNames geo集合中的经纬点的名
     * @return 经纬点列表
     */
    List<RedisGeoPoint> geoPosition(String key, String... pointNames);

    /**
     * 以给定的距离和指定的集合中的某个点，返回包含在指定半径距离中的指定数量的元素
     * m ：米，默认单位。
     * km ：千米。
     * mi ：英里。
     * ft ：英尺。
     * WITHDIST: 在返回位置元素的同时， 将位置元素与中心之间的距离也一并返回。
     * WITHCOORD: 将位置元素的经度和维度也一并返回。
     * COUNT 限定返回的记录数。
     * ASC: 查找结果根据距离从近到远排序。
     * DESC: 查找结果根据从远到近排序。
     *
     * @param key       redis geo集合key名
     * @param pointName 集合中的元素名
     * @param distance  指定距离半径
     * @param limit     限制取多少条，0表示不限制
     * @param isSortAsc 是否由近到远，为false则是由远到近
     * @param meters    距离单位
     * @return key:pointName,value:{@link RedisGeoResult}
     */
    Map<String, RedisGeoResult> geoRadius(String key, String pointName, double distance, long limit, boolean isSortAsc, RedisMetric meters);

    /**
     * 以给定经纬度做原点，返回包含在指定半径距离中的指定数量的元素
     *
     * @param key       redis geo集合key名
     * @param lon       经度
     * @param lat       纬度
     * @param distance  指定距离半径
     * @param limit     限制取多少条，0表示不限制
     * @param isSortAsc 是否由近到远，为false则是由远到近
     * @param meters    距离单位
     * @return key:pointName,value:{@link RedisGeoResult}
     */
    Map<String, RedisGeoResult> geoRadius(String key, double lon, double lat, double distance, long limit, boolean isSortAsc, RedisMetric meters);

    /**
     * 指定删除指定geo key的有序集中的一个或多个成员
     *
     * @param key        redis key名
     * @param pointNames 存进去的经纬点名
     * @return 是否移除成功
     */
    Boolean geoRemove(String key, String... pointNames);

    //~ Commons
    void del(String... keys);

    Boolean expire(String key, long timeout, TimeUnit timeunit);

    long expire(long timeout, TimeUnit timeunit, String... keys);

    boolean exists(String key);

    /**
     * 扫描Keys
     *
     * @param pattern 表达式
     * @return keys
     **/
    Set<String> sacn(String pattern);

    /**
     * 扫描Keys
     *
     * @param pattern 表达式
     * @param action  动作
     **/
    void scan(String pattern, Consumer<String> action);
}
