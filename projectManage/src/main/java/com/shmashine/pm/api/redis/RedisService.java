package com.shmashine.pm.api.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shmashine.pm.api.redis.utils.RedisKeyUtils;
import com.shmashine.pm.api.redis.utils.RedisUtils;

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

    public void setElevatorStatus(String elevatorId, int status, float time) {
        String key = "elevator:status:" + elevatorId;
        redisUtils.set(key, status, (long) (time * 60 * 60));
    }

    public String getElevatorStatus(String elevatorId) {
        String key = "elevator:status:" + elevatorId;
        return redisUtils.get(key);
    }

    public void setElevatorStatusTimeOut(String elevatorId) {
        String key = "elevator:status:" + elevatorId;
        redisUtils.expire(key, 120);
    }

    public Boolean existsSensorConfigKey(String vElevatorCode, String sensorType) {
        String key = RedisKeyUtils.getSensorConfigKey(vElevatorCode, sensorType);
        return redisUtils.hasKey(key);
    }
}
