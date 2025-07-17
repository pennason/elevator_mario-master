package com.shmashine.hkcamerabyys.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 电动车入梯故障记录临时表
 *
 * @author jiangheng
 * @version V1.0.0 - 2024/2/28 15:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UncivilizedBehavior37Fault implements Serializable {

    /**
     * 临时故障id
     */
    private String faultId;

    /**
     * 电梯ID
     */
    private String elevatorId;

    /**
     * 电梯ID
     */
    private String elevatorCode;

    /**
     * 电梯安装地址
     */
    private String address;

    /**
     * 故障上报时间
     */
    private Date reportTime;

    /**
     * 故障结束时间
     */
    private Date endTime;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 故障名称
     */
    private String faultName;

    /**
     * 故障级别（严重Lv1、重要Lv2、中等Lv3、普通Lv4）
     */
    private Integer level;

    /**
     * 故障级别名称
     */
    private String levelName;

    /**
     * 故障报文
     * {"elevatorCode":"MX3820","ST":"add","uncivilizedBehaviorFlag":1,"D":"Sn3eYAEJAAAAAQEAAQAAAAEAACU=",
     * "TY":"Fault","sensorType":"CarRoof","fault_type":37,"faultId":"8099132193508491264",
     * "time":"2021-07-02 10:43:22","faultName":"电动车乘梯"}
     */
    private String faultMessage;

    /**
     * 故障次数
     */
    private Integer faultNum;

    /**
     * 不文明行为标识，0：故障，1：不文明行为
     */
    private Integer uncivilizedBehaviorFlag;

    /**
     * 服务模式(0:正常运行, 1:停止服务，2:检修模式)
     */
    private Integer modeStatus;

    /**
     * 状态（0:故障中、1:已恢复）
     */
    private Integer status;

    /**
     * 确认标识 0-未确认，1-已确认故障， 2-已确认非故障, 3-自动消除
     */
    private Integer confirmStatus;

    /**
     * 故障楼层
     */
    private Integer floor;

    private String vAddress;
    private String vFaultName;
}
