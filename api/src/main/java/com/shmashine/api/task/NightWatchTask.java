// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.task;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;

import com.alibaba.fastjson2.JSON;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.api.service.camera.CameraServerClientBeanService;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.dto.NightWatchLastStatusDTO;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.common.enums.CameraTaskTypeEnum;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.RedisKeyUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/8 11:35
 * @since v1.0
 */

@Slf4j
@Profile({"prod"})
@Component
// @EnableScheduling
@EnableAsync
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class NightWatchTask {
    private final RedisUtils redisUtils;
    private final CameraServerClientBeanService cameraServerClientBeanService;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(4, 8, 2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "NightWatchTask");

    /**
     * 浮点数与0比较使用的数字
     */
    private static final Double DOUBLE_ZERO = 0.1;

    /**
     * 默认提取10秒视频
     */
    private static final Long RECORD_TIME_SECOND = 10L;

    // 每30秒执行一次
    @Scheduled(fixedDelay = 15000, initialDelay = 15000)
    public void scheduledNightWatchTask() {
        var list = redisUtils.lGetAndRemove(RedisKeyUtils.getElevatorNightWatchQueue(), 0);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(item -> {
            executorService.submit(() -> {
                doNightWatchTask(item.toString());
            });
        });
    }

    /**
     * 当前无人  上一次无人或无记录   记录无人 采集时间
     * 当前有人  上一次无人或无记录   记录有人 采集时间前推10秒
     * 当前有人  上一次有人          记录有人， 时间不变
     * 当前无人  上一次有人          结束记录，开始录像， 重新 记录无人 采集时间
     *
     * @param taskString 详情
     */
    public void doNightWatchTask(String taskString) {
        var jsonObject = JSON.parseObject(taskString, Map.class);
        // 字段判断
        var timeObj = jsonObject.get("time");
        var elevatorCodeObj = jsonObject.get("elevatorCode");
        var hasPeopleObj = jsonObject.get("hasPeople");
        if (timeObj == null || elevatorCodeObj == null || hasPeopleObj == null) {
            return;
        }
        var elevatorCode = elevatorCodeObj.toString();
        var time = timeObj.toString();
        var hasPeople = Integer.parseInt(hasPeopleObj.toString());

        var lastStatus = getLastStatus(elevatorCode);
        // 上一次无人或无记录
        if (lastStatus == null || 0 == lastStatus.getHasPeople()) {
            var redisTime = time;
            // 有人 采集时间前推10秒
            if (0 != hasPeople) {
                redisTime = getDateTimeAfter(time, -1 * RECORD_TIME_SECOND);
            }
            // 记录有人/无人 采集时间
            setLastStatus(elevatorCode, redisTime, hasPeople);
            return;
        }
        // 上一次有人 情况
        // 本次有人
        if (0 != hasPeople) {
            updateLastStatusExpire(elevatorCode);
            return;
        }
        // 本次无人，上次有人，发起录像，重新记录无人采集时间
        var startTime = lastStatus.getTime();
        var endTime = time;

        // 从 timeStart 时间开始获取 RECORD_TIME_SECOND 秒视频并记录
        // 发起录像，截取录像的电梯
        var entity = CamareMediaDownloadRequestDTO.builder()
                .elevatorCode(elevatorCode)
                .collectTime(time)
                .startTime(startTime)
                .endTime(endTime)
                .floor(jsonObject.get("floor") == null ? null : jsonObject.get("floor").toString())
                .taskType(CameraTaskTypeEnum.NIGHT_WATCH)
                .mediaType(CameraMediaTypeEnum.MP4)
                .build();
        var res = cameraServerClientBeanService.downloadCameraFileByElevatorCode(entity);
        //redisUtils.lSet(RedisKeyUtils.getElevatorVideoRecordQueue(), JSON.toJSONString(entity));
        log.info("NightWatch-downloadCameraFile result status:{}, data:{}", res.getStatusCode(),
                res.getBody());
        // 重新存储状态
        setLastStatus(elevatorCode, time, hasPeople);
    }

    private String getDateTimeAfter(String time, Long seconds) {
        return new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN).format(
                DateTime.of(
                        DateTime.of(time, DatePattern.NORM_DATETIME_PATTERN).getTime() + seconds * 1000L));
    }

    private NightWatchLastStatusDTO getLastStatus(String elevatorCode) {
        var redisKey = RedisKeyUtils.getElevatorNightWatchLatestPeopleStatus(elevatorCode);
        var resString = redisUtils.get(redisKey);
        if (!StringUtils.hasText(resString)) {
            return null;
        }
        return JSON.parseObject(resString, NightWatchLastStatusDTO.class);
    }

    private void setLastStatus(String elevatorCode, String time, Integer hasPeople) {
        var redisKey = RedisKeyUtils.getElevatorNightWatchLatestPeopleStatus(elevatorCode);
        redisUtils.set(redisKey, JSON.toJSONString(NightWatchLastStatusDTO.builder()
                .time(time)
                .hasPeople(hasPeople)
                .build()), hasPeople > 0 ? 600 : 60);
    }

    private void updateLastStatusExpire(String elevatorCode) {
        var redisKey = RedisKeyUtils.getElevatorNightWatchLatestPeopleStatus(elevatorCode);
        redisUtils.expire(redisKey, 600);
    }

}
