package com.shmashine.api.module.elevator;

/**
 * @PackgeName: com.shmashine.api.module.elevator
 * @ClassName: ElevatorDetailDownloadModuleMap
 * @Date: 2020/7/3016:34
 * @Author: LiuLiFu
 * @Description: 打印实体
 */
public class ElevatorDetailDownloadModuleMap {

    /**
     * 电梯注册码
     */
    private String elevatorRegister;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 省ID
     */
    private String provinceIdName;
    /**
     * 市ID
     */
    private String cityIdName;
    /**
     * 区ID
     */
    private String areaIdName;
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 小区名称
     */
    private String villageName;

    /**
     * 电梯地址
     */
    private String address;
    /**
     * 维保公司ID
     */
    private String maintainCompanyIdName;
    /**
     * 电梯类型
     */
    private String elevatorTypeName;
    /**
     * 设备安装状态 0未安装 1安装
     */
    private String installStatusName;
    /**
     * 电梯在线状态，0 离线，1在线（有一个设备在线就认为电梯在线）
     */
    private String onLineName;
    /**
     * 故障状态 0 未故障，1 故障
     */
    private String faultStatusName;
    /**
     * 电梯服务模式 0 正常运行，1 检修模式，2 停止服务
     */
    private String modeStatusName;
    /**
     * 软件版本
     */
    private String sw_version;
    /**
     * 硬件版本
     */
    private String hw_version;
    /**
     * 固件版本
     */
    private String fw_version;
    /**
     * 下盒软件版本
     */
    private String b_sw_version;
    /**
     * 下盒硬件版本
     */
    private String b_hw_version;
    /**
     * 下盒固件版本
     */
    private String b_fw_version;

    public String getElevatorRegister() {
        return elevatorRegister;
    }

    public void setElevatorRegister(String elevatorRegister) {
        this.elevatorRegister = elevatorRegister;
    }

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    public String getProvinceIdName() {
        return provinceIdName;
    }

    public void setProvinceIdName(String provinceIdName) {
        this.provinceIdName = provinceIdName;
    }

    public String getCityIdName() {
        return cityIdName;
    }

    public void setCityIdName(String cityIdName) {
        this.cityIdName = cityIdName;
    }

    public String getAreaIdName() {
        return areaIdName;
    }

    public void setAreaIdName(String areaIdName) {
        this.areaIdName = areaIdName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMaintainCompanyIdName() {
        return maintainCompanyIdName;
    }

    public void setMaintainCompanyIdName(String maintainCompanyIdName) {
        this.maintainCompanyIdName = maintainCompanyIdName;
    }

    public String getElevatorTypeName() {
        return elevatorTypeName;
    }

    public void setElevatorTypeName(String elevatorTypeName) {
        this.elevatorTypeName = elevatorTypeName;
    }

    public String getInstallStatusName() {
        return installStatusName;
    }

    public void setInstallStatusName(String installStatusName) {
        this.installStatusName = installStatusName;
    }

    public String getOnLineName() {
        return onLineName;
    }

    public void setOnLineName(String onLineName) {
        this.onLineName = onLineName;
    }

    public String getFaultStatusName() {
        return faultStatusName;
    }

    public void setFaultStatusName(String faultStatusName) {
        this.faultStatusName = faultStatusName;
    }

    public String getModeStatusName() {
        return modeStatusName;
    }

    public void setModeStatusName(String modeStatusName) {
        this.modeStatusName = modeStatusName;
    }

    public String getSw_version() {
        return sw_version;
    }

    public void setSw_version(String sw_version) {
        this.sw_version = sw_version;
    }

    public String getHw_version() {
        return hw_version;
    }

    public void setHw_version(String hw_version) {
        this.hw_version = hw_version;
    }

    public String getFw_version() {
        return fw_version;
    }

    public void setFw_version(String fw_version) {
        this.fw_version = fw_version;
    }

    public String getB_sw_version() {
        return b_sw_version;
    }

    public void setB_sw_version(String b_sw_version) {
        this.b_sw_version = b_sw_version;
    }

    public String getB_hw_version() {
        return b_hw_version;
    }

    public void setB_hw_version(String b_hw_version) {
        this.b_hw_version = b_hw_version;
    }

    public String getB_fw_version() {
        return b_fw_version;
    }

    public void setB_fw_version(String b_fw_version) {
        this.b_fw_version = b_fw_version;
    }
}
