// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.kafka.forward.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.shmashine.kafka.forward.service.RedisServiceI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/16 13:53
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RedisServiceImpl implements RedisServiceI {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String KAFKA_MONITOR_ELEVATOR_PRE = "KAFKA:MONITOR:ELEVATOR:";
    private static final String KAFKA_FAULT_ELEVATOR_PRE = "KAFKA:FAULT:ELEVATOR:";

    @Override
    public void cacheKafkaMonitorElevator(String elevatorCode, String monitorInfo) {
        var redisKey = KAFKA_MONITOR_ELEVATOR_PRE + elevatorCode;
        redisTemplate.opsForValue().set(redisKey, monitorInfo);
    }

    @Override
    public Object getMonitorInfoByElevatorCode(String elevatorCode) {
        var redisKey = KAFKA_MONITOR_ELEVATOR_PRE + elevatorCode;
        return redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public void cacheKafkaFaultElevator(String elevatorCode, String toString) {
        var redisKey = KAFKA_FAULT_ELEVATOR_PRE + elevatorCode;
        redisTemplate.opsForValue().set(redisKey, toString);
    }

    @Override
    public Object getFaultInfoByElevatorCode(String elevatorCode) {
        var redisKey = KAFKA_FAULT_ELEVATOR_PRE + elevatorCode;
        return redisTemplate.opsForValue().get(redisKey);
    }
}
