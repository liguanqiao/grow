package com.liguanqiao.grow.redis.spring;

import cn.hutool.core.collection.CollUtil;
import com.liguanqiao.grow.redis.AbsRedisOps;
import com.liguanqiao.grow.redis.RedisOps;
import com.liguanqiao.grow.redis.model.RedisGeoPoint;
import com.liguanqiao.grow.redis.model.RedisGeoResult;
import com.liguanqiao.grow.redis.model.RedisMetric;
import com.liguanqiao.grow.redis.serializer.RedisOpsSerializer;
import com.liguanqiao.grow.redis.spring.model.RedisSpringMetricRelevance;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * @author liguanqiao
 * @since 2023/1/4
 **/
@AllArgsConstructor
public class RedisOpsSpringImpl extends AbsRedisOps implements RedisOps {

    private final StringRedisTemplate template;
    @Getter(value = AccessLevel.PROTECTED)
    private final RedisOpsSerializer serializer;

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        return Optional.ofNullable(opsForValue().get(key))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> void set(String key, T value) {
        opsForValue().set(key, serialize(value));
    }

    @Override
    public <T> Optional<T> getAndSet(String key, T value, Class<T> type) {
        return Optional.ofNullable(opsForValue().getAndSet(key, serialize(value)))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> Optional<T> getRange(String key, long start, long end, Class<T> type) {
        return Optional.ofNullable(opsForValue().get(key, start, end))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> void setRange(String key, T value, long offset) {
        opsForValue().set(key, serialize(value), offset);
    }

    @Override
    public <T> void setEx(String key, T value, long timeout, TimeUnit timeunit) {
        opsForValue().set(key, serialize(value), timeout, timeunit);
    }

    @Override
    public <T> Boolean setNx(String key, T value) {
        return opsForValue().setIfAbsent(key, serialize(value));
    }

    @Override
    public <T> Boolean setNx(String key, T value, long timeout, TimeUnit timeunit) {
        return opsForValue().setIfAbsent(key, serialize(value), timeout, timeunit);
    }

    @Override
    public <T> List<T> mGet(Class<T> type, String... keys) {
        return mGet(type, Arrays.asList(keys));
    }

    @Override
    public <T> List<T> mGet(Class<T> type, Collection<String> keys) {
        return Optional.ofNullable(opsForValue().multiGet(keys))
                .map(rawVal -> deserialize2List(rawVal, type))
                .orElseGet(ArrayList::new);
    }

    @Override
    public <T> void mSet(Map<String, T> map) {
        opsForValue().multiSet(
                map.entrySet()
                        .stream()
                        .filter(e -> Objects.nonNull(e.getKey()) && Objects.nonNull(e.getValue()))
                        .collect(toMap(Map.Entry::getKey, e -> serialize(e.getValue())))
        );
    }

    @Override
    public Long incr(String key) {
        return incrBy(key, 1);
    }

    @Override
    public Long incrBy(String key, long amount) {
        return opsForValue().increment(key, amount);
    }

    @Override
    public Long decr(String key) {
        return decrBy(key, 1);
    }

    @Override
    public Long decrBy(String key, long amount) {
        return incrBy(key, -amount);
    }

    @Override
    public Boolean setBit(String key, long offset, boolean value) {
        return opsForValue().setBit(key, offset, value);
    }

    @Override
    public Boolean getBit(String key, long offset) {
        return opsForValue().getBit(key, offset);
    }

    //~ Lists
    @Override
    public <T> Optional<T> lGet(String key, int index, Class<T> type) {
        return Optional.ofNullable(opsForList().index(key, index))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> void lSet(String key, int index, T value) {
        opsForList().set(key, index, serialize(value));
    }

    @Override
    public <T> Optional<T> lLeftPop(String key, Class<T> type) {
        return Optional.ofNullable(opsForList().leftPop(key))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> Optional<T> lRightPop(String key, Class<T> type) {
        return Optional.ofNullable(opsForList().rightPop(key))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public Long lLeftPush(String key, Object... values) {
        return opsForList().leftPushAll(key, serialize2List(values));
    }

    @Override
    public <T> Long lLeftPush(String key, Collection<T> values) {
        return opsForList().leftPushAll(key, serialize2List(values));
    }

    @Override
    public <T> List<T> lRange(String key, int start, int end, Class<T> type) {
        return Optional.ofNullable(opsForList().range(key, start, end))
                .map(rawVal -> deserialize2List(rawVal, type))
                .orElseGet(ArrayList::new);
    }

    @Override
    public Long lRightPush(String key, Object... values) {
        return opsForList().rightPushAll(key, serialize2List(values));
    }

    @Override
    public <T> Long lRightPush(String key, Collection<T> values) {
        return opsForList().rightPushAll(key, serialize2List(values));
    }

    @Override
    public Long lLen(String key) {
        return opsForList().size(key);
    }

    //~ Hashes
    @Override
    public Long hDel(String key, String... fields) {
        return opsForHash().delete(key, Arrays.stream(fields).toArray());
    }

    @Override
    public Long hDel(String key, Collection<String> fields) {
        return opsForHash().delete(key, fields.toArray());
    }

    @Override
    public <T> Optional<T> hGet(String key, String field, Class<T> type) {
        return Optional.ofNullable(opsForHash().get(key, field))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> List<T> hMGet(String key, Class<T> type, String... fields) {
        return hMGet(key, type, Arrays.asList(fields));
    }

    @Override
    public <T> List<T> hMGet(String key, Class<T> type, Collection<String> fields) {
        return Optional.ofNullable(opsForHash().multiGet(key, fields))
                .map(rawVal -> deserialize2List(rawVal, type))
                .orElseGet(ArrayList::new);
    }

    @Override
    public <T, K, V> Map<K, V> hMGet(String key, Collection<String> fields, Class<T> type, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return opsForHash().multiGet(key, fields)
                .stream()
                .filter(Objects::nonNull)
                .map(rawVal -> deserialize(rawVal, type))
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }

    @Override
    public <T> void hSet(String key, String field, T value) {
        opsForHash().put(key, field, serialize(value));
    }

    @Override
    public <T> void hMSet(String key, Map<String, T> fieldValues) {
        opsForHash().putAll(key, fieldValues.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> serialize(e.getValue()))));
    }

    @Override
    public Long hLen(String key) {
        return opsForHash().size(key);
    }

    @Override
    public Boolean hExists(String key, String field) {
        return opsForHash().hasKey(key, field);
    }

    @Override
    public <T> Map<String, T> hGetAll(String key, Class<T> type) {
        return opsForHash().entries(key).entrySet()
                .stream()
                .filter(e -> Objects.nonNull(e.getKey()) && Objects.nonNull(e.getValue()))
                .collect(toMap(Map.Entry::getKey, e -> deserialize(e.getValue(), type)));
    }

    @Override
    public Set<String> hKeys(String key) {
        return opsForHash().keys(key);
    }

    @Override
    public Long hIncr(String key, String field, long amount) {
        return opsForHash().increment(key, field, amount);
    }

    @Override
    public Long hDecr(String key, String field, long amount) {
        return opsForHash().increment(key, field, -amount);
    }

    @Override
    public Double hIncr(String key, String field, double amount) {
        return opsForHash().increment(key, field, amount);
    }

    @Override
    public Double hDecr(String key, String field, double amount) {
        return opsForHash().increment(key, field, -amount);
    }

    //~ Sets
    @Override
    public Long sAdd(String key, Object... members) {
        return opsForSet().add(key, serialize2Array(members));
    }

    @Override
    public <T> Long sAdd(String key, Collection<T> members) {
        return opsForSet().add(key, serialize2Array(members));
    }

    @Override
    public Long sLen(String key) {
        return opsForSet().size(key);
    }

    @Override
    public <T> Optional<T> sPop(String key, Class<T> type) {
        return Optional.ofNullable(opsForSet().pop(key))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> Boolean sIsMember(String key, T member) {
        return opsForSet().isMember(key, serialize(member));
    }

    @Override
    public <T> Set<T> sMembers(String key, Class<T> type) {
        return Optional.ofNullable(opsForSet().members(key))
                .map(rawVal -> deserialize2Set(rawVal, type))
                .orElseGet(HashSet::new);
    }

    @Override
    public <T> Optional<T> sRandomMember(String key, Class<T> type) {
        return Optional.ofNullable(opsForSet().randomMember(key))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> Set<T> sRandomMember(String key, int count, Class<T> type) {
        return Optional.ofNullable(opsForSet().randomMembers(key, count))
                .map(rawVal -> deserialize2Set(rawVal, type))
                .orElseGet(HashSet::new);
    }

    //~ ZSet
    @Override
    public <T> Boolean zAdd(String key, T value, double score) {
        return opsForZSet().add(key, serialize(value), score);
    }

    @Override
    public <T> Long zAdd(String key, Map<T, Double> values) {
        return opsForZSet().add(
                key,
                values.entrySet()
                        .stream()
                        .map(e -> new DefaultTypedTuple<>(serialize(e.getKey()), e.getValue()))
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public Long zCount(String key, double min, double max) {
        return opsForZSet().count(key, min, max);
    }

    @Override
    public <T> Double zScore(String key, T member) {
        return opsForZSet().score(key, member);
    }

    @Override
    public Boolean zRemove(String key, Object... members) {
        return Optional.ofNullable(opsForZSet().remove(key, Arrays.stream(members).map(this::serialize).toArray()))
                .map(count -> count > 0)
                .orElse(Boolean.FALSE);
    }

    @Override
    public <T> Boolean zRemove(String key, Collection<T> members) {
        return Optional.ofNullable(opsForZSet().remove(key, members.stream().map(this::serialize).toArray()))
                .map(count -> count > 0)
                .orElse(Boolean.FALSE);
    }

    @Override
    public <T> Long zRank(String key, T member) {
        return opsForZSet().rank(key, serialize(member));
    }

    @Override
    public <T> Long zRevRank(String key, T member) {
        return opsForZSet().reverseRank(key, serialize(member));
    }

    @Override
    public Long zLen(String key) {
        return opsForZSet().zCard(key);
    }

    @Override
    public <T> Set<T> zRange(String key, int start, int end, Class<T> type) {
        return Optional.ofNullable(opsForZSet().range(key, start, end))
                .map(rawVal -> deserialize2Set(rawVal, type))
                .orElseGet(HashSet::new);
    }

    @Override
    public <T> Set<T> zRangeByScore(String key, double min, double max, Class<T> type) {
        return Optional.ofNullable(opsForZSet().rangeByScore(key, min, max))
                .map(rawVal -> deserialize2Set(rawVal, type))
                .orElseGet(HashSet::new);
    }

    @Override
    public <T> Set<T> zRevRangeByScore(String key, double min, double max, Class<T> type) {
        return Optional.ofNullable(opsForZSet().reverseRangeByScore(key, min, max))
                .map(rawVal -> deserialize2Set(rawVal, type))
                .orElseGet(HashSet::new);
    }

    //~ Geos

    /**
     * 仅提取业务需要用到的字段
     *
     * @param redisResult 原生data-redis获取回来的geo结构体
     * @return 通用封装仅业务所需字段的Map列表
     */
    private Map<String, RedisGeoResult> convertToRedisGeoResult(GeoResults<RedisGeoCommands.GeoLocation<String>> redisResult, RedisMetric meters) {
        Map<String, RedisGeoResult> resultMap = new HashMap<>(10);
        if (redisResult != null) {
            List<GeoResult<RedisGeoCommands.GeoLocation<String>>> geoResultList = redisResult.getContent();
            //赋值
            geoResultList.forEach(item -> resultMap.put(
                    item.getContent().getName(),
                    RedisGeoResult.of(item.getDistance().getValue(), meters, item.getContent().getPoint().getX(), item.getContent().getPoint().getY())
            ));
        }
        return resultMap;
    }

    @Override
    public Long geoAdd(String key, String pointName, double lon, double lat) {
        if (geoCheck(lon, lat)) {
            return opsForGeo().add(key, new Point(lon, lat), pointName);
        }
        return 0L;
    }

    @Override
    public Long geoAdd(String key, Map<String, RedisGeoPoint> pointMap) {
        Map<String, Point> coordinateMap = new HashMap<>(10);
        pointMap.forEach((pointName, value) -> {
            //过滤不合法的经纬度点
            if (geoCheck(value.getLon(), value.getLat())) {
                //转成data-redis包里的对象
                coordinateMap.put(pointName, new Point(value.getLon(), value.getLat()));
            }
        });
        return Optional.of(coordinateMap)
                .filter(CollUtil::isNotEmpty)
                .map(map -> opsForGeo().add(key, map))
                .orElse(0L);
    }

    @Override
    public Optional<Double> geoDist(String key, String pointOneName, String pointTwoName, RedisMetric meters) {
        return geoDist(key, pointOneName, pointTwoName, RedisSpringMetricRelevance.convert(meters));
    }

    /**
     * 计算指定名字的两个点之间的距离
     *
     * @param key          key名
     * @param pointOneName 第一个点的名字,如果点已被删除
     * @param pointTwoName 第二个点的名字,如果点已被删除
     * @return Double类型的以指定单位的距离值
     */
    private Optional<Double> geoDist(String key, String pointOneName, String pointTwoName, Metric metric) {
        return Optional.ofNullable(opsForGeo().distance(key, pointOneName, pointTwoName, metric))
                .map(Distance::getValue);
    }

    @Override
    public List<String> geoHash(String key, String... pointNames) {
        return opsForGeo().hash(key, pointNames);
    }

    @Override
    public List<String> geoHash(String key, Collection<String> pointNames) {
        return opsForGeo().hash(key, pointNames.toArray(new String[0]));
    }

    @Override
    public List<RedisGeoPoint> geoPosition(String key, String... pointNames) {
        return Optional.ofNullable(opsForGeo().position(key, pointNames))
                .map(list -> list.stream().map(point -> new RedisGeoPoint(point.getX(), point.getY())).collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<RedisGeoPoint> geoPosition(String key, Collection<String> pointNames) {
        return Optional.ofNullable(opsForGeo().position(key, pointNames.toArray(new String[0])))
                .map(list -> list.stream().map(point -> new RedisGeoPoint(point.getX(), point.getY())).collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    @Override
    public Map<String, RedisGeoResult> geoRadius(String key, String pointName, double distance, long limit, boolean isSortAsc, RedisMetric meters) {
        //指定距离与单位，按m计算或km计算
        Distance distanceObj = new Distance(distance, RedisSpringMetricRelevance.convert(meters));

        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands
                .GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                //在返回位置元素的同时， 将位置元素与中心之间的距离也一并返回。
                .includeDistance()
                //将位置元素的经度和维度也一并返回。
                .includeCoordinates();

        //是否由近到远还是由远到近排序
        if (isSortAsc) {
            args.sortAscending();
        } else {
            args.sortDescending();
        }

        //限定数量,大于0时生效，等于0不限
        if (limit > 0L) {
            args.limit(limit);
        }

        GeoResults<RedisGeoCommands.GeoLocation<String>> redisResult = opsForGeo().radius(key, pointName, distanceObj, args);
        return convertToRedisGeoResult(redisResult, meters);
    }

    @Override
    public Map<String, RedisGeoResult> geoRadius(String key, double lon, double lat, double distance, long limit, boolean isSortAsc, RedisMetric meters) {
        if (geoCheck(lon, lat)) {
            //指定点经纬度
            Point point = new Point(lon, lat);

            //指定距离与单位，按m计算或km计算
            Distance distanceObj = new Distance(distance, RedisSpringMetricRelevance.convert(meters));

            //构造一个以指定点为原点，指定距离为半径的圆
            Circle circle = new Circle(point, distanceObj);

            RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands
                    .GeoRadiusCommandArgs
                    .newGeoRadiusArgs()
                    //在返回位置元素的同时， 将位置元素与中心之间的距离也一并返回。
                    .includeDistance()
                    //将位置元素的经度和维度也一并返回。
                    .includeCoordinates();

            //是否由近到远还是由远到近排序
            if (isSortAsc) {
                args.sortAscending();
            } else {
                args.sortDescending();
            }

            //限定数量,大于0时生效，等于0不限
            if (limit > 0L) {
                args.limit(limit);
            }

            GeoResults<RedisGeoCommands.GeoLocation<String>> redisResult = opsForGeo().radius(key, circle, args);
            return convertToRedisGeoResult(redisResult, meters);
        }
        return new HashMap<>();
    }

    @Override
    public Boolean geoRemove(String key, String... pointNames) {
        return Optional.ofNullable(opsForGeo().remove(key, pointNames))
                .map(count -> count > 0)
                .orElse(Boolean.FALSE);
    }

    @Override
    public Boolean geoRemove(String key, Collection<String> pointNames) {
        return geoRemove(key, pointNames.toArray(new String[0]));
    }

    //~ Common
    @Override
    public void del(String... keys) {
        del(Arrays.asList(keys));
    }

    @Override
    public void del(Collection<String> keys) {
        template.delete(keys);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit timeunit) {
        return template.expire(key, timeout, timeunit);
    }

    @Override
    public long expire(long timeout, TimeUnit timeunit, String... keys) {
        return Arrays.stream(keys)
                .map(key -> expire(key, timeout, timeunit))
                .filter(Boolean::booleanValue)
                .count();
    }

    @Override
    public long expire(long timeout, TimeUnit timeunit, Collection<String> keys) {
        return keys.stream()
                .map(key -> expire(key, timeout, timeunit))
                .filter(Boolean::booleanValue)
                .count();
    }

    @Override
    public boolean exists(String key) {
        return Optional.ofNullable(template.hasKey(key))
                .orElse(Boolean.FALSE);
    }

    @Override
    public long ttl(String key, TimeUnit timeunit) {
        return Optional.ofNullable(template.getExpire(key, timeunit))
                .orElse(-2L);
    }

    @Override
    public Set<String> sacn(String pattern) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(1000).build();
        return template.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(options);
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });
    }

    @Override
    public void scan(String pattern, Consumer<String> action) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(1000).build();
        template.execute((RedisCallback<Set<String>>) connection -> {
            Cursor<byte[]> cursor = connection.scan(options);
            while (cursor.hasNext()) {
                action.accept(new String(cursor.next()));
            }
            return null;
        });
    }

    //~ Base
    private ValueOperations<String, String> opsForValue() {
        return template.opsForValue();
    }

    private ListOperations<String, String> opsForList() {
        return template.opsForList();
    }

    private HashOperations<String, String, String> opsForHash() {
        return template.opsForHash();
    }

    private SetOperations<String, String> opsForSet() {
        return template.opsForSet();
    }

    private ZSetOperations<String, String> opsForZSet() {
        return template.opsForZSet();
    }

    private GeoOperations<String, String> opsForGeo() {
        return template.opsForGeo();
    }

}
