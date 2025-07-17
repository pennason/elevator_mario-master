// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.elevator.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;

import com.shmashine.api.dao.TblCameraDownloadTaskMapper;
import com.shmashine.api.dao.TblGroupLeasingFloorEvidenceMapper;
import com.shmashine.api.dao.TblGroupLeasingStatisticsMapper;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.elevator.ElevatorGroupLeasingHotMapModule;
import com.shmashine.api.module.elevator.GroupLeasingFloorEvidenceModule;
import com.shmashine.api.service.elevator.ElevatorGroupLeasingServiceI;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;
import com.shmashine.common.entity.TblGroupLeasingFloorEvidenceEntity;
import com.shmashine.common.entity.TblGroupLeasingStatisticsEntity;
import com.shmashine.common.enums.CameraTaskTypeEnum;
import com.shmashine.common.enums.GroupLeasingLevelEnum;

import lombok.RequiredArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/3/10 14:09
 * @since v1.0
 */

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ElevatorGroupLeasingServiceImpl implements ElevatorGroupLeasingServiceI {
    private final TblGroupLeasingStatisticsMapper statisticsMapper;
    private final TblCameraDownloadTaskMapper cameraDownloadTaskMapper;
    private final TblGroupLeasingFloorEvidenceMapper floorEvidenceMapper;

    @Override
    public List<ElevatorGroupLeasingHotMapModule> getFloorWithEvidenceByElevatorCode(String elevatorCode) {
        var floorEntities = statisticsMapper.findByEntity(TblGroupLeasingStatisticsEntity.builder()
                .elevatorCode(elevatorCode)
                .statisticsType("floor")
                .build());
        if (CollectionUtils.isEmpty(floorEntities)) {
            return Collections.emptyList();
        }
        var hotMap = floorEntities.stream()
                .map(item -> ElevatorGroupLeasingHotMapModule.builder()
                        .elevatorCode(item.getElevatorCode())
                        .statisticsType(item.getStatisticsType())
                        .floor(item.getFloor())
                        .dayCoefficient(item.getDayCoefficient())
                        .averageCoefficient(item.getAverageCoefficient())
                        .percent(item.getPercent())
                        .level(item.getLevel())
                        .build())
                .sorted(Comparator.comparing(item -> Integer.parseInt(item.getFloor())))
                .toList();

        // 获取取证信息
        var evidenceMap = cameraDownloadTaskMapper.findByElevatorCodeAndTaskType(elevatorCode,
                        CameraTaskTypeEnum.GROUP_LEASING.getCode())
                .stream()
                .collect(Collectors.groupingBy(TblCameraDownloadTaskEntity::getFloor));
        if (CollectionUtils.isEmpty(evidenceMap)) {
            return hotMap;
        }
        hotMap.forEach(item -> {
            var floor = item.getFloor();
            if (evidenceMap.containsKey(floor)) {
                item.setEvidences(evidenceMap.get(floor)
                        .stream()
                        .map(entity -> ElevatorGroupLeasingHotMapModule.CameraDownloadTaskModule.builder()
                                .elevatorCode(entity.getElevatorCode())
                                .taskCustomId(entity.getTaskCustomId())
                                .collectTime(StringUtils.hasText(entity.getCollectTime())
                                        ? entity.getCollectTime() : dateShortToNormal(entity.getStartTime()))
                                .startTime(dateShortToNormal(entity.getStartTime()))
                                .endTime(dateShortToNormal(entity.getEndTime()))
                                .floor(entity.getFloor())
                                .ossUrl(StringUtils.hasText(entity.getOssUrl())
                                        ? entity.getOssUrl() : entity.getSourceUrl())
                                .build())
                        .collect(Collectors
                                .toMap(ElevatorGroupLeasingHotMapModule.CameraDownloadTaskModule::getCollectTime,
                                        Function.identity())));

            }
        });

        return hotMap;
    }

    @Override
    public String dateShortToNormal(String date) {
        if (!StringUtils.hasText(date)) {
            return null;
        }
        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + " "
                + date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12);
    }

    @Override
    public List<TblGroupLeasingFloorEvidenceEntity> getFloorEvidenceConfigByElevatorCode(String elevatorCode) {
        return floorEvidenceMapper.findByEntity(TblGroupLeasingFloorEvidenceEntity.builder()
                .elevatorCode(elevatorCode)
                .build());
    }

    @Override
    public List<TblGroupLeasingStatisticsEntity> getFloorSuspiciousByElevatorCode(String elevatorCode) {
        return statisticsMapper.listByElevatorCodes(Collections.singletonList(elevatorCode),
                "floor", GroupLeasingLevelEnum.VERY_SUSPICIOUS.getLevel());

    }

    @Override
    public ResponseResult saveFloorEvidences(GroupLeasingFloorEvidenceModule module) {
        if (!StringUtils.hasText(module.getElevatorCode()) || CollectionUtils.isEmpty(module.getFloors())) {
            return ResponseResult.error("电梯编号或楼层不能为空");
        }
        var date = DateUtil.tomorrow().toJdkDate();
        if (module.getDate() != null) {
            date = DateUtil.parse(module.getDate(), DatePattern.NORM_DATE_PATTERN);
        }
        var startTime = LocalDateTimeUtil.beginOfDay(LocalDateTimeUtil.of(date));
        var endTime = LocalDateTimeUtil.endOfDay(LocalDateTimeUtil.of(date));
        module.getFloors().forEach(floor -> {
            floorEvidenceMapper.save(TblGroupLeasingFloorEvidenceEntity.builder()
                    .elevatorCode(module.getElevatorCode())
                    .floor(floor)
                    .startTime(startTime)
                    .endTime(endTime)
                    .status(0)
                    .build());
        });
        return ResponseResult.successObj(module.getFloors().size());
    }
}
