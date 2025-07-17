package com.shmashine.camera.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shmashine.camera.redis.utils.RedisUtils;
import com.shmashine.common.utils.RedisKeyUtils;

/**
 * redis服务
 *
 * @author little.li
 */
@Component
public class RedisService {


    private final RedisUtils redisUtils;

    @Autowired
    public RedisService(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    public boolean setCameraHistoryStatus(String elevatorCode, String id) {
        String elevatorStatusKey = RedisKeyUtils.getCameraHistoryStatus(elevatorCode);
        return redisUtils.setIfAbsent(elevatorStatusKey, id, 600);
    }

    public String getXiongMaiHlsUrl(String elevatorCode) {
        String xiongMaiHlsKey = RedisKeyUtils.getXiongMaiHlsUrl(elevatorCode);
        String s = redisUtils.get(xiongMaiHlsKey);
        if ("null".equals(s) || StringUtils.isBlank(s)) {
            return null;
        }
        return s;
    }

    public void setXiongMaiHlsUrl(String elevatorCode, String hlsUrl) {
        String xiongMaiHlsKey = RedisKeyUtils.getXiongMaiHlsUrl(elevatorCode);
        redisUtils.set(xiongMaiHlsKey, hlsUrl, 6000);
    }

    public void delXiongMaiHlsUrl(String elevatorCode) {
        String xiongMaiHlsKey = RedisKeyUtils.getXiongMaiHlsUrl(elevatorCode);
        redisUtils.del(xiongMaiHlsKey);
    }

    public void delElevatorCacheForCity(String elevatorCode) {
        redisUtils.del(RedisKeyUtils.getCityPushPlatformElevatorExistsKey(elevatorCode));
        redisUtils.del("elevator_cache:" + elevatorCode);
    }

}
