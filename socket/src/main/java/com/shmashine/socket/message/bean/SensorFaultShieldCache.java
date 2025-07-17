package com.shmashine.socket.message.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 传感器故障屏蔽规则 - 本地缓存
 *
 * @author little.li
 */
@Slf4j
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class SensorFaultShieldCache {

    @ExcelProperty("电梯code")
    private String elevatorCode;
    @ExcelProperty("传感器故障屏蔽列表")
    private String sensorFailureShields;
    @ExcelProperty("故障屏蔽列表")
    private String failureShields;

    public static Map<String, List<Integer>> SENSOR_FAILURE_SHIELD_MAP = new ConcurrentHashMap<>();

    public static Map<String, List<Integer>> FAILURE_SHIELD_MAP = new ConcurrentHashMap<>();

    /**
     * 刷新传感器故障屏蔽规则
     *
     * @param cacheMap 屏蔽规则缓存
     */
    public static void loadSensorFaultShieldCache(Map<String, List<Integer>> cacheMap) {
        try {
            SENSOR_FAILURE_SHIELD_MAP.clear();
            SENSOR_FAILURE_SHIELD_MAP.putAll(cacheMap);
        } catch (Exception e) {
            log.error("刷新传感器关联故障屏蔽-错误,error:{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 刷新传感器关联故障屏蔽
     *
     * @param cacheMap 屏蔽规则缓存
     */
    public static void loadFaultShieldCache(Map<String, List<Integer>> cacheMap) {
        try {
            FAILURE_SHIELD_MAP.clear();
            FAILURE_SHIELD_MAP.putAll(cacheMap);
        } catch (Exception e) {
            log.error("刷新传感器关联故障屏蔽-错误,error:{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 添加传感器故障屏蔽规则
     */
    public static void addSensorFaultShieldCache(String elevatorCode, List<Integer> faultTypes) {
        try {
            SENSOR_FAILURE_SHIELD_MAP.put(elevatorCode, faultTypes);
        } catch (Exception e) {
            log.error("添加传感器故障屏蔽规则-错误,error:{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 添加传感器关联故障屏蔽规则
     */
    public static void addFaultShieldCache(String elevatorCode, List<Integer> faultTypes) {
        try {
            FAILURE_SHIELD_MAP.put(elevatorCode, faultTypes);
        } catch (Exception e) {
            log.error("添加传感器关联故障屏蔽规则-错误,error:{}", ExceptionUtils.getStackTrace(e));
        }
    }


    /**
     * 删除电梯传感器故障屏蔽
     *
     * @param elevatorCode 电梯编号
     */
    public static void removeSensorFaultShieldCache(String elevatorCode) {
        try {
            SENSOR_FAILURE_SHIELD_MAP.remove(elevatorCode);
        } catch (Exception e) {
            log.error("删除电梯传感器故障屏蔽-错误,error:{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 删除电梯传感器故障屏蔽
     *
     * @param elevatorCode 电梯编号
     */
    public static void removeSensorFaultShieldCache(String elevatorCode, Integer value) {
        try {
            SENSOR_FAILURE_SHIELD_MAP.remove(elevatorCode, value);
        } catch (Exception e) {
            log.error("删除电梯传感器故障屏蔽-错误,error:{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 删除电梯传感器关联故障屏蔽
     *
     * @param elevatorCode 电梯编号
     */
    public static void removeFaultShieldCache(String elevatorCode) {
        try {
            FAILURE_SHIELD_MAP.remove(elevatorCode);
        } catch (Exception e) {
            log.error("删除电梯传感器关联故障屏蔽-错误,error:{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 删除电梯传感器故障屏蔽
     *
     * @param elevatorCode 电梯编号
     */
    public static void removeFaultShieldCache(String elevatorCode, Integer value) {
        try {
            FAILURE_SHIELD_MAP.remove(elevatorCode, value);
        } catch (Exception e) {
            log.error("删除电梯传感器故障屏蔽-错误,error:{}", ExceptionUtils.getStackTrace(e));
        }
    }


    /**
     * 校验传感器故障是否被屏蔽
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @return 是否屏蔽
     */
    public static boolean checkSensorFaultShield(String elevatorCode, Integer faultType) {

        List<Integer> list;

        list = SENSOR_FAILURE_SHIELD_MAP.get(elevatorCode);
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }

        return list.contains(faultType);

    }

    /**
     * 校验传感器关联故障是否被屏蔽
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @return 是否屏蔽
     */
    public static boolean checkFaultShield(String elevatorCode, Integer faultType) {

        List<Integer> list;

        list = FAILURE_SHIELD_MAP.get(elevatorCode);
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }

        return list.contains(faultType);

    }

    /**
     * 获取所有故障屏蔽
     *
     * @param elevatorCode 电梯编号
     */
    public static Map<String, List<Integer>> getFaultShields(String elevatorCode) {

        if (!StringUtils.hasText(elevatorCode)) {

            return FAILURE_SHIELD_MAP;
        }

        HashMap<String, List<Integer>> faultShields = new HashMap<>();

        faultShields.put(elevatorCode, FAILURE_SHIELD_MAP.get(elevatorCode));

        return faultShields;

    }

    /**
     * 获取所有传感器故障屏蔽
     *
     * @param elevatorCode 电梯编号
     */
    public static Map<String, List<Integer>> getSensorFaultShields(String elevatorCode) {

        if (!StringUtils.hasText(elevatorCode)) {

            return SENSOR_FAILURE_SHIELD_MAP;
        }

        HashMap<String, List<Integer>> faultShields = new HashMap<>();

        faultShields.put(elevatorCode, SENSOR_FAILURE_SHIELD_MAP.get(elevatorCode));

        return faultShields;

    }
}
