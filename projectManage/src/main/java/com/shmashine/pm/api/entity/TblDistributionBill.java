package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 配货单详情
 */
@SuppressWarnings("checkstyle:MethodCount")
public class TblDistributionBill implements Serializable {

    private static final long serialVersionUID = 6683505052091855123L;

    @NotNull(message = "id不能为空")
    private String vDistributionBillId;

    @NotNull(message = "配货任务不能为空")
    private String vDistributionTaskId;

    @NotNull(message = "项目不能为空")
    private String vProjectId;

    @NotNull(message = "小区不能为空")
    private String vVillageId;

    @NotNull(message = "电梯不能为空")
    private String vElevatorId;

    //    @NotNull(message = "电梯绿码不能为空")
    private String vElevatorEquipmentId;

    /**
     * 配货人名
     */
    private String vDistributerName;
    /**
     * 验证码
     */
    private String vVerifyCode;
    /**
     * 麦信号
     */
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_-]{2,11}$", message = "电梯code为：字母开头，3-12字节，字母数字下划线横杠")
    private String vElevatorCode;
    /**
     * 盒子数 1 单盒 2 双盒
     */
    private Integer iCubeCount;
    /**
     * 链接方式
     */
    private String vConnectAdapter;
    /**
     * 链接方式型号
     */
    private String vConnectMode;

    /**
     * plc线缆
     */
    private Integer iPlcCable;

    /**
     * plc线缆数量
     */
    private Integer iPlcCableCount;
    /**
     * 4g路由器型号
     */
    private String v4gRouterMode;
    /**
     * 4g路由器ip字段
     */
    private String v4gRouterIp;
    /**
     * 4g路由器数量
     */
    private Integer i4gRouterCount;
    /**
     * 4g路由器厂家
     */
    private String v4gRouterManufactor;
    /**
     * 路由器型号
     */
    private String vRouterMode;
    /**
     * 路由器ip字段
     */
    private String vRouterIp;
    /**
     * 路由器数量
     */
    private Integer iRouterCount;
    /**
     * 路由器厂家
     */
    private String vRouterManufactor;
    /**
     * 4g路由器天线规格
     */
    private String v4gRouterAirwireSpec;
    /**
     * 4g路由器天线厂家
     */
    private String v4gRouterAirwireManufactor;

    /**
     * 有无摄像头
     */
    private Integer iCamera;
    /**
     * 摄像头型号
     */
    private String vCameraMode;
    /**
     * 摄像头类型
     */
    private Integer cameraType;
    /**
     * 摄像头厂家
     */
    private String vCameraManufactor;
    /**
     * 4g模块型号
     */
    private String v4gModuleMode;
    /**
     * 4g模块厂家
     */
    private String v4gModuleManufactor;
    /**
     * 物联网卡号
     */
    private String vIotNetworkCard;
    /**
     * 物联网卡运营商
     */
    private String vIotNetworkCardOperator;
    /**
     * 磁铁数量
     */
    private Integer iMagnetSensor;
    /**
     * 门磁开关数量
     */
    private Integer iMagnetDoorCount;
    /**
     * 温度传感器数量
     */
    private Integer iTemperSensorCount;
    /**
     * 有无人体传感器
     */
    private Integer iBodySensor;
    /**
     * 人体传感器型号
     */
    private String vBodySensorMode;

    /**
     * 传感器配置子选项 1:烟杆 2:小平层 3:U型光电 4:门磁
     */
    private Integer floorSensorRemark;

    /**
     * 平层传感器
     */
    private Integer iFloorSensor;
    /**
     * 平层传感器工作模式
     */
    private String vFloorSensorWorkmode;
    /**
     * 平层传感器型号
     */
    private String vFloorSensorMode;
    /**
     * 短网线长度
     */
    private String vShortCableLength;
    /**
     * 短网线数量
     */
    private String vShortCableCount;

    /**
     * 长网线长度
     */
    private Integer iLongCableLength;
    /**
     * 长网线数量
     */
    private Integer iLongCableCount;
    /**
     * 电源电压
     */
    private String vPowerSupply;
    /**
     * 电源数量
     */
    private Integer iPowerSupplyCount;
    /**
     * 电源品牌
     */
    private String vPowerSupplyBrand;
    /**
     * 插线板孔数
     */
    private String vPatchBorad;
    /**
     * 插线板数量
     */
    private String iPatchBoradCount;
    /**
     * 插线板品牌
     */
    private String vPatchBoradBrand;
    /**
     * 插线板长度
     */
    private Integer iPatchBoradLength;
    /**
     * 天线防尘帽数量
     */
    private Integer iAirwireCoverCount;
    /**
     * 有无轿顶门机电机电流传感器
     */
    private Integer iCarroofElectricitySensor;
    /**
     * 轿顶门机电机电流传感器型号
     */
    private String vCarroofElectricitySensorMode;
    /**
     * 轿顶轿门锁止电流传感器
     */
    private Integer iCarroofLockSensor;
    /**
     * 轿顶轿门锁止电流传感器型号
     */
    private String vCarroofLockSensorMode;
    /**
     * 机房停止服务电流传感器
     */
    private Integer iRoomElectricitySensor;
    /**
     * 机房停止服务电流传感器型号
     */
    private String vRoomElectricitySensorMode;
    /**
     * 机房检修服务电流传感器
     */
    private Integer iRoomServiceSensor;
    /**
     * 机房检修服务电流传感器型号
     */
    private String vRoomServiceSensorMode;
    /**
     * 机房抱闸服务电流传感器
     */
    private Integer iRoomSwitchSensor;
    /**
     * 机房抱闸服务电流传感器型号
     */
    private String vRoomSwitchSensorMode;
    /**
     * 机房紧急呼叫电流传感器
     */
    private Integer iRoomCallSensor;
    /**
     * 机房紧急呼叫电流传感器型号
     */
    private String vRoomCallSensorMode;
    /**
     * 机房安全回路电流传感器
     */
    private Integer iRoomSafeSensor;
    /**
     * 机房安全回路电流传感器型号
     */
    private String vRoomSafeSensorMode;
    /**
     * 机房厅门回路电流传感器
     */
    private Integer iRoomHallSensor;
    /**
     * 机房厅门回路电流传感器型号
     */
    private String vRoomHallSensorMode;
    /**
     * 机房抱闸回路电流传感器
     */
    private Integer iRoomSwitchCircuitSensor;
    /**
     * 机房抱闸回路电流传感器型号
     */
    private String vRoomSwitchCircuitSensorMode;
    /**
     * 机房停电服务电流传感器
     */
    private Integer iRoomCutElectricitySensor;
    /**
     * 机房停电服务电流传感器型号
     */
    private String vRoomCutElectricitySensorMode;

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

    private String vCreateUserId;

    private String vModifyUserId;
    /**
     * 状态
     */
    private Integer iStatus;

    /**
     * 是否网桥
     */
    private Integer iNetworkBridge;

    /**
     * 网桥数量
     */
    private Integer iNetworkBridgeCount;

    /**
     * 设备类型
     */
    private String vDeviceType;

    /**
     * 温度传感器
     */
    private Integer iTempSensor;

    //基准楼层
    private Integer iCollateFloorSensor;

    //4g模块
    private Integer i4gModule;

    //4g模块数量
    private Integer i4gModuleCount;

    //
    public String getvDistributionBillId() {
        return vDistributionBillId;
    }

    public void setvDistributionBillId(String vDistributionBillId) {
        this.vDistributionBillId = vDistributionBillId;
    }

    public String getvDistributionTaskId() {
        return vDistributionTaskId;
    }

    public void setvDistributionTaskId(String vDistributionTaskId) {
        this.vDistributionTaskId = vDistributionTaskId;
    }

    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId;
    }

    public String getvVillageId() {
        return vVillageId;
    }

    public void setvVillageId(String vVillageId) {
        this.vVillageId = vVillageId;
    }

    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    public String getvElevatorEquipmentId() {
        return vElevatorEquipmentId;
    }

    public void setvElevatorEquipmentId(String vElevatorEquipmentId) {
        this.vElevatorEquipmentId = vElevatorEquipmentId;
    }

    public String getvDistributerName() {
        return vDistributerName;
    }

    public void setvDistributerName(String vDistributerName) {
        this.vDistributerName = vDistributerName;
    }

    public String getvVerifyCode() {
        return vVerifyCode;
    }

    public void setvVerifyCode(String vVerifyCode) {
        this.vVerifyCode = vVerifyCode;
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public Integer getiCubeCount() {
        return iCubeCount;
    }

    public void setiCubeCount(Integer iCubeCount) {
        this.iCubeCount = iCubeCount;
    }

    public String getvConnectAdapter() {
        return vConnectAdapter;
    }

    public void setvConnectAdapter(String vConnectAdapter) {
        this.vConnectAdapter = vConnectAdapter;
    }

    public String getvConnectMode() {
        return vConnectMode;
    }

    public void setvConnectMode(String vConnectMode) {
        this.vConnectMode = vConnectMode;
    }

    public Integer getiPlcCable() {
        return iPlcCable;
    }

    public void setiPlcCable(Integer iPlcCable) {
        this.iPlcCable = iPlcCable;
    }

    public Integer getiPlcCableCount() {
        return iPlcCableCount;
    }

    public void setiPlcCableCount(Integer iPlcCableCount) {
        this.iPlcCableCount = iPlcCableCount;
    }

    public String getV4gRouterMode() {
        return v4gRouterMode;
    }

    public void setV4gRouterMode(String v4gRouterMode) {
        this.v4gRouterMode = v4gRouterMode;
    }

    public String getV4gRouterIp() {
        return v4gRouterIp;
    }

    public void setV4gRouterIp(String v4gRouterIp) {
        this.v4gRouterIp = v4gRouterIp;
    }

    public Integer getI4gRouterCount() {
        return i4gRouterCount;
    }

    public void setI4gRouterCount(Integer i4gRouterCount) {
        this.i4gRouterCount = i4gRouterCount;
    }

    public String getV4gRouterManufactor() {
        return v4gRouterManufactor;
    }

    public void setV4gRouterManufactor(String v4gRouterManufactor) {
        this.v4gRouterManufactor = v4gRouterManufactor;
    }

    public String getvRouterMode() {
        return vRouterMode;
    }

    public void setvRouterMode(String vRouterMode) {
        this.vRouterMode = vRouterMode;
    }

    public String getvRouterIp() {
        return vRouterIp;
    }

    public void setvRouterIp(String vRouterIp) {
        this.vRouterIp = vRouterIp;
    }

    public Integer getiRouterCount() {
        return iRouterCount;
    }

    public void setiRouterCount(Integer iRouterCount) {
        this.iRouterCount = iRouterCount;
    }

    public String getvRouterManufactor() {
        return vRouterManufactor;
    }

    public void setvRouterManufactor(String vRouterManufactor) {
        this.vRouterManufactor = vRouterManufactor;
    }

    public String getV4gRouterAirwireSpec() {
        return v4gRouterAirwireSpec;
    }

    public void setV4gRouterAirwireSpec(String v4gRouterAirwireSpec) {
        this.v4gRouterAirwireSpec = v4gRouterAirwireSpec;
    }

    public String getV4gRouterAirwireManufactor() {
        return v4gRouterAirwireManufactor;
    }

    public void setV4gRouterAirwireManufactor(String v4gRouterAirwireManufactor) {
        this.v4gRouterAirwireManufactor = v4gRouterAirwireManufactor;
    }

    public Integer getiCamera() {
        return iCamera;
    }

    public void setiCamera(Integer iCamera) {
        this.iCamera = iCamera;
    }

    public String getvCameraMode() {
        return vCameraMode;
    }

    public void setvCameraMode(String vCameraMode) {
        this.vCameraMode = vCameraMode;
    }

    public String getvCameraManufactor() {
        return vCameraManufactor;
    }

    public void setvCameraManufactor(String vCameraManufactor) {
        this.vCameraManufactor = vCameraManufactor;
    }

    public String getV4gModuleMode() {
        return v4gModuleMode;
    }

    public void setV4gModuleMode(String v4gModuleMode) {
        this.v4gModuleMode = v4gModuleMode;
    }

    public String getV4gModuleManufactor() {
        return v4gModuleManufactor;
    }

    public void setV4gModuleManufactor(String v4gModuleManufactor) {
        this.v4gModuleManufactor = v4gModuleManufactor;
    }

    public String getvIotNetworkCard() {
        return vIotNetworkCard;
    }

    public void setvIotNetworkCard(String vIotNetworkCard) {
        this.vIotNetworkCard = vIotNetworkCard;
    }

    public String getvIotNetworkCardOperator() {
        return vIotNetworkCardOperator;
    }

    public void setvIotNetworkCardOperator(String vIotNetworkCardOperator) {
        this.vIotNetworkCardOperator = vIotNetworkCardOperator;
    }

    public Integer getiMagnetSensor() {
        return iMagnetSensor;
    }

    public void setiMagnetSensor(Integer iMagnetSensor) {
        this.iMagnetSensor = iMagnetSensor;
    }

    public Integer getiMagnetDoorCount() {
        return iMagnetDoorCount;
    }

    public void setiMagnetDoorCount(Integer iMagnetDoorCount) {
        this.iMagnetDoorCount = iMagnetDoorCount;
    }

    public Integer getiTemperSensorCount() {
        return iTemperSensorCount;
    }

    public void setiTemperSensorCount(Integer iTemperSensorCount) {
        this.iTemperSensorCount = iTemperSensorCount;
    }

    public Integer getiBodySensor() {
        return iBodySensor;
    }

    public void setiBodySensor(Integer iBodySensor) {
        this.iBodySensor = iBodySensor;
    }

    public String getvBodySensorMode() {
        return vBodySensorMode;
    }

    public void setvBodySensorMode(String vBodySensorMode) {
        this.vBodySensorMode = vBodySensorMode;
    }

    public Integer getiFloorSensor() {
        return iFloorSensor;
    }

    public Integer getFloorSensorRemark() {
        return floorSensorRemark;
    }

    public void setFloorSensorRemark(Integer floorSensorRemark) {
        this.floorSensorRemark = floorSensorRemark;
    }

    public void setiFloorSensor(Integer iFloorSensor) {
        this.iFloorSensor = iFloorSensor;
    }

    public String getvFloorSensorWorkmode() {
        return vFloorSensorWorkmode;
    }

    public void setvFloorSensorWorkmode(String vFloorSensorWorkmode) {
        this.vFloorSensorWorkmode = vFloorSensorWorkmode;
    }

    public String getvFloorSensorMode() {
        return vFloorSensorMode;
    }

    public void setvFloorSensorMode(String vFloorSensorMode) {
        this.vFloorSensorMode = vFloorSensorMode;
    }

    public String getvShortCableLength() {
        return vShortCableLength;
    }

    public void setvShortCableLength(String vShortCableLength) {
        this.vShortCableLength = vShortCableLength;
    }

    public String getvShortCableCount() {
        return vShortCableCount;
    }

    public void setvShortCableCount(String vShortCableCount) {
        this.vShortCableCount = vShortCableCount;
    }

    public Integer getiLongCableLength() {
        return iLongCableLength;
    }

    public void setiLongCableLength(Integer iLongCableLength) {
        this.iLongCableLength = iLongCableLength;
    }

    public Integer getiLongCableCount() {
        return iLongCableCount;
    }

    public void setiLongCableCount(Integer iLongCableCount) {
        this.iLongCableCount = iLongCableCount;
    }

    public String getvPowerSupply() {
        return vPowerSupply;
    }

    public void setvPowerSupply(String vPowerSupply) {
        this.vPowerSupply = vPowerSupply;
    }

    public Integer getiPowerSupplyCount() {
        return iPowerSupplyCount;
    }

    public void setiPowerSupplyCount(Integer iPowerSupplyCount) {
        this.iPowerSupplyCount = iPowerSupplyCount;
    }

    public String getvPowerSupplyBrand() {
        return vPowerSupplyBrand;
    }

    public void setvPowerSupplyBrand(String vPowerSupplyBrand) {
        this.vPowerSupplyBrand = vPowerSupplyBrand;
    }

    public String getvPatchBorad() {
        return vPatchBorad;
    }

    public void setvPatchBorad(String vPatchBorad) {
        this.vPatchBorad = vPatchBorad;
    }

    public String getiPatchBoradCount() {
        return iPatchBoradCount;
    }

    public void setiPatchBoradCount(String iPatchBoradCount) {
        this.iPatchBoradCount = iPatchBoradCount;
    }

    public String getvPatchBoradBrand() {
        return vPatchBoradBrand;
    }

    public void setvPatchBoradBrand(String vPatchBoradBrand) {
        this.vPatchBoradBrand = vPatchBoradBrand;
    }

    public Integer getiPatchBoradLength() {
        return iPatchBoradLength;
    }

    public void setiPatchBoradLength(Integer iPatchBoradLength) {
        this.iPatchBoradLength = iPatchBoradLength;
    }

    public Integer getiAirwireCoverCount() {
        return iAirwireCoverCount;
    }

    public void setiAirwireCoverCount(Integer iAirwireCoverCount) {
        this.iAirwireCoverCount = iAirwireCoverCount;
    }

    public Integer getiCarroofElectricitySensor() {
        return iCarroofElectricitySensor;
    }

    public void setiCarroofElectricitySensor(Integer iCarroofElectricitySensor) {
        this.iCarroofElectricitySensor = iCarroofElectricitySensor;
    }

    public String getvCarroofElectricitySensorMode() {
        return vCarroofElectricitySensorMode;
    }

    public void setvCarroofElectricitySensorMode(String vCarroofElectricitySensorMode) {
        this.vCarroofElectricitySensorMode = vCarroofElectricitySensorMode;
    }

    public Integer getiCarroofLockSensor() {
        return iCarroofLockSensor;
    }

    public void setiCarroofLockSensor(Integer iCarroofLockSensor) {
        this.iCarroofLockSensor = iCarroofLockSensor;
    }

    public String getvCarroofLockSensorMode() {
        return vCarroofLockSensorMode;
    }

    public void setvCarroofLockSensorMode(String vCarroofLockSensorMode) {
        this.vCarroofLockSensorMode = vCarroofLockSensorMode;
    }

    public Integer getiRoomElectricitySensor() {
        return iRoomElectricitySensor;
    }

    public void setiRoomElectricitySensor(Integer iRoomElectricitySensor) {
        this.iRoomElectricitySensor = iRoomElectricitySensor;
    }

    public String getvRoomElectricitySensorMode() {
        return vRoomElectricitySensorMode;
    }

    public void setvRoomElectricitySensorMode(String vRoomElectricitySensorMode) {
        this.vRoomElectricitySensorMode = vRoomElectricitySensorMode;
    }

    public Integer getiRoomServiceSensor() {
        return iRoomServiceSensor;
    }

    public void setiRoomServiceSensor(Integer iRoomServiceSensor) {
        this.iRoomServiceSensor = iRoomServiceSensor;
    }

    public String getvRoomServiceSensorMode() {
        return vRoomServiceSensorMode;
    }

    public void setvRoomServiceSensorMode(String vRoomServiceSensorMode) {
        this.vRoomServiceSensorMode = vRoomServiceSensorMode;
    }

    public Integer getiRoomSwitchSensor() {
        return iRoomSwitchSensor;
    }

    public void setiRoomSwitchSensor(Integer iRoomSwitchSensor) {
        this.iRoomSwitchSensor = iRoomSwitchSensor;
    }

    public String getvRoomSwitchSensorMode() {
        return vRoomSwitchSensorMode;
    }

    public void setvRoomSwitchSensorMode(String vRoomSwitchSensorMode) {
        this.vRoomSwitchSensorMode = vRoomSwitchSensorMode;
    }

    public Integer getiRoomCallSensor() {
        return iRoomCallSensor;
    }

    public void setiRoomCallSensor(Integer iRoomCallSensor) {
        this.iRoomCallSensor = iRoomCallSensor;
    }

    public String getvRoomCallSensorMode() {
        return vRoomCallSensorMode;
    }

    public void setvRoomCallSensorMode(String vRoomCallSensorMode) {
        this.vRoomCallSensorMode = vRoomCallSensorMode;
    }

    public Integer getiRoomSafeSensor() {
        return iRoomSafeSensor;
    }

    public void setiRoomSafeSensor(Integer iRoomSafeSensor) {
        this.iRoomSafeSensor = iRoomSafeSensor;
    }

    public String getvRoomSafeSensorMode() {
        return vRoomSafeSensorMode;
    }

    public void setvRoomSafeSensorMode(String vRoomSafeSensorMode) {
        this.vRoomSafeSensorMode = vRoomSafeSensorMode;
    }

    public Integer getiRoomHallSensor() {
        return iRoomHallSensor;
    }

    public void setiRoomHallSensor(Integer iRoomHallSensor) {
        this.iRoomHallSensor = iRoomHallSensor;
    }

    public String getvRoomHallSensorMode() {
        return vRoomHallSensorMode;
    }

    public void setvRoomHallSensorMode(String vRoomHallSensorMode) {
        this.vRoomHallSensorMode = vRoomHallSensorMode;
    }

    public Integer getiRoomSwitchCircuitSensor() {
        return iRoomSwitchCircuitSensor;
    }

    public void setiRoomSwitchCircuitSensor(Integer iRoomSwitchCircuitSensor) {
        this.iRoomSwitchCircuitSensor = iRoomSwitchCircuitSensor;
    }

    public String getvRoomSwitchCircuitSensorMode() {
        return vRoomSwitchCircuitSensorMode;
    }

    public void setvRoomSwitchCircuitSensorMode(String vRoomSwitchCircuitSensorMode) {
        this.vRoomSwitchCircuitSensorMode = vRoomSwitchCircuitSensorMode;
    }

    public Integer getiRoomCutElectricitySensor() {
        return iRoomCutElectricitySensor;
    }

    public void setiRoomCutElectricitySensor(Integer iRoomCutElectricitySensor) {
        this.iRoomCutElectricitySensor = iRoomCutElectricitySensor;
    }

    public String getvRoomCutElectricitySensorMode() {
        return vRoomCutElectricitySensorMode;
    }

    public void setvRoomCutElectricitySensorMode(String vRoomCutElectricitySensorMode) {
        this.vRoomCutElectricitySensorMode = vRoomCutElectricitySensorMode;
    }

    public Date getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(Date dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    public Date getDtModifyTime() {
        return dtModifyTime;
    }

    public void setDtModifyTime(Date dtModifyTime) {
        this.dtModifyTime = dtModifyTime;
    }

    public String getvCreateUserId() {
        return vCreateUserId;
    }

    public void setvCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
    }

    public String getvModifyUserId() {
        return vModifyUserId;
    }

    public void setvModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    public Integer getiNetworkBridge() {
        return iNetworkBridge;
    }

    public void setiNetworkBridge(Integer iNetworkBridge) {
        this.iNetworkBridge = iNetworkBridge;
    }

    public Integer getiNetworkBridgeCount() {
        return iNetworkBridgeCount;
    }

    public void setiNetworkBridgeCount(Integer iNetworkBridgeCount) {
        this.iNetworkBridgeCount = iNetworkBridgeCount;
    }

    public String getvDeviceType() {
        return vDeviceType;
    }

    public void setvDeviceType(String vDeviceType) {
        this.vDeviceType = vDeviceType;
    }

    public Integer getiTempSensor() {
        return iTempSensor;
    }

    public void setiTempSensor(Integer iTempSensor) {
        this.iTempSensor = iTempSensor;
    }

    public Integer getiCollateFloorSensor() {
        return iCollateFloorSensor;
    }

    public void setiCollateFloorSensor(Integer iCollateFloorSensor) {
        this.iCollateFloorSensor = iCollateFloorSensor;
    }

    public Integer getI4gModule() {
        return i4gModule;
    }

    public void setI4gModule(Integer i4gModule) {
        this.i4gModule = i4gModule;
    }

    public Integer getI4gModuleCount() {
        return i4gModuleCount;
    }

    public void setI4gModuleCount(Integer i4gModuleCount) {
        this.i4gModuleCount = i4gModuleCount;
    }

    public Integer getCameraType() {
        return cameraType;
    }

    public void setCameraType(Integer cameraType) {
        this.cameraType = cameraType;
    }
}
