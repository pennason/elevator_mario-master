package com.shmashine.api.module.elevator;

/**
 * 对外电梯列表接口返回Pojo
 */

public class ElevatorThirdPartyList {

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
     * 应急处理人
     */
    private String emergencyPersonName;
    /**
     * 应急服务电话
     */
    private String emergencyPersonTel;
    /**
     * 电梯使用类型
     */
    private Integer elevatorUseType;
    private String elevatorUseTypeName;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer delFlag;

    /**
     * 夜间守护模式 开启状态
     */
    private Integer nightWatchStatus;

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
