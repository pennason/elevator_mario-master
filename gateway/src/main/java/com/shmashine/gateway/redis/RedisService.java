package com.shmashine.gateway.redis;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shmashine.gateway.redis.utils.RedisKeyUtils;
import com.shmashine.gateway.redis.utils.RedisUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * redis服务
 *
 * @author little.li
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RedisService {
    private final RedisUtils redisUtils;

    /**
     * 获取redis中记录设备状态
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 操作结果
     */
    public Map<Object, Object> getDeviceStatus(String elevatorCode, String sensorType) {
        String deviceStatus = RedisKeyUtils.getDeviceStatus(elevatorCode, sensorType);
        return redisUtils.hmget(deviceStatus);
    }


    /**
     * 获取redis中记录电梯状态
     *
     * @param registerNum 电梯注册编号
     * @return 操作结果
     */
    public Map<Object, Object> getRegisterStatus(String registerNum) {
        String registerStatusKey = RedisKeyUtils.getRegisterStatus(registerNum);
        log.info("redis key is {}", registerStatusKey);
        return redisUtils.hmget(registerStatusKey);
    }

}
