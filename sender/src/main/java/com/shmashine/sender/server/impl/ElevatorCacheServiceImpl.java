// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.server.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.message.FaultWithVideoPhoto;
import com.shmashine.common.utils.RedisKeyUtils;
import com.shmashine.sender.dto.mongo.ElevatorFaultCacheDTO;
import com.shmashine.sender.redis.utils.RedisUtils;
import com.shmashine.sender.server.ElevatorCacheServiceI;
import com.shmashine.sender.utils.MongoTemplateUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/27 16:52
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ElevatorCacheServiceImpl implements ElevatorCacheServiceI {
    private final RedisUtils redisUtils;
    private final MongoTemplateUtil mongoTemplateUtil;

    @Override
    public <T> void setMonitorCache(String elevatorCode, T message) {
        redisUtils.set(RedisKeyUtils.getRunKafkaMessageCacheKey(elevatorCode),
                message instanceof String ? message : JSON.toJSONString(message));
    }

    @Override
    public Object getMonitorCache(String elevatorCode) {
        var res = redisUtils.get(RedisKeyUtils.getRunKafkaMessageCacheKey(elevatorCode));
        return res == null ? null : JSON.parseObject(res);
    }

    @Override
    public <T> void setOnOfflineStatusCache(String elevatorCode, T message) {
        redisUtils.set(RedisKeyUtils.getOnOfflineKafkaMessageCacheKey(elevatorCode),
                message instanceof String ? message : JSON.toJSONString(message));
    }

    @Override
    public String getOnOfflineStatusCache(String elevatorCode) {
        return redisUtils.get(RedisKeyUtils.getOnOfflineKafkaMessageCacheKey(elevatorCode));
    }

    @Override
    public <T> void saveFaultMessage(String elevatorCode, T message) {
        var faultMessage = JSON.parseObject(JSON.toJSONString(message), FaultWithVideoPhoto.class);
        var hasRecord = mongoTemplateUtil.queryByFaultId(faultMessage.getFaultId(), ElevatorFaultCacheDTO.class);
        if (hasRecord == null) {
            mongoTemplateUtil.insert(ElevatorFaultCacheDTO.builder()
                    .faultId(faultMessage.getFaultId())
                    .elevatorCode(faultMessage.getElevatorCode())
                    .createAt(DateUtil.date())
                    .message(JSON.toJSONString(message))
                    .build());
            return;
        }
        hasRecord.setMessage(JSON.toJSONString(message));
        mongoTemplateUtil.updateById(hasRecord);
    }

    @Override
    public List<Object> getFaultMessageCache(String elevatorCode) {
        var res = mongoTemplateUtil.queryByElevatorCodeDesc(elevatorCode, ElevatorFaultCacheDTO.class, "faultId");
        if (CollectionUtils.isEmpty(res)) {
            return null;
        }
        return res.stream()
                .map(item -> JSON.parseObject(item.getMessage(), FaultWithVideoPhoto.class))
                .collect(Collectors.toList());
    }
}
