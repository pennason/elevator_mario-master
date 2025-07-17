package com.shmashine.socket.utils;

import static com.shmashine.common.constants.RedisConstants.SENSOR_FAULT_SHIELD_CACHE;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.shmashine.socket.redis.utils.RedisUtils;

/**
 * 传感器关联故障屏蔽
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/12/14 11:23
 */
@Component
public class SensorFaultShieldedUtil {

    @Resource
    private RedisUtils redisUtils;


    /**
     * 是否屏蔽
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     */
    public boolean checkShielded(String elevatorCode, String faultType) {

        List<String> sh = redisUtils.getCacheList(SENSOR_FAULT_SHIELD_CACHE + elevatorCode);

        if (sh == null) {
            return false;
        }

        return sh.contains(faultType);
    }
}
