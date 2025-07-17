package com.shmashine.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 电梯表(TblElevator)实体类
 *
 * @author makejava
 * @since 2020-09-21 14:32:21
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblElevator implements Serializable {
    // CHECKSTYLE:OFF
    @Serial
    private static final long serialVersionUID = -28465663804212357L;


    @JsonProperty("vElevatorName")
    private String vElevatorName;

    /**
     * 电梯唯一ID
     */
    @JsonProperty("vElevatorId")
    private String vElevatorId;
    /**
     * 电梯编号
     */
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]{2,11}$", message = "电梯code为：字母开头，3-12字节，字母数字下划线横杠")
    @JsonProperty("vElevatorCode")
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
    @JsonProperty("vEmergencyPersonTel")
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
     * 小区名
     */
    private String vVillageName;

    /**
     * 楼宇id
     */
    private String vBuildingId;
    /**
     * 楼宇类型： 0:未设置，1 住宅 2 办公楼宇 3 商场超市 4 宾馆饭店 5 交通场所 6 医院 7 学校  8 文体娱场所 9 其他
     */
    private Integer buildingType;
    /**
     * 设备安装状态 0未安装 1安装
     */
    private Integer iInstallStatus;
    /**
     * 设备安装时间
     */
    private Object dtInstallTime;
    /**
     * 电梯在线状态，0 离线，1在线（有一个设备在线就认为电梯在线）
     */
    private Integer iOnLine;
    /**
     * 电梯服务模式 0 正常运行，1 检修模式，2 停止服务
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
     * 出厂编号
     * 新装电梯
     * （尚未取得
     * 电梯注册
     * 码）必填
     */
    private String vLeaveFactoryNumber;
    /**
     * 制造单位统一社会信
     * 用代码 新装电梯
     * （尚未取得
     * 电梯注册
     * 码）必填
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
     * 设备标识：1：前装设备；2：单盒设备；null：后装设备
     */
    private String deviceMark;

    /**
     * 门类型：0单门，1贯通门
     */
    private Integer doorType;

    /**
     * 工程管理状态
     */
    private Integer iPmStatus;
    /**
     * 状态名
     */
    private String vPmStatusName;

    /**
     * 设备类型
     */
    @JsonProperty("eType")
    private String eType;

    /**
     * 夜间守护模式
     */
    private Integer nightWatchStatus;

    /**
     * 夜间守护开始时间
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Time nightWatchStartTime;
    /**
     * 夜间守护结束时间
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
     * 重庆-经度
     */
    private String lon;
    /**
     * 重庆-纬度
     */
    private String lat;
    /**
     * 重庆-电梯出厂编号
     */
    private String factoryNumber;
    /**
     * 重庆-电梯注册代码
     */
    private String registrationCode;
    /**
     * 重庆-96333电梯识别码 数据库字段 code_96333
     */
    private String code96333;
    /**
     * 重庆-电梯品种
     */
    private String category;
    /**
     * 重庆-电梯型号
     */
    private String equipmentNumber;
    /**
     * 重庆-使用单位名称
     */
    private String useUnitName;
    /**
     * 重庆-地址
     */
    private String addr;
    /**
     * 重庆-电梯内部编号
     */
    private String insideNumber;
    /**
     * 重庆-电梯适用场所
     */
    private String suitablePlace;
    /**
     * 重庆-电梯生产商、进口商
     */
    private String manufacturer;
    /**
     * 重庆-电梯出厂日期
     */
    private Object productionDate;
    /**
     * 重庆-电梯改造单位
     */
    private String reformingUnit;
    /**
     * 重庆-电梯改造日期
     */
    private Object reformingDate;

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
     * 重庆-楼层数
     */
    private Integer floorNumber;
    /**
     * 重庆-停站数
     */
    private Integer stopsNumber;
    /**
     * 重庆-额定速度
     */
    private BigDecimal speed;
    /**
     * 重庆-额定载重量
     */
    private BigDecimal loadCapacity;

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
    /**
     * 重庆-紧急联系人电话
     */
    private String emergencyPhone;
    /**
     * 重庆-单位内部编号
     */
    private String innerCode;
    /**
     * 重庆-安全管理人员
     */
    private String safetyManager;
    /**
     * 重庆-安全人员联系电话
     */
    private String safetyManagerPhone;
    /**
     * 重庆-维保责任人姓名
     */
    private String supervisorName;
    /**
     * 重庆-维保责任人电话
     */
    private String supervisorPhone;
    /**
     * 重庆-维护保养单位名称
     */
    private String maintenanceUnit;
    /**
     * 重庆-是否参与按需维保
     */
    private Integer isMaintenaince;
    /**
     * 重庆-使用单位人员电话
     */
    private String usePhone;
    /**
     * 重庆-应急救援电话（维保单位）
     */
    private String maintenancePhone;
    /**
     * 重庆-维保周期
     */
    private String maintenanceCycle;
    /**
     * 重庆-下次检验/检测日期
     */
    private Object nextInspection;

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

    @JsonProperty("vVillageName")
    public String getVVillageName() {
        return vVillageName;
    }

    public void setVVillageName(String vVillageName) {
        this.vVillageName = vVillageName;
    }

    @JsonProperty("vBuildingId")
    public String getVBuildingId() {
        return vBuildingId;
    }

    public void setVBuildingId(String vBuildingId) {
        this.vBuildingId = vBuildingId;
    }

    @JsonProperty("iInstallStatus")
    public Integer getIInstallStatus() {
        return iInstallStatus;
    }

    public void setIInstallStatus(Integer iInstallStatus) {
        this.iInstallStatus = iInstallStatus;
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

    public Integer getiPmStatus() {
        return iPmStatus;
    }

    public void setiPmStatus(Integer iPmStatus) {
        this.iPmStatus = iPmStatus;
    }


    public String getvPmStatusName() {
        if (iPmStatus == null) {
            return "待现勘";
        }
        return switch (iPmStatus) {
            case 2 -> "待配货";
            case 3 -> "待安装";
            case 4 -> "待调测";
            case 5 -> "待验收";
            case 6 -> "运行中";
            case 7 -> "托管";
            case 11 -> "现勘中";
            case 12 -> "配货中";
            case 13 -> "安装中";
            case 14 -> "调测中";
            case 15 -> "验收中";
            default -> "待现勘";
        };
    }
    // CHECKSTYLE:ON
}