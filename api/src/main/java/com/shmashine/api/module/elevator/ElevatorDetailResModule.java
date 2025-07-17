package com.shmashine.api.module.elevator;


import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @PackgeName: com.shmashine.api.module.elevator
 * @ClassName: ElevatorDetailResModule
 * @Date: 2020/7/1012:23
 * @Author: LiuLiFu
 * @Description:
 */

@Data
public class ElevatorDetailResModule implements Serializable {

//    private static final long serialVersionUID = 501520397444589399L;
    /**
     * 电梯唯一ID
     */
    private String vElevatorId;
    /**
     * 电梯编号
     */
    private String vElevatorCode;
    /**
     * 电梯名称
     */
    private String vElevatorName;
    /**
     * 省ID
     */
    private Integer iProvinceId;
    private String iProvinceIdName;
    /**
     * 市ID
     */
    private Integer iCityId;
    private String iCityIdName;
    /**
     * 区ID
     */
    private Integer iAreaId;
    private String iAreaIdName;
    /**
     * 电梯地址
     */
    private String vAddress;
    /**
     * 电梯类型
     */
    private Integer iElevatorType;
    private String iElevatorTypeName;
    /**
     * 电梯使用类型
     */
    private Integer iElevatorUseType;
    private String iElevatorUseTypeName;
    /**
     * 电梯安装地类型
     */
    private Integer iElevatorPlaceType;
    /**
     * 电梯品牌ID
     */
    private String vElevatorBrandId;
    private String vElevatorBrandIdName;
    /**
     * 维保间隔（天）
     */
    private Integer iMaintainDays;
    /**
     * 上次维保日期
     */
    private Object dLastMaintainDate;
    /**
     * 下次维保日期
     */
    private Object dNextMaintainDate;
    /**
     * 上次年检时间
     */
    private Object dLastInspectDate;
    /**
     * 下次年检时间
     */
    private Object dNextInspectDate;
    /**
     * 维保公司ID
     */
    private String vMaintainCompanyId;
    private String vMaintainCompanyIdName;
    /**
     * 物业公司ID
     */
    private String vPropertyCompanyId;
    private String vPropertyCompanyIdName;
    /**
     * 政府部门id
     */
    private String iGovernmentId;
    private String iGovernmentIdName;
    /**
     * 维保人姓名
     */
    private String vMaintainPersonName;
    /**
     * 维保人手机号
     */
    private String vMaintainPersonTel;
    /**
     * 应急处理人
     */
    private String vEmergencyPersonName;
    /**
     * 应急服务电话
     */
    private String vEmergencyPersonTel;
    /**
     * http推送平台code
     */
    private String vHttpPtCodes;
    /**
     * 经度
     */
    private String vLongitude;
    /**
     * 纬度
     */
    private String vLatitude;
    /**
     * 使用地图类型 1 GPS，2 百度地图，3 高德地图
     */
    private Integer iCoordinateType;
    /**
     * 项目ID
     */
    private String vProjectId;
    private String vProjectIdName;
    /**
     * 小区ID
     */
    private String vVillageId;
    private String vVillageIdName;
    /**
     * 楼宇id
     */
    private String vBuildingId;
    /**
     * 楼宇类型 0:未设置，1 住宅 2 办公楼宇 3 商场超市 4 宾馆饭店 5 交通场所 6 医院 7 学校  8 文体娱场所 9 其他
     */
    private Integer buildingType;
    /**
     * 轿门类型
     */
    private Integer doorType;
    /**
     * 设备安装状态 0未安装 1安装
     */
    private Integer iInstallStatus;
    private String iInstallStatusName;
    /**
     * 设备安装时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtInstallTime;
    /**
     * 电梯在线状态，0 离线，1在线（有一个设备在线就认为电梯在线）
     */
    private Integer iOnLine;
    private String iOnLineName;
    /**
     * 电梯服务模式 0 正常运行，1 检修模式，2 停止服务
     */
    private Integer iModeStatus;
    private String iModeStatusName;
    /**
     * 故障状态 0 未故障，1 故障
     */
    private Integer iFaultStatus;
    private String iFaultStatusName;
    /**
     * 最高楼层
     */
    private Integer iMaxFloor;
    /**
     * 最低楼层
     */
    private Integer iMinFloor;
    /**
     * 层数
     */
    private Integer floorCount;
    /**
     * 站数
     */
    private Integer stationCount;
    /**
     * 基站（默认停的楼层）
     */
    private String baseStation;
    /**
     * 楼层详细信息
     */
    private String vFloorDetail;
    /**
     * 楼层设置状态
     */
    private String vFloorSettingStatus;
    /**
     * 额定限速
     */
    private Double dcSpeed;
    /**
     * 电梯上行额定速度（m/s）
     */
    private Double nominalSpeedUp;
    /**
     * 电梯下行额定速度（m/s）
     */
    private Double nominalSpeedDown;
    /**
     * 额定载重量
     */
    private String nominalLoadCapacity;
    /**
     * 运行时长（小时）
     */
    private Long biRunTime;
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
     * 往返次数
     */
    private Long backAndForthCount;
    /**
     * 曳引轮直径Dtmm
     */
    private Integer iDt;
    /**
     * 导向轮直径Dp1mm
     */
    private Integer iDt1;
    /**
     * 导向轮直径Dp2mm
     */
    private Integer iDt2;
    /**
     * 导向轮数量
     */
    private Integer iNps;
    /**
     * 反向弯折导向轮数量
     */
    private Integer iNpr;
    /**
     * 调整系数
     */
    private Double dcNdaj;
    /**
     * 角度
     */
    private Integer iAngle;
    /**
     * 1γ,2β
     */
    private Integer iAngleType;
    /**
     * 电梯位置码
     */
    private String vSglnCode;
    /**
     * 单位内编号
     */
    private String vUnitCode;
    /**
     * 出厂编号 新装电梯 （尚未取得 电梯注册 码）必填
     */
    private String vLeaveFactoryNumber;
    /**
     * 制造单位统一社会信 用代码 新装电梯 （尚未取得 电梯注册 码）必填
     */
    private String vManufacturerCode;
    /**
     * 电梯统一的注册编号
     */
    private String vEquipmentCode;
    /**
     * 特种设备代码
     */
    private String equCode;
    /**
     * 电梯识别码
     */
    private String identificationNumber;
    /**
     * 登记机关
     */
    private String vRegistrationAuthority;
    /**
     * 登记机构
     */
    private String vRegistrationMechanism;
    /**
     * 故障平台
     */
    private Integer platformType;
    /**
     * 登记证编号
     */
    private String vRegistrationCertificateNo;
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
    /**
     * 是否拦截确认 0：不拦截-直接生成故障，1：拦截-人工确认故障， 2：拦截-延迟1分钟自动确认故障
     */
    private Integer filterFlag;
    /**
     * 工程管理状态
     */
    private Integer pmStatus;

    /**
     * 夜间守护模式 开启状态 1：开启
     */
    private Integer nightWatchStatus;
    /**
     * 夜间守护模式  开始时间
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Time nightWatchStartTime;
    /**
     * 夜间守护模式  开始时间
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Time nightWatchEndTime;

    /**
     * 物业安全管理员
     */
    private String propertySafeMan;
    /**
     * 物业安全管理员电话
     */
    private String propertySafePhone;
    /**
     * 维保第一责任人
     */
    private String maintainPrincipalPerson;
    /**
     * 维保第一责任人电话
     */
    private String maintainPrincipalPhone;
    /**
     * 维保第二责任人
     */
    private String maintainSubordinatePerson;
    /**
     * 维保第二责任人电话
     */
    private String maintainSubordinatePhone;
    /**
     * 维保设备保险信息（如果有）
     */
    private String maintainEquInsuranceInfo;
    /**
     * 维保应急救援电话
     */
    private String maintainEmergencyPhone;

    /**
     * 高德地图-经度
     */
    private String gcj02Longitude;

    /**
     * 高德地图-纬度
     */
    private String gcj02Latitude;
    /**
     * 电梯门， 如：6厅门7层6站，那么此字段应为6厅门，传参：6
     */
    private String liftGate;
    /**
     * 电梯显示楼层
     */
    private String showFloor;
    /**
     * 曳引比
     */
    private String traction;
    /**
     * 是否购买电梯保险, 0:否，1：是
     */
    private Integer hasInsurance;
    /**
     * 有无机房, 0:无，1：有
     */
    private Integer haveHouse;
    /**
     * 年检检验日期 yyyy-MM-dd
     */
    private Object examineDate;
    /**
     * 电梯安装日期 yyyy-MM-dd
     */
    private Object installationDate;
    /**
     * 电梯型号
     */
    private String modelName;
    /**
     * 年检检验单位
     */
    private String examineEntities;
    /**
     * 按需维保开始日期 yyyy-MM-dd
     */
    private Object maintenanceParticipateInDate;
    /**
     * 使用单位人电话
     */
    private String useUnitPhone;
    /**
     * 下次检验/检测日期 yyyy-mm-dd
     */
    private Object nextInspectionDate;
    /**
     * 检验/检测单位
     */
    private String inspectionUnit;
    /**
     * 保险状态
     */
    private String insuranceStatus;
    /**
     * 保险险种
     */
    private String insuranceType;
    /**
     * 承保单位
     */
    private String contractor;
    /**
     * 承保有效期开始日期 yyyy-mm-dd
     */
    private Object contractorStart;
    /**
     * 承保有效期到期日期 yyyy-mm-dd
     */
    private Object contractorEnd;
    /**
     * 电梯控制方式
     */
    private String controlMode;
    /**
     * 扶梯 提升高度
     */
    private String normalRiseHeight;
    /**
     * 扶梯/人行道倾斜角
     */
    private String normalAngle;
    /**
     * 扶梯/人行道名义宽度
     */
    private String normalWidth;
    /**
     * 扶梯/人行道使用区段长度
     */
    private String normalLength;


    @JsonProperty("iProvinceIdName")
    public String getIProvinceIdName() {
        return iProvinceIdName;
    }

    public void setIProvinceIdName(String iProvinceIdName) {
        this.iProvinceIdName = iProvinceIdName;
    }

    @JsonProperty("iCityIdName")
    public String getICityIdName() {
        return iCityIdName;
    }

    public void setICityIdName(String iCityIdName) {
        this.iCityIdName = iCityIdName;
    }

    @JsonProperty("iAreaIdName")
    public String getIAreaIdName() {
        return iAreaIdName;
    }

    public void setIAreaIdName(String iAreaIdName) {
        this.iAreaIdName = iAreaIdName;
    }

    @JsonProperty("iElevatorTypeName")
    public String getIElevatorTypeName() {
        return iElevatorTypeName;
    }

    public void setIElevatorTypeName(String iElevatorTypeName) {
        this.iElevatorTypeName = iElevatorTypeName;
    }

    @JsonProperty("iElevatorUseTypeName")
    public String getIElevatorUseTypeName() {
        return iElevatorUseTypeName;
    }

    public void setIElevatorUseTypeName(String iElevatorUseTypeName) {
        this.iElevatorUseTypeName = iElevatorUseTypeName;
    }

    @JsonProperty("vElevatorBrandIdName")
    public String getVElevatorBrandIdName() {
        return vElevatorBrandIdName;
    }

    public void setVElevatorBrandIdName(String vElevatorBrandIdName) {
        this.vElevatorBrandIdName = vElevatorBrandIdName;
    }

    @JsonProperty("vMaintainCompanyIdName")
    public String getVMaintainCompanyIdName() {
        return vMaintainCompanyIdName;
    }

    public void setVMaintainCompanyIdName(String vMaintainCompanyIdName) {
        this.vMaintainCompanyIdName = vMaintainCompanyIdName;
    }

    @JsonProperty("vLeaveFactoryNumber")
    public String getVLeaveFactoryNumber() {
        return vLeaveFactoryNumber;
    }

    public void setVLeaveFactoryNumber(String vLeaveFactoryNumber) {
        this.vLeaveFactoryNumber = vLeaveFactoryNumber;
    }

    @JsonProperty("vManufacturerCode")
    public String getVManufacturerCode() {
        return vManufacturerCode;
    }

    public void setVManufacturerCode(String vManufacturerCode) {
        this.vManufacturerCode = vManufacturerCode;
    }

    @JsonProperty("vPropertyCompanyIdName")
    public String getVPropertyCompanyIdName() {
        return vPropertyCompanyIdName;
    }

    public void setVPropertyCompanyIdName(String vPropertyCompanyIdName) {
        this.vPropertyCompanyIdName = vPropertyCompanyIdName;
    }

    @JsonProperty("iGovernmentIdName")
    public String getIGovernmentIdName() {
        return iGovernmentIdName;
    }

    public void setIGovernmentIdName(String iGovernmentIdName) {
        this.iGovernmentIdName = iGovernmentIdName;
    }

    @JsonProperty("vProjectIdName")
    public String getVProjectIdName() {
        return vProjectIdName;
    }

    public void setVProjectIdName(String vProjectIdName) {
        this.vProjectIdName = vProjectIdName;
    }

    @JsonProperty("vVillageIdName")
    public String getVVillageIdName() {
        return vVillageIdName;
    }

    public void setVVillageIdName(String vVillageIdName) {
        this.vVillageIdName = vVillageIdName;
    }

    @JsonProperty("iInstallStatusName")
    public String getIInstallStatusName() {
        return iInstallStatusName;
    }

    public void setIInstallStatusName(String iInstallStatusName) {
        this.iInstallStatusName = iInstallStatusName;
    }

    @JsonProperty("iOnLineName")
    public String getIOnLineName() {
        return iOnLineName;
    }

    public void setIOnLineName(String iOnLineName) {
        this.iOnLineName = iOnLineName;
    }

    @JsonProperty("iModeStatusName")
    public String getIModeStatusName() {
        return iModeStatusName;
    }

    public void setIModeStatusName(String iModeStatusName) {
        this.iModeStatusName = iModeStatusName;
    }

    @JsonProperty("iFaultStatusName")
    public String getIFaultStatusName() {
        return iFaultStatusName;
    }

    public void setIFaultStatusName(String iFaultStatusName) {
        this.iFaultStatusName = iFaultStatusName;
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

    @JsonProperty("iProvinceId")
    public Integer getIProvinceId() {
        return iProvinceId;
    }

    public void setIProvinceId(Integer iProvinceId) {
        this.iProvinceId = iProvinceId;
    }

    @JsonProperty("iCityId")
    public Integer getICityId() {
        return iCityId;
    }

    public void setICityId(Integer iCityId) {
        this.iCityId = iCityId;
    }

    @JsonProperty("iAreaId")
    public Integer getIAreaId() {
        return iAreaId;
    }

    public void setIAreaId(Integer iAreaId) {
        this.iAreaId = iAreaId;
    }

    @JsonProperty("vAddress")
    public String getVAddress() {
        return vAddress;
    }

    public void setVAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    @JsonProperty("iElevatorType")
    public Integer getIElevatorType() {
        return iElevatorType;
    }

    public void setIElevatorType(Integer iElevatorType) {
        this.iElevatorType = iElevatorType;
    }

    @JsonProperty("iElevatorUseType")
    public Integer getIElevatorUseType() {
        return iElevatorUseType;
    }

    public void setIElevatorUseType(Integer iElevatorUseType) {
        this.iElevatorUseType = iElevatorUseType;
    }

    @JsonProperty("iElevatorPlaceType")
    public Integer getIElevatorPlaceType() {
        return iElevatorPlaceType;
    }

    public void setIElevatorPlaceType(Integer iElevatorPlaceType) {
        this.iElevatorPlaceType = iElevatorPlaceType;
    }

    @JsonProperty("vElevatorBrandId")
    public String getVElevatorBrandId() {
        return vElevatorBrandId;
    }

    public void setVElevatorBrandId(String vElevatorBrandId) {
        this.vElevatorBrandId = vElevatorBrandId;
    }

    @JsonProperty("iMaintainDays")
    public Integer getIMaintainDays() {
        return iMaintainDays;
    }

    public void setIMaintainDays(Integer iMaintainDays) {
        this.iMaintainDays = iMaintainDays;
    }

    @JsonProperty("dLastMaintainDate")
    public Object getDLastMaintainDate() {
        return dLastMaintainDate;
    }

    public void setDLastMaintainDate(Object dLastMaintainDate) {
        this.dLastMaintainDate = dLastMaintainDate;
    }

    @JsonProperty("dNextMaintainDate")
    public Object getDNextMaintainDate() {
        return dNextMaintainDate;
    }

    public void setDNextMaintainDate(Object dNextMaintainDate) {
        this.dNextMaintainDate = dNextMaintainDate;
    }

    @JsonProperty("dLastInspectDate")
    public Object getDLastInspectDate() {
        return dLastInspectDate;
    }

    public void setDLastInspectDate(Object dLastInspectDate) {
        this.dLastInspectDate = dLastInspectDate;
    }

    @JsonProperty("dNextInspectDate")
    public Object getDNextInspectDate() {
        return dNextInspectDate;
    }

    public void setDNextInspectDate(Object dNextInspectDate) {
        this.dNextInspectDate = dNextInspectDate;
    }

    @JsonProperty("vMaintainCompanyId")
    public String getVMaintainCompanyId() {
        return vMaintainCompanyId;
    }

    public void setVMaintainCompanyId(String vMaintainCompanyId) {
        this.vMaintainCompanyId = vMaintainCompanyId;
    }

    @JsonProperty("vPropertyCompanyId")
    public String getVPropertyCompanyId() {
        return vPropertyCompanyId;
    }

    public void setVPropertyCompanyId(String vPropertyCompanyId) {
        this.vPropertyCompanyId = vPropertyCompanyId;
    }

    @JsonProperty("iGovernmentId")
    public String getIGovernmentId() {
        return iGovernmentId;
    }

    public void setIGovernmentId(String iGovernmentId) {
        this.iGovernmentId = iGovernmentId;
    }

    @JsonProperty("vMaintainPersonName")
    public String getVMaintainPersonName() {
        return vMaintainPersonName;
    }

    public void setVMaintainPersonName(String vMaintainPersonName) {
        this.vMaintainPersonName = vMaintainPersonName;
    }

    @JsonProperty("vMaintainPersonTel")
    public String getVMaintainPersonTel() {
        return vMaintainPersonTel;
    }

    public void setVMaintainPersonTel(String vMaintainPersonTel) {
        this.vMaintainPersonTel = vMaintainPersonTel;
    }

    @JsonProperty("vEmergencyPersonName")
    public String getVEmergencyPersonName() {
        return vEmergencyPersonName;
    }

    public void setVEmergencyPersonName(String vEmergencyPersonName) {
        this.vEmergencyPersonName = vEmergencyPersonName;
    }

    @JsonProperty("vEmergencyPersonTel")
    public String getVEmergencyPersonTel() {
        return vEmergencyPersonTel;
    }

    public void setVEmergencyPersonTel(String vEmergencyPersonTel) {
        this.vEmergencyPersonTel = vEmergencyPersonTel;
    }

    @JsonProperty("vHttpPtCodes")
    public String getVHttpPtCodes() {
        return vHttpPtCodes;
    }

    public void setVHttpPtCodes(String vHttpPtCodes) {
        this.vHttpPtCodes = vHttpPtCodes;
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

    @JsonProperty("iCoordinateType")
    public Integer getICoordinateType() {
        return iCoordinateType;
    }

    public void setICoordinateType(Integer iCoordinateType) {
        this.iCoordinateType = iCoordinateType;
    }

    @JsonProperty("vProjectId")
    public String getVProjectId() {
        return vProjectId;
    }

    public void setVProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    @JsonProperty("vVillageId")
    public String getVVillageId() {
        return vVillageId;
    }

    public void setVVillageId(String vVillageId) {
        this.vVillageId = vVillageId;
    }

    @JsonProperty("iInstallStatus")
    public Integer getIInstallStatus() {
        return iInstallStatus;
    }

    public void setIInstallStatus(Integer iInstallStatus) {
        this.iInstallStatus = iInstallStatus;
    }

    @JsonProperty("dtInstallTime")
    public Date getDtInstallTime() {
        return dtInstallTime;
    }

    public void setDtInstallTime(Date dtInstallTime) {
        this.dtInstallTime = dtInstallTime;
    }

    @JsonProperty("iOnLine")
    public Integer getIOnLine() {
        return iOnLine;
    }

    public void setIOnLine(Integer iOnLine) {
        this.iOnLine = iOnLine;
    }

    @JsonProperty("iModeStatus")
    public Integer getIModeStatus() {
        return iModeStatus;
    }

    public void setIModeStatus(Integer iModeStatus) {
        this.iModeStatus = iModeStatus;
    }

    @JsonProperty("iFaultStatus")
    public Integer getIFaultStatus() {
        return iFaultStatus;
    }

    public void setIFaultStatus(Integer iFaultStatus) {
        this.iFaultStatus = iFaultStatus;
    }

    @JsonProperty("iMaxFloor")
    public Integer getIMaxFloor() {
        return iMaxFloor;
    }

    public void setIMaxFloor(Integer iMaxFloor) {
        this.iMaxFloor = iMaxFloor;
    }

    @JsonProperty("iMinFloor")
    public Integer getIMinFloor() {
        return iMinFloor;
    }

    public void setIMinFloor(Integer iMinFloor) {
        this.iMinFloor = iMinFloor;
    }

    @JsonProperty("vFloorDetail")
    public String getVFloorDetail() {
        return vFloorDetail;
    }

    public void setVFloorDetail(String vFloorDetail) {
        this.vFloorDetail = vFloorDetail;
    }

    @JsonProperty("vFloorSettingStatus")
    public String getVFloorSettingStatus() {
        return vFloorSettingStatus;
    }

    public void setVFloorSettingStatus(String vFloorSettingStatus) {
        this.vFloorSettingStatus = vFloorSettingStatus;
    }

    @JsonProperty("dcSpeed")
    public Double getDcSpeed() {
        return dcSpeed;
    }

    public void setDcSpeed(Double dcSpeed) {
        this.dcSpeed = dcSpeed;
    }

    @JsonProperty("biRunCount")
    public Long getBiRunCount() {
        return biRunCount;
    }

    public void setBiRunCount(Long biRunCount) {
        this.biRunCount = biRunCount;
    }

    @JsonProperty("biDoorCount")
    public Long getBiDoorCount() {
        return biDoorCount;
    }

    public void setBiDoorCount(Long biDoorCount) {
        this.biDoorCount = biDoorCount;
    }

    @JsonProperty("biBendCount")
    public Long getBiBendCount() {
        return biBendCount;
    }

    public void setBiBendCount(Long biBendCount) {
        this.biBendCount = biBendCount;
    }

    @JsonProperty("biLevelTriggerCount")
    public Long getBiLevelTriggerCount() {
        return biLevelTriggerCount;
    }

    public void setBiLevelTriggerCount(Long biLevelTriggerCount) {
        this.biLevelTriggerCount = biLevelTriggerCount;
    }

    @JsonProperty("biRunDistanceCount")
    public Long getBiRunDistanceCount() {
        return biRunDistanceCount;
    }

    public void setBiRunDistanceCount(Long biRunDistanceCount) {
        this.biRunDistanceCount = biRunDistanceCount;
    }

    @JsonProperty("iDt")
    public Integer getIDt() {
        return iDt;
    }

    public void setIDt(Integer iDt) {
        this.iDt = iDt;
    }

    @JsonProperty("iDt1")
    public Integer getIDt1() {
        return iDt1;
    }

    public void setIDt1(Integer iDt1) {
        this.iDt1 = iDt1;
    }

    @JsonProperty("iDt2")
    public Integer getIDt2() {
        return iDt2;
    }

    public void setIDt2(Integer iDt2) {
        this.iDt2 = iDt2;
    }

    @JsonProperty("iNps")
    public Integer getINps() {
        return iNps;
    }

    public void setINps(Integer iNps) {
        this.iNps = iNps;
    }

    @JsonProperty("iNpr")
    public Integer getINpr() {
        return iNpr;
    }

    public void setINpr(Integer iNpr) {
        this.iNpr = iNpr;
    }

    @JsonProperty("dcNdaj")
    public Double getDcNdaj() {
        return dcNdaj;
    }

    public void setDcNdaj(Double dcNdaj) {
        this.dcNdaj = dcNdaj;
    }

    @JsonProperty("iAngle")
    public Integer getIAngle() {
        return iAngle;
    }

    public void setIAngle(Integer iAngle) {
        this.iAngle = iAngle;
    }

    @JsonProperty("iAngleType")
    public Integer getIAngleType() {
        return iAngleType;
    }

    public void setIAngleType(Integer iAngleType) {
        this.iAngleType = iAngleType;
    }

    @JsonProperty("vSglnCode")
    public String getVSglnCode() {
        return vSglnCode;
    }

    public void setVSglnCode(String vSglnCode) {
        this.vSglnCode = vSglnCode;
    }

    @JsonProperty("vUnitCode")
    public String getVUnitCode() {
        return vUnitCode;
    }

    public void setVUnitCode(String vUnitCode) {
        this.vUnitCode = vUnitCode;
    }

    @JsonProperty("vEquipmentCode")
    public String getVEquipmentCode() {
        return vEquipmentCode;
    }

    public void setVEquipmentCode(String vEquipmentCode) {
        this.vEquipmentCode = vEquipmentCode;
    }

    @JsonProperty("vRegistrationAuthority")
    public String getVRegistrationAuthority() {
        return vRegistrationAuthority;
    }

    public void setVRegistrationAuthority(String vRegistrationAuthority) {
        this.vRegistrationAuthority = vRegistrationAuthority;
    }

    @JsonProperty("vRegistrationMechanism")
    public String getVRegistrationMechanism() {
        return vRegistrationMechanism;
    }

    public void setVRegistrationMechanism(String vRegistrationMechanism) {
        this.vRegistrationMechanism = vRegistrationMechanism;
    }

    @JsonProperty("vRegistrationCertificateNo")
    public String getVRegistrationCertificateNo() {
        return vRegistrationCertificateNo;
    }

    public void setVRegistrationCertificateNo(String vRegistrationCertificateNo) {
        this.vRegistrationCertificateNo = vRegistrationCertificateNo;
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

    public String getvElevatorName() {
        return vElevatorName;
    }

    public void setvElevatorName(String vElevatorName) {
        this.vElevatorName = vElevatorName;
    }

    public String getvBuildingId() {
        return vBuildingId;
    }

    public void setvBuildingId(String vBuildingId) {
        this.vBuildingId = vBuildingId;
    }

    public Integer getDoorType() {
        return doorType;
    }

    public void setDoorType(Integer doorType) {
        this.doorType = doorType;
    }

    public Integer getNightWatchStatus() {
        return nightWatchStatus;
    }

    public void setNightWatchStatus(Integer nightWatchStatus) {
        this.nightWatchStatus = nightWatchStatus;
    }

    public Time getNightWatchStartTime() {
        return nightWatchStartTime;
    }

    public void setNightWatchStartTime(Time nightWatchStartTime) {
        this.nightWatchStartTime = nightWatchStartTime;
    }

    public Time getNightWatchEndTime() {
        return nightWatchEndTime;
    }

    public void setNightWatchEndTime(Time nightWatchEndTime) {
        this.nightWatchEndTime = nightWatchEndTime;
    }
}