package com.shmashine.socket.elevator.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 电梯表(TblElevator)实体类
 *
 * @author makejava
 * @since 2020-06-14 15:17:38
 */
@SuppressWarnings("checkstyle:MethodCount")
public class TblElevator implements Serializable {
    private static final long serialVersionUID = -25949284524752002L;
    /**
     * 电梯唯一ID
     */
    private String vElevatorId;
    /**
     * 电梯编号
     */
    private String vElevatorCode;
    /**
     * 省ID
     */
    private Integer iProvinceId;
    /**
     * 市ID
     */
    private Integer iCityId;
    /**
     * 区ID
     */
    private Integer iAreaId;
    /**
     * 电梯地址
     */
    private String vAddress;
    /**
     * 电梯类型
     */
    private Integer iElevatorType;
    /**
     * 电梯使用类型
     */
    private Integer iElevatorUseType;
    /**
     * 电梯安装地类型
     */
    private Integer iElevatorPlaceType;
    /**
     * 电梯品牌ID
     */
    private String vElevatorBrandId;
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
    /**
     * 物业公司ID
     */
    private String vPropertyCompanyId;
    /**
     * 政府部门id
     */
    private String iGovernmentId;
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
    /**
     * 小区ID
     */
    private String vVillageId;
    /**
     * 设备安装状态 0未安装 1安装
     */
    private Integer iInstallStatus;
    /**
     * 设备安装时间
     */
    private Date dtInstallTime;
    /**
     * 电梯在线状态，0 离线，1在线（有一个设备在线就认为电梯在线）
     */
    private Integer iOnLine;
    /**
     * 电梯服务模式 0 正常运行，1 停止服务，2 检修模式
     */
    private Integer iModeStatus;
    /**
     * 故障状态 0 未故障，1 故障
     */
    private Integer iFaultStatus;
    /**
     * 最高楼层
     */
    private Integer iMaxFloor;
    /**
     * 最低楼层
     */
    private Integer iMinFloor;
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
     * 电梯统一的注册编号
     */
    private String vEquipmentCode;
    /**
     * 登记机关
     */
    private String vRegistrationAuthority;
    /**
     * 登记机构
     */
    private String vRegistrationMechanism;
    /**
     * 登记证编号
     */
    private String vRegistrationCertificateNo;
    /**
     * 创建时间
     */
    private Date dtCreateTime;
    /**
     * 修改时间
     */
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
     * i_filter_flag 是否拦截确认 0：不拦截-直接生成故障，1：拦截-人工确认故障
     */
    private Integer iFilterFlag;

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
    public String getIMaintainPersonTel() {
        return vMaintainPersonTel;
    }

    public void setIMaintainPersonTel(String vMaintainPersonTel) {
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
    public String getIEmergencyPersonTel() {
        return vEmergencyPersonTel;
    }

    public void setIEmergencyPersonTel(String iEmergencyPersonTel) {
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

    @JsonProperty("iFilterFlag")
    public Integer getIFilterFlag() {
        return iFilterFlag;
    }

    public void setIFilterFlag(Integer iFilterFlag) {
        this.iFilterFlag = iFilterFlag;
    }

}