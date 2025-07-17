package com.shmashine.sender.platform.city.shanghai;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblFaultSendLog;
import com.shmashine.common.message.FaultMessage;
import com.shmashine.common.message.MessageData;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.DateUtils;
import com.shmashine.sender.message.cache.MessageCache;
import com.shmashine.sender.message.handle.FaultHandle;
import com.shmashine.sender.message.handle.MonitorHandle;
import com.shmashine.sender.message.handle.TrappedHandle;
import com.shmashine.sender.message.send.FaultSend;
import com.shmashine.sender.message.send.MonitorSend;
import com.shmashine.sender.message.send.TrappedSend;
import com.shmashine.sender.server.dataAccount.DataAccountService;
import com.shmashine.sender.server.elevator.BizElevatorService;
import com.shmashine.sender.server.fault.TblFaultMappingService;
import com.shmashine.sender.server.fault.TblFaultSendLogService;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Slf4j
@Component
@EnableScheduling
public class YidianSender implements FaultSend, MonitorSend, TrappedSend {

    private static final String PT_CODE = "yidian";

    @Autowired
    private DataAccountService dataAccountService;

    private final ExecutorService yidianSetDataExecutor = new ShmashineThreadPoolExecutor(32, 64,
            8L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1024), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("SetData"), "yidian");

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(512, 2048,
            10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(5000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "yidian");


    /**
     * 故障告警接口url
     */
    public static final String DEFAULT_ALARM_URL = "/platform/api/v1/iotAlarms";
    /**
     * 实时数据接口url
     */
    private static final String DEFAULT_REAL_DATA_URL = "/iot/api/v1/iotRealData";
    /**
     * 统计数据接口url
     */
    private static final String DEFAULT_STATISTICS_DATA_URL = "/iot/api/v1/iotStatisticsData";

    @Autowired
    BizElevatorService bizElevatorService;
    @Autowired
    TblFaultMappingService tblFaultMappingService;
    @Autowired
    TblFaultSendLogService tblFaultSendLogService;

    @Autowired
    YidianHttpUtil yidianHttpUtil;

    @Autowired
    MessageCache messageCache;

    //创建过期缓存 4分钟
    TimedCache<String, String> timedCache = CacheUtil.newTimedCache(240000L);

    @PostConstruct
    public void registerHandle() {
        // 注册到监控、故障、困人消息的处理流程
        FaultHandle.register(this);
        MonitorHandle.register(this);
        TrappedHandle.register(this);
        timedCache.schedulePrune(1000L);
    }

    @Override
    public String getPtCode() {
        return PT_CODE;
    }

    @Override
    public void handleMonitor(MessageData messageData) {

        executorService.submit(() -> {

            String c = timedCache.get(messageData.getElevatorCode(), false);

            try {
                String body;
                log.info("YidianSender-handleMonitor-messageFormat-start - message:{}", messageData.toString());
                TblElevator elevator = bizElevatorService.getByElevatorCode(messageData.getElevatorCode());
                if (elevator.getIElevatorType() == 2) { // 扶梯
                    body = getEscalatorRealDataInfo(elevator, messageData);
                } else { // 直梯
                    body = getRealDataInfo(elevator, messageData);
                }
                yidianSetDataExecutor.execute(() -> dataAccountService.bizSetData(elevator.getVEquipmentCode(), body));

                if (StringUtils.hasText(c)) {
                    log.info("四分钟内已经推送数据-等待下次推送-elevatorCode:{}", messageData.getElevatorCode());
                    return;
                }
                timedCache.put(messageData.getElevatorCode(), messageData.getElevatorCode());

                // 电梯注册码
                String registerNumber = StringUtils.hasText(elevator.getVEquipmentCode())
                        ? elevator.getVEquipmentCode() : elevator.getVLeaveFactoryNumber();


                var monitor = yidianHttpUtil.send(registerNumber, "monitor", DEFAULT_REAL_DATA_URL, body);
                /*if (monitor != null && monitor.getStatus() == 200) {
                    timedCache.put(messageData.getElevatorCode(), messageData.getElevatorCode());
                }*/
                log.info("YidianSender-handleMonitor-messageFormat-end - message:{} result:{}", body,
                        JSON.toJSONString(monitor));

            } catch (Exception e) {
                log.error("send monitor message error : {}", ExceptionUtils.getStackTrace(e));
            }
        });

    }

    @Override
    public void handleFault(FaultMessage faultMessage) {

        if (!"add".equals(faultMessage.getST())) {
            // 无效报文落日志
            log.info("disappear no send fault : " + faultMessage);
            return;
        }

        if (null == faultMessage || !StringUtils.hasText(faultMessage.getFault_type())
                || null != messageCache.cacheFaultNX(faultMessage)) {
            return;
        }

        // 故障编码适配
        var faultMapping = tblFaultMappingService.getByPtCodeAndMxType(PT_CODE, faultMessage.getFault_type());
        if (null == faultMapping || !StringUtils.hasText(faultMapping.getFaultType())) {
            // 未找到故障映射的上海市标准故障code，不推送告警
            log.error("no faultMapping with faultMessage : {}", JSON.toJSONString(faultMessage));
            return;
        }
        faultMessage.setFault_type(faultMapping.getFaultType());
        faultMessage.setFaultName(faultMapping.getFaultName());
        postFaultData(faultMessage);
    }

    /**
     * 推送故障
     */
    @Override
    public void handleTrapped(FaultMessage faultMessage) {
        if (null == faultMessage) {
            return;
        }

        if (!"add".equals(faultMessage.getST())) {
            // 无效报文落日志
            log.info("disappear no send fault : " + faultMessage);
            return;
        }

        String faultType = faultMessage.getFault_type();
        // 非困人故障
        if (!"7".equals(faultType) && !"8".equals(faultType)) {
            // 未找到故障映射的上海市标准故障code，不推送告警
            log.error("not trapped with faultMessage : {}", JSON.toJSONString(faultMessage));
            return;
        }

        faultMessage.setFault_type("09");
        faultMessage.setFaultName("困人");
        postFaultData(faultMessage);
    }

    /**
     * 推送故障
     */
    public PostResponse postFaultData(FaultMessage faultMessage) {
        PostResponse response = null;
        String body = null;
        String responseMsg = "未知响应！";

        try {
            // 获取电梯的基本信息
            TblElevator elevator = bizElevatorService.getByElevatorCode(faultMessage.getElevatorCode());
            body = getAlarmsInfo(elevator, faultMessage);
            if (StringUtils.hasText(body)) {
                // 电梯注册码
                String registerNumber = elevator.getVEquipmentCode();
                response = yidianHttpUtil.send(registerNumber, "fault", DEFAULT_ALARM_URL, body);
                responseMsg = response.getMessage();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // 记录到推送日志
        try {
            saveFaultLog(faultMessage, body, responseMsg);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return response;
    }

    /**
     * 推送统计数据
     */
    public PostResponse postStatisticsData(TblElevator elevator) {
        PostResponse response = null;
        try {
            String body = getStatisticsData(elevator);
            if (StringUtils.hasText(body)) {
                // 电梯注册码
                String registerNumber = elevator.getVEquipmentCode();
                response = yidianHttpUtil.send(registerNumber, "statistics", DEFAULT_STATISTICS_DATA_URL, body);
            }
        } catch (Exception e) {
            log.error("推送统计数据错误，error:{}", ExceptionUtils.getStackTrace(e));
        }
        return response;
    }

    /**
     * 记录推送日志
     */
    private void saveFaultLog(FaultMessage faultMessage, String sendMessage, String responseMsg) {
        // 获取电梯的基本信息
        TblElevator elevator = bizElevatorService.getByElevatorCode(faultMessage.getElevatorCode());
        // 记录故障应该推送的信息
        TblFaultSendLog failureSendLog = new TblFaultSendLog();
        failureSendLog.setElevatorCode(elevator.getVElevatorCode());
        failureSendLog.setRegisterNo(elevator.getVEquipmentCode());
        failureSendLog.setFaultId(faultMessage.getFaultId());
        failureSendLog.setFaultType(faultMessage.getFault_type());
        failureSendLog.setSendMessage(sendMessage);
        failureSendLog.setResponseMessage(responseMsg);
        failureSendLog.setPtCodes("yidian");
        failureSendLog.setLogType("2");
        failureSendLog.setCreateTime(new Date());
        tblFaultSendLogService.insert(failureSendLog);
    }

    private String getRealDataInfo(TblElevator elevator, MessageData messageData) {

        Map<String, Object> map = new HashMap<>(64);

        if (StringUtils.hasText(elevator.getVEquipmentCode())) {
            // 电梯注册码，存量电梯（已有电梯注册码）必填
            map.put("registerNumber", elevator.getVEquipmentCode());
        } else if (StringUtils.hasText(elevator.getVManufacturerCode())
                && StringUtils.hasText(elevator.getVLeaveFactoryNumber())) {
            //制造单位统一社会信用代码，新装电梯（尚未取得电梯注册码）必填
            map.put("manufacturerCode", elevator.getVManufacturerCode());
            // 出厂编号，新装电梯（尚未取得电梯注册码）必填
            map.put("leaveFactoryNumber", elevator.getVLeaveFactoryNumber());
        } else {
            return null;
        }

        MonitorMessage monitor = messageData.getMonitorMessage();
        // (扶梯/垂直梯)当前服务模式 0:停止服务, 1:正常运行, 2:检修, 3:消防返回, 4:消防员运行, 5:应急电源运行, 6:地震模式, 7:未知
        map.put("Service_Mode", switch (monitor.getModeStatus()) {
            case 0 -> 1;
            case 1 -> 2;
            case 2 -> 0;
            default -> 1;
        });
        // (扶梯/垂直梯)运行状态 0：停止, 1：运行
        map.put("Car_Status", monitor.getRunStatus());
        //  (扶梯/垂直梯)运行方向 0：无方向, 1：上行,  2：下行
        map.put("Car_Direction", monitor.getDirection());
        // (垂直梯)开锁区域  True：轿厢在开锁区域, False：轿厢在非开锁区
        map.put("Door_Zone", (monitor.getFloorStatus() != null && monitor.getFloorStatus() == 0)); // 平层状态[0平层 1 非平层]
        // (垂直梯)电梯当前楼层
        map.put("Car_Position", monitor.getFloor() == null ? "1" : monitor.getFloor());
        // (垂直梯)轿内是否有人 True：有人, False：无人
        map.put("Passenger_Status", (monitor.getHasPeople() == 1));
        // (垂直梯)关门是否到位 True：关门到位, False：无关门到位信号
        map.put("Door_Close_Status", (monitor.getDroopClose() == 1));
        // (垂直梯)轿门状态 0: 未知, 1:正在关门, 2:关门到位, 3:正在开门, 4:开门到位, 5:门锁锁止, 6:保持不完全关闭状态
        map.put("Door_Status", monitor.getCarStatus() == 0 ? 2 : 4);
        //厅门状态 True：门锁锁止 False：无门锁锁止信号
        map.put("Histway_Door", monitor.getCarStatus() == 0);

        // 机房上报信息
        // (垂直梯)机房温度 单位为摄氏度
        //        map.put("Machine_Room_Temperature", 26.5);
        // (垂直梯)机房门开关  True：关门, False：开门
        //        map.put("Machine_Room_Door_Status", true);
        // (垂直梯)轿厢超载 True：超载, False：未超载
        //        map.put("Car_Overload", false);
        // (垂直梯)曳引机状态 0:待机, 1:曳引机制动器提起, 2:曳引机制动器释放
        map.put("Lift_Car_Drive_Status", monitor.getRunStatus());
        // (垂直梯)人脸识别实时人数
        //        map.put("People_Number", 5);
        // (垂直梯)乘梯人行为模式 0：正常, 1：不文明行为, 2：危险行为
        map.put("Activity_Mode", "0");
        //采样时间必填，格式（yyyy-MM-dd HH:mm:ss）
        //map.put("samplingTime", DateUtils.formatDate(messageData.getTime()));
        map.put("samplingTime", messageData.getTime());
        String jsonStr = JSONObject.toJSONString(map);
        return jsonStr;
    }

    /**
     * 扶梯实时数据
     */
    private String getEscalatorRealDataInfo(TblElevator elevator, MessageData messageData) {
        Map<String, Object> map = new HashMap<>(64);
        if (StringUtils.hasText(elevator.getVEquipmentCode())) {
            // 电梯注册码，存量电梯（已有电梯注册码）必填
            map.put("registerNumber", elevator.getVEquipmentCode());
        } else if (StringUtils.hasText(elevator.getVManufacturerCode())
                && StringUtils.hasText(elevator.getVLeaveFactoryNumber())) {
            //制造单位统一社会信用代码，新装电梯（尚未取得电梯注册码）必填
            map.put("manufacturerCode", elevator.getVManufacturerCode());
            // 出厂编号，新装电梯（尚未取得电梯注册码）必填
            map.put("leaveFactoryNumber", elevator.getVLeaveFactoryNumber());
        } else {
            return null;
        }

        // (扶梯)当前服务模式 0:停止服务, 1:正常运行, 2:检修, 3:消防返回, 4:消防员运行, 5:应急电源运行, 6:地震模式, 7:未知
        map.put("Escalator_Service_Mode", "1");
        // (扶梯/垂直梯)运行状态 0：停止, 1：运行
        map.put("Car_Status", "1");
        //  (扶梯/垂直梯)运行方向 0：无方向, 1：上行,  2：下行
        map.put("Car_Direction", "1");
        // (扶梯)运行方向 0:节能停止运行, 1:名义速度上行, 2:节能减速上行, 3:名义速度下行, 4:节能速度下行
        map.put("Escalator_Operation_Direction", "1");
        //采样时间必填，格式（yyyy-MM-dd HH:mm:ss）
        //map.put("samplingTime", DateUtils.formatDate(messageData.getTime()));
        map.put("samplingTime", messageData.getTime());
        String jsonStr = JSONObject.toJSONString(map);
        return jsonStr;
    }

    // CHECKSTYLE:OFF
    private String getAlarmsInfo(TblElevator elevator, FaultMessage faultMessage) {
        if (!StringUtils.hasText(elevator.getVEquipmentCode())) {
            return null;
        }

        YidianFailureData yiDianF = new YidianFailureData();
        // 供应商代码+'-'+供应商报警id，保证唯一， 例如 xzl-0000001, wxcl-a000111
        yiDianF.setAlarmId("shmx-" + faultMessage.getFaultId());
        // 事件来源: 110电话：S01，物业：S02,维保：S03，物联网：S04，小程序：S05
        yiDianF.setAlarmChannel("S04");
        yiDianF.setRegisterNumber(elevator.getVEquipmentCode());
        yiDianF.setOccurTime(faultMessage.getTime());
        // 获取上海标准的故障code
        yiDianF.setFailureCode(faultMessage.getFault_type());
        // 获取上海标准的故障名称
        yiDianF.setEventDesc(faultMessage.getFaultName());
        // 故障收集时间
        yiDianF.setStatusCollectTime(faultMessage.getTime());
        //增加电梯告警时刻的运行参数
        List<Map<String, String>> listmap = new ArrayList<>();
        MonitorMessage monitor = faultMessage.getMonitorMessage();
        if (null != monitor) {
            // speed
            Float speed = monitor.getSpeed();
            if (null != speed) {
                Map<String, String> mapSpeed = new HashMap<>();
                mapSpeed.put("paramCode", "speed");
                mapSpeed.put("paramName", "电梯的运行速度");
                mapSpeed.put("paramValue", speed.toString());
                listmap.add(mapSpeed);
            }

            // Passenger_Status
            Integer hasPeople = monitor.getHasPeople();
            if (null != hasPeople) {
                Map<String, String> mapPassengerStatus = new HashMap<>();
                mapPassengerStatus.put("paramCode", "Passenger_Status");
                mapPassengerStatus.put("paramName", "骄箱是否有人");
                if (hasPeople == 0) {
                    mapPassengerStatus.put("paramValue", "False");
                } else {
                    mapPassengerStatus.put("paramValue", "True");
                }
                listmap.add(mapPassengerStatus);
            }

            // Door_Close_Status
            Integer doorCloseStatus = monitor.getDroopClose();
            if (null != doorCloseStatus) {
                Map<String, String> mapDoorCloseStatus = new HashMap<>();
                mapDoorCloseStatus.put("paramCode", "Door_Close_Status");
                mapDoorCloseStatus.put("paramName", "关门是否到位");
                // 0无关门到位、1关门到位
                if (doorCloseStatus == 0) {
                    mapDoorCloseStatus.put("paramValue", "False");
                } else {
                    mapDoorCloseStatus.put("paramValue", "True");
                }
                listmap.add(mapDoorCloseStatus);
            }

            // Door_Status
            Integer doorStatus = monitor.getDoorStatus();
            if (null != doorStatus) {
                Map<String, String> mapDoorStatus = new HashMap<>();
                mapDoorStatus.put("paramCode", "Door_Status");
                mapDoorStatus.put("paramName", "轿门状态");
                mapDoorStatus.put("paramValue", doorStatus.toString());
                listmap.add(mapDoorStatus);
            }

            // Car_Status
            Integer carStatus = monitor.getRunStatus();
            if (null != carStatus) {
                Map<String, String> mapCarStatus = new HashMap<>();
                mapCarStatus.put("paramCode", "Car_Status");
                mapCarStatus.put("paramName", "轿厢运行状态");
                mapCarStatus.put("paramValue", carStatus.toString());
                listmap.add(mapCarStatus);
            }

            // Car_Position
            String floor = monitor.getFloor();
            if (null != floor) {
                Map<String, String> mapFloor = new HashMap<>();
                mapFloor.put("paramCode", "Car_Position");
                mapFloor.put("paramName", "电梯所在的楼层");
                mapFloor.put("paramValue", floor);
                listmap.add(mapFloor);
            }

            // Car_Direction
            Integer direction = monitor.getDirection();
            if (null != direction) {
                Map<String, String> mapDirection = new HashMap<>();
                mapDirection.put("paramCode", "Car_Direction");
                mapDirection.put("paramName", "骄箱运行方向");
                mapDirection.put("paramValue", direction.toString());
                listmap.add(mapDirection);
            }

            // Package_Time
            String timestamp = faultMessage.getTime();
            if (null != timestamp) {
                Map<String, String> mapTimestamp = new HashMap<>();
                mapTimestamp.put("paramCode", "Package_Time");
                mapTimestamp.put("paramName", "数据生产时间");
                mapTimestamp.put("paramValue", timestamp);
                listmap.add(mapTimestamp);
            }
        }
        yiDianF.setStatusDetails(listmap);
        return JSONObject.toJSONString(yiDianF);
    }
    // CHECKSTYLE:ON

    /**
     * 获取
     */
    private String getStatisticsData(TblElevator elevator) {

        if (elevator == null) {
            return null;
        }
        //  表中字段：  code,elevator_equipment_code,door_times,start_running_time,run_count,run_distance,bend_count
        Map<String, Object> map = new HashMap<>(64);
        if (StringUtils.hasText(elevator.getVEquipmentCode())) {
            // 电梯注册码，存量电梯（已有电梯注册码）必填
            map.put("registerNumber", elevator.getVEquipmentCode());
        } else if (StringUtils.hasText(elevator.getVManufacturerCode())
                && StringUtils.hasText(elevator.getVLeaveFactoryNumber())) {
            //制造单位统一社会信用代码，新装电梯（尚未取得电梯注册码）必填
            map.put("manufacturerCode", elevator.getVManufacturerCode());
            // 出厂编号，新装电梯（尚未取得电梯注册码）必填
            map.put("leaveFactoryNumber", elevator.getVLeaveFactoryNumber());
        } else {
            return null;
        }

        // (垂直梯)设备开门次数
        map.put("Door_Open_Count", elevator.getBiDoorCount());
        // (扶梯/垂直梯)能耗统计 单位千瓦时
        //  map.put("Power_Consumption", 110.56);
        // (扶梯/垂直梯)设备累计运行时间 单位小时
        Long startRunningTime = ((LocalDateTime) elevator.getDtInstallTime()).atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        //        Date startRunningTime = ((LocalDateTime) elevator.getDtInstallTime());
        if (null != startRunningTime) {
            long runTime = System.currentTimeMillis() - startRunningTime;
            runTime = runTime / (3600 * 1000);
            map.put("Total_Running_Time", runTime);
        }
        // (扶梯/垂直梯)设备累计运行次数
        map.put("Present_Counter_Value", elevator.getBiRunCount());
        // (垂直梯)设备钢丝绳（带）折弯次数
        map.put("Rope_Bend_Count", elevator.getBiBendCount());
        // (扶梯/垂直梯)设备累计运行距离  单位米
        map.put("Lift_Mileage", elevator.getBiRunDistanceCount());
        // (扶梯/垂直梯)乘梯人数
        //        map.put("Passenger_Counter", 1112);
        //采样时间必填，格式（yyyy-MM-dd HH:mm:ss）
        map.put("samplingTime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        String jsonStr = JSONObject.toJSONString(map);
        return jsonStr;
    }
}

