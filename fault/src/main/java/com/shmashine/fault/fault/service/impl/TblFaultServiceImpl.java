package com.shmashine.fault.fault.service.impl;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.entity.TblSenSorFault;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.common.utils.SendMessageUtil;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.fault.elevator.dao.TblElevatorDao;
import com.shmashine.fault.elevator.entity.TblElevator;
import com.shmashine.fault.fault.dao.TblFaultDao;
import com.shmashine.fault.fault.dao.TblFaultDefinitionDao;
import com.shmashine.fault.fault.entity.TblFault;
import com.shmashine.fault.fault.entity.TblFaultDefinition0902;
import com.shmashine.fault.fault.entity.TblFaultTemp;
import com.shmashine.fault.fault.service.TblFaultServiceI;
import com.shmashine.fault.fault.service.TblFaultTempService;
import com.shmashine.fault.file.dao.TblSysFileDao;
import com.shmashine.fault.file.entity.TblSysFile;
import com.shmashine.fault.redis.util.RedisKeyUtils;
import com.shmashine.fault.redis.util.RedisUtils;
import com.shmashine.fault.utils.SensorFaultShieldedUtil;

/**
 * 故障服务实现类
 */
@Service
public class TblFaultServiceImpl implements TblFaultServiceI {

    private static Logger log = LoggerFactory.getLogger("faultLog");

    @Resource(type = TblFaultDao.class)
    private TblFaultDao tblFaultDao;

    @Resource(type = TblElevatorDao.class)
    private TblElevatorDao elevatorDao;

    @Resource(type = TblFaultDefinitionDao.class)
    private TblFaultDefinitionDao faultDefinitionDao;

    @Resource(type = RedisUtils.class)
    private RedisUtils redisUtils;

    @Resource(type = TblSysFileDao.class)
    private TblSysFileDao tblSysFileDao;

    @Resource(type = TblFaultTempService.class)
    private TblFaultTempService faultTempService;

    @Resource
    private SensorFaultShieldedUtil sensorFaultShieldedUtil;


    @Override
    public TblFaultDao getTblFaultDao() {
        return tblFaultDao;
    }

    @Override
    public TblFault getById(String vFaultId) {
        return tblFaultDao.getById(vFaultId);
    }

    @Override
    public TblSenSorFault getSenSorFaultById(String faultId) {
        return tblFaultDao.getSenSorFaultById(faultId);
    }

    @Override
    public List<TblFault> getByEntity(TblFault tblFault) {
        return tblFaultDao.getByEntity(tblFault);
    }

    @Override
    public List<TblFault> listByEntity(TblFault tblFault) {
        return tblFaultDao.listByEntity(tblFault);
    }

    @Override
    public List<TblFault> listByIds(List<String> ids) {
        return tblFaultDao.listByIds(ids);
    }

    @Override
    public int insert(TblFault tblFault) {
        Date date = new Date();
        tblFault.setDtCreateTime(date);
        tblFault.setDtModifyTime(date);
        return tblFaultDao.insert(tblFault);
    }

    @Override
    public int insertBatch(List<TblFault> list) {
        return tblFaultDao.insertBatch(list);
    }

    @Override
    public int update(TblFault tblFault) {
        tblFault.setDtModifyTime(new Date());
        return tblFaultDao.update(tblFault);
    }

    @Override
    public int updateBatch(List<TblFault> list) {
        return tblFaultDao.updateBatch(list);
    }

    @Override
    public int deleteById(String vFaultId) {
        return tblFaultDao.deleteById(vFaultId);
    }

    @Override
    public int deleteByEntity(TblFault tblFault) {
        return tblFaultDao.deleteByEntity(tblFault);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblFaultDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblFaultDao.countAll();
    }

    @Override
    public int countByEntity(TblFault tblFault) {
        return tblFaultDao.countByEntity(tblFault);
    }

    /**
     * 通过电梯编号和故障类型，查找故障中的记录
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     */
    @Override
    public TblFault getInFaultByFaultType(String elevatorCode, String faultType, String faultSecondType) {
        return tblFaultDao.getInFaultByFaultTypeAndSecondType(elevatorCode, faultType, faultSecondType);
    }

    @Override
    public TblFault getInFaultByFaultType(String elevatorCode, String faultType) {
        return tblFaultDao.getInFaultByFaultType(elevatorCode, faultType);
    }

    @Override
    public TblSenSorFault getInSensorFaultByFaultType(String elevatorCode, String faultType) {
        return tblFaultDao.getInSensorFaultByFaultType(elevatorCode, faultType);
    }


    /**
     * 根据故障报文，新增故障记录
     *
     * @param messageJson  故障报文
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     */
    @Override
    public int addFaultByMessage(JSONObject messageJson, MonitorMessage monitorMessage, String elevatorCode,
                                 String faultType, String faultSecondType) {

        String sensorType = messageJson.getString(MessageConstants.SENSOR_TYPE);

        // FaultDefinition替换为新的故障定义
        TblFaultDefinition0902 faultDefinition;
        if (SocketConstants.SENSOR_TYPE_FRONT.equals(sensorType)) {
            faultDefinition = faultDefinitionDao.getByFaultTypeAndSecondType(faultType, faultSecondType);
        } else {
            faultDefinition = faultDefinitionDao.getByFaultType(faultType);
        }

        TblFault tblFault = new TblFault();

        // base64报文数据解析
        if (org.springframework.util.StringUtils.hasText(messageJson.getString("D"))) {
            try {
                //楼层信息
                tblFault.setFloor(Integer.valueOf(monitorMessage.getFloor()));

                Integer status = monitorMessage.getModeStatus();
                if (status != 0) {
                    log.info("非正常模式不生成故障");
                    return -1;
                }

                tblFault.setIModeStatus(status);

            } catch (Exception e) {
                e.printStackTrace();
                log.info("故障：" + messageJson.getString("faultId") + "获取楼层失败");
            }
        }

        TblElevator elevator = elevatorDao.getByElevatorCode(elevatorCode);
        tblFault.setVFaultId(messageJson.getString("faultId"));
        tblFault.setVElevatorId(elevator.getVElevatorId());
        tblFault.setVElevatorCode(elevatorCode);
        tblFault.setDtReportTime(messageJson.getDate("time"));
        tblFault.setDReportDate(messageJson.getDate("time"));

        tblFault.setVAddress(elevator.getVAddress());
        tblFault.setIFaultType(faultType);
        tblFault.setVFaultSecondType(faultSecondType);
        tblFault.setIFaultNum(1);
        tblFault.setVFaultName(messageJson.getString("faultName"));
        tblFault.setILevel(faultDefinition.getLevel());
        tblFault.setILevelName(faultDefinition.getLevelName());
        tblFault.setIStatus(0);
        tblFault.setVEventChannel(faultDefinition.getPlatformType());
        tblFault.setDtCreateTime(new Date());
        // 不文明行为标识
        tblFault.setIUncivilizedBehaviorFlag(faultDefinition.getUncivilizedBehaviorFlag());
        // 服务模式
        /*String statusKey = RedisKeyUtils.getElevatorStatus(elevatorCode);
        String status = redisUtils.hmGet(statusKey, "mode_status");
        if (StringUtils.isEmpty(status)) {
            status = "0";
            redisUtils.hmSet(statusKey, "mode_status", status);
        }*/

        return tblFaultDao.insert(tblFault);
    }

    /**
     * 根据故障报文，新增传感器故障记录
     *
     * @param messageJson  故障报文
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     */
    @Override
    public void addSensorFaultByMessage(JSONObject messageJson, String elevatorCode, String faultType,
                                        TblFaultDefinition0902 faultDefinition) {

        TblElevator elevator = elevatorDao.getByElevatorCode(elevatorCode);

        TblSenSorFault tblFault = new TblSenSorFault();

        // base64报文数据解析
        if (messageJson.getString("D") != null) {
            try {
                MonitorMessage monitorMessage = new MonitorMessage();
                monitorMessage.setFromBase64(messageJson);
                //楼层信息
                tblFault.setFloor(Integer.valueOf(monitorMessage.getFloor()));

            } catch (Exception e) {
                e.printStackTrace();
                log.info("故障：" + messageJson.getString("faultId") + "获取楼层失败");
            }
        }

        tblFault.setVFaultId(messageJson.getString("faultId"));
        tblFault.setVElevatorId(elevator.getVElevatorId());
        tblFault.setVElevatorCode(elevatorCode);
        tblFault.setDtReportTime(messageJson.getDate("time"));
        tblFault.setVAddress(elevator.getVAddress());
        tblFault.setIFaultType(faultType);
        tblFault.setIFaultNum(1);
        tblFault.setVFaultName(messageJson.getString("faultName"));
        tblFault.setILevel(faultDefinition.getLevel());
        tblFault.setILevelName(faultDefinition.getLevelName());
        tblFault.setIStatus(0);
        tblFault.setDtCreateTime(new Date());
        tblFault.setDtModifyTime(new Date());

        tblFaultDao.addSensorFault(tblFault);
    }

    @Override
    public void updateSensorFault(TblSenSorFault fault) {
        tblFaultDao.disappearSensorFault(fault);
    }

    @Override
    public void addDeviceTimeOutFault(String value) {

        String[] split = value.split(":");

        String elevatorCode = split[0];
        String sensorType = split[1];

        tblFaultDao.addDeviceTimeOutFault(SnowFlakeUtils.nextStrId(), elevatorCode, sensorType);

    }

    @Override
    public void checkSensorFaultIsContinue() {

        //获取所有故障中传感器故障
        List<HashMap<String, String>> sensorFaults = tblFaultDao.getAllSensorFaultOnFaulting(null, null);

        //获取今日达三次传感器故障
        List<HashMap<String, String>> todaySensorFaultOnThreeTimes = tblFaultDao.getTodaySensorFaultOnThreeTimes();
        List<String> collect = todaySensorFaultOnThreeTimes.stream()
                .map(it -> it.get("v_elevator_code") + "_" + it.get("i_fault_type"))
                .distinct().collect(Collectors.toList());

        //恢复未达标准故障
        for (HashMap<String, String> sensorFault : sensorFaults) {

            String elevatorCode = sensorFault.get("v_elevator_code");
            String faultType = sensorFault.get("i_fault_type");

            if (!collect.contains(elevatorCode + "_" + faultType)) {

                //未达三次故障恢复故障
                tblFaultDao.disappearSensorFaultByEleCodeAndFaultType(elevatorCode, faultType);

                sensorFaultShieldedUtil.disappearShieldCache(elevatorCode);
            }

        }

    }


    @Override
    public void addEscalatorFaultByMessage(JSONObject messageJson, String elevatorCode, String faultType,
                                           String faultSecondType, TblFaultDefinition0902 faultDefinition) {

        TblElevator elevator = elevatorDao.getByElevatorCode(elevatorCode);

        TblFault tblFault = new TblFault();

        tblFault.setVFaultId(messageJson.getString("faultId"));
        tblFault.setVElevatorId(elevator.getVElevatorId());
        tblFault.setVElevatorCode(elevatorCode);
        tblFault.setDtReportTime(messageJson.getDate("time"));
        tblFault.setDReportDate(messageJson.getDate("time"));

        tblFault.setVAddress(elevator.getVAddress());
        tblFault.setIFaultType(faultType);
        tblFault.setVFaultSecondType(faultSecondType);
        tblFault.setIFaultNum(1);
        tblFault.setVFaultName(messageJson.getString("faultName"));
        tblFault.setILevel(faultDefinition.getLevel());
        tblFault.setILevelName(faultDefinition.getLevelName());
        tblFault.setIStatus(0);
        tblFault.setVEventChannel(faultDefinition.getPlatformType());
        tblFault.setDtCreateTime(new Date());
        // 不文明行为标识
        tblFault.setIUncivilizedBehaviorFlag(faultDefinition.getUncivilizedBehaviorFlag());
        // 服务模式
        String statusKey = RedisKeyUtils.getElevatorStatus(elevatorCode);
        String status = redisUtils.hmGet(statusKey, "mode_status");
        if (StringUtils.isEmpty(status)) {
            status = "0";
            redisUtils.hmSet(statusKey, "mode_status", status);
        }
        tblFault.setIModeStatus(Integer.valueOf(status));

        tblFaultDao.insert(tblFault);
    }

    @Override
    public void faultConfirmBy6(String faultId) {

        //非平层停梯故障
        TblFault tblFault = tblFaultDao.getById(faultId);

        //先判断是否还在故障中
        if (tblFault.getIStatus() == 0) {

            String id = SnowFlakeUtils.nextStrId();
            //故障消息
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");

            String time = simpleDateFormat.format(tblFault.getDtReportTime());

            final Base64.Encoder encoder = Base64.getEncoder();
            byte floor = toBytes(tblFault.getFloor())[0];
            //
            byte[] x = {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, floor, 0, 0, 0, 0, 0};

            //编码
            final String encodedText = encoder.encodeToString(x);

            String message =
                    "{\"elevatorCode\":\"" + tblFault.getVElevatorCode()
                            + "\"," + "\"ST\":\"add\"," + "\"uncivilizedBehaviorFlag\":0,"
                            + "\"D\":\"" + encodedText + "\"," + "\"TY\":\"Fault\","
                            + "\"sensorType\":\"CarRoof\"," + "\"fault_type\":8,"
                            + "\"faultId\":\"" + id + "\"," + "\"time\":\"" + time + "\","
                            + "\"faultName\":\"非平层关人\"}";

            /*待确认故障处理*/
            TblFault inFaultByFaultType = tblFaultDao.getInFaultByFaultType(tblFault.getVElevatorCode(), "8");
            if (inFaultByFaultType != null) {
                // 正式故障表中存在故障(故障已记录且确认过)
                return;
            }

            // 处理临时表：  临时故障表中是否 存在未确认的故障
            TblFaultTemp faultTempInFaultByFaultType =
                    faultTempService.getInFaultByFaultType(tblFault.getVElevatorCode(), "8", null);

            if (faultTempInFaultByFaultType == null) {

                TblFaultTemp faultTemp = new TblFaultTemp();

                faultTemp.setVFaultId(id);
                faultTemp.setVElevatorId(tblFault.getVElevatorId());
                faultTemp.setVElevatorCode(tblFault.getVElevatorCode());
                faultTemp.setDtReportTime(tblFault.getDtReportTime());
                faultTemp.setFloor(tblFault.getFloor());
                faultTemp.setVAddress(tblFault.getVAddress());
                faultTemp.setIFaultType("8");
                faultTemp.setIFaultNum(1);
                faultTemp.setVFaultName("非平层关人");
                faultTemp.setILevel(1);
                faultTemp.setILevelName("严重Lv1");
                faultTemp.setIStatus(0);
                faultTemp.setIUncivilizedBehaviorFlag(0);
                faultTemp.setIFaultMessage(message);
                faultTemp.setDtCreateTime(new Date());
                faultTemp.setIConfirmStatus(0);
                faultTemp.setIModeStatus(tblFault.getIModeStatus());

                faultTempService.insert(faultTemp);

                log.info("非平层停梯识别困人——添加待确认故障成功，tblFault：{}", tblFault);

                // 故障电话、短信通知， 获取监听者账号对应的电话号码
                TblElevator ele = elevatorDao.getByElevatorCode(tblFault.getVElevatorCode());
                // 未安装、停用不推送短信
                if (ele.getIInstallStatus() == 1 || ele.getIInstallStatus() == 3) {
                    sendFaultMessage(faultTemp, ele);
                }

                // 添加取证文件
                try {
                    List<TblSysFile> files = tblSysFileDao.getFilesById(faultId);
                    for (TblSysFile file : files) {
                        file.setVBusinessId(id);
                        file.setVFileId(SnowFlakeUtils.nextStrId());
                        tblSysFileDao.insert(file);
                    }
                } catch (Exception e) {
                    log.info("非平层停梯识别困人——添加故障取证失败，message:{}", e.getMessage());
                }

            } else {
                // 故障次数 + 1
                faultTempInFaultByFaultType.setIFaultNum(faultTempInFaultByFaultType.getIFaultNum() + 1);
                faultTempService.update(faultTempInFaultByFaultType);
            }
        }
    }

    private void sendFaultMessage(TblFaultTemp faultTemp, TblElevator elevator) {

        List<String> tels = faultTempService.getSeatsTel();
        Date reportTime = faultTemp.getDtReportTime();
        String time = TimeUtils.dateFormat(reportTime);
        if (null != tels && tels.size() > 0) {
            for (String tel : tels) {
                if (StringUtils.isNotBlank(tel)) {

                    String type = "非平层";
                    String occurrenceTime = DateUtil.format(DateUtil.parse(time), "yyyy年MM月dd日 HH:mm");
                    SendMessageUtil.sendEntrapMessage(tel, type, elevator.getVillageName(),
                            elevator.getVElevatorName(), occurrenceTime, String.valueOf(faultTemp.getFloor()));

                }
            }
        }
    }

    @Override
    public List<TblFault> getInFaultByFault6(String elevatorCode) {
        return tblFaultDao.getInFaultByFault6(elevatorCode);
    }

    @Override
    public void disappearFaultByMessage(JSONObject messageJson, TblFault fault) {
        // 计算故障持续时间
        var time = messageJson.getDate("time");
        Date dtReportTime = fault.getDtReportTime();
        long startTime = dtReportTime.getTime();
        long duration = time.getTime() - startTime;

        // 改变故障状态为已恢复
        fault.setIStatus(1);
        fault.setDtEndTime(time);
        fault.setIDurationTime(Math.toIntExact(duration));
        fault.setDtModifyTime(time);

        tblFaultDao.update(fault);
    }

    @Override
    public void disappearSensorFaultByMessage(JSONObject messageJson, TblSenSorFault fault) {

        var time = messageJson.getDate("time");

        // 改变故障状态为已恢复
        fault.setIStatus(1);
        fault.setDtEndTime(time);

        tblFaultDao.disappearSensorFault(fault);
    }

    @Override
    public List<TblFault> getInFault(String elevatorCode) {
        return tblFaultDao.getInFault(elevatorCode);
    }

    public static byte[] toBytes(int number) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) number;
        bytes[1] = (byte) (number >> 8);
        bytes[2] = (byte) (number >> 16);
        bytes[3] = (byte) (number >> 24);
        return bytes;
    }

}
