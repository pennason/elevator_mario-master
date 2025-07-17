package com.shmashine.common.message;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;
import lombok.ToString;

/**
 * 设备上报信息抽象类
 *
 * @author little.li
 */
@Data
@ToString
public class MessageData {


    /**
     * 上报时间（服务端接收到消息时间）
     */
    private String time;

    /**
     * 关联电梯编号
     */
    private String elevatorCode;

    /**
     * 设备类型
     */
    private String sensorType;

    /**
     * 上报信息类型
     */
    private String TY;

    /**
     * 上报信息子类型
     */
    private String ST;

    /**
     * 原始信息
     */
    private JSONObject messageJson;

    /**
     * 监控消息对象
     */
    private MonitorMessage monitorMessage;

    /**
     * 监控消息对象
     */
    private FaultMessage faultMessage;

}
