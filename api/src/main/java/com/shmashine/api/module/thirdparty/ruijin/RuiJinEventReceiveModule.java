package com.shmashine.api.module.thirdparty.ruijin;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @PackgeName: com.shmashine.api.module.thirdparty.ruijin
 * @ClassName: RuiJinEventReceiveModule
 * @Date: 2020/7/2212:31
 * @Author: LiuLiFu
 * @Description: 接受故障数据接口类
 */
public class RuiJinEventReceiveModule {

    /**
     * 事件编号
     */
    @NotNull(message = "请输入事件编号")
    private String eventNumber;
    /**
     * 事件来源
     */
    @NotNull(message = "请输入事件来源")
    private String eventChannel;
    /**
     * 注册代码
     */
    @NotNull(message = "请输入注册代码")
    private String registerNumber;
    /**
     * 报警时间
     */
    @NotNull(message = "请输入报警时间")
    private Object occurTime;
    /**
     * 当前状态
     */
    @NotNull(message = "请输入当前状态、")
    private Integer currentStatus;
    /**
     * 事件分类
     */
    @NotNull(message = "请输入事件分类、")
    private Integer eventType;
    /**
     * 故障类型代码
     */
    private String failureCode;
    /**
     * 事件内容
     */
    private String eventDesc;
    /**
     * 报警人
     */
    private String reporter;
    /**
     * 报警人电话
     */
    private String reporterTel;
    /**
     * 救援人数
     */
    private Integer rescueNum;
    /**
     * 处理人
     */
    private String handler;
    /**
     * 处理人电话
     */
    private String handlerTel;
    /**
     * 救援单位名称
     */
    private String rescueCompanyName;

    @NotNull(message = "请输入事件明细")
    @Size(min = 1, message = "至少输入一条明细数据")
    private ArrayList<RuiJinEventReceiveDetailModule> statusLogs;

    public ArrayList<RuiJinEventReceiveDetailModule> getStatusLogs() {
        return statusLogs;
    }

    public void setStatusLogs(ArrayList<RuiJinEventReceiveDetailModule> statusLogs) {
        this.statusLogs = statusLogs;
    }

    public String getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(String eventNumber) {
        this.eventNumber = eventNumber;
    }

    public String getEventChannel() {
        return eventChannel;
    }

    public void setEventChannel(String eventChannel) {
        this.eventChannel = eventChannel;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public Object getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Object occurTime) {
        this.occurTime = occurTime;
    }

    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public String getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(String failureCode) {
        this.failureCode = failureCode;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getReporterTel() {
        return reporterTel;
    }

    public void setReporterTel(String reporterTel) {
        this.reporterTel = reporterTel;
    }

    public Integer getRescueNum() {
        return rescueNum;
    }

    public void setRescueNum(Integer rescueNum) {
        this.rescueNum = rescueNum;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getHandlerTel() {
        return handlerTel;
    }

    public void setHandlerTel(String handlerTel) {
        this.handlerTel = handlerTel;
    }

    public String getRescueCompanyName() {
        return rescueCompanyName;
    }

    public void setRescueCompanyName(String rescueCompanyName) {
        this.rescueCompanyName = rescueCompanyName;
    }

    @Override
    public String toString() {
        return "RuiJinEventReceiveModule{" +
                "eventNumber='" + eventNumber + '\'' +
                ", eventChannel='" + eventChannel + '\'' +
                ", registerNumber='" + registerNumber + '\'' +
                ", occurTime=" + occurTime +
                ", currentStatus=" + currentStatus +
                ", eventType=" + eventType +
                ", failureCode='" + failureCode + '\'' +
                ", eventDesc='" + eventDesc + '\'' +
                ", reporter='" + reporter + '\'' +
                ", reporterTel='" + reporterTel + '\'' +
                ", rescueNum=" + rescueNum +
                ", handler='" + handler + '\'' +
                ", handlerTel='" + handlerTel + '\'' +
                ", rescueCompanyName='" + rescueCompanyName + '\'' +
                ", statusLogs=" + statusLogs +
                '}';
    }
}
