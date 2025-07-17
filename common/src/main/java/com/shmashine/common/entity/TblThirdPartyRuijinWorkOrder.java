package com.shmashine.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * (TblThirdPartyRuijinWorkOrder)实体类
 *
 * @author makejava
 * @since 2020-07-24 14:44:44
 */
public class TblThirdPartyRuijinWorkOrder implements Serializable {
    @Serial
    private static final long serialVersionUID = -41231283568096732L;
    /**
     * 注册代码
     */
    private String registerNumber;
    /**
     * 工单编号
     */
    private String workOrderNumber;
    /**
     * 合同编号
     */
    private String contractNumber;
    /**
     * 维保单位代码
     */
    private String maintenanceCompanyCode;
    /**
     * 维保单位名称
     */
    private String maintenanceCompanyName;
    /**
     * 保养工单类型编号
     */
    private String orderTypeNumber;
    /**
     * 班组代号
     */
    private String teamCode;
    /**
     * 工单生成日期
     */
    private Object ordeTime;
    /**
     * 工单状态
     */
    private String orderStatus;
    /**
     * 具体处理人员编号
     */
    private String dealEmployeeCode;
    /**
     * 处理人员姓名
     */
    private String dealEmployeeName;
    /**
     * 处理人员电话
     */
    private String dealEmployeeTel;
    /**
     * 应完成日期
     */
    private Object shouldCompleteDate;
    /**
     * 签到时间
     */
    private Object signTime;
    /**
     * 完成时间
     */
    private Date completeTime;
    /**
     * 确认时间
     */
    private Object confirmTime;
    /**
     * 维保项目信息
     */
    private String items;
    /**
     * 故障信息
     */
    private String failureInfo;
    /**
     * 下次维保日期
     */
    private Object nextMaintenanceDate;
    /**
     * 发现的主要问题
     */
    private String issues;
    /**
     * 主要问题处理情况
     */
    private String issueHandling;
    /**
     * 应急救援电话
     */
    private String rescueTel;
    /**
     * 保养基础信息记录编码
     */
    private String wbid;
    /**
     * 工单说明
     */
    private String orderExplain;

    /**
     * 维保是否超期 0:正常，1：超期
     */
    private Integer overdue;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyTime;
    /**
     * 创建记录用户
     */
    private String vCreateUserId;
    /**
     * 修改记录用户
     */
    private String vModifyUserId;

    @JsonProperty("registerNumber")
    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    @JsonProperty("workOrderNumber")
    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    @JsonProperty("contractNumber")
    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    @JsonProperty("maintenanceCompanyCode")
    public String getMaintenanceCompanyCode() {
        return maintenanceCompanyCode;
    }

    public void setMaintenanceCompanyCode(String maintenanceCompanyCode) {
        this.maintenanceCompanyCode = maintenanceCompanyCode;
    }

    @JsonProperty("maintenanceCompanyName")
    public String getMaintenanceCompanyName() {
        return maintenanceCompanyName;
    }

    public void setMaintenanceCompanyName(String maintenanceCompanyName) {
        this.maintenanceCompanyName = maintenanceCompanyName;
    }

    @JsonProperty("orderTypeNumber")
    public String getOrderTypeNumber() {
        return orderTypeNumber;
    }

    public void setOrderTypeNumber(String orderTypeNumber) {
        this.orderTypeNumber = orderTypeNumber;
    }

    @JsonProperty("teamCode")
    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    @JsonProperty("ordeTime")
    public Object getOrdeTime() {
        return ordeTime;
    }

    public void setOrdeTime(Object ordeTime) {
        this.ordeTime = ordeTime;
    }

    @JsonProperty("orderStatus")
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @JsonProperty("dealEmployeeCode")
    public String getDealEmployeeCode() {
        return dealEmployeeCode;
    }

    public void setDealEmployeeCode(String dealEmployeeCode) {
        this.dealEmployeeCode = dealEmployeeCode;
    }

    @JsonProperty("dealEmployeeName")
    public String getDealEmployeeName() {
        return dealEmployeeName;
    }

    public void setDealEmployeeName(String dealEmployeeName) {
        this.dealEmployeeName = dealEmployeeName;
    }

    @JsonProperty("dealEmployeeTel")
    public String getDealEmployeeTel() {
        return dealEmployeeTel;
    }

    public void setDealEmployeeTel(String dealEmployeeTel) {
        this.dealEmployeeTel = dealEmployeeTel;
    }

    @JsonProperty("shouldCompleteDate")
    public Object getShouldCompleteDate() {
        return shouldCompleteDate;
    }

    public void setShouldCompleteDate(Object shouldCompleteDate) {
        this.shouldCompleteDate = shouldCompleteDate;
    }

    @JsonProperty("signTime")
    public Object getSignTime() {
        return signTime;
    }

    public void setSignTime(Object signTime) {
        this.signTime = signTime;
    }

    @JsonProperty("completeTime")
    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    @JsonProperty("confirmTime")
    public Object getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Object confirmTime) {
        this.confirmTime = confirmTime;
    }

    @JsonProperty("items")
    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    @JsonProperty("failureInfo")
    public String getFailureInfo() {
        return failureInfo;
    }

    public void setFailureInfo(String failureInfo) {
        this.failureInfo = failureInfo;
    }

    @JsonProperty("nextMaintenanceDate")
    public Object getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(Object nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    @JsonProperty("issues")
    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    @JsonProperty("issueHandling")
    public String getIssueHandling() {
        return issueHandling;
    }

    public void setIssueHandling(String issueHandling) {
        this.issueHandling = issueHandling;
    }

    @JsonProperty("rescueTel")
    public String getRescueTel() {
        return rescueTel;
    }

    public void setRescueTel(String rescueTel) {
        this.rescueTel = rescueTel;
    }

    @JsonProperty("wbid")
    public String getWbid() {
        return wbid;
    }

    public void setWbid(String wbid) {
        this.wbid = wbid;
    }

    @JsonProperty("orderExplain")
    public String getOrderExplain() {
        return orderExplain;
    }

    public void setOrderExplain(String orderExplain) {
        this.orderExplain = orderExplain;
    }

    @JsonProperty("dtCreateTime")
    public Date getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(Date dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    @JsonProperty("dtModifyTime")
    public Date getDtModifyTime() {
        return dtModifyTime;
    }

    public void setDtModifyTime(Date dtModifyTime) {
        this.dtModifyTime = dtModifyTime;
    }

    @JsonProperty("vCreateUserId")
    public String getVCreateUserId() {
        return vCreateUserId;
    }

    public void setVCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
    }

    @JsonProperty("vModifyUserId")
    public String getVModifyUserId() {
        return vModifyUserId;
    }

    public void setVModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

    public Integer getOverdue() {
        return overdue;
    }

    public void setOverdue(Integer overdue) {
        this.overdue = overdue;
    }
}