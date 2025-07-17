package com.shmashine.api.module.fault.input;

import java.util.List;

import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 故障统计请求对象
 *
 * @author little.li
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FaultStatisticsModule extends PageListParams {


    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 电梯类型
     */
    private Integer elevatorType;

    /**
     * 设备版本
     */
    private Integer sensorVersion;

    /**
     * 不文明行为标识（0:故障、1:不文明行为）
     */
    private Integer uncivilizedBehaviorFlag;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 电梯所属项目
     */
    private String projectId;
    /**
     * 电梯所属项目列表
     */
    private List<String> projectIds;

    /**
     * 是否按照电梯展示
     */
    private String groupByElevator;

    /**
     * 故障上报时间
     */
    private String startTime;

    /**
     * 故障结束时间
     */
    private String endTime;


    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 导出类型
     */
    private String exportType;

    /**
     * 匹配小区
     */
    private String villageId;

    /**
     * 设备类型
     */
    private String eType;

    /**
     * 排序类型(asc, desc)
     */
    private String orderType;

    /**
     * 排序关键字
     */
    private String orderBy;

    /**
     * 时间标准 00 日 11 月 22 年
     */
    private String timeFlag;

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(String timeFlag) {
        this.timeFlag = timeFlag;
    }
    private List<String> elevator;
    private Boolean isAdmin;
}
