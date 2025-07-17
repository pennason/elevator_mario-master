package com.shmashine.api.module.elevator;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 对外电梯详情接口返回Pojo
 */
public class ElevatorThirdPartyDetail {

    /**
     * 电梯唯一ID
     */
    private String elevatorId;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 省ID
     */
    private Integer provinceId;
    private String provinceIdName;
    /**
     * 市ID
     */
    private Integer cityId;
    private String cityIdName;
    /**
     * 区ID
     */
    private Integer areaId;
    private String areaIdName;
    /**
     * 电梯地址
     */
    private String address;
    /**
     * 电梯类型
     */
    private Integer elevatorType;
    private String elevatorTypeName;
    /**
     * 电梯使用类型
     */
    private Integer elevatorUseType;
    private String elevatorUseTypeName;
    /**
     * 电梯安装地类型
     */
    private Integer elevatorPlaceType;
    /**
     * 电梯品牌ID
     */
    private String elevatorBrandId;
    private String elevatorBrandIdName;
    /**
     * 维保间隔（天）
     */
    private Integer maintainDays;
    /**
     * 上次维保日期
     */
    private Object lastMaintainDate;
    /**
     * 下次维保日期
     */
    private Object nextMaintainDate;
    /**
     * 上次年检时间
     */
    private Object lastInspectDate;
    /**
     * 下次年检时间
     */
    private Object nextInspectDate;
    /**
     * 维保公司ID
     */
    private String maintainCompanyId;
    private String maintainCompanyIdName;
    /**
     * 物业公司ID
     */
    private String propertyCompanyId;
    private String propertyCompanyIdName;
    /**
     * 政府部门id
     */
    private String governmentId;
    private String governmentIdName;
    /**
     * 维保人姓名
     */
    private String maintainPersonName;
    /**
     * 维保人手机号
     */
    private String maintainPersonTel;
    /**
     * 应急处理人
     */
    private String emergencyPersonName;
    /**
     * 应急服务电话
     */
    private String emergencyPersonTel;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 电梯位置码
     */
    private String sglnCode;
    /**
     * 单位内编号
     */
    private String unitCode;
    /**
     * 电梯统一的注册编号
     */
    private String equipmentCode;
    /**
     * 登记机关
     */
    private String registrationAuthority;
    /**
     * 登记机构
     */
    private String registrationMechanism;
    /**
     * 登记证编号
     */
    private String registrationCertificateNo;
    /**
     * 设备安装时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date installTime;
    /**
     * 累计运行天数
     */
    private String cumulativeOperationDays;
    /**
     * 运行次数
     */
    private Long biRunCount;
    /**
     * 开关门次数
     */
    private Long biDoorCount;
    /**
     * 钢丝绳折弯次数
     */
    private Long biBendCount;
    /**
     * 平层触发次数
     */
    private Long biLevelTriggerCount;
    /**
     * 累计运行距离（米）
     */
    private Long biRunDistanceCount;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date createTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date modifyTime;
    /**
     * 创建记录用户
     */
    private String createUserId;
    /**
     * 修改记录用户
     */
    private String modifyUserId;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer delFlag;

    /**
     * 夜间守护模式 开启状态
     */
    private Integer nightWatchStatus;

    public String getCumulativeOperationDays() {
        return cumulativeOperationDays;
    }

    public void setCumulativeOperationDays(String cumulativeOperationDays) {
        this.cumulativeOperationDays = cumulativeOperationDays;
    }

    public Date getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    public Long getBiRunCount() {
        return biRunCount;
    }

    public void setBiRunCount(Long biRunCount) {
        this.biRunCount = biRunCount;
    }

    public Long getBiDoorCount() {
        return biDoorCount;
    }

    public void setBiDoorCount(Long biDoorCount) {
        this.biDoorCount = biDoorCount;
    }

    public Long getBiBendCount() {
        return biBendCount;
    }

    public void setBiBendCount(Long biBendCount) {
        this.biBendCount = biBendCount;
    }

    public Long getBiLevelTriggerCount() {
        return biLevelTriggerCount;
    }

    public void setBiLevelTriggerCount(Long biLevelTriggerCount) {
        this.biLevelTriggerCount = biLevelTriggerCount;
    }

    public Long getBiRunDistanceCount() {
        return biRunDistanceCount;
    }

    public void setBiRunDistanceCount(Long biRunDistanceCount) {
        this.biRunDistanceCount = biRunDistanceCount;
    }

    public String getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(String elevatorId) {
        this.elevatorId = elevatorId;
    }

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceIdName() {
        return provinceIdName;
    }

    public void setProvinceIdName(String provinceIdName) {
        this.provinceIdName = provinceIdName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityIdName() {
        return cityIdName;
    }

    public void setCityIdName(String cityIdName) {
        this.cityIdName = cityIdName;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaIdName() {
        return areaIdName;
    }

    public void setAreaIdName(String areaIdName) {
        this.areaIdName = areaIdName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getElevatorType() {
        return elevatorType;
    }

    public void setElevatorType(Integer elevatorType) {
        this.elevatorType = elevatorType;
    }

    public String getElevatorTypeName() {
        return elevatorTypeName;
    }

    public void setElevatorTypeName(String elevatorTypeName) {
        this.elevatorTypeName = elevatorTypeName;
    }

    public Integer getElevatorUseType() {
        return elevatorUseType;
    }

    public void setElevatorUseType(Integer elevatorUseType) {
        this.elevatorUseType = elevatorUseType;
    }

    public String getElevatorUseTypeName() {
        return elevatorUseTypeName;
    }

    public void setElevatorUseTypeName(String elevatorUseTypeName) {
        this.elevatorUseTypeName = elevatorUseTypeName;
    }

    public Integer getElevatorPlaceType() {
        return elevatorPlaceType;
    }

    public void setElevatorPlaceType(Integer elevatorPlaceType) {
        this.elevatorPlaceType = elevatorPlaceType;
    }

    public String getElevatorBrandId() {
        return elevatorBrandId;
    }

    public void setElevatorBrandId(String elevatorBrandId) {
        this.elevatorBrandId = elevatorBrandId;
    }

    public String getElevatorBrandIdName() {
        return elevatorBrandIdName;
    }

    public void setElevatorBrandIdName(String elevatorBrandIdName) {
        this.elevatorBrandIdName = elevatorBrandIdName;
    }

    public Integer getMaintainDays() {
        return maintainDays;
    }

    public void setMaintainDays(Integer maintainDays) {
        this.maintainDays = maintainDays;
    }

    public Object getLastMaintainDate() {
        return lastMaintainDate;
    }

    public void setLastMaintainDate(Object lastMaintainDate) {
        this.lastMaintainDate = lastMaintainDate;
    }

    public Object getNextMaintainDate() {
        return nextMaintainDate;
    }

    public void setNextMaintainDate(Object nextMaintainDate) {
        this.nextMaintainDate = nextMaintainDate;
    }

    public Object getLastInspectDate() {
        return lastInspectDate;
    }

    public void setLastInspectDate(Object lastInspectDate) {
        this.lastInspectDate = lastInspectDate;
    }

    public Object getNextInspectDate() {
        return nextInspectDate;
    }

    public void setNextInspectDate(Object nextInspectDate) {
        this.nextInspectDate = nextInspectDate;
    }

    public String getMaintainCompanyId() {
        return maintainCompanyId;
    }

    public void setMaintainCompanyId(String maintainCompanyId) {
        this.maintainCompanyId = maintainCompanyId;
    }

    public String getMaintainCompanyIdName() {
        return maintainCompanyIdName;
    }

    public void setMaintainCompanyIdName(String maintainCompanyIdName) {
        this.maintainCompanyIdName = maintainCompanyIdName;
    }

    public String getPropertyCompanyId() {
        return propertyCompanyId;
    }

    public void setPropertyCompanyId(String propertyCompanyId) {
        this.propertyCompanyId = propertyCompanyId;
    }

    public String getPropertyCompanyIdName() {
        return propertyCompanyIdName;
    }

    public void setPropertyCompanyIdName(String propertyCompanyIdName) {
        this.propertyCompanyIdName = propertyCompanyIdName;
    }

    public String getGovernmentId() {
        return governmentId;
    }

    public void setGovernmentId(String governmentId) {
        this.governmentId = governmentId;
    }

    public String getGovernmentIdName() {
        return governmentIdName;
    }

    public void setGovernmentIdName(String governmentIdName) {
        this.governmentIdName = governmentIdName;
    }

    public String getMaintainPersonName() {
        return maintainPersonName;
    }

    public void setMaintainPersonName(String maintainPersonName) {
        this.maintainPersonName = maintainPersonName;
    }

    public String getMaintainPersonTel() {
        return maintainPersonTel;
    }

    public void setMaintainPersonTel(String maintainPersonTel) {
        this.maintainPersonTel = maintainPersonTel;
    }

    public String getEmergencyPersonName() {
        return emergencyPersonName;
    }

    public void setEmergencyPersonName(String emergencyPersonName) {
        this.emergencyPersonName = emergencyPersonName;
    }

    public String getEmergencyPersonTel() {
        return emergencyPersonTel;
    }

    public void setEmergencyPersonTel(String emergencyPersonTel) {
        this.emergencyPersonTel = emergencyPersonTel;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSglnCode() {
        return sglnCode;
    }

    public void setSglnCode(String sglnCode) {
        this.sglnCode = sglnCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getRegistrationAuthority() {
        return registrationAuthority;
    }

    public void setRegistrationAuthority(String registrationAuthority) {
        this.registrationAuthority = registrationAuthority;
    }

    public String getRegistrationMechanism() {
        return registrationMechanism;
    }

    public void setRegistrationMechanism(String registrationMechanism) {
        this.registrationMechanism = registrationMechanism;
    }

    public String getRegistrationCertificateNo() {
        return registrationCertificateNo;
    }

    public void setRegistrationCertificateNo(String registrationCertificateNo) {
        this.registrationCertificateNo = registrationCertificateNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getNightWatchStatus() {
        return nightWatchStatus;
    }

    public void setNightWatchStatus(Integer nightWatchStatus) {
        this.nightWatchStatus = nightWatchStatus;
    }
}
