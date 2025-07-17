package com.shmashine.socket.redis.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * redis 工具类
 *
 * @author little.li
 */
@SuppressWarnings("checkstyle:MethodName")
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Resource(name = "hashOperations")
    HashOperations<String, String, String> hashOpsStr;

    @Resource(name = "listOperations")
    ListOperations<String, String> listOpsStr;

    @Resource(name = "setOperations")
    SetOperations<String, String> setOpsStr;


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
        return redisTemplate.getExpire(key, unit);
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
        hashOpsStr.putAll(key, map);
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
    public Long size(String k) {
        return listOpsStr.size(k);
    }

    /**
     * 列表添加
     */
    public Long lPush(String k, String v) {
        return listOpsStr.leftPush(k, v);
    }

    /**
     * 列表弹出
     */
    public Long rPush(String k, String v) {
        return listOpsStr.rightPush(k, v);
    }

    /**
     * 列表弹出
     */
    public void lPop(String k) {
        listOpsStr.leftPop(k, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * 列表添加
     */
    public void rPop(String k) {
        listOpsStr.rightPop(k, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * 列表获取
     */
    public List<String> lRange(String k, long l, long l1) {
        return listOpsStr.range(k, l, l1);
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
     * set添加
     */
    public void setAdd(String k, String v) {
        setOpsStr.add(k, v);
    }

    /**
     * set删除
     */
    public void setRemove(String k, String v) {
        setOpsStr.remove(k, v);
    }

    /**
     * set获取
     */
    public Set<String> setMembers(String k) {
        return setOpsStr.members(k);
    }


    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

}