package com.shmashine.socket.message.handle.defaultHandle;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Maps;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.socket.elevator.service.TblElevatorService;
import com.shmashine.socket.kafka.KafkaProducer;
import com.shmashine.socket.kafka.KafkaTopicConstants;
import com.shmashine.socket.message.MessageHandle;
import com.shmashine.socket.message.bean.MessageData;
import com.shmashine.socket.message.bean.MonitorMessage;
import com.shmashine.socket.message.handle.MonitorHandle;
import com.shmashine.socket.redis.RedisService;
import com.shmashine.socket.redis.utils.RedisKeyUtils;
import com.shmashine.socket.redis.utils.RedisUtils;
import com.shmashine.socket.websocket.WebSocketManager;

import lombok.extern.slf4j.Slf4j;


/**
 * 默认monitor消息处理
 *
 * @author little.li
 */
@Slf4j
@Component
public class DefaultMonitorHandle implements MonitorHandle {

    private static final String PROTOCAL_VERSION = "default";

    private final RedisUtils redisUtils;

    private final RedisService redisService;

    private final TblElevatorService elevatorService;

    private final DefaultFaultHandle faultHandle;

    private final KafkaProducer kafkaProducer;

    /**
     * 机房报文缓存 - 用于轿顶上报报文合并后统一上报
     */
    private Map<String, MonitorMessage> motorRoomMessageCache = Maps.newConcurrentMap();

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(2, 8,
            1L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "DefaultMonitorHandle");


    @Autowired
    public DefaultMonitorHandle(RedisUtils redisUtils, RedisService redisService, TblElevatorService elevatorService,
                                DefaultFaultHandle faultHandle, KafkaProducer kafkaProducer) {

        this.redisService = redisService;
        this.elevatorService = elevatorService;
        this.faultHandle = faultHandle;
        this.kafkaProducer = kafkaProducer;
        this.redisUtils = redisUtils;
    }

    @PostConstruct
    public void registerHandle() {
        // 注册到监控、故障、困人消息的处理流程
        MessageHandle.register(this);
    }

    @Override
    public String getProtocalVersion() {
        return PROTOCAL_VERSION;
    }

    /**
     * 监控消息处理
     *
     * @param messageDate messageDate
     */
    @Override
    public void monitorHandle(MessageData messageDate) {

        String sensorType = messageDate.getSensorType();
        String elevatorCode = messageDate.getElevatorCode();
        JSONObject messageJson = messageDate.getMessageJson();

        MonitorMessage monitorMessage = new MonitorMessage();

        switch (sensorType) {

            case SocketConstants.SENSOR_TYPE_FRONT:
                //  前装DTU设备 监控消息处理
                monitorMessage = convertFrontMessage(messageJson, elevatorCode, sensorType);
                break;
            case SocketConstants.SENSOR_TYPE_CAR_ROOF:
                // 轿顶监控消息处理
                monitorMessage = convertCarRoofMessage(messageJson, elevatorCode, sensorType);
                break;
            case SocketConstants.SENSOR_TYPE_MOTOR_ROOM:
                // 机房监控消息处理
                monitorMessage = convertMotorRoomMessage(messageJson, elevatorCode, sensorType);
                break;
            case SocketConstants.SENSOR_TYPE_CUBE:
                // 初版设备消息处理（轿顶）
                monitorMessage = convertCubeMessage(messageJson, sensorType);
                break;
            case SocketConstants.SENSOR_TYPE_SINGLEBOX:
            case SocketConstants.SENSOR_TYPE_CAR_DOOR:
                // 单盒方案消息处理
                monitorMessage = convertSingleBoxMessage(messageJson, elevatorCode, sensorType);
                break;
            case SocketConstants.SENSOR_TYPE_ESCALATOR:

                //如果xizi扶梯为255故障(盒子与梯通信故障)中，则丢弃该消息
                Set<String> status = redisService.getFaultStatusByKey("ELEVATOR:FAULT:Escalator:" + elevatorCode);

                for (String s : status) {
                    if ("255".equals(s)) {
                        return;
                    }
                }
                // 扶梯监控消息处理
                monitorMessage = convertEscalatorMessage(messageJson, elevatorCode, sensorType);
                break;
            default:
                log.error("sensorType:{} is not support", sensorType);
                return;
        }

        // 推送到kafka ：monitor
        messageDate.setMonitorMessage(monitorMessage);

        executorService.submit(() -> kafkaProducer.sendMessageToKafka(KafkaTopicConstants.MONITOR_TOPIC,
                elevatorCode, JSONObject.toJSONString(messageDate)));

        // 向浏览器发送消息
        MonitorMessage finalMonitorMessage = monitorMessage;
        executorService.submit(() ->
                WebSocketManager.sendMessageToBrowsers(
                        elevatorCode, SocketConstants.MESSAGE_TYPE_MONITOR,
                        messageDate.getSensorType(),
                        JSONObject.toJSONString(finalMonitorMessage)
                ));

        // 合并机房轿顶信息后推送到 合并的 topic:
        executorService.submit(() -> sendMonitorMergeMessage(messageDate));
    }


    /**
     * 机房轿顶信息合并后推送
     *
     * @param messageDate 消息
     */
    private void sendMonitorMergeMessage(MessageData<MonitorMessage> messageDate) {
        String elevatorCode = messageDate.getElevatorCode();
        String sensorType = messageDate.getSensorType();

        // 机房消息直接缓存
        if (SocketConstants.SENSOR_TYPE_MOTOR_ROOM.equals(sensorType)) {
            motorRoomMessageCache.put(elevatorCode, messageDate.getMonitorMessage());
            return;
        }

        // 轿顶信息
        MonitorMessage carRoofMessage = messageDate.getMonitorMessage();
        // 获取缓存的机房报文
        MonitorMessage motorRoomMessage = this.motorRoomMessageCache.get(elevatorCode);

        if (null != motorRoomMessage) {
            carRoofMessage.setTemperature(motorRoomMessage.getTemperature());
            carRoofMessage.setDoorStatus(motorRoomMessage.getDoorStatus());
            carRoofMessage.setDriveStatus(motorRoomMessage.getDriveStatus());
            carRoofMessage.setSafeLoop(motorRoomMessage.getSafeLoop());
            carRoofMessage.setModeStatus(motorRoomMessage.getModeStatus());
        }

        kafkaProducer.sendMessageToKafka(KafkaTopicConstants.CUBE_MONITOR, elevatorCode,
                JSONObject.toJSONString(messageDate));
    }


    /// /////////////////////////////private方法///////////////////////////////


    private MonitorMessage convertFrontMessage(JSONObject messageJson, String elevatorCode, String sensorType) {

        MonitorMessage monitorMessage = new MonitorMessage();
        // base64报文数据解析
        monitorMessage.setFromBase64(messageJson);
        String deviceFloor = monitorMessage.getFloor();

        // 当前楼层处理
        String floor = elevatorService.getRightFloor(elevatorCode, deviceFloor, messageJson);
        monitorMessage.setFloor(floor);

        // 添加设备类型
        monitorMessage.setSensorType(sensorType);
        if (monitorMessage.getNowStatus() != null && 0 == monitorMessage.getNowStatus()) {
            // 当前设备状态为正常，清空所有故障
            faultHandle.cleanAllFault(elevatorCode, sensorType);
        }
        return monitorMessage;

    }


    /**
     * 轿顶设备监控消息处理
     *
     * @param messageJson  源上报信息
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 处理后的结果
     */
    private MonitorMessage convertCarRoofMessage(JSONObject messageJson, String elevatorCode, String sensorType) {

        MonitorMessage monitorMessage = new MonitorMessage();
        // base64报文数据解析
        monitorMessage.setFromBase64(messageJson);
        String deviceFloor = monitorMessage.getFloor();

        // 当前楼层处理
        String floor = elevatorService.getRightFloor(elevatorCode, deviceFloor, messageJson);
        monitorMessage.setFloor(floor);

        // 轿顶检修服务模式处理
        redisService.carRoofModeStatusHandle(elevatorCode, messageJson.getInteger("repair"));

        // 添加设备类型
        monitorMessage.setSensorType(sensorType);
        if (monitorMessage.getNowStatus() != null && 0 == monitorMessage.getNowStatus()) {
            // 当前设备状态为正常，清空所有故障
            faultHandle.cleanAllFault(elevatorCode, sensorType);
        }
        return monitorMessage;

    }


    /**
     * 机房监控消息处理
     *
     * @param messageJson  消息体
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 处理后的消息
     */
    private MonitorMessage convertMotorRoomMessage(JSONObject messageJson, String elevatorCode, String sensorType) {

        MonitorMessage monitorMessage = new MonitorMessage();
        // 曳引机状态-制动器提起或释放  0：提起，1：释放
        monitorMessage.setDriveStatus(messageJson.getInteger("brake"));
        // 厅门状态-门锁锁止 0：非锁止，1：锁止
        monitorMessage.setDoorStatus(messageJson.getInteger("door_loop"));
        // 机房温度
        monitorMessage.setTemperature(messageJson.getFloat("temperature"));
        // 安全回路状态-正常或断开 0：断开，1：正常
        monitorMessage.setSafeLoop(messageJson.getInteger("safe_loop") == 0 ? 1 : 0);

        // 机房检修服务模式处理
        Integer modeStatus;
        Integer repair = messageJson.getInteger("repair");
        if (repair == 1) {
            redisService.motorRoomUpdateModeStatus(elevatorCode, 1);
        } else {
            redisService.motorRoomUpdateModeStatus(elevatorCode, 0);
        }

        //停止服务模式处理
        Integer stop = messageJson.getInteger("stop");
        if (stop == 1) {
            modeStatus = redisService.motorRoomUpdateModeStatus(elevatorCode, 2);
        } else {
            modeStatus = redisService.motorRoomUpdateModeStatus(elevatorCode, 3);
        }

        monitorMessage.setModeStatus(modeStatus);

        // 添加设备类型
        monitorMessage.setSensorType(sensorType);
        return monitorMessage;
    }


    /**
     * 机房监控消息处理
     *
     * @param messageJson 消息体
     * @param sensorType  设备类型
     * @return 处理后的消息
     */
    private MonitorMessage convertCubeMessage(JSONObject messageJson, String sensorType) {
        MonitorMessage monitorMessage = new MonitorMessage();
        monitorMessage.setFloor(messageJson.getString("now_floor"));
        monitorMessage.setPowerStatus(messageJson.getInteger("power_status"));
        monitorMessage.setDirection(messageJson.getInteger("direction"));
        monitorMessage.setDoorStatus(messageJson.getInteger("door_status"));
        monitorMessage.setFloorStatus(messageJson.getInteger("floor_status"));
        monitorMessage.setSpeed(messageJson.getFloatValue("speed"));
        monitorMessage.setHasPeople(messageJson.getInteger("has_people"));
        monitorMessage.setSensorType(sensorType);
        return monitorMessage;
    }

    private MonitorMessage convertSingleBoxMessage(JSONObject messageJson, String elevatorCode, String sensorType) {

        MonitorMessage monitorMessage = new MonitorMessage();
        // base64报文数据解析
        monitorMessage.setFromBase64ForSingleBox(messageJson);
        String deviceFloor = monitorMessage.getFloor();

        // 当前楼层处理
        String floor = elevatorService.getRightFloor(elevatorCode, deviceFloor, messageJson);
        monitorMessage.setFloor(floor);

        // 服务模式处理
        Integer modeStatus;
        Integer repair = messageJson.getInteger("repair");
        if (repair != null && repair == 1) {
            modeStatus = redisService.singleBoxUpdateModeStatus(elevatorCode, 1);
        } else {
            modeStatus = redisService.singleBoxUpdateModeStatus(elevatorCode, 0);
        }

        monitorMessage.setModeStatus(modeStatus);

        // 添加设备类型
        monitorMessage.setSensorType(sensorType);
        if (monitorMessage.getNowStatus() != null && 0 == monitorMessage.getNowStatus()) {
            // 当前设备状态为正常，清空所有故障
            faultHandle.cleanAllFault(elevatorCode, sensorType);
        }
        return monitorMessage;

    }

    /**
     * 扶梯监控消息处理
     *
     * @param messageJson  消息体
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     */
    private MonitorMessage convertEscalatorMessage(JSONObject messageJson, String elevatorCode, String sensorType) {

        MonitorMessage monitorMessage = new MonitorMessage();
        // 扶梯设备base64报文数据解析
        monitorMessage.setFromBase64ForEscalator(messageJson);

        //获取当前电梯模式
        Integer escalatorStatus = monitorMessage.getEscalatorStatus();

        //获取上次电梯模式
        String statusKey = RedisKeyUtils.getElevatorStatus(elevatorCode);
        String status = redisUtils.hmGet(statusKey, "mode_status");

        //当前状态与缓存状态不同时（状态变更）
        if (escalatorStatus != Integer.parseInt(status)) {

            // 缓存当前状态
            redisUtils.hmSet(statusKey, "mode_status", String.valueOf(escalatorStatus));

            //推送到fault模块处理短信通知
            kafkaProducer.sendMessageToKafka(KafkaTopicConstants.EVENT_TOPIC,
                    "{\"TY\":\"EventEscalator\",\"elevatorCode\":\"" + elevatorCode
                            + "\",\"ST\":\"add\",\"event_type\":" + escalatorStatus + "}");

        }
        // 扶梯停止服务模式处理
        /*Integer modeStatus;
        Integer deviceModeStatus = monitorMessage.getModeStatus();
        if (deviceModeStatus == 1) {
            //检修模式设置电梯为停止服务
            modeStatus = redisService.carRoofUpdateModeStatus(elevatorCode, 2);
        } else {
            modeStatus = redisService.carRoofUpdateModeStatus(elevatorCode, 0);
        }*/

        monitorMessage.setModeStatus(monitorMessage.getModeStatus());

        // 添加设备类型
        monitorMessage.setSensorType(sensorType);
        if (monitorMessage.getNowStatus() != null && 0 == monitorMessage.getNowStatus()) {
            // 当前设备状态为正常，清空所有故障
            faultHandle.cleanAllFault(elevatorCode, sensorType);
        }
        return monitorMessage;

    }


}
