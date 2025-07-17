package com.shmashine.sender.platform.city.shanghai;

import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblFaultSendLog;
import com.shmashine.common.message.FaultMessage;
import com.shmashine.common.message.MessageData;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.sender.entity.TblHaierLiftInfo;
import com.shmashine.sender.message.handle.FaultHandle;
import com.shmashine.sender.message.handle.MonitorHandle;
import com.shmashine.sender.message.handle.TrappedHandle;
import com.shmashine.sender.message.send.FaultSend;
import com.shmashine.sender.message.send.MonitorSend;
import com.shmashine.sender.message.send.TrappedSend;
import com.shmashine.sender.server.elevator.BizElevatorService;
import com.shmashine.sender.server.fault.TblFaultMappingService;
import com.shmashine.sender.server.fault.TblFaultSendLogService;

/**
 * 海尔——松江电信对接
 */
@Component
@EnableScheduling
public class DianXinSender implements FaultSend, MonitorSend, TrappedSend {

    private static Logger log = LoggerFactory.getLogger("sonjiang_dianxinlogger");

    private static final String PT_CODE = "sonjiang_dianxin";

    //梯信息缓存
    private static final String HAIERCAMERA_LIFTINFO_KEY = "haier:camera:liftInfo";

    /**
     * 故障告警接口url
     */
    private static final String DEFAULT_ALARM_URL = "/hjdata-server/fault/postLiftFault";

    /**
     * 电梯事件数据url
     */
    private static final String DEFAULT_EVENT_URL = "/hjdata-server/event/postEventFault";

    /**
     * 实时数据接口url
     */
    private static final String DEFAULT_REAL_DATA_URL = "/hjdata-server/realtime/postLiftMessage";

    //密钥
    private static final String TAG_SALT = "awozcaldf";
    private static final String KEY_AES = "AES";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    BizElevatorService bizElevatorService;
    @Autowired
    TblFaultMappingService tblFaultMappingService;
    @Autowired
    TblFaultSendLogService tblFaultSendLogService;

    @Autowired
    SonJiangHttpUtil sonJiangHttpUtil;

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
        try {
            String body;
            TblElevator elevator = bizElevatorService.getByElevatorCode(messageData.getElevatorCode());
            if (elevator.getIElevatorType() == 2) { // 扶梯
                //                body = getEscalatorRealDataInfo(elevator, messageData);
                return;
            } else { // 直梯
                body = getRealDataInfo(elevator, messageData);
            }

            // 电梯注册码
            String registerNumber = StringUtils.isNotBlank(elevator.getVEquipmentCode())
                    ? elevator.getVEquipmentCode() : elevator.getVLeaveFactoryNumber();

            sonJiangHttpUtil.send(registerNumber, "monitor", DEFAULT_REAL_DATA_URL, body);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 推送故障
     *
     * @param faultMessage 故障消息
     */
    @Override
    public void handleFault(FaultMessage faultMessage) {

        if (!"add".equals(faultMessage.getST())) {
            // 无效报文落日志
            log.info("disappear no send fault : " + faultMessage);
            return;
        }

        if (null == faultMessage || StringUtils.isBlank(faultMessage.getFault_type())) {
            return;
        }
        postFaultData(faultMessage);
    }

    /**
     * 推送困人故障
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

            String url;
            String topic;
            if ("37".equals(faultMessage.getFault_type())) {
                body = getEventInfo(elevator, faultMessage);
                url = DEFAULT_EVENT_URL;
                topic = "event";
            } else {
                body = getAlarmsInfo(elevator, faultMessage);
                url = DEFAULT_ALARM_URL;
                topic = "fault";
            }

            if (StringUtils.isNotBlank(body)) {
                // 电梯注册码
                String registerNumber = elevator.getVEquipmentCode();
                responseMsg = sonJiangHttpUtil.send(registerNumber, topic, url, body);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        // 记录到推送日志
        try {
            saveFaultLog(faultMessage, body, responseMsg);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
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
        failureSendLog.setPtCodes("sonjiang_dianxin");
        failureSendLog.setLogType("2");
        failureSendLog.setCreateTime(new Date());
        tblFaultSendLogService.insert(failureSendLog);
    }

    /**
     * 实时数据解析
     */
    private String getRealDataInfo(TblElevator elevator, MessageData messageData) {

        //获取电梯注册码
        String equipmentCode = elevator.getVEquipmentCode();
        //缓存的电梯信息
        TblHaierLiftInfo liftInfo = getLiftInfoCache(equipmentCode);

        HashMap<String, String> data = new HashMap<>();

        MonitorMessage monitor = messageData.getMonitorMessage();

        //轿厢运行状态（0：停止，1：上行，2：下行）
        data.put("runingStatus", String.valueOf(monitor.getDirection()));
        //轿厢运行速度(单位是 m/s)
        data.put("speed", String.valueOf(monitor.getSpeed()));
        //电梯额定速度
        data.put("liftSpeed", liftInfo.getLiftSpeed());
        //当前楼层
        data.put("floor", monitor.getFloor());
        //如果有双门(1:前门，2：后门)
        data.put("whichDoor", "1");
        //开关门状态(0:关门，1：开门)
        data.put("doorStatus", String.valueOf(null != monitor.getDroopClose() && 1 == monitor.getDroopClose() ? 0 : 1));
        //轿厢是否有人(0:无人，1：有人)
        data.put("isPerson", String.valueOf(monitor.getHasPeople()));
        //轿厢内温度
        data.put("temperature", String.valueOf(monitor.getTemperature()));
        //电梯累计运行时间(单位是s)
        Date startRunningTime = (Date) elevator.getDtInstallTime();
        if (null != startRunningTime) {
            long runTime = System.currentTimeMillis() - startRunningTime.getTime();
            runTime = runTime / 1000;
            data.put("timeTotal", String.valueOf(runTime));
        }
        // 累计运行次数
        data.put("runTotal", String.valueOf(elevator.getBiRunCount()));
        // 累计开关门次数
        data.put("openTotal", String.valueOf(elevator.getBiDoorCount()));
        // 钢丝绳（带）折弯次数
        data.put("bendTotal", String.valueOf(elevator.getBiBendCount()));
        // (扶梯/垂直梯)设备累计运行距离  单位米
        data.put("distanceTotal", String.valueOf(elevator.getBiRunDistanceCount()));

        //制动器：tight(制动器抱闸) / pine(制动器松闸)
        //        data.put("brakeHold",null);
        //轿门机：run(轿门机动作) / stop(轿门机停止)
        //        data.put("doormachine",null);
        //        //电梯锁：Y(电梯锁梯)   / N(电梯未锁)
        //        data.put("liftLock",null);

        //电源模式：normal(正常供电) /battery(电池)
        data.put("electric", monitor.getPowerStatus() == 0 ? "normal" : "battery");
        //检修：Y(是)   / N(否)
        data.put("overhaul", monitor.getModeStatus() == 0 ? "N" : "Y");
        //安全回路：normal(安全回路正常) break(安全回路断开)
        data.put("safetyLoop", monitor.getSafeLoop() == 1 ? "break" : "normal");

        HashMap<String, Object> realtime = new HashMap<>();
        realtime.put("data", data);
        realtime.put("time", String.valueOf(System.currentTimeMillis() / 1000));
        String remark = encrypt(data.toString());
        realtime.put("remark", remark);

        HashMap<String, Object> map = new HashMap<>();
        //实时数据集和数据摘要信息
        map.put("realtime", realtime);
        //项目ID
        map.put("projectId", liftInfo.getProjectId());
        //项目name
        map.put("projectName", liftInfo.getProjectName());
        //电梯地址
        map.put("address", liftInfo.getAddress());
        //物理设备设备编号
        map.put("deviceNo", liftInfo.getDeviceNo());
        //楼栋号
        map.put("buildNo", liftInfo.getBuildingNum());
        //电梯位置码
        map.put("locationeCode", liftInfo.getPosition());
        //电梯编号
        map.put("liftNo", liftInfo.getLiftNo());
        //数据上报时间戳
        map.put("timeStamp", String.valueOf(System.currentTimeMillis()));

        String jsonStr = JSON.toJSONString(map);
        return jsonStr;
    }

    /**
     * 故障数据解析
     */
    // CHECKSTYLE:OFF
    private String getAlarmsInfo(TblElevator elevator, FaultMessage faultMessage) {

        if (StringUtils.isBlank(elevator.getVEquipmentCode())) {
            return null;
        }
        HashMap<String, String> data = new HashMap<>();

        //停梯关人 平层困人（7）
        data.put("F9999", "N");
        //停梯长时间不关门 关门故障（12）
        data.put("F9998", "N");
        //上行开门走车 开门走车（5）
        data.put("F9997", "N");
        //下行开门走车 开门走车（5）
        data.put("F9996", "N");
        //长时间关人
        data.put("F9995", "N");
        //非平层停梯关人 非平层困人（8）
        data.put("F9994", "N");
        //紧急呼叫 用户报警（21）
        data.put("F9993", "N");
        //冲顶 冲顶（1）
        data.put("F9992", "N");
        //蹲底 蹲底（2）
        data.put("F9991", "N");
        //运行中门开 运行中开门（4）
        data.put("F9990", "N");
        //运行中门关
        data.put("F9989", "N");
        //撞击故障
        data.put("F9988", "N");
        switch (faultMessage.getFault_type()) {
            case "7":
                data.put("F9999", "Y");
                break;
            case "12":
                data.put("F9998", "Y");
                break;
            case "5":
                MonitorMessage monitorMessage = faultMessage.getMonitorMessage();
                Integer direction = monitorMessage.getDirection();
                if (direction == 1) { //上行
                    data.put("F9997", "Y");
                }
                if (direction == 2) { //下行
                    data.put("F9996", "Y");
                }
                break;
            case "8":
                data.put("F9994", "Y");
                break;
            case "21":
                data.put("F9993", "Y");
                break;
            case "1":
                data.put("F9992", "Y");
                break;
            case "2":
                data.put("F9991", "Y");
                break;
            case "4":
                data.put("F9990", "Y");
                break;
            default:
                break;
        }

        HashMap<String, Object> fault = new HashMap<>();
        fault.put("data", data);
        //数据加密时间
        fault.put("time", String.valueOf(System.currentTimeMillis() / 1000));
        //生成的摘要信息
        String remark = encrypt(data.toString());
        fault.put("remark", remark);
        //故障数据集和数据摘要信息
        HashMap<String, Object> map = new HashMap<>();
        //缓存的电梯信息
        TblHaierLiftInfo liftInfo = getLiftInfoCache(elevator.getVEquipmentCode());
        map.put("fault", fault);
        //项目ID
        map.put("projectId", liftInfo.getProjectId());
        //项目name
        map.put("projectName", liftInfo.getProjectName());
        //电梯地址
        map.put("address", liftInfo.getAddress());
        //物理设备设备编号
        map.put("deviceNo", liftInfo.getDeviceNo());
        //楼栋号
        map.put("buildNo", liftInfo.getBuildingNum());
        //电梯位置码
        map.put("locationeCode", liftInfo.getPosition());
        //故障、事件id
        map.put("messageId", faultMessage.getFaultId());
        //电梯编号
        map.put("liftNo", liftInfo.getLiftNo());
        //数据上报时间戳
        map.put("timeStamp", String.valueOf(System.currentTimeMillis()));

        return JSON.toJSONString(map);
    }
    // CHECKSTYLE:ON

    /**
     * 事件数据解析
     */
    private String getEventInfo(TblElevator elevator, FaultMessage faultMessage) {
        if (StringUtils.isBlank(elevator.getVEquipmentCode())) {
            return null;
        }
        HashMap<String, String> data = new HashMap<>();

        //按键电量过低
        data.put("E9899", "N");
        //烟雾报警
        data.put("E9898", "N");
        //超员
        data.put("E9897", "N");
        //助动车报警
        data.put("E9896", "Y");
        //电梯超速 10%
        data.put("E9895", "N");

        HashMap<String, Object> event = new HashMap<>();
        event.put("data", data);
        //数据加密时间
        event.put("time", String.valueOf(System.currentTimeMillis() / 1000));
        //生成的摘要信息
        String remark = encrypt(data.toString());
        event.put("remark", remark);
        //故障数据集和数据摘要信息
        HashMap<String, Object> map = new HashMap<>();
        //缓存的电梯信息
        TblHaierLiftInfo liftInfo = getLiftInfoCache(elevator.getVEquipmentCode());
        map.put("event", event);
        //项目ID
        map.put("projectId", liftInfo.getProjectId());
        //项目name
        map.put("projectName", liftInfo.getProjectName());
        //电梯地址
        map.put("address", liftInfo.getAddress());
        //物理设备设备编号
        map.put("deviceNo", liftInfo.getDeviceNo());
        //楼栋号
        map.put("buildNo", liftInfo.getBuildingNum());
        //电梯位置码
        map.put("locationeCode", liftInfo.getPosition());
        //故障、事件id
        map.put("messageId", faultMessage.getFaultId());
        //电梯编号
        map.put("liftNo", liftInfo.getLiftNo());
        //数据上报时间戳
        map.put("timeStamp", String.valueOf(System.currentTimeMillis()));
        //添加故障的取证文件
        getEventUrl(faultMessage.getFaultId(), map);

        return JSON.toJSONString(map);
    }

    /**
     * 添加取证文件
     */
    private void getEventUrl(String faultId, HashMap<String, Object> map) {

        HashMap<String, String> eventUrl = tblFaultMappingService.getEventUrl(faultId);

        //发生事件时抓拍到图片URL
        map.put("imgAddress", eventUrl == null ? "null" : eventUrl.get("imgAddress"));
        //发生事件时的视频URL
        map.put("videoAddress", eventUrl == null ? "null" : eventUrl.get("videoAddress"));
    }

    /**
     * redis缓存的海尔推送电梯信息
     */
    private TblHaierLiftInfo getLiftInfoCache(String equipmentCode) {
        HashMap map = (HashMap) redisTemplate.opsForHash().get(HAIERCAMERA_LIFTINFO_KEY, equipmentCode);
        if (map != null) {
            TblHaierLiftInfo tblHaierLiftInfo = JSON.parseObject(JSON.toJSONString(map), TblHaierLiftInfo.class);
            return tblHaierLiftInfo;
        } else {
            TblHaierLiftInfo tblHaierLiftInfo = bizElevatorService.getLiftInfoCache(equipmentCode);
            HashMap<String, TblHaierLiftInfo> cacheMap = new HashMap<>();
            cacheMap.put(String.valueOf(tblHaierLiftInfo.getRegistrationCode()), tblHaierLiftInfo);
            //更新缓存
            redisTemplate.opsForHash().putAll(HAIERCAMERA_LIFTINFO_KEY, cacheMap);
            return tblHaierLiftInfo;
        }
    }

    /**
     * AES加密方法
     *
     * @param oldString 将要加密的字符串
     * @return 加密后的字符串
     */
    private static String encrypt(String oldString) {

        if ("".equals(oldString)) {
            return oldString;
        }

        String screctString = "";
        try {
            //对oldString加密
            byte[] inputSalt = Hex.decodeHex(TAG_SALT.toCharArray());

            SecretKeySpec secretKey = new SecretKeySpec(inputSalt, KEY_AES);
            Cipher cipher = Cipher.getInstance(KEY_AES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] newString = cipher.doFinal(oldString.getBytes());

            //oldString加密后的字符
            screctString = Hex.encodeHexString(newString);
        } catch (Exception e) {
            e.printStackTrace();
            screctString = "错误";
        }
        return screctString;
    }

}

