package com.shmashine.socket.message.handle;

import com.alibaba.fastjson2.JSONObject;

/**
 * update消息处理
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/8/30 14:07
 */
public interface UpdateHandle {

    /**
     * 获取协议版本
     */
    String getProtocalVersion();

    /**
     * 更新设备ip 返回处理状态
     */
    void ipHandle(JSONObject messageJson, String elevatorCode, String sensorType);


    /**
     * 更新设备frep 返回处理状态
     */
    void frepHandle(JSONObject messageJson, String elevatorCode, String sensorType);


    /**
     * limit处理
     */
    void limitHandle(JSONObject messageJson, String elevatorCode, String sensorType);
}
