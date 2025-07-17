package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * (TblWorkOrder)实体类
 *
 * @author little.li
 * @since 2020-06-19 09:17:27
 */
public class TblWorkOrder implements Serializable {

    private static final long serialVersionUID = 223958430485639212L;

    /**
     * 工单id
     */
    private String vWorkOrderId;
    /**
     * 工单类型 1：故障工单 2：困人工单 3：维保工单 4: 设备工单
     */
    private Integer iWorkOrderType;
    /**
     * 电梯id
     */
    private String vElevatorId;
    /**
     * 电梯编号
     */
    private String vElevatorCode;
    /**
     * 来源id（故障id）用户生成的工单source_id为0
     */
    private String vSourceId;
    /**
     * 来源类型 1：系统生成，2：用户生成
     */
    private Integer iSourceType;
    /**
     * 故障类型
     */
    private String iFaultType;

    /**
     * 故障类型
     */
    private String iFaultTypeName;

    /**
     * 维保类型
     */
    private Integer iMaintenanceType;
    /**
     * 当前处理状态 1：创建（系统生成），2：创建（用户生成），3：处理中（指派），4：处理中（驳回），5：处理中（转交），6：处理中（跨部门转交），7：结束（已取消），8：结束（已完成）,9：系统结束（故障消除后工单结束）
     */
    private Integer iHandleStatus;

    /**
     * 当前处理状态 1：创建（系统生成），2：创建（用户生成），3：处理中（指派），4：处理中（驳回），5：处理中（转交），6：处理中（跨部门转交），7：结束（已取消），8：结束（已完成）,9：系统结束（故障消除后工单结束）
     */
    private String iHandleStatusName;
    /**
     * 当前处理人
     */
    private String iHandleUserId;
    /**
     * 最新详情id
     */
    private String vRecentDetailId;
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
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer iDelFlag;

    @JsonProperty("iFaultTypeName")
    public String getiFaultTypeName() {
        return iFaultTypeName;
    }

    public void setiFaultTypeName(String iFaultTypeName) {
        this.iFaultTypeName = iFaultTypeName;
    }

    @JsonProperty("iHandleStatusName")
    public String getiHandleStatusName() {
        return iHandleStatusName;
    }

    public void setiHandleStatusName(String iHandleStatusName) {
        this.iHandleStatusName = iHandleStatusName;
    }

    @JsonProperty("vWorkOrderId")
    public String getVWorkOrderId() {
        return vWorkOrderId;
    }

    public void setVWorkOrderId(String vWorkOrderId) {
        this.vWorkOrderId = vWorkOrderId;
    }

    @JsonProperty("iWorkOrderType")
    public Integer getIWorkOrderType() {
        return iWorkOrderType;
    }

    public void setIWorkOrderType(Integer iWorkOrderType) {
        this.iWorkOrderType = iWorkOrderType;
    }

    @JsonProperty("vElevatorId")
    public String getVElevatorId() {
        return vElevatorId;
    }

    public void setVElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    @JsonProperty("vElevatorCode")
    public String getVElevatorCode() {
        return vElevatorCode;
    }

    public void setVElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    @JsonProperty("vSourceId")
    public String getVSourceId() {
        return vSourceId;
    }

    public void setVSourceId(String vSourceId) {
        this.vSourceId = vSourceId;
    }

    @JsonProperty("iSourceType")
    public Integer getISourceType() {
        return iSourceType;
    }

    public void setISourceType(Integer iSourceType) {
        this.iSourceType = iSourceType;
    }

    @JsonProperty("iFaultType")
    public String getIFaultType() {
        return iFaultType;
    }

    public void setIFaultType(String iFaultType) {
        this.iFaultType = iFaultType;
    }

    @JsonProperty("iMaintenanceType")
    public Integer getIMaintenanceType() {
        return iMaintenanceType;
    }

    public void setIMaintenanceType(Integer iMaintenanceType) {
        this.iMaintenanceType = iMaintenanceType;
    }

    @JsonProperty("iHandleStatus")
    public Integer getIHandleStatus() {
        return iHandleStatus;
    }

    public void setIHandleStatus(Integer iHandleStatus) {
        this.iHandleStatus = iHandleStatus;
    }

    @JsonProperty("iHandleUserId")
    public String getIHandleUserId() {
        return iHandleUserId;
    }

    public void setIHandleUserId(String iHandleUserId) {
        this.iHandleUserId = iHandleUserId;
    }

    @JsonProperty("vRecentDetailId")
    public String getVRecentDetailId() {
        return vRecentDetailId;
    }

    public void setVRecentDetailId(String vRecentDetailId) {
        this.vRecentDetailId = vRecentDetailId;
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

    @JsonProperty("iDelFlag")
    public Integer getIDelFlag() {
        return iDelFlag;
    }

    public void setIDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

}