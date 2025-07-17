// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.elevator.impl;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.shmashine.api.convert.ElevatorCacheEntityDtoConvertor;
import com.shmashine.api.dao.TblElevatorDao;
import com.shmashine.api.dao.TblVillageDao;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.api.service.elevator.ElevatorCacheService;
import com.shmashine.common.dto.ElevatorCacheDTO;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.utils.RedisKeyUtils;

import lombok.RequiredArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/7 15:47
 * @since v1.0
 */

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ElevatorCacheServiceImpl implements ElevatorCacheService {
    private final TblElevatorDao tblElevatorDao;
    private final TblVillageDao tblVillageDao;
    private final ElevatorCacheEntityDtoConvertor convertor;
    private final RedisUtils redisUtils;


    @Override
    public ElevatorCacheDTO getByElevatorCode(String elevatorCode) {
        var redisKey = RedisKeyUtils.getElevatorCacheKeyByCode(elevatorCode);
        var result = redisUtils.get(redisKey);
        if (StringUtils.hasText(result)) {
            return JSON.parseObject(result, ElevatorCacheDTO.class);
        }
        return updateCacheByElevatorCodeFromDatabase(elevatorCode);
    }

    @Override
    public ElevatorCacheDTO updateCacheByElevatorCodeFromDatabase(String elevatorCode) {
        var redisKey = RedisKeyUtils.getElevatorCacheKeyByCode(elevatorCode);
        var elevator = tblElevatorDao.getByElevatorCode(elevatorCode);
        if (elevator == null) {
            return null;
        }
        var village = tblVillageDao.getById(elevator.getVVillageId());
        var elevatorCacheDTO = convertor.entity2Dto(elevator, village);
        // 存入缓存
        redisUtils.set(redisKey, JSON.toJSONString(elevatorCacheDTO));
        // 城市推送相关的缓存
        redisUtils.del(RedisKeyUtils.getCityPushPlatformElevatorExistsKey(elevator.getVElevatorCode()));
        redisUtils.del("elevator_cache:" + elevatorCode);
        return elevatorCacheDTO;
    }

    @Override
    public Integer updateCacheByVillageIdFromDatabase(String villageId) {
        var tblElevator = new TblElevator();
        tblElevator.setVVillageId(villageId);
        // 清除城市推送 villiage缓存
        redisUtils.del(RedisKeyUtils.getCityPushPlatformProjectExistsKey(villageId));

        return updateCacheByEntityFromDatabase(tblElevator);
    }

    @Override
    public Integer updateCacheByEntityFromDatabase(TblElevator tblElevator) {
        var elevators = tblElevatorDao.getByEntity(tblElevator);
        if (CollectionUtils.isEmpty(elevators)) {
            return 0;
        }
        // 小区
        var villageIds = elevators.stream().map(TblElevator::getVVillageId).toList();
        var villageMap = tblVillageDao.listByIds(villageIds)
                .stream()
                .collect(Collectors.toMap(TblVillage::getVVillageId, Function.identity()));
        // 缓存
        elevators.forEach(elevator -> {
            var redisKey = RedisKeyUtils.getElevatorCacheKeyByCode(elevator.getVElevatorCode());
            var elevatorCacheDTO = convertor.entity2Dto(elevator, villageMap.get(elevator.getVVillageId()));
            // 存入缓存
            redisUtils.set(redisKey, JSON.toJSONString(elevatorCacheDTO));
        });
        return elevators.size();
    }
}
