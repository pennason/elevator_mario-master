package com.shmashine.pm.api.entity.dto;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblVillageDeviceBill;

import lombok.Data;
import lombok.ToString;

/**
 * 小区信息
 */

@Data
@ToString
public class TblVillageDto implements Serializable {
    /**
     * 小区id
     */
    private String vVillageId;
    /**
     * 小区名称
     */
    private String vVillageName;
    /**
     * 小区详细地址
     */
    private String vAddress;
    /**
     * 小区所属项目id
     */
    private String vProjectId;
    /**
     * 经度
     */
    private String vLongitude;
    /**
     * 纬度
     */
    private String vLatitude;
    /**
     * 备注
     */
    private String vRemarks;
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

    private String vContactsId;

    private String vInstallerId;

    private String vInvestigatorId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dInvestigateDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dInstallDate;

    private Integer iElevatorCount;

    private Integer iStatus;

    private String vStatusName;

    private TblVillageDeviceBill deviceBill;

    private String vContactsName;

    private String vInvestigatorName;

    private String vInstallerName;

    private String vContactsPhone;

    private String vInvestigatorPhone;

    private String vInstallerPhone;

    private List<TblElevator> elevatorList;

    @JsonProperty("vProjectName")
    private String vProjectName;

    /**
     * 群租识别开启 0：不开启 1：开启中
     */
    private Integer groupLeasingStatus;
    /**
     * 群租 时间系数 0-23  2,2,2,2,1.5,1.5,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1.5,1.5
     */
    private String groupLeasingTimeCoefficient;
    /**
     * 群租 阈值 50, 100, 150, 200,
     */
    private String groupLeasingStepRange;
    /**
     * 群租 开始取证日期
     */
    private Date groupLeasingStartDate;
    /**
     * 群租 结束取证日期 默认30天结束
     */
    private Date groupLeasingEndDate;
    /**
     * 群租 每日取证开始时间
     */
    private Time groupLeasingStartTime;
    /**
     * 群租 每日取证结束时间
     */
    private Time groupLeasingEndTime;
    /**
     * 群租状态确认 0：未确认 1：取证中 2：已确认
     */
    private Integer groupLeasingResult;

    /**
     * 所属小区, 电信需要
     */
    private String street;

    /**
     * 所属居委会
     */
    private String neighborhood;

    /**
     * 群租 电梯的群租系数
     */
    private Map<String, Map<String, Object>> groupLeasingElevatorCoefficient;
    /**
     * 群租 电梯楼层的群租系数
     */
    private Map<String, Map<String, Map<String, Object>>> groupLeasingFloorCoefficient;


    @JsonProperty("vVillageId")
    public String getVVillageId() {
        return vVillageId;
    }

    public void setVVillageId(String vVillageId) {
        this.vVillageId = vVillageId;
    }

    @JsonProperty("vVillageName")
    public String getVVillageName() {
        return vVillageName;
    }

    public void setVVillageName(String vVillageName) {
        this.vVillageName = vVillageName;
    }

    @JsonProperty("vAddress")
    public String getVAddress() {
        return vAddress;
    }

    public void setVAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    @JsonProperty("vProjectId")
    public String getVProjectId() {
        return vProjectId;
    }

    public void setVProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    @JsonProperty("vLongitude")
    public String getVLongitude() {
        return vLongitude;
    }

    public void setVLongitude(String vLongitude) {
        this.vLongitude = vLongitude;
    }

    @JsonProperty("vLatitude")
    public String getVLatitude() {
        return vLatitude;
    }

    public void setVLatitude(String vLatitude) {
        this.vLatitude = vLatitude;
    }

    @JsonProperty("vRemarks")
    public String getVRemarks() {
        return vRemarks;
    }

    public void setVRemarks(String vRemarks) {
        this.vRemarks = vRemarks;
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

    @JsonProperty("vContactsId")
    public String getvContactsId() {
        return vContactsId;
    }

    public void setvContactsId(String vContactsId) {
        this.vContactsId = vContactsId;
    }

    @JsonProperty("vContactsPhone")
    public String getvContactsPhone() {
        return vContactsPhone;
    }

    public void setvContactsPhone(String vContactsPhone) {
        this.vContactsPhone = vContactsPhone;
    }


    @JsonProperty("dInvestigateDate")
    public Date getdInvestigateDate() {
        return dInvestigateDate;
    }

    public void setdInvestigateDate(Date dInvestigateDate) {
        this.dInvestigateDate = dInvestigateDate;
    }

    @JsonProperty("dInstallDate")
    public Date getdInstallDate() {
        return dInstallDate;
    }

    public void setdInstallDate(Date dInstallDate) {
        this.dInstallDate = dInstallDate;
    }

    @JsonProperty("iElevatorCount")
    public Integer getiElevatorCount() {
        return iElevatorCount;
    }

    public void setiElevatorCount(Integer iElevatorCount) {
        this.iElevatorCount = iElevatorCount;
    }

    @JsonProperty("iStatus")
    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("deviceBill")
    public TblVillageDeviceBill getDeviceBill() {
        return deviceBill;
    }

    public void setDeviceBill(TblVillageDeviceBill deviceBill) {
        this.deviceBill = deviceBill;
    }

    @JsonProperty("vInstallerId")
    public String getvInstallerId() {
        return vInstallerId;
    }

    public void setvInstallerId(String vInstallerId) {
        this.vInstallerId = vInstallerId;
    }

    @JsonProperty("vInvestigatorId")
    public String getvInvestigatorId() {
        return vInvestigatorId;
    }

    public void setvInvestigatorId(String vInvestigatorId) {
        this.vInvestigatorId = vInvestigatorId;
    }

    @JsonProperty("vContactsName")
    public String getvContactsName() {
        return vContactsName;
    }

    public void setvContactsName(String vContactsName) {
        this.vContactsName = vContactsName;
    }

    @JsonProperty("vInvestigatorName")
    public String getvInvestigatorName() {
        return vInvestigatorName;
    }

    public void setvInvestigatorName(String vInvestigatorName) {
        this.vInvestigatorName = vInvestigatorName;
    }

    @JsonProperty("vInstallerName")
    public String getvInstallerName() {
        return vInstallerName;
    }

    public void setvInstallerName(String vInstallerName) {
        this.vInstallerName = vInstallerName;
    }

    @JsonProperty("vInvestigatorPhone")
    public String getvInvestigatorPhone() {
        return vInvestigatorPhone;
    }

    public void setvInvestigatorPhone(String vInvestigatorPhone) {
        this.vInvestigatorPhone = vInvestigatorPhone;
    }

    @JsonProperty("vInstallerPhone")
    public String getvInstallerPhone() {
        return vInstallerPhone;
    }

    public void setvInstallerPhone(String vInstallerPhone) {
        this.vInstallerPhone = vInstallerPhone;
    }

    @JsonProperty("elevatorList")
    public List<TblElevator> getElevatorList() {
        return elevatorList;
    }

    public void setElevatorList(List<TblElevator> elevatorList) {
        this.elevatorList = elevatorList;
    }

    @JsonProperty("vStatusName")
    public String getvStatusName() {
        return vStatusName;
    }

    public void setvStatusName(String vStatusName) {
        this.vStatusName = vStatusName;
    }

}
