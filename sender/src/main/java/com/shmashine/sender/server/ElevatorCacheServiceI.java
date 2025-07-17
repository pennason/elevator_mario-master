// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.server;

import java.util.List;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/27 16:52
 * @since v1.0
 */

public interface ElevatorCacheServiceI {
    /**
     * 缓存监控运行数据
     *
     * @param elevatorCode 电梯编号
     * @param message      运行数据
     * @param <T>          类型
     */
    <T> void setMonitorCache(String elevatorCode, T message);

    /**
     * 获取缓存中的运行数据
     *
     * @param elevatorCode 电梯编号
     * @return 结果
     */
    Object getMonitorCache(String elevatorCode);

    /**
     * 上下线记录
     *
     * @param elevatorCode 电梯编号
     * @param message      上下线信息
     * @param <T>          类型
     */
    <T> void setOnOfflineStatusCache(String elevatorCode, T message);

    /**
     * 获取缓存中的上下新信息
     *
     * @param elevatorCode 电梯编号
     * @return 结果
     */
    String getOnOfflineStatusCache(String elevatorCode);

    /**
     * 缓存故障信息
     *
     * @param elevatorCode 电梯编号
     * @param message      消息
     * @param <T>          类型
     */
    <T> void saveFaultMessage(String elevatorCode, T message);

    /**
     * 获取故障列表
     *
     * @param elevatorCode 电梯编号
     * @return 结果
     */
    List<Object> getFaultMessageCache(String elevatorCode);
}
