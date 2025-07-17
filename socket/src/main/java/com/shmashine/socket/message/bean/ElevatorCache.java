package com.shmashine.socket.message.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shmashine.socket.elevator.entity.TblElevator;

/**
 * 电梯表 - 本地缓存
 *
 * @author little.li
 */
public class ElevatorCache {


    /**
     * key: 电梯编号
     * value: 电梯对象
     */
    public static Map<String, TblElevator> ELEVATOR_MAP = new ConcurrentHashMap<>();


    /**
     * 刷新电梯表缓存
     */
    public static void reloadCache(Map<String, TblElevator> cacheMap) {
        try {
            ELEVATOR_MAP.clear();
            ELEVATOR_MAP.putAll(cacheMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据电梯编号获取电梯对象
     *
     * @param elevatorCode 电梯编号
     * @return 电梯对象
     */
    public static TblElevator getByElevatorCode(String elevatorCode) {
        return ELEVATOR_MAP.get(elevatorCode);
    }


}
