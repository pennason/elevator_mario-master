package com.shmashine.socket.message;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.hutool.core.util.IdUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.MessageUtils;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.socket.elevator.entity.TblRedirectElevatorMapping;
import com.shmashine.socket.elevator.service.TblRedirectElevatorMappingService;
import com.shmashine.socket.kafka.KafkaProducer;
import com.shmashine.socket.kafka.KafkaTopicConstants;
import com.shmashine.socket.message.bean.MessageData;
import com.shmashine.socket.message.bean.ProtocalVersionCache;
import com.shmashine.socket.message.handle.FaultHandle;
import com.shmashine.socket.message.handle.LoginHandle;
import com.shmashine.socket.message.handle.MonitorHandle;
import com.shmashine.socket.message.handle.UpdateHandle;
import com.shmashine.socket.mongo.entity.FaultMessageConfirm;
import com.shmashine.socket.mongo.entity.PushDockingFloorRecord;
import com.shmashine.socket.mongo.utils.MongoTemplateUtil;
import com.shmashine.socket.netty.ChannelManager;
import com.shmashine.socket.redis.utils.RedisUtils;
import com.shmashine.socket.utils.FloorDockingAlgorithm;
import com.shmashine.socket.websocket.WebSocketManager;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;


/**
 * 设备上报消息处理
 *
 * @author little.li
 */
@Slf4j
@Component
public class MessageHandle {

    private static Logger faultMessageLogger = LoggerFactory.getLogger("faultMessageLogger");

    /**
     * 报文格式化异常日志
     */
    private static Logger formatMessageLogger = LoggerFactory.getLogger("formatMessageLogger");

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(64, 256,
            10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "MessageHandle");


    /**
     * 创建一个 Cache 实例
     * 设置写入后10分钟过期
     */
    Cache<String, Boolean> detectedPeopleNumsIsOpenCache = Caffeine.newBuilder()
            .maximumSize(10000) // 设置最大容量
            .expireAfterWrite(10, TimeUnit.HOURS).build();

    @Resource
    private MongoTemplateUtil mongoTemplateUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Resource
    private FloorDockingAlgorithm floorDockingAlgorithm;

    private final KafkaProducer kafkaProducer;

    private final RedisUtils redisUtils;

    private final TblRedirectElevatorMappingService tblRedirectElevatorMappingService;

    private final ElevatorStatusHandle elevatorStatusHandle;

    /**
     * 故障推送处理类
     */
    private static ConcurrentHashMap<String, FaultHandle> faultHandles = new ConcurrentHashMap<>();

    /**
     * 实时推送处理类
     */
    private static ConcurrentHashMap<String, MonitorHandle> monitorHandles = new ConcurrentHashMap<>();

    /**
     * 登录处理类
     */
    private static ConcurrentHashMap<String, LoginHandle> loginHandles = new ConcurrentHashMap<>();

    /**
     * update消息处理类
     */
    private static ConcurrentHashMap<String, UpdateHandle> updateHandles = new ConcurrentHashMap<>();


    @Autowired
    public MessageHandle(
            KafkaProducer kafkaProducer, RedisUtils redisUtils,
            TblRedirectElevatorMappingService tblRedirectElevatorMappingService,
            ElevatorStatusHandle elevatorStatusHandle) {
        this.kafkaProducer = kafkaProducer;
        this.redisUtils = redisUtils;
        this.tblRedirectElevatorMappingService = tblRedirectElevatorMappingService;
        this.elevatorStatusHandle = elevatorStatusHandle;
    }

    /**
     * 故障处理类
     */
    public static void register(FaultHandle faultHandle) {
        if (null != faultHandle && StringUtils.hasText(faultHandle.getProtocalVersion())) {
            faultHandles.put(faultHandle.getProtocalVersion(), faultHandle);
        }
    }

    /**
     * 监控消息处理类
     */
    public static void register(MonitorHandle monitorHandle) {
        if (null != monitorHandle && StringUtils.hasText(monitorHandle.getProtocalVersion())) {
            monitorHandles.put(monitorHandle.getProtocalVersion(), monitorHandle);
        }
    }

    /**
     * 登录消息处理类
     */
    public static void register(LoginHandle loginHandle) {
        if (null != loginHandle && StringUtils.hasText(loginHandle.getProtocalVersion())) {
            loginHandles.put(loginHandle.getProtocalVersion(), loginHandle);
        }
    }

    /**
     * update消息处理类
     */
    public static void register(UpdateHandle updateHandle) {
        if (null != updateHandle && StringUtils.hasText(updateHandle.getProtocalVersion())) {
            updateHandles.put(updateHandle.getProtocalVersion(), updateHandle);
        }
    }


    /**
     * 消息处理
     *
     * @param message    消息内容
     * @param sensorType 设备类型
     * @param channel    设备连接到的channel
     */
    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    public boolean messageHandle(String message, String sensorType, Channel channel) {
        JSONObject messageJson;
        MessageData messageData;
        String type;

        String elevatorCode = ChannelManager.getElevatorCodeByChannel(channel);

        formatMessageLogger.info("{}, 收到设备[{}]消息, message{}", new Date(), elevatorCode, message);

        //过滤nginx探活
        if (!StringUtils.hasText(message) || message.contains("Connection: Keep-Alive")) {
            return true;
        }

        try {
            // 将设备发送的消息转换为统一格式
            messageJson = MessageUtils.formatMessage(message, sensorType);
            // 构建MessageData对象
            messageData = convertMessageData(messageJson, sensorType, channel);
            // 获取上报消息类型
            type = messageData.getTY();

        } catch (Exception e) {
            formatMessageLogger.error("messageHandle format message error ! cube[{}] sensorType[{}] message: {}",
                    ChannelManager.getElevatorCodeByChannel(channel), sensorType, message);
            return true;
        }

        // TY: Login 登录消息处理
        if (MessageConstants.TYPE_LOGIN.equals(type)) {

            Future<?> submit = executorService.submit(() -> loginMessageHandle(messageData, channel));

            try {
                Object isLogin = submit.get();
                return (boolean) isLogin;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return false;

        } else if (!StringUtils.hasText(elevatorCode)) {
            // 设备未登录处理
            return false;
        }
        if (ChannelManager.deviceIsOnline(elevatorCode, sensorType) == 0) {
            return false;
        }

        // TY: System 心跳消息处理
        if (MessageConstants.TYPE_SYSTEM.equals(type)) {

            executorService.submit(() -> systemMessageHandle(messageData));

            return true;
        }

        //  TY: Fault 故障消息处理
        if (MessageConstants.TYPE_FAULT.equals(type) || MessageConstants.TYPE_FAULT_FRONT.equals(type)
                || MessageConstants.TYPE_FAULT_ESCALATOR.equals(type)) {

            //记录设备上报的故障报文
            faultMessageLogger.info("recived CUBE fault:" + message + ",    to json:" + messageJson.toJSONString());

            executorService.submit(() -> faultMessageHandle(messageData));

        }

        //  TY: 传感器故障消息处理
        if (MessageConstants.TYPE_FAULT_SENSOR.equals(type)) {
            //记录设备上报的故障报文
            faultMessageLogger.info("传感器故障：" + message + ",    to json:" + messageJson.toJSONString());

            executorService.submit(() -> sensorFaultMessageHandle(messageData));
        }

        //  TY: M 监控消息处理
        if (MessageConstants.TYPE_M.equals(type)) {

            executorService.submit(() -> {

                formatMessageLogger.info("messageHandle 收到监控信息 info{}", messageData);

                monitorMessageHandle(messageData);

            });
        }

        //  TY: TR debug消息/统计信息处理
        if (MessageConstants.TYPE_TR.equals(type)) {

            executorService.submit(() -> trMessageHandle(messageData));
        }

        // 西子扶梯状态切换消息处理
        if (MessageConstants.TYPE_EVENT_ESCALATOR.equals(type)) {

            executorService.submit(() -> eventMessageHandle(messageData));
        }

        // TY: Update 更新消息处理（ip, freq, floor等）
        if (MessageConstants.TYPE_UPDATE.equals(type)) {

            executorService.submit(() -> updateMessageHandle(messageData));
        }

        //  TY: confirm 设置返回消息处理 --- 推送到fault模块处理
        if (MessageConstants.TYPE_CONFIRM.equals(type)) {

            executorService.submit(() -> confirmMessageHandle(messageData));
        }

        if (MessageConstants.TYPE_DLOG.equals(type) || MessageConstants.TYPE_SELF_CHECK.equals(type)) {
            executorService.submit(() -> redisMessageHandle(messageData));
        }

        // TY: stopFloor 楼层停靠处理
        if (MessageConstants.STOP_FLOOR.equals(type)) {
            executorService.submit(() -> stopFloorMessageHandle(messageData));
        }


        // 推送到http和nezha_web项目
        //JSONObject finalMessageJson = messageJson;
        //executorService.submit(() -> kafkaProducer.sendMessageToKafka(KafkaTopicConstants.HTTP_TOPIC,
        // finalMessageJson.toJSONString()));

        // 为设备生成重定向数据报文
        //executorService.submit(() -> createMsgForRedirectElevator(messageData.getElevatorCode(), messageData));

        return true;
    }


    //////////////////////////////////private method//////////////////////////////////


    /**
     * 为设备生成数据报文
     *
     * @param elevatorCode 电梯编号
     * @param messageData  数据报文
     */
    private void createMsgForRedirectElevator(String elevatorCode, MessageData messageData) {
        try {
            // 查找设备映射关系
            TblRedirectElevatorMapping elevatorMapping =
                    tblRedirectElevatorMappingService.getByElevatorCode(elevatorCode);

            if (null == elevatorMapping) {
                return;
            }
            String redirectCodes = elevatorMapping.getVRedirectCodes();
            // 生成数据
            if (!StringUtils.hasText(redirectCodes)) {
                return;
            }
            for (String badCode : redirectCodes.split(",")) {
                messageData.setElevatorCode(badCode);
                JSONObject jsonObject = messageData.getMessageJson();
                if (jsonObject != null) {
                    jsonObject.put("elevatorCode", badCode);
                }
                handleMsgForRedirectElevator(messageData);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    private void handleMsgForRedirectElevator(MessageData messageData) {

        String type = messageData.getTY();
        String stype = messageData.getST();

        // TY: Login 登录消息处理
        if (MessageConstants.TYPE_LOGIN.equals(type) && MessageConstants.STYPE_LOGIN.equals(stype)) {
            JSONObject messageJson = messageData.getMessageJson();
            String sensorType = messageData.getSensorType();
            String elevatorCode = messageJson.getString(SocketConstants.MESSAGE_EID);
            String sensorIp = messageJson.getString(SocketConstants.SENSOR_IP);
            // 更新数据库和Redis中在线状态
            elevatorStatusHandle.onLineHandle(elevatorCode, sensorType, sensorIp);
        }

        //  TY: Fault 故障消息处理
        if (MessageConstants.TYPE_FAULT.equals(type) || MessageConstants.TYPE_FAULT_FRONT.equals(type)) {
            //记录设备上报的故障报文
            faultMessageHandle(messageData);
        }

        //  TY: M 监控消息处理
        if (MessageConstants.TYPE_M.equals(type)) {
            monitorMessageHandle(messageData);
        }

        //  TY: TR debug消息/统计信息处理
        if (MessageConstants.TYPE_TR.equals(type)) {
            trMessageHandle(messageData);
        }

        // TY: Update 更新消息处理（ip, freq, floor等）
        if (MessageConstants.TYPE_UPDATE.equals(type)) {
            updateMessageHandle(messageData);
        }

        //  TY: confirm 设置返回消息处理 --- 推送到fault模块处理
        if (MessageConstants.TYPE_CONFIRM.equals(type)) {
            confirmMessageHandle(messageData);
        }

        // 推送到http和nezha_web项目
        String message = messageData.getMessageJson().toJSONString();
        kafkaProducer.sendMessageToKafka(KafkaTopicConstants.HTTP_TOPIC, message);
    }

    /**
     * 构建MessageData对象
     *
     * @param messageJson 原始上报消息
     * @param sensorType  设备类型
     * @param channel     channel
     * @return MessageData
     */
    private MessageData convertMessageData(JSONObject messageJson, String sensorType, Channel channel) {
        // 获取电梯编号
        String elevatorCode = ChannelManager.getElevatorCodeByChannel(channel);
        // 获取type、stype
        String type = messageJson.getString(MessageConstants.MESSAGE_TYPE);
        type = type.equals(MessageConstants.TYPE_FAULT_FRONT) ? MessageConstants.TYPE_FAULT : type;

        // 消息中增加电梯编号，设备类型，上报时间
        String time = TimeUtils.nowTime();
        messageJson = appendInfo(messageJson, elevatorCode, sensorType, time);

        // 构建MessageData对象
        MessageData messageData = new MessageData();
        messageData.setElevatorCode(elevatorCode);
        messageData.setSensorType(sensorType);
        messageData.setTY(type);
        String stype = messageJson.getString(MessageConstants.MESSAGE_STYPE);
        messageData.setST(stype);
        messageData.setTime(time);
        messageData.setMessageJson(messageJson);
        var requestId = messageJson.get("requestId") == null ? "" : messageJson.getString("requestId");
        messageData.setRequestId(requestId);
        return messageData;
    }


    /**
     * 报文中添加电梯编号，设备类型，时间
     *
     * @param messageJson  报文
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 处理后的报文
     */
    private JSONObject appendInfo(JSONObject messageJson, String elevatorCode, String sensorType, String time) {
        String message = messageJson.toJSONString();
        // 报文增加电梯编号
        message = MessageUtils.appendElevatorCode(message, elevatorCode);
        // 报文增加设备类型
        message = MessageUtils.appendSensorType(message, sensorType);
        // 报文增加时间
        message = MessageUtils.appendTime(message, time);
        return JSONObject.parseObject(message);

    }


    /**
     * 登录消息处理
     *
     * @param messageData messageData
     * @param channel     channel
     */
    private boolean loginMessageHandle(MessageData messageData, Channel channel) {

        String stype = messageData.getST();

        JSONObject messageJson = messageData.getMessageJson();

        //获取协议版本
        String protocalVersion = messageJson.getString(SocketConstants.PROTOCAL_VERSION);
        protocalVersion = !StringUtils.hasText(protocalVersion) ? "default" : protocalVersion;

        LoginHandle loginHandle = loginHandles.get(protocalVersion);

        try {

            // ST : login 登录消息处理
            if (MessageConstants.STYPE_LOGIN.equals(stype)) {

                // 执行任务
                return loginHandle.loginHandle(messageData, channel);
            }
            // ST : deviceInfo 设备信息消息处理
            if (MessageConstants.STYPE_DEVICE_INFO.equals(stype)) {
                loginHandle.deviceInfoHandle(messageData, channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("loginMessageHandle exception: [{}]", e.getMessage());
            return false;
        }

        return true;
    }


    /**
     * 心跳消息处理
     *
     * @param messageData messageData
     */
    private void systemMessageHandle(MessageData messageData) {
        String stype = messageData.getST();

        try {
            // ST : cping 设备心跳
            if (MessageConstants.STYPE_CPING.equals(stype)) {
                ChannelManager.sendMessageToChannel(messageData.getElevatorCode(), messageData.getSensorType(),
                        SocketConstants.CPING_MESSAGE);
            }
        } catch (Exception e) {
            log.error("systemMessageHandle exception: [{}]", ExceptionUtils.getStackTrace(e));
        }

    }


    /**
     * 故障消息处理
     *
     * @param messageData messageData
     */
    public void faultMessageHandle(MessageData messageData) {
        String stype = messageData.getST();
        JSONObject messageJson = messageData.getMessageJson();
        String elevatorCode = messageData.getElevatorCode();
        String sensorType = messageData.getSensorType();

        //获取协议版本
        String protocalVersion = ProtocalVersionCache.getProtocalVersion(elevatorCode + sensorType);
        protocalVersion = !StringUtils.hasText(protocalVersion) ? "default" : protocalVersion;

        FaultHandle faultHandle = faultHandles.get(protocalVersion);

        try {
            // 获取故障类型
            String faultType = messageJson.getString(MessageConstants.FAULT_TYPE);
            String faultSecondType = messageJson.getString(MessageConstants.FAULT_STYPE);

            // ST : clear 手动清除故障
            if (MessageConstants.STYPE_CLEAR.equals(stype)) {
                faultHandle.cleanFaultHandle(messageJson, faultType, elevatorCode);
                return;
            }

            /*
             * ST : update 设备重启后故障处理
             * 先消除当前电梯的所有故障，然后新建update中的故障类型
             */
            if (MessageConstants.STYPE_UPDATE.equals(stype)) {
                faultHandle.updateFaultHandle(faultType, faultSecondType, elevatorCode, sensorType);
                return;
            }

            /*
             * ST : add/disappear 设备正常上报新增/消除故障报文
             */
            if (MessageConstants.STYPE_ADD.equals(stype) || MessageConstants.STYPE_DISAPPEAR.equals(stype)) {

                String faultId = SnowFlakeUtils.nextStrId();

                RLock lock = redissonClient.getFairLock(RedisConstants.ELEVATOR_FAULT_MESSAGE_MARK_LOCK
                        + elevatorCode + faultType);

                //尝试加锁，最多等待10s，上锁以后10分钟自动解锁
                if (lock.tryLock(10, 600, TimeUnit.SECONDS)) {

                    try {

                        //更新redis中故障状态
                        redisUtils.hmSet(RedisConstants.ELEVATOR_FAULT_TYPE_MARK + elevatorCode, faultType, stype);

                        //添加到队列
                        FaultMessageConfirm faultMessageConfirm = FaultMessageConfirm.builder()
                                .id(faultId).TY(MessageConstants.TYPE_FAULT).ST(stype)
                                .D(messageData.getMessageJson().getString("D"))
                                .elevatorCode(elevatorCode).faultType(faultType).fault_stype(faultSecondType)
                                .sensorType(sensorType).time(TimeUtils.nowTime())
                                .build();

                        mongoTemplateUtil.insert(faultMessageConfirm);

                        // 反馈设备 - 标识已收到故障
                        faultHandle.faultResponse(messageData, sensorType, 1, faultType);

                        // 处理 新增/消除故障
                        String secondType = "";
                        if (SocketConstants.SENSOR_TYPE_FRONT.equals(sensorType)) {
                            secondType = messageJson.getString(MessageConstants.FAULT_STYPE);
                        }
                        faultHandle.process(faultId, messageJson, elevatorCode, sensorType, faultType,
                                secondType, stype);

                    } finally {
                        lock.unlock();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 传感器故障处理
     */
    public void sensorFaultMessageHandle(MessageData messageData) {
        String stype = messageData.getST();
        JSONObject messageJson = messageData.getMessageJson();
        String elevatorCode = messageData.getElevatorCode();
        String sensorType = messageData.getSensorType();

        //获取协议版本
        String protocalVersion = ProtocalVersionCache.getProtocalVersion(elevatorCode + sensorType);
        protocalVersion = !StringUtils.hasText(protocalVersion) ? "default" : protocalVersion;

        FaultHandle faultHandle = faultHandles.get(protocalVersion);

        try {
            // 获取故障类型
            String faultType = messageJson.getString(MessageConstants.FAULT_TYPE);

            /*
             * ST : add/disappear 设备正常上报新增/消除故障报文
             */
            if (MessageConstants.STYPE_ADD.equals(stype) || MessageConstants.STYPE_DISAPPEAR.equals(stype)) {
                // 反馈设备 - 标识已收到故障
                faultHandle.sensorFaultResponse(messageData, sensorType, 1, faultType);
                // 处理 新增/消除故障
                faultHandle.sensorProcess(messageJson, elevatorCode, sensorType, faultType, stype);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监控消息处理
     *
     * @param messageData messageData
     */
    private void monitorMessageHandle(MessageData messageData) {
        String stype = messageData.getST();

        //获取协议版本
        String protocalVersion = ProtocalVersionCache.getProtocalVersion(messageData.getElevatorCode()
                + messageData.getSensorType());

        protocalVersion = !StringUtils.hasText(protocalVersion) ? "default" : protocalVersion;

        MonitorHandle monitorHandle = monitorHandles.get(protocalVersion);

        try {
            // ST : s 监控消息处理
            if (MessageConstants.STYPE_S.equals(stype)) {
                monitorHandle.monitorHandle(messageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("monitorMessageHandle exception: [{}]", e.getMessage());
        }
    }

    /**
     * 西子扶梯模式切换消息处理
     */
    private void eventMessageHandle(MessageData messageData) {

        //获取协议版本
        String protocalVersion = ProtocalVersionCache.getProtocalVersion(messageData.getElevatorCode()
                + messageData.getSensorType());

        FaultHandle faultHandle = faultHandles.get(protocalVersion);

        //反馈设备——已收到事件切换标识
        faultHandle.eventResponse(messageData, 1, messageData.getMessageJson().getString("event_type"));
        //推送到fault模块处理
        kafkaProducer.sendMessageToKafka(KafkaTopicConstants.EVENT_TOPIC, messageData.getMessageJson().toJSONString());

    }


    /**
     * 统计消息处理
     *
     * @param messageData messageData
     */
    private void trMessageHandle(MessageData messageData) {
        JSONObject messageJson = messageData.getMessageJson();
        String elevatorCode = messageData.getElevatorCode();
        String sensorType = messageData.getSensorType();
        String stype = messageData.getST();

        try {
            // ST : debug_info 调试消息处理
            if (MessageConstants.STYPE_DEBUG_INFO.equals(stype)) {
                // 向浏览器发送消息
                WebSocketManager.sendMessageToBrowsers(
                        messageData.getElevatorCode(), SocketConstants.MESSAGE_TYPE_DEBUG,
                        messageData.getSensorType(), messageJson.toJSONString());
            }
            // 统计消息推送到fault模块处理
            if (MessageConstants.STYPE_UPDATE_LOG.equals(stype)) {
                messageJson.put("protocalVersion",
                        ProtocalVersionCache.getProtocalVersion(messageData.getElevatorCode()
                                + messageData.getSensorType()));

                // 推送到fault模块处理
                kafkaProducer.sendMessageToKafka(KafkaTopicConstants.STATISTICS_TOPIC, messageJson.toJSONString());
                // 返回设备已接收数据
                ChannelManager.sendMessageToChannel(elevatorCode, sensorType, MessageConstants.TR_UPDATE_LOG_SUCCESS);
            }
            // 人流量统计
            if (MessageConstants.DETECTED_PEOPLE_NUMS.equals(stype)) {

                //开启配置才推送
                if (getDetectedPeopleNumsIsOpen(elevatorCode)) {
                    // 推送到fault模块处理
                    JSONObject js = new JSONObject();
                    js.put("elevatorCode", elevatorCode);
                    js.put("triggerTime", messageJson.get("time"));
                    js.put("floor", messageJson.get("floor"));
                    js.put("direction", messageJson.get("direction"));

                    kafkaProducer.sendMessageToKafka(
                            KafkaTopicConstants.STATISTICS_PEOPLE_TOPIC,
                            js.toJSONString());
                }

                // 返回设备已接收数据
                ChannelManager.sendMessageToChannel(elevatorCode, sensorType,
                        MessageConstants.DETECTED_PEOPLE_NUMS_SUCCESS);
            }
            // 前装统计消息处理
            if (MessageConstants.STYPE_UPDATE_LOG_FRONT.equals(stype)) {
                // 返回设备已接收数据
                ChannelManager.sendMessageToChannel(elevatorCode, sensorType,
                        MessageConstants.TR_UPDATE_LOG_FRONT_SUCCESS);

                // 推送到fault模块处理
                kafkaProducer.sendMessageToKafka(
                        KafkaTopicConstants.STATISTICS_TOPIC,
                        messageJson.toJSONString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("trMessageHandle exception: [{}]", e.getMessage());
        }
    }

    /**
     * 人流量统计是否开启
     *
     * @param elevatorCode 电梯编号
     */
    private boolean getDetectedPeopleNumsIsOpen(String elevatorCode) {

        //获取缓存
        Boolean result = detectedPeopleNumsIsOpenCache.getIfPresent(elevatorCode);
        if (result != null) {
            return result;
        }

        //读表
        Boolean detectedPeopleNumsIsOpen = elevatorStatusHandle.getDetectedPeopleNumsIsOpen(elevatorCode);
        result = detectedPeopleNumsIsOpen != null ? detectedPeopleNumsIsOpen : false;
        //添加缓存
        detectedPeopleNumsIsOpenCache.put(elevatorCode, result);

        return result;
    }


    /**
     * 更新消息处理
     *
     * @param messageData messageData
     */
    private void updateMessageHandle(MessageData messageData) {
        try {
            JSONObject messageJson = messageData.getMessageJson();
            String elevatorCode = messageData.getElevatorCode();
            String sensorType = messageData.getSensorType();
            String stype = messageData.getST();

            //获取协议版本
            String protocalVersion = ProtocalVersionCache.getProtocalVersion(elevatorCode + sensorType);
            protocalVersion = !StringUtils.hasText(protocalVersion) ? "default" : protocalVersion;

            UpdateHandle updateHandle = updateHandles.get(protocalVersion);

            if (MessageConstants.STYPE_IP.equals(stype)) {
                // ST: ip
                updateHandle.ipHandle(messageJson, elevatorCode, sensorType);
            } else if (MessageConstants.STYPE_FREP.equals(stype)) {
                // ST: freq
                updateHandle.frepHandle(messageJson, elevatorCode, sensorType);
            } else if (MessageConstants.STYPE_LIMIT.equals(stype)) {
                // ST: limit
                updateHandle.limitHandle(messageJson, elevatorCode, sensorType);
            } else {
                // 推送到fault模块处理
                kafkaProducer.sendMessageToKafka(
                        KafkaTopicConstants.FAULT_TOPIC,
                        messageJson.toJSONString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * confirm消息处理
     *
     * @param messageData messageData
     */
    private void confirmMessageHandle(MessageData messageData) {
        try {
            // 推送到fault模块处理
            kafkaProducer.sendMessageToKafka(
                    KafkaTopicConstants.FAULT_TOPIC,
                    JSONObject.toJSONString(messageData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 楼层停靠信息处理
     *
     * @param messageData 终端消息
     */
    private void stopFloorMessageHandle(MessageData messageData) {

        if ("getStopFloor".equals(messageData.getST())) {
            //获取当前楼层
            JSONObject messageJson = messageData.getMessageJson();
            Integer nowFloor = messageJson.getInteger("nowFloor");
            String elevatorCode = messageData.getElevatorCode();

            // 获取当前楼层的预停靠楼层
            Integer dockingFloor = floorDockingAlgorithm.getDockingFloor(elevatorCode);

            //下发预停靠楼层
            var message = JSON.toJSONString(Map.of("etype", "MX201",
                    "eid", elevatorCode,
                    "TY", "stopFloor",
                    "ST", "stopFloorResp",
                    "dockingFloor", dockingFloor));
            ChannelManager.sendMessageToChannel(elevatorCode, messageData.getSensorType(), message);

            // 记录预停靠楼层下发记录
            PushDockingFloorRecord pushDockingFloorRecord = PushDockingFloorRecord.builder()
                    .id(IdUtil.getSnowflakeNextIdStr())
                    .elevatorCode(elevatorCode)
                    .nowFloor(nowFloor)
                    .dockingFloor(dockingFloor)
                    .recordTime(new Date())
                    .build();

            mongoTemplateUtil.insert(pushDockingFloorRecord);
        }
    }

    /**
     * 日志文件或者redis的方式缓存监控告警消息
     */
    private void cacheMessage(MessageData messageData) {
        String elevatorCode = messageData.getElevatorCode();
        String sensorType = messageData.getSensorType();
        String monitorKey = String.format("messageCache:%s:%s", elevatorCode, sensorType);
        redisUtils.lPush(monitorKey, JSONObject.toJSONString(messageData));
        while (redisUtils.size(monitorKey) > 10) {
            redisUtils.rPop(monitorKey);
        }
    }

    private void redisMessageHandle(MessageData messageData) {
        var redisKey = "DEVICE:" + messageData.getTY() + "-" + messageData.getST() + ":"
                + messageData.getElevatorCode() + "-" + messageData.getRequestId();
        redisUtils.setStr(redisKey, JSON.toJSONString(messageData), 10L, TimeUnit.MINUTES);
    }

}
