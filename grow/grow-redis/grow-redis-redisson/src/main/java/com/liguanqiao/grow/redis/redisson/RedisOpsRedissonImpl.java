package com.liguanqiao.grow.redis.redisson;

import cn.hutool.core.collection.CollUtil;
import com.liguanqiao.grow.json.JsonException;
import com.liguanqiao.grow.redis.RedisOps;
import com.liguanqiao.grow.redis.model.RedisGeoPoint;
import com.liguanqiao.grow.redis.model.RedisGeoResult;
import com.liguanqiao.grow.redis.model.RedisMetric;
import com.liguanqiao.grow.redis.redisson.model.RedisRedissonMetricRelevance;
import com.liguanqiao.grow.redis.util.RedisSerializeUtil;
import lombok.AllArgsConstructor;
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
 * @author liguanqiao
 * @since 2023/1/4
 **/
@AllArgsConstructor
public class RedisOpsRedissonImpl implements RedisOps {

    private final RedissonClient redissonClient;

    //~ String
    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        return toType(get(key), type);
    }

    @Override
    public String get(String key) {
        return opsForValue(key).get();
    }

    @Override
    public <T> void set(String key, T value) {
        opsForValue(key).set(toString(value));
    }

    @Override
    public <T> Optional<T> getAndSet(String key, T value, Class<T> type) {
        return toType(getAndSet(key, toString(value)), type);
    }

    @Override
    public String getAndSet(String key, String value) {
        return opsForValue(key).getAndSet(value);
    }

    @Override
    public <T> Optional<T> getRange(String key, long start, long end, Class<T> type) {
        return toType(getRange(key, start, end), type);
    }

    @Override
    @SneakyThrows
    public String getRange(String key, long start, long end) {
        ByteBuffer byteBuf = ByteBuffer.wrap(new byte[]{}, (int) start, (int) end);
        redissonClient.getBinaryStream(key).getChannel().read(byteBuf);
        return StandardCharsets.UTF_8.decode(byteBuf).toString();
    }

    @Override
    @SneakyThrows
    public <T> void setRange(String key, T value, long offset) {
        byte[] array = toString(value).getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuf = ByteBuffer.wrap(array, (int) offset, array.length);
        redissonClient.getBinaryStream(key).getChannel().write(byteBuf);
    }

    @Override
    public <T> void setEx(String key, T value, long timeout, TimeUnit timeunit) {
        opsForValue(key).set(toString(value), timeout, timeunit);
    }

    @Override
    public <T> Boolean setNx(String key, T value) {
        return opsForValue(key).setIfAbsent(toString(value));
    }

    @Override
    public <T> Boolean setNx(String key, T value, long timeout, TimeUnit timeunit) {
        return opsForValue(key).setIfAbsent(toString(value), Duration.ofMillis(timeunit.toMillis(timeout)));
    }

    @Override
    public <T> List<T> mGet(Class<T> type, String... keys) {
        return Optional.ofNullable(mGet(keys))
                .map(list -> list.stream().map(RedisSerializeUtil.castType(type)).collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<String> mGet(String... keys) {
        return new ArrayList<>(redissonClient.getBuckets().<String>get(keys).values());
    }

    @Override
    public <T> void mSet(Map<String, T> map) {
        redissonClient.getBuckets().set(map.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey,
                        RedisSerializeUtil.valueToString().compose(Map.Entry::getValue))));
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
    public <T> Optional<T> lPop(String key, Class<T> type) {
        return toType(lPop(key), type);
    }

    @Override
    public String lPop(String key) {
        return opsForDeque(key).pollFirst();
    }

    @Override
    public <T> Optional<T> rPop(String key, Class<T> type) {
        return toType(rPop(key), type);
    }

    @Override
    public String rPop(String key) {
        return opsForDeque(key).pollLast();
    }

    @Override
    public Long lPush(String key, Object... values) {
        return (long) opsForDeque(key).addFirst(toStrArray(values));
    }

    @Override
    public List<String> lRange(String key, long start, long end) {
        return opsForList(key).range((int) start, (int) end);
    }

    @Override
    public <T> List<T> lRange(String key, long start, long end, Class<T> type) {
        return toType(lRange(key, start, end), type);
    }

    @Override
    public Long rPush(String key, Object... values) {
        return (long) opsForDeque(key).addLast(toStrArray(values));
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
    public <T> Optional<T> hGet(String key, String field, Class<T> type) {
        return toType(hGet(key, field), type);
    }

    @Override
    public String hGet(String key, String field) {
        return opsForHash(key).get(field);
    }

    @Override
    public <T> List<T> hMGet(String key, Class<T> type, String... fields) {
        return toType(hMGet(key, fields), type);
    }

    @Override
    public List<String> hMGet(String key, String... fields) {
        return new ArrayList<>(opsForHash(key).getAll(Arrays.stream(fields).collect(Collectors.toSet())).values());
    }

    @Override
    public <T, K, V> Map<K, V> hMGet(String key, Collection<String> fields, Class<T> type, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return opsForHash(key).getAll(new HashSet<>(fields)).values()
                .stream()
                .filter(Objects::nonNull)
                .map(RedisSerializeUtil.castType(type))
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }

    @Override
    public <T> void hSet(String key, String field, T value) {
        opsForHash(key).put(field, toString(value));
    }

    @Override
    public <T> void hMSet(String key, Map<String, T> fieldValues) {
        opsForHash(key).putAll(fieldValues.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, RedisSerializeUtil.valueToString().compose(Map.Entry::getValue))));
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
        return hGetAll(key).entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, RedisSerializeUtil.castType(type).compose(Map.Entry::getValue)));
    }

    @Override
    public Map<String, String> hGetAll(String key) {
        return opsForHash(key).readAllMap();
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
        return (long) opsForSet(key).addAllCounted(toStrList(members));
    }

    @Override
    public Long sLen(String key) {
        return (long) opsForSet(key).size();
    }

    @Override
    public String sPop(String key) {
        return opsForSet(key).removeRandom();
    }

    @Override
    public <T> Optional<T> sPop(String key, Class<T> type) {
        return toType(sPop(key), type);
    }

    @Override
    public <T> Boolean sIsMember(String key, T member) {
        return opsForSet(key).contains(toString(member));
    }

    @Override
    public Set<String> sMembers(String key) {
        return opsForSet(key).readAll();
    }

    @Override
    public String sRandomMember(String key) {
        return opsForSet(key).random();
    }

    @Override
    public <T> Optional<T> sRandomMember(String key, Class<T> type) {
        return toType(sRandomMember(key), type);
    }

    @Override
    public Set<String> sRandomMember(String key, int count) {
        return opsForSet(key).random(count);
    }

    @Override
    public <T> Set<T> sRandomMember(String key, int count, Class<T> type) {
        return toType(sRandomMember(key, count), type);
    }

    //~ ZSet

    @Override
    public <T> Boolean zAdd(String key, T value, double score) {
        return opsForZSet(key).add(score, toString(value));
    }

    @Override
    public Long zCount(String key, double min, double max) {
        return (long) opsForZSet(key).count(min, Boolean.TRUE, max, Boolean.FALSE);
    }

    @Override
    public <T> Double zScore(String key, T member) {
        return opsForZSet(key).getScore(toString(member));
    }

    @Override
    public Boolean zRemove(String key, Object... members) {
        return opsForZSet(key).remove(toStrList(members));
    }

    @Override
    public <T> Long zRank(String key, T member) {
        return Long.valueOf(opsForZSet(key).rank(toString(member)));
    }

    @Override
    public <T> Long zRevRank(String key, T member) {
        return Long.valueOf(opsForZSet(key).revRank(toString(member)));
    }

    @Override
    public Long zLen(String key) {
        return (long) opsForZSet(key).size();
    }

    @Override
    public Set<String> zRange(String key, long start, long end) {
        return Optional.ofNullable(opsForZSet(key).valueRange((int) start, (int) end))
                .map(HashSet::new)
                .orElseGet(HashSet::new);
    }

    @Override
    public <T> Set<T> zRange(String key, long start, long end, Class<T> type) {
        return toType(zRange(key, start, end), type);
    }

    @Override
    public Set<String> zRangeByScore(String key, double min, double max) {
        return Optional.ofNullable(opsForZSet(key).valueRange(min, Boolean.TRUE, max, Boolean.FALSE))
                .map(HashSet::new)
                .orElseGet(HashSet::new);
    }

    @Override
    public <T> Set<T> zRangeByScore(String key, double min, double max, Class<T> type) {
        return toType(zRangeByScore(key, min, max), type);
    }

    @Override
    public Set<String> zRevRangeByScore(String key, double min, double max) {
        return Optional.ofNullable(opsForZSet(key).valueRangeReversed(min, Boolean.TRUE, max, Boolean.FALSE))
                .map(HashSet::new)
                .orElseGet(HashSet::new);
    }

    @Override
    public <T> Set<T> zRevRangeByScore(String key, double min, double max, Class<T> type) {
        return toType(zRevRangeByScore(key, min, max), type);
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
    public List<RedisGeoPoint> geoPosition(String key, String... pointNames) {
        return Optional.ofNullable(opsForGeo(key).pos(pointNames))
                .map(posMap -> Arrays.stream(pointNames)
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
        return opsForGeo(key).removeAll(toStrList(pointNames));
    }

    //~ Commons

    @Override
    public void del(String... keys) {
        redissonClient.getKeys().delete(keys);
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
    public boolean exists(String key) {
        return opsForValue(key).isExists();
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

    private <T> Optional<T> toType(String value, Class<T> type) {
        return Optional.ofNullable(value)
                .map(RedisSerializeUtil.castType(type));
    }

    @SuppressWarnings("unchecked")
    private <T, C extends Collection<T>> C toType(Collection<String> values, Class<T> type) {
        return (C) values.stream()
                .map(RedisSerializeUtil.castType(type))
                .collect(Collectors.toCollection(() -> {
                    try {
                        return values.getClass().newInstance();
                    } catch (Throwable var3) {
                        throw new IllegalStateException("Exception handler must throw a RuntimeException", var3);
                    }
                }));
    }

    private <T> String toString(T value) {
        return RedisSerializeUtil.valueToString().apply(value);
    }


    private <T> List<String> toStrList(T[] values) {
        return Arrays.stream(values)
                .map(this::toString)
                .collect(Collectors.toList());
    }

    private <T> String[] toStrArray(T[] values) {
        return Arrays.stream(values)
                .map(this::toString)
                .toArray(String[]::new);
    }
}
