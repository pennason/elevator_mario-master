package com.shmashine.fault.redis.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * redis 工具类
 *
 * @author little.li
 */
@Component
public class RedisUtils {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Resource(name = "hashOperations")
    HashOperations<String, String, String> hashOpsStr;

    @Resource(name = "listOperations")
    ListOperations<String, String> listOpsStr;


    /**
     * 根据指定key获取String
     */
    public String getStr(String key) {
        return valOpsStr.get(key);
    }

    /**
     * 设置Str缓存
     */
    public void setStr(String key, String val) {
        valOpsStr.set(key, val);
    }

    /**
     * 设置Str缓存，指定超时时间
     */
    public void setStr(String key, String val, long time, TimeUnit timeUnit) {
        valOpsStr.set(key, val, time, timeUnit);
    }

    /**
     * 删除指定key
     */
    public void del(String key) {
        stringRedisTemplate.expire(key, -1, TimeUnit.SECONDS);
    }


    /**
     * 设置超时时间
     */
    public void expire(String key, long timeout, TimeUnit unit) {
        stringRedisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置超时时间
     */
    public void expireHm(String key, long timeout, TimeUnit unit) {
        hashOpsStr.getOperations().expire(key, timeout, unit);
    }

    /**
     * 获取超时时间
     */
    public long getExpire(String key, TimeUnit unit) {
        return stringRedisTemplate.getExpire(key, unit);
    }

    /**
     * 获取超时时间
     */
    public long getExpireHm(String key, TimeUnit unit) {
        return hashOpsStr.getOperations().getExpire(key, unit);
    }

    /**
     * 哈希 添加
     */
    public void hmSet(String key, Map<String, String> map) {
        hashOpsStr.putAll(key, map);
    }

    /**
     * 哈希 添加
     */
    public void hmSet(String key, String column, String value) {
        Map map = new HashMap();
        map.put(column, value);
        hashOpsStr.put(key, column, value);
    }

    /**
     * 哈希获取数据
     */
    public String hmGet(String hashKey, String key) {
        return hashOpsStr.get(hashKey, key);
    }

    /**
     * 哈希获取数据
     */
    public Map<String, String> hmGetAll(String key) {
        return hashOpsStr.entries(key);
    }

    /**
     * 列表添加
     */
    public void pushList(String k, String v) {
        listOpsStr.rightPush(k, v);
    }

    /**
     * 列表获取
     */
    public List<String> rangeList(String k, long l, long l1) {
        return listOpsStr.range(k, l, l1);
    }


    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 删除单个对象
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     */
    public long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    ///////////////////////zSet//////////////////////////

    /**
     * 添加数据（zSet）
     *
     * @param key   key
     * @param value 值
     * @param score 分数
     */
    public void addZSet(String key, String value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

}