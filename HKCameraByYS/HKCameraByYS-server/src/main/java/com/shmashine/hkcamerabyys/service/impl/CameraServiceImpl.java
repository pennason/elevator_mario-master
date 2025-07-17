// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.service.impl;

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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.shmashine.common.dto.CamaraMediaDownloadBaseRequestDTO;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.ImageRecognitionMattingConfigEntity;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;
import com.shmashine.common.entity.TblCameraImageIdentifyEntity;
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
import com.shmashine.hkcamerabyys.convert.CameraMediaDownloadCovertor;
import com.shmashine.hkcamerabyys.dao.HKCameraByYSDao;
import com.shmashine.hkcamerabyys.dao.TblCameraDownloadTaskMapper;
import com.shmashine.hkcamerabyys.dao.TblCameraImageIdentifyMapper;
import com.shmashine.hkcamerabyys.dao.TblCameraMapper;
import com.shmashine.hkcamerabyys.dao.TblElevatorMapper;
import com.shmashine.hkcamerabyys.dao.TblFaultMapper;
import com.shmashine.hkcamerabyys.entity.ResponseCustom;
import com.shmashine.hkcamerabyys.entity.ResponseDeviceEntityHaikang;
import com.shmashine.hkcamerabyys.properties.EndpointProperties;
import com.shmashine.hkcamerabyys.service.CameraServiceI;
import com.shmashine.hkcamerabyys.service.CameraTypeServiceI;
import com.shmashine.hkcamerabyys.utils.BeanFactoryTools;
import com.shmashine.mgppf.components.dto.enums.GovernFaultTypeEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 14:23
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CameraServiceImpl implements CameraServiceI {

    private static Logger downloadDebugLog = LoggerFactory.getLogger("downloadDebugLogger");

    //@Value("${spring.profiles.active}")
    //private String env;
    private final CameraMediaDownloadCovertor covertor;
    private final TblElevatorMapper elevatorMapper;
    private final TblCameraMapper cameraMapper;
    private final TblFaultMapper faultMapper;
    private final TblCameraDownloadTaskMapper cameraDownloadTaskMapper;
    private final TblCameraImageIdentifyMapper cameraImageIdentifyMapper;
    private final RedissonClient redissonClient;
    private final EndpointProperties properties;
    private final AliOssProperties aliOssProperties;
    private final HKCameraByYSDao hkCameraByYSDao;
    private final RedisTemplate redisTemplate;

    //public static final String OSS_MAIXIN_DOMAIN = "https://oss-maixin.oss-cn-shanghai.aliyuncs.com/";
    /**
     * 夜间守护模式记录超过 NIGHT_WATCH_EXPIRED_DAYS 天则删除
     */
    public static final Integer NIGHT_WATCH_EXPIRED_DAYS = 10;
    // public static final String ENV_PROD = "prod";

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(32, 512,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(300), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "CameraService");

    private final ExecutorService sendExecutorService = new ShmashineThreadPoolExecutor(8, 32,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("sendExecutorService"), "CameraService");


    //CHECKSTYLE:OFF
    @Override
    public void saveCameraDownloadTask(CamareMediaDownloadRequestDTO request) {

        downloadDebugLog.info("saveCameraDownloadTask-收到取证下载任务，taskId:{}, elevatorCode: {}",
                request.getTaskCustomId(), request.getElevatorCode());

        if (request.getTaskCustomType() == null) {
            request.setTaskCustomType(0);
        }

        // 如果有 故障ID，没有 elevatorCode, 则可通过故障ID查询
        if (!StringUtils.hasText(request.getElevatorCode()) && StringUtils.hasText(request.getTaskCustomId())
                && request.getTaskCustomType() > 0) {

            var fault = faultMapper.getById(request.getTaskCustomId());
            if (fault == null) {
                log.info("saveCameraDownloadTask - 不存在的故障ID,req: {}", request);
                return;
            }
            request.setElevatorCode(fault.getVElevatorCode());
        }

        if (CameraMediaTypeEnum.MP4.equals(request.getMediaType())) {

            // 如果没有传开始时间和结束时间，则计算开始时间结束时间
            if (!StringUtils.hasText(request.getStartTime()) || !StringUtils.hasText(request.getEndTime())) {

                String collectTime = request.getCollectTime();
                Date occurTime;
                if (StringUtils.hasText(collectTime)) {
                    occurTime = DateUtil.parseTime(collectTime);
                } else {
                    occurTime = new Date();
                }
                // 这个逻辑需要在确认
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
            log.info("saveCameraDownloadTask - 未安装梯不能下载图像或视频！,req: {}", request);
            return;
        }

        var camera = cameraMapper.getCameraInfoByElevatorCode(request.getElevatorCode());
        if (camera == null || !StringUtils.hasText(camera.getCloudNumber())) {
            log.info("saveCameraDownloadTask - 未获取到摄像头相关信息！,req: {}", request);
            return;
        }

        var cameraDownloadTask = covertor.dto2Entity(request, camera.getCameraType(), camera.getCloudNumber());

        // 存储记录，等待下载
        cameraDownloadTaskMapper.insert(cameraDownloadTask);

        //请求下载
        executorService.submit(() -> {

            downloadDebugLog.info("saveCameraDownloadTask-开始下载，taskId:{}, elevatorCode: {}",
                    request.getTaskCustomId(), request.getElevatorCode());

            var cameraTypeService = getCameraTypeBean(camera.getCameraType());
            cameraTypeService.downloadVideoOrImage(cameraDownloadTask);
        });

    }
    //CHECKSTYLE:ON

    @Override
    public CameraTypeServiceI getCameraTypeBean(Integer cameraType) {
        var typeSlug = CameraTypeEnum.getByCode(cameraType).getSlug();
        var beanName = "cameraTypeServiceImpl" + typeSlug.substring(0, 1).toUpperCase() + typeSlug.substring(1);
        log.info("camera use bean: {}", beanName);
        return BeanFactoryTools.getBean(beanName);
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
                .filter(item -> CameraTypeEnum.HIK_EZVIZ.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        // 设置最小值为1最大32
        int corePoolSize = Math.max(1, (tasks.size() / 2) > 32 ? 32 : tasks.size() / 2);
        ExecutorService checkAndDownloadCameraFileExecutor = new ShmashineThreadPoolExecutor(corePoolSize,
                corePoolSize,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(Math.max(tasks.size(), 1)), // 如果tasks非常多，可能需要调整参数
                ShmashineThreadFactory.of(),
                PersistentRejectedExecutionHandler.of("checkAndDownloadCameraFile"), "CameraService");

        try {

            CompletableFuture<?>[] completableFutures = tasks.stream()
                    .map(task -> CompletableFuture.runAsync(() -> dobeginDownloadCameraFile(task),
                            checkAndDownloadCameraFileExecutor)).toArray(CompletableFuture[]::new);

            try {
                CompletableFuture.allOf(completableFutures).get(100, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("批量执行任务列表中的任务失败，error：{}", ExceptionUtils.getStackTrace(e));
                throw new RuntimeException(e);
            }

        } finally {
            checkAndDownloadCameraFileExecutor.shutdown();
            try {
                if (!checkAndDownloadCameraFileExecutor.awaitTermination(10, TimeUnit.SECONDS)) { // 等待终止
                    checkAndDownloadCameraFileExecutor.shutdownNow(); // 取消当前执行的任务
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // 再次设置中断状态
                checkAndDownloadCameraFileExecutor.shutdownNow(); // 取消当前执行的任务
            }
        }
    }

    @Override
    public void checkAndDoNextDownloadingTask() {

        var tasks = cameraDownloadTaskMapper.getByFileStatus(
                        Collections.singletonList(CameraDownloadFileStatusEnum.DOWNLOADING.getStatus()))
                .stream()
                .filter(item -> CameraTypeEnum.HIK_EZVIZ.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        // 设置最小值为1最大32
        int corePoolSize = Math.max(1, (tasks.size() / 2) > 32 ? 32 : tasks.size() / 2);
        ExecutorService checkAndDoNextDownloadingTaskExecutor = new ShmashineThreadPoolExecutor(
                corePoolSize, corePoolSize,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(Math.max(tasks.size(), 1)), // 如果tasks非常多，可能需要调整参数
                ShmashineThreadFactory.of(),
                PersistentRejectedExecutionHandler.of("checkAndDoNextDownloadingTask"), "CameraService");

        try {

            CompletableFuture<?>[] completableFutures = tasks.stream()
                    .map(task -> CompletableFuture.runAsync(() -> doDownloadingCameraFile(task),
                            checkAndDoNextDownloadingTaskExecutor)).toArray(CompletableFuture[]::new);

            try {
                CompletableFuture.allOf(completableFutures).get(100, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("批量执行状态是 DOWNLOADING 的 任务 后台获取任务状态并更新任务失败，error：{}", ExceptionUtils.getStackTrace(e));
            }

        } finally {
            checkAndDoNextDownloadingTaskExecutor.shutdown();
            try {
                if (!checkAndDoNextDownloadingTaskExecutor.awaitTermination(10, TimeUnit.SECONDS)) { // 等待终止
                    checkAndDoNextDownloadingTaskExecutor.shutdownNow(); // 取消当前执行的任务
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // 再次设置中断状态
                checkAndDoNextDownloadingTaskExecutor.shutdownNow(); // 取消当前执行的任务
            }
        }

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
                .filter(item -> CameraTypeEnum.HIK_EZVIZ.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        // 设置最小值为1最大32
        int corePoolSize = Math.max(1, (tasks.size() / 2) > 32 ? 32 : tasks.size() / 2);
        ExecutorService checkAndDoNextOssUploadTaskExecutor = new ShmashineThreadPoolExecutor(
                corePoolSize, corePoolSize,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(Math.max(tasks.size(), 1)), // 如果tasks非常多，可能需要调整参数
                ShmashineThreadFactory.of(),
                PersistentRejectedExecutionHandler.of("checkAndDownloadCameraFile"), "CameraService");

        try {

            CompletableFuture<?>[] completableFutures = tasks.stream()
                    .map(task -> CompletableFuture.runAsync(() -> startOssUploadCameraFile(task),
                            checkAndDoNextOssUploadTaskExecutor)).toArray(CompletableFuture[]::new);

            try {
                CompletableFuture.allOf(completableFutures).get(100, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("批量执行状态为 WAIT_UPLOAD_OSS 的任务后台更新到OSS上任务失败，error：{}", ExceptionUtils.getStackTrace(e));
                throw new RuntimeException(e);
            }

        } finally {
            checkAndDoNextOssUploadTaskExecutor.shutdown();
            try {
                if (!checkAndDoNextOssUploadTaskExecutor.awaitTermination(10, TimeUnit.SECONDS)) { // 等待终止
                    checkAndDoNextOssUploadTaskExecutor.shutdownNow(); // 取消当前执行的任务
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // 再次设置中断状态
                checkAndDoNextOssUploadTaskExecutor.shutdownNow(); // 取消当前执行的任务
            }
        }
    }

    @Override
    public void retryFailedCameraFile(int start, int end) {
        var tasks = cameraDownloadTaskMapper.getByFileStatusAndFailedCount(
                        Collections.singletonList(CameraDownloadFileStatusEnum.REQUEST_FAILED.getStatus()), start, end)
                .stream()
                .filter(item -> CameraTypeEnum.HIK_EZVIZ.getCode().equals(item.getCameraType()))
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        // 设置最小值为1最大32
        int corePoolSize = Math.max(1, (tasks.size() / 2) > 32 ? 32 : tasks.size() / 2);
        ExecutorService retryFailedCameraFileExecutor = new ShmashineThreadPoolExecutor(
                corePoolSize, corePoolSize,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(Math.max(tasks.size(), 1)), // 如果tasks非常多，可能需要调整参数
                ShmashineThreadFactory.of(),
                PersistentRejectedExecutionHandler.of("retryFailedCameraFileExecutor"), "CameraService");

        try {

            CompletableFuture<?>[] completableFutures = tasks.stream()
                    .map(task -> CompletableFuture.runAsync(() -> dobeginDownloadCameraFile(task),
                            retryFailedCameraFileExecutor)).toArray(CompletableFuture[]::new);

            try {
                CompletableFuture.allOf(completableFutures).get(100, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("批量执行任务列表中的任务失败，error：{}", ExceptionUtils.getStackTrace(e));
                throw new RuntimeException(e);
            }

        } finally {
            retryFailedCameraFileExecutor.shutdown();
            try {
                if (!retryFailedCameraFileExecutor.awaitTermination(10, TimeUnit.SECONDS)) { // 等待终止
                    retryFailedCameraFileExecutor.shutdownNow(); // 取消当前执行的任务
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // 再次设置中断状态
                retryFailedCameraFileExecutor.shutdownNow(); // 取消当前执行的任务
            }
        }

    }

    @Override
    public void deleteNightWatchExpiredRecord() {
        // 当前时间向前推 NIGHT_WATCH_EXPIRED_DAYS 天
        var expiredDate = DateTime.of(DateTime.now().getTime() - NIGHT_WATCH_EXPIRED_DAYS * 24 * 3600 * 1000);
        var tasks = cameraDownloadTaskMapper.listTaskTypeExpiredRecords(CameraTaskTypeEnum.NIGHT_WATCH.getCode(),
                new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN).format(expiredDate));

        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }

        // 设置最小值为1最大32
        int corePoolSize = Math.max(1, (tasks.size() / 2) > 32 ? 32 : tasks.size() / 2);
        ExecutorService checkAndDownloadCameraFileExecutor = new ShmashineThreadPoolExecutor(
                corePoolSize, corePoolSize,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(Math.max(tasks.size(), 1)), // 如果tasks非常多，可能需要调整参数
                ShmashineThreadFactory.of(),
                PersistentRejectedExecutionHandler.of("checkAndDownloadCameraFile"), "CameraService");

        try {

            CompletableFuture<?>[] completableFutures = tasks.stream()
                    .map(task -> CompletableFuture.runAsync(() -> removeTaskAndOssFile(task.getId()),
                            checkAndDownloadCameraFileExecutor)).toArray(CompletableFuture[]::new);

            try {
                CompletableFuture.allOf(completableFutures).get(100, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("批量执行清理守夜模式过期的记录任务失败，error：{}", ExceptionUtils.getStackTrace(e));
                throw new RuntimeException(e);
            }

        } finally {
            checkAndDownloadCameraFileExecutor.shutdown();
            try {
                if (!checkAndDownloadCameraFileExecutor.awaitTermination(10, TimeUnit.SECONDS)) { // 等待终止
                    checkAndDownloadCameraFileExecutor.shutdownNow(); // 取消当前执行的任务
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // 再次设置中断状态
                checkAndDownloadCameraFileExecutor.shutdownNow(); // 取消当前执行的任务
            }
        }

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
    public ResponseEntity<String> renewCameraOnlineStatus() {
        return cameraMapper.listCloudNumbers()
                .stream()
                .map(cloudNumber -> {
                    var res = getCameraTypeBean(CameraTypeEnum.HIK_EZVIZ.getCode())
                            .renewCameraOnlineStatus(cloudNumber);

                    return updateCameraOnlineStatus(cloudNumber, res);
                })
                .reduce((a, b) -> a + "\n" + b)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok("操作失败！"));
    }

    @Override
    public void startOssUploadCameraFileByTaskId(String taskId) {

        TblCameraDownloadTaskEntity task = cameraDownloadTaskMapper.getByByTaskId(taskId);

        if (task == null) {
            return;
        }

        downloadDebugLog.info("上传阿里云-开始 , taskId {}", taskId);

        var lock = redissonClient.getLock("CAMERA:FILE:LOCK:OSS_UPLOAD:" + task.getTaskCustomId());

        try {
            //尝试加锁，最多等待10秒，上锁以后3分钟自动解锁
            if (lock.tryLock(10, 180, TimeUnit.SECONDS)) {
                try {
                    //获取文件地址
                    var cameraTypeService = getCameraTypeBean(task.getCameraType());
                    if (!StringUtils.hasText(task.getSourceUrl())) {
                        cameraTypeService.doDownloadingCameraFile(task);
                    }
                    //文件上传OSS
                    doOssUploadCameraFile(task);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException ignored) {
            log.error("startOssUploadCameraFileByTaskId-获取锁失败，task: {}", task);
        }
    }

    @Override
    public void cloudRecordingFailed(String taskId, String errorCode, String errorMsg) {

        TblCameraDownloadTaskEntity task = cameraDownloadTaskMapper.getByByTaskId(taskId);

        if (task == null) {
            return;
        }

        var lock = redissonClient.getLock("CAMERA:FILE:LOCK:DOWNLOADING:" + task.getTaskCustomId());
        try {
            //尝试加锁，最多等待100秒，上锁以后3分钟自动解锁
            if (lock.tryLock(100, 180, TimeUnit.SECONDS)) {
                try {

                    var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                            .fileStatus(CameraDownloadFileStatusEnum.REQUEST_FAILED.getStatus())
                            .returnCode(Integer.parseInt(errorCode))
                            .errMessage(errorMsg)
                            .requestFailedCount(task.getRequestFailedCount() + 1)
                            .build();

                    cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);

                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException ignored) {
            log.error("cloudRecordingFailed-错误，error：{}", ExceptionUtils.getStackTrace(ignored));
        }
    }

    @Override
    public Integer getCameraOnlineStatusByElevatorCode(String elevatorCode) {
        return cameraMapper.getCameraOnlineStatusByElevatorCode(elevatorCode);
    }

    private void dobeginDownloadCameraFile(TblCameraDownloadTaskEntity task) {
        // 重新获取任务状态，如果是执行中或成功，则忽略
        var fileStatus = cameraDownloadTaskMapper.getFileStatusById(task.getId());
        if (Objects.equals(CameraDownloadFileStatusEnum.SUCCESS.getStatus(), fileStatus)
                || CameraDownloadFileStatusEnum.START_DEAL.getStatus().equals(fileStatus)
                || CameraDownloadFileStatusEnum.DOWNLOADING.getStatus().equals(fileStatus)) {
            return;
        }
        // 设置任务开始
        setDownloadStatus(task.getId(), CameraDownloadFileStatusEnum.START_DEAL);

        var cameraTypeService = getCameraTypeBean(task.getCameraType());
        cameraTypeService.downloadVideoOrImage(task);
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
            if (lock.tryLock(10, 180, TimeUnit.SECONDS)) {
                try {
                    var cameraTypeService = getCameraTypeBean(task.getCameraType());
                    cameraTypeService.doDownloadingCameraFile(task);
                } finally {
                    lock.unlock();
                }
            }
        } catch (Exception ignored) {
            log.error("doDownloadingCameraFile-错误，error：{}", ExceptionUtils.getStackTrace(ignored));
        }
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
            log.error("startOssUploadCameraFile-错误，error:{}", ExceptionUtils.getStackTrace(ignored));
        }
    }

    //CHECKSTYLE:OFF
    private void doOssUploadCameraFile(TblCameraDownloadTaskEntity task) {
        String ossUrlPre = "Oreo_Project/" + CameraTaskTypeEnum.getPathByCode(task.getTaskType()) + "/"
                + DateUtil.today().replace("-", "/") + "/";

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
                OssInternalUtils.setOSS(FileUtil.getBytesByRemotePath(url.trim()), ossUrl,
                        aliOssProperties.getUseInternal());

                downloadDebugLog.info("上传阿里云-存阿里云成功 , taskId {}", task.getTaskCustomId());

            } catch (Exception e) {
                var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                        .fileStatus(CameraDownloadFileStatusEnum.UPLOAD_OSS_FAILED.getStatus())
                        .returnCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errMessage(e.getMessage())
                        .build();
                cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
                cameraDownloadTaskMapper.increaseUploadFailedCount(task.getId());
                downloadDebugLog.info("上传阿里云-存阿里云异常 , taskId {}", task.getTaskCustomId());
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
            downloadDebugLog.info("上传阿里云-更新下载记录表 , taskId {}", task.getTaskCustomId());

            if (CameraTaskTypeEnum.FAULT.getCode().equals(task.getTaskType())) {

                //存sys_file表
                hkCameraByYSDao.addTblSysFile(IdUtil.getSnowflakeNextIdStr(), task.getTaskCustomId(),
                        StringUtils.collectionToCommaDelimitedString(ossUrlList), null,
                        2, new Date(), "jpg".equals(task.getMediaType()) ? "0" : "1");

                downloadDebugLog.info("上传阿里云-存sys_file表 , taskId {}", task.getTaskCustomId());

                if ("图像".equals(CameraMediaTypeEnum.getDescriptionMediaType(task.getMediaType()))) {
                    //图像二次识别
                    imageIdentification(StringUtils.collectionToCommaDelimitedString(ossUrlList),
                            String.valueOf(task.getTaskCustomType()), task.getTaskCustomId(), task.getElevatorCode());
                    downloadDebugLog.info("上传阿里云-图像二次识别结束 , taskId {}", task.getTaskCustomId());
                }

                // 发送任务完成消息给sender服务
                sendExecutorService.submit(() -> sendTaskFinishedToSenderService(task.getTaskCustomId()));
                downloadDebugLog.info("上传阿里云-发送任务完成消息给sender服务 , taskId {}", task.getTaskCustomId());
            }

            // 自研电动车识别 || 人流统计 的需要更新记录
            if (CameraTaskTypeEnum.ELECTRIC_BIKE_IDENTIFY.getCode().equals(task.getTaskType())
                    || CameraTaskTypeEnum.PEOPLE_FLOW_STATISTICS.getCode().equals(task.getTaskType())) {
                cameraImageIdentifyMapper.update(new TblCameraImageIdentifyEntity(task.getTaskCustomId())
                        .setOssUrl(ossUrlList.get(0))
                        .setStatus(1));
                downloadDebugLog.info("上传阿里云-更新图像识别记录表 , taskId {}", task.getTaskCustomId());
            }
        }

    } //CHECKSTYLE:ON

    private String updateCameraOnlineStatus(String cloudNumber, ResponseCustom res) {
        if (res == null || HttpStatus.OK.value() != res.getCode()) {
            return "更新云台在线状态失败，云台编号：" + cloudNumber + "，原因：" + (null == res ? "无返回" : res.getMessage());
        }
        var deviceInfo = (ResponseDeviceEntityHaikang.DeviceInfoHaikang) res.getData();
        cameraMapper.updateOnlineStatus(cloudNumber, deviceInfo.getStatus());
        return "更新云台在线状态成功，云台编号：" + cloudNumber + "，在线状态：" + deviceInfo.getStatus();
    }

    /**
     * 调用图像识别
     */
    private void imageIdentification(String picUrl, String faultType, String faultId, String elevatorCode) {

        downloadDebugLog.info("上传阿里云-图像二次识别开始 , taskId {}", faultId);

        try {
            if ("6".equals(faultType)) {
                // restTemplateSendMessage(faultId, picUrl, "person");
                ImageIdentifyUtils.identifyImage(faultId, picUrl, ImageIdentifyUtils.IDENTIFY_TYPE_PERSON,
                        aliOssProperties.getUseInternal());
                log.info("非平层停梯调用二次识别确认，故障id【{}】", faultId);
            }

            if ("37".equals(faultType)) {
                // restTemplateSendMessage(faultId, picUrl, "motorcycle");
                ImageIdentifyUtils.identifyImage(faultId, picUrl, ImageIdentifyUtils.IDENTIFY_TYPE_MOTOR_CYCLE,
                        aliOssProperties.getUseInternal());
                log.info("电动车乘梯调用二次识别确认，故障id【{}】", faultId);
            }

            //困人
            if ("7".equals(faultType) || "8".equals(faultType)) {

                Long remove = redisTemplate.opsForZSet().remove("HLS:IMAGE", faultId);

                if (remove > 0) {

                    try {
                        //图片下载成功释放锁
                        RLock lock = redissonClient.getLock("AFRESDOWNLOADIMAGE_LOCK" + faultId);
                        lock.forceUnlock();
                        log.info("---------平层困人图片下载成功，释放锁------------");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    downloadDebugLog.info("上传阿里云-图像二次识别-抠图开始 , taskId {}", faultId);
                    //抠图上传阿里云并返回文件地址
                    picUrl = imageMatting(faultId, picUrl, faultType, elevatorCode);
                    downloadDebugLog.info("上传阿里云-图像二次识别-抠图结束 , taskId {}", faultId);

                    //调用图像识别
                    // restTemplateSendMessage(faultId, picUrl, "person");
                    ImageIdentifyUtils.identifyImage(faultId, picUrl, ImageIdentifyUtils.IDENTIFY_TYPE_PERSON,
                            aliOssProperties.getUseInternal());
                    //添加识别标识
                    redisTemplate.opsForZSet().add("HLS:IDENTIFICATION", faultId, System.currentTimeMillis() / 1000);
                }
            }

        } catch (Exception e) {
            downloadDebugLog.info("调用识别服务失败：e： {}，故障id：{}，故障类型：{}", e.getMessage(), faultId, faultType);
        }
    }

    /**
     * 计算抠图
     *
     * @param faultId      故障id
     * @param picUrl       图片地址
     * @param faultType    故障类型
     * @param elevatorCode 电梯编号
     * @return 抠图后图片地址
     */
    private String imageMatting(String faultId, String picUrl, String faultType, String elevatorCode) {

        try {
            if (StrUtil.isBlank(elevatorCode)) {
                return picUrl;
            }
            //根据故障id或电梯抠图配置
            ImageRecognitionMattingConfigEntity config =
                    elevatorMapper.getImageMattingConfigByFaultType(elevatorCode, faultType);

            if (config == null) {
                return picUrl;
            }
            var realCoordinates = JSON.parseArray(config.getRealCoordinates(), JSONObject.class);

            if (realCoordinates == null || realCoordinates.size() == 0) {

                if (StrUtil.isBlank(config.getCoordinates())) {
                    return picUrl;
                }

                //抠图坐标点
                var points = JSON.parseArray(config.getCoordinates(), JSONObject.class);

                if (points == null || points.size() == 0) {
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
            String uri = "Oreo_Project/fault/" + DateUtil.today().replace("-", "/")
                    + "/" + faultId + "_imageMatting.jpg";

            OssInternalUtils.setOSS(FileUtil.getBytesByFile(toPath), uri, aliOssProperties.getUseInternal());
            log.info("------上传阿里云成功，故障id：[{}]", faultId);

            //删除本地文件
            new File(toPath).delete();

            return OssInternalUtils.OSS_URL + uri;
        } catch (Exception e) {
            log.error("抠图失败,faultId:[{}],error:[{}]", faultId, ExceptionUtils.getStackTrace(e));
        }

        return picUrl;
    }


    /**
     * 绘制并填充多边形
     *
     * @param srcImagePath 源图像路径
     * @param points       坐标数组
     * @param imageFormat  写入图形格式
     */
    /*private void drawAndAlphaPolygon(String srcImagePath, List<JSONObject> points,
                                     String imageFormat, String toPath) {

        FileOutputStream fos = null;
        try {
            //获取图片
            BufferedImage image = ImageIO.read(new URL(srcImagePath));
            //根据xy点坐标绘制闭合多边形
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.BLACK);

            for (JSONObject point : points) {
                int[] xpoints = point.get("x", int[].class);
                int[] ypoints = point.get("y", int[].class);
                g2d.fillPolygon(xpoints, ypoints, xpoints.length);
            }

            fos = new FileOutputStream(toPath);
            ImageIO.write(image, imageFormat, fos);

            g2d.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    /**
     * 获取图片分辨率
     *
     * @param imgPath 图片地址
     * @return
     */
    /*private Integer[] getImgSize(String imgPath) {
        Integer[] size = new Integer[2];
        try {
            //BufferedImage image = ImageIO.read(new File(imgPath));
            BufferedImage image = ImageIO.read(new URL(imgPath));
            size[0] = image.getWidth();
            size[1] = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
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
     * @return 实际坐标点
     */
    private List<JSONObject> getRealCoordinates(List<JSONObject> points,
                                                               Integer width, Integer height, Integer[] imgSize) {

        return points.stream().map(p -> {

            var pointsX = p.getObject("x", Integer[].class);
            var pointsY = p.getObject("y", Integer[].class);


            var res = Map.of("x", Arrays.stream(pointsX).mapToInt(x -> (x * imgSize[0]) / width).toArray(),
                    "y", Arrays.stream(pointsY).mapToInt(y -> (y * imgSize[1]) / height).toArray());

            return JSON.parseObject(JSON.toJSONString(res), JSONObject.class);
        }).toList();
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
        String url = "http://47.105.214.0:10089/?type=" + type + "&url=" + fileUrl + "&faultId=" + workOrderId;

        //异步请求
        try {
            HttpRequest.get(url).timeout(200).executeAsync();
        } catch (Exception e) {
            // 调用超时异常
        }
    }*/


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
                    .replace("{cameraType}", String.valueOf(CameraTypeEnum.HIK_EZVIZ.getCode()));
            var res = HttpUtil.get(url, 15000);
            log.info("发送任务完成消息给sender服务，故障id：[{}]，返回结果：[{}]", faultId, res);
        } catch (Exception e) {
            log.error("发送任务完成消息给sender服务异常，故障id：[{}], error:{}", faultId, ExceptionUtils.getStackTrace(e));
        }
    }
}
