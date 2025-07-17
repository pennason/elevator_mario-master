// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.service.impl;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.base.CharMatcher;
import com.shmashine.cameratysl.client.dto.ResponseCustom;
import com.shmashine.cameratysl.client.dto.requests.FaultForHistoryPhotoVideoRequestDTO;
import com.shmashine.cameratysl.convert.CameraMediaDownloadCovertor;
import com.shmashine.cameratysl.dao.TblCameraDownloadTaskMapper;
import com.shmashine.cameratysl.dao.TblCameraExtendInfoMapper;
import com.shmashine.cameratysl.dao.TblCameraImageIdentifyMapper;
import com.shmashine.cameratysl.dao.TblCameraMapper;
import com.shmashine.cameratysl.dao.TblElevatorMapper;
import com.shmashine.cameratysl.dao.TblFaultMapper;
import com.shmashine.cameratysl.dao.TblSysFileMapper;
import com.shmashine.cameratysl.enums.TyslVideoProtoEnum;
import com.shmashine.cameratysl.gateway.TyslGateway;
import com.shmashine.cameratysl.gateway.dto.TyslDevicePlaybackUrlResponseDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePlaybackControlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePlaybackUrlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DeviceStreamUrlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.PageRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.VoiceWssRequestDTO;
import com.shmashine.cameratysl.properties.EndpointProperties;
import com.shmashine.cameratysl.service.CameraServiceI;
import com.shmashine.cameratysl.utils.RedisUtils;
import com.shmashine.cameratysl.videohandle.VideoStreamDownloadHandle;
import com.shmashine.common.dto.CamaraMediaDownloadBaseRequestDTO;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.dto.TblCameraDTO;
import com.shmashine.common.entity.CameraStatusRecordEntity;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;
import com.shmashine.common.entity.TblCameraExtendInfoEntity;
import com.shmashine.common.entity.TblCameraImageIdentifyEntity;
import com.shmashine.common.entity.TblSysFile;
import com.shmashine.common.enums.CameraDownloadFileStatusEnum;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.common.enums.CameraTaskTypeEnum;
import com.shmashine.common.enums.CameraTypeEnum;
import com.shmashine.common.properties.AliOssProperties;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.FileUtil;
import com.shmashine.common.utils.ImageIdentifyUtils;
import com.shmashine.common.utils.OssInternalUtils;
import com.shmashine.common.utils.RedisKeyUtils;
import com.shmashine.mgppf.components.dto.enums.GovernFaultTypeEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 16:59
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CameraServiceImpl implements CameraServiceI {

    private final CameraMediaDownloadCovertor covertor;

    private final RedissonClient redissonClient;
    private final RedisUtils redisUtils;
    private final VideoStreamDownloadHandle videoStreamDownloadHandle;

    private final TyslGateway gateway;

    private final TblCameraMapper cameraMapper;
    private final TblCameraExtendInfoMapper cameraExtendInfoMapper;
    private final TblFaultMapper faultMapper;
    private final TblElevatorMapper elevatorMapper;
    private final TblCameraDownloadTaskMapper cameraDownloadTaskMapper;
    private final TblSysFileMapper sysFileMapper;
    private final TblCameraImageIdentifyMapper cameraImageIdentifyMapper;

    private final AliOssProperties aliOssProperties;
    private final EndpointProperties properties;

    public static final Integer NIGHT_WATCH_EXPIRED_DAYS = 10;
    public static final String PATH_FILE_DOWNLOAD_LOCAL = "/data/camera/download/";

    public static final String REDIS_KEY_NEED_DOWNLOAD_CAMERA_TASK = "CAMERA:NEED_DOWNLOAD_TASK:TYSL";
    public static final String REDIS_KEY_TYSL_PLAYBACK_PRE = "CAMERA:TYSL_PLAYBACK:";
    public static final Long DEFAULT_RECORD_SECONDS = 6L;

    private final ExecutorService executorServiceCheck = new ShmashineThreadPoolExecutor(32, 64,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(3000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService-check"), "CameraService");

    private final ExecutorService executorServiceOss = new ShmashineThreadPoolExecutor(8, 16,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService-Oss"), "CameraService");

    private final ExecutorService executorServiceRetry = new ShmashineThreadPoolExecutor(8, 16,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService-retry"), "CameraService");

    private final ExecutorService executorStopService = new ShmashineThreadPoolExecutor(16, 32,
            6L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(3000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorStopService"), "CameraService");

    private final ExecutorService sendExecutorService = new ShmashineThreadPoolExecutor(8, 64,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("sendExecutorService"), "CameraService");


    //CHECKSTYLE:OFF
    @Override
    public ResponseCustom<String> saveCameraDownloadTask(CamareMediaDownloadRequestDTO request) {
        log.info("saveCameraDownloadTask {}", JSON.toJSONString(request));
        if (request.getTaskCustomType() == null) {
            request.setTaskCustomType(0);
        }

        // 如果有 故障ID，没有 elevatorCode, 则可通过故障ID查询
        if (StringUtils.hasText(request.getTaskCustomId()) && request.getTaskCustomType() > 0) {
            log.info("saveCameraDownloadTask step 1 {}", request.getTaskCustomId());
            // 故障中查询
            var fault = faultMapper.getById(request.getTaskCustomId());
            // 不文明行为中查询
            if (fault == null) {
                log.info("saveCameraDownloadTask step 2 不文明行为 {}", request.getTaskCustomId());
                fault = faultMapper.getByIdFromBehavior37(request.getTaskCustomId());
            }
            if (fault == null) {
                log.info("saveCameraDownloadTask step 3 {} 故障不存在", request.getTaskCustomId());
                return ResponseCustom.buildFailed(404, "不存在的故障ID！");
            }
            request.setElevatorCode(fault.getVElevatorCode());
            request.setStartTime(DateUtil.format(fault.getDtReportTime(), DatePattern.NORM_DATETIME_PATTERN));
            if (null != fault.getFloor()) {
                request.setFloor(String.valueOf(fault.getFloor()));
            }
            log.info("saveCameraDownloadTask step 4 {}", request.getTaskCustomId());
            if (!StringUtils.hasText(request.getStartTime()) || !StringUtils.hasText(request.getEndTime())) {
                var occurTime = fault.getDtReportTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(occurTime);
                Date start;
                Date end;
                if (GovernFaultTypeEnum.ENTRAP.getShmashineCode().equals(String.valueOf(request.getTaskCustomType()))
                        || GovernFaultTypeEnum.ENTRAP2.getShmashineCode()
                        .equals(String.valueOf(request.getTaskCustomType()))) {
                    calendar.add(Calendar.SECOND, -120);
                    start = calendar.getTime();
                    calendar.setTime(occurTime);
                    calendar.add(Calendar.SECOND, +600);
                    end = calendar.getTime();
                } else if (GovernFaultTypeEnum.ELECTRIC_VEHICLE_LADDER.getShmashineCode()
                        .equals(String.valueOf(request.getTaskCustomType()))) {
                    start = calendar.getTime();
                    calendar.setTime(occurTime);
                    calendar.add(Calendar.SECOND, +7);
                    end = calendar.getTime();
                } else {
                    calendar.add(Calendar.SECOND, -15);
                    start = calendar.getTime();
                    calendar.setTime(occurTime);
                    calendar.add(Calendar.SECOND, +45);
                    end = calendar.getTime();
                }
                if (!StringUtils.hasText(request.getStartTime())) {
                    request.setStartTime(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN).format(start));
                }
                if (!StringUtils.hasText(request.getEndTime())) {
                    request.setEndTime(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN).format(end));
                }
            }
        }

        //未安装梯不下载视频
        var isCanDownMedia = elevatorMapper.checkCanDownMedia(request.getElevatorCode());
        if (isCanDownMedia == null || Boolean.FALSE.equals(isCanDownMedia)) {
            log.info("saveCameraDownloadTask step 5 {} 未安装梯不能下载图像或视频", request.getTaskCustomId());
            return ResponseCustom.buildFailed(400, "未安装梯不能下载图像或视频！");
        }

        var camera = cameraMapper.getCameraInfoByElevatorCode(request.getElevatorCode());
        if (camera == null || !StringUtils.hasText(camera.getCloudNumber())) {
            log.info("saveCameraDownloadTask step 6 {} 未获取到摄像头相关信息", request.getTaskCustomId());
            return ResponseCustom.buildFailed(404, "未获取到摄像头相关信息！");
        }
        var cameraDownloadTask = covertor.dto2Entity(request, camera.getCameraType(), camera.getCloudNumber());
        // 存储记录，等待下载
        cameraDownloadTaskMapper.insert(cameraDownloadTask);
        log.info("saveCameraDownloadTask step 7 {} end", request.getTaskCustomId());

        // 记录到 redis 标记有任务需要下载
        redisUtils.setCacheObject(REDIS_KEY_NEED_DOWNLOAD_CAMERA_TASK, "1");
        return ResponseCustom.success("已加入执行队列，请稍等...");
    }
    //CHECKSTYLE:ON

    @Override
    public ResponseCustom<String> removeTaskAndOssFile(Long id) {
        var task = cameraDownloadTaskMapper.getById(id);
        if (task == null) {
            return ResponseCustom.buildFailed(404, "任务不存在（" + id + "）！");
        }
        // 删除OSS文件
        if (StringUtils.hasText(task.getOssUrl())) {
            Arrays.stream(task.getOssUrl().split(",")).toList().forEach(ossUrl -> {
                OssInternalUtils.delFile(ossUrl.trim().replace(OssInternalUtils.OSS_URL, ""),
                        aliOssProperties.getUseInternal());
            });
        }
        // 删除记录
        cameraDownloadTaskMapper.deleteById(id);
        return ResponseCustom.success("操作成功！");
    }

    @Override
    public void checkAndDownloadCameraFile() {
        var tasks = cameraDownloadTaskMapper.getByFileStatus(
                        Collections.singletonList(CameraDownloadFileStatusEnum.WAITING.getStatus()), 20, null)
                .stream()
                .filter(item -> CameraTypeEnum.TYYY.getCode().equals(item.getCameraType())
                        || CameraTypeEnum.TYBD.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        tasks.forEach(task -> executorServiceCheck.submit(() -> {
            doBeginDownloadCameraFileLocal(task);
        }));
    }

    /*@Override
    public void checkAndDoNextDownloadingTask() {
        var tasks = cameraDownloadTaskMapper.getByFileStatus(
                        Collections.singletonList(CameraDownloadFileStatusEnum.DOWNLOADING.getStatus()))
                .stream()
                .filter(item -> CameraTypeEnum.TYYY.getCode().equals(item.getCameraType())
                        || CameraTypeEnum.TYBD.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        tasks.forEach(task -> executorService.submit(() -> {
            doDownloadingCameraFile(task);
        }));
    }*/

    @Override
    public void checkAndDoNextOssUploadTask() {
        var fileStatus = new ArrayList<Integer>() {
            {
                add(CameraDownloadFileStatusEnum.WAIT_UPLOAD_OSS.getStatus());
                add(CameraDownloadFileStatusEnum.UPLOAD_OSS_FAILED.getStatus());
            }
        };
        var tasks = cameraDownloadTaskMapper.getByFileStatus(fileStatus, 20, null)
                .stream()
                .filter(item -> CameraTypeEnum.TYYY.getCode().equals(item.getCameraType())
                        || CameraTypeEnum.TYBD.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        tasks.forEach(task -> executorServiceOss.submit(() -> {
            startOssUploadCameraFile(task);
        }));
    }

    @Override
    public void retryFailedCameraFile() {
        /*var exceptReturnCodes = new ArrayList<Integer>(4) {
            {
                // {"code":300,"message":"http响应异常!厂商返回失败!没有获取到录像文件url"}
                add(300);
            }
        };*/
        var tasks = cameraDownloadTaskMapper.getByFileStatus(
                        Collections.singletonList(CameraDownloadFileStatusEnum.REQUEST_FAILED.getStatus()), 20, null)
                .stream()
                .filter(item -> CameraTypeEnum.TYYY.getCode().equals(item.getCameraType())
                        || CameraTypeEnum.TYBD.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        tasks.forEach(task -> executorServiceRetry.submit(() -> {
            doBeginDownloadCameraFileLocal(task);
        }));
    }

    @Override
    public void deleteNightWatchExpiredRecord() {
        // 当前时间向前推 NIGHT_WATCH_EXPIRED_DAYS 天
        var expiredDate = DateTime.of(DateTime.now().getTime() - NIGHT_WATCH_EXPIRED_DAYS * 24 * 3600 * 1000);
        var tasks = cameraDownloadTaskMapper.listTaskTypeExpiredRecords(CameraTaskTypeEnum.NIGHT_WATCH.getCode(),
                        new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN).format(expiredDate))
                .stream()
                .filter(item -> CameraTypeEnum.TYYY.getCode().equals(item.getCameraType())
                        || CameraTypeEnum.TYBD.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        tasks.forEach(task -> removeTaskAndOssFile(task.getId()));
    }

    @Override
    public ResponseCustom<List<TblCameraDownloadTaskEntity>> listSuccessDownloadRecords(
            CamaraMediaDownloadBaseRequestDTO request) {
        return null;
    }

    //CHECKSTYLE:OFF
    @Override
    public void syncAllCameraExtendInfo() {
        var pageSize = 100;
        var pageNum = 1;
        var responseDTO = gateway.listDeviceInfo(PageRequestDTO.builder()
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build());
        if (null == responseDTO) {
            log.error("获取天翼视联设备列表失败");
            return;
        }
        if (HttpStatus.OK.value() != responseDTO.getCode()) {
            log.error("获取天翼视联设备列表失败：{}", responseDTO.getMsg());
            return;
        }
        var pageRes = responseDTO.getData();
        if (null == pageRes) {
            log.error("获取天翼视联设备列表失败：{}", responseDTO.getMsg());
            return;
        }

        var res = pageRes.getList();
        var pages = pageRes.getPages();
        // 记录多于一页
        if (pages > pageNum) {
            for (var page = 2; page <= pages; page++) {
                var responseTemp = gateway.listDeviceInfo(PageRequestDTO.builder()
                        .pageNum(page)
                        .pageSize(pageSize)
                        .build());
                if (null == responseTemp || HttpStatus.OK.value() != responseTemp.getCode()) {
                    continue;
                }
                pageRes = responseTemp.getData();
                if (null != pageRes && null != pageRes.getList() && !CollectionUtils.isEmpty(pageRes.getList())) {
                    res.addAll(pageRes.getList());
                }
            }
        }
        // 整理结果
        if (CollectionUtils.isEmpty(res)) {
            return;
        }
        res.forEach(item -> {
            var platformId = null == item.getPlatformid() ? "0" : String.valueOf(item.getPlatformid());
            var hasRecord = cameraExtendInfoMapper.getByCloudNumberAndPlatformId(item.getName(), platformId);
            if (hasRecord != null) {
                if (!hasRecord.getDeviceCode().equals(item.getDeviceCode())
                        || !hasRecord.getVendorCode().equals(item.getVendorCode())) {
                    redisUtils.deleteObject(
                            RedisKeyUtils.getCityPushPlatformElevatorExistsKey(hasRecord.getElevatorCode()));
                    redisUtils.deleteObject("elevator_cache:" + hasRecord.getElevatorCode());
                }
            }

            var tblCameraExtendInfo = TblCameraExtendInfoEntity.builder()
                    .cloudNumber(item.getName())
                    .platformId(platformId)
                    .platformName(item.getPlatformName())
                    .supOrSub(1)
                    .elevatorCode("")
                    .guid(item.getGuid())
                    .gbid(item.getGbId())
                    .ctei(item.getCtei())
                    .cameraType(5)
                    .channelSeq(item.getChannelSeq())
                    .deviceCode(item.getDeviceCode())
                    .vendorCode(item.getVendorCode())
                    .onlineState(1 <= item.getStatus() ? 1 : 0)
                    .status(item.getStatus())
                    .build();
            // 补充电梯编号和摄像头厂家类型
            extendCameraInfo(tblCameraExtendInfo);
            cameraExtendInfoMapper.saveCameraExtendInfo(tblCameraExtendInfo);
        });
    }
    //CHECKSTYLE:ON

    @Override
    public ResponseCustom<TyslDevicePlaybackUrlResponseDTO.PlayBackUrlData> getVideoPlaybackUrl(
            CamareMediaDownloadRequestDTO request) {
        // 开启获取回放流
        var hasRecording = redisUtils.setIfAbsent(REDIS_KEY_TYSL_PLAYBACK_PRE + request.getElevatorCode(), "init",
                40L, TimeUnit.SECONDS);
        if (!hasRecording) {
            return ResponseCustom.buildFailed(505, "有正在执行录制的任务，稍后重试");
        }
        if (null != request.getDuringSeconds() && !StringUtils.hasText(request.getEndTime())) {
            request.setEndTime(DateUtil.format(
                    DateUtil.offsetSecond(
                            DateUtil.parse(request.getStartTime(), "yyyy-MM-dd HH:mm:ss"),
                            request.getDuringSeconds().intValue()),
                    "yyyy-MM-dd HH:mm:ss"));
        }
        // 获取设备类型
        var camera = getCameraTypeByElevatorCode(request.getElevatorCode());
        // 根据电梯编号获取guid
        var cameraExtendInfo = cameraExtendInfoMapper.getCameraExtendInfoByElevatorCode(request.getElevatorCode(),
                null == camera ? CameraTypeEnum.TYYY.getCode() : camera.getCameraType());
        if (null == cameraExtendInfo) {
            return ResponseCustom.buildFailed(404, "根据电梯编号没有找到相关GUID信息");
        }
        // 获取回放地址
        var res = gateway.listDevicePlaybackUrl(DevicePlaybackUrlRequestDTO.builder()
                .guid(cameraExtendInfo.getGuid())
                .accessType(1)
                .beginTime(request.getStartTime().replace("-", "").replace(" ", "").replace(":", ""))
                .endTime(request.getEndTime().replace("-", "").replace(" ", "").replace(":", ""))
                .proto(2)
                .pageNum(1)
                .pageSize(50)
                .isDownload(0)
                .fileId("")
                .streamId(1)
                .location("1")
                .build());
        if (null == res) {
            return ResponseCustom.buildFailed(500, "获取回放地址失败, 结果为空");
        }
        if (null == res.getData() || CollectionUtils.isEmpty(res.getData().getList())) {
            return ResponseCustom.buildFailed(res.getCode(), res.getMsg());
        }
        return ResponseCustom.success(res.getData().getList().get(0));
    }

    @Override
    public ResponseCustom<String> getCameraStreamUrl(String elevatorCode, String protoString) {
        // 获取设备类型
        var camera = getCameraTypeByElevatorCode(elevatorCode);
        // 根据电梯编号获取guid
        var cameraExtendInfo = cameraExtendInfoMapper.getCameraExtendInfoByElevatorCode(elevatorCode,
                null == camera ? CameraTypeEnum.TYYY.getCode() : camera.getCameraType());
        if (null == cameraExtendInfo) {
            return ResponseCustom.buildFailed(404, "根据电梯编号没有找到相关GUID信息");
        }
        if (!StringUtils.hasText(protoString)) {
            protoString = "HLS";
        }
        var res = gateway.getDeviceStreamUrl(DeviceStreamUrlRequestDTO.builder()
                .guid(cameraExtendInfo.getGuid())
                .proto(TyslVideoProtoEnum.valueOf(protoString.toUpperCase()).getCode())
                .accessType(1)
                .streamType(0)
                .build());
        if (null == res || HttpStatus.OK.value() != res.getCode()) {
            return ResponseCustom.buildFailed(500, "获取视频流地址失败");
        }
        return ResponseCustom.success(res.getData().getUrl());
    }

    @Override
    public ResponseCustom<String> liveSnapshot(String elevatorCode) {
        var urlRes = getCameraStreamUrl(elevatorCode, "HLS");
        if (null == urlRes || !StringUtils.hasText(urlRes.getData())) {
            return urlRes;
        }
        var workId = elevatorCode + "-" + System.currentTimeMillis();
        var res = videoStreamDownloadHandle.capture(urlRes.getData(), PATH_FILE_DOWNLOAD_LOCAL, workId, null);
        if (StringUtils.hasText(res)) {
            return ResponseCustom.success(res);
        }
        return ResponseCustom.buildFailed(500, "截图失败");
    }

    @Override
    public ResponseCustom<String> liveRecord(String elevatorCode, Long duringSeconds) {
        var urlRes = getCameraStreamUrl(elevatorCode, "HLS");
        if (null == urlRes || !StringUtils.hasText(urlRes.getData())) {
            return urlRes;
        }
        var workId = elevatorCode + "-" + System.currentTimeMillis();
        // 指定时间后停止录制
        executorStopService.submit(() -> {
            try {
                // 增加8秒时长，否则录制的视频时长少于指定时长
                Thread.sleep((duringSeconds + 8) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            videoStreamDownloadHandle.stop(workId);
        });
        var res = videoStreamDownloadHandle.run(urlRes.getData(), PATH_FILE_DOWNLOAD_LOCAL, workId, null);
        if (StringUtils.hasText(res)) {
            return ResponseCustom.success(res);
        }
        return ResponseCustom.buildFailed(500, "录制直播失败");
    }

    @Override
    public ResponseCustom<String> downloadPictureFile(FaultForHistoryPhotoVideoRequestDTO request) {
        log.info("添加下载图片文件，{}", JSON.toJSONString(request));
        return saveCameraDownloadTask(CamareMediaDownloadRequestDTO.builder()
                .elevatorCode(request.getElevatorCode())
                .collectTime(request.getOccurTime())
                .taskType(CameraTaskTypeEnum.FAULT)
                .taskCustomId(request.getFaultId())
                .taskCustomType(Integer.valueOf(request.getFaultType()))
                .mediaType(CameraMediaTypeEnum.JPG)
                .build());
    }

    @Override
    public ResponseCustom<String> downloadFaultVideoFile(FaultForHistoryPhotoVideoRequestDTO request) {
        log.info("添加下载视频文件，{}", JSON.toJSONString(request));
        return saveCameraDownloadTask(CamareMediaDownloadRequestDTO.builder()
                .elevatorCode(request.getElevatorCode())
                .collectTime(request.getOccurTime())
                .taskType(CameraTaskTypeEnum.FAULT)
                .taskCustomId(request.getFaultId())
                .taskCustomType(Integer.valueOf(request.getFaultType()))
                .mediaType(CameraMediaTypeEnum.M3U8)
                .build());
    }

    @Override
    public ResponseCustom<Object> getVoiceWssInfo(String elevatorCode, Integer domain) {
        // 获取设备类型
        var camera = getCameraTypeByElevatorCode(elevatorCode);
        // 根据电梯编号获取guid
        var cameraExtendInfo = cameraExtendInfoMapper.getCameraExtendInfoByElevatorCode(elevatorCode,
                null == camera ? CameraTypeEnum.TYYY.getCode() : camera.getCameraType());
        if (null == cameraExtendInfo) {
            return ResponseCustom.buildFailed(404, "根据电梯编号没有找到相关GUID信息");
        }
        var res = gateway.voiceWss(VoiceWssRequestDTO.builder()
                .guid(cameraExtendInfo.getGuid())
                .domain(domain)
                .build());
        if (null == res || HttpStatus.OK.value() != res.getCode()) {
            return ResponseCustom.buildFailed(500, "获取语音对讲WSS失败：" + (null == res ? "未知" : res.getMsg()));
        }
        res.getData().setDeviceCode(cameraExtendInfo.getDeviceCode());
        return ResponseCustom.success(res.getData());
    }

    @Override
    public ResponseCustom<String> getVoiceStreamToken(String elevatorCode) {
        // 获取设备类型
        var camera = getCameraTypeByElevatorCode(elevatorCode);
        // 根据电梯编号获取guid
        var cameraExtendInfo = cameraExtendInfoMapper.getCameraExtendInfoByElevatorCode(elevatorCode,
                null == camera ? CameraTypeEnum.TYYY.getCode() : camera.getCameraType());
        if (null == cameraExtendInfo) {
            return ResponseCustom.buildFailed(404, "根据电梯编号没有找到相关GUID信息");
        }
        var res = gateway.voiceStreamToken(cameraExtendInfo.getGuid());
        if (null == res || HttpStatus.OK.value() != res.getCode()) {
            return ResponseCustom.buildFailed(500, "获取语音对讲WSS失败：" + (null == res ? "未知" : res.getMsg()));
        }
        return ResponseCustom.success(res.getData().getStreamToken());
    }

    @Override
    public void deleteDownloadSuccessfulLocalFile() {
        //获取最近三天的下载成功的文件地址
        List<String> filePathList = cameraDownloadTaskMapper.getDownloadSuccessfulLocalFile();
        //文件是否存在并且删除
        filePathList.forEach(FileUtil::deleteFile);
        log.info("删除下载成功的本地文件-执行成功");
    }

    /**
     * 通过标准流获取历史视频和图片， 因为云录制未开通，暂时使用该方法, 如果后期开通，可以使用 doBeginDownloadCameraFile 方法
     *
     * @param task 任务
     */
    private void doBeginDownloadCameraFileLocal(TblCameraDownloadTaskEntity task) {
        // 重新获取任务状态，如果是执行中或成功，则忽略
        var fileStatus = cameraDownloadTaskMapper.getFileStatusById(task.getId());
        if (Objects.equals(CameraDownloadFileStatusEnum.SUCCESS.getStatus(), fileStatus)
                || CameraDownloadFileStatusEnum.START_DEAL.getStatus().equals(fileStatus)
                || CameraDownloadFileStatusEnum.DOWNLOADING.getStatus().equals(fileStatus)) {
            return;
        }
        // 设置任务开始
        setDownloadStatus(task.getId(), CameraDownloadFileStatusEnum.START_DEAL);
        if (CameraMediaTypeEnum.getMediaTypeListByFileType("image").contains(task.getMediaType().toLowerCase())) {
            doDownloadImageLocal(task);
        } else {
            // 开始本地录制
            var startTime = DateUtil.format(DateUtil.parse(task.getStartTime(), DatePattern.PURE_DATETIME_PATTERN),
                    DatePattern.NORM_DATETIME_PATTERN);
            var endTime = null == task.getEndTime()
                    ? null
                    : DateUtil.format(DateUtil.parse(task.getEndTime(), DatePattern.PURE_DATETIME_PATTERN),
                    DatePattern.NORM_DATETIME_PATTERN);
            //获取视频回放流地址
            var playbackRes = getVideoPlaybackUrl(CamareMediaDownloadRequestDTO.builder()
                    .elevatorCode(task.getElevatorCode())
                    .startTime(startTime)
                    .endTime(endTime)
                    .duringSeconds(DEFAULT_RECORD_SECONDS)
                    .build());
            if (HttpStatus.OK.value() != playbackRes.getCode()) {
                // 505 的定义是当前设备有正在执行的任务， 这里就忽略， 等待下次执行，不记录错误次数
                if (playbackRes.getCode() == 505) {
                    setDownloadStatus(task.getId(), CameraDownloadFileStatusEnum.WAITING);
                    return;
                }
                setTaskRequestFailed(task, ResponseCustom.buildFailed(playbackRes.getCode(),
                        JSON.toJSONString(playbackRes)));
                return;
            }
            // 缓存ssrc任务关系
            var sourceUrl = buildTyslSourceUrl(playbackRes.getData().getUrl(), playbackRes.getData().getSsrc());

            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                // do nothing
            }
            // 存储视频回放地址
            log.info("获取视频回放地址成功-开始录制视频，faultId:{}, playbackRes：{}", task.getTaskCustomId(), playbackRes);
            // 下载视频，上传阿里云
            var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                    .fileStatus(CameraDownloadFileStatusEnum.WAIT_UPLOAD_OSS.getStatus())
                    // 去除开头，结尾的双引号
                    .sourceUrl(CharMatcher.is('\"').trimFrom(sourceUrl))
                    .returnCode(playbackRes.getCode())
                    .errMessage("")
                    .build();
            cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
            task.setSourceUrl(CharMatcher.is('\"').trimFrom(sourceUrl));
            // 开始录制本地视频并上传阿里云
            startOssUploadCameraFile(task);
            return;
        }
    }

    private Integer setDownloadStatus(Long id, CameraDownloadFileStatusEnum statusEnum) {
        return cameraDownloadTaskMapper.updateFileStatusById(id, statusEnum.getStatus());
    }

    /*private void doDownloadingCameraFile(TblCameraDownloadTaskEntity task) {
        // 重新获取任务状态，如果已变为其他状态，则忽略
        var fileStatus = cameraDownloadTaskMapper.getFileStatusById(task.getId());
        if (!CameraDownloadFileStatusEnum.DOWNLOADING.getStatus().equals(fileStatus)) {
            return;
        }
        var lock = redissonClient.getLock("CAMERA:FILE:LOCK:DOWNLOADING:" + task.getTaskCustomId());
        try {
            //尝试加锁，最多等待100秒，上锁以后3分钟自动解锁
            if (lock.tryLock(100, 180, TimeUnit.SECONDS)) {
                try {
                    doDownloadingCameraFileUrl(task);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException ignored) {

        }
    }*/

    /*private void doDownloadingCameraFileUrl(TblCameraDownloadTaskEntity task) {

        var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                .sourceUrl(StringUtils.collectionToCommaDelimitedString(res.getData().getUrls()))
                .returnCode(res.getCode())
                .errMessage(res.getMessage())
                .fileStatus(CameraDownloadFileStatusEnum.WAIT_UPLOAD_OSS.getStatus())
                .build();
        cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
    }*/

    private void startOssUploadCameraFile(TblCameraDownloadTaskEntity task) {
        // 重新获取任务状态，如果已变为其他状态，则忽略
        var fileStatus = cameraDownloadTaskMapper.getFileStatusById(task.getId());
        if (!CameraDownloadFileStatusEnum.WAIT_UPLOAD_OSS.getStatus().equals(fileStatus)
                && !CameraDownloadFileStatusEnum.UPLOAD_OSS_FAILED.getStatus().equals(fileStatus)) {
            return;
        }
        var lock = redissonClient.getLock("CAMERA:FILE:LOCK:OSS_UPLOAD:" + task.getTaskCustomId());
        try {
            //尝试加锁，最多等待100秒，上锁以后3分钟自动解锁
            if (lock.tryLock(100, 180, TimeUnit.SECONDS)) {
                try {
                    doOssUploadCameraFile(task);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException ignored) {
            // do null
        }
    }

    //CHECKSTYLE:OFF
    private void doOssUploadCameraFile(TblCameraDownloadTaskEntity task) {
        log.info("oss-upload-camera-file taskid {}, url:{}", task.getTaskCustomId(), task.getSourceUrl());
        if (task.getSourceUrl().contains("ctyunxs.cn")
                || task.getSourceUrl().startsWith("http")) {
            doGetFileBytesAndUploadOss(task);
        }

        String ossUrlPre = "Oreo_Project/" + CameraTaskTypeEnum.getPathByCode(task.getTaskType()) + "/"
                + DateUtil.today().replace("-", "/") + "/";
        //  + task.getTaskCustomId() + "." + task.getMediaType()

        if (!StringUtils.hasText(task.getSourceUrl())) {
            log.info("doOssUploadCameraFile {} task.getSourceUr is null {}", task.getTaskCustomId(),
                    task.getSourceUrl());
            setTaskRequestFailed(task, ResponseCustom.buildFailed(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "转存本地视频失败"));
            return;
        }
        if (task.getSourceUrl().startsWith("http")) {
            var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                    .fileStatus(CameraDownloadFileStatusEnum.REQUEST_FAILED.getStatus())
                    .returnCode(404)
                    .errMessage("Video record failed")
                    .build();
            cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
            return;
        }
        var sourceUris = Collections.singletonList(task.getSourceUrl());
        if (task.getSourceUrl().contains(",")) {
            sourceUris = Arrays.asList(task.getSourceUrl().split(","));
        }

        var i = 0;
        var ossUrlPathList = new ArrayList<String>();
        for (var url : sourceUris) {
            var ossUrl = ossUrlPre + task.getTaskCustomId() + i + "." + url.split("\\.")[url.split("\\.").length - 1];
            ossUrlPathList.add(ossUrl);
            try {
                var bytes = (url.trim().startsWith("http") || url.trim().startsWith("rtsp"))
                        ? FileUtil.getBytesByRemotePath(url.trim())
                        : FileUtil.getBytesByFile(url.trim());
                if (bytes != null && bytes.length > 0) {
                    OssInternalUtils.setOSS(bytes, ossUrl, aliOssProperties.getUseInternal());
                }
            } catch (Exception e) {
                e.printStackTrace();
                var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                        .fileStatus(CameraDownloadFileStatusEnum.UPLOAD_OSS_FAILED.getStatus())
                        .returnCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errMessage("oss upload failed: " + e.getMessage())
                        .build();
                cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
                cameraDownloadTaskMapper.increaseUploadFailedCount(task.getId());
                return;
            }
            i++;
        }
        if (!CollectionUtils.isEmpty(ossUrlPathList)) {
            var ossUrlList = ossUrlPathList.stream()
                    .map(itemUrl -> OssInternalUtils.OSS_URL + itemUrl)
                    .toList();

            var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                    .fileStatus(CameraDownloadFileStatusEnum.SUCCESS.getStatus())
                    .ossUrl(StringUtils.collectionToCommaDelimitedString(ossUrlList))
                    .returnCode(HttpStatus.OK.value())
                    .errMessage("SUCCESS")
                    .build();
            cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
            log.info("开始处理消息 taskId {}， {}， {} ", task.getTaskCustomId(), task.getTaskType(), task.getElevatorCode());

            // 如果是FAULT，则需要在 sys_file中添加记录
            if (CameraTaskTypeEnum.FAULT.getCode().equals(task.getTaskType())) {
                log.info("开始故障处理 taskId {}", task.getTaskCustomId());
                var fileEntity = TblSysFile.builder()
                        .vFileId(IdUtil.getSnowflake(1, 1).nextIdStr())
                        .vFileType("image".equals(CameraMediaTypeEnum.getFileTypeByMediaType(task.getMediaType()))
                                ? "0" : "1")
                        .vFileName(task.getTaskCustomId() + i + "." + task.getMediaType())
                        .vBusinessId(task.getTaskCustomId())
                        .iBusinessType(2)
                        .vUrl(ossUrlList.get(0))
                        .vRemark("故障图片抓取")
                        .dtCreateTime(DateUtil.date())
                        .build();
                log.info("开始故障处理2 taskId {}, {}", task.getTaskCustomId(), JSON.toJSONString(fileEntity));
                sysFileMapper.insert(fileEntity);
                log.info("上传阿里云-存sys_file表 , taskId {}", task.getTaskCustomId());

                if ("image".equals(CameraMediaTypeEnum.getFileTypeByMediaType(task.getMediaType()))) {
                    //图像二次识别
                    imageIdentification(
                            ossUrlList.get(0),
                            String.valueOf(task.getTaskCustomType()),
                            task.getTaskCustomId(), task.getElevatorCode());
                    log.info("上传阿里云-图像二次识别结束 , taskId {}", task.getTaskCustomId());
                }
            }
            // 发送任务完成消息给sender服务
            sendExecutorService.submit(() -> sendTaskFinishedToSenderService(task.getTaskCustomId(),
                    task.getCameraType()));
            log.info("上传阿里云-发送任务完成消息给sender服务 , taskId {}", task.getTaskCustomId());

            // 自研电动车识别的需要更新记录
            if (CameraTaskTypeEnum.ELECTRIC_BIKE_IDENTIFY.getCode().equals(task.getTaskType())
                    || CameraTaskTypeEnum.PEOPLE_FLOW_STATISTICS.getCode().equals(task.getTaskType())) {
                cameraImageIdentifyMapper.update(new TblCameraImageIdentifyEntity(task.getTaskCustomId())
                        .setOssUrl(ossUrlList.get(0))
                        .setStatus(1));
            }
        }
    }
    //CHECKSTYLE:ON

    /**
     * 视频流的先下载到本地在上传到OSS
     *
     * @param task 任务
     */
    private void doGetFileBytesAndUploadOss(TblCameraDownloadTaskEntity task) {
        if (!StringUtils.hasText(task.getSourceUrl())) {
            log.info("doGetFileBytesAndUploadOss {} task.getSourceUr is null {}", task.getTaskCustomId(),
                    task.getSourceUrl());
            return;
        }
        var localUrl = task.getSourceUrl();
        var fileName = localUrl.split("/")[localUrl.split("/").length - 1];
        // 路径是 rtmp格式的网址需要下载视频到本地
        if (!task.getSourceUrl().startsWith("/")) {
            setDownloadStatus(task.getId(), CameraDownloadFileStatusEnum.START_DEAL);
            var recordResult = covertCameraRecordToLocal(task);
            if (CollectionUtils.isEmpty(recordResult)) {
                log.info("doGetFileBytesAndUploadOss {} 存储本地文件失败", task.getTaskCustomId());
                return;
            }
            localUrl = recordResult.get("localUrl");
            fileName = recordResult.get("fileName");
            task.setSourceUrl(localUrl);
        }
    }


    private void doDownloadImageLocal(TblCameraDownloadTaskEntity task) {
        // 获取视频回放流地址
        var startTime = DateUtil.format(DateUtil.parse(task.getStartTime(), DatePattern.PURE_DATETIME_PATTERN),
                DatePattern.NORM_DATETIME_PATTERN);
        var endTime = null == task.getEndTime()
                ? null
                : DateUtil.format(DateUtil.parse(task.getEndTime(), DatePattern.PURE_DATETIME_PATTERN),
                DatePattern.NORM_DATETIME_PATTERN);
        //获取视频回放流地址
        var playbackRes = getVideoPlaybackUrl(CamareMediaDownloadRequestDTO.builder()
                .elevatorCode(task.getElevatorCode())
                .startTime(startTime)
                .endTime(endTime)
                .duringSeconds(DEFAULT_RECORD_SECONDS)
                .build());
        if (HttpStatus.OK.value() != playbackRes.getCode()) {
            // 505 的定义是当前设备有正在执行的任务， 这里就忽略， 等待下次执行，不记录错误次数
            if (playbackRes.getCode() == 505) {
                setDownloadStatus(task.getId(), CameraDownloadFileStatusEnum.WAITING);
                return;
            }
            setTaskRequestFailed(task, ResponseCustom.buildFailed(playbackRes.getCode(),
                    "截图：" + JSON.toJSONString(playbackRes)));
            return;
        }
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            // do nothing
        }
        /*// 等待3秒，避免电信地址访问404
        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException ignore) {
        }*/
        var redisStatus = redisUtils.getCacheObject(REDIS_KEY_TYSL_PLAYBACK_PRE + task.getElevatorCode());
        if (redisStatus != null && !"init".equals(redisStatus)) {
            setTaskWait(task, ResponseCustom.buildFailed(500, "其他任务在执行"));
            return;
        }
        // 执行状态缓存
        redisUtils.setCacheObject(REDIS_KEY_TYSL_PLAYBACK_PRE + task.getElevatorCode(), "capture", 5L,
                TimeUnit.MINUTES);
        var capture = videoStreamDownloadHandle.capture(CharMatcher.is('\"').trimFrom(playbackRes.getData().getUrl()),
                PATH_FILE_DOWNLOAD_LOCAL, task.getTaskCustomId(), playbackRes.getData().getSsrc());
        if (!StringUtils.hasText(capture)) {
            // 执行状态缓存 30秒内不要在获取，否则也会失败
            redisUtils.setCacheObject(REDIS_KEY_TYSL_PLAYBACK_PRE + task.getElevatorCode(), "capture-failed", 30L,
                    TimeUnit.SECONDS);
            setTaskWait(task, ResponseCustom.buildFailed(500, "截图失败"));
            return;
        }
        stopRemotePlayback(task.getElevatorCode(), playbackRes.getData().getSsrc());

        // 执行状态缓存 30秒内不要在获取，否则也会失败
        redisUtils.setCacheObject(REDIS_KEY_TYSL_PLAYBACK_PRE + task.getElevatorCode(), "capture-finished", 30L,
                TimeUnit.SECONDS);
        // 下载视频，上传阿里云
        var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                .fileStatus(CameraDownloadFileStatusEnum.WAIT_UPLOAD_OSS.getStatus())
                // 去除开头，结尾的双引号
                .sourceUrl(capture)
                .returnCode(playbackRes.getCode())
                .errMessage("")
                .build();
        cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
        return;
    }


    private Map<String, String> covertCameraRecordToLocal(TblCameraDownloadTaskEntity task) {
        var redisStatus = redisUtils.getCacheObject(REDIS_KEY_TYSL_PLAYBACK_PRE + task.getElevatorCode());
        if (redisStatus != null && !"init".equals(redisStatus)) {
            setTaskWait(task, ResponseCustom.buildFailed(500, "其他任务在执行"));
            return null;
        }
        // 执行状态缓存
        redisUtils.setCacheObject(REDIS_KEY_TYSL_PLAYBACK_PRE + task.getElevatorCode(), "video", 10L,
                TimeUnit.MINUTES);

        var filePath = PATH_FILE_DOWNLOAD_LOCAL + DateUtil.today().replace("-", "/") + "/";
        var ssrc = getSsrcFromTyslUrl(task.getSourceUrl());
        // 下载视频
        var localUrl = videoStreamDownloadHandle.run(getUrlFromTyslUrl(task.getSourceUrl()), filePath,
                task.getTaskCustomId(), ssrc);
        if (!StringUtils.hasText(localUrl)) {
            // 执行状态缓存 30秒内不要在获取，否则也会失败
            redisUtils.setCacheObject(REDIS_KEY_TYSL_PLAYBACK_PRE + task.getElevatorCode(), "video-failed", 30L,
                    TimeUnit.SECONDS);
            setTaskWait(task, ResponseCustom.buildFailed(500, "下载到本地视频失败"));

            if (StringUtils.hasText(ssrc)) {
                stopRemotePlayback(task.getElevatorCode(), ssrc);
            }
            return null;
        }
        // 执行状态缓存 30秒内不要在获取，否则也会失败
        redisUtils.setCacheObject(REDIS_KEY_TYSL_PLAYBACK_PRE + task.getElevatorCode(), "video-finished", 30L,
                TimeUnit.SECONDS);
        // 将 rtmp地址修改为本地地址
        var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                .fileStatus(CameraDownloadFileStatusEnum.START_DEAL.getStatus())
                .sourceUrl(localUrl)
                .errMessage("视频已存储到本地")
                .build();
        cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
        var fileName = localUrl.replace(filePath, "");
        task.setSourceUrl(localUrl);
        log.info("covertCameraRecordToLocal {}, localUrl:{}, fileName:{}", task.getTaskCustomId(), localUrl, fileName);
        return Map.of("localUrl", localUrl, "fileName", fileName);
    }

    private <T extends ResponseCustom> void setTaskWait(TblCameraDownloadTaskEntity task, T res) {
        var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                .fileStatus(CameraDownloadFileStatusEnum.WAITING.getStatus())
                .returnCode(res.getCode())
                .errMessage(res.getMessage())
                .sourceUrl("")
                .build();
        cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
        // cameraDownloadTaskMapper.increaseRequestFailedCount(task.getId());
    }

    private <T extends ResponseCustom> void setTaskRequestFailed(TblCameraDownloadTaskEntity task, T res) {
        var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                .fileStatus(CameraDownloadFileStatusEnum.REQUEST_FAILED.getStatus())
                .returnCode(res.getCode())
                .errMessage(res.getMessage())
                .build();
        cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
        cameraDownloadTaskMapper.increaseRequestFailedCount(task.getId());
        // 判断错误次数是否为3次, 是并且是非平层困人, 则发送短信
        sendExecutorService.submit(() -> checkEntrap2AndSendSms(task));
    }

    private <T extends ResponseCustom> void setTaskUploadFileFailed(TblCameraDownloadTaskEntity task, T res) {
        var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                .fileStatus(CameraDownloadFileStatusEnum.UPLOAD_OSS_FAILED.getStatus())
                .returnCode(res.getCode())
                .errMessage(res.getMessage())
                .build();
        cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
        cameraDownloadTaskMapper.increaseUploadFailedCount(task.getId());
    }

    private void extendCameraInfo(TblCameraExtendInfoEntity tblCameraExtendInfo) {
        var cameraInfo = cameraMapper.getCameraInfoByCloudNumber(tblCameraExtendInfo.getCloudNumber(),
                tblCameraExtendInfo.getCameraType());
        if (null == cameraInfo) {
            return;
        }
        // 更新摄像头在线状态
        cameraMapper.updateCameraStatus(CameraStatusRecordEntity.builder()
                .status(tblCameraExtendInfo.getOnlineState())
                .serialNumber(tblCameraExtendInfo.getCloudNumber())
                .build());
        // 补充电梯编号
        tblCameraExtendInfo.setElevatorCode(cameraInfo.getElevatorCode());
        // 补充设备类型
        var cameraTypeEnum = CameraTypeEnum.getBySlug(tblCameraExtendInfo.getVendorCode());
        if (cameraTypeEnum != null) {
            tblCameraExtendInfo.setCameraType(cameraTypeEnum.getCode());
        } else {
            tblCameraExtendInfo.setCameraType(cameraInfo.getCameraType());
        }
    }

    private void stopRemotePlayback(String elevatorCode, String ssrc) {
        // 获取设备类型
        var camera = getCameraTypeByElevatorCode(elevatorCode);
        // 根据电梯编号获取guid
        var cameraExtendInfo = cameraExtendInfoMapper.getCameraExtendInfoByElevatorCode(elevatorCode,
                null == camera ? CameraTypeEnum.TYYY.getCode() : camera.getCameraType());
        if (null != cameraExtendInfo) {
            // 执行清理远程录制请求
            gateway.playbackControl(DevicePlaybackControlRequestDTO.builder()
                    .guid(cameraExtendInfo.getGuid())
                    .ssrc(ssrc)
                    .control("STOP")
                    .scale("1")
                    .range(0)
                    .build());
        }
    }


    /**
     * 调用图像识别
     *
     * @param picUrl       图片地址
     * @param faultType    麦信故障类型
     * @param faultId      故障ID
     * @param elevatorCode 电梯编号
     */
    private void imageIdentification(String picUrl, String faultType, String faultId, String elevatorCode) {
        log.info("上传阿里云-图像二次识别开始 , taskId {}", faultId);
        try {
            if (GovernFaultTypeEnum.LEVEL_STOP.getShmashineCode().equals(faultType)) {
                // restTemplateSendMessage(faultId, picUrl, "person");
                ImageIdentifyUtils.identifyImage(faultId, picUrl, ImageIdentifyUtils.IDENTIFY_TYPE_PERSON,
                        aliOssProperties.getUseInternal());
                log.info("非平层停梯调用二次识别确认，故障id【{}】", faultId);
            }
            if (GovernFaultTypeEnum.ELECTRIC_VEHICLE_LADDER.getShmashineCode().equals(faultType)) {
                // restTemplateSendMessage(faultId, picUrl, "motorcycle");
                ImageIdentifyUtils.identifyImage(faultId, picUrl, ImageIdentifyUtils.IDENTIFY_TYPE_MOTOR_CYCLE,
                        aliOssProperties.getUseInternal());
                log.info("电动车乘梯调用二次识别确认，故障id【{}】", faultId);
            }
            if (GovernFaultTypeEnum.ENTRAP.getShmashineCode().equals(faultType)) {

                var remove = redisUtils.redisTemplate.opsForZSet().remove("HLS:IMAGE", faultId);
                if (null != remove && remove > 0L) {
                    try {
                        //图片下载成功释放锁
                        var lock = redissonClient.getLock("AFRESDOWNLOADIMAGE_LOCK" + faultId);
                        lock.forceUnlock();
                        log.info("---------平层困人图片下载成功，释放锁------------");
                    } catch (RuntimeException e) {
                        log.error("error ", e);
                    }

                    log.info("上传阿里云-图像二次识别-抠图开始 , taskId {}", faultId);
                    //抠图上传阿里云并返回文件地址
                    picUrl = imageMatting(faultId, picUrl, faultType, elevatorCode);
                    log.info("上传阿里云-图像二次识别-抠图结束 , taskId {}", faultId);

                    //调用图像识别
                    // restTemplateSendMessage(faultId, picUrl, "person");
                    ImageIdentifyUtils.identifyImage(faultId, picUrl, ImageIdentifyUtils.IDENTIFY_TYPE_PERSON,
                            aliOssProperties.getUseInternal());
                    //添加识别标识
                    redisUtils.redisTemplate.opsForZSet().add("HLS:IDENTIFICATION", faultId,
                            System.currentTimeMillis() / 1000);
                }
            }
        } catch (RuntimeException e) {
            log.info("调用识别服务失败：e： {}，故障id：{}，故障类型：{}", e.getMessage(), faultId, faultType);
        }
    }

    /**
     * 判断是否为非平层困人,并且达到了3次(task原失败次数应该为2), 发送短信
     */
    private void checkEntrap2AndSendSms(TblCameraDownloadTaskEntity task) {
        if (!CameraTaskTypeEnum.FAULT.getCode().equals(task.getTaskType())) {
            return;
        }
        if (!GovernFaultTypeEnum.ENTRAP2.getShmashineCode().equals(String.valueOf(task.getTaskCustomType()))) {
            return;
        }
        if (task.getRequestFailedCount() != 2) {
            return;
        }
        // 发送短信逻辑
        log.info("sendEntrap2Sms {} faultId：{}", task.getElevatorCode(), task.getTaskCustomId());
        try {
            //拼接请求参数
            String url = properties.getShmashineApi()
                    + "/api/faultTemp/sendEntrap2Message/{faultId}".replace("{faultId}", task.getTaskCustomId());
            var res = HttpUtil.get(url, 15000);
            log.info("发送非平层困人给API服务，故障id：[{}]，返回结果：[{}]", task.getTaskCustomId(), res);
        } catch (Exception e) {
            log.error("发送非平层困人给API服务异常，故障id：[{}], error:{}", task.getTaskCustomId(), ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 发送任务完成消息给sender服务
     *
     * @param faultId 故障ID
     */
    private void sendTaskFinishedToSenderService(String faultId, Integer cameraType) {
        log.info("sendTaskFinishedToSenderService faultId：{}", faultId);
        try {
            //拼接请求参数
            String url = properties.getSenderServer()
                    + "/sender/fault/resend/{faultId}/{cameraType}".replace("{faultId}", faultId)
                    .replace("{cameraType}", String.valueOf(cameraType));
            var res = HttpUtil.get(url, 15000);
            log.info("发送任务完成消息给sender服务，故障id：[{}]，返回结果：[{}]", faultId, res);
        } catch (Exception e) {
            log.error("发送任务完成消息给sender服务失败，故障id：[{}]，error：{}", faultId, ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 调用二次识别服务
     *
     * @param workOrderId 故障id
     * @param fileUrl     待识别图片url
     * @param type        识别类型
     */
    /*private void restTemplateSendMessage(String workOrderId, String fileUrl, String type) {
        //拼接请求参数
        String url = properties.getImageIdentify() + "/?type=" + type + "&url=" + fileUrl + "&faultId=" + workOrderId;
        //异步请求
        try {
            HttpRequest.get(url).timeout(500).executeAsync();
        } catch (RuntimeException e) {
            log.error("restTemplateSendMessage error ", e);
        }
    }*/

    /**
     * 计算抠图
     *
     * @param faultId      故障ID
     * @param picUrl       图片地址
     * @param faultType    故障类型
     * @param elevatorCode 电梯编号
     * @return 结果
     */
    private String imageMatting(String faultId, String picUrl, String faultType, String elevatorCode) {
        try {
            if (!StringUtils.hasText(elevatorCode)) {
                return picUrl;
            }
            //根据故障id或电梯抠图配置
            var config = elevatorMapper.getImageMattingConfigByFaultType(elevatorCode, faultType);
            if (config == null) {
                return picUrl;
            }
            var realCoordinates = JSON.parseArray(config.getRealCoordinates(), JSONObject.class);
            if (realCoordinates == null || realCoordinates.isEmpty()) {
                if (StrUtil.isBlank(config.getCoordinates())) {
                    return picUrl;
                }
                //抠图坐标点
                var points = JSON.parseArray(config.getCoordinates(), JSONObject.class);
                if (points == null || points.isEmpty()) {
                    return picUrl;
                }
                //获取图片分辨率
                Integer[] imgSize = ImageIdentifyUtils.getImgSize(picUrl, aliOssProperties.getUseInternal());
                //计算实际坐标点
                realCoordinates = getRealCoordinates(points, config.getWidth(), config.getHeight(), imgSize);
                //更新配置实际坐标点
                elevatorMapper.updateImageMattingConfig(JSON.toJSONString(realCoordinates), config.getId());
            }
            //本地存储路径
            String toPath = "/media/" + faultId + "_imageMatting.jpg";
            //抠图
            ImageIdentifyUtils.drawAndAlphaPolygon(picUrl, realCoordinates, "jpg", toPath,
                    aliOssProperties.getUseInternal());
            //上传阿里云
            //SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            log.info("------开始上传阿里云，故障id：[{}]", faultId);
            String uri = "Oreo_Project/fault/" + DateUtil.today().replace("-", "/") + "/" + faultId
                    + "_imageMatting.jpg";
            OssInternalUtils.setOSS(FileUtil.getBytesByFile(toPath), uri, aliOssProperties.getUseInternal());
            log.info("------上传阿里云成功，故障id：[{}]", faultId);

            //删除本地文件
            new File(toPath).delete();

            // return OssInternalUtils.getOssUrl(SpringContextUtil.isProdEnv()) + uri;
            return OssInternalUtils.extendOssUrl(uri);
        } catch (Exception e) {
            log.error("抠图失败,faultId:[{}],error:[{}]", faultId, ExceptionUtils.getStackTrace(e));
        }

        return picUrl;
    }

    /*private Integer[] getImgSize(String imgPath) {
        Integer[] size = new Integer[2];
        try {
            BufferedImage image = ImageIO.read(new URL(imgPath));
            size[0] = image.getWidth();
            size[1] = image.getHeight();
        } catch (IOException e) {
            log.error("getImgSize error", e);
        }
        return size;
    }*/

    /**
     * 计算实际坐标点
     *
     * @param points  坐标点
     * @param width   图片宽度
     * @param height  图片高度
     * @param imgSize 图片分辨率
     * @return list
     */
    private List<JSONObject> getRealCoordinates(List<JSONObject> points, Integer width,
                                                               Integer height, Integer[] imgSize) {

        return points.stream().map(p -> {
            int[] pointsX = p.getObject("x", int[].class);
            int[] pointsY = p.getObject("y", int[].class);
            return JSON.parseObject(JSON.toJSONString(Map.of("x", Arrays.stream(pointsX)
                            .mapToObj(x -> (x * imgSize[0]) / width)
                            .toList().toArray(new Integer[pointsX.length]),
                    "y", Arrays.stream(pointsY).mapToObj(y -> (y * imgSize[1]) / height)
                            .toList().toArray(new Integer[pointsX.length]))));

        }).collect(Collectors.toList());
    }

    /**
     * 绘制并填充多边形
     *
     * @param srcImagePath 源图像路径
     * @param points       坐标数组
     * @param imageFormat  写入图形格式
     * @param toPath       目标路径
     */
    /*private void drawAndAlphaPolygon(String srcImagePath, List<JSONObject> points, String imageFormat,
                                     String toPath) {
        try (var fos = new FileOutputStream(toPath)) {
            //获取图片
            var image = ImageIO.read(new URL(srcImagePath));
            //根据xy点坐标绘制闭合多边形
            var g2d = image.createGraphics();
            g2d.setColor(Color.BLACK);

            for (JSONObject point : points) {
                int[] pointsX = point.get("x", int[].class);
                int[] pointsY = point.get("y", int[].class);
                g2d.fillPolygon(pointsX, pointsY, pointsX.length);
            }
            ImageIO.write(image, imageFormat, fos);
            g2d.dispose();
        } catch (RuntimeException | IOException e) {
            log.error("drawAndAlphaPolygon error ", e);
        }
    }*/

    /**
     * 获取摄像头
     *
     * @param elevatorCode 电梯编号
     * @return 摄像头
     */
    private TblCameraDTO getCameraTypeByElevatorCode(String elevatorCode) {
        return cameraMapper.getCameraInfoByElevatorCode(elevatorCode);
    }

    private String buildTyslSourceUrl(String url, String ssrc) {
        return url + "?ssrc=" + ssrc;
    }

    private String getSsrcFromTyslUrl(String url) {
        if (url == null || !url.contains("ssrc=")) {
            return null;
        }
        return url.substring(url.indexOf("ssrc=") + 5);
    }

    private String getUrlFromTyslUrl(String url) {
        if (url == null || !url.contains("ssrc=")) {
            return url;
        }
        return url.substring(0, url.indexOf("?ssrc="));
    }

}
