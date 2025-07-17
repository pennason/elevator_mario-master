package com.shmashine.socket.message.bean;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * 协议缓存
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/8/30 9:58
 */
@Component
public class ProtocalVersionCache {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 电梯协议
     */
    private static ConcurrentHashMap<String, String> protocalVersions = new ConcurrentHashMap<>();

    private static final String PROTOCOL_VERSION_PREFIX = "MX:DEVICE:PROTOCAL:";

    /**
     * 加载协议
     */
    @PostConstruct
    public void registerHandle() {

        //获取redis缓存
        Set<String> keys = stringRedisTemplate.keys(PROTOCOL_VERSION_PREFIX.concat("*"));

        for (String key : keys) {
            //刷新本地缓存
            String vaulue = stringRedisTemplate.opsForValue().get(key);
            protocalVersions.put(key.substring(19), vaulue);
        }

    }


    /**
     * 刷新协议
     *
     * @param key             key
     * @param protocalVersion 协议版本
     */
    public void refreshProtocalVersion(String key, String protocalVersion) {

        protocalVersions.put(key, protocalVersion);

        stringRedisTemplate.opsForValue().set(PROTOCOL_VERSION_PREFIX + key, protocalVersion);
    }

    /**
     * 获取协议
     *
     * @param key key
     */
    public static String getProtocalVersion(String key) {
        String protocalVersion = protocalVersions.get(key);
        return !StringUtils.hasText(protocalVersion) ? "default" : protocalVersion;
    }
}
