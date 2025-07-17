package com.shmashine.api.redis;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateUtil;

import com.shmashine.api.redis.utils.RedisUtils;
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

    public void setMobileFrequencyRequest(String mobileNum) {
        String key = "mobile:login:" + mobileNum;
        redisUtils.set(key, new Date().getTime(), (long) 60);
    }

    public String getMobileFrequencyRequest(String mobileNum) {
        String key = "mobile:login:" + mobileNum;
        return redisUtils.get(key);
    }

    // 发送企业微信消息缓存

    public Boolean setWecomUserList(String projectId, String jsonString) {
        var key = buildWecomUserListKey(projectId);
        return redisUtils.set(key, jsonString, (long) 60 * 60 * 24);
    }

    public String getWecomUserList(String projectId) {
        var key = buildWecomUserListKey(projectId);
        return redisUtils.get(key);
    }

    private String buildWecomUserListKey(String projectId) {
        return "PROJECT:WECOM:USER:LIST:" + projectId;
    }

    public Boolean setWecomMessageLogKey(String projectId, String messageType, String message) {
        var key = buildWecomMessageLogKey(projectId, messageType);
        return redisUtils.set(key, message, (long) 60 * 60 * 24);
    }

    public String getWecomMessageLogKey(String projectId, String messageType) {
        var key = buildWecomMessageLogKey(projectId, messageType);
        return redisUtils.get(key);
    }

    private String buildWecomMessageLogKey(String projectId, String messageType) {
        return "PROJECT:WECOM:MESSAGE:" + DateUtil.today() + ":" + messageType + ":" + projectId;
    }
}
