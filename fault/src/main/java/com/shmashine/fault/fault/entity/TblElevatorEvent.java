package com.shmashine.fault.fault.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 西子扶梯模式切换记录 实体类
 *
 * @author jiangheng
 * @version 1.0 -2021/5/6 11:46
 */
@Data
public class TblElevatorEvent implements Serializable {

    private static final long serialVersionUID = 454358954564854654L;

    /**
     * 唯一id
     */
    private String id;

    /**
     * 电梯code
     */
    private String elevatorCode;

    /**
     * 当前模式 0：正常 1：检修
     */
    private Integer eventType;

    /**
     * 当前状态 0：正常 1：故障中
     */
    private Integer status;

    /**
     * 故障类型
     */
    private Integer faultType;

    /**
     * 故障名称
     */
    private String faultName;

    /**
     * 创建时间
     */
    private Date creatTime;
}
