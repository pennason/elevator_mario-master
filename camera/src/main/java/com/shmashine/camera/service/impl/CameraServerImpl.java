package com.shmashine.camera.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.json.JSONArray;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.camera.constants.CameraConstants;
import com.shmashine.camera.constants.SystemConstants;
import com.shmashine.camera.dao.BizCameraDao;
import com.shmashine.camera.dao.BizElevatorDao;
import com.shmashine.camera.dao.TblResponseXmReportDao;
import com.shmashine.camera.dao.TblSenderFaultMapper;
import com.shmashine.camera.dao.TblSysFileDao;
import com.shmashine.camera.entity.TblSysFile;
import com.shmashine.camera.kafka.KafkaProducer;
import com.shmashine.camera.kafka.KafkaTopicConstants;
import com.shmashine.camera.message.FaultMessage;
import com.shmashine.camera.model.CameraModule;
import com.shmashine.camera.model.CamerasResponseModule;
import com.shmashine.camera.model.FaultCameraModule;
import com.shmashine.camera.model.ImageHandleRequest;
import com.shmashine.camera.model.Result;
import com.shmashine.camera.model.SearchCamerasModule;
import com.shmashine.camera.model.VedioResponse;
import com.shmashine.camera.model.VideoHandlerRequest;
import com.shmashine.camera.model.XmHlsHttpOrHttpsModule;
import com.shmashine.camera.model.base.PageListResultEntity;
import com.shmashine.camera.model.base.ResponseResult;
import com.shmashine.camera.model.elevator.ElevatorDetailModule;
import com.shmashine.camera.model.elevator.TblElevator;
import com.shmashine.camera.redis.RedisService;
import com.shmashine.camera.service.CameraServer;
import com.shmashine.camera.service.FileService;
import com.shmashine.camera.utils.CameraUtils;
import com.shmashine.camera.utils.CamerasUtils;
import com.shmashine.camera.utils.VideoUtils;
import com.shmashine.cameratysl.client.RemoteCameraTyslClient;
import com.shmashine.cameratysl.client.dto.ResponseCustom;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.constants.ServiceConstants;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.TblCamera;
import com.shmashine.common.entity.TblCameraCascadePlatformEntity;
import com.shmashine.common.entity.TblFaultTemp;
import com.shmashine.common.entity.TblResponseXmReport;
import com.shmashine.common.entity.TblSenderFaultEntity;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.common.enums.CameraTaskTypeEnum;
import com.shmashine.common.enums.CameraTypeEnum;
import com.shmashine.common.model.request.FaceRecognitionRequest;
import com.shmashine.common.properties.AliOssProperties;
import com.shmashine.common.utils.FileUtil;
import com.shmashine.common.utils.ImageIdentifyUtils;
import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.RequestUtil;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.hikYunMou.client.RemoteHikCloudClient;
import com.shmashine.hkcamerabyys.client.RemoteHikEzvizClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 摄像头业务实现类
 *
 * @author Dean Winchester
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CameraServerImpl implements CameraServer {
    private final TblSysFileDao tblSysFileDao;
    private final BizCameraDao bizCameraDao;
    private final BizElevatorDao bizElevatorDao;
    private final TblResponseXmReportDao tblResponseXmReportDao;
    private final TblSenderFaultMapper senderFaultMapper;
    private final RedisService redisService;
    private final RedisTemplate redisTemplate;
    private final FileService fileService;
    private final RedissonClient redissonClient;
    private final KafkaProducer kafkaProducer;
    private final RemoteHikEzvizClient hikEzvizClient;
    private final RemoteHikCloudClient hikCloudClient;
    private final RemoteCameraTyslClient tyslClient;
    private final AliOssProperties aliOssProperties;


    public static final String XM_NUMBER_OF_CALLS_CAMERSERVER = "XM_NUMBER_OF_CALLS_CAMERSERVER";


    /**
     * 摄像头的添加
     *
     * @param tblCamera 摄像头参数
     * @return
     */
    @Override
    public ResponseResult insert(TblCamera tblCamera) {

        TblCamera camera = new TblCamera();
        //如果有电梯码，需要校验电梯是否存在
        if (StringUtils.hasText(tblCamera.getVElevatorCode())) {
            TblElevator elevator = bizElevatorDao.getElevatorByCode(tblCamera.getVElevatorCode());
            if (elevator == null) {
                //无效的电梯编号
                return new ResponseResult(ResponseResult.CODE_ERROR, "msg9_02");
            }
            //根据电梯码找到电梯编号
            camera.setVElevatorId(elevator.getVElevatorId());
        }

        //如果添加序列号，需要判断序列号是否存在，存在需要提示序列号已经注册
        if (StringUtils.hasText(tblCamera.getVCloudNumber())) {
            List<TblCamera> cameras = bizCameraDao.getByvCloudNumber(tblCamera.getVCloudNumber());
            if (cameras.size() > 0) {
                //序列号已经注册
                return new ResponseResult(ResponseResult.CODE_ERROR, "msg9_06");
            }
        }

        String cameraId = tblCamera.getVCameraId();
        //如果手输cameraId需要校验是否存在
        if (StringUtils.hasText(cameraId)) {
            TblCamera camera1 = bizCameraDao.getByCameraId(cameraId);
            //存在的话需要提示已经存在id请重新输入
            if (camera1 != null) {
                return new ResponseResult(ResponseResult.CODE_ERROR, "msg9_03");
            }
            //表记录不存在的话，使用手动输入的id
            camera.setVCameraId(cameraId);
        } else {
            //没有输入id，雪花算法生成
            camera.setVCameraId(SnowFlakeUtils.nextStrId());
        }
        //注入其他参数
        Date date = new Date();
        camera.setDtCreateTime(date);
        camera.setDtModifyTime(date);
        camera.setIDelFlag(0);
        camera.setICameraType(tblCamera.getICameraType());
        camera.setVPrivateUrl(tblCamera.getVPrivateUrl());
        camera.setVCloudNumber(tblCamera.getVCloudNumber());
        camera.setVSerialNumber(tblCamera.getVSerialNumber());
        camera.setVHlsUrl(tblCamera.getVHlsUrl());
        camera.setVRtmpUrl(tblCamera.getVRtmpUrl());
        camera.setVElevatorCode(tblCamera.getVElevatorCode());
        camera.setIsActivate(tblCamera.getIsActivate());
        camera.setDeviceCode(tblCamera.getDeviceCode());
        camera.setPushTrappedPeopleVoice(tblCamera.getPushTrappedPeopleVoice());

        if (StringUtils.hasText(tblCamera.getVElevatorCode())) {
            //查看该摄像头是否被绑定
            TblCamera bindingCamera = bizCameraDao.getByElevatorCode(tblCamera.getVElevatorCode());
            //电梯已经绑定摄像头返回：（该电梯已经绑定摄像头）
            if (bindingCamera != null) {
                return new ResponseResult(ResponseResult.CODE_ERROR, "msg9_04");
            }
            //新增
            bizCameraDao.insertCamera(camera);
        } else {
            //新增
            bizCameraDao.insertCamera(camera);
        }

        log.info("--- 添加摄像头  摄像头id: --- 摄像头类型: --- 绑定的电梯: ---" + TimeUtils.nowTime() + camera.getVCameraId() + camera.getICameraType() + camera.getVElevatorCode());

        //添加成功
        return ResponseResult.successObj(bizCameraDao.getByCameraId(camera.getVCameraId()));

    }

    /**
     * 摄像头修改、删除
     *
     * @param tblCamera
     * @return
     */
    @Override
    public ResponseResult update(TblCamera tblCamera) {

        if (tblCamera.getIDelFlag() != null && tblCamera.getIDelFlag() == 1) {

            log.info("用户请求删除摄像头，vCameraId：{}", tblCamera.getVCameraId());

            bizCameraDao.deleteCameraInfoById(tblCamera.getVCameraId());

            return ResponseResult.successObj("摄像头删除成功");
        }

        //如果有电梯码，需要校验电梯是否存在
        if (StringUtils.hasText(tblCamera.getVElevatorCode())) {
            TblElevator elevator = bizElevatorDao.getElevatorByCode(tblCamera.getVElevatorCode());
            if (elevator == null) {
                //无效的电梯编号
                return new ResponseResult(ResponseResult.CODE_ERROR, "msg9_02");
            }
            TblCamera tblCameraByEleCode = bizCameraDao.getByElevatorCode(tblCamera.getVElevatorCode());
            //若该电梯有绑定摄像头
            if (tblCameraByEleCode != null) {
                //与原来的摄像头进行解绑
                tblCameraByEleCode.setVElevatorCode(null);
                tblCameraByEleCode.setVElevatorId(null);
                //修改时间
                tblCameraByEleCode.setDtModifyTime(new Date());
                //解绑操作
                bizCameraDao.updateCamera(tblCameraByEleCode);
            }
            //根据电梯码找到电梯编号
            tblCamera.setVElevatorId(elevator.getVElevatorId());
        }

        bizCameraDao.updateCamera(tblCamera);
        // 删除城市推送相关缓存
        if (StringUtils.hasText(tblCamera.getVElevatorCode())) {
            redisService.delElevatorCacheForCity(tblCamera.getVElevatorCode());
        }


        log.info("--- 修改摄像头  摄像头id: --- 摄像头类型: --- 绑定的电梯: ---" + TimeUtils.nowTime() + tblCamera.getVCameraId() + tblCamera.getICameraType() + tblCamera.getVElevatorCode());

        return ResponseResult.successObj(bizCameraDao.getByCameraId(tblCamera.getVCameraId()));
    }

    /**
     * 摄像头绑定修改根据摄像头id绑定解绑
     *
     * @param tblCamera
     * @return
     */
    @Override
    public ResponseResult updateElevotorBound(TblCamera tblCamera) {

        //判断摄像头是否存在
        if (StringUtils.isEmpty(tblCamera.getVCameraId())) {
            //摄像头为空时表示解除绑定
            TblCamera camera = bizCameraDao.getByElevatorCode(tblCamera.getVElevatorCode());
            //设置改摄像头绑定状态为空
            camera.setVElevatorCode(null);
            camera.setVElevatorId(null);
            //修改时间
            camera.setDtModifyTime(new Date());
            //修改绑定

            log.info("--- 解除绑定  摄像头id: --- 原来绑定的电梯: ---" + TimeUtils.nowTime() + tblCamera.getVCameraId() + tblCamera.getVElevatorCode());

            bizCameraDao.updateCamera(camera);
        } else {

            TblCamera camera = bizCameraDao.getByCameraId(tblCamera.getVCameraId());
            if (camera == null) {
                //摄像头不存在
                return new ResponseResult(ResponseResult.CODE_ERROR, "msg9_05");
            }
            //根据电梯码查询原来绑定情况
            TblCamera oldElevator = bizCameraDao.getByElevatorCode(tblCamera.getVElevatorCode());

            if (oldElevator != null) {
                //修改电梯码
                camera.setVElevatorCode(tblCamera.getVElevatorCode());
                //修改电梯码id如果有的话
                camera.setVElevatorId(oldElevator.getVElevatorId());
                //修改时间
                camera.setDtModifyTime(new Date());

                //原来绑定的电梯
                String oldElevatorVElevatorCode = oldElevator.getVElevatorCode();

                //删除原来电梯绑定,摄像头与电梯为一对一绑定
                oldElevator.setVElevatorCode(null);
                oldElevator.setVElevatorId(null);
                bizCameraDao.updateCamera(oldElevator);

                log.info("--- 绑定电梯  摄像头id: --- 原来绑定的电梯: ---  现在绑定的电梯：" + TimeUtils.nowTime() + tblCamera.getVCameraId() + oldElevatorVElevatorCode + camera.getVElevatorCode());

                //修改绑定
                bizCameraDao.updateCamera(camera);
            } else {
                //如果没有绑定，则新加绑定
                camera.setVElevatorCode(tblCamera.getVElevatorCode());
                TblElevator elevator = bizElevatorDao.getElevatorByCode(tblCamera.getVElevatorCode());
                //修改电梯码id
                camera.setVElevatorId(elevator.getVElevatorId());
                //修改时间
                camera.setDtModifyTime(new Date());

                log.info("--- 绑定电梯  摄像头id: --- 原来绑定的电梯: ---  现在绑定的电梯：" + TimeUtils.nowTime() + tblCamera.getVCameraId() + "" + camera.getVElevatorCode());

                //新增绑定
                bizCameraDao.updateCamera(camera);
            }

        }
        return ResponseResult.success();
    }

    /**
     * 清理下载超过一天未完成的视频
     */
    @Override
    public void autoClearnDownloadingVideo() {
        tblResponseXmReportDao.autoClearnDownloadingVideo();
    }

    @Override
    public void afreshGetRecognitionResult() {

        Set range = redisTemplate.opsForZSet().reverseRange(RedisConstants.HLS_IDENTIFICATION, 0, -1);

        for (Object o : range) {

            String faultId = (String) o;

            String imageUrl = fileService.getFaultTempImage(faultId);

            if (imageUrl == null) {
                redisTemplate.opsForZSet().remove(RedisConstants.HLS_IDENTIFICATION, faultId);
                return;
            }

            // restTemplateSendMessage(faultId, imageUrl, "person");
            ImageIdentifyUtils.identifyImage(faultId, imageUrl, ImageIdentifyUtils.IDENTIFY_TYPE_PERSON,
                    aliOssProperties.getUseInternal());
        }
    }

    @Override
    public void afreshDownloadImage() {

        long nowTime = System.currentTimeMillis() / 1000;
        //超过30s没下载成功不再下载
        redisTemplate.opsForZSet().removeRangeByScore(RedisConstants.HLS_IMAGE, 0, nowTime - 31);

        Set afresh = redisTemplate.opsForZSet().rangeByScore(RedisConstants.HLS_IMAGE, nowTime - 30, nowTime);

        for (Object o : afresh) {

            //重新下载
            String faultId = (String) o;

            TblFaultTemp faultTemp = fileService.getFalutTempById(faultId);

            TblCamera camera = getByElevatorCode(faultTemp.getVElevatorCode());
            // 未配置摄像头直接返回
            if (camera == null) {
                return;
            }

            // 摄像头类型 1：海康，2：雄迈
            if (camera.getICameraType() == 2) {// 雄迈
                ImageHandleRequest imageHandleRequest = new ImageHandleRequest();
                imageHandleRequest.setElevatorCode(faultTemp.getVElevatorCode());
                imageHandleRequest.setFaultId(faultId);
                imageHandleApplication(imageHandleRequest);
            } else {// 海康萤石云

//                DownLoadByHKYSRequestBody downLoadByHKYSRequestBody = new DownLoadByHKYSRequestBody();
//
//                downLoadByHKYSRequestBody.setDeviceSerial(camera.getVCloudNumber());
//                downLoadByHKYSRequestBody.setElevatorCode(faultTemp.getVElevatorCode());
//                downLoadByHKYSRequestBody.setFaultId(faultId);
//                downLoadByHKYSRequestBody.setFaultType("7");
//                downLoadByHKYSRequestBody.setOccurTime(new Date());
//                downLoadByHKYSRequestBody.setSType("add");

                //获取锁，下载是一个耗时操作，防止重复下载
                RLock lock = redissonClient.getLock(RedisConstants.AFRESDOWNLOADIMAGE_LOCK + faultId);
                if (!lock.isLocked()) {
                    log.info("-------困人图片下载-----重新下载---------------");
                    CamareMediaDownloadRequestDTO request = CamareMediaDownloadRequestDTO.builder().elevatorCode(faultTemp.getVElevatorCode())
                            .taskType(CameraTaskTypeEnum.FAULT).taskCustomId(faultId)
                            .taskCustomType(7).mediaType(CameraMediaTypeEnum.JPG)
                            .collectTime(DateUtil.formatDateTime(faultTemp.getDtReportTime())).build();

                    hikEzvizClient.downloadCameraFileByElevatorCode(request);
//                    hikEzvizClient.downloadPictureFile(downLoadByHKYSRequestBody);
                } else {
                    log.info("-------困人图片下载-----正在下载中.............");
                }
            }
        }
    }

    @Override
    public List<TblResponseXmReport> findResponeXmReportByStatus(String actionType) {
        return tblResponseXmReportDao.findResponeXmReportByStatus(actionType);
    }

    @Override
    public List<TblResponseXmReport> findResponeXmReportByFileStatus(String fileStatus, String actionType) {
        return tblResponseXmReportDao.findResponeXmReportByFileStatus(fileStatus, actionType);
    }

    /**
     * 基础服务：分页获取摄像头信息
     *
     * @param searchCamerasModule
     * @return
     */
    @Override
    public PageListResultEntity searchElevatorListByPage(SearchCamerasModule searchCamerasModule) {
        Integer pageIndex = searchCamerasModule.getPageIndex();
        Integer pageSize = searchCamerasModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo<>(bizCameraDao.searchElevatorListByPage(searchCamerasModule), pageSize);

        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }

    /**
     * 分页获取摄像头信息
     *
     * @param searchCamerasModule
     * @return
     */
    @Override
    public PageListResultEntity searchCamerasListByPage(SearchCamerasModule searchCamerasModule) {

        //分页信息建全（设置默认页码和每页展示数据）
        Integer pageIndex = searchCamerasModule.getPageIndex();
        Integer pageSize = searchCamerasModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo<>(bizCameraDao.searchCamerasListByPage(searchCamerasModule), pageSize);

        //补充摄像头告警配置
        List<Map<String, Object>> resultMap = hashMapPageInfo.getList().stream().map(camera -> {

            String elevatorCode = (String) camera.get("vElevatorCode");

            if (StringUtils.hasText(elevatorCode)) {

                String id = bizCameraDao.getCameraAlarmConfigByElevatorCode(elevatorCode);
                camera.put("alarmConfigId", id);
            }

            return camera;
        }).collect(Collectors.toList());

        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), resultMap);
    }


    /**
     * 分页获取摄像头相关的视频和图片
     *
     * @param searchCamerasModule
     * @return
     */
    @Override
    public PageListResultEntity searchCamerasVedioAndPicByPage(SearchCamerasModule searchCamerasModule) {
        Integer pageIndex = searchCamerasModule.getPageIndex();
        Integer pageSize = searchCamerasModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }
        PageHelper.startPage(pageIndex, pageSize);
        List<CamerasResponseModule> list = bizCameraDao.searchCamerasVedioAndPicByPage(searchCamerasModule);
        PageInfo<CamerasResponseModule> hashMapPageInfo = new PageInfo<>(list, pageSize);
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }

    /**
     * fault调用录制视频处理程序
     *
     * @param videoHandlerRequest
     * @return
     */
    @Override
    public ResponseResult videoHandlerApplication(VideoHandlerRequest videoHandlerRequest) {
        TblCamera camera = bizCameraDao.getByElevatorCode(videoHandlerRequest.getElevatorCode());
        // 未配置摄像头直接返回
        if (camera == null) {
            // 该电梯未安装摄像头，无法保存录像
            return new ResponseResult(ResponseResult.CODE_VIDEO_ERROR, "msg_vedio_01");
        }

        // 雄迈摄像头获取历史视频
        if (camera.getICameraType() == 2) {
            // 故障结束时才进行保存视频
            if (MessageConstants.STYPE_ADD.equals(videoHandlerRequest.getStype()) || org.apache.commons.lang3.StringUtils.isBlank(camera.getVSerialNumber())) {
                return new ResponseResult(ResponseResult.CODE_ERROR, "msg_vedio_02");
            }
            Integer resultCode = CameraUtils.saveXiongMaiHistoryVideo(videoHandlerRequest.getFaultId(), camera.getVSerialNumber(), videoHandlerRequest.getStartTime(), videoHandlerRequest.getEndTime());
            return ResponseResult.successObj("saveXiongMaiHistoryVideo code:" + resultCode);

        } else { // 海康摄像头获取历史视频

            // 没有hls流直接返回
            if (camera.getVHlsUrl() == null) {
                return new ResponseResult(ResponseResult.CODE_ERROR, "msg_vedio_03");
            }

            // hls流录制视频
            JSONObject failureVideoJson = new JSONObject();
            failureVideoJson.put("elevatorCode", videoHandlerRequest.getElevatorCode());
            failureVideoJson.put("playUrl", camera.getVHlsUrl());
            // 沿用之前的代码，这里实际上是故障id
            failureVideoJson.put("workOrderId", videoHandlerRequest.getFaultId());

            // type:add 产生故障，disappear故障结束
            failureVideoJson.put("type", videoHandlerRequest.getStype());
            kafkaProducer.sendMessageToKafka(KafkaTopicConstants.HLS_TOPIC, failureVideoJson.toJSONString());
            return ResponseResult.success();
        }
    }

    /**
     * fault调用截图处理程序
     *
     * @param imageHandleRequest
     * @return
     */
    @Override
    public ResponseResult imageHandleApplication(ImageHandleRequest imageHandleRequest) {

        log.info("save XiongMai history Picture request！ imageHandleApplication imageHandleRequest: {}", JSONObject.toJSONString(imageHandleRequest));

        TblCamera camera = bizCameraDao.getByElevatorCode(imageHandleRequest.getElevatorCode());

        // 未配置摄像头直接返回
        if (camera == null) {
            log.info("imageHandleApplication error: camera is null!  imageHandleRequest: {}", JSONObject.toJSONString(imageHandleRequest));
            return new ResponseResult(ResponseResult.CODE_IMAGE_ERROR, "msg_image_01");
        }

        // 雄迈摄像头截取图片
        if (camera.getICameraType() == 2) {
            Integer resultCode = CameraUtils.saveXiongMaiRealtimePicture(imageHandleRequest.getFaultId(), camera.getVSerialNumber());
            log.info("imageHandleApplication imageHandleRequest: {}, resultCode: {}", JSONObject.toJSONString(imageHandleRequest), resultCode);
            return ResponseResult.successObj("saveXiongMaiHistoryVideo code:" + resultCode);
        }
        return new ResponseResult(ResponseResult.CODE_IMAGE_ERROR, "msg_image_03");
    }

    @Override
    public Integer faceRecognition(FaceRecognitionRequest faceRecognitionRequest) {
        TblCamera camera = bizCameraDao.getByElevatorCode(faceRecognitionRequest.getElevatorCode());
        if (camera == null) {
            log.info("------ 困人 --- 视频流不存在 --- " + TimeUtils.nowTime() + faceRecognitionRequest.getElevatorCode());
            return -1;
        }
        //String path = "D:\\shmashine-deploy\\java-oreo\\socket\\image"+TimeUtils.getTenBitTimestamp() + ".jpg";
        String path = "/shmashine-deploy/java-oreo/socket/image/" + TimeUtils.getTenBitTimestamp() + ".jpg";
        if (camera.getICameraType() == 2) {
            // 雄迈摄像头
            String hlsUrl;
            String redisHlsUrl = redisService.getXiongMaiHlsUrl(faceRecognitionRequest.getElevatorCode());
            if (StringUtils.hasText(redisHlsUrl)) {
                hlsUrl = redisHlsUrl;
                log.info("--- 获取雄迈byRedis --- " + TimeUtils.nowTime() + faceRecognitionRequest.getElevatorCode() + path + hlsUrl);
            } else if ("".equals(redisHlsUrl) || null == redisHlsUrl) {
                hlsUrl = camera.getVHlsUrl();
                log.info("--- 获取雄迈byRedis --- " + TimeUtils.nowTime() + faceRecognitionRequest.getElevatorCode() + path + hlsUrl);
            } else {
                //调用-雄迈摄像-头次数累加
                redisTemplate.opsForHash().increment(XM_NUMBER_OF_CALLS_CAMERSERVER, camera.getVElevatorCode(), 1);

                hlsUrl = CameraUtils.getXiongMaiHls(camera.getVCloudNumber(), faceRecognitionRequest.getElevatorCode());
                redisService.setXiongMaiHlsUrl(faceRecognitionRequest.getElevatorCode(), hlsUrl);
            }
            log.info("--- 困人 --- Baidu截取图片 --- 存储路径: --- sourceUrl: ---" + TimeUtils.nowTime() + faceRecognitionRequest.getElevatorCode() + path + hlsUrl);

            // 重试三次
            int flag = 3;
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(hlsUrl);
                    ff.start();
                    doExecuteFrame(ff.grabImage(), path);
                    ff.stop();
                    break;
                } catch (FrameGrabber.Exception | InterruptedException e) {
                    log.info(" --- 困人 --- 打开流失败...重试 ---" + TimeUtils.nowTime() + faceRecognitionRequest.getElevatorCode() + flag);
                    flag--;
                }
                if (flag == 0) {
                    log.info(" --- 困人 --- 重试3次失败... ---" + TimeUtils.nowTime() + faceRecognitionRequest.getElevatorCode());
                    return -1;
                }
            }
        }
        File file = new File(path);
        if (file.exists()) {
            try {
                if (StringUtils.isEmpty(path)) {
                    log.info("--- 困人 --- 截取图片失败，没有视频源返回 ---" + TimeUtils.nowTime() + faceRecognitionRequest.getElevatorCode());
                    // 没有视频源返回 -1
                    return -1;
                }

                // 初始化一个AipBodyAnalysis
                AipBodyAnalysis client = new AipBodyAnalysis(ServiceConstants.APP_ID, ServiceConstants.API_KEY, ServiceConstants.SECRET_KEY);

                // 可选：设置网络连接参数
                client.setConnectionTimeoutInMillis(2000);
                client.setSocketTimeoutInMillis(10000);

                // 调用接口
                org.json.JSONObject res = client.bodyAnalysis(path, new HashMap<>());
                if (!res.has("error_code")) {
                    int personNum = 0;
                    JSONArray personInfo = res.getJSONArray("person_info");
                    if (personInfo.length() == 0) {
                        FileUtil.deleteFile(path);
                        System.out.printf("%s --- %s --- 困人 --- Baidu识别结果: %s\n", TimeUtils.nowTime(), faceRecognitionRequest.getElevatorCode(), 0);
                        return -1;
                    }

                    double score = personInfo.getJSONObject(0).getJSONObject("location").getDouble("score");
                    if (score < 0.03) {
                        FileUtil.deleteFile(path);
                        System.out.printf("%s --- %s --- 困人 --- Baidu识别结果: score: %s\n", TimeUtils.nowTime(), faceRecognitionRequest.getElevatorCode(), score);
                        return -1;
                    }

                    for (int i = 0; i < personInfo.length(); i++) {
                        double countScore = 0.0;
                        org.json.JSONObject jsonObject = personInfo.getJSONObject(i);
                        org.json.JSONObject bodyParts = jsonObject.getJSONObject("body_parts");
                        String[] names = org.json.JSONObject.getNames(bodyParts);
                        for (String name : names) {
                            org.json.JSONObject soc = bodyParts.getJSONObject(name);
                            countScore += soc.getDouble("score");
                        }
                        if (countScore / 21 > 0.2) {
                            personNum++;
                        }
                    }
                    if (personNum <= 0) {
                        FileUtil.deleteFile(path);
                    } else {
                        // 存储图片文件，关联故障id（故障审核）
                        String faultName = OSSUtil.saveFaultMP4(path);

                        TblSysFile files = new TblSysFile();
                        files.setDtCreateTime(new Date());
                        files.setDtModifyTime(new Date());
                        files.setIBusinessType(2);
                        files.setVFileType(String.valueOf(0));
                        files.setVBusinessId(faceRecognitionRequest.getFaultId());
                        files.setVFileId(SnowFlakeUtils.nextStrId());
                        files.setVFileName(faultName);
                        files.setVUrl(OSSUtil.OSS_URL + faultName);

                        //////////////////////////////////////////////////////
                        String debugInfo = JSONObject.toJSONString(files);
                        log.info("*************debugInfo*************" + debugInfo);
                        //////////////////////////////////////////////////////

                        tblSysFileDao.saveFile(files);
                        //////////////////////////////////////////////////////
                        log.info("*************信息保存成功*************");
                        //////////////////////////////////////////////////////
                        FileUtil.deleteFile(path);
                    }
                    return personNum;
                } else {
                    FileUtil.deleteFile(path);
                    return -1;
                }
            } catch (Exception e) {
                FileUtil.deleteFile(path);
                e.printStackTrace();
                log.info("faceRecognition: [{}] exception: [{}]" + faceRecognitionRequest.getElevatorCode() + e.getMessage());
            }
            return -1;
        }

        return -1;
    }

    /**
     * 调用雄迈摄像头获取历史视频，进行截取
     *
     * @param elevatorCode
     * @param faultCameraModule
     * @return
     */
    @Override
    public Result getCameraXmHistory(String elevatorCode, FaultCameraModule faultCameraModule) {
        log.info("--- getCameraXmHistory --- API服务请求 --- 添加记录表 --- 开始 ---");
        //判断faultId
        if (faultCameraModule.getId() == null) {
            log.info("--- getCameraXmHistory --- API服务请求 --- 添加记录表 --- 故障id不能为空 ---");
            return Result.error("故障id不能为空");
        }
        TblResponseXmReport oldReport = tblResponseXmReportDao.findResponeXmReportByFaultId(faultCameraModule.getId());
        if (oldReport != null) {
            log.info("--- getCameraXmHistory --- API服务请求 --- 添加记录表 --- 记录表中存在，不可重复添加 ---");
            return Result.error("正在处理，请稍后查看");
        }

        //写入记录表 通过定时处理
        TblResponseXmReport tblResponseXmReport = new TblResponseXmReport();
        tblResponseXmReport.setId(SnowFlakeUtils.nextStrId());
        //'1：请求雄迈 2：雄迈回调
        tblResponseXmReport.setActionType("1");
        //0：图片，1：视频
        tblResponseXmReport.setFileType("1");
        //0：待下载 1：下载成功 2：下载中 3：下载失败
        tblResponseXmReport.setFileStatus("0");
        //不文明行为标记
        tblResponseXmReport.setUncivilizedBehaviorFlag(faultCameraModule.getUncivilizedBehaviorFlag());
        tblResponseXmReport.setFaultId(faultCameraModule.getId());
        tblResponseXmReport.setCreateTime(new Date());
        tblResponseXmReport.setStartTime(faultCameraModule.getStartTime());
        tblResponseXmReport.setEndTime(faultCameraModule.getEndTime());
        tblResponseXmReport.setSerialNumber(elevatorCode);
        tblResponseXmReport.setElevatorCode(elevatorCode);
        Integer report = tblResponseXmReportDao.insertResponeXmReport(tblResponseXmReport);
        if (report == 1) {
            log.info("--- getCameraXmHistory --- API服务请求 --- 添加记录表 --- 成功 ---");
            TblResponseXmReport info = tblResponseXmReportDao.findResponeXmReportByFaultId(faultCameraModule.getId());
            return Result.success(info, "开始下载，请稍后查看");
        } else {
            log.info("--- getCameraXmHistory --- API服务请求 --- 添加记录表 --- 失败 ---");
            return Result.error("处理失败");
        }
    }


    @Override
    public Result getVedioHlsForHttpsOrHttp(XmHlsHttpOrHttpsModule xmHlsHttpOrHttpsModule) {
        List<TblCamera> cameras = bizCameraDao.getByvCloudNumber(xmHlsHttpOrHttpsModule.getvCloudNumber());
        if (cameras.size() == 0) {
            return Result.error("序列号配置错误");
        }
        TblCamera camera = cameras.get(0);
        String vHlsUrl = null;
        if (camera != null) {
            //雄迈获取实时的Hls流
            if (camera.getICameraType() == CameraConstants.CameraType.XIONGMAI.getType()) {
                vHlsUrl = CamerasUtils.getXiongMaiHls(camera.getVCloudNumber(), xmHlsHttpOrHttpsModule.getElevatorCode(), xmHlsHttpOrHttpsModule.getType());
            } else {
                vHlsUrl = camera.getVHlsUrl();
            }
            return Result.success(new VedioResponse(vHlsUrl), "获取视频流成功");
        }
        return Result.error(xmHlsHttpOrHttpsModule.getElevatorCode() + "暂无绑定摄像头信息");
    }


    @Override
    public Object getByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            log.info("请输入麦信设备编号或电梯注册码");
            return Result.error("请输入麦信设备编号或电梯注册码");

        }
        // 根据注册码查询电梯code
        ElevatorDetailModule elevator = bizElevatorDao.getElevatorByEquipmentCode(code);
        String elevatorCode = (null == elevator) ? code : elevator.getElevatorCode();
        TblCamera tblCamera = bizCameraDao.getByElevatorCode(elevatorCode);
        return getCamera(tblCamera);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object saveAndTest(TblCamera tblCamera) {
        if (StringUtils.isEmpty(tblCamera.getVElevatorCode())) {
            return ResponseResult.error();
        }
        // 根据注册码查询电梯code
        TblCamera cameraOld = bizCameraDao.getByElevatorId(tblCamera.getVElevatorId());
        if (cameraOld != null) {
            tblCamera.setVCameraId(cameraOld.getVCameraId());
            bizCameraDao.updateCamera(tblCamera);
        } else {
            tblCamera.setVCameraId(SnowFlakeUtils.nextStrId());
            bizCameraDao.insertCamera(tblCamera);
        }
        TblCamera cameraNew = bizCameraDao.getByElevatorId(tblCamera.getVElevatorId());
        return getCamera(cameraNew);

    }

    @Override
    public String getVedio(String businessNo) {
        String failureNo = businessNo.replace("shmx-", "");
        // 故障录制视频
        String videoUrl = tblSysFileDao.getVideoUrl(failureNo, 2, 1);
        log.info("businessNo:{} " + failureNo + "\n" + "获取故障视频url：{} " + videoUrl + "\n" + "文件类型：{} " + "视频");
        return videoUrl;
    }

    @Override
    public String getPicture(String businessNo) {
        String failureNo = businessNo.replace("shmx-", "");
        // 故障录制图片
        String videoUrl = tblSysFileDao.getVideoUrl(failureNo, 2, 0);
        log.info("businessNo:{} " + failureNo + "\n" + "获取故障视频url：{} " + videoUrl + "\n" + "文件类型：{} " + "图片");
        return videoUrl;
    }

    /**
     * 雄迈回调记录
     *
     * @param fileName
     * @return
     */
    @Override
    public Result xmSaveVideoReport(String fileName) {
        log.info("--- xmSaveVideoReport --- 雄迈回调 --- 更新记录表 --- Url地址 ---");
        //1.判断当前故障id在记录表中是否存在
        if (fileName.isEmpty()) {
            log.info("--- xmSaveVideoReport --- 雄迈回调 --- 更新记录表 --- Url地址 --- url为空 ---");
            return Result.error("下载故障视频url为空！");
        }

        String faultId = VideoUtils.videoGetFaultId(fileName);
        TblResponseXmReport oldReport = tblResponseXmReportDao.findResponeXmReportByFaultId(faultId);
        if (oldReport == null) {
            log.info("--- xmSaveVideoReport --- 雄迈回调 --- 更新记录表 --- Url地址 --- 记录表中暂无该故障 --- faultId：{} " + faultId);
            return Result.error("记录表中暂无此故障id！");
        }
        //2.记录存在的话，保存url即可，通过定时任务去处理图片上传
        oldReport.setUrl(fileName);
        //1：请求雄迈 2：雄迈回调
        oldReport.setActionType("2");
        oldReport.setModifyTime(new Date());
        //0：未下载 1：下载成功 2：下载中 3：下载失败 4：请求失败
        oldReport.setFileStatus("2");

        Integer report = tblResponseXmReportDao.updateResponeXmReport(oldReport);
        if (report == 1) {
            TblResponseXmReport newReport = tblResponseXmReportDao.findResponeXmReportByFaultId(faultId);
            log.info("--- xmSaveVideoReport --- 雄迈回调 --- 更新记录表 --- Url地址 --- 成功 --- " + "faultId：{} " + faultId + "fileName:{} " + fileName);
            return Result.success(newReport, "更新下载记录文件地址成功");
        } else {
            log.info("--- xmSaveVideoReport --- 雄迈回调 --- 更新记录表 --- Url地址 --- 失败 --- " + "faultId：{} " + faultId + "fileName:{} " + fileName);
            return Result.error("更新下载记录文件地址失败");
        }
    }

    /**
     * 雄迈上传视频逻辑
     *
     * @param fileName
     * @return
     */
    @Override
    public Result xmSaveVideo(String fileName) {

        if (fileName.isEmpty()) {
            return Result.error("下载故障视频文件名为空！");
        }

        String faultId = VideoUtils.videoGetFaultId(fileName);

        TblResponseXmReport oldReport = tblResponseXmReportDao.findResponeXmReportByFaultId(faultId);
        if (oldReport == null) {
            log.info("--- 下载视频 --- 失败! 记录表中暂无此故障id！--- fileName: {}", fileName);
            return Result.error("记录表中暂无此故障id！");
        }

        // h264文件转MP4 并上传oss
        String MP4Path = VideoUtils.h264ToMp4(fileName);
        log.info("--- 故障[{}]取证视频--- 取证视频上传阿里OSS --- 上传结束！ MP4Path: {}", faultId, fileName);
        if (StringUtils.isEmpty(MP4Path)) {// 上传失败,重新取证
            //0：待下载 1：下载成功 2：下载中（请求成功） 3：请求失败（等待重试） 4：文件上传阿里解析失败
            oldReport.setActionType("1");
            oldReport.setFileStatus("3");
            oldReport.setModifyTime(new Date());
            oldReport.setUploadFailedNum(oldReport.getUploadFailedNum() + 1);
            tblResponseXmReportDao.updateResponeXmReport(oldReport);
            VideoUtils.deleteFile(fileName);
            log.info("--- 故障[{}]取证视频--- 取证视频上传阿里OSS --- 失败！删除本地临时文件 --- {}", faultId, fileName);
            return Result.error("文件解析错误，暂停保存！");
        } else {
            // 记录到系统文件表
            saveVideoFile(MP4Path, faultId);
            //上传：2
            oldReport.setActionType("2");
            //0：待下载 1：下载成功 2：下载中 3：下载失败 4：请求失败
            oldReport.setFileStatus("1");
            oldReport.setModifyTime(new Date());
            tblResponseXmReportDao.updateResponeXmReport(oldReport);
            // 删除文件
            VideoUtils.deleteFile(fileName);
            log.info("--- 故障[{}]取证视频--- 取证视频上传阿里OSS --- 成功！阿里云OSS路径[{}],  删除本地临时文件 --- {}", faultId, MP4Path, fileName);
            return Result.success("故障视屏保存成功");
        }
    }

    /**
     * 海康上传视频或图片逻辑
     *
     * @param value
     * @return
     */
    @Transactional
    @Override
    public Result hkSaveVideo(String value) {
        log.info("--- hkSaveVideo --- camera服务 --- kafuka消费者 --- 开始 ---");
        JSONObject jsonObject = JSONObject.parseObject(value);

        // 沿用之前代码，实际传输的是故障id
        String faultId = jsonObject.getString("workOrderId");

        String fileName = jsonObject.getString("fileName");
        Integer photoCount = jsonObject.getInteger("photoCount");
        String date = jsonObject.getString("date");

        String photoName = fileName.substring(0, fileName.lastIndexOf("."));

        Date now = new Date();
        Set<TblSysFile> setList = new HashSet<>();

        for (int i = 1; i <= photoCount; i++) {
            TblSysFile file = new TblSysFile();
            String id = SnowFlakeUtils.nextStrId();
            file.setVFileId(id);
            file.setVBusinessId(faultId);
            file.setIBusinessType(2);
            file.setVFileType(String.valueOf(0));
            file.setVFileName(photoName + "-" + i + ".jpg");
            file.setVUrl(OSSUtil.OSS_URL + OSSUtil.OREO_PROJECT_FAULT_URL + date + File.separator + photoName + "-" + i + ".jpg");
            file.setDtCreateTime(now);
            setList.add(file);
        }

        TblSysFile file = new TblSysFile();
        String id = SnowFlakeUtils.nextStrId();
        file.setVFileId(id);
        file.setVBusinessId(faultId);
        file.setIBusinessType(2);
        file.setVFileType(String.valueOf(1));
        file.setVFileName(fileName);
        file.setVUrl(OSSUtil.OSS_URL + OSSUtil.OREO_PROJECT_FAULT_URL + date + File.separator + fileName);
        file.setDtCreateTime(now);
        setList.add(file);
        List<TblSysFile> newList = new ArrayList<>(setList);
        tblSysFileDao.insertFileBatch(newList);
        log.info("--- hkSaveVideo --- camera服务 --- kafuka消费者 --- 文件保存完成 ---");
        return Result.success("海康摄像头信息保存成功");
    }


    @Override
    public void xmSaveImage(String filePath) {

        if (filePath.isEmpty()) {
            return;
        }

        String faultId = VideoUtils.imageGetFaultId(filePath);

        // 获取图片list
        List<String> imageList = VideoUtils.getImageFileList(filePath, faultId);
        if (CollectionUtils.isEmpty(imageList)) {
            log.info("--- 下载故障图片 --- 失败...文件名不能为空！--- {}", filePath);
            return;
        }

        // 上传OSS
        List<String> ossFileUrlList = new ArrayList<>();
        List<String> saveSuccessFileList = new ArrayList<>();
        for (String file : imageList) {
            // 上传文件到阿里云OSS
            String ossFileUrl = OSSUtil.saveFaultMP4(file);
            if (StringUtils.isEmpty(ossFileUrl)) {
                continue;
            }

            //非平层停梯，图片上传阿里云之后调用二次识别是否困人
            String faultType = bizElevatorDao.getFaultTypeByFaultId(faultId);
            if ("6".equals(faultType)) {
                // restTemplateSendMessage(faultId, OSSUtil.OSS_URL + ossFileUrl, "person");
                ImageIdentifyUtils.identifyImage(faultId, OSSUtil.OSS_URL + ossFileUrl,
                        ImageIdentifyUtils.IDENTIFY_TYPE_PERSON, aliOssProperties.getUseInternal());
                log.info("非平层停梯调用二次识别确认，故障id【{}】", faultId);
            }

            //平层困人
            if ("7".equals(faultType) || "8".equals(faultType)) {

                Long remove = redisTemplate.opsForZSet().remove(RedisConstants.HLS_IMAGE, faultId);

                if (remove > 0) {

                    try {
                        //图片下载成功释放锁
                        RLock lock = redissonClient.getLock(RedisConstants.AFRESDOWNLOADIMAGE_LOCK + faultId);
                        lock.forceUnlock();
                        log.info("---------平层困人图片下载成功，释放锁------------");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //调用图像识别
                    //restTemplateSendMessage(faultId, OSSUtil.OSS_URL + ossFileUrl, "person");
                    ImageIdentifyUtils.identifyImage(faultId, OSSUtil.OSS_URL + ossFileUrl,
                            ImageIdentifyUtils.IDENTIFY_TYPE_PERSON, aliOssProperties.getUseInternal());
                }
                //添加识别标识
                redisTemplate.opsForZSet().add(RedisConstants.HLS_IDENTIFICATION, faultId, System.currentTimeMillis() / 1000);
            }

            ossFileUrlList.add(ossFileUrl);
            saveSuccessFileList.add(file);
        }
        // 批量保存数据库
        this.saveImageFile(ossFileUrlList, faultId);
        // 删除以保存到数据库成功的文件
        for (String file : saveSuccessFileList) {
            VideoUtils.deleteFile(file);
        }
        log.info("雄迈保存图片成功 ！ 故障id：{} ", faultId);
    }

    @Override
    public Object ezSaveVideo(String filePath) {
        String faultId = VideoUtils.videoGetFaultId(filePath);
        // 上传OSS
        String fileUrl = OSSUtil.saveFaultMP4(filePath);
        if (StringUtils.isEmpty(fileUrl)) {
            return null;
        }
        // 落库
        this.saveVideoFile(fileUrl, faultId);
        // 删除文件
        VideoUtils.deleteFile(filePath);
        log.info("海康保存视频成功! 故障id：{} ", faultId);
        return null;
    }

    @Override
    public Object ezSaveImage(String filePath) {
        String faultId = VideoUtils.videoGetFaultId(filePath);

        // 获取图片list
        List<String> imageList = VideoUtils.makeScreenCut(filePath);
        if (CollectionUtils.isEmpty(imageList)) {
            return null;
        }

        // 上传OSS
        List<String> ossFileUrlList = new ArrayList<>();
        for (String file : imageList) {
            String ossFileUrl = OSSUtil.saveFaultMP4(file);
            if (StringUtils.isEmpty(ossFileUrl)) {
                continue;
            }
            ossFileUrlList.add(ossFileUrl);
            // 删除文件
            VideoUtils.deleteFile(file);
        }
        // 上传OSS
        String fileUrl = OSSUtil.saveFaultMP4(filePath);
        if (StringUtils.hasText(fileUrl)) {
            // 落库
            this.saveVideoFile(fileUrl, faultId);
            // 删除文件
            VideoUtils.deleteFile(filePath);
        }
        // 落库
        this.saveImageFile(ossFileUrlList, faultId);
        log.info("海康保存视频成功 " + "\n" + "故障id：{} " + faultId);
        return null;
    }


    /**
     * 添加文件上传记录
     *
     * @param tblResponseXmReport
     * @return
     */
    @Override
    public Result insertResponeXmReport(TblResponseXmReport tblResponseXmReport) {
        if (tblResponseXmReport.getFaultId() == null) {
            return Result.error("故障id不能为空");
        }
        TblResponseXmReport reportInfo = tblResponseXmReportDao.findResponeXmReportByFaultId(tblResponseXmReport.getFaultId());
        if (reportInfo != null) {
            return Result.error("该记录已存在，不能重复添加");
        }

        Integer report = tblResponseXmReportDao.insertResponeXmReport(tblResponseXmReport);
        if (report == 1) {
            TblResponseXmReport ReportInfo = tblResponseXmReportDao.findResponeXmReportByFaultId(tblResponseXmReport.getFaultId());
            return Result.success(ReportInfo, "添加请求雄迈服务记录成功");
        } else {
            return Result.error("添加请求雄迈服务记录失败");
        }


    }

    /**
     * 更新文件上传记录
     *
     * @param tblResponseXmReport
     * @return
     */
    @Override
    public Result updateResponeXmReport(TblResponseXmReport tblResponseXmReport) {
        if (tblResponseXmReport.getFaultId() == null) {
            return Result.error("故障id不能为空");
        }
        TblResponseXmReport oldReportInfo = tblResponseXmReportDao.findResponeXmReportByFaultId(tblResponseXmReport.getFaultId());
        oldReportInfo.setFaultId(tblResponseXmReport.getFaultId());
        oldReportInfo.setElevatorCode(tblResponseXmReport.getElevatorCode());
        oldReportInfo.setActionType(tblResponseXmReport.getActionType());
        oldReportInfo.setFileType(tblResponseXmReport.getFileType());
        oldReportInfo.setReturnCode(tblResponseXmReport.getReturnCode());
        oldReportInfo.setStartTime(tblResponseXmReport.getStartTime());
        oldReportInfo.setEndTime(tblResponseXmReport.getEndTime());
        oldReportInfo.setSerialNumber(tblResponseXmReport.getSerialNumber());
        oldReportInfo.setUncivilizedBehaviorFlag(tblResponseXmReport.getUncivilizedBehaviorFlag());
        oldReportInfo.setFileStatus(tblResponseXmReport.getFileStatus());
        oldReportInfo.setModifyTime(new Date());
        oldReportInfo.setUrl(tblResponseXmReport.getUrl());

        Integer report = tblResponseXmReportDao.updateResponeXmReport(tblResponseXmReport);
        if (report == 1) {
            TblResponseXmReport newReportInfo = tblResponseXmReportDao.findResponeXmReportByFaultId(tblResponseXmReport.getFaultId());
            return Result.success(newReportInfo, "更新请求雄迈服务记录成功");
        } else {
            return Result.error("更新请求雄迈服务记录失败");
        }

    }

    /**
     * 根据文件名（路径）获取当前记录
     *
     * @param url
     * @return
     */
    @Override
    public TblResponseXmReport findResponeXmReportByUrl(String url) {
        return tblResponseXmReportDao.findResponeXmReportByUrl(url);
    }

    /**
     * 扫描待取证的记录 发生请求到摄像头
     *
     * @return
     */
    @Override
    public void requestXmTask() {

        // 视频取证状态： 0：待下载 1：下载成功 2：下载中（请求成功） 3：请求失败（等待重试） 4：文件上传阿里解析失败

        // 查询数据库中需要下载取证视频的记录（包括：待下载[0], 下载失败[3], 下载中[2]超时 ）
        // 单个摄像头每次只取一条记录
        List<TblResponseXmReport> requestXmReportList = tblResponseXmReportDao.findNeedDownloadVedioList();

        if (requestXmReportList.size() > 0) {
            requestXmReportList.forEach(task -> {
                try {
                    if (StringUtils.isEmpty(task.getFaultId())) {
                        return;
                    }
                    String faultType = null;
                    try {
                        //获取故障类型
                        faultType = bizElevatorDao.getFaultTempTypeByFaultId(task.getFaultId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Integer code;
                    // 通知摄像头服务拉取视频（直接返回：验证成功触发下载[200]， 摄像头未在线[300], 服务异常[500]）
                    if ("7".equals(faultType) || "8".equals(faultType)) {
                        code = CameraUtils.saveLongXiongLMaiHistoryVideo(task.getFaultId(), task.getSerialNumber(), task.getStartTime(), task.getEndTime());
                    } else {
                        code = CameraUtils.saveXiongMaiHistoryVideo(task.getFaultId(), task.getSerialNumber(), task.getStartTime(), task.getEndTime());
                    }

                    if (code == 200 || code == 302) {// 设备下载开始/下载中
                        task.setFileStatus("2");// 下载中
                        task.setComment("下载中");
                        task.setModifyTime(new Date());
                    } else {// 请求失败场景
                        task.setModifyTime(new Date());
                        task.setFileStatus("3");// 请求失败
                        task.setReturnCode(code);
                        task.setRequestFailedNum(task.getRequestFailedNum() + 1); // 失败次数 +1
                        if (code == 300) {// 设备不在线
                            task.setComment("摄像头设备不在线");
                        } else if (code == 301) {// 设备下载开始/下载中
                            task.setComment("摄像头ID无法匹配");
                        } else if (code == 303) {// 设备下载开始/下载中
                            task.setComment("录像开始时间 - 结束时间不符合规范, 时间长度过大或小于0");
                        } else {
                            task.setComment("未知错误！");
                        }
                    }
                    tblResponseXmReportDao.updateResponeXmReport(task);
                } catch (Exception e) {
                    log.error("scheduled save XiongMai History Video error! message :" + e.getMessage());
                }
            });
        }
    }

    /**
     * 扫描上传阿里OSS的记录 上传并更新状态
     */
    @Override
    public void responeXmTask() {

        //获取记录表中 雄迈回调后需要上传的文件
        List<TblResponseXmReport> fileReportInfo = tblResponseXmReportDao.findNeedUploadOSSVedioList();
        if (fileReportInfo.size() > 0) {
            fileReportInfo.forEach(report -> {
                if (StringUtils.isEmpty(report.getUrl())) {
                    return;
                }
                this.xmSaveVideo(report.getUrl());
            });
        }
    }

    @Override
    public TblResponseXmReport findResponeXmReportByFaultId(String faultId) {
        return tblResponseXmReportDao.findResponeXmReportByFaultId(faultId);
    }


    @Override
    public Object getCameraInfoByElevatorId(String elevatorId) {
        TblCamera tblCamera = bizCameraDao.getByElevatorId(elevatorId);
        return getCamera(tblCamera);
    }

    @Override
    public CameraModule getCamera(TblCamera tblCamera) {
        if (null == tblCamera) {
            return null;
        }
        CameraModule cameraModule = new CameraModule();
        BeanUtils.copyProperties(tblCamera, cameraModule);
        if (cameraModule.getiCameraType() != null && cameraModule.getiCameraType() == CameraConstants.CameraType.HAIKANG.getType()) {
            // 摄像头类型 1：海康，2：雄迈   海康萤石云需要token访问
            cameraModule.setToken(this.getEzopenToken());
        }
        if (cameraModule.getiCameraType() != null && cameraModule.getiCameraType() == CameraConstants.CameraType.XIONGMAI.getType()) {
            // 摄像头类型 1：海康，2：雄迈   雄迈hls流获取
            String hlsUrl;
            String redisHlsUrl = redisService.getXiongMaiHlsUrl(tblCamera.getVElevatorCode());
            if (StringUtils.hasText(redisHlsUrl)) {
                hlsUrl = redisHlsUrl;
                System.out.printf("%s --- 获取雄迈byRedis %s --- %s --- %s\n",
                        TimeUtils.nowTime(), tblCamera.getVCloudNumber(), tblCamera.getVElevatorCode(), hlsUrl);
            } else {
                hlsUrl = CameraUtils.getXiongMaiHls(tblCamera.getVCloudNumber(), tblCamera.getVElevatorCode());
                redisService.setXiongMaiHlsUrl(tblCamera.getVElevatorCode(), hlsUrl);
            }
            cameraModule.setvHlsUrl(hlsUrl);
        }
        return cameraModule;
    }

    @Override
    public TblCamera getByElevatorCode(String code) {
        return bizCameraDao.getByElevatorCode(code);
    }

    /**
     * 获取萤石云token
     */
    @Override
    public String getEzopenToken() {
        // 拼接请求参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CameraConstants.Ezopen.APP_TOKEN_URL)
                .queryParam("appKey", CameraConstants.Ezopen.APP_KEY)
                .queryParam("appSecret", CameraConstants.Ezopen.APP_SECRET);

        Map<String, Object> queryMap = new HashMap<>();
        ResponseEntity<String> map = RequestUtil.sendPost(builder, queryMap);
        JSONObject body = JSONObject.parseObject(map.getBody());
        return body.getJSONObject("data").getString("accessToken");
    }

    /**
     * 保存文件路径
     *
     * @param fileName 文件路径
     * @param faultId  关联的故障id
     */
    @Override
    public TblSysFile saveVideoFile(String fileName, String faultId) {

        // 故障对应的取证视频只能存一个
        TblSysFile fileInfo = tblSysFileDao.getFileByFaultIdAnd(faultId, "1");
        if (null != fileInfo) {
            return fileInfo;
        }

        TblSysFile file = new TblSysFile();
        file.setVFileId(SnowFlakeUtils.nextStrId());
        file.setVFileType(String.valueOf(1));
        file.setVFileName(fileName);
        file.setVUrl(OSSUtil.OSS_URL + fileName);
        file.setDtCreateTime(new Date());
        file.setDtModifyTime(new Date());
        file.setIBusinessType(2);
        file.setVBusinessId(faultId);
        tblSysFileDao.insert(file);

        return tblSysFileDao.getFileByFaultIdAnd(faultId, "1");
    }

    @Override
    public void saveImageFile(List<String> fileNameList, String faultId) {
        TblSysFile file = new TblSysFile();
        file.setDtCreateTime(new Date());
        file.setDtModifyTime(new Date());
        file.setIBusinessType(2);
        file.setVFileType(String.valueOf(0));
        file.setVBusinessId(faultId);

        fileNameList.forEach(value -> {
            file.setVFileId(SnowFlakeUtils.nextStrId());
            file.setVFileName(value);
            file.setVUrl(OSSUtil.OSS_URL + value);
            int result = tblSysFileDao.insert(file);
        });
    }

    /**
     * Socket服务调用：根据电梯编号 获取rtmp流地址
     *
     * @param elevatorCode 电梯编号
     * @return rtmp流地址
     */
    @Override
    public String getRtmpUrlByElevatorCode(String elevatorCode) {
        return bizCameraDao.getRtmpUrlByElevatorCode(elevatorCode);
    }

    /**
     * Socket服务调用：通过电梯编号 获取hls流地址
     *
     * @param elevatorCode 电梯编号
     * @return hls流地址
     */
    @Override
    public String getHlsUrlByElevatorCode(String elevatorCode) {
        return bizCameraDao.getRtmpUrlByElevatorCode(elevatorCode);
    }

    /**
     * Socket服务调用：截取摄像头当前一帧图片，返回图片存储路径
     *
     * @param elevatorCode 电梯编号
     * @return 图片存储路径
     */
    @Override
    public String getCurrentImagePathByElevatorCode(String elevatorCode) {
        TblCamera camera = bizCameraDao.getByElevatorCode(elevatorCode);
        if (camera == null) {
            System.out.printf("%s --- %s --- 困人 --- 视频流不存在\n", TimeUtils.nowTime(), elevatorCode);
            return "";
        }
        String path = "/shmashine-deploy/java-oreo/socket/image/" + TimeUtils.getTenBitTimestamp() + ".jpg";
//        String path = "D:\\Desktop\\新建文件夹\\" + TimeUtils.getTenBitTimestamp() + ".jpg"; // 本地TEST
        if (camera.getICameraType() == 2) {
            // 雄迈摄像头
            String hlsUrl;
            String redisHlsUrl = redisService.getXiongMaiHlsUrl(elevatorCode);
            if (StringUtils.hasText(redisHlsUrl)) {
                hlsUrl = redisHlsUrl;
                System.out.printf("%s --- 获取雄迈byRedis %s --- %s --- %s\n",
                        TimeUtils.nowTime(), camera.getVCloudNumber(), camera.getVElevatorCode(), hlsUrl);
            } else {

                //调用-雄迈摄像-头次数累加
                redisTemplate.opsForHash().increment(XM_NUMBER_OF_CALLS_CAMERSERVER, camera.getVElevatorCode(), 1);

                hlsUrl = CameraUtils.getXiongMaiHls(camera.getVCloudNumber(), elevatorCode);
                redisService.setXiongMaiHlsUrl(elevatorCode, hlsUrl);
            }

            System.out.printf("%s --- %s --- 困人 --- Baidu截取图片\n存储路径: %s \nsourceUrl: %s\n", TimeUtils.nowTime(), elevatorCode, path, hlsUrl);

            // 重试三次
            int flag = 3;
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(hlsUrl);
                    ff.start();
                    doExecuteFrame(ff.grabImage(), path);
                    ff.stop();
                    break;
                } catch (FrameGrabber.Exception | InterruptedException e) {
                    System.out.printf("%s --- %s --- 困人 --- 打开流失败...重试%s...\n", TimeUtils.nowTime(), elevatorCode, flag);
                    flag--;
                }
                if (flag == 0) {
                    System.out.printf("%s --- %s --- 困人 --- 重试3次失败...\n", TimeUtils.nowTime(), elevatorCode);
                    return "";
                }
            }
        }
        if (StringUtils.hasText(camera.getVRtmpUrl())) {
            // rtmp流处理
            String rtmpUrl = camera.getVRtmpUrl();
            System.out.printf("%s --- %s --- 困人 --- Baidu截取图片\n存储路径: %s \nsourceUrl: %s\n", TimeUtils.nowTime(), elevatorCode, path, rtmpUrl);
            // 截取视频图片
            String command = "ffmpeg -i " + rtmpUrl + " -vframes 1 -y -f image2 -t 0.001 " + path;
            try {
                Process process = Runtime.getRuntime().exec(command);
                process.waitFor();
                System.out.printf("%s --- %s --- 困人 --- Baidu识别中...\n", TimeUtils.nowTime(), elevatorCode);
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }
        }
        File file = new File(path);
        if (file.exists()) {
            return path;
        }
        return "";

    }


    @Override
    public TblSysFileDao getTblSysFileDao() {
        return tblSysFileDao;
    }

    @Override
    public TblSysFile getById(String vFileId) {
        return tblSysFileDao.getById(vFileId);
    }

    @Override
    public List<TblSysFile> getByEntity(TblSysFile tblSysFile) {
        return tblSysFileDao.getByEntity(tblSysFile);
    }

    @Override
    public List<TblSysFile> listByEntity(TblSysFile tblSysFile) {
        return tblSysFileDao.listByEntity(tblSysFile);
    }

    @Override
    public List<TblSysFile> listByIds(List<String> ids) {
        return tblSysFileDao.listByIds(ids);
    }

    @Override
    public int insertFile(TblSysFile tblSysFile) {
        Date date = new Date();
        tblSysFile.setDtCreateTime(date);
        tblSysFile.setDtModifyTime(date);
        return tblSysFileDao.insert(tblSysFile);
    }

    @Override
    public int insertCamera(TblCamera tblCamera) {
        Date date = new Date();
        tblCamera.setDtCreateTime(date);
        tblCamera.setDtModifyTime(date);
        return bizCameraDao.insertCamera(tblCamera);
    }

    @Override
    public void insertWorkOrderBatch(List<String> list, String workOrderDetailId) {

        for (String fileName : list) {
            TblSysFile file = new TblSysFile();
            file.setVFileId(SnowFlakeUtils.nextStrId());
            file.setVFileType(String.valueOf(0));
            file.setVFileName(fileName);
            file.setVUrl(OSSUtil.OSS_URL + fileName);
            file.setDtCreateTime(new Date());
            file.setDtModifyTime(new Date());
            file.setIBusinessType(1);
            file.setVBusinessId(workOrderDetailId);
            tblSysFileDao.insert(file);
        }
    }

    @Override
    public int updateFile(TblSysFile tblSysFile) {
        tblSysFile.setDtModifyTime(new Date());
        return tblSysFileDao.update(tblSysFile);
    }

    @Override
    public int updateCamera(TblCamera tblCamera) {
        tblCamera.setDtModifyTime(new Date());
        return bizCameraDao.updateCamera(tblCamera);
    }


    @Override
    public int updateBatch(List<TblSysFile> list) {
        return tblSysFileDao.updateBatch(list);
    }

    @Override
    public int deleteById(String vFileId) {
        return tblSysFileDao.deleteById(vFileId);
    }

    @Override
    public int deleteByEntity(TblSysFile tblSysFile) {
        return tblSysFileDao.deleteByEntity(tblSysFile);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblSysFileDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblSysFileDao.countAll();
    }

    @Override
    public int countByEntity(TblSysFile tblSysFile) {
        return tblSysFileDao.countByEntity(tblSysFile);
    }

    @Override
    public ResponseEntity<String> downloadCameraFileByElevatorCode(CamareMediaDownloadRequestDTO request) {
        var camera = bizCameraDao.getByElevatorCode(request.getElevatorCode());
        if (camera == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到该电梯的摄像头信息");
        }
        // 急救急修的需要发送结果到维保平台
        if (CameraTaskTypeEnum.EMERGENCY_REPAIR_FIRST_AID.equals(request.getTaskType())) {
            var faultMessage = FaultMessage.builder()
                    .elevatorCode(request.getElevatorCode())
                    .TY("Fault")
                    .ST("add")
                    .faultId(request.getTaskCustomId())
                    .fault_type("0")
                    .faultName("急救急修")
                    .uncivilizedBehaviorFlag(0)
                    .sensorType("SINGLEBOX")
                    .time(DateUtil.now())
                    .build();
            saveSenderFault(request.getTaskCustomId(), request.getElevatorCode(), "maintenance", false, faultMessage);
        }
        var cameraType = camera.getICameraType();
        var cameraTypeEnum = CameraTypeEnum.getByCode(cameraType);
        return switch (cameraTypeEnum) {
            case HIK_EZVIZ -> hikEzvizClient.downloadCameraFileByElevatorCode(request);
            case HIK_CLOUD -> hikCloudClient.downloadCameraFileByElevatorCode(request);
            case TYYY -> tyslDownloadCameraFileByElevatorCode(request);
            case TYBD -> tyslDownloadCameraFileByElevatorCode(request);
            default -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("不支持的摄像头类型 " + cameraType);
        };
    }

    @Override
    public TblCameraCascadePlatformEntity queryCameraInfoByChannelCode(String channelCode) {

        return bizCameraDao.queryCameraInfoByChannelCode(channelCode);
    }

    @Override
    public ResponseResult getVoiceWssInfoByElevatorCode(String elevatorCode, Integer domain) {
        var camera = bizCameraDao.getByElevatorCode(elevatorCode);
        // 未配置摄像头直接返回
        if (camera == null) {
            // 该电梯未安装摄像头
            return new ResponseResult(ResponseResult.CODE_VIDEO_ERROR, "msg_vedio_01");
        }
        return getVoiceWssInfoByCamera(camera, domain);
    }

    @Override
    public ResponseResult getVoiceWssInfoByCloudNumber(String cloudNumber, Integer domain) {
        var cameras = bizCameraDao.getByvCloudNumber(cloudNumber);
        // 未配置摄像头直接返回
        if (CollectionUtils.isEmpty(cameras)) {
            // 该电梯未安装摄像头
            return new ResponseResult(ResponseResult.CODE_VIDEO_ERROR, "msg_vedio_01");
        }
        var camera = cameras.get(0);
        return getVoiceWssInfoByCamera(camera, domain);
    }


    /**
     * javacv 截取图片
     */
    public static void doExecuteFrame(Frame frame, String fileName) {
        if (null == frame || null == frame.image) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        String imageMat = "jpg";
        BufferedImage bi = converter.getBufferedImage(frame);
        File output = new File(fileName);
        try {
            ImageIO.write(bi, imageMat, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用天翼平台生成直播流地址
     *
     * @param tenantNo  租户唯一识别码
     * @param tenantKey 租户密钥
     * @param deviceNo  设备编号
     * @return
     */
    private String getUrl(String tenantNo, String tenantKey, String deviceNo) {

        String url = "http://101.91.204.40:10090/viot-openapi/openapi/GBDevApi/V2/media/play";

        String time = DateUtil.format(DateTime.now(), DatePattern.PURE_DATETIME_PATTERN);
        StringBuilder signSb = new StringBuilder();
        String singStr = signSb.append(tenantNo + time + tenantKey).toString();
        String sign = DigestUtils.md5DigestAsHex(singStr.getBytes());
        RestTemplate restTemplate = new RestTemplate();

        Map reqMap = new HashMap();
        reqMap.put("deviceNo", deviceNo);

        var reqBody = new JSONObject();
        reqBody.put("tenantNo", tenantNo);
        reqBody.put("sign", sign);
        reqBody.put("time", time);
        reqBody.put("req", reqMap);


        System.out.println("reqParam>>>>>>>" + JSON.toJSONString(reqBody));

        String result = null;
        try {
            result = restTemplate.postForObject(url, reqBody, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return result;
    }


    /*private void restTemplateSendMessage(String workOrderId, String fileUrl, String type) {
        //拼接请求参数
        String url = "http://47.105.214.0:10089/?type=" + type + "&url=" + fileUrl + "&faultId=" + workOrderId;

        //异步请求
        try {
            HttpRequest.get(url).timeout(200).executeAsync();
        } catch (Exception e) {
            // 调用超时异常
        }

    }*/

    private void saveSenderFault(String taskCustomId, String elevatorCode, String ptCode, Boolean entrap, FaultMessage message) {
        var senderFaultEntity = TblSenderFaultEntity.builder()
                .faultId(message.getFaultId())
                .elevatorCode(message.getElevatorCode())
                .pushGovern(ptCode)
                .entrap(entrap ? 1 : 0)
                .faultMessage(JSON.toJSONString(message))
                .finished(0)
                .build();
        senderFaultMapper.insert(senderFaultEntity);
    }

    private ResponseEntity<String> tyslDownloadCameraFileByElevatorCode(CamareMediaDownloadRequestDTO request) {
        var res = tyslClient.downloadCameraFileByElevatorCode(request);
        if (null == res) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("未能获取到返回结果");
        }
        if (HttpStatus.OK.value() != res.getCode()) {
            return ResponseEntity.status(res.getCode()).body(res.getData());
        }
        return ResponseEntity.ok(res.getData());
    }

    /**
     * 根据摄像头信息 获取语音对讲功能
     *
     * @param camera 摄像头信息
     * @param domain 1:域名 0：IP+端口
     * @return 结果
     */
    private ResponseResult getVoiceWssInfoByCamera(TblCamera camera, Integer domain) {
        var res = switch (CameraTypeEnum.getByCode(camera.getICameraType())) {
            case TYYY -> tyslClient.getVoiceWssInfo(camera.getVElevatorCode(), domain);
            //case HIK_EZVIZ ->
            default -> ResponseCustom.buildFailed(400, "NOT SUPPORT for " + camera.getVElevatorCode()
                    + ", cloudNumber:" + camera.getVCloudNumber()
                    + ", cameraType:" + camera.getICameraType());
        };
        return res.getCode().equals(HttpStatus.OK.value())
                ? ResponseResult.successObj(res.getData())
                : ResponseResult.error(res.getMessage());
    }

}
