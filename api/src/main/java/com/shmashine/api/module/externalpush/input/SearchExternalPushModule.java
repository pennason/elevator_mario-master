package com.shmashine.api.module.externalpush.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.common.entity.base.PageListParams;

/**
 * @PackgeName: com.shmashine.api.module.externalpush.input
 * @ClassName: SearchExternalPushModule
 * @Date: 2020/7/818:32
 * @Author: LiuLiFu
 * @Description: 查询对外推送列表参数类
 */
public class SearchExternalPushModule extends PageListParams {
    /**
     * ID
     */
    private String vFaultSendShiledId;
    /**
     * 电梯ID
     */
    private String vElevatorId;
    /**
     * 电梯编号
     */
    private String vElevatorCode;
    /**
     * 电梯注册码
     */
    private String vRegisterNo;
    /**
     * 故障类型
     */
    private String vFaultType;
    /**
     * 故障名称
     */
    private String vFaultName;
    /**
     * 推送的平台
     */
    private String vPtCode;
    /**
     * 是否推送，0 屏蔽推送，1 推送
     */
    private Integer iIsReport;

    @JsonProperty("vFaultSendShiledId")
    public String getVFaultSendShiledId() {
        return vFaultSendShiledId;
    }

    public void setVFaultSendShiledId(String vFaultSendShiledId) {
        this.vFaultSendShiledId = vFaultSendShiledId;
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

    @JsonProperty("vRegisterNo")
    public String getVRegisterNo() {
        return vRegisterNo;
    }

    public void setVRegisterNo(String vRegisterNo) {
        this.vRegisterNo = vRegisterNo;
    }

    @JsonProperty("vFaultType")
    public String getVFaultType() {
        return vFaultType;
    }

    public void setVFaultType(String vFaultType) {
        this.vFaultType = vFaultType;
    }

    @JsonProperty("vFaultName")
    public String getVFaultName() {
        return vFaultName;
    }

    public void setVFaultName(String vFaultName) {
        this.vFaultName = vFaultName;
    }

    @JsonProperty("vPtCode")
    public String getVPtCode() {
        return vPtCode;
    }

    public void setVPtCode(String vPtCode) {
        this.vPtCode = vPtCode;
    }

    @JsonProperty("iIsReport")
    public Integer getIIsReport() {
        return iIsReport;
    }

    public void setIIsReport(Integer iIsReport) {
        this.iIsReport = iIsReport;
    }
}
