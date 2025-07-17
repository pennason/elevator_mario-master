package com.shmashine.sender.platform.city.shanghai;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblElevatorInfos;
import com.shmashine.common.message.FaultMessage;
import com.shmashine.common.message.MessageData;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.sender.message.handle.FaultHandle;
import com.shmashine.sender.message.handle.MonitorHandle;
import com.shmashine.sender.message.handle.TrappedHandle;
import com.shmashine.sender.message.send.FaultSend;
import com.shmashine.sender.message.send.MonitorSend;
import com.shmashine.sender.message.send.TrappedSend;
import com.shmashine.sender.server.dataAccount.BizDataAccountService;
import com.shmashine.sender.server.elevator.BizElevatorService;

import lombok.extern.slf4j.Slf4j;

/**
 * 临港推送
 *
 * @author jiangheng
 * @since 2022/11/7 16:24
 */

@Slf4j
@Component
public class LinGangSender implements FaultSend, MonitorSend, TrappedSend {

    private static final String PT_CODE = "linGang";

    @Autowired
    BizElevatorService bizElevatorService;

    @Autowired
    LinGangHttpUtil linGangHttpUtil;

    @Autowired
    private BizDataAccountService bizDataAccountService;

    @PostConstruct
    public void registerHandle() {
        // 注册到监控、故障、困人消息的处理流程
        FaultHandle.register(this);
        MonitorHandle.register(this);
        TrappedHandle.register(this);
    }

    @Override
    public String getPtCode() {
        return PT_CODE;
    }


    @Override
    public void handleMonitor(MessageData messageData) {

        if (null == messageData) {
            return;
        }

        TblElevator elevator = bizElevatorService.getByElevatorCode(messageData.getElevatorCode());

        // 电梯注册码
        String registerNumber = StringUtils.isNotBlank(elevator.getVEquipmentCode())
                ? elevator.getVEquipmentCode() : elevator.getVLeaveFactoryNumber();

        //实时数据格式转换
        var realDataInfo = getRealDataInfo(registerNumber, messageData);

        String url = StrUtil.format("{}/{}/user/data", "005b3548375b17c6", registerNumber);

        linGangHttpUtil.send(registerNumber, "monitor", url, realDataInfo.toJSONString(0));

    }

    //困人
    @Override
    public void handleTrapped(FaultMessage faultMessage) {

        if (faultMessage == null) {
            return;
        }

        // 获取电梯的基本信息
        TblElevator elevator = bizElevatorService.getByElevatorCode(faultMessage.getElevatorCode());

        // 电梯注册码
        String registerNumber = StringUtils.isNotBlank(elevator.getVEquipmentCode())
                ? elevator.getVEquipmentCode() : elevator.getVLeaveFactoryNumber();

        //困人数据格式转换
        JSONObject trappedDataInfo = getFaultDataInfo(registerNumber, elevator, faultMessage, "entrapWO", "困人急救工单");

        String url = StrUtil.format("{}/{}/user/data", "005b3548375b17c6", registerNumber);

        linGangHttpUtil.send(elevator.getVEquipmentCode(), "fault", url, trappedDataInfo.toJSONString(0));

    }


    @Override
    public void handleFault(FaultMessage faultMessage) {

        if (null == faultMessage) {
            return;
        }
        // 获取电梯的基本信息
        TblElevator elevator = bizElevatorService.getByElevatorCode(faultMessage.getElevatorCode());

        // 电梯注册码
        String registerNumber = StringUtils.isNotBlank(elevator.getVEquipmentCode())
                ? elevator.getVEquipmentCode() : elevator.getVLeaveFactoryNumber();

        JSONObject trappedDataInfo;

        if (faultMessage.getUncivilizedBehaviorFlag() == 0) {

            //故障
            trappedDataInfo = getFaultDataInfo(registerNumber, elevator, faultMessage, "emergencyWO", "急修工单");
        } else {

            //不文明行为
            trappedDataInfo = getFaultDataInfo(registerNumber, elevator, faultMessage, "uncivilizedWO", "不文明行为工单");
        }

        //故障数据格式转换
        String url = StrUtil.format("{}/{}/user/data", "005b3548375b17c6", registerNumber);

        linGangHttpUtil.send(elevator.getVEquipmentCode(), "fault", url, trappedDataInfo.toJSONString(0));


    }

    public void pushBasicInformation2Lingang() {
        //获取临港电梯基础信息
        List<TblElevatorInfos> elevators = bizDataAccountService.getElevatorsInfosByLingang();

        for (TblElevatorInfos elevator : elevators) {

            String registerNumber = elevator.getVEquipmentCode();

            JSONObject content = new JSONObject();
            content.put("elevatorCode", elevator.getVElevatorCode());
            content.put("registrtionCode", registerNumber);
            content.put("installTime", DateUtil.format((LocalDateTime) elevator.getDtInstallTime(),
                            "yyyy-MM-dd HH:mm:ss"));
            content.put("address", elevator.getVAddress());
            content.put("provinceIdName", elevator.getProvinceName());
            content.put("cityIdName", elevator.getCityName());
            content.put("areaIdName", elevator.getAreaName());
            content.put("minFloor", elevator.getIMinFloor());
            content.put("maxFloor", elevator.getIMaxFloor());
            content.put("elevatorTypeName", elevator.getIElevatorType() == 1 ? "直梯" : "扶梯");
            content.put("elevatorBrandIdName", elevator.getElevatorBrandName());
            content.put("projectIdName", elevator.getProjectName());
            content.put("villageIdName", elevator.getVVillageName());
            content.put("longitude", elevator.getVLongitude());
            content.put("latitude", elevator.getVLatitude());
            content.put("maintenanceCompanyName", elevator.getMaintainCompanyName());
            content.put("maintainPersonName", elevator.getVMaintainPersonName());
            content.put("maintainPersonTel", elevator.getVMaintainPersonTel());

            var params = Map.of("msgType", "liftBasicInfo",
                    "description", "电梯基础数据",
                    "content", content);

            String url = StrUtil.format("{}/{}/user/data", "005b3548375b17c6", registerNumber);

            //推送数据
            linGangHttpUtil.send(registerNumber, "statistics", url, JSON.toJSONString(params));

        }
    }


    private JSONObject getRealDataInfo(String registrtionCode, MessageData messageData) {

        MonitorMessage monitor = messageData.getMonitorMessage();

        JSONObject content = new JSONObject();
        content.put("elevatorCode", messageData.getElevatorCode());
        content.put("registrtionCode", registrtionCode);
        content.put("carStatus", monitor.getCarStatus());
        content.put("carroofAccidentShift", monitor.getCarroofAccidentShift());
        content.put("direction", monitor.getDirection());
        content.put("doorLoop", monitor.getDoorLoop());
        content.put("doorStatus", monitor.getDoorStatus());
        content.put("driveStatus", monitor.getDriveStatus());
        content.put("doorClose", monitor.getDroopClose());
        content.put("floor", monitor.getFloor());
        content.put("floorStatus", monitor.getFloorStatus());
        content.put("hasPeople", monitor.getHasPeople());
        content.put("modeStatus", monitor.getModeStatus());
        content.put("nowStatus", monitor.getModeStatus());
        content.put("safeLoop", monitor.getSafeLoop());
        content.put("speed", monitor.getSpeed());
        content.put("stopOutLockArea", monitor.getStopOutLockArea());
        content.put("temperature", monitor.getTemperature());

        var params = Map.of("msgType", "runInfo",
                "description", "实时运行数据",
                "content", content);

        return JSON.parseObject(JSON.toJSONString(params));

    }

    private JSONObject getFaultDataInfo(String registerNumber, TblElevator elevator, FaultMessage fault,
                                        String msgType, String description) {

        var content = Map.of("elevatorCode", fault.getElevatorCode(),
                "registrtionCode", registerNumber,
                "faultId", fault.getFaultId(),
                "reportTime", fault.getTime(),
                "faultType", fault.getFault_type(),
                "faultName", fault.getFaultName(),
                "faultStatus", "add".equals(fault.getST()) ? 1 : 0,
                "address", elevator.getVAddress(),
                "villageName", elevator.getVVillageName());

        var params = Map.of("msgType", msgType,
                "description", description,
                "content", content);

        return JSON.parseObject(JSON.toJSONString(params));
    }
}
