// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.kafka.forward.service;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/16 13:52
 * @since v1.0
 */

public interface RedisServiceI {

    /**
     * 缓存kafka监控电梯信息
     *
     * @param elevatorCode 电梯编号
     * @param monitorInfo  监控信息
     */
    void cacheKafkaMonitorElevator(String elevatorCode, String monitorInfo);

    /**
     * 根据电梯编号获取监控信息
     *
     * @param elevatorCode 电梯编号
     * @return 监控信息
     */
    Object getMonitorInfoByElevatorCode(String elevatorCode);

    /**
     * 缓存kafka故障电梯信息
     *
     * @param elevatorCode 电梯编号
     * @param toString     故障信息
     */
    void cacheKafkaFaultElevator(String elevatorCode, String toString);

    /**
     * 根据电梯编号获取故障信息
     *
     * @param elevatorCode 电梯编号
     * @return 故障信息
     */
    Object getFaultInfoByElevatorCode(String elevatorCode);
}
