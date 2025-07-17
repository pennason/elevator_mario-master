package com.shmashine.userclientapplets.entity;

import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;

/**
 * 故障请求VO
 */
@Data
public class ApiSearchFaultModule extends PageListParams {

    /**
     * 电梯编号
     */
    private String vElevatorCode;

    /**
     * 故障上报时间
     */
    private String dtReportTime;

    /**
     * 故障结束时间
     */
    private String dtEndTime;

    /**
     * 不文明行为标识（0:故障、1:不文明行为）
     */
    private Integer iUncivilizedBehaviorFlag;

    /**
     * 状态（0:实时、1:历史）
     */
    private Integer iStatus;

    /**
     * 故障类型
     */
    private String iFaultType;

    /**
     * 电梯类型
     */
    private Integer iElevatorType;

    /**
     * 事件来源
     */
    private String vEventType;

    /**
     * 前装，后装
     */
    private Integer elevatorInstallType;

    private String vProjectId;

    private String villageId;

    /**
     * 维保状态，0：正常；1：超期
     */
    private Integer overdue;

    private Integer pageIndex;

    private Integer pageSize;

}
