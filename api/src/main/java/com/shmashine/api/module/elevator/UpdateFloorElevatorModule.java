package com.shmashine.api.module.elevator;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @PackgeName: com.shmashine.api.module.elevator
 * @ClassName: UpdateFloorElevatorModule
 * @Date: 2020/7/2110:01
 * @Author: LiuLiFu
 * @Description: 设置楼层信息入参类
 */
public class UpdateFloorElevatorModule {
    /**
     * 电梯唯一ID
     */
    @NotNull(message = "请输入电梯Id")
    private String vElevatorId;
    /**
     * 电梯编号
     */
    @NotNull(message = "请输入电梯Code")
    private String vElevatorCode;
    /**
     * 最高楼层
     */
    @NotNull(message = "请输入最高楼层")
    private Integer iMaxFloor;
    /**
     * 最低楼层
     */
    @NotNull(message = "请输入最低楼层")
    private Integer iMinFloor;
    /**
     * 特殊楼层
     */
    // @Pattern(regexp = "^(\\d+\\,)+\\d+$",message = "请用正确得格式输入特殊楼层，逗号分隔并且结尾不能有逗号（英文哦！）")
    private String vFloorDetail;
    /**
     * 额定速度
     */
    private Double dcSpeed;

    /**
     * 电梯地址
     */
    @Size(max = 100, message = "地址不能超过100位长度")
    private String vAddress;
    /**
     * 经度
     */
    @Pattern(regexp = "^[\\-\\+]?(0(\\.\\d{1,10})?|([1-9](\\d)?)(\\.\\d{1,10})?|1[0-7]\\d{1}(\\.\\d{1,10})?|180\\.0{1,10})$", message = "请输入正确的经度")
    private String vLongitude;
    /**
     * 纬度
     */
    @Pattern(regexp = "^[\\-\\+]?(0(\\.\\d{1,10})?|([1-9](\\d)?)(\\.\\d{1,10})?|1[0-7]\\d{1}(\\.\\d{1,10})?|180\\.0{1,10})$", message = "请输入正确的纬度")
    private String vLatitude;

    /**
     * 门类型：0单门，1贯通门
     */
    private Integer doorType;

    /**
     * 设备安装位置 左开门或右开门
     */
    private String doorInstall;

    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public Integer getiMaxFloor() {
        return iMaxFloor;
    }

    public void setiMaxFloor(Integer iMaxFloor) {
        this.iMaxFloor = iMaxFloor;
    }

    public Integer getiMinFloor() {
        return iMinFloor;
    }

    public void setiMinFloor(Integer iMinFloor) {
        this.iMinFloor = iMinFloor;
    }

    public String getvFloorDetail() {
        return vFloorDetail;
    }

    public void setvFloorDetail(String vFloorDetail) {
        this.vFloorDetail = vFloorDetail;
    }


    public Double getdcSpeed() {
        return dcSpeed;
    }

    public void setdcSpeed(Double dcSpeed) {
        this.dcSpeed = dcSpeed;
    }

    public String getvAddress() {
        return vAddress;
    }

    public void setvAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    public String getvLongitude() {
        return vLongitude;
    }

    public void setvLongitude(String vLongitude) {
        this.vLongitude = vLongitude;
    }

    public String getvLatitude() {
        return vLatitude;
    }

    public void setvLatitude(String vLatitude) {
        this.vLatitude = vLatitude;
    }

    public Integer getDoorType() {
        return doorType;
    }

    public void setDoorType(Integer doorType) {
        this.doorType = doorType;
    }

    public String getDoorInstall() {
        return doorInstall;
    }

    public void setDoorInstall(String doorInstall) {
        this.doorInstall = doorInstall;
    }
}
