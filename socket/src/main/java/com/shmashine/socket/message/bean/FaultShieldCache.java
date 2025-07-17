package com.shmashine.socket.message.bean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.CollectionUtils;

import com.shmashine.socket.fault.entity.TblFaultShield;

/**
 * 故障屏蔽规则 - 本地缓存
 *
 * @author little.li
 */
public class FaultShieldCache {


    public static Map<String, List<TblFaultShield>> FAILURE_SHIELD_MAP = new ConcurrentHashMap<>();

    /**
     * 刷新故障屏蔽规则
     *
     * @param cacheMap 规则缓存
     */
    public static void reloadCache(Map<String, List<TblFaultShield>> cacheMap) {
        try {
            FAILURE_SHIELD_MAP = new ConcurrentHashMap<>(cacheMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 校验故障是否被屏蔽
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @return 是否屏蔽 0:不屏蔽，1，北向屏蔽，2：平台屏蔽
     */
    public static int checkFailure(String elevatorCode, String faultType) {

        List<TblFaultShield> list = FAILURE_SHIELD_MAP.get(elevatorCode);
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }

        for (TblFaultShield value : list) {
            if (!String.valueOf(value.getIFaultType()).equals(faultType)) {
                continue;
            }
            if (value.getIIsUserVisible() == 0) {
                return 2;
            }
            if (value.getIIsUserVisible() == 1 && value.getIIsReport() == 0) {
                return 1;
            }
        }


        return 0;
    }
}
