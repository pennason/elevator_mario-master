package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 电梯拓展信息 上海师大
 */
public class TblElevatorExtInfo implements Serializable {

    private static final long serialVersionUID = 8731487168444600L;

    /**
     * id
     */
    private String vElevatorInfoId;

    /**
     * 电梯id
     */
    @NotNull
    private String vElevatorId;

    /**
     * 电梯型号
     */
    private String vElevatorMode;

    /**
     * 电梯楼层
     */
    private Integer iElevatorFloor;

    /**
     * 电梯功率
     */
    private String vElevatorPower;

    /**
     * 建造时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtMadeTime;

    @JsonProperty("vElevatorInfoId")
    public String getvElevatorInfoId() {
        return vElevatorInfoId;
    }

    public void setvElevatorInfoId(String vElevatorInfoId) {
        this.vElevatorInfoId = vElevatorInfoId;
    }

    @JsonProperty("vElevatorId")
    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    @JsonProperty("vElevatorMode")
    public String getvElevatorMode() {
        return vElevatorMode;
    }

    public void setvElevatorMode(String vElevatorMode) {
        this.vElevatorMode = vElevatorMode;
    }

    @JsonProperty("iElevatorFloor")
    public Integer getiElevatorFloor() {
        return iElevatorFloor;
    }

    public void setiElevatorFloor(Integer iElevatorFloor) {
        this.iElevatorFloor = iElevatorFloor;
    }

    @JsonProperty("vElevatorPower")
    public String getvElevatorPower() {
        return vElevatorPower;
    }

    public void setvElevatorPower(String vElevatorPower) {
        this.vElevatorPower = vElevatorPower;
    }

    @JsonProperty("dtMadeTime")
    public Date getDtMadeTime() {
        return dtMadeTime;
    }

    public void setDtMadeTime(Date dtMadeTime) {
        this.dtMadeTime = dtMadeTime;
    }

    @Override
    public String toString() {
        return "TblElevatorExtInfo{" +
                "vElevatorInfoId='" + vElevatorInfoId + '\'' +
                ", vElevatorId='" + vElevatorId + '\'' +
                ", vElevatorMode='" + vElevatorMode + '\'' +
                ", iElevatorFloor=" + iElevatorFloor +
                ", vElevatorPower='" + vElevatorPower + '\'' +
                ", dtMadeTime=" + dtMadeTime +
                '}';
    }
}
