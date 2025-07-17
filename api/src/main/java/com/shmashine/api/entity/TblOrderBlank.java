package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 配货单实体
 *
 * @author depp
 */
public class TblOrderBlank implements Serializable {

    private static final long serialVersionUID = -7396753528628491578L;

    private String vOrderBlankId;

    private String vProjectId;

    private String vAddress;

    private String vBuildingName;

    private String vCubeNo;

    private String vElevatorCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dtPlanInstallDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dtAssignDate;

    private String vAssigner;

    private Integer iStatus;

    private List<TblOrderBlankForm> orderBlankFormList;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyAt;

    private String vUserId;

    private Integer iDelFlag;

    private String vElevatorId;

    private String vProjectName;

    @JsonProperty("vOrderBlankId")
    public String getvOrderBlankId() {
        return vOrderBlankId;
    }

    public void setvOrderBlankId(String vOrderBlankId) {
        this.vOrderBlankId = vOrderBlankId;
    }

    @JsonProperty("vProjectId")
    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    @JsonProperty("vAddress")
    public String getvAddress() {
        return vAddress;
    }

    public void setvAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    @JsonProperty("vBuildingName")
    public String getvBuildingName() {
        return vBuildingName;
    }

    public void setvBuildingName(String vBuildingName) {
        this.vBuildingName = vBuildingName;
    }

    @JsonProperty("vCubeNo")
    public String getvCubeNo() {
        return vCubeNo;
    }

    public void setvCubeNo(String vCubeNo) {
        this.vCubeNo = vCubeNo;
    }

    @JsonProperty("vElevatorCode")
    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    @JsonProperty("dtPlanInstallDate")
    public Date getDtPlanInstallDate() {
        return dtPlanInstallDate;
    }

    public void setDtPlanInstallDate(Date dtPlanInstallDate) {
        this.dtPlanInstallDate = dtPlanInstallDate;
    }

    @JsonProperty("dtAssignDate")
    public Date getDtAssignDate() {
        return dtAssignDate;
    }

    public void setDtAssignDate(Date dtAssignDate) {
        this.dtAssignDate = dtAssignDate;
    }

    @JsonProperty("vAssigner")
    public String getvAssigner() {
        return vAssigner;
    }

    public void setvAssigner(String vAssigner) {
        this.vAssigner = vAssigner;
    }

    @JsonProperty("iStatus")
    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("dtCreateAt")
    public Date getDtCreateAt() {
        return dtCreateAt;
    }

    public void setDtCreateAt(Date dtCreateAt) {
        this.dtCreateAt = dtCreateAt;
    }

    @JsonProperty("getDtModifyAt")
    public Date getDtModifyAt() {
        return dtModifyAt;
    }

    public void setDtModifyAt(Date dtModifyAt) {
        this.dtModifyAt = dtModifyAt;
    }

    @JsonProperty("vUserId")
    public String getvUserId() {
        return vUserId;
    }

    public void setvUserId(String vUserId) {
        this.vUserId = vUserId;
    }

    @JsonProperty("iDelFlag")
    public Integer getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

    @JsonProperty("orderBlankFormList")
    public List<TblOrderBlankForm> getOrderBlankFormList() {
        return orderBlankFormList;
    }

    public void setOrderBlankFormList(List<TblOrderBlankForm> orderBlankFormList) {
        this.orderBlankFormList = orderBlankFormList;
    }

    @JsonProperty("vElevatorId")
    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    @JsonProperty("vProjectName")
    public String getvProjectName() {
        return vProjectName;
    }

    public void setvProjectName(String vProjectName) {
        this.vProjectName = vProjectName;
    }
}
