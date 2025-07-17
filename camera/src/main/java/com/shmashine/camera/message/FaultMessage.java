package com.shmashine.camera.message;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 故障消息
 * {"elevatorCode":"MX000488","ST":"add","uncivilizedBehaviorFlag":0,"D":"GKlyXwEOAAAAAQEACwBkAAAAABg=",
 * "TY":"Fault","sensorType":"CarRoof","fault_type":4,"faultId":"7999123676985884672",
 * "time":"2020-09-29 11:25:14","faultName":"运行中开门"}
 *
 * @author little.li
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaultMessage implements Serializable {

    /**
     * 电梯code
     */
    private String elevatorCode;

    /**
     * 消息类型
     */
    private String TY = "Fault";

    /**
     * 消息子类型
     */
    private String ST;

    /**
     * 故障ID（业务流水号）
     */
    private String faultId;

    /**
     * 故障类型
     */
    private String fault_type;

    /**
     * 故障名称
     */
    private String faultName;

    /**
     * 不文明行为标识 0：非不文明行为， 1：不文明行为
     */
    private int uncivilizedBehaviorFlag;

    /**
     * 设备类型 Cube：轿顶（初版设备），CarRoof：轿顶，MotorRoom：机房，FRONT：前装设备
     */
    private String sensorType;

    /**
     * 故障上报时间
     */
    private String time;

    /**
     * 故障上报时的监控报文
     */
    private MonitorMessage monitorMessage;

}
