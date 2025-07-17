package com.shmashine.socket.message.bean;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;

/**
 * 设备上报信息抽象类
 *
 * @param <T> 监控消息对象
 * @author little.li
 */
@SuppressWarnings("checkstyle:MemberName")
@Data
public class MessageData<T> {


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
    private T monitorMessage;

    /**
     * 请求ID， 可以根据请求ID来判断消息返回
     */
    private String requestId;

}
