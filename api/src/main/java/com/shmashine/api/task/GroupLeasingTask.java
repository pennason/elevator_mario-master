// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.task;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.shmashine.api.dao.TblElevatorDao;
import com.shmashine.api.dao.TblGroupLeasingElevatorCoefficientMapper;
import com.shmashine.api.dao.TblGroupLeasingFloorCoefficientMapper;
import com.shmashine.api.dao.TblGroupLeasingFloorEvidenceMapper;
import com.shmashine.api.dao.TblGroupLeasingStatisticsMapper;
import com.shmashine.api.dao.TblGroupLeasingVillageCoefficientMapper;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.api.service.camera.CameraServerClientBeanService;
import com.shmashine.api.service.elevator.ElevatorCacheService;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.dto.ElevatorCacheDTO;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblGroupLeasingElevatorCoefficientEntity;
import com.shmashine.common.entity.TblGroupLeasingFloorCoefficientEntity;
import com.shmashine.common.entity.TblGroupLeasingStatisticsEntity;
import com.shmashine.common.entity.TblGroupLeasingVillageCoefficientEntity;
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
 * @version v1.0  -  2023/2/13 16:43
 * @since v1.0
 */

@Slf4j
@Profile({"prod"})
@Component
// @EnableScheduling
@EnableAsync
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GroupLeasingTask {
    private final RedisUtils redisUtils;
    private final ElevatorCacheService elevatorCacheService;
    private final TblElevatorDao elevatorDao;
    private final TblGroupLeasingFloorCoefficientMapper groupLeasingFloorCoefficientMapper;
    private final TblGroupLeasingElevatorCoefficientMapper groupLeasingElevatorCoefficientMapper;
    private final TblGroupLeasingVillageCoefficientMapper groupLeasingVillageCoefficientMapper;
    private final TblGroupLeasingFloorEvidenceMapper groupLeasingFloorEvidenceMapper;
    private final TblGroupLeasingStatisticsMapper groupLeasingStatisticsMapper;

    private final CameraServerClientBeanService cameraServerClientBeanService;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(2, 4, 2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "GroupLeasingTask");

    private final ExecutorService statisticsExecutorService = new ShmashineThreadPoolExecutor(8, 12, 2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("statisticsExecutorService"), "GroupLeasingTask");

    public static final Integer VALID_MIN_FLOOR = 2;

    /**
     * 默认提取5秒视频
     */
    private static final Long RECORD_TIME_SECOND = 5L;

    /**
     * 群租识别任务处理
     */
    @Scheduled(fixedDelay = 15000, initialDelay = 35000)
    public void scheduledGroupLeasingTask() {
        log.info("GroupLeasingTask-scheduledGroupLeasingTask");
        var list = redisUtils.lGetAndRemove(RedisKeyUtils.getElevatorGroupLeasingQueue(), 0);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        list.forEach(item -> {
            executorService.submit(() -> {
                doGroupLeasingTask(item.toString());
            });
        });
    }

    /**
     * 每日统计 昨日电梯楼层的群租系数, 昨日电梯的群租系数
     */
    @Scheduled(cron = "0 40 2 * * ?")
    public void scheduledGroupLeasingStatisticsTask() {
        log.info("GroupLeasingTask-scheduledGroupLeasingStatisticsTask");
        // 获取所有电梯
        var elevatorSet = redisUtils.sGet(RedisKeyUtils.getElevatorMembers());
        if (CollectionUtils.isEmpty(elevatorSet)) {
            return;
        }
        // 获取电梯 群租统计详情
        elevatorSet.forEach(item -> {
            statisticsExecutorService.submit(() -> {
                doGroupLeasingStatisticsTask(item.toString());
            });
        });
    }

    /**
     * 每日统计 昨日小区的群租系数
     */
    @Scheduled(cron = "0 30 3 * * ?")
    public void scheduledGroupLeasingVillageStatisticsTask() {
        log.info("GroupLeasingTask-scheduledGroupLeasingVillageStatisticsTask");
        var yesterday = getDateYesterday();
        // 1. 计算昨日想去的群租系数
        doGroupLeasingStatisticsVillage(yesterday);
        // 2. 计算楼层群租热力图，阈值等级
        doGroupLeasingStatisticsFloorLevel(yesterday);
        // 3. 计算电梯的热力图，阈值等级
        doGroupLeasingStatisticsElevatorLevel(yesterday);

    }


    public void doGroupLeasingTask(String task) {
        var jsonObject = JSON.parseObject(task, Map.class);
        // 1. 合法性判断
        var elevatorCodeObject = jsonObject.get("elevatorCode");
        var timeObject = jsonObject.get("time");
        var floorObject = jsonObject.get("floor");
        if (elevatorCodeObject == null || timeObject == null || floorObject == null) {
            return;
        }
        var hasPeopleObject = jsonObject.get("hasPeople");
        var elevatorCode = elevatorCodeObject.toString();
        var time = timeObject.toString();
        var floor = floorObject.toString();
        var hasPeople = hasPeopleObject == null ? 0 : Integer.parseInt(hasPeopleObject.toString());


        // 2. 电梯信息
        var elevator = elevatorCacheService.getByElevatorCode(elevatorCode);

        // 3. 判断最近停靠楼层 是否有变化， 无变化则不做任何处理
        var latestFloorKey = RedisKeyUtils.getElevatorGroupLeasingLatestFloor(elevatorCode);
        var latestFloor = redisUtils.get(latestFloorKey);
        // 3.1 楼层没有变化 则忽略
        if (floor.equals(latestFloor)) {
            return;
        }
        // 3.2 更新电梯的最新楼层
        redisUtils.set(latestFloorKey, floor);


        //  1.3 一楼及以下不统计
        if (Integer.parseInt(floor) < VALID_MIN_FLOOR) {
            return;
        }

        // 4. 缓存数据
        // 4.1 时间系数
        var timeCoefficientList = elevator.getGroupLeasingTimeCoefficient().split(",");
        var timeHour = Integer.parseInt(time.substring(11, 13));
        var totalQuantity = Float.valueOf(timeCoefficientList[timeHour]);
        // redis key
        var redisHashMapKey = RedisKeyUtils.getElevatorDateMapKey(elevatorCode, time.substring(0, 10));
        // 4.2. 查询是否存在相关电梯楼层记录
        var floorData = redisUtils.hget(redisHashMapKey, floor);
        if (floorData != null) {
            totalQuantity = Float.parseFloat(floorData.toString()) + totalQuantity;
        }
        // 4.3. 缓存相关楼层数据， 缓存 2天
        redisUtils.hset(redisHashMapKey, floor, totalQuantity, 3600 * 24 * 2);

        // 5. 取证相关逻辑 如果需要拍照取证则判断日期
        evidenceByVillage(jsonObject, elevatorCode, time, hasPeople, elevator);
        evidenceByElevatorFloor(elevatorCode, floor, time);

    }


    private void evidenceByVillage(Map jsonObject, String elevatorCode, String time, int hasPeople, ElevatorCacheDTO elevator) {

        // 5.1. 取证只需要考虑有人的情况
        if (0 == hasPeople) {
            return;
        }
        // 5.2 判断 日期范围
        var collectDate = DateTime.of(time, DatePattern.NORM_DATETIME_PATTERN);
        // 5.2.1. 时间未到
        if (elevator.getGroupLeasingStartDate() == null || collectDate.before(elevator.getGroupLeasingStartDate())) {
            return;
        }
        // 5.2.2. 已结束
        if (elevator.getGroupLeasingEndDate() != null && collectDate.after(elevator.getGroupLeasingEndDate())) {
            return;
        }
        // 5.3. 是否在取证开始和结束时间范围内
        var collectTime = Time.valueOf(time.substring(11));

        var startTime = elevator.getGroupLeasingStartTime() == null
                ? null : Time.valueOf(elevator.getGroupLeasingStartTime());
        var endTime = elevator.getGroupLeasingEndTime() == null
                ? null : Time.valueOf(elevator.getGroupLeasingEndTime());
        if (startTime == null || endTime == null) {
            return;
        }
        // 5.3.1. 开始时间小于结束时间
        if (startTime.before(endTime)) {
            if (collectTime.before(startTime) || collectTime.after(endTime)) {
                return;
            }
        }
        // 5.3.2. 开始时间大于结束时间
        else {
            if (collectTime.before(startTime) && collectTime.after(endTime)) {
                return;
            }
        }
        // 发起获取取证照片
        var entity = CamareMediaDownloadRequestDTO.builder()
                .elevatorCode(elevatorCode)
                .collectTime(time)
                .startTime(time)
                .floor(jsonObject.get("floor") == null ? null : jsonObject.get("floor").toString())
                .taskType(CameraTaskTypeEnum.GROUP_LEASING)
                .mediaType(CameraMediaTypeEnum.JPG)
                .build();
        var res = cameraServerClientBeanService.downloadCameraFileByElevatorCode(entity);
        log.info("GroupLeasing-downloadCameraFile village result status:{}, data:{}", res.getStatusCode(),
                res.getBody());
    }

    /**
     * 取证录像
     *
     * @param elevatorCode 电梯编号
     * @param floor        楼层
     * @param time         时间
     */
    private void evidenceByElevatorFloor(String elevatorCode, String floor, String time) {
        var floorEvidence = groupLeasingFloorEvidenceMapper.getByUnique(elevatorCode, floor);
        if (floorEvidence == null) {
            return;
        }
        if (floorEvidence.getStatus() > 0) {
            return;
        }
        var startTime = floorEvidence.getStartTime().toInstant(ZoneOffset.of("+8"));
        var endTime = floorEvidence.getEndTime().toInstant(ZoneOffset.of("+8"));
        var collectTime = DateTime.of(time, DatePattern.NORM_DATETIME_PATTERN)
                .setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                .toInstant();
        if (collectTime.isBefore(startTime) || collectTime.isAfter(endTime)) {
            return;
        }
        // 发起获取取证照片
        var entity = CamareMediaDownloadRequestDTO.builder()
                .elevatorCode(elevatorCode)
                .collectTime(time)
                .startTime(time)
                .endTime(getDateTimeAfter(time, RECORD_TIME_SECOND))
                .floor(floor)
                .taskType(CameraTaskTypeEnum.GROUP_LEASING)
                .mediaType(CameraMediaTypeEnum.MP4)
                .build();
        var res = cameraServerClientBeanService.downloadCameraFileByElevatorCode(entity);
        log.info("GroupLeasing-downloadCameraFile floor result status:{}, data:{}", res.getStatusCode(),
                res.getBody());


    }

    /**
     * 每日统计昨日相关记录
     *
     * @param elevatorCode 电梯编号
     */
    private void doGroupLeasingStatisticsTask(String elevatorCode) {
        var yesterday = getDateYesterday();
        // 1. 统计楼层的 群租系数
        doGroupLeasingStatisticsFloor(elevatorCode, yesterday);
        // 2. 统计电梯的 群租系数
        doGroupLeasingStatisticsElevator(elevatorCode, yesterday);
    }

    /**
     * 按楼层统计
     *
     * @param elevatorCode 电梯编号
     * @param yesterday    日期
     */
    public void doGroupLeasingStatisticsFloor(String elevatorCode, String yesterday) {
        var dataMap = redisUtils.hmget(RedisKeyUtils.getElevatorDateMapKey(elevatorCode, yesterday));
        if (CollectionUtils.isEmpty(dataMap)) {
            return;
        }
        for (var item : dataMap.entrySet()) {
            var floor = item.getKey().toString();
            var totalQuantity = Double.parseDouble(item.getValue().toString());
            // 1. 获取统计信息并存储
            var entity = TblGroupLeasingFloorCoefficientEntity.builder()
                    .elevatorCode(elevatorCode)
                    .day(Date.valueOf(yesterday))
                    .floor(floor)
                    .dayTotalQuantity(totalQuantity)
                    .build();
            var isSuccess = groupLeasingFloorCoefficientMapper.insert(entity);
            if (Boolean.FALSE.equals(isSuccess)) {
                continue;
            }
            var id = entity.getId();
            // 2. 计算 群租系数，并更新
            // 2.1 获取最近30天记录，并计算平均群租系数
            var tmpEntity = TblGroupLeasingFloorCoefficientEntity.builder()
                    .elevatorCode(elevatorCode)
                    .floor(floor)
                    .build();
            var list = groupLeasingFloorCoefficientMapper.findByEntityLatestMonth(tmpEntity);
            // 2.2 计算月平均群租系数
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            var coefficient = list.stream()
                    .mapToDouble(TblGroupLeasingFloorCoefficientEntity::getDayTotalQuantity)
                    .sum() / (list.size() * 24);

            // 2.3 更新群租系统值
            var updateEntity = TblGroupLeasingFloorCoefficientEntity.builder()
                    .id(id)
                    .coefficient(coefficient)
                    .build();
            groupLeasingFloorCoefficientMapper.updateById(updateEntity);
        }
    }

    /**
     * 按电梯统计
     *
     * @param elevatorCode 电梯编号
     * @param yesterday    日期
     */
    public void doGroupLeasingStatisticsElevator(String elevatorCode, String yesterday) {
        var tmpFloorEntity = TblGroupLeasingFloorCoefficientEntity.builder()
                .elevatorCode(elevatorCode)
                .day(Date.valueOf(yesterday))
                .build();
        var list = groupLeasingFloorCoefficientMapper.findByEntity(tmpFloorEntity);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // 电梯信息
        var elevator = elevatorCacheService.getByElevatorCode(elevatorCode);
        if (elevator.getMaxFloor() == null) {
            return;
        }
        var minFloor = elevator.getMinFloor() == null
                ? VALID_MIN_FLOOR
                : (elevator.getMinFloor() >= VALID_MIN_FLOOR ? elevator.getMinFloor() : VALID_MIN_FLOOR);
        // 计算总值
        var totalQuantity = list.stream()
                .mapToDouble(TblGroupLeasingFloorCoefficientEntity::getDayTotalQuantity).sum();
        var floorCount = elevator.getMaxFloor() - minFloor + 1;

        var entity = TblGroupLeasingElevatorCoefficientEntity.builder()
                .elevatorCode(elevatorCode)
                .day(Date.valueOf(yesterday))
                .floorCount(floorCount)
                .dayTotalQuantity(totalQuantity)
                .build();
        var isSuccess = groupLeasingElevatorCoefficientMapper.insert(entity);
        if (Boolean.FALSE.equals(isSuccess)) {
            return;
        }
        var id = entity.getId();

        // 计算30天平均值
        // 获取最近30天记录，并计算平均群租系数
        var tmpMonthEntity = TblGroupLeasingElevatorCoefficientEntity.builder()
                .elevatorCode(elevatorCode)
                .build();
        var monthList = groupLeasingElevatorCoefficientMapper.findByEntityLatestMonth(tmpMonthEntity);
        // 计算月平均群租系数
        if (CollectionUtils.isEmpty(monthList)) {
            return;
        }
        var coefficient = monthList.stream()
                .mapToDouble(TblGroupLeasingElevatorCoefficientEntity::getDayTotalQuantity)
                .sum() / (monthList.size() * floorCount * 24);
        // 更新群租系统值
        var updateEntity = TblGroupLeasingElevatorCoefficientEntity.builder()
                .id(id)
                .coefficient(coefficient)
                .build();
        groupLeasingElevatorCoefficientMapper.updateById(updateEntity);
    }

    /**
     * 计算楼层的 群租热力图，阈值等级
     *
     * @param yesterday 日期
     */
    public void doGroupLeasingStatisticsFloorLevel(String yesterday) {
        // 1. 获取昨日的 电梯统计
        var elevatorCoefficientList = groupLeasingElevatorCoefficientMapper
                .findByEntity(TblGroupLeasingElevatorCoefficientEntity.builder()
                        .day(Date.valueOf(yesterday))
                        .build());
        if (CollectionUtils.isEmpty(elevatorCoefficientList)) {
            return;
        }

        elevatorCoefficientList.forEach(elevatorCoefficient -> {
            var elevatorCode = elevatorCoefficient.getElevatorCode();
            // 2. 获取昨日的 楼层统计
            var floorCoefficientList = groupLeasingFloorCoefficientMapper
                    .findByEntity(TblGroupLeasingFloorCoefficientEntity.builder()
                            .elevatorCode(elevatorCode)
                            .day(Date.valueOf(yesterday))
                            .build());
            if (CollectionUtils.isEmpty(floorCoefficientList)) {
                return;
            }
            // 3. 电梯信息
            var elevator = elevatorCacheService.getByElevatorCode(elevatorCode);
            // 3. 计算热力图，阈值等级
            floorCoefficientList.forEach(item -> saveStatisticsFloor(elevator, item.getFloor(), item.getCoefficient(),
                    elevatorCoefficient.getCoefficient()));
        });

    }

    /**
     * 按小区统计
     *
     * @param yesterday 日期
     */
    public void doGroupLeasingStatisticsVillage(String yesterday) {
        // 1. 获取有统计的电梯
        var elevatorEntity = TblGroupLeasingElevatorCoefficientEntity.builder()
                .day(Date.valueOf(yesterday))
                .build();
        var elevatorCoefficientList = groupLeasingElevatorCoefficientMapper.findByEntity(elevatorEntity);
        if (CollectionUtils.isEmpty(elevatorCoefficientList)) {
            return;
        }
        var elevatorCoefficientMap = elevatorCoefficientList.stream()
                .collect(Collectors
                        .toMap(TblGroupLeasingElevatorCoefficientEntity::getElevatorCode, Function.identity()));
        // 2. 根据电梯获取小区ID
        var elevatorCodes = elevatorCoefficientList.stream()
                .map(TblGroupLeasingElevatorCoefficientEntity::getElevatorCode)
                .toList();
        var elevatorsGroup = elevatorDao.listByCodes(elevatorCodes)
                .stream()
                .collect(Collectors.groupingBy(TblElevator::getVVillageId));
        // 3. 按小区ID 分组电梯，计算总数
        for (var group : elevatorsGroup.entrySet()) {
            var villageId = group.getKey();
            var elevatorCoefficientListGroup = new ArrayList<TblGroupLeasingElevatorCoefficientEntity>();
            group.getValue().forEach(item -> {
                if (elevatorCoefficientMap.containsKey(item.getVElevatorCode())) {
                    elevatorCoefficientListGroup.add(elevatorCoefficientMap.get(item.getVElevatorCode()));
                }
            });
            // 3.1 计算总值
            var totalQuantity = elevatorCoefficientListGroup.stream()
                    .mapToDouble(TblGroupLeasingElevatorCoefficientEntity::getDayTotalQuantity).sum();
            var elevatorCount = elevatorCoefficientListGroup.size();
            var floorQuantity = elevatorCoefficientListGroup.stream()
                    .mapToDouble(TblGroupLeasingElevatorCoefficientEntity::getFloorCount).sum();

            var entity = TblGroupLeasingVillageCoefficientEntity.builder()
                    .villageId(villageId)
                    .day(Date.valueOf(yesterday))
                    .elevatorCount(elevatorCount)
                    .dayTotalQuantity(totalQuantity)
                    .build();
            var isSuccess = groupLeasingVillageCoefficientMapper.insert(entity);
            var id = entity.getId();

            // 3.2. 计算30天平均值
            // 获取最近30天记录，并计算平均群租系数
            var tmpMonthEntity = TblGroupLeasingVillageCoefficientEntity.builder()
                    .villageId(villageId)
                    .build();
            var monthList = groupLeasingVillageCoefficientMapper.findByEntityLatestMonth(tmpMonthEntity);
            // 计算月平均群租系数
            if (CollectionUtils.isEmpty(monthList)) {
                return;
            }
            var coefficient = monthList.stream()
                    .mapToDouble(TblGroupLeasingVillageCoefficientEntity::getDayTotalQuantity)
                    .sum() / (monthList.size() * floorQuantity * 24);
            // 更新群租系统值
            var updateEntity = TblGroupLeasingVillageCoefficientEntity.builder()
                    .id(id)
                    .coefficient(coefficient)
                    .build();
            groupLeasingVillageCoefficientMapper.updateById(updateEntity);
        }
    }

    /**
     * 按电梯统计 热力图，阈值等级
     *
     * @param yesterday 日期
     */
    public void doGroupLeasingStatisticsElevatorLevel(String yesterday) {
        // 1. 获取昨日的小区统计
        var villageList = groupLeasingVillageCoefficientMapper
                .findByEntity(TblGroupLeasingVillageCoefficientEntity.builder()
                        .day(Date.valueOf(yesterday))
                        .build());
        if (CollectionUtils.isEmpty(villageList)) {
            return;
        }
        // 2. 获取昨日的电梯统计
        var elevatorList = groupLeasingElevatorCoefficientMapper
                .findByEntity(TblGroupLeasingElevatorCoefficientEntity.builder()
                        .day(Date.valueOf(yesterday))
                        .build());
        if (CollectionUtils.isEmpty(elevatorList)) {
            return;
        }
        // 3. 电梯按小区分组
        // 3.1. 电梯与小区关系
        var elevatorsGroup = elevatorDao.listByCodes(elevatorList.stream()
                        .map(TblGroupLeasingElevatorCoefficientEntity::getElevatorCode)
                        .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(TblElevator::getVElevatorCode, TblElevator::getVVillageId));
        // 3.2. 将电梯按小区分组
        var elevatorGroup = elevatorList.stream()
                .collect(Collectors.groupingBy(item -> elevatorsGroup.get(item.getElevatorCode())));
        // 4. 计算热力图，阈值等级
        var villageMap = villageList.stream()
                .collect(Collectors.toMap(TblGroupLeasingVillageCoefficientEntity::getVillageId, Function.identity()));
        for (var entry : elevatorGroup.entrySet()) {
            var villageId = entry.getKey();
            entry.getValue().forEach(item -> {
                var elevator = elevatorCacheService.getByElevatorCode(item.getElevatorCode());
                saveStatisticsElevator(villageMap.get(villageId), item, elevator.getGroupLeasingStepRange());
            });
        }
    }

    /**
     * 按楼层统计热力图
     *
     * @param elevator           电梯
     * @param floor              楼层
     * @param coefficient        每小时系数
     * @param averageCoefficient 平均每天系数
     */
    private void saveStatisticsFloor(ElevatorCacheDTO elevator, String floor, Double coefficient,
                                     Double averageCoefficient) {
        var statisticsType = "floor";
        var statisticsEntity = TblGroupLeasingStatisticsEntity.builder()
                .villageId(elevator.getVillageId())
                .elevatorCode(elevator.getElevatorCode())
                .statisticsType(statisticsType)
                .floor(floor)
                .dayCoefficient(coefficient)
                .averageCoefficient(averageCoefficient)
                .percent(coefficient / averageCoefficient)
                .level(buildLevel(coefficient * 100 / averageCoefficient, elevator.getGroupLeasingStepRange()))
                .build();
        // 是否已经存在相关记录
        var exists = groupLeasingStatisticsMapper.getByUnique(TblGroupLeasingStatisticsEntity.builder()
                .elevatorCode(elevator.getElevatorCode())
                .statisticsType(statisticsType)
                .floor(floor)
                .build());
        if (exists != null) {
            statisticsEntity.setId(exists.getId());
            groupLeasingStatisticsMapper.updateById(statisticsEntity);
        } else {
            groupLeasingStatisticsMapper.insert(statisticsEntity);
        }
    }

    /**
     * 按电梯统计热力图
     *
     * @param villageCoefficient  小区统计
     * @param elevatorCoefficient 电梯统计
     */
    private void saveStatisticsElevator(TblGroupLeasingVillageCoefficientEntity villageCoefficient,
                                        TblGroupLeasingElevatorCoefficientEntity elevatorCoefficient,
                                        String stepRange) {
        var statisticsType = "elevator";
        //var dayCoefficient = elevatorCoefficient.getCoefficient() * elevatorCoefficient.getFloorCount();
        var dayCoefficient = elevatorCoefficient.getCoefficient();
        var statisticsEntity = TblGroupLeasingStatisticsEntity.builder()
                .villageId(villageCoefficient.getVillageId())
                .elevatorCode(elevatorCoefficient.getElevatorCode())
                .statisticsType(statisticsType)
                .floor("")
                .dayCoefficient(dayCoefficient)
                .averageCoefficient(villageCoefficient.getCoefficient())
                .percent(dayCoefficient / villageCoefficient.getCoefficient())
                .level(buildLevel(dayCoefficient * 100 / villageCoefficient.getCoefficient(), stepRange))
                .build();
        // 是否已经存在相关记录
        var exists = groupLeasingStatisticsMapper.getByUnique(TblGroupLeasingStatisticsEntity.builder()
                .elevatorCode(elevatorCoefficient.getElevatorCode())
                .statisticsType(statisticsType)
                .floor("")
                .build());
        if (exists != null) {
            statisticsEntity.setId(exists.getId());
            groupLeasingStatisticsMapper.updateById(statisticsEntity);
        } else {
            groupLeasingStatisticsMapper.insert(statisticsEntity);
        }
    }

    private Integer buildLevel(double thresholdValue, String stepRange) {
        if (!StringUtils.hasText(stepRange)) {
            stepRange = "100, 150, 200";
        }
        var stepRangList = Arrays.stream(stepRange.split(","))
                .map(item -> Double.parseDouble(item.trim())).toList();
        var level = 0;
        for (var item : stepRangList) {
            if (thresholdValue < item) {
                return level;
            }
            level++;
        }
        return level;
    }

    private String getDateYesterday() {
        return new SimpleDateFormat(DatePattern.NORM_DATE_PATTERN)
                .format(DateTime.of(DateTime.now().setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                        .getTime() - 24 * 3600 * 1000));
    }


    private String getDateTimeAfter(String time, Long seconds) {
        return new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN).format(
                DateTime.of(
                        DateTime.of(time, DatePattern.NORM_DATETIME_PATTERN).getTime() + seconds * 1000L));
    }
}
