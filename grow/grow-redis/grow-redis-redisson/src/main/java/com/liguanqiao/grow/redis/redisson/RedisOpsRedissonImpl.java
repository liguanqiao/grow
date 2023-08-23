package com.liguanqiao.grow.redis.redisson;

import cn.hutool.core.collection.CollUtil;
import com.liguanqiao.grow.json.JsonException;
import com.liguanqiao.grow.redis.AbsRedisOps;
import com.liguanqiao.grow.redis.RedisOps;
import com.liguanqiao.grow.redis.model.RedisGeoPoint;
import com.liguanqiao.grow.redis.model.RedisGeoResult;
import com.liguanqiao.grow.redis.model.RedisMetric;
import com.liguanqiao.grow.redis.redisson.model.RedisRedissonMetricRelevance;
import com.liguanqiao.grow.redis.serializer.RedisOpsSerializer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.redisson.api.*;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.api.geo.OptionalGeoSearch;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * Redis 操作接口 Redisson实现
 *
 * @author liguanqiao
 * @since 2023/1/4
 **/
@AllArgsConstructor
public class RedisOpsRedissonImpl extends AbsRedisOps implements RedisOps {

    private final RedissonClient redissonClient;
    @Getter(value = AccessLevel.PROTECTED)
    private final RedisOpsSerializer serializer;

    //~ String
    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        return Optional.ofNullable(opsForValue(key).get())
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> void set(String key, T value) {
        opsForValue(key).set(serialize(value));
    }

    @Override
    public <T> Optional<T> getAndSet(String key, T value, Class<T> type) {
        return Optional.ofNullable(opsForValue(key).getAndSet(serialize(value)))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> Optional<T> getRange(String key, long start, long end, Class<T> type) {
        return Optional.ofNullable(getRange(key, start, end))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @SneakyThrows
    private String getRange(String key, long start, long end) {
        ByteBuffer byteBuf = ByteBuffer.wrap(new byte[]{}, (int) start, (int) end);
        redissonClient.getBinaryStream(key).getChannel().read(byteBuf);
        return StandardCharsets.UTF_8.decode(byteBuf).toString();
    }

    @Override
    @SneakyThrows
    public <T> void setRange(String key, T value, long offset) {
        byte[] array = serialize(value).getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuf = ByteBuffer.wrap(array, (int) offset, array.length);
        redissonClient.getBinaryStream(key).getChannel().write(byteBuf);
    }

    @Override
    public <T> void setEx(String key, T value, long timeout, TimeUnit timeunit) {
        opsForValue(key).set(serialize(value), timeout, timeunit);
    }

    @Override
    public <T> Boolean setNx(String key, T value) {
        return opsForValue(key).setIfAbsent(serialize(value));
    }

    @Override
    public <T> Boolean setNx(String key, T value, long timeout, TimeUnit timeunit) {
        return opsForValue(key).setIfAbsent(serialize(value), Duration.ofMillis(timeunit.toMillis(timeout)));
    }

    @Override
    public <T> List<T> mGet(Class<T> type, String... keys) {
        return Optional.ofNullable(redissonClient.getBuckets().<String>get(keys))
                .map(Map::values)
                .map(rawVal -> deserialize2List(rawVal, type))
                .orElseGet(ArrayList::new);
    }

    @Override
    public <T> List<T> mGet(Class<T> type, Collection<String> keys) {
        return mGet(type, keys.toArray(new String[0]));
    }

    @Override
    public <T> void mSet(Map<String, T> map) {
        redissonClient.getBuckets().set(map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> serialize(e.getValue()))));
    }

    @Override
    public Long incr(String key) {
        return incrBy(key, 1);
    }

    @Override
    public Long incrBy(String key, long amount) {
        return redissonClient.getAtomicLong(key).addAndGet(amount);
    }

    @Override
    public Long decr(String key) {
        return decrBy(key, 1);
    }

    @Override
    public Long decrBy(String key, long amount) {
        return redissonClient.getAtomicLong(key).addAndGet(-amount);
    }

    @Override
    public Boolean setBit(String key, long offset, boolean value) {
        return redissonClient.getBitSet(key).set(offset, value);
    }

    @Override
    public Boolean getBit(String key, long offset) {
        return redissonClient.getBitSet(key).get(offset);
    }

    //~ List
    @Override
    public <T> Optional<T> lGet(String key, int index, Class<T> type) {
        return Optional.ofNullable(opsForList(key).get(index))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> void lSet(String key, int index, T value) {
        opsForList(key).set(index, serialize(value));
    }

    @Override
    public <T> Optional<T> lLeftPop(String key, Class<T> type) {
        return Optional.ofNullable(opsForDeque(key).pollFirst())
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> Optional<T> lRightPop(String key, Class<T> type) {
        return Optional.ofNullable(opsForDeque(key).pollLast())
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public Long lLeftPush(String key, Object... values) {
        return (long) opsForDeque(key).addFirst(serialize2Array(values));
    }

    @Override
    public <T> Long lLeftPush(String key, Collection<T> values) {
        return (long) opsForDeque(key).addFirst(serialize2Array(values));
    }

    @Override
    public <T> List<T> lRange(String key, int start, int end, Class<T> type) {
        return Optional.ofNullable(opsForList(key).range(start, end))
                .map(rawVal -> deserialize2List(rawVal, type))
                .orElseGet(ArrayList::new);
    }

    @Override
    public Long lRightPush(String key, Object... values) {
        return (long) opsForDeque(key).addLast(serialize2Array(values));
    }

    @Override
    public <T> Long lRightPush(String key, Collection<T> values) {
        return (long) opsForDeque(key).addLast(serialize2Array(values));
    }

    @Override
    public Long lLen(String key) {
        return (long) opsForList(key).size();
    }

    //~ Hash

    @Override
    public Long hDel(String key, String... fields) {
        return opsForHash(key).fastRemove(fields);
    }

    @Override
    public Long hDel(String key, Collection<String> fields) {
        return hDel(key, fields.toArray(new String[0]));
    }

    @Override
    public <T> Optional<T> hGet(String key, String field, Class<T> type) {
        return Optional.ofNullable(opsForHash(key).get(field))
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> List<T> hMGet(String key, Class<T> type, String... fields) {
        return Optional.ofNullable(opsForHash(key).getAll(Arrays.stream(fields).collect(Collectors.toSet())))
                .map(Map::values)
                .map(rawVal -> deserialize2List(rawVal, type))
                .orElseGet(ArrayList::new);
    }

    @Override
    public <T> List<T> hMGet(String key, Class<T> type, Collection<String> fields) {
        return Optional.ofNullable(opsForHash(key).getAll(new HashSet<>(fields)))
                .map(Map::values)
                .map(rawVal -> deserialize2List(rawVal, type))
                .orElseGet(ArrayList::new);
    }

    @Override
    public <T, K, V> Map<K, V> hMGet(String key, Collection<String> fields, Class<T> type, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return opsForHash(key).getAll(new HashSet<>(fields)).values()
                .stream()
                .filter(Objects::nonNull)
                .map(rawVal -> deserialize(rawVal, type))
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }

    @Override
    public <T> void hSet(String key, String field, T value) {
        opsForHash(key).put(field, serialize(value));
    }

    @Override
    public <T> void hMSet(String key, Map<String, T> fieldValues) {
        opsForHash(key).putAll(
                fieldValues.entrySet().stream()
                        .collect(toMap(Map.Entry::getKey, e -> serialize(e.getValue())))
        );
    }

    @Override
    public Long hLen(String key) {
        return (long) opsForHash(key).size();
    }

    @Override
    public Boolean hExists(String key, String field) {
        return opsForHash(key).containsKey(field);
    }

    @Override
    public <T> Map<String, T> hGetAll(String key, Class<T> type) {
        return opsForHash(key).readAllMap().entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> deserialize(e.getValue(), type)));
    }

    @Override
    public Set<String> hKeys(String key) {
        return opsForHash(key).keySet();
    }

    @Override
    public Long hIncr(String key, String field, long amount) {
        return Optional.ofNullable(opsForHash(key).addAndGet(field, amount))
                .map(Long::valueOf)
                .orElseThrow(() -> new JsonException("redisson command [HINCRBY] error"));
    }

    @Override
    public Long hDecr(String key, String field, long amount) {
        return hIncr(key, field, -amount);
    }

    @Override
    public Double hIncr(String key, String field, double amount) {
        return Optional.ofNullable(opsForHash(key).addAndGet(field, amount))
                .map(Double::valueOf)
                .orElseThrow(() -> new JsonException("redisson command [HINCRBYFLOAT] error"));
    }

    @Override
    public Double hDecr(String key, String field, double amount) {
        return hIncr(key, field, -amount);
    }

    //~ Set

    @Override
    public Long sAdd(String key, Object... members) {
        return (long) opsForSet(key).addAllCounted(serialize2List(members));
    }

    @Override
    public <T> Long sAdd(String key, Collection<T> members) {
        return (long) opsForSet(key).addAllCounted(serialize2List(members));
    }

    @Override
    public Long sLen(String key) {
        return (long) opsForSet(key).size();
    }

    @Override
    public <T> Optional<T> sPop(String key, Class<T> type) {
        return Optional.ofNullable(opsForSet(key).removeRandom())
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> Boolean sIsMember(String key, T member) {
        return opsForSet(key).contains(serialize(member));
    }

    @Override
    public <T> Set<T> sMembers(String key, Class<T> type) {
        return Optional.ofNullable(opsForSet(key).readAll())
                .map(rawVal -> deserialize2Set(rawVal, type))
                .orElseGet(HashSet::new);
    }

    @Override
    public <T> Optional<T> sRandomMember(String key, Class<T> type) {
        return Optional.ofNullable(opsForSet(key).random())
                .map(rawVal -> deserialize(rawVal, type));
    }

    @Override
    public <T> Set<T> sRandomMember(String key, int count, Class<T> type) {
        return Optional.ofNullable(opsForSet(key).random(count))
                .map(rawVal -> deserialize2Set(rawVal, type))
                .orElseGet(HashSet::new);
    }

    //~ ZSet

    @Override
    public <T> Boolean zAdd(String key, T value, double score) {
        return opsForZSet(key).add(score, serialize(value));
    }

    @Override
    public <T> Long zAdd(String key, Map<T, Double> values) {
        return (long) opsForZSet(key).addAll(
                values.entrySet().stream()
                        .collect(Collectors.toMap(e -> serialize(e.getKey()), Map.Entry::getValue))
        );
    }

    @Override
    public Long zCount(String key, double min, double max) {
        return (long) opsForZSet(key).count(min, Boolean.TRUE, max, Boolean.FALSE);
    }

    @Override
    public <T> Double zScore(String key, T member) {
        return opsForZSet(key).getScore(serialize(member));
    }

    @Override
    public Boolean zRemove(String key, Object... members) {
        return opsForZSet(key).removeAll(serialize2List(members));
    }

    @Override
    public <T> Boolean zRemove(String key, Collection<T> members) {
        return opsForZSet(key).removeAll(serialize2List(members));
    }

    @Override
    public <T> Long zRank(String key, T member) {
        return Long.valueOf(opsForZSet(key).rank(serialize(member)));
    }

    @Override
    public <T> Long zRevRank(String key, T member) {
        return Long.valueOf(opsForZSet(key).revRank(serialize(member)));
    }

    @Override
    public Long zLen(String key) {
        return (long) opsForZSet(key).size();
    }

    @Override
    public <T> Set<T> zRange(String key, int start, int end, Class<T> type) {
        return Optional.ofNullable(opsForZSet(key).valueRange(start, end))
                .map(rawVal -> deserialize2Set(rawVal, type))
                .orElseGet(HashSet::new);
    }

    @Override
    public <T> Set<T> zRangeByScore(String key, double min, double max, Class<T> type) {
        return Optional.ofNullable(opsForZSet(key).valueRange(min, Boolean.TRUE, max, Boolean.FALSE))
                .map(rawVal -> deserialize2Set(rawVal, type))
                .orElseGet(HashSet::new);
    }

    @Override
    public <T> Set<T> zRevRangeByScore(String key, double min, double max, Class<T> type) {
        return Optional.ofNullable(opsForZSet(key).valueRangeReversed(min, Boolean.TRUE, max, Boolean.FALSE))
                .map(rawVal -> deserialize2Set(rawVal, type))
                .orElseGet(HashSet::new);
    }

    //~ Geo
    @Override
    public Long geoAdd(String key, String pointName, double lon, double lat) {
        if (geoCheck(lon, lat)) {
            return opsForGeo(key).add(lon, lat, pointName);
        }
        return 0L;
    }

    @Override
    public Long geoAdd(String key, Map<String, RedisGeoPoint> pointMap) {
        List<GeoEntry> geoEntryList = new ArrayList<>();
        pointMap.forEach((pointName, value) -> {
            //过滤不合法的经纬度点
            if (geoCheck(value.getLon(), value.getLat())) {
                //转成data-redis包里的对象
                geoEntryList.add(new GeoEntry(value.getLon(), value.getLat(), pointName));
            }
        });
        return Optional.of(geoEntryList)
                .filter(CollUtil::isNotEmpty)
                .map(list -> opsForGeo(key).add(list.toArray(new GeoEntry[0])))
                .orElse(0L);
    }

    @Override
    public Optional<Double> geoDist(String key, String pointOneName, String pointTwoName, RedisMetric meters) {
        return geoDist(key, pointOneName, pointTwoName, RedisRedissonMetricRelevance.convert(meters));
    }

    /**
     * 计算指定名字的两个点之间的距离
     *
     * @param key          key名
     * @param pointOneName 第一个点的名字
     * @param pointTwoName 第二个点的名字
     * @return Double类型的以公里为单位的距离值, 如果第一个点或第二个点已被删除, 则返回 {@link Optional#empty()}
     */
    private Optional<Double> geoDist(String key, String pointOneName, String pointTwoName, GeoUnit metric) {
        return Optional.ofNullable(opsForGeo(key).dist(pointOneName, pointTwoName, metric));
    }

    @Override
    public List<String> geoHash(String key, String... pointNames) {
        return Optional.ofNullable(opsForGeo(key).hash(pointNames))
                .map(hashMap -> Arrays.stream(pointNames).map(hashMap::get).collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<String> geoHash(String key, Collection<String> pointNames) {
        return Optional.ofNullable(opsForGeo(key).hash(pointNames.toArray(new String[0])))
                .map(hashMap -> pointNames.stream().map(hashMap::get).collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<RedisGeoPoint> geoPosition(String key, String... pointNames) {
        return Optional.ofNullable(opsForGeo(key).pos(pointNames))
                .map(posMap -> Arrays.stream(pointNames)
                        .map(posMap::get)
                        .map(pos -> new RedisGeoPoint(pos.getLongitude(), pos.getLatitude()))
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<RedisGeoPoint> geoPosition(String key, Collection<String> pointNames) {
        return Optional.ofNullable(opsForGeo(key).pos(pointNames.toArray(new String[0])))
                .map(posMap -> pointNames.stream()
                        .map(posMap::get)
                        .map(pos -> new RedisGeoPoint(pos.getLongitude(), pos.getLatitude()))
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    @Override
    public Map<String, RedisGeoResult> geoRadius(String key, String pointName, double distance, long limit, boolean isSortAsc, RedisMetric meters) {
        OptionalGeoSearch args = GeoSearchArgs.from(pointName)
                //指定距离与单位，按m计算或km计算
                .radius(distance, RedisRedissonMetricRelevance.convert(meters))
                //是否由近到远还是由远到近排序
                .order(isSortAsc ? GeoOrder.ASC : GeoOrder.DESC);

        //限定数量,大于0时生效，等于0不限
        if (limit > 0L) {
            args.countAny((int) limit);
        }

        Map<String, Double> redisResult = opsForGeo(key).searchWithDistance(args);
        return convertToRedisGeoResult(key, redisResult, meters);
    }

    @Override
    public Map<String, RedisGeoResult> geoRadius(String key, double lon, double lat, double distance, long limit, boolean isSortAsc, RedisMetric meters) {
        if (geoCheck(lon, lat)) {
            OptionalGeoSearch args = GeoSearchArgs.from(lon, lat)
                    //指定距离与单位，按m计算或km计算
                    .radius(distance, RedisRedissonMetricRelevance.convert(meters))
                    //是否由近到远还是由远到近排序
                    .order(isSortAsc ? GeoOrder.ASC : GeoOrder.DESC);

            //限定数量,大于0时生效，等于0不限
            if (limit > 0L) {
                args.countAny((int) limit);
            }

            Map<String, Double> redisResult = opsForGeo(key).searchWithDistance(args);
            return convertToRedisGeoResult(key, redisResult, meters);
        }
        return new HashMap<>();
    }

    /**
     * 仅提取业务需要用到的字段
     *
     * @param key         redis geo集合key名
     * @param redisResult 原生data-redis获取回来的geo结构体
     * @return 通用封装仅业务所需字段的Map列表
     */
    private Map<String, RedisGeoResult> convertToRedisGeoResult(String key, Map<String, Double> redisResult, RedisMetric meters) {
        Map<String, RedisGeoResult> resultMap = new HashMap<>();
        if (CollUtil.isNotEmpty(redisResult)) {
            //查询经纬度
            Map<String, GeoPosition> posMap = opsForGeo(key).pos(redisResult.keySet().toArray(new String[0]));
            //赋值
            redisResult.forEach((posName, dis) ->
                    Optional.ofNullable(posMap.get(posName))
                            .map(pos -> RedisGeoResult.of(dis, meters, pos.getLatitude(), pos.getLatitude()))
                            .ifPresent(res -> resultMap.put(posName, res)));
        }
        return resultMap;
    }

    @Override
    public Boolean geoRemove(String key, String... pointNames) {
        return geoRemove(key, serialize2List(pointNames));
    }

    @Override
    public Boolean geoRemove(String key, Collection<String> pointNames) {
        return opsForGeo(key).removeAll(pointNames);
    }

    //~ Commons

    @Override
    public void del(String... keys) {
        redissonClient.getKeys().delete(keys);
    }

    @Override
    public void del(Collection<String> keys) {
        del(keys.toArray(new String[0]));
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit timeunit) {
        return redissonClient.getKeys().expire(key, timeout, timeunit);
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
        return opsForValue(key).isExists();
    }

    @Override
    public long ttl(String key, TimeUnit timeunit) {
        long time = opsForValue(key).remainTimeToLive();
        return time > 0 ? TimeUnit.MILLISECONDS.convert(time, timeunit) : time;
    }

    @Override
    public Set<String> sacn(String pattern) {
        Set<String> keys = new HashSet<>();
        redissonClient.getKeys()
                .getKeysByPattern(pattern, 1000)
                .forEach(keys::add);
        return keys;
    }

    @Override
    public void scan(String pattern, Consumer<String> action) {
        redissonClient.getKeys()
                .getKeysByPattern(pattern, 1000)
                .forEach(action);
    }

    //~ Base
    private RBucket<String> opsForValue(String key) {
        return redissonClient.getBucket(key);
    }

    private RList<String> opsForList(String key) {
        return redissonClient.getList(key);
    }

    private RDeque<String> opsForDeque(String key) {
        return redissonClient.getDeque(key);
    }

    private RMap<String, String> opsForHash(String key) {
        return redissonClient.getMap(key);
    }

    private RSet<String> opsForSet(String key) {
        return redissonClient.getSet(key);
    }

    private RScoredSortedSet<String> opsForZSet(String key) {
        return redissonClient.getScoredSortedSet(key);
    }

    private RGeo<String> opsForGeo(String key) {
        return redissonClient.getGeo(key);
    }
}
