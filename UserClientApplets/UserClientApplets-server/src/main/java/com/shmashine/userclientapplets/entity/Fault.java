package com.shmashine.userclientapplets.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Fault
 *
 * @author jiangheng
 * @version V1.0 - 2022/2/9 10:50
 */

@TableName("tbl_fault")
@Data
@EqualsAndHashCode(callSuper = false)
public class Fault extends BaseEntity {

    /**
     * id
     */
    private String faultId;

    /**
     * 电梯id
     */
    private String vElevatorId;

    /**
     * 电梯code
     */
    private String elevatorCode;

    /**
     * 电梯名
     */
    private String elevatorName;

    /**
     * 故障级别（严重Lv1、重要Lv2、中等Lv3、普通Lv4）
     */
    private Integer level;

    /**
     * 电梯安装地址
     */
    private String address;

    /**
     * 上报时间
     */
    private Date reportTime;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 故障名称
     */
    private String faultName;

    /**
     * 故障子类型
     */
    private String faultSecondType;

    /**
     * 故障子类型名称
     */
    private String faultSecondName;

    /**
     * 故障次数
     */
    private Integer faultNumber;

    /**
     * 服务模式(0:正常运行, 1:检修模式，2:停止服务)
     */
    private Integer modeStatus;

    /**
     * 状态（0:故障中、1:已恢复）
     */
    private Integer status;

}
