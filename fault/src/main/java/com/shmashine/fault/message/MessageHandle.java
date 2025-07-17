package com.shmashine.fault.message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.TblCameraImageIdentifyEntity;
import com.shmashine.common.entity.TblDevice;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.common.enums.CameraTaskTypeEnum;
import com.shmashine.common.message.PeopleFlowStatisticsMessage;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.fault.dal.dao.DeviceConfDao;
import com.shmashine.fault.dal.dao.TblDeviceDao;
import com.shmashine.fault.dal.dataobject.DeviceParamConfDO;
import com.shmashine.fault.elevator.entity.TblElevator;
import com.shmashine.fault.elevator.service.TblElevatorService;
import com.shmashine.fault.fault.service.TblFaultServiceI;
import com.shmashine.fault.message.handle.EscalatorFaultHandle;
import com.shmashine.fault.message.handle.EventHandle;
import com.shmashine.fault.message.handle.FaultHandle;
import com.shmashine.fault.message.handle.PreFaultHandle;
import com.shmashine.fault.mongo.entity.DevicePingQuality;
import com.shmashine.fault.mongo.utils.MongoTemplateUtil;
import com.shmashine.fault.service.PeopleFlowStatistics.PeopleFlowStatisticsService;
import com.shmashine.hkcamerabyys.client.RemoteHikEzvizClient;

import lombok.extern.slf4j.Slf4j;


/**
 * 故障消息处理
 *
 * @author little.li
 */
@Slf4j
@Component
public class MessageHandle {

    private static Logger faultMessageLogger = LoggerFactory.getLogger("faultMessageLogger");

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(8, 16,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "MessageHandle");


    private final TblElevatorService elevatorService;

    //默认故障处理
    private final FaultHandle faultHandle;

    //1.0.0E 协议故障处理
    private final FaultHandle faultHandleV110E;

    private final EscalatorFaultHandle escalatorFaultHandle;

    private final PreFaultHandle preFaultHandle;

    private final EventHandle eventHandle;

    private final TblFaultServiceI tblFaultService;

    private final RedisTemplate redisTemplate;

    private final DeviceConfDao deviceConfDao;

    private final TblDeviceDao deviceDao;

    private final PeopleFlowStatisticsService peopleFlowStatisticsService;

    private final RemoteHikEzvizClient remoteHikEzvizClient;

    @Autowired
    private RedissonClient redissonClient;

    @Resource
    private MongoTemplateUtil mongoTemplateUtil;

    @Autowired
    public MessageHandle(TblElevatorService elevatorService, EscalatorFaultHandle escalatorFaultHandle,
                         FaultHandle faultHandle, PreFaultHandle preFaultHandle,
                         EventHandle eventHandle, TblFaultServiceI tblFaultService, RedisTemplate redisTemplate,
                         @Qualifier("faultHandleV100E") FaultHandle faultHandleV110E, DeviceConfDao deviceConfDao,
                         TblDeviceDao deviceDao, PeopleFlowStatisticsService peopleFlowStatisticsService,
                         RemoteHikEzvizClient remoteHikEzvizClient) {

        this.elevatorService = elevatorService;
        this.faultHandle = faultHandle;
        this.preFaultHandle = preFaultHandle;
        this.escalatorFaultHandle = escalatorFaultHandle;
        this.eventHandle = eventHandle;
        this.tblFaultService = tblFaultService;
        this.redisTemplate = redisTemplate;
        this.faultHandleV110E = faultHandleV110E;
        this.deviceConfDao = deviceConfDao;
        this.deviceDao = deviceDao;
        this.peopleFlowStatisticsService = peopleFlowStatisticsService;
        this.remoteHikEzvizClient = remoteHikEzvizClient;
    }


    /**
     * 设备故障落库消息处理
     *
     * @param message 消息内容
     */
    public void messageHandle(String message) {

        faultMessageLogger.info("{} --收到故障消息 faultMessage[{}] ", TimeUtils.nowTime(), message);

        JSONObject messageJson = JSONObject.parseObject(message);
        String type = messageJson.getString("TY");
        String stype = messageJson.getString("ST");

        //  TY: Fault 故障消息处理
        if (MessageConstants.TYPE_FAULT.equals(type) || MessageConstants.TYPE_FAULT_FRONT.equals(type)) {
            faultMessageHandle(messageJson, stype);
        }

        // 西子扶梯故障标准
        if (MessageConstants.TYPE_FAULT_ESCALATOR.equals(type)) {
            escalatorFaultMessageHandle(messageJson, stype);
        }

        // 传感器故障处理
        if (MessageConstants.TYPE_FAULT_SENSOR.equals(type)) {
            sensorFaultMessageHandle(messageJson, stype);
        }

        // TY: Update 更新消息处理（ip, freq, floor等）
        if (MessageConstants.TYPE_UPDATE.equals(type)) {
            updateMessageHandle(messageJson);
        }

        //  TY: confirm 设置返回消息处理
        if (MessageConstants.TYPE_CONFIRM.equals(type)) {
            confirmMessageHandle(messageJson, stype);
        }

    }


    /**
     * 设备统计落库消息处理
     *
     * @param message 消息内容
     */
    public void trMessageHandle(String message) {

        log.info("收到设备统计消息，message:{}", message);

        JSONObject messageJson = JSONObject.parseObject(message);
        String type = messageJson.getString("TY");
        String stype = messageJson.getString("ST");

        //  TY: TR 统计信息处理
        if (MessageConstants.TYPE_TR.equals(type)) {

            String protocalVersion = messageJson.getString("protocalVersion");

            if ("1.0.0E".equals(protocalVersion)) {
                // 并发更新电梯统计信息
                executorService.submit(() -> trMessageHandleV100E(messageJson, stype));
            } else {
                // 并发更新电梯统计信息
                executorService.submit(() -> trMessageHandle(messageJson, stype));
            }
        }
    }

    ////////////////////////////////private method///////////////////////////////


    /**
     * 故障落库处理
     */
    private void faultMessageHandle(JSONObject messageJson, String stype) {

        String elevatorCode = messageJson.getString("elevatorCode");
        String faultType = messageJson.getString("fault_type");
        String protocalVersion = messageJson.getString("protocalVersion");

        RLock lock = redissonClient.getFairLock(RedisConstants.ELEVATOR_FAULT_MESSAGE_MARK_LOCK
                + elevatorCode + faultType);

        try {

            //尝试加锁，最多等待300s，上锁以后10分钟自动解锁
            if (lock.tryLock(300, 600, TimeUnit.SECONDS)) {

                try {

                    // ST : add 新增故障处理
                    if (MessageConstants.STYPE_ADD.equals(stype)) {

                        if ("1.0.0E".equals(protocalVersion)) {
                            faultHandleV110E.addFault(messageJson);
                            return;
                        }
                        faultHandle.addFault(messageJson);
                        return;
                    }
                    // ST : disappear 设备消除故障处理
                    if (MessageConstants.STYPE_DISAPPEAR.equals(stype)) {
                        if ("1.0.0E".equals(protocalVersion)) {
                            faultHandleV110E.disappearFault(messageJson);
                            return;
                        }
                        faultHandle.disappearFault(messageJson);
                    }

                } finally {
                    lock.unlock();
                }

            }

        } catch (InterruptedException e) {
            //
        }

    }

    /**
     * 西子扶梯 模式切换处理
     */
    public void eventMessageHandle(String message) {

        eventHandle.eventChange(JSONObject.parseObject(message));
    }

    /**
     * 西子扶梯故障标准处理
     */
    private void escalatorFaultMessageHandle(JSONObject messageJson, String stype) {
        // ST : add 新增故障处理
        if (MessageConstants.STYPE_ADD.equals(stype)) {
            escalatorFaultHandle.addFault(messageJson);
            return;
        }
        // ST : disappear 设备消除故障处理
        if (MessageConstants.STYPE_DISAPPEAR.equals(stype)) {
            escalatorFaultHandle.disappearFault(messageJson);
        }

    }

    /**
     * 传感器故障处理
     *
     * @param messageJson 故障消息
     * @param stype       故障类型
     */
    private void sensorFaultMessageHandle(JSONObject messageJson, String stype) {
        // ST : add 新增传感器故障处理(特定时间才记录)
        if (MessageConstants.STYPE_ADD.equals(stype)) {
            Date date = new Date();
            int hours = date.getHours();
            if (hours >= 7 && hours <= 21) {
                faultHandle.addSensorFault(messageJson);
            }
            return;
        }
        // ST : disappear 设备消除传感器故障处理
        if (MessageConstants.STYPE_DISAPPEAR.equals(stype)) {
            faultHandle.disappearSensorFault(messageJson);
        }

    }

    /**
     * 待确认故障 - 故障落库处理
     */
    public void preFaultMessageHandle(String message) {

        JSONObject messageJson = JSONObject.parseObject(message);
        String stype = messageJson.getString("ST");

        String elevatorCode = messageJson.getString("elevatorCode");
        String faultType = messageJson.getString("fault_type");

        RLock lock = redissonClient.getFairLock(RedisConstants.ELEVATOR_FAULT_MESSAGE_MARK_LOCK
                + elevatorCode + faultType);

        try {

            //尝试加锁，最多等待300s，上锁以后10分钟自动解锁
            if (lock.tryLock(300, 600, TimeUnit.SECONDS)) {

                try {

                    // ST : add 新增故障处理
                    if (MessageConstants.STYPE_ADD.equals(stype)) {
                        preFaultHandle.addFault(messageJson);
                        return;
                    }
                    // ST : disappear 设备消除故障处理
                    if (MessageConstants.STYPE_DISAPPEAR.equals(stype)) {
                        preFaultHandle.disappearFault(messageJson);
                    }

                } finally {
                    lock.unlock();
                }

            }

        } catch (InterruptedException e) {
            //
        }


    }

    //CHECKSTYLE:OFF

    /**
     * 统计消息处理
     */
    private void trMessageHandle(JSONObject messageJson, String stype) {
        String elevatorCode = messageJson.getString("elevatorCode");

        if (!StringUtils.hasText(elevatorCode) || !StringUtils.hasText(elevatorCode.trim())) {
            return;
        } else {
            elevatorCode = elevatorCode.trim().replaceAll("\r|\n", "");
        }

        try {
            // ST: update_log 统计消息处理
            if (MessageConstants.STYPE_UPDATE_LOG.equals(stype)) {
                // 获取钢丝绳折弯系数
                TblElevator elevator = elevatorService.getByElevatorCode(elevatorCode);
                if (elevator == null) {
                    return;
                }
                // Dt,Dp1,Dp2 ,Nps,Npr,Ndaj,angle,angle_Type
                double a = elevator.getIDt().doubleValue() / (elevator.getIDt1().doubleValue()
                        + elevator.getIDt2().doubleValue()) * 2;

                double kp = a * a * a * a;
                double nequivp = kp * (elevator.getINps().doubleValue() + 4 * elevator.getINpr().doubleValue());
                Map<String, Object> map = new HashMap<>();
                map.put("type", elevator.getIAngleType());
                map.put("typeValue", elevator.getIAngle());

                double nequivt = 5;
                double nequiv = nequivp + nequivt;
                // 运行次数作为变量
                Double finalValuedou = nequiv * elevator.getDcNdaj();

                // 计算钢丝绳折弯次数
                // 配置了钢丝绳折弯系数
                // 钢丝绳折弯次数 = 钢丝绳折弯系数 * 往返次数
                messageJson.put("backAndForthCount", messageJson.getInteger("bend_count"));
                messageJson.put("bend_count", finalValuedou.intValue() * messageJson.getInteger("bend_count"));

                // 修改电梯统计数据
                elevatorService.updateStatisticalInformationByElevatorCode(messageJson, elevatorCode);

                try {
                    //添加|更新今日终端通讯质量
                    //最小延迟
                    Integer ping_min = null == messageJson.getInteger("ping_min") ? 0
                            : messageJson.getInteger("ping_min");
                    //最大延迟
                    Integer ping_max = null == messageJson.getInteger("ping_max") ? 0
                            : messageJson.getInteger("ping_max");
                    //平均延迟
                    Integer ping_avg = null == messageJson.getInteger("ping_avg") ? 0
                            : messageJson.getInteger("ping_avg");
                    //丢包数量（每十次）
                    Double ping_loss = null == messageJson.getInteger("ping_loss") ? 0
                            : messageJson.getInteger("ping_loss") * 0.1;

                    //获取今日通讯质量
                    DevicePingQuality pingQuality =
                            mongoTemplateUtil.getPingQualityByElevatorCode(elevatorCode, DateUtil.today());

                    //添加|更新数据
                    if (pingQuality != null) {

                        Integer pingMax = pingQuality.getPingMax();
                        Integer pingMin = pingQuality.getPingMin();
                        Integer pingAvg = pingQuality.getPingAvg();
                        Double pingLoss = pingQuality.getPingLoss();

                        //更新当前通讯质量
                        pingQuality.setPingMax(ping_max);
                        pingQuality.setPingMin(ping_min);
                        pingQuality.setPingAvg(ping_avg);
                        pingQuality.setPingLoss(ping_loss);

                        //更新今日通讯质量
                        if (ping_max > pingMax) {
                            pingQuality.setTodayPingMax(ping_max);
                        }
                        if (ping_min < pingMin) {
                            pingQuality.setTodayPingMin(ping_min);
                        }
                        pingQuality.setTodayPingAvg((pingAvg + ping_avg) * 0.5);
                        pingQuality.setTodayPingLoss((pingLoss + ping_loss) * 0.5);
                        pingQuality.setUpdateTime(new Date());

                        mongoTemplateUtil.updateById(pingQuality);

                    } else {

                        pingQuality = DevicePingQuality.builder().id(IdUtil.getSnowflakeNextIdStr())
                                .elevatorCode(elevatorCode)
                                .pingMax(ping_max).todayPingMax(ping_max).pingMin(ping_min).todayPingMin(ping_min)
                                .pingAvg(ping_avg).todayPingAvg(Double.valueOf(ping_avg))
                                .pingLoss(ping_loss).todayPingLoss(ping_loss)
                                .updateTime(new Date()).updateDate(DateUtil.today()).build();

                        mongoTemplateUtil.insert(pingQuality);
                    }
                    //log.info("更新电梯通讯质量成功，message:{}", messageJson);
                } catch (Exception e) {
                    //log.error("更新电梯通讯质量失败，error:{}", ExceptionUtils.getStackTrace(e));
                }

            }

            // ST: update_log 统计消息处理
            if (MessageConstants.STYPE_UPDATE_LOG_FRONT.equals(stype)) {

                Calendar nowTime = Calendar.getInstance();
                nowTime.add(Calendar.SECOND, -messageJson.getInteger("run_time"));
                String afterTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowTime.getTime());
                messageJson.put("run_time", afterTime);

                Integer runCount = messageJson.getInteger("run_count");
                if (runCount == 0) {
                    messageJson.remove("run_count");
                }
                // 修改电梯统计数据
                elevatorService.updateStatisticalInformationByElevatorCode2(messageJson, elevatorCode);
            }
        } catch (Exception e) {
            log.error("--- 更新统计数据失败,message:{},errpr:{}", messageJson, ExceptionUtils.getStackTrace(e));
        }

    }   //CHECKSTYLE:ON


    /**
     * 统计消息处理 1.0.0E协议
     */
    private void trMessageHandleV100E(JSONObject messageJson, String stype) {

        String elevatorCode = messageJson.getString("elevatorCode");
        if (!StringUtils.hasText(elevatorCode) || !StringUtils.hasText(elevatorCode.trim())) {
            return;
        } else {
            elevatorCode = elevatorCode.trim().replaceAll("\r|\n", "");
        }

        try {
            // ST: update_log 统计消息处理
            if (MessageConstants.STYPE_UPDATE_LOG.equals(stype)) {

                // 修改电梯统计数据
                elevatorService.updateStatisticalInformationByElevatorCode3(messageJson, elevatorCode);

            }
        } catch (Exception e) {
            log.error("--- 更新统计数据失败,message:{},errpr:{}", messageJson, e.getMessage());
        }

    }


    //CHECKSTYLE:OFF

    /**
     * 升级文件消息处理
     */
    private void updateMessageHandle(JSONObject messageJson) {
        String fileName = (String) messageJson.get("fileName");
        String returnMsg = null;
        // TODO updateType落库
//        if ("master".equals("updateType")) {
//            if (Constants.STYPE_READY.equals(stype)) {
//                // 升级准备
//                // 客户端请求升级文件，版本号不为空
////                updateStatus = (String) messageJson.get(Constants.STATUS);
//            } else if (Constants.STYPE_FILE_DETAIL.equals(stype)) {
//                // 查询升级文件详情
//                // 客户端请求升级文件，版本号不为空
//
//                if (null == fileName) {
//                    String version = (String) messageJson.get(Constants.VERSION);
//                    // 依据版本号获取升级文件处理器
//                    fileFentchHandler = new FileFentchHandler(version, elevatorCode, 1);
//                } else {
//                    fileFentchHandler = new FileFentchHandler(fileName, elevatorCode, 3);
//                }
//
//                returnMsg = fileFentchHandler.getFileDetail();
//            } else if (Constants.STYPE_DOWNLOAD.equals(stype)) {
//                // 下载升级文件详情
//                if (fileFentchHandler == null || !fileFentchHandler.isNeedUpdate()) {
//                    // 找不到升级文件
// returnMsg = "{\"type\":\"Update\",\"stype\":\"download\",\"error\":\"no need update or has no update file!\"}";
//                } else {
//                    // 可以向客户端传输升级文件
//                    if ("OK".equals(String.valueOf(messageJson.get(Constants.STATUS)))) {
//                        if (!fileFentchHandler.isEnd()) {
//                            returnMsg = fileFentchHandler.handler();
//                        }
//                    } else if ("FAIL".equals(String.valueOf(messageJson.get(Constants.STATUS)))) {
//                        if (!fileFentchHandler.isEnd()) {
//                            returnMsg = fileFentchHandler.handlerFail();
//                        }
//                    } else {
//                        returnMsg = "{\"type\":\"Update\",\"stype\":\"download\",\"error\":\" return OK or FAIL !\"}";
//                    }
//                }
//            }
//        }

        // TODO updateType落库
//        if ("slaver".equals("updateType")) {
//            if (Constants.STYPE_READY.equals(stype)) {// 升级准备
//                // 客户端请求升级文件，版本号不为空 TODO updateStatus落库
////                updateStatus = (String) messageJson.get(Constants.STATUS);
//            } else if (Constants.STYPE_FILE_DETAIL.equals(stype)) {// 查询升级文件详情
//                // 客户端请求升级文件，版本号不为空
//                if (null == fileName) {
//                    String version = (String) messageJson.get(Constants.VERSION);
//                    // 依据版本号获取升级文件处理器
//                    fileFentchHandler = new FileFentchHandler(version, elevatorCode, 2);
//                } else {
//                    fileFentchHandler = new FileFentchHandler(fileName, elevatorCode, 4);
//                }
//
//                returnMsg = fileFentchHandler.getFileDetail();
//
//            } else if (Constants.STYPE_DOWNLOAD.equals(stype)) {
//                // 下载升级文件详情
//                if (fileFentchHandler == null || !fileFentchHandler.isNeedUpdate()) {
//                    // 找不到升级文件
// returnMsg = "{\"type\":\"Update\",\"stype\":\"download\",\"error\":\"no need update or has no update file!\"}";
//                } else {
//                    // 可以向客户端传输升级文件
//                    if ("OK".equals(String.valueOf(messageJson.get(Constants.STATUS)))) {
//                        if (!fileFentchHandler.isEnd()) {
//                            returnMsg = fileFentchHandler.handler();
//                        }
//                    } else if ("FAIL".equals(String.valueOf(messageJson.get(Constants.STATUS)))) {
//                        if (!fileFentchHandler.isEnd()) {
//                            returnMsg = fileFentchHandler.handlerFail();
//                        }
//                    } else {
//                        returnMsg = "{\"type\":\"Update\",\"stype\":\"download\",\"error\":\" return OK or FAIL !\"}";
//                    }
//                }
//            }
//        }

//        CommandIssuedHandle.sendMessage(returnMsg, elevatorCode, sensorType);

    }   //CHECKSTYLE:ON


    //CHECKSTYLE:OFF

    /**
     * confirm消息处理
     */
    private void confirmMessageHandle(JSONObject messageJson, String stype) {

        String elevatorCode = messageJson.getString("elevatorCode");
        String sensorType = messageJson.getString("sensorType");
        messageJson = (JSONObject) messageJson.get("messageJson");


        switch (stype) {
            case "limit":    //楼层配置返回
                // 获取电梯
                TblElevator elevator = elevatorService.getByElevatorCode(elevatorCode);

                // 楼层设置返回 更新电梯楼层设置状态
                Map<String, Object> map = new LinkedHashMap<>();
                Integer max = (Integer) messageJson.get("max");
                Integer min = (Integer) messageJson.get("min");
                if (max.equals(elevator.getIMaxFloor()) && min.equals(elevator.getIMinFloor())) {
                    map.put("code", elevatorCode);
                    map.put("settingFloorStatus", MessageConstants.FLOOR_STATUS_SUCCESS);
                    elevatorService.updateFloorSettingStatus(map);
                } else {
                    map.put("code", elevatorCode);
                    map.put("settingFloorStatus", MessageConstants.FLOOR_STATUS_ERROR);
                    elevatorService.updateFloorSettingStatus(map);
                }
                //更新传感器配置状态
                updateDeviceParamConfStatus(elevatorCode, sensorType, stype);
                break;
            case "sensors":    //传感器配置返回
            case "humanType":    //人感传感器电平类型返回
            case "Srv":    //Server参数配置返回
            case "DlogSrv":    //Dlog服务器参数配置返回
            case "DlogSW":    //Dlog开关远程配置返回
            case "DoorNum":    //单门/贯通门配置返回
            case "PeopleTrapped":    //困人延时时间上报配置返回
            case "StatData":    //统计数据上报周期配置返回
            case "OverSpeed":    //超速速度配置返回
            case "Mode":    //模式配置返回
            case "BaseFloor":    //基准楼层配置返回
            case "IP":    //终端设备IP配置返回
            case "SensorInvertCfg":    //机房传感器极性配置返回
            case "doorFaultTimeRpt":    //开关门故障时间和阻挡门次数配置返回
            case "ParamPreCfg":    //参数预配置下发返回
                if ("success".equals(messageJson.get("status"))) {
                    //更新传感器配置状态
                    updateDeviceParamConfStatus(elevatorCode, sensorType, stype);
                }
                break;
            case "ReadAllParam":    //终端参数回读
                updateAllDeviceParamConf(elevatorCode, sensorType, messageJson);
                break;
        }

    }        //CHECKSTYLE:OFF

    /**
     * 更新终端参数回读-更新所有传感器配置状态
     *
     * @param elevatorCode
     * @param sensorType
     * @param messageJson
     */
    private void updateAllDeviceParamConf(String elevatorCode, String sensorType, JSONObject messageJson) {
        //获取配置表
        DeviceParamConfDO deviceParamConf = deviceConfDao.getConfByCodeAndType(elevatorCode, sensorType);
        if (deviceParamConf == null) {
            // 获取电梯
            TblElevator elevator = elevatorService.getByElevatorCode(elevatorCode);
            if (elevator == null) {
                return;
            }
            //获取设备表
            TblDevice device = deviceDao.getByElevatorIdAndSensorType(elevator.getVElevatorId(), sensorType);
            if (device == null) {
                return;
            }
            //新建配置表
            deviceParamConf = new DeviceParamConfDO();
            deviceParamConf.setId(IdUtil.getSnowflakeNextId());
            deviceParamConf.setElevatorId(elevator.getVElevatorId());
            deviceParamConf.setElevatorCode(elevatorCode);
            deviceParamConf.setDeviceId(device.getVDeviceId());
            deviceParamConf.setSensorType(sensorType);
        }

        String dlogIP = messageJson.getString("DlogIP");
        String dlogPort = messageJson.getString("DlogPort");
        deviceParamConf.setDlogIpPort(dlogIP + ":" + dlogPort);
        deviceParamConf.setDlogIpPortStatus(2);

        String floor = messageJson.getString("Floor");
        deviceParamConf.setFloor(floor);
        deviceParamConf.setFloorStatus(2);

        Integer doorNum = messageJson.getInteger("DoorNum");
        deviceParamConf.setDoorNum(doorNum);
        deviceParamConf.setDoorNumStatus(2);

        Integer baseFloor = messageJson.getInteger("BaseFloor");
        deviceParamConf.setBaseFloor(baseFloor);
        deviceParamConf.setBaseFloorStatus(2);

        Integer peopleRptTm = messageJson.getInteger("PeopleRptTm");
        deviceParamConf.setPeopleRptTm(peopleRptTm);
        deviceParamConf.setPeopleRptTmStatus(2);

        Integer statData = messageJson.getInteger("StatData");
        deviceParamConf.setStatData(statData);
        deviceParamConf.setStatDataStatus(2);

        String overSpeed = messageJson.getString("OverSpeed");
        deviceParamConf.setOverSpeed(overSpeed);
        deviceParamConf.setOverSpeedStatus(2);

        String sensorCfg = messageJson.getString("SensorCfg");
        if (StringUtils.hasText(sensorCfg)) {
            deviceParamConf.setSensorCfg(Integer.toBinaryString(Integer.parseInt(sensorCfg, 16)));
            deviceParamConf.setSensorCfgStatus(2);
        }


        String sensorInvert = messageJson.getString("SensorInvert");
        if (StringUtils.hasText(sensorInvert)) {
            deviceParamConf.setSensorInvertCfg(Integer.toBinaryString(Integer.parseInt(sensorInvert, 16)));
            deviceParamConf.setSensorInvertCfgStatus(2);
        }

        Integer humanType = messageJson.getInteger("HumanType");
        deviceParamConf.setHumanType(humanType);
        deviceParamConf.setHumanTypeStatus(2);

        Integer mode = messageJson.getInteger("Mode");
        deviceParamConf.setMode(mode);
        deviceParamConf.setModeStatus(2);

        Integer normalDlog = messageJson.getInteger("NormalDlog");
        Integer abortDlog = messageJson.getInteger("AbortDlog");
        Integer aiDlog = messageJson.getInteger("AIDlog");
        deviceParamConf.setNormalDlog(normalDlog);
        deviceParamConf.setAbortDlog(abortDlog);
        deviceParamConf.setAiDlog(aiDlog);
        deviceParamConf.setDlogStatus(2);

        Integer openWaitingTime = messageJson.getInteger("openWatingTm");
        Integer closeWaitingTime = messageJson.getInteger("closeWatingTm");
        Integer closeRepeatCount = messageJson.getInteger("closeRepeatCnt");
        deviceParamConf.setOpenWaitingTime(openWaitingTime);
        deviceParamConf.setCloseWaitingTime(closeWaitingTime);
        deviceParamConf.setCloseRepeatCount(closeRepeatCount);
        deviceParamConf.setDoorFaultStatus(2);

        String devIpType = messageJson.getString("DevIPtype");
        String devIP = messageJson.getString("DevIP");
        String devNetmask = messageJson.getString("DevNetmask");
        String devGateway = messageJson.getString("DevGateway");
        deviceParamConf.setDevIpType(devIpType);
        deviceParamConf.setDevIp(devIP);
        deviceParamConf.setDevNetmask(devNetmask);
        deviceParamConf.setDevGateway(devGateway);
        deviceParamConf.setDevIpStatus(2);

        deviceParamConf.setDeviceConfStatus(2);

        deviceConfDao.updateDeviceConf(deviceParamConf);

    }

    /**
     * 更新传感器配置状态
     *
     * @param elevatorCode
     * @param sensorType
     * @param stype
     */
    private void updateDeviceParamConfStatus(String elevatorCode, String sensorType, String stype) {

        //获取配置表
        DeviceParamConfDO deviceParamConf = deviceConfDao.getConfByCodeAndType(elevatorCode, sensorType);

        if (deviceParamConf == null) {

            //构建请求体，{"etype": "MX201","eid":"MX3502","TY":"Update","ST":"ReadAllParam"}
            var message = Map.of("etype", "MX201",
                    "eid", elevatorCode,
                    "sensorType", sensorType,
                    "TY", "Update",
                    "ST", "ReadAllParam")
                    .toString();

            //下发同步设备配置命令
            sendMessage2Device(elevatorCode, sensorType, message);
            return;
        }

        HashMap<String, Integer> statusMap = new HashMap<>();

        switch (stype) {
            case "limit":    //楼层配置返回
                deviceParamConf.setFloorStatus(2);
                break;
            case "sensors":    //传感器配置返回
                deviceParamConf.setSensorCfgStatus(2);
                break;
            case "humanType":    //人感传感器电平类型返回\
                deviceParamConf.setHumanTypeStatus(2);
                break;
            case "Srv":    //Server参数配置返回
                deviceParamConf.setServerIpPortStatus(2);
                break;
            case "DlogSrv":    //Dlog服务器参数配置返回
                deviceParamConf.setDlogIpPortStatus(2);
                break;
            case "DlogSW":    //Dlog开关远程配置返回
                deviceParamConf.setDlogStatus(2);
                break;
            case "DoorNum":    //单门/贯通门配置返回
                deviceParamConf.setDoorNumStatus(2);
                break;
            case "PeopleTrapped":    //困人延时时间上报配置返回
                deviceParamConf.setPeopleRptTmStatus(2);
                break;
            case "StatData":    //统计数据上报周期配置返回
                deviceParamConf.setStatDataStatus(2);
                break;
            case "OverSpeed":    //超速速度配置返回
                deviceParamConf.setOverSpeedStatus(2);
                break;
            case "Mode":    //模式配置返回
                deviceParamConf.setModeStatus(2);
                break;
            case "BaseFloor":    //基准楼层配置返回
                deviceParamConf.setBaseFloorStatus(2);
                break;
            case "IP":    //设备IP地址配置返回
                deviceParamConf.setDevIpStatus(2);
                break;
            case "SensorInvertCfg":    //机房传感器极性配置返回
                deviceParamConf.setSensorInvertCfgStatus(2);
                break;
            case "doorFaultTimeRpt":    //开关门故障时间和阻挡门次数配置返回
                deviceParamConf.setDoorFaultStatus(2);
                break;
            case "ParamPreCfg":    //参数预配置下发返回
                if (deviceParamConf.getFloorStatus() == 1) {
                    deviceParamConf.setFloorStatus(2);
                }
                if (deviceParamConf.getSensorCfgStatus() == 1) {
                    deviceParamConf.setSensorCfgStatus(2);
                }
                if (deviceParamConf.getHumanTypeStatus() == 1) {
                    deviceParamConf.setHumanTypeStatus(2);
                }
                if (deviceParamConf.getServerIpPortStatus() == 1) {
                    deviceParamConf.setServerIpPortStatus(2);
                }
                if (deviceParamConf.getDlogIpPortStatus() == 1) {
                    deviceParamConf.setDlogIpPortStatus(2);
                }
                if (deviceParamConf.getDlogStatus() == 1) {
                    deviceParamConf.setDlogStatus(2);
                }
                if (deviceParamConf.getDoorNumStatus() == 1) {
                    deviceParamConf.setDoorNumStatus(2);
                }
                if (deviceParamConf.getPeopleRptTmStatus() == 1) {
                    deviceParamConf.setPeopleRptTmStatus(2);
                }
                if (deviceParamConf.getStatDataStatus() == 1) {
                    deviceParamConf.setStatDataStatus(2);
                }
                if (deviceParamConf.getOverSpeedStatus() == 1) {
                    deviceParamConf.setOverSpeedStatus(2);
                }
                if (deviceParamConf.getModeStatus() == 1) {
                    deviceParamConf.setModeStatus(2);
                }
                if (deviceParamConf.getBaseFloorStatus() == 1) {
                    deviceParamConf.setBaseFloorStatus(2);
                }
                if (deviceParamConf.getDevIpStatus() == 1) {
                    deviceParamConf.setDevIpStatus(2);
                }
                if (deviceParamConf.getSensorInvertCfgStatus() == 1) {
                    deviceParamConf.setSensorInvertCfgStatus(2);
                }
                //预参数配置 开关门故障时间和阻挡门次数配置 固件暂时不支持
                /*if (deviceParamConf.getDoorFaultStatus() == 1) {
                    deviceParamConf.setDoorFaultStatus(2);
                }*/
                break;
        }

        //获取所有配置状态
        statusMap.put("limit", deviceParamConf.getFloorStatus());
        statusMap.put("sensors", deviceParamConf.getSensorCfgStatus());
        statusMap.put("humanType", deviceParamConf.getHumanTypeStatus());
        statusMap.put("Srv", deviceParamConf.getServerIpPortStatus());
        statusMap.put("DlogSrv", deviceParamConf.getDlogIpPortStatus());
        statusMap.put("DlogSW", deviceParamConf.getDlogStatus());
        statusMap.put("DoorNum", deviceParamConf.getDoorNumStatus());
        statusMap.put("PeopleTrapped", deviceParamConf.getPeopleRptTmStatus());
        statusMap.put("StatData", deviceParamConf.getStatDataStatus());
        statusMap.put("OverSpeed", deviceParamConf.getOverSpeedStatus());
        statusMap.put("Mode", deviceParamConf.getModeStatus());
        statusMap.put("BaseFloor", deviceParamConf.getBaseFloorStatus());
        statusMap.put("IP", deviceParamConf.getDevIpStatus());
        statusMap.put("SensorInvertCfg", deviceParamConf.getSensorInvertCfgStatus());
        statusMap.put("floorStatus", deviceParamConf.getFloorStatus());
        //预参数配置 开关门故障时间和阻挡门次数配置 固件暂时不支持
        if (!"ParamPreCfg".equals(stype)) {
            statusMap.put("doorFaultTimeRpt", deviceParamConf.getDoorFaultStatus());
        }

        //配置状态
        statusMap.remove(stype);
        if (statusMap.values().contains(1)) {
            deviceParamConf.setDeviceConfStatus(1);
        } else {

            deviceParamConf.setDeviceConfStatus(2);

            //构建请求体
            var message = JSON.toJSONString(Map.of("eid", elevatorCode,
                    "sensorType", sensorType,
                    "TY", "reboot"));

            //设备重启-更新配置
            sendMessage2Device(elevatorCode, sensorType, message);

        }

        //更新状态
        deviceConfDao.updateDeviceConfStatus(deviceParamConf);

        //更新电梯表配置状态
        //设备配置状态 0：未配置 1：已下发 2：已配置
        Integer deviceConfStatus = deviceParamConf.getDeviceConfStatus();
        if ("SINGLEBOX".equals(sensorType) || deviceConfStatus == 1) {
            elevatorService.updateDeviceConfStatusByCode(elevatorCode, deviceConfStatus);

        } else {
            Integer confStatus = deviceConfDao.getConfStatusByCodeAndSensorType(elevatorCode,
                    "CarRoof".equals(sensorType) ? "MotorRoom" : sensorType);

            if (confStatus != null && confStatus == 1) {
                elevatorService.updateDeviceConfStatusByCode(elevatorCode, 1);
            } else {
                elevatorService.updateDeviceConfStatusByCode(elevatorCode, 2);
            }
        }

    }

    /**
     * 下发设备配置同步命令
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   传感器类型
     */
    private void sendMessage2Device(String elevatorCode, String sensorType, String message) {

        String key = "DEVICE:STATUS:" + elevatorCode + ":" + sensorType;
        Map<Object, Object> deviceStatus = redisTemplate.opsForHash().entries(key);
        String nodeIp = String.valueOf(deviceStatus.get("server_ip"));
        String nodePort = String.valueOf(deviceStatus.get("server_port"));

        // 请求路径：
        String url = String.format("http://%s:%d/cube/sendMessage", nodeIp, Integer.valueOf(nodePort));

        // 发出一个post请求
        try {
            String s = HttpUtil.post(url, message, 6000);
            log.info("[{}] ---指令下发成功，下发地址：[{}]，下发消息：[{}]，返回信息：[{}]\n", TimeUtils.nowTime(), url, message, s);
        } catch (RestClientException e) {
            log.info("接口调用失败,入参为{}, error:{}", message, ExceptionUtils.getStackTrace(e));
        }

    }

    /**
     * 非平层停梯识别到有人
     *
     * @param faultId
     */
    public void faultConfirmBy6(String faultId) {
        tblFaultService.faultConfirmBy6(faultId);
    }

    /**
     * 人流统计
     *
     * @param message
     */
    public void peopleFlowStatistics(String message) {

        if (!StringUtils.hasText(message)) {
            return;
        }

        PeopleFlowStatisticsMessage messageData = JSON.parseObject(message, PeopleFlowStatisticsMessage.class);

        //主键id
        long snowflakeNextId = IdUtil.getSnowflakeNextId();
        messageData.setId(snowflakeNextId);

        //落表
        peopleFlowStatisticsService.insert(messageData);

        //抓取图片
        var entity = CamareMediaDownloadRequestDTO.builder()
                .elevatorCode(messageData.getElevatorCode())
                .collectTime(DateUtil.formatDateTime(messageData.getTriggerTime()))
                .floor(String.valueOf(messageData.getFloor())).taskType(CameraTaskTypeEnum.PEOPLE_FLOW_STATISTICS)
                .taskCustomId(String.valueOf(snowflakeNextId)).mediaType(CameraMediaTypeEnum.JPG).build();

        try {
            remoteHikEzvizClient.downloadCameraFileByElevatorCode(entity);

            //添加图像识别记录
            var cameraImageIdentifyEntity =
                    new TblCameraImageIdentifyEntity(String.valueOf(snowflakeNextId));

            cameraImageIdentifyEntity.setFloor(String.valueOf(messageData.getFloor()))
                    .setElevatorCode(messageData.getElevatorCode())
                    .setCollectTime(DateUtil.formatDateTime(messageData.getTriggerTime())).setIdentifyType(4);

            peopleFlowStatisticsService.insertCameraImageIdentify(cameraImageIdentifyEntity);

        } catch (Exception e) {
            log.error("人流统计失败，message:{} ; error:{}", message, ExceptionUtils.getStackTrace(e));
        }


    }
}
