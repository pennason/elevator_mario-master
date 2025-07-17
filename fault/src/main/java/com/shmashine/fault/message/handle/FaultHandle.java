package com.shmashine.fault.message.handle;

import java.util.Date;

import com.alibaba.fastjson2.JSONObject;

/**
 * 故障处理
 *
 * @author jiangheng
 * @version V1.0.0 - 2023/7/5 10:37
 */
public interface FaultHandle {

    /**
     * 新增故障
     */
    default void addFault(JSONObject messageJson) {
        //
    }


    /**
     * 消除故障
     */
    default void disappearFault(JSONObject messageJson) {
        //
    }

    /**
     * 生成故障历史取证文件
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @param faultId      故障id
     * @param occurTime    发生时间
     */
    default void saveFaultHistoryFlie(String elevatorCode, String faultType, String faultId,
                                      Date occurTime, String floor) {
        //
    }

    /**
     * 获取待识别图片
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @param faultId      故障id
     */
    default void getImage(String elevatorCode, String faultType, String faultId) {
        //
    }

    /**
     * 添加传感器故障
     */
    default void addSensorFault(JSONObject messageJson) {
        //
    }

    /**
     * 消除传感器故障
     */
    default void disappearSensorFault(JSONObject messageJson) {
        //
    }
}