package com.shmashine.fault.message.handle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.cameratysl.client.RemoteCameraTyslClient;
import com.shmashine.cameratysl.client.dto.requests.FaultForHistoryPhotoVideoRequestDTO;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.DeviceMaintenanceOrder;
import com.shmashine.common.entity.TblResponseXmReport;
import com.shmashine.common.entity.TblSenSorFault;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.common.enums.CameraTaskTypeEnum;
import com.shmashine.common.enums.CameraTypeEnum;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.common.model.Result;
import com.shmashine.common.model.request.ImageHandleRequest;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.AlarmTaskUtils;
import com.shmashine.common.utils.DateUtils;
import com.shmashine.common.utils.SendMessageUtil;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.fault.camera.entity.TblCamera;
import com.shmashine.fault.camera.service.TblCameraServiceI;
import com.shmashine.fault.dal.dao.DeviceMaintenanceOrderDAO;
import com.shmashine.fault.elevator.dao.TblElevatorDao;
import com.shmashine.fault.elevator.entity.TblElevator;
import com.shmashine.fault.elevator.service.TblElevatorService;
import com.shmashine.fault.fault.dao.TblFaultDefinitionDao;
import com.shmashine.fault.fault.entity.TblFault;
import com.shmashine.fault.fault.entity.TblFaultDefinition0902;
import com.shmashine.fault.fault.service.TblFaultDefinitionServiceI;
import com.shmashine.fault.fault.service.TblFaultServiceI;
import com.shmashine.fault.feign.RemoteCameraServer;
import com.shmashine.fault.kafka.KafkaProducer;
import com.shmashine.fault.kafka.KafkaTopicConstants;
import com.shmashine.fault.mongo.entity.FaultMessageConfirm;
import com.shmashine.fault.mongo.utils.MongoTemplateUtil;
import com.shmashine.fault.redis.RedisService;
import com.shmashine.fault.user.entity.TblSysUser;
import com.shmashine.fault.user.service.TblSysUserServiceI;
import com.shmashine.fault.utils.SensorFaultShieldedUtil;
import com.shmashine.hikYunMou.client.RemoteHikCloudClient;
import com.shmashine.hkcamerabyys.client.RemoteHikEzvizClient;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认故障落库处理
 *
 * @author little.li
 */
@Slf4j
@Component
@Primary
public class DefaultFaultHandle implements FaultHandle {

    private static Logger faultMessageLogger = LoggerFactory.getLogger("faultMessageLogger");

    private final TblFaultServiceI faultService;

    private final TblFaultDefinitionServiceI faultDefinitionService;

    private final TblElevatorService elevatorService;

    private final TblCameraServiceI cameraService;

    private final RedisService redisService;

    private final KafkaProducer kafkaProducer;

    private final TblSysUserServiceI userService;

    @Resource(type = TblFaultDefinitionDao.class)
    private TblFaultDefinitionDao faultDefinitionDao;

    @Resource(type = TblElevatorDao.class)
    private TblElevatorDao elevatorDao;

    @Resource
    private DeviceMaintenanceOrderDAO deviceMaintenanceOrder;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(8, 16,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "DefaultFaultHandle");


    @Autowired
    public DefaultFaultHandle(TblFaultServiceI faultService, TblFaultDefinitionServiceI faultDefinitionService,
                              TblElevatorService elevatorService, TblSysUserServiceI userService,
                              TblCameraServiceI cameraService, RedisService redisService, KafkaProducer kafkaProducer) {
        this.faultService = faultService;
        this.faultDefinitionService = faultDefinitionService;
        this.elevatorService = elevatorService;
        this.cameraService = cameraService;
        this.redisService = redisService;
        this.kafkaProducer = kafkaProducer;
        this.userService = userService;
    }

    @Autowired
    private RemoteCameraServer remoteCameraServer;

    @Autowired
    private RemoteHikEzvizClient hikEzvizClient;

    @Resource
    private SensorFaultShieldedUtil sensorFaultShieldedUtil;

    @Resource
    private MongoTemplateUtil mongoTemplateUtil;

    @Resource
    private RemoteHikCloudClient hikCloudClient;

    @Resource
    private RemoteCameraTyslClient tyslCameraClient;

    //CHECKSTYLE:OFF

    /**
     * 新增故障
     */
    @Override
    public void addFault(JSONObject messageJson) {
        String elevatorCode = messageJson.getString("elevatorCode");
        String faultType = messageJson.getString("fault_type");
        String faultSecondType = messageJson.getString("fault_stype");
        String sensorType = messageJson.getString(MessageConstants.SENSOR_TYPE);

        // 获取故障业务id（全系统唯一标识）
        String faultId = messageJson.getString("faultId");

        faultMessageLogger.info("{} --收到故障消息 elevatorCode[{}] , faultType[{}]", TimeUtils.nowTime(),
                elevatorCode, faultType);

        // 判断改故障是否已经记录（幂等判断）
        TblFault fault = faultService.getById(faultId);
        // 未记录情况判断 该电梯是否处于该类故障中
        if (fault == null) {
            if (sensorType.equals(SocketConstants.SENSOR_TYPE_FRONT)) {
                fault = faultService.getInFaultByFaultType(elevatorCode, faultType, faultSecondType);
            } else {
                fault = faultService.getInFaultByFaultType(elevatorCode, faultType);
            }
        }

        // 不存在故障（走新增故障逻辑）
        if (fault == null) {
            // redis中记录电梯为【故障中】
            redisService.updateFaultStatus(elevatorCode, 1);

            // 获取故障类型的详细信息
            TblFaultDefinition0902 faultDefinition;
            if (sensorType.equals(SocketConstants.SENSOR_TYPE_FRONT)) {
                faultDefinition = faultDefinitionService.getByFaultTypeAndSecondType(faultType, faultSecondType);
            } else {
                faultDefinition = faultDefinitionService.getByFaultType(faultType);
            }

            // base64报文数据解析
            MonitorMessage monitorMessage = new MonitorMessage();
            if (org.springframework.util.StringUtils.hasText(messageJson.getString("D"))) {
                try {
                    monitorMessage.setFromBase64(messageJson);
                } catch (Exception e) {
                    log.info("故障：{}，获取运行信息失败，error:{}",
                            messageJson.getString("faultId"), ExceptionUtils.getStackTrace(e));
                }
            }

            // 新增故障记录
            int status = faultService.addFaultByMessage(messageJson, monitorMessage, elevatorCode,
                    faultType, faultSecondType);
            if (status < 0) {
                return;
            }

            // 故障
            if (faultDefinition.getUncivilizedBehaviorFlag() == 0) { //不文明行为标识，0：故障，1：不文明行为

                // 数据库中更新电梯为 【故障中】
                executorService.submit(() -> elevatorService.updateFaultStatus(elevatorCode, 1));

                if (faultDefinition.getSendSMS() == 1) {
                    // 故障电话、短信通知
                    faultNotify(faultId, faultDefinition.getFaultName(), elevatorCode, messageJson.getString("time"),
                            monitorMessage.getFloor(), faultType);
                }
            }

            this.saveFaultHistoryFlie(elevatorCode, faultType, faultId, messageJson.getDate("time"),
                    monitorMessage.getFloor());

            // 屏幕通知困人
            if ("7".equals(faultType) || "8".equals(faultType)) {
                String message = convertTrappedMessage(SocketConstants.SCREEN_TRAPPED_ADD_MESSAGE, elevatorCode);
                kafkaProducer.sendMessageToKafka(KafkaTopicConstants.SOCKET_TOPIC, message);

                //给摄像头推送困人语音播报
                pushCameraVoice(elevatorCode, faultId, faultType);

            }

        } else {
            // 故障次数 + 1
            fault.setIFaultNum(fault.getIFaultNum() + 1);
            faultService.update(fault);
        }

        //移除mongo故障记录
        mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
    }  //CHECKSTYLE:ON

    /**
     * 是否关联传感器屏蔽
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @return 1 屏蔽 0 未屏蔽
     */
    private int checkShielded(String elevatorCode, String faultType) {

        //获取电梯相关屏蔽故障
        boolean shielded = sensorFaultShieldedUtil.checkShielded(elevatorCode, faultType);

        return shielded ? 1 : 0;
    }

    //CHECKSTYLE:OFF

    /**
     * 消除故障
     */
    @Override
    public void disappearFault(JSONObject messageJson) {
        String elevatorCode = messageJson.getString("elevatorCode");
        String faultType = messageJson.getString("fault_type");
        String faultSecondType = messageJson.getString("fault_stype");
        String sensorType = messageJson.getString(MessageConstants.SENSOR_TYPE);
        String faultId = messageJson.getString("faultId");

        // 获取故障中的故障记录
        TblFault fault;

        //若果为非平层停梯故障消除——则需要消除非平层停梯和非平层困人
        if ("6".equals(faultType)) {

            //查找故障中的非平层停梯或非平层困人故障
            List<TblFault> faultList = faultService.getInFaultByFault6(elevatorCode);

            //对故障中的非平层停梯和非平层困人故障进行消除
            if (faultList != null && faultList.size() > 0) {
                for (TblFault tblFault : faultList) {
                    dissapperFault(messageJson, elevatorCode, faultType, tblFault);
                }
                return;
            }

            //是否有add任务未处理
            List<FaultMessageConfirm> faults = mongoTemplateUtil.queryByElevatorAndFault(elevatorCode, faultType,
                    "add", FaultMessageConfirm.class);

            if (faults == null || faults.size() == 0) {
                mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
            }
        }

        if (sensorType.equals(SocketConstants.SENSOR_TYPE_FRONT)) {
            fault = faultService.getInFaultByFaultType(elevatorCode, faultType, faultSecondType);
        } else {
            fault = faultService.getInFaultByFaultType(elevatorCode, faultType);
        }

        // 存在故障中的故障
        if (fault != null) {
            dissapperFault(messageJson, elevatorCode, faultType, fault);
            return;
        }

        //是否有add任务未处理
        List<FaultMessageConfirm> faults = mongoTemplateUtil.queryByElevatorAndFault(elevatorCode, faultType, "add",
                FaultMessageConfirm.class);

        if (faults == null || faults.size() == 0) {
            mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
        }

    }   //CHECKSTYLE:ON

    /**
     * 新增传感器故障
     */
    @Override
    public void addSensorFault(JSONObject messageJson) {

        String elevatorCode = messageJson.getString("elevatorCode");
        String faultType = messageJson.getString("fault_type");
        String faultId = messageJson.getString("faultId");

        // 获取故障定义
        TblFaultDefinition0902 faultDefinition = faultDefinitionDao.getByFaultTypeAndPlatformType(faultType, 6);

        // 判断改故障是否已经记录（幂等判断）
        TblSenSorFault fault = faultService.getSenSorFaultById(faultId);
        // 未记录情况判断 该电梯是否处于该类故障中(当天故障，今天没报则新增)
        if (fault == null) {
            fault = faultService.getInSensorFaultByFaultType(elevatorCode, faultType);
        }

        // 不存在故障（走新增故障逻辑）
        if (fault == null) {

            // 新增传感器故障记录
            faultService.addSensorFaultByMessage(messageJson, elevatorCode, faultType, faultDefinition);

        } else {
            // 故障次数 + 1
            fault.setIFaultNum(fault.getIFaultNum() + 1);
            fault.setIStatus(0);
            fault.setDtModifyTime(new Date());
            faultService.updateSensorFault(fault);

            if (fault.getIFaultNum() > 2) {
                //添加传感器关联故障屏蔽
                log.info("添加传感器关联故障屏蔽，elevatorCode:{}，faultType:{}", elevatorCode, faultType);
                sensorFaultShieldedUtil.addShieldCache(elevatorCode, faultType);

                /*
                生成设备运维单
                备故障到达3次生成运维单 未派单状态 （新的故障类型，未派单、已派单：添加故障类型；待审核、审核未通过、已完成：生成新运维单）
                 */
                //executorService.submit(() -> this.generateDeviceMaintenanceOrder(elevatorCode, faultDefinition));

                //更新电梯安装状态为待运维
                log.info("更新电梯安装状态为待运维elevator：{}", elevatorCode);
                elevatorService.updateInstallStatus(elevatorCode, 3);

            }
        }
    }

    /**
     * 生成设备运维单
     *
     * @param elevatorCode    电梯编号
     * @param faultDefinition 故障定义
     */
    private void generateDeviceMaintenanceOrder(String elevatorCode, TblFaultDefinition0902 faultDefinition) {

        log.info("添加设备运维单,elevatorCode:{},faultName:{}", elevatorCode, faultDefinition.getFaultName());
        //是否存在未完成设备运维单
        List<DeviceMaintenanceOrder> deviceMaintenanceOrders =
                deviceMaintenanceOrder.queryByElevatorAndNotComplete(elevatorCode);

        if (deviceMaintenanceOrders != null && deviceMaintenanceOrders.size() > 0) {

            HashMap<String, Boolean> status = new HashMap<>() {
                {
                    put("contains", false);
                    put("orderStatus", false);
                }
            };

            //设备运维单状态
            for (DeviceMaintenanceOrder order : deviceMaintenanceOrders) {

                if (order.getFaultName().contains(faultDefinition.getFaultName())) {
                    status.put("contains", true);
                }

                if (order.getOrderStatus() > 1) {
                    status.put("orderStatus", true);
                }
            }

            //不存在该故障运维单
            if (!status.get("contains")) {

                //未派单,补充更新故障name
                if (!status.get("orderStatus")) {

                    DeviceMaintenanceOrder maintenanceOrder = deviceMaintenanceOrders.get(0);

                    String faultName = String.format("maintenanceOrder.getFaultName()%s%s%s%s%s",
                            ",", faultDefinition.getFaultName(), "[", DateUtil.now(), "]");

                    maintenanceOrder.setFaultName(faultName);
                    deviceMaintenanceOrder.updateOrderById(maintenanceOrder);

                } else {
                    //生成设备运维单
                    insertDeviceMaintenanceOrder(elevatorCode, faultDefinition);
                }

            }

        } else {
            //生成设备运维单
            insertDeviceMaintenanceOrder(elevatorCode, faultDefinition);
        }
    }

    /**
     * 新增设备运维单
     *
     * @param elevatorCode    电梯编号
     * @param faultDefinition 故障定义
     */
    private void insertDeviceMaintenanceOrder(String elevatorCode, TblFaultDefinition0902 faultDefinition) {
        Date time = new Date();

        //获取设备地址项目信息小区信息
        DeviceMaintenanceOrder elevatorInfo = elevatorDao.getProjectAndVillageByElevatorCode(elevatorCode);

        DeviceMaintenanceOrder maintenanceOrder = DeviceMaintenanceOrder.builder()
                .id(IdUtil.getSnowflakeNextId()).projectId(elevatorInfo.getProjectId())
                .projectName(elevatorInfo.getProjectName())
                .villageId(elevatorInfo.getVillageId()).villageName(elevatorInfo.getVillageName())
                .address(elevatorInfo.getAddress()).elevatorCode(elevatorCode)
                .faultName(faultDefinition.getFaultName() + "[" + DateUtil.now() + "]")
                .orderGenerationTime(time).createTime(time).build();

        deviceMaintenanceOrder.insert(maintenanceOrder);
    }

    /**
     * 消除传感器故障
     */
    @Override
    public void disappearSensorFault(JSONObject messageJson) {

        String elevatorCode = messageJson.getString("elevatorCode");
        String faultType = messageJson.getString("fault_type");

        // 获取故障中的故障记录
        TblSenSorFault fault = faultService.getInSensorFaultByFaultType(elevatorCode, faultType);

        // 存在故障中的故障
        if (fault != null) {

            // 消除故障表中该故障状态为 已恢复
            faultService.disappearSensorFaultByMessage(messageJson, fault);

        }

        //清除传感器关联故障屏蔽
        sensorFaultShieldedUtil.disappearShieldCache(elevatorCode);
    }

    @Transactional(rollbackFor = Exception.class)
    public void dissapperFault(JSONObject messageJson, String elevatorCode, String faultType, TblFault fault) {

        // 消除故障表中该故障状态为 已恢复
        faultService.disappearFaultByMessage(messageJson, fault);

        // 更新电梯表状态：通过判断故障表中当前电梯处于【故障中】的故障
        List<TblFault> faultList = faultService.getInFault(elevatorCode);
        if (CollectionUtils.isEmpty(faultList)) {
            // 数据库中更新电梯为无故障
            elevatorService.updateFaultStatus(elevatorCode, 0);
            // redis中记录电梯为无故障
            redisService.updateFaultStatus(elevatorCode, 0);
        }

        // 屏幕通知困人消除
        if ("7".equals(faultType) || "8".equals(faultType)) {
            String message = convertTrappedMessage(SocketConstants.SCREEN_TRAPPED_DIS_MESSAGE, elevatorCode);
            kafkaProducer.sendMessageToKafka(KafkaTopicConstants.SOCKET_TOPIC, message);
        }

        //移除mongo故障记录
        mongoTemplateUtil.removeById(messageJson.getString("faultId"), FaultMessageConfirm.class);
    }


    //CHECKSTYLE:OFF

    /**
     * 生成故障历史取证文件
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @param faultId      故障id
     * @param occurTime    发生时间
     */
    @Override
    public void saveFaultHistoryFlie(String elevatorCode, String faultType, String faultId,
                                     Date occurTime, String floor) {

        TblCamera camera = cameraService.getByElevatorCode(elevatorCode);
        // 未配置摄像头直接返回
        if (camera == null) {
            log.error("saveFaultHistoryFlie error : no camera with elevatorCode[{}]", elevatorCode);
            return;
        }

        // 摄像头类型 1：海康，2：雄迈
        if (camera.getICameraType() == 2) {
            // 未佩戴口罩 仅保存图片
            if ("39".equals(faultType)) {
                // 图片取证无法使用时间参数，直接获取当前时间点的图片
                realPictureDownloadTask(elevatorCode, faultId);
            } else {  // 其他故障
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(occurTime);
                //困人故障
                if ("7".equals(faultType) || "8".equals(faultType)) {
                    calendar.add(Calendar.SECOND, -60);
                    Date startTime = calendar.getTime();
                    calendar.setTime(occurTime);
                    calendar.add(Calendar.SECOND, +(14 * 60));
                    Date endTime = calendar.getTime();
                    saveHistoryDownloadTaskForXM(elevatorCode, "1", faultId, startTime, endTime);
                } else {
                    calendar.add(Calendar.SECOND, -15);
                    Date startTime = calendar.getTime();
                    calendar.setTime(occurTime);
                    calendar.add(Calendar.SECOND, +45);
                    Date endTime = calendar.getTime();
                    saveHistoryDownloadTaskForXM(elevatorCode, "1", faultId, startTime, endTime);
                }

                //平层困人无需二次取图片
                if ("37".equals(faultType) || "7".equals(faultType)) {
                    return;
                }

                //获取图片
                realPictureDownloadTask(elevatorCode, faultId);
            }
            return;
        }

        // 海康萤石云
        if (camera.getICameraType() == 1) {

            if (faultType.equals("39")) {   // 未佩戴口罩 仅保存图片
                //下载图片
                CamareMediaDownloadRequestDTO request = CamareMediaDownloadRequestDTO.builder()
                        .elevatorCode(elevatorCode).floor(floor).taskType(CameraTaskTypeEnum.FAULT)
                        .taskCustomId(faultId).taskCustomType(Integer.parseInt(faultType))
                        .mediaType(CameraMediaTypeEnum.JPG).build();

                hikEzvizClient.downloadCameraFileByElevatorCode(request);
                return;
            }

            //下载视频
            try {

                String start;
                String end;
                if ("7".equals(faultType) || "8".equals(faultType)) {
                    start = DateUtil.offsetSecond(occurTime, -120).toStringDefaultTimeZone();
                    end = DateUtil.offsetSecond(occurTime, 600).toStringDefaultTimeZone();
                } else if ("37".equals(faultType)) {
                    start = DateUtil.formatDateTime(occurTime);
                    end = DateUtil.offsetSecond(occurTime, 7).toStringDefaultTimeZone();
                } else {
                    start = DateUtil.offsetSecond(occurTime, -15).toStringDefaultTimeZone();
                    end = DateUtil.offsetSecond(occurTime, 45).toStringDefaultTimeZone();
                }

                CamareMediaDownloadRequestDTO request = CamareMediaDownloadRequestDTO.builder()
                        .elevatorCode(elevatorCode).startTime(start).endTime(end).floor(floor)
                        .taskType(CameraTaskTypeEnum.FAULT).taskCustomId(faultId)
                        .taskCustomType(Integer.parseInt(faultType)).mediaType(CameraMediaTypeEnum.MP4)
                        .collectTime(DateUtil.formatDateTime(occurTime)).build();

                hikEzvizClient.downloadCameraFileByElevatorCode(request);

                log.info("请求海康萤石历史视频下载成功：requestBody{}", request);
            } catch (Exception e) {
                log.info("请求海康萤石历史视频下载失败,Exception：", ExceptionUtils.getStackTrace(e));
            }

            //平层困人电动车乘梯无需二次取图片
            if ("37".equals(faultType) || "7".equals(faultType)) {
                return;
            }

            //获取图片
            CamareMediaDownloadRequestDTO request = CamareMediaDownloadRequestDTO.builder().elevatorCode(elevatorCode)
                    .floor(floor).taskType(CameraTaskTypeEnum.FAULT).taskCustomId(faultId)
                    .taskCustomType(Integer.parseInt(faultType)).mediaType(CameraMediaTypeEnum.JPG)
                    .build();

            hikEzvizClient.downloadCameraFileByElevatorCode(request);

            return;
        }


        //海康云眸平台
        if (camera.getICameraType() == 4) {

            // 未佩戴口罩 仅保存图片
            if (faultType.equals("39")) {
                //下载图片
                hikCloudClient.downloadPictureFile(camera.getVCloudNumber(), elevatorCode, faultType, faultId);
                return;
            }

            //下载视频
            try {
                hikCloudClient.downloadFaultVideoFile(camera.getVCloudNumber(), elevatorCode, faultType, faultId,
                        DateUtils.format(occurTime, "yyyy-MM-dd HH:mm:ss"));
                log.info("请求海康云眸下载历史视频成功：elevatorCode:{},faultType{},faultId{}", elevatorCode, faultType, faultId);
            } catch (Exception e) {
                log.info("请求海康云眸下载历史视频失败,Exception：", e.getMessage());
            }

            //平层困人电动车乘梯无需二次取图片
            if ("37".equals(faultType) || "7".equals(faultType)) {
                return;
            }

            //获取图片
            hikCloudClient.downloadPictureFile(camera.getVCloudNumber(), elevatorCode, faultType, faultId);
        }
        // 天翼视联 （天翼云眼，中兴）
        if (CameraTypeEnum.TYBD.getCode().equals(camera.getICameraType())
                || CameraTypeEnum.TYYY.getCode().equals(camera.getICameraType())) {
            // 未佩戴口罩 仅保存图片
            if ("39".equals(faultType)) {
                //下载图片
                tyslCameraClient.downloadPictureFile(new FaultForHistoryPhotoVideoRequestDTO(camera.getVCloudNumber(),
                        elevatorCode, faultType, faultId, camera.getICameraType()));
                return;
            }

            //下载视频
            try {
                tyslCameraClient.downloadFaultVideoFile(
                        new FaultForHistoryPhotoVideoRequestDTO(camera.getVCloudNumber(), elevatorCode,
                                faultType, faultId, camera.getICameraType())
                                .setOccurTime(DateUtils.format(occurTime, "yyyy-MM-dd HH:mm:ss")));

                log.info("请求天翼视联下载历史视频成功：elevatorCode:{},faultType{},faultId{}", elevatorCode, faultType, faultId);
            } catch (Exception e) {
                log.info("请求天翼视联下载历史视频失败,Exception：", e.getMessage());
            }

            //平层困人电动车乘梯无需二次取图片
            if ("37".equals(faultType) || "7".equals(faultType)) {
                return;
            }

            //获取图片
            tyslCameraClient.downloadPictureFile(new FaultForHistoryPhotoVideoRequestDTO(camera.getVCloudNumber(),
                    elevatorCode, faultType, faultId, camera.getICameraType()));
        }

    }        //CHECKSTYLE:ON

    /**
     * 向摄像头推送困人语音播报
     *
     * @param elevatorCode 电梯编号
     * @param faultId      故障id
     */
    private void pushCameraVoice(String elevatorCode, String faultId, String faultType) {

        log.info("给摄像头推送困人语音播报 - elevatorCode:{} - faultId:{}", elevatorCode, faultId);

        TblCamera camera = cameraService.getByElevatorCode(elevatorCode);
        // 未配置摄像头直接返回
        if (camera == null) {
            log.info("pushCameraVoice error : no camera with elevatorCode[{}]", elevatorCode);
            return;
        }

        // 摄像头类型 1：海康萤石
        if (camera.getICameraType() == 1 && camera.getPushTrappedPeopleVoice() == 1) {
            // 推送困人语音
            ResponseEntity<String> responseEntity = hikEzvizClient.pushCameraVoice(camera.getVCloudNumber(), faultType);

            log.info("给摄像头推送困人语音播报- 请求成功 - elevatorCode:{} - resp:{}",
                    elevatorCode, responseEntity.getBody());

            /**
             * 启动一个任务 3分钟执行一次
             * 因此即使上一个任务执行时间超过周期，也不会影响下一个任务的执行时间
             */
            ScheduledExecutorService sch = Executors.newSingleThreadScheduledExecutor();

            // 创建计数器，初始值为0
            AtomicInteger counter = new AtomicInteger(0);

            sch.scheduleWithFixedDelay(() -> {

                try {
                    //当前故障是否在故障中
                    TblFault fault = faultService.getById(faultId);
                    if (counter.incrementAndGet() < 3 && fault != null && fault.getIStatus() == 0) {
                        // 推送困人语音
                        hikEzvizClient.pushCameraVoice(camera.getVCloudNumber(), faultType);

                        log.info("给摄像头推送困人语音播报- 三分钟重试 - 请求成功 - elevatorCode:{}", elevatorCode);

                    } else {
                        // 停止任务
                        sch.shutdownNow();
                        log.info("给摄像头推送困人语音播报- 三分钟重试 - 故障恢复 - elevatorCode:{}", elevatorCode);
                    }
                } catch (Exception e) {
                    sch.shutdownNow();
                    log.error("困人语音播报-3min执行一次失败，faultId：{}，error:{}", faultId, ExceptionUtils.getStackTrace(e));
                }

            }, 180, 180, TimeUnit.SECONDS);

        } else {
            log.info("pushCameraVoice error : camera not support - elevatorCode[{}] - cameraInfo[{}]",
                    elevatorCode, camera.toString());
        }

    }

    /**
     * 停止实时视频录制【海康】
     *
     * @param elevatorCode 电梯编号
     * @param faultId      故障id
     */
    private void stopFaultHistoryVedio(String elevatorCode, String faultId, String faultType) {

        TblCamera camera = cameraService.getByElevatorCode(elevatorCode);
        // 未配置摄像头直接返回
        if (camera == null) {
            log.error("stopFaultHistoryVedio error : no camera with elevatorCode[{}]", elevatorCode);
            return;
        }

        // 摄像头类型 1：海康，2：雄迈
        if (camera.getICameraType() == 1) {
            // 新增困人时触发开始录制视频， 结束时触发关闭视频录制
            saveHistoryDownloadTaskForYS7(elevatorCode, "disappear", faultId, camera.getVHlsUrl(), faultType);
        }

    }

    /**
     * 保存历史视频/图片下载任务到任务表（由定时任务出发下载视频图片）【雄迈】
     *
     * @param elevatorCode 电梯编号
     * @param fileType     文件类型  0：图片，1：视频
     * @param faultId      故障ID
     * @param startTime    开始时间
     * @param endTime      结束时间
     */
    private Result saveHistoryDownloadTaskForXM(String elevatorCode, String fileType, String faultId,
                                                Date startTime, Date endTime) {

        TblResponseXmReport tblResponseXmReport = new TblResponseXmReport();
        tblResponseXmReport.setId(SnowFlakeUtils.nextStrId());
        //0：图片，1：视频
        tblResponseXmReport.setFileType(fileType);
        //0：待下载 1：下载成功 2：下载中 3:下载失败
        tblResponseXmReport.setFileStatus("0");
        tblResponseXmReport.setCreateTime(new Date());
        tblResponseXmReport.setElevatorCode(elevatorCode);
        tblResponseXmReport.setSerialNumber(elevatorCode);
        tblResponseXmReport.setFaultId(faultId);
        tblResponseXmReport.setStartTime(startTime);
        tblResponseXmReport.setEndTime(endTime);
        return remoteCameraServer.insertResponeXmReport(tblResponseXmReport);
    }


    /**
     * 录制视频处理（海康走kafka）
     */
    public void saveHistoryDownloadTaskForYS7(String elevatorCode, String stype, String faultId,
                                              String hlsUrl, String faultType) {

        //是否连接的天翼物联平台
        HashMap<String, String> info = cameraService.getHKCameraInfoForTY(elevatorCode);
        if (info != null) {
            //推给tyCamera模块处理
            JSONObject failureVideoJson = new JSONObject();
            failureVideoJson.put("elevatorCode", elevatorCode);
            // 沿用之前的代码，这里实际上是故障id
            failureVideoJson.put("workOrderId", faultId);
            // type:add 产生故障，disappear故障结束
            failureVideoJson.put("type", stype);
            //故障类型
            failureVideoJson.put("faultType", faultType);
            kafkaProducer.sendMessageToKafka(KafkaTopicConstants.TY_TOPIC, failureVideoJson.toJSONString());
        } else {
            // 没有hls流直接返回
            if (StringUtil.isNullOrEmpty(hlsUrl)) {
                log.error("saveHistoryDownloadTaskForYS7 error: no hlsUrl with elevatorCode[{}]", elevatorCode);
                return;
            }

            // hls流录制视频
            JSONObject failureVideoJson = new JSONObject();
            failureVideoJson.put("elevatorCode", elevatorCode);
            failureVideoJson.put("playUrl", hlsUrl);
            // 沿用之前的代码，这里实际上是故障id
            failureVideoJson.put("workOrderId", faultId);
            // type:add 产生故障，disappear故障结束
            failureVideoJson.put("type", stype);
            //故障类型
            failureVideoJson.put("faultType", faultType);
            kafkaProducer.sendMessageToKafka(KafkaTopicConstants.HLS_TOPIC, failureVideoJson.toJSONString());
        }

    }

    /**
     * 实时图片下载任务
     *
     * @param elevatorCode 电梯编号
     * @param faultId      故障id
     */
    private Object realPictureDownloadTask(String elevatorCode, String faultId) {
        ImageHandleRequest imageHandleRequest = new ImageHandleRequest();
        imageHandleRequest.setElevatorCode(elevatorCode);
        imageHandleRequest.setFaultId(faultId);
        return remoteCameraServer.imageHandleApplication(imageHandleRequest);
    }

    //CHECKSTYLE:OFF

    /**
     * 故障时 打电话、发短信
     *
     * @param faultId      故障表id
     * @param faultName    故障类型名称
     * @param elevatorCode 电梯编号
     */
    private void faultNotify(String faultId, String faultName, String elevatorCode, String time, String floor,
                             String faultType) {

        TblElevator elevator = elevatorService.getByElevatorCode(elevatorCode);
        if (elevator == null) {
            return;
        }

        // 获取拥有该电梯的用户
        List<TblSysUser> userList = userService.getUserListByCode(elevatorCode);

        if (CollectionUtils.isEmpty(userList)) {
            return;
        }

        //需要推送短信的手机号
        ArrayList<String> pushPhones = new ArrayList<>();

        //正常需推短信用户
        userList.stream().filter(u -> u.getISendMessageStatus() == 0).forEach(u -> {
            String telStr = u.getVMobile();
            if (StringUtils.isNotBlank(telStr)) {
                pushPhones.addAll(Arrays.stream(telStr.split(",")).toList());
            }
        });

        //获取微信小程序推送困人短信用户
        if ("7".equals(faultType) || "8".equals(faultType)) {
            List<String> userIds = userList.stream().map(TblSysUser::getVUserId).collect(Collectors.toList());
            List<String> weChatUserPhones = userService.getWeChatUserPhoneByUserId(userIds, 1);
            pushPhones.addAll(weChatUserPhones);
        }

        //手机号去重并推送短信
        pushPhones.stream().distinct().forEach(tel -> {

            if ("7".equals(faultType) || "8".equals(faultType)) {

                String type = "7".equals(faultType) ? "平层" : "非平层";
                String reportTime = DateUtil.format(DateUtil.parse(time), "yyyy年MM月dd日 HH:mm");
                SendMessageUtil.sendEntrapMessage(tel, type, elevator.getVillageName(),
                        elevator.getVElevatorName(), reportTime, floor);
            } else {
                SendMessageUtil.sendFaultMessageWithFloor(tel, elevatorCode,
                        elevator.getVAddress(), faultName, floor, time);
            }
        });

        //故障电话
        userList.stream().filter(u -> u.getISendPhoneStatus() == 0).forEach(u -> {
            String telStr = u.getVMobile();
            if (StringUtils.isNotBlank(telStr)) {
                String[] tels = telStr.split(",");
                for (String tel : tels) {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", elevatorCode);
                    // 语音模板里有故障两字了，可以去掉
                    jsonObject.put("level", faultName.replace("故障", ""));
                    jsonObject.put("local", elevator.getVAddress());
                    String message = jsonObject.toJSONString();

                    AlarmTaskUtils.singleCallByTts(tel, message, faultId);

                }

            }

        });
    }        //CHECKSTYLE:ON


    private String convertTrappedMessage(String screenTrappedMessage, String elevatorCode) {
        JSONObject jsonObject = JSONObject.parseObject(screenTrappedMessage);
        jsonObject.put(MessageConstants.ELEVATOR_CODE, elevatorCode);
        jsonObject.put(MessageConstants.SENSOR_TYPE, SocketConstants.SENSOR_TYPE_CAR_ROOF);
        return jsonObject.toJSONString();
    }


    /**
     * 获取待识别图片
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @param faultId      故障id
     */
    @Override
    public void getImage(String elevatorCode, String faultType, String faultId) {

        TblCamera camera = cameraService.getByElevatorCode(elevatorCode);
        // 未配置摄像头直接返回
        if (camera == null) {
            log.error("saveFaultHistoryFlie error : no camera with elevatorCode[{}]", elevatorCode);
            return;
        }

        // 摄像头类型 1：海康，2：雄迈
        if (camera.getICameraType() == 2) {
            // 图片取证
            realPictureDownloadTask(elevatorCode, faultId);
            return;
        }


        // 海康萤石云
        if (camera.getICameraType() == 1) {

            CamareMediaDownloadRequestDTO request = CamareMediaDownloadRequestDTO.builder().elevatorCode(elevatorCode)
                    .taskType(CameraTaskTypeEnum.FAULT).taskCustomId(faultId)
                    .taskCustomType(Integer.parseInt(faultType)).mediaType(CameraMediaTypeEnum.JPG)
                    .build();

            hikEzvizClient.downloadCameraFileByElevatorCode(request);

            return;
        }

        //海康云眸
        if (camera.getICameraType() == 4) {

            //获取图片
            hikCloudClient.downloadPictureFile(camera.getVCloudNumber(), elevatorCode, faultType, faultId);

        }
        // 天翼视联 （天翼云眼，中兴）
        if (CameraTypeEnum.TYBD.getCode().equals(camera.getICameraType())
                || CameraTypeEnum.TYYY.getCode().equals(camera.getICameraType())) {

            //获取图片
            tyslCameraClient.downloadPictureFile(new FaultForHistoryPhotoVideoRequestDTO(camera.getVCloudNumber(),
                    elevatorCode, faultType, faultId, camera.getICameraType()));

        }
    }
}