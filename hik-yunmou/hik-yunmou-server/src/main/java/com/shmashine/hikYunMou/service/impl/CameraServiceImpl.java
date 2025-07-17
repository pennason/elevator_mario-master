// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.service.impl;

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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;

import com.shmashine.common.dto.CamaraMediaDownloadBaseRequestDTO;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.dto.TblCameraDTO;
import com.shmashine.common.entity.TblCameraCascadePlatformEntity;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;
import com.shmashine.common.entity.TblCameraHikCloudProjectEntity;
import com.shmashine.common.enums.CameraDownloadFileStatusEnum;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.common.enums.CameraTaskTypeEnum;
import com.shmashine.common.enums.CameraTypeEnum;
import com.shmashine.common.properties.AliOssProperties;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.FileUtil;
import com.shmashine.common.utils.OssInternalUtils;
import com.shmashine.common.utils.RedisKeyUtils;
import com.shmashine.hikYunMou.constants.HikvPlatformConstants;
import com.shmashine.hikYunMou.convert.CameraMediaDownloadCovertor;
import com.shmashine.hikYunMou.dao.TblCameraCascadePlatformMapper;
import com.shmashine.hikYunMou.dao.TblCameraDownloadTaskMapper;
import com.shmashine.hikYunMou.dao.TblCameraHikCloudProjectMapper;
import com.shmashine.hikYunMou.dao.TblCameraMapper;
import com.shmashine.hikYunMou.dao.TblElevatorMapper;
import com.shmashine.hikYunMou.dao.TblFaultMapper;
import com.shmashine.hikYunMou.dto.ResponseCustom;
import com.shmashine.hikYunMou.dto.requests.HikCloudProjectCreateRequestDTO;
import com.shmashine.hikYunMou.properties.EndpointProperties;
import com.shmashine.hikYunMou.service.CameraServiceI;
import com.shmashine.hikYunMou.utils.HikPlatformUtil;
import com.shmashine.hikYunMou.utils.RedisUtils;
import com.shmashine.hikYunMou.videoHandle.VideoStreamDownloadHandle;
import com.shmashine.mgppf.components.dto.enums.GovernFaultTypeEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/8 19:52
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CameraServiceImpl implements CameraServiceI {
    private final CameraMediaDownloadCovertor covertor;
    private final TblElevatorMapper elevatorMapper;
    private final TblCameraMapper cameraMapper;
    private final TblFaultMapper faultMapper;
    private final TblCameraDownloadTaskMapper cameraDownloadTaskMapper;
    private final TblCameraHikCloudProjectMapper cameraHikCloudProjectMapper;
    private final TblCameraCascadePlatformMapper cameraCascadePlatformMapper;
    private final RedissonClient redissonClient;
    private final RedisUtils redisUtils;
    private final HikPlatformUtil hikPlatformUtil;
    private final VideoStreamDownloadHandle videoStreamDownloadHandle;
    private final AliOssProperties aliOssProperties;
    private final EndpointProperties properties;

    /**
     * 夜间守护模式记录超过 NIGHT_WATCH_EXPIRED_DAYS 天则删除
     */
    public static final Integer NIGHT_WATCH_EXPIRED_DAYS = 10;
    //public static final String ENV_PROD = "prod";

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(8, 16,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "CameraServiceI");

    @Override
    public ResponseEntity<String> saveCameraDownloadTask(CamareMediaDownloadRequestDTO request) {
        if (request.getTaskCustomType() == null) {
            request.setTaskCustomType(0);
        }

        // 如果有 故障ID，没有 elevatorCode, 则可通过故障ID查询
        if (StringUtils.hasText(request.getTaskCustomId()) && request.getTaskCustomType() > 0
                && !StringUtils.hasText(request.getElevatorCode())) {
            var fault = faultMapper.getById(request.getTaskCustomId());
            if (fault == null) {
                return ResponseEntity.ok("不存在的故障ID！");
            }
            request.setElevatorCode(fault.getVElevatorCode());

            if (!StringUtils.hasText(request.getStartTime()) || !StringUtils.hasText(request.getEndTime())) {
                Date occurTime = new Date();    // 这个逻辑需要在确认
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(occurTime);
                Date start;
                Date end;
                if (GovernFaultTypeEnum.ENTRAP.getShmashineCode().equals(String.valueOf(request.getTaskCustomType()))
                        || GovernFaultTypeEnum.ENTRAP2.getShmashineCode().equals(String.valueOf(request.getTaskCustomType()))) {
                    calendar.add(Calendar.SECOND, -120);
                    start = calendar.getTime();
                    calendar.setTime(occurTime);
                    calendar.add(Calendar.SECOND, +600);
                    end = calendar.getTime();
                } else if (GovernFaultTypeEnum.ELECTRIC_VEHICLE_LADDER.getShmashineCode().equals(String.valueOf(request.getTaskCustomType()))) {
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
            return ResponseEntity.ok("未安装梯不能下载图像或视频！");
        }

        var camera = cameraMapper.getCameraInfoByElevatorCode(request.getElevatorCode());
        if (camera == null || !StringUtils.hasText(camera.getCloudNumber())) {
            return ResponseEntity.ok("未获取到摄像头相关信息！");
        }


        var cameraDownloadTask = covertor.dto2Entity(request, camera.getCameraType(), camera.getCloudNumber());
        // 存储记录，等待下载
        cameraDownloadTaskMapper.insert(cameraDownloadTask);

        return ResponseEntity.ok("已加入执行队列，请稍等...");
    }

    @Override
    public ResponseEntity<String> removeTaskAndOssFile(Long id) {
        var task = cameraDownloadTaskMapper.getById(id);
        if (task == null) {
            return ResponseEntity.ok("任务不存在（" + id + "）！");
        }
        // 删除OSS文件
        if (StringUtils.hasText(task.getOssUrl())) {
            Arrays.stream(task.getOssUrl().split(",")).toList().forEach(ossUrl -> {
                //OssInternalUtils.delFile(ossUrl.trim().replace(OssInternalUtils.OSS_URL, ""),
                // SpringContextUtil.getProfiles().contains(ENV_PROD));
                OssInternalUtils.delFile(ossUrl.trim().replace(OssInternalUtils.OSS_URL, ""),
                        aliOssProperties.getUseInternal());
            });
        }
        // 删除记录
        cameraDownloadTaskMapper.deleteById(id);
        return ResponseEntity.ok("操作成功！");
    }

    @Override
    public void checkAndDownloadCameraFile() {
        var tasks = cameraDownloadTaskMapper.getByFileStatus(
                        Collections.singletonList(CameraDownloadFileStatusEnum.WAITING.getStatus()))
                .stream()
                .filter(item -> CameraTypeEnum.HIK_CLOUD.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        tasks.forEach(task -> executorService.submit(() -> {
            doBeginDownloadCameraFileLocal(task);
        }));
    }

    @Override
    public void checkAndDoNextDownloadingTask() {
        var tasks = cameraDownloadTaskMapper.getByFileStatus(
                        Collections.singletonList(CameraDownloadFileStatusEnum.DOWNLOADING.getStatus()))
                .stream()
                .filter(item -> CameraTypeEnum.HIK_CLOUD.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        tasks.forEach(task -> executorService.submit(() -> {
            doDownloadingCameraFile(task);
        }));
    }

    @Override
    public void checkAndDoNextOssUploadTask() {
        var fileStatus = new ArrayList<Integer>() {
            {
                add(CameraDownloadFileStatusEnum.WAIT_UPLOAD_OSS.getStatus());
                add(CameraDownloadFileStatusEnum.UPLOAD_OSS_FAILED.getStatus());
            }
        };
        var tasks = cameraDownloadTaskMapper.getByFileStatus(fileStatus)
                .stream()
                .filter(item -> CameraTypeEnum.HIK_CLOUD.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        tasks.forEach(task -> executorService.submit(() -> {
            startOssUploadCameraFile(task);
        }));
    }

    @Override
    public void retryFailedCameraFile() {
        var tasks = cameraDownloadTaskMapper.getByFileStatus(
                        Collections.singletonList(CameraDownloadFileStatusEnum.REQUEST_FAILED.getStatus()))
                .stream()
                .filter(item -> CameraTypeEnum.HIK_CLOUD.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        tasks.forEach(task -> executorService.submit(() -> {
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
                .filter(item -> CameraTypeEnum.HIK_CLOUD.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        tasks.forEach(task -> removeTaskAndOssFile(task.getId()));
    }

    @Override
    public ResponseEntity<List<TblCameraDownloadTaskEntity>> listSuccessDownloadRecords(
            CamaraMediaDownloadBaseRequestDTO request) {
        return ResponseEntity.ok(cameraDownloadTaskMapper.findByEntity(TblCameraDownloadTaskEntity.builder()
                .elevatorCode(request.getElevatorCode())
                .floor(request.getFloor())
                .taskType(request.getTaskType() == null ? null : request.getTaskType().getCode())
                .mediaType(request.getMediaType() == null ? null : request.getMediaType().getMediaType())
                .fileStatus(CameraDownloadFileStatusEnum.SUCCESS.getStatus())
                .build()));
    }

    @Override
    public Integer syncCascadeChannelToDb() {
        // 获取云眸的国标级联相关数据，并级联
        var cascadeConfigs = hikPlatformUtil.listCascadePlatform();
        if (CollectionUtils.isEmpty(cascadeConfigs)) {
            return 0;
        }
        var total = new AtomicInteger();
        // 获取所有摄像头信息，并使用cloudNumber作为key, elevatorCode作为value
        var cameraMap = cameraMapper.listCamerasByCameraType(CameraTypeEnum.HIK_CLOUD.getCode())
                .stream()
                .collect(Collectors.toMap(TblCameraDTO::getCloudNumber, TblCameraDTO::getElevatorCode));

        cascadeConfigs.forEach(cascadeConfig -> {
            var cascadeChannels = hikPlatformUtil.listCascadeChannel(cascadeConfig.getConfigId());
            if (CollectionUtils.isEmpty(cascadeChannels)) {
                return;
            }
            cascadeChannels.forEach(cascadeChannel -> {
                cameraCascadePlatformMapper.insert(TblCameraCascadePlatformEntity.builder()
                        .cloudNumber(cascadeChannel.getDeviceSerial())
                        .platformId(cascadeConfig.getConfigId())
                        .platformName(cascadeConfig.getSupPlatformName())
                        .supOrSub(1)
                        .elevatorCode(cameraMap.getOrDefault(cascadeChannel.getDeviceSerial(), null))
                        .cameraType(CameraTypeEnum.HIK_CLOUD.getCode())
                        .channelId(cascadeChannel.getChannelId())
                        .channelNo(cascadeChannel.getChannelNo())
                        .channelCode(cascadeChannel.getChanelCode())
                        .build());
            });
            total.addAndGet(cascadeChannels.size());
        });
        return total.get();
    }

    /**
     * 此方法需要开通云录制服务，暂时不用
     *
     * @param task 任务
     */
    @Deprecated
    private void doBeginDownloadCameraFile(TblCameraDownloadTaskEntity task) {
        // 重新获取任务状态，如果是执行中或成功，则忽略
        var fileStatus = cameraDownloadTaskMapper.getFileStatusById(task.getId());
        if (Objects.equals(CameraDownloadFileStatusEnum.SUCCESS.getStatus(), fileStatus)
                || CameraDownloadFileStatusEnum.START_DEAL.getStatus().equals(fileStatus)
                || CameraDownloadFileStatusEnum.DOWNLOADING.getStatus().equals(fileStatus)) {
            return;
        }
        // 设置任务开始
        setDownloadStatus(task.getId(), CameraDownloadFileStatusEnum.START_DEAL);
        var projectInfo = getProjectInfoByTaskType(task.getTaskType());
        var res = CameraMediaTypeEnum.getMediaTypeListByFileType("image").contains(task.getMediaType().toLowerCase())
                ? hikPlatformUtil.doRecordHistoryImage(task, projectInfo)
                : hikPlatformUtil.doRecordHistoryVideo(task, projectInfo);
        if (!isSuccess(res)) {
            setTaskRequestFailed(task, res);
            return;
        }
        // 云眸通过消息将正在处理的任务转换成下载中， 这里不更改状态
        var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                .cloudTaskId(res.getData().getTaskId())
                .build();
        cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
        return;
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
            //doDownloadImage(task);
            // TODO  云眸暂时不支持 图片格式获取
            var res = new ResponseCustom();
            res.setCode(500);
            res.setMessage("云眸暂时不支持历史图片获取");
            setTaskRequestFailed(task, res);
            return;
        } else {
            // 开始本地录制
            // 流播放协议，2-hls、3-rtmp、4-flv
            var protocol = "3";
            // 视频清晰度，1-高清，2-标清
            var quality = 2;
            // 过期时间，单位秒
            var expireTime = "86400";
            var startTime = DateUtil.format(DateUtil.parse(task.getStartTime(), DatePattern.PURE_DATETIME_PATTERN),
                    DatePattern.NORM_DATETIME_PATTERN);
            var endTime = DateUtil.format(DateUtil.parse(task.getEndTime(), DatePattern.PURE_DATETIME_PATTERN),
                    DatePattern.NORM_DATETIME_PATTERN);
            //获取视频回放流地址
            var playbackRes = hikPlatformUtil.getPlaybackURL(task.getCloudNumber(), protocol, quality, startTime,
                    endTime, expireTime);
            if (!isSuccess(playbackRes)) {
                setTaskRequestFailed(task, playbackRes);
                return;
            }
            // 存储视频回放地址
            // 下载视频，上传阿里云
            var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                    .fileStatus(CameraDownloadFileStatusEnum.WAIT_UPLOAD_OSS.getStatus())
                    .sourceUrl(playbackRes.getData().getUrl())
                    .returnCode(playbackRes.getCode())
                    .errMessage(playbackRes.getMessage())
                    .build();
            cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
            return;
        }
    }


    private Integer setDownloadStatus(Long id, CameraDownloadFileStatusEnum statusEnum) {
        return cameraDownloadTaskMapper.updateFileStatusById(id, statusEnum.getStatus());
    }

    private void doDownloadingCameraFile(TblCameraDownloadTaskEntity task) {
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
    }

    private void doDownloadingCameraFileUrl(TblCameraDownloadTaskEntity task) {
        var project = getProjectInfoByTaskType(task.getTaskType());
        var res = hikPlatformUtil.getRecordFileDownloadUrl(task, project);
        if (!isSuccess(res)) {
            setTaskRequestFailed(task, res);
            return;
        }
        var expire = res.getData().getExpire();
        var nowMillis = DateTime.now().getTime();
        if (nowMillis > expire) {
            setTaskRequestFailed(task, ResponseCustom.buildFailed(500, "下载链接已过期"));
            return;
        }
        var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                .sourceUrl(StringUtils.collectionToCommaDelimitedString(res.getData().getUrls()))
                .returnCode(res.getCode())
                .errMessage(res.getMessage())
                .fileStatus(CameraDownloadFileStatusEnum.WAIT_UPLOAD_OSS.getStatus())
                .build();
        cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
    }

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

        }
    }

    private void doOssUploadCameraFile(TblCameraDownloadTaskEntity task) {
        log.info("oss-upload-camera-file taskid {}, url:{}", task.getTaskCustomId(), task.getSourceUrl());
        if (!task.getSourceUrl().startsWith("http")) {
            doGetFileBytesAndUploadOss(task);
            return;
        }

        String ossUrlPre = "Oreo_Project/" + CameraTaskTypeEnum.getPathByCode(task.getTaskType()) + "/"
                + DateUtil.today().replace("-", "/") + "/";
        //  + task.getTaskCustomId() + "." + task.getMediaType()

        if (!StringUtils.hasText(task.getSourceUrl())) {
            return;
        }
        var sourceUris = Collections.singletonList(task.getSourceUrl());
        if (task.getSourceUrl().contains(",")) {
            sourceUris = Arrays.asList(task.getSourceUrl().split(","));
        }

        var i = 0;
        var ossUrlPathList = new ArrayList<String>();
        for (var url : sourceUris) {
            var ossUrl = ossUrlPre + task.getTaskCustomId() + i + "." + task.getMediaType();
            ossUrlPathList.add(ossUrl);
            try {
                var bytes = url.trim().startsWith("/") ? FileUtil.getBytesByFile(url.trim()) :
                        FileUtil.getBytesByRemotePath(url.trim());
                if (bytes.length == 0) {
                    throw new Exception("文件内容为空");
                }
                //OssInternalUtils.setOSS(bytes, ossUrl, SpringContextUtil.getProfiles().contains(ENV_PROD));
                OssInternalUtils.setOSS(bytes, ossUrl, aliOssProperties.getUseInternal());
            } catch (Exception e) {
                var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                        .fileStatus(CameraDownloadFileStatusEnum.UPLOAD_OSS_FAILED.getStatus())
                        .returnCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errMessage(e.getMessage())
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
            // 发送任务完成消息给sender服务
            sendTaskFinishedToSenderService(task.getTaskCustomId());
        }
    }

    /**
     * 视频流的先下载到本地在上传到OSS
     *
     * @param task 任务
     */
    private void doGetFileBytesAndUploadOss(TblCameraDownloadTaskEntity task) {
        if (!StringUtils.hasText(task.getSourceUrl())) {
            return;
        }
        setDownloadStatus(task.getId(), CameraDownloadFileStatusEnum.START_DEAL);
        var localUrl = task.getSourceUrl();
        var fileName = localUrl.split("/")[localUrl.split("/").length - 1];
        // 路径是 rtmp格式的网址需要下载视频到本地
        if (!task.getSourceUrl().startsWith("/")) {
            var recordResult = covertCameraRecordToLocal(task);
            if (CollectionUtils.isEmpty(recordResult)) {
                return;
            }
            localUrl = recordResult.get("localUrl");
            fileName = recordResult.get("fileName");
        }

        String ossUrlPre = "Oreo_Project/" + CameraTaskTypeEnum.getPathByCode(task.getTaskType()) + "/"
                + DateUtil.today().replace("-", "/") + "/";

        var ossUrl = ossUrlPre + fileName;
        try {
            var bytes = localUrl.startsWith("/") ? FileUtil.getBytesByFile(localUrl) :
                    FileUtil.getBytesByRemotePath(localUrl);
            //OssInternalUtils.setOSS(bytes, ossUrl, SpringContextUtil.getProfiles().contains(ENV_PROD));
            OssInternalUtils.setOSS(bytes, ossUrl, aliOssProperties.getUseInternal());
        } catch (Exception e) {
            setTaskUploadFileFailed(task, ResponseCustom.buildFailed(500, e.getMessage()));
            return;
        }
        var ossUrlFull = OssInternalUtils.OSS_URL + ossUrl;

        var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                .fileStatus(CameraDownloadFileStatusEnum.SUCCESS.getStatus())
                .ossUrl(ossUrlFull)
                .returnCode(HttpStatus.OK.value())
                .errMessage("SUCCESS")
                .build();
        cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
    }

    private Map<String, String> covertCameraRecordToLocal(TblCameraDownloadTaskEntity task) {
        // var recordStatusRedisKey = "HIKCLOUD:CAMERA:RECORDING:" + task.getCloudNumber();
        var recordStatusRedisKey = RedisKeyUtils.getHikCloudRecordVideoStatusKey(task.getCloudNumber());
        // 判断是否有已经在取视频的记录
        var hasRecording = redisUtils.setIfAbsent(recordStatusRedisKey, DateUtil.now(), 10L, TimeUnit.MINUTES);
        if (!hasRecording) {
            // 恢复原来的待上传阿里云状态
            setDownloadStatus(task.getId(), CameraDownloadFileStatusEnum.WAIT_UPLOAD_OSS);
            return null;
        }

        var filePath = HikvPlatformConstants.PATH_FILE_DOWNLOAD_LOCAL + DateUtil.today() + "/";
        var localUrl = videoStreamDownloadHandle.run(task.getSourceUrl(), filePath, task.getTaskCustomId());
        // 删除正在取视频的记录
        redisUtils.deleteObject(recordStatusRedisKey);
        if (!StringUtils.hasText(localUrl)) {
            setTaskUploadFileFailed(task, ResponseCustom.buildFailed(500, "下载到本地视频失败"));
            return null;
        }
        // 将 rtmp地址修改为本地地址
        var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                .fileStatus(CameraDownloadFileStatusEnum.START_DEAL.getStatus())
                .sourceUrl(localUrl)
                .errMessage("视频已存储到本地")
                .build();
        cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
        var fileName = localUrl.replace(filePath, "");
        return Map.of("localUrl", localUrl, "fileName", fileName);
    }


    /**
     * 云眸项目管理
     */
    private TblCameraHikCloudProjectEntity getProjectInfoByTaskType(Integer taskType) {
        // path 为 projectCode
        var projectEnum = CameraTaskTypeEnum.getByCode(taskType);
        var info = cameraHikCloudProjectMapper.getByProjectCode(projectEnum.getPath());
        if (info == null) {
            // 云眸创建项目
            info = createProjectInfoByTaskType(projectEnum);
            cameraHikCloudProjectMapper.insert(info);
        }
        return info;
    }

    /**
     * 云眸创建项目
     *
     * @param projectEnum 项目枚举
     * @return 项目信息
     */
    private TblCameraHikCloudProjectEntity createProjectInfoByTaskType(CameraTaskTypeEnum projectEnum) {
        // 3天过期
        var expireDays = 1;
        // 不限制流量
        var flowLimit = -1L;

        var projectInfo = HikCloudProjectCreateRequestDTO.builder()
                .projectName(projectEnum.getDescription())
                .expireDays(expireDays)
                .flowLimit(flowLimit)
                .build();
        var result = hikPlatformUtil.createProject(projectInfo);
        if (!isSuccess(result)) {
            throw new RuntimeException("创建云眸项目失败");
        }

        return TblCameraHikCloudProjectEntity.builder()
                .projectCode(projectEnum.getPath())
                .projectName(projectEnum.getDescription())
                .hikCloudProjectId(result.getData().getProjectId())
                .hikCloudExpireDays(expireDays)
                .hikCloudFlowLimit(flowLimit)
                .build();
    }

    private <T extends ResponseCustom> Boolean isSuccess(T response) {
        return response != null && (0 == response.getCode() || 200 == response.getCode());
    }

    private <T extends ResponseCustom> void setTaskRequestFailed(TblCameraDownloadTaskEntity task, T res) {
        var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                .fileStatus(CameraDownloadFileStatusEnum.REQUEST_FAILED.getStatus())
                .returnCode(res.getCode())
                .errMessage(res.getMessage())
                .build();
        cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
        cameraDownloadTaskMapper.increaseRequestFailedCount(task.getId());
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


    /**
     * 发送任务完成消息给sender服务
     *
     * @param faultId 故障ID
     */
    private void sendTaskFinishedToSenderService(String faultId) {
        log.info("sendTaskFinishedToSenderService faultId：{}", faultId);
        try {
            //拼接请求参数
            String url = properties.getSenderServer()
                    + "/sender/fault/resend/{faultId}/{cameraType}".replace("{faultId}", faultId)
                    .replace("{cameraType}", String.valueOf(CameraTypeEnum.HIK_CLOUD.getCode()));
            var res = HttpUtil.get(url, 15000);
            log.info("发送任务完成消息给sender服务，故障id：[{}]，返回结果：[{}]", faultId, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
