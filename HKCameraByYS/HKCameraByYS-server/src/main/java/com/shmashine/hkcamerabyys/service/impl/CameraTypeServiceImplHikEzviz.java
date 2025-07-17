// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;
import com.shmashine.common.enums.CameraDownloadFileStatusEnum;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.common.enums.CameraTaskTypeEnum;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.hkcamerabyys.dao.TblCameraDownloadTaskMapper;
import com.shmashine.hkcamerabyys.entity.ResponseCustom;
import com.shmashine.hkcamerabyys.entity.ResponseDeviceEntityHaikang;
import com.shmashine.hkcamerabyys.entity.ResponseEntityHaikang;
import com.shmashine.hkcamerabyys.entity.ResponseFileEntityHaikang;
import com.shmashine.hkcamerabyys.entity.ResponseImageEntityHaikang;
import com.shmashine.hkcamerabyys.entity.ResponseTaskEntityHaikang;
import com.shmashine.hkcamerabyys.entity.ResponseTaskFileEntityHaikang;
import com.shmashine.hkcamerabyys.entity.ResponseTokenEntityHaikang;
import com.shmashine.hkcamerabyys.entity.TaskStatusRespVO;
import com.shmashine.hkcamerabyys.enums.HkYsFlowErrorEnum;
import com.shmashine.hkcamerabyys.service.CameraServiceI;
import com.shmashine.hkcamerabyys.service.CameraTypeServiceI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/10 9:50
 * @since v1.0
 */

@Slf4j
@Primary
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CameraTypeServiceImplHikEzviz implements CameraTypeServiceI {

    private static Logger downloadDebugLog = LoggerFactory.getLogger("downloadDebugLogger");


    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(32, 512,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(600), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("OssUpload"), "CameraTypeServiceImplHikEzviz");

    private final RestTemplate restTemplate;
    private final RedisTemplate redisTemplate;
    private final TblCameraDownloadTaskMapper cameraDownloadTaskMapper;
    private final CameraServiceI cameraService;

    /**
     * appTokenUrl
     */
    private static final String APP_TOKEN_URL = "https://open.ys7.com/api/lapp/token/get";

    /**
     * 获取文件下载地址接口
     */
    public static final String OPENYS_GET_DOWNLOAD_URL = "https://open.ys7.com/api/open/cloud/v1/file/download";

    /**
     * 删除文件接口
     */
    public static final String OPENYS_DELETE_FILE_URL = "https://open.ys7.com/api/open/cloud/v1/file";

    /**
     * 云存储视频转码录制接口
     */
    // public static final String OPENYS_VIDEOSAVE_URL = "https://open.ys7.com/api/open/cloud/v1/video/save";
    public static final String OPENYS_VIDEOSAVE_URL = "https://open.ys7.com/api/open/cloud/v1/rec/video/save";

    /**
     * 图片抓拍接口, 实时抓拍， 不可用于指定时间
     */
    public static final String OPENYS_PICSAVE_URL = "https://open.ys7.com/api/open/cloud/v1/capture/save";
    /**
     * 历史视频中抽帧，按时间点抽帧接口-抽帧时间点与当前时间间隔较短（约一分钟），数据可能会没落盘，会抓取不到图像
     */
    public static final String OPENYS_HISTORY_FRAME_URL
            = "https://open.ys7.com/api/v3/open/cloud/video/frame/timing/start";
    /**
     * 历史视频中抽帧，按间隔时间抽帧， 目前时间点抽帧有问题
     */
    public static final String OPENYS_HISTORY_MULTI_FRAME_URL
            = "https://open.ys7.com/api/v3/open/cloud/video/frame/interval/start";

    /**
     * 根据任务ID查询文件列表
     */
    public static final String OPENYS_TASK_FILE_CHECK_URL = "https://open.ys7.com/api/v3/open/cloud/task/files";
    /**
     * 根据任务ID查询任务状态信息
     */
    public static final String OPENYS_TASK_CHECK_URL = "https://open.ys7.com/api/v3/open/cloud/task/";
    /**
     * 获取单个设备信息，在线状态
     */
    public static final String OPENYS_DEVICE_INFO_URL = "https://open.ys7.com/api/lapp/device/info";

    /**
     * appKey
     */
    private static final String APP_KEY = "8374cfb69acd473d8b4a65c8837c364a";
    /**
     * appSecret
     */
    private static final String APP_SECRET = "25f086df116c78899863c7fc0b8e24ae";

    private static final Map<Integer, String> PROJECT_MAP = new HashMap<>() {{
        put(1, "shmashine_faultFile");
        put(2, "shmashineNightWatch");
        put(3, "shmashineGroupLeasing");
        put(4, "emergencyRepairFirstAid");
        put(5, "electricBikeIdentify");
        put(6, "people_flow_statistics");
    }};


    /**
     * 创建云存储项目接口
     */
    //public static final String OPENYS_CREATPROJECT_URL = "https://open.ys7.com/api/open/cloud/v1/project/";
    @Override
    public void downloadVideoOrImage(TblCameraDownloadTaskEntity entity) {
        // 如果有失败次数的，则删除远程记录
        if (entity.getRequestFailedCount() > 0 || entity.getUploadFailedCount() > 0) {
            var deleteFile = deleteFileCloud(entity.getTaskCustomId(), entity.getTaskType());
            log.info("delete old file {}", deleteFile);
        }
        // 发起远程下载任务
        downloadDebugLog.info("start to remote download , taskId {}", entity.getTaskCustomId());
        downloadFileByType(entity);
    }

    //CHECKSTYLE:OFF
    @Override
    public void doDownloadingCameraFile(TblCameraDownloadTaskEntity task) {
        var res = StringUtils.hasText(task.getCloudTaskId())
                ? checkTaskStatusAndGetFile(task)
                : checkAndGetFileDownloadUrl(task.getTaskCustomId(), task.getTaskType());
        if (HttpStatus.OK.value() == res.getCode()) {
            var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                    .fileStatus(CameraDownloadFileStatusEnum.WAIT_UPLOAD_OSS.getStatus())
                    .sourceUrl(res.getData().toString())
                    .returnCode(res.getCode())
                    .errMessage(res.getMessage())
                    .build();
            if (task.getCollectTime() == null) {
                tmpTaskEntity.setCollectTime(task.getCreateTime() != null ? task.getCollectTime() : DateUtil.now());
            }
            cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
        } else if (HttpStatus.NOT_FOUND.value() == res.getCode()) {
            //等待重新下载
            var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                    .fileStatus(CameraDownloadFileStatusEnum.WAITING.getStatus())
                    .returnCode(res.getCode())
                    .errMessage(res.getMessage())
                    .build();
            if (task.getCollectTime() == null) {
                tmpTaskEntity.setCollectTime(task.getCreateTime() != null ? task.getCollectTime() : DateUtil.now());
            }
            cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
            cameraDownloadTaskMapper.increaseRequestFailedCount(task.getId());

            log.info("暂时没有获取到相关记录， 稍后重试");
        } else if (HttpStatus.CREATED.value() == res.getCode()) {

            //等待完成
            var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                    .returnCode(res.getCode())
                    .errMessage(res.getMessage())
                    .build();
            cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);

            log.info("任务下载中-等待完成");

        } else { //// 非 404时 则认为存在异常
            var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                    .fileStatus(CameraDownloadFileStatusEnum.REQUEST_FAILED.getStatus())
                    .returnCode(res.getCode())
                    .errMessage(res.getMessage())
                    .build();

            if (task.getCollectTime() == null) {
                tmpTaskEntity.setCollectTime(task.getCreateTime() != null ? task.getCollectTime() : DateUtil.now());
            }
            cameraDownloadTaskMapper.updateById(task.getId(), tmpTaskEntity);
            cameraDownloadTaskMapper.increaseRequestFailedCount(task.getId());
        }
    }   //CHECKSTYLE:ON

    @Override
    public ResponseCustom renewCameraOnlineStatus(String cloudNumber) {
        return getDeviceInfo(cloudNumber);
    }

    /**
     * 缓存中获取token
     *
     * @return token
     */
    public ResponseCustom getAccessToken() {
        var res = redisTemplate.opsForValue().get(buildRedisTokenKey());
        if (res == null) {
            return getAccessTokenFromSource();
        }
        return ResponseCustom.builder()
                .code(HttpStatus.OK.value())
                .message(null)
                .data(res)
                .build();
    }

    /**
     * 获取Token
     *
     * @return 对象
     */
    private ResponseCustom getAccessTokenFromSource() {
        try {
            // 拼接请求参数
            var builder = UriComponentsBuilder.fromHttpUrl(APP_TOKEN_URL)
                    .queryParam("appKey", APP_KEY)
                    .queryParam("appSecret", APP_SECRET);

            var queryMap = new HashMap<String, Object>();

            var res = restTemplate.postForObject(
                    builder.build().encode().toUri(),
                    queryMap,
                    ResponseTokenEntityHaikang.class);

            if (res == null) {
                return ResponseCustom.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Token is null")
                        .data("")
                        .build();
            }
            if (HttpStatus.OK.value() != Integer.parseInt(res.getCode())) {
                log.error("get Haikang accessToken error {}", res);
                return ResponseCustom.builder()
                        .code(Integer.parseInt(res.getCode()))
                        .message(res.getMsg())
                        .data("")
                        .build();
            }

            var token = res.getData().getAccessToken();
            var now = DateTime.now().setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")).getTime();
            var time = res.getData().getExpireTime() - now - 3600 * 1000;
            redisTemplate.opsForValue().set(buildRedisTokenKey(), token, time, TimeUnit.MILLISECONDS);
            return ResponseCustom.builder()
                    .code(HttpStatus.OK.value())
                    .message(null)
                    .data(token)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseCustom.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .data("")
                    .build();
        }
    }

    private String buildRedisTokenKey() {
        return "TOKEN:HAIKANG:" + APP_KEY;
    }

    //CHECKSTYLE:OFF
    private ResponseCustom downloadFileByType(TblCameraDownloadTaskEntity entity) {

        //摄像头离线不进行取证
        Integer cameraOnlineStatus = cameraService.getCameraOnlineStatusByElevatorCode(entity.getElevatorCode());
        if (cameraOnlineStatus == 0) {
            return ResponseCustom.builder().message("摄像头离线,cloud_number = " + entity.getCloudNumber()).build();
        }

        downloadDebugLog.info("getCameraOnlineStatusByElevatorCode, cameraOnlineStatus:{}, taskId:{}",
                cameraOnlineStatus, entity.getTaskCustomId());

        ResponseCustom responseCustom = null;

        //下载文件
        if (CameraMediaTypeEnum.MP4.getMediaType().equals(entity.getMediaType())) {
            responseCustom = downloadVideoFile(entity);
        } else if (CameraMediaTypeEnum.JPG.getMediaType().equals(entity.getMediaType())) {
            if (!StringUtils.hasText(entity.getCollectTime()) && !StringUtils.hasText(entity.getStartTime())) {
                Date createTime = entity.getCreateTime();
                if (createTime != null) {
                    if (DateUtil.offsetSecond(createTime, 5).isBefore(new Date())) {

                        //等待任务下载
                        String collectTime = DateUtil.formatDateTime(entity.getCreateTime());
                        cameraDownloadTaskMapper.updateById(entity.getId(),
                                TblCameraDownloadTaskEntity.builder().collectTime(collectTime).build());

                    } else {
                        responseCustom = downloadRealTimeImageFile(entity);
                    }
                } else {
                    responseCustom = downloadRealTimeImageFile(entity);
                }

            } else {

                DateTime collectTime = DateUtil.parse(entity.getCollectTime());

                //采集时间小于当前时间3s-使用实时抓图
                if (DateUtil.offsetSecond(collectTime, 3).isAfter(new Date())) {
                    responseCustom = downloadRealTimeImageFile(entity);
                }

                //超过一分钟再下载
                if (DateUtil.offsetSecond(collectTime, 60).isBefore(new Date())) {
                    responseCustom = downloadHistoryImageFile(entity);
                }

            }
        }

        // 下载失败 构建响应结果
        if (responseCustom != null) {
            if (HttpStatus.OK.value() != responseCustom.getCode()) {
                var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                        .fileStatus(CameraDownloadFileStatusEnum.REQUEST_FAILED.getStatus())
                        .returnCode(responseCustom.getCode())
                        .errMessage(responseCustom.getMessage())
                        .build();
                //实时下载失败取证时间没有补充为当前时间
                if (!StringUtils.hasText(entity.getCollectTime()) && !StringUtils.hasText(entity.getStartTime())) {
                    tmpTaskEntity.setCollectTime(DateUtil.now());
                }
                //更新状态
                cameraDownloadTaskMapper.updateById(entity.getId(), tmpTaskEntity);
                //增加请求失败计数
                cameraDownloadTaskMapper.increaseRequestFailedCount(entity.getId());
                log.error("文件下载请求失败，requestBody：{}，responseCustom：{}", entity, responseCustom);
            }
            return responseCustom;
        }

        var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                .fileStatus(CameraDownloadFileStatusEnum.REQUEST_FAILED.getStatus())
                .returnCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errMessage("目前只支持 jpg, mp4两种文件格式")
                .build();
        //更新状态
        cameraDownloadTaskMapper.updateById(entity.getId(), tmpTaskEntity);
        //增加请求失败计数
        cameraDownloadTaskMapper.increaseRequestFailedCount(entity.getId());
        return ResponseCustom.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("目前只支持 jpg, mp4两种文件格式")
                .build();
    }   //CHECKSTYLE:ON


    /**
     * 获取文件下载地址
     *
     * @param fileId 唯一ID
     * @return 对象
     */
    private ResponseCustom checkAndGetFileDownloadUrl(String fileId, Integer taskType) {
        var tokenEntity = getAccessToken();
        if (HttpStatus.OK.value() != tokenEntity.getCode()) {
            return tokenEntity;
        }
        var messagePre = "获取下载地址：";
        try {
            var builder = UriComponentsBuilder.fromHttpUrl(OPENYS_GET_DOWNLOAD_URL)
                    .queryParam("accessToken", tokenEntity.getData().toString())
                    .queryParam("fileId", fileId)
                    .queryParam("projectId", PROJECT_MAP.get(taskType));
            //var res = restTemplate.getForObject(builder.build().encode().toUri(), ResponseFileEntityHaikang.class);
            var res = restDealResult(builder.build().encode().toUriString(), null, ResponseFileEntityHaikang.class,
                    HttpMethod.GET);

            if (res == null) {
                return ResponseCustom.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(messagePre + "no response")
                        .data(null)
                        .build();
            }
            // 无相关资源
            if (HttpStatus.NOT_FOUND.value() == res.getMeta().getCode()) {
                return ResponseCustom.builder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(messagePre + "no file exists")
                        .data(null)
                        .build();
            }

            if (HttpStatus.OK.value() != res.getMeta().getCode()) {
                log.error("get Haikang File url error {}", res);
                return ResponseCustom.builder()
                        .code(res.getMeta().getCode())
                        .message(messagePre + res.getMeta().getMessage())
                        .data(null)
                        .build();
            }
            var now = DateTime.now().setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")).getTime();
            var urls = String.join(",", res.getData().getUrls());
            if (now >= res.getData().getExpire()) {
                //throw new RuntimeException("文件地址已过期 " + urls);
                return ResponseCustom.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(messagePre + "文件地址已过期 " + urls)
                        .data(urls)
                        .build();
            }
            return ResponseCustom.builder()
                    .code(HttpStatus.OK.value())
                    .message(messagePre + "成功")
                    .data(urls)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseCustom.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(messagePre + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * 删除云文件，以便重新下载
     *
     * @param fileId 自定义ID
     * @return 对象
     */
    private ResponseCustom deleteFileCloud(String fileId, Integer taskType) {
        var tokenEntity = getAccessToken();
        if (HttpStatus.OK.value() != tokenEntity.getCode()) {
            return tokenEntity;
        }
        var messagePre = "删除萤石文件记录：";
        try {
            var delUrl = UriComponentsBuilder
                    .fromHttpUrl(OPENYS_DELETE_FILE_URL)
                    .queryParam("accessToken", tokenEntity.getData().toString())
                    .queryParam("fileId", fileId)
                    .queryParam("projectId", PROJECT_MAP.get(taskType))
                    .build()
                    .encode()
                    .toString();
            restTemplate.delete(delUrl);

            return ResponseCustom.builder()
                    .code(HttpStatus.OK.value())
                    .message(messagePre + "成功")
                    .data(null)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseCustom.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(messagePre + e.getMessage())
                    .data(null)
                    .build();
        }


    }


    /**
     * 实时的图片下载， 不可用于获取历史时间点的图片
     *
     * @param entity 条件
     * @return 结果
     */
    private ResponseCustom downloadRealTimeImageFile(TblCameraDownloadTaskEntity entity) {

        downloadDebugLog.info("downloadRealTimeImageFile-start,  taskId:{}", entity.getTaskCustomId());
        var tokenEntity = getAccessToken();
        if (HttpStatus.OK.value() != tokenEntity.getCode()) {
            return tokenEntity;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var mulmap = new LinkedMultiValueMap<String, Object>();
        mulmap.add("accessToken", tokenEntity.getData().toString());
        mulmap.add("channelNo", 1);
        mulmap.add("deviceSerial", entity.getCloudNumber());
        mulmap.add("projectId", PROJECT_MAP.get(entity.getTaskType()));
        mulmap.add("fileId", entity.getTaskCustomId());
        var messagePre = "获取实时照片：";
        try {
            var request = new HttpEntity<>(mulmap, headers);
            //var res = restTemplate.postForObject(OPENYS_PICSAVE_URL, request, ResponseImageEntityHaikang.class);
            var res = restDealResult(OPENYS_PICSAVE_URL, request, ResponseImageEntityHaikang.class, HttpMethod.POST);

            downloadDebugLog.info("downloadRealTimeImageFile-end,  taskId:{}, res:{}", entity.getTaskCustomId(), res);

            if (res == null) {
                return ResponseCustom.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(messagePre + "no response")
                        .data(null)
                        .build();
            }
            if (HttpStatus.OK.value() == res.getMeta().getCode()) {
                var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                        .fileStatus(CameraDownloadFileStatusEnum.WAIT_UPLOAD_OSS.getStatus())
                        .sourceUrl(res.getData())
                        .returnCode(res.getMeta().getCode())
                        .errMessage(messagePre + res.getMeta().getMessage())
                        .build();
                tmpTaskEntity.setCloudTaskId(String.valueOf(entity.getId()));
                cameraDownloadTaskMapper.updateById(entity.getId(), tmpTaskEntity);
                downloadDebugLog.info("云录制-成功-待上传OSS，taskID【{}】", entity.getId());
                executorService.submit(() ->
                        cameraService.startOssUploadCameraFileByTaskId(String.valueOf(entity.getId())));

                return ResponseCustom.builder()
                        .code(HttpStatus.OK.value())
                        .message(messagePre + "成功")
                        .data(res.getData())
                        .build();
            }
            return ResponseCustom.builder()
                    .code(res.getMeta().getCode())
                    .message(messagePre + res.getMeta().getMessage())
                    .data(null)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseCustom.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(messagePre + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * 获取历史某个时刻的照片， 需要从视频里获取一帧
     *
     * @param entity 条件
     * @return 结果
     */
    private ResponseCustom downloadHistoryImageFile(TblCameraDownloadTaskEntity entity) {

        downloadDebugLog.info("downloadHistoryImageFile-start,  taskId:{}", entity.getTaskCustomId());

        var tokenEntity = getAccessToken();
        if (HttpStatus.OK.value() != tokenEntity.getCode()) {
            return tokenEntity;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var timePoint = StringUtils.hasText(entity.getCollectTime())
                ? entity.getCollectTime().replace("-", "").replace(" ", "").replace(":", "")
                : (StringUtils.hasText(entity.getStartTime()) ? entity.getStartTime() : null);

        var mulmap = new LinkedMultiValueMap<String, Object>();
        mulmap.add("accessToken", tokenEntity.getData().toString());
        mulmap.add("projectId", PROJECT_MAP.get(entity.getTaskType()));
        mulmap.add("deviceSerial", entity.getCloudNumber());
        mulmap.add("channelNo", 1);
        mulmap.add("recType", "local");
        mulmap.add("frameModel", 0);
        mulmap.add("timingPoints", timePoint);
        if (CameraTaskTypeEnum.ELECTRIC_BIKE_IDENTIFY.getCode().equals(entity.getTaskType())) {
            // 标清
            mulmap.add("streamType", 2);
        }
        var messagePre = "生成获取某个时刻的照片任务：";
        try {
            var request = new HttpEntity<>(mulmap, headers);
            var res = restDealResult(OPENYS_HISTORY_FRAME_URL,
                    request, ResponseTaskEntityHaikang.class, HttpMethod.POST);

            downloadDebugLog.info("downloadHistoryImageFile-end, taskId:{}, res:{}", entity.getTaskCustomId(), res);

            if (res == null) {
                return ResponseCustom.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(messagePre + "no response")
                        .data(null)
                        .build();
            }
            if (HttpStatus.OK.value() == res.getMeta().getCode()) {
                var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                        .cloudTaskId(res.getData().getTaskId())
                        .fileStatus(CameraDownloadFileStatusEnum.DOWNLOADING.getStatus())
                        .returnCode(res.getMeta().getCode())
                        .errMessage(messagePre + res.getMeta().getMessage())
                        .build();
                cameraDownloadTaskMapper.updateById(entity.getId(), tmpTaskEntity);
                return ResponseCustom.builder()
                        .code(HttpStatus.OK.value())
                        .message(messagePre + "成功")
                        .data(res.getData())
                        .build();
            }
            return ResponseCustom.builder()
                    .code(res.getMeta().getCode())
                    .message(messagePre + res.getMeta().getMessage())
                    .data(null)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseCustom.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(messagePre + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    /**
     * 获取历史某段时间 间隔抽帧
     *
     * @param entity 条件
     * @return 结果
     */
    private ResponseCustom downloadHistoryMultiImageFile(TblCameraDownloadTaskEntity entity) {
        var tokenEntity = getAccessToken();
        if (HttpStatus.OK.value() != tokenEntity.getCode()) {
            return tokenEntity;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var timePointStart = StringUtils.hasText(entity.getStartTime()) ? entity.getStartTime()
                : (StringUtils.hasText(entity.getCollectTime())
                ? entity.getCollectTime().replace(" ", "")
                .replace("-", "").replace(":", "")
                : null);
        var timePointEnd = StringUtils.hasText(entity.getEndTime())
                ? entity.getEndTime()
                : new SimpleDateFormat(DatePattern.PURE_DATETIME_PATTERN).format(
                DateTime.of(DateTime
                                .of(timePointStart, DatePattern.PURE_DATETIME_PATTERN).getTime() + 10 * 1000)
                        .setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")));

        var mulmap = new LinkedMultiValueMap<String, Object>();
        mulmap.add("accessToken", tokenEntity.getData().toString());
        mulmap.add("projectId", PROJECT_MAP.get(entity.getTaskType()));
        mulmap.add("deviceSerial", entity.getCloudNumber());
        mulmap.add("channelNo", 1);
        mulmap.add("recType", "local");
        // 抽帧模式，0：普通模式 1：错峰抽帧模式* 2：抽 I 帧模式*；不填默认:0
        mulmap.add("frameModel", 0);
        // 抽帧间隔 普通模式和抽I帧模式模式，单位：秒；如果选择抽 I 帧模式，此处传 GOP 间隔倍数，单位：倍数
        mulmap.add("frameInterval", 60);
        mulmap.add("startTime", timePointStart);
        mulmap.add("endTime", timePointEnd);
        var messagePre = "生成获取某个时刻的照片任务：";
        try {
            var request = new HttpEntity<>(mulmap, headers);
            var res = restDealResult(OPENYS_HISTORY_MULTI_FRAME_URL,
                    request, ResponseTaskEntityHaikang.class, HttpMethod.POST);

            if (res == null) {
                return ResponseCustom.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(messagePre + "no response")
                        .data(null)
                        .build();
            }
            if (HttpStatus.OK.value() == res.getMeta().getCode()) {
                var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                        .cloudTaskId(res.getData().getTaskId())
                        .fileStatus(CameraDownloadFileStatusEnum.DOWNLOADING.getStatus())
                        .returnCode(res.getMeta().getCode())
                        .errMessage(messagePre + res.getMeta().getMessage())
                        .build();
                cameraDownloadTaskMapper.updateById(entity.getId(), tmpTaskEntity);
                return ResponseCustom.builder()
                        .code(HttpStatus.OK.value())
                        .message(messagePre + "成功")
                        .data(res.getData())
                        .build();
            }
            return ResponseCustom.builder()
                    .code(res.getMeta().getCode())
                    .message(messagePre + res.getMeta().getMessage())
                    .data(null)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseCustom.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(messagePre + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    private ResponseCustom downloadVideoFile(TblCameraDownloadTaskEntity entity) {

        downloadDebugLog.info("downloadVideoFile-start,  taskId:{}", entity.getTaskCustomId());

        var tokenEntity = getAccessToken();
        if (HttpStatus.OK.value() != tokenEntity.getCode()) {
            return tokenEntity;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("accessToken", tokenEntity.getData().toString());
        headers.set("deviceSerial", entity.getCloudNumber());

        var mulmap = new LinkedMultiValueMap<String, Object>();
        mulmap.set("projectId", PROJECT_MAP.get(entity.getTaskType()));
        mulmap.set("startTime", entity.getStartTime());
        mulmap.set("endTime", entity.getEndTime());
        mulmap.set("voiceSwitch", "1");
        mulmap.set("recType", "local");
        mulmap.set("format", entity.getMediaType());

        var messagePre = "生成获取视频文件任务：";
        try {
            var request = new HttpEntity<>(mulmap, headers);
            //var res = restTemplate.postForObject(OPENYS_VIDEOSAVE_URL, request, ResponseTaskEntityHaikang.class);
            var res = restDealResult(OPENYS_VIDEOSAVE_URL, request, ResponseTaskEntityHaikang.class, HttpMethod.POST);

            downloadDebugLog.info("downloadVideoFile-end,  taskId:{}, res:{}", entity.getTaskCustomId(), res);

            if (res == null) {
                return ResponseCustom.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(messagePre + "no response")
                        .data(null)
                        .build();
            }
            if (HttpStatus.OK.value() == res.getMeta().getCode()) {
                var tmpTaskEntity = TblCameraDownloadTaskEntity.builder()
                        .cloudTaskId(res.getData().getTaskId())
                        .fileStatus(CameraDownloadFileStatusEnum.DOWNLOADING.getStatus())
                        .returnCode(res.getMeta().getCode())
                        .errMessage(messagePre + res.getMeta().getMessage())
                        .build();
                cameraDownloadTaskMapper.updateById(entity.getId(), tmpTaskEntity);
                return ResponseCustom.builder()
                        .code(HttpStatus.OK.value())
                        .message(messagePre + "成功")
                        .data(res.getData().getTaskId())
                        .build();
            }
            return ResponseCustom.builder()
                    .code(res.getMeta().getCode())
                    .message(messagePre + res.getMeta().getMessage())
                    .data(null)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseCustom.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(messagePre + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    //CHECKSTYLE:OFF
    private ResponseCustom checkTaskStatusAndGetFile(TblCameraDownloadTaskEntity task) {
        var tokenEntity = getAccessToken();
        if (HttpStatus.OK.value() != tokenEntity.getCode()) {
            return tokenEntity;
        }

        //1、获取任务状态
        var taskCheckMessagePre = "获取任务状态：";
        var taskCheckReqVO = UriComponentsBuilder.fromHttpUrl(OPENYS_TASK_CHECK_URL + task.getCloudTaskId())
                .queryParam("accessToken", tokenEntity.getData().toString());
        ResponseEntityHaikang<JSONObject> taskCheckRes = restDealResult(taskCheckReqVO.build().encode().toUriString(),
                null, ResponseEntityHaikang.class, HttpMethod.GET);

        if (taskCheckRes == null) {
            return ResponseCustom.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(taskCheckMessagePre + "no response")
                    .data(null)
                    .build();
        }

        // 无相关资源
        if (HttpStatus.NOT_FOUND.value() == taskCheckRes.getMeta().getCode()) {
            return ResponseCustom.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(taskCheckMessagePre + "no file exists")
                    .data(null)
                    .build();
        }

        if (HttpStatus.OK.value() != taskCheckRes.getMeta().getCode()) {
            log.error("get Haikang File url error {}", taskCheckRes);
            return ResponseCustom.builder()
                    .code(taskCheckRes.getMeta().getCode())
                    .message(taskCheckMessagePre + taskCheckRes.getMeta().getMessage())
                    .data(null)
                    .build();
        }

        TaskStatusRespVO taskStatusRespVO = JSON.parseObject(taskCheckRes.getData().toString(), TaskStatusRespVO.class);

        Integer taskStatus = taskStatusRespVO.getTaskStatus();

        //异常|暂停
        if (taskStatus == 4 || taskStatus == 5) {
            return ResponseCustom.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(taskCheckMessagePre + "任务异常或暂停")
                    .data(null)
                    .build();
        }

        //已完成
        if (taskStatus == 0) {

            //2、获取文件
            var messagePre = "根据任务查询文件：";
            try {
                var builder = UriComponentsBuilder.fromHttpUrl(OPENYS_TASK_FILE_CHECK_URL)
                        .queryParam("accessToken", tokenEntity.getData().toString())
                        .queryParam("taskId", task.getCloudTaskId())
                        .queryParam("hasUrl", true);
                var res = restDealResult(builder.build().encode().toUriString(), null,
                        ResponseTaskFileEntityHaikang.class, HttpMethod.GET);

                if (res == null) {
                    return ResponseCustom.builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message(messagePre + "no response")
                            .data(null)
                            .build();
                }
                // 无相关资源
                if (HttpStatus.NOT_FOUND.value() == res.getMeta().getCode()) {
                    return ResponseCustom.builder()
                            .code(HttpStatus.NOT_FOUND.value())
                            .message(messagePre + "no file exists")
                            .data(null)
                            .build();
                }

                if (HttpStatus.OK.value() != res.getMeta().getCode()) {
                    log.error("get Haikang File url error {}", res);
                    return ResponseCustom.builder()
                            .code(res.getMeta().getCode())
                            .message(messagePre + res.getMeta().getMessage())
                            .data(null)
                            .build();
                }
                if (CollectionUtils.isEmpty(res.getData())) {
                    return ResponseCustom.builder()
                            .code(HttpStatus.NOT_FOUND.value())
                            .message(messagePre + "萤石云任务：未能获取到任何数据")
                            .data(null)
                            .build();
                }
                var dataErrorCode = Integer.parseInt(res.getData().get(0).getErrorCode());
                if (0 != dataErrorCode) {
                    // 取流信息失败
                    return ResponseCustom.builder()
                            .code(dataErrorCode)
                            .message(messagePre + "萤石云任务失败：" + HkYsFlowErrorEnum.getDescriptionByCode(dataErrorCode))
                            .data(null)
                            .build();
                }
                return ResponseCustom.builder()
                        .code(HttpStatus.OK.value())
                        .message(messagePre + "成功")
                        .data(StringUtils.collectionToCommaDelimitedString(res.getData().get(0).getDownloadUrls()))
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseCustom.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(messagePre + e.getMessage())
                        .data(null)
                        .build();
            }

        }

        return ResponseCustom.builder()
                .code(HttpStatus.CREATED.value())
                .message(taskCheckMessagePre + "任务进行中，taskStatus：" + taskStatus)
                .data(taskStatus)
                .build();

    }   //CHECKSTYLE:ON


    private ResponseCustom getDeviceInfo(String deviceSerial) {
        var tokenEntity = getAccessToken();
        if (HttpStatus.OK.value() != tokenEntity.getCode()) {
            return tokenEntity;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("accessToken", tokenEntity.getData().toString());
        headers.set("deviceSerial", deviceSerial);

        var mulmap = new LinkedMultiValueMap<String, Object>();
        mulmap.set("accessToken", tokenEntity.getData().toString());
        mulmap.set("deviceSerial", deviceSerial);

        var messagePre = "获取萤石摄像头信息，含在线状态：";
        try {
            var request = new HttpEntity<>(mulmap, headers);

            var response = restTemplate.postForObject(OPENYS_DEVICE_INFO_URL, request, String.class);
            log.info("rest result for uri {} is {}", OPENYS_DEVICE_INFO_URL, response);

            if (!StringUtils.hasText(response)) {
                return ResponseCustom.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(messagePre + "no response")
                        .data(null)
                        .build();
            }
            var res = JSON.parseObject(response, ResponseDeviceEntityHaikang.class);
            if (String.valueOf(HttpStatus.OK.value()).equals(res.getCode())) {
                return ResponseCustom.builder()
                        .code(HttpStatus.OK.value())
                        .message(messagePre + "成功")
                        .data(res.getData())
                        .build();
            }
            return ResponseCustom.builder()
                    .code(Integer.parseInt(res.getCode()))
                    .message(messagePre + res.getMsg())
                    .data(null)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseCustom.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(messagePre + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    private <T extends ResponseEntityHaikang> T restDealResult(String uri, HttpEntity request, Class<T> clazz,
                                                               HttpMethod method) {
        String response = "";
        if (method.matches("GET")) {
            response = restTemplate.getForObject(uri, String.class);
        } else {
            response = restTemplate.postForObject(uri, request, String.class);
        }
        log.info("rest result for uri {} is {}", uri, response);
        if (StringUtils.hasText(response)) {
            return JSON.parseObject(response, clazz);
        }
        return null;
    }
}
