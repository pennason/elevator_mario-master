package com.shmashine.socket.message.handle;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.socket.message.bean.MessageData;

/**
 * 故障消息处理
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/8/29 17:09
 */
public interface FaultHandle {

    /**
     * 获取协议版本
     */
    String getProtocalVersion();

    /**
     * 设备新增/消除故障 处理
     *
     * @param faultType    故障类型 以,分割
     * @param elevatorCode 电梯code
     */
    void process(String faultId, JSONObject messageJson, String elevatorCode, String sensorType, String faultType,
                 String secondType, String stype);

    /**
     * 传感器新增/消除故障 处理
     *
     * @param faultType    故障类型
     * @param elevatorCode 电梯code
     */
    default void sensorProcess(JSONObject messageJson, String elevatorCode, String sensorType,
                               String faultType, String stype) {
    }

    /**
     * ST：clear 手动清除故障处理
     * 手动清除故障 通知设备后，设备的返回status 0 失败，1成功，2故障不存在
     *
     * @param faultJson    故障报文
     * @param faultType    故障类型
     * @param elevatorCode 电梯编号
     */
    void cleanFaultHandle(JSONObject faultJson, String faultType, String elevatorCode);


    /**
     * 处理设备重连后上传的当前电梯故障状态
     *
     * @param faultType    故障类型 以,分割
     * @param elevatorCode 电梯code
     */
    default void updateFaultHandle(String faultType, String faultSecondType, String elevatorCode, String sensorType) {
    }


    /**
     * 收到故障消息后反馈给设备
     *
     * @param messageData messageData
     * @param sensorType  设备类型
     * @param status      0 失败，1 成功
     * @param faultType   故障类型
     */
    void faultResponse(MessageData messageData, String sensorType, int status, String faultType);

    /**
     * 传感器故障反馈
     *
     * @param messageData 反馈消息
     * @param sensorType  传感器类型
     * @param status      状态
     * @param faultType   故障类型
     */
    void sensorFaultResponse(MessageData messageData, String sensorType, int status, String faultType);

    /**
     * 收到模式切换消息反馈
     *
     * @param messageData messageData
     * @param status      状态
     * @param eventType   事件状态
     */
    void eventResponse(MessageData messageData, int status, String eventType);


    /**
     * 将电梯当前故障清除
     *
     * @param elevatorCode 电梯code
     * @param sensorType   设备类型
     */
    void cleanAllFault(String elevatorCode, String sensorType);
}
