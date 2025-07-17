package com.shmashine.fault.message.handle;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.common.enums.CameraTaskTypeEnum;
import com.shmashine.common.model.request.ImageHandleRequest;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.DateUtils;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.fault.camera.entity.TblCamera;
import com.shmashine.fault.camera.service.TblCameraServiceI;
import com.shmashine.fault.elevator.dao.TblElevatorDao;
import com.shmashine.fault.elevator.entity.TblElevator;
import com.shmashine.fault.elevator.service.TblElevatorService;
import com.shmashine.fault.fault.entity.TblFault;
import com.shmashine.fault.fault.service.TblFaultDefinitionServiceI;
import com.shmashine.fault.fault.service.TblFaultServiceI;
import com.shmashine.fault.feign.RemoteCameraServer;
import com.shmashine.fault.kafka.KafkaProducer;
import com.shmashine.fault.mongo.entity.FaultMessageConfirm;
import com.shmashine.fault.mongo.utils.MongoTemplateUtil;
import com.shmashine.fault.redis.RedisService;
import com.shmashine.fault.user.service.TblSysUserServiceI;
import com.shmashine.hikYunMou.client.RemoteHikCloudClient;
import com.shmashine.hkcamerabyys.client.RemoteHikEzvizClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 故障落库处理-扶梯协议(上海浮奈 电信)
 *
 * @author little.li
 */
@Slf4j
@Component
public class FaultHandleV100E implements FaultHandle {

    private static Logger faultMessageLogger = LoggerFactory.getLogger("faultMessageLogger");

    private final TblFaultServiceI faultService;

    private final TblFaultDefinitionServiceI faultDefinitionService;

    private final TblElevatorService elevatorService;

    private final TblCameraServiceI cameraService;

    private final RedisService redisService;

    private final KafkaProducer kafkaProducer;

    private final TblSysUserServiceI userService;

    @Resource(type = TblElevatorDao.class)
    private TblElevatorDao elevatorDao;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(8, 16,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "FaultHandleV100E");


    @Autowired
    public FaultHandleV100E(TblFaultServiceI faultService, TblFaultDefinitionServiceI faultDefinitionService,
                            TblElevatorService elevatorService, TblCameraServiceI cameraService,
                            RedisService redisService, KafkaProducer kafkaProducer, TblSysUserServiceI userService) {
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
    private MongoTemplateUtil mongoTemplateUtil;

    @Resource
    private RemoteHikCloudClient hikCloudClient;

    /**
     * 新增故障
     */
    @Override
    public void addFault(JSONObject messageJson) {

        String elevatorCode = messageJson.getString("elevatorCode");
        String faultType = messageJson.getString("fault_type");
        String faultSecondType = messageJson.getString("fault_stype");

        // 获取故障业务id（全系统唯一标识）
        String faultId = messageJson.getString("faultId");

        faultMessageLogger.info("{} --收到故障消息 elevatorCode[{}] , faultType[{}]",
                TimeUtils.nowTime(), elevatorCode, faultType);

        // 判断改故障是否已经记录（幂等判断）
        TblFault fault = faultService.getById(faultId);
        // 未记录情况判断 该电梯是否处于该类故障中
        if (fault == null) {
            fault = faultService.getInFaultByFaultType(elevatorCode, faultType);
        }

        // 不存在故障（走新增故障逻辑）
        if (fault == null) {
            // redis中记录电梯为【故障中】
            redisService.updateFaultStatus(elevatorCode, 1);
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
            tblFault.setILevel(1);
            tblFault.setILevelName("1");
            tblFault.setVEventChannel("n");
            tblFault.setIStatus(0);
            tblFault.setDtCreateTime(new Date());
            // 不文明行为标识
            tblFault.setIUncivilizedBehaviorFlag(0);

            // 新增故障记录
            int status = faultService.insert(tblFault);

            if (status < 0) {
                return;
            }

            // 数据库中更新电梯为 【故障中】
            executorService.submit(() -> elevatorService.updateFaultStatus(elevatorCode, 1));


        } else {
            // 故障次数 + 1
            fault.setIFaultNum(fault.getIFaultNum() + 1);
            faultService.update(fault);
        }

        //移除mongo故障记录
        mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
    }


    /**
     * 消除故障
     */
    @Override
    public void disappearFault(JSONObject messageJson) {
        String elevatorCode = messageJson.getString("elevatorCode");
        String faultType = messageJson.getString("fault_type");
        String faultId = messageJson.getString("faultId");

        // 获取故障中的故障记录
        TblFault fault = faultService.getInFaultByFaultType(elevatorCode, faultType);

        // 存在故障中的故障
        if (fault != null) {
            dissapperFault(messageJson, elevatorCode, faultType, fault);
            return;
        }

        //是否有add任务未处理
        List<FaultMessageConfirm> faults = mongoTemplateUtil.queryByElevatorAndFault(elevatorCode, faultType,
                "add", FaultMessageConfirm.class);

        if (faults == null || faults.size() == 0) {
            mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
        }

    }


    @Transactional(rollbackFor = Exception.class)
    public void dissapperFault(JSONObject messageJson, String elevatorCode, String faultType, TblFault fault) {

        // 获取故障业务id（全系统唯一标识）
        String faultId = messageJson.getString("faultId");

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

        //移除mongo故障记录
        mongoTemplateUtil.removeById(faultId, FaultMessageConfirm.class);
    }

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

        // 海康萤石云
        if (camera.getICameraType() == 1) {

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
                log.info("请求海康萤石历史视频下载失败,Exception：", e.getMessage());
            }

            //获取图片
            CamareMediaDownloadRequestDTO request = CamareMediaDownloadRequestDTO.builder().elevatorCode(elevatorCode)
                    .taskType(CameraTaskTypeEnum.FAULT).taskCustomId(faultId)
                    .taskCustomType(7).mediaType(CameraMediaTypeEnum.JPG)
                    .collectTime(DateUtil.formatDateTime(occurTime)).build();

            hikEzvizClient.downloadCameraFileByElevatorCode(request);

            return;
        }


        //海康云眸平台
        if (camera.getICameraType() == 4) {

            //下载视频
            try {

                hikCloudClient.downloadFaultVideoFile(camera.getVCloudNumber(), elevatorCode, faultType, faultId,
                        DateUtils.format(occurTime, "yyyy-MM-dd HH:mm:ss"));

                log.info("请求海康云眸下载历史视频成功：elevatorCode:{},faultType{},faultId{}", elevatorCode, faultType, faultId);
            } catch (Exception e) {
                log.info("请求海康云眸下载历史视频失败,Exception：", e.getMessage());
            }

            //获取图片
            hikCloudClient.downloadPictureFile(camera.getVCloudNumber(), elevatorCode, faultType, faultId);
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
                    .taskCustomType(7).mediaType(CameraMediaTypeEnum.JPG)
                    .collectTime(DateUtil.formatDateTime(new Date())).build();

            hikEzvizClient.downloadCameraFileByElevatorCode(request);

            return;
        }

        //海康云眸
        if (camera.getICameraType() == 4) {

            //获取图片
            hikCloudClient.downloadPictureFile(camera.getVCloudNumber(), elevatorCode, faultType, faultId);

        }
    }
}