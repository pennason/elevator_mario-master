// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.kafka.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.NumberUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.api.service.elevator.ElevatorCacheService;
import com.shmashine.common.enums.GroupLeasingResultEnum;
import com.shmashine.common.utils.RedisKeyUtils;

import lombok.RequiredArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/7 14:08
 * @since v1.0
 */

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GroupLeasingHandler {
    private final RedisUtils redisUtils;
    private final ElevatorCacheService elevatorCacheService;
    //private static final Integer VALID_MIN_FLOOR = 2;

    public void checkAndSaveByElevator(String elevatorCode, JSONObject monitorMessage, String time) {
        // 测试
        /*if (!elevatorCode.startsWith("MX48")) {
            return;
        }*/
        // 1. 基础数据验证
        // 1.1 非平层舍弃 floorStatus 平层状态 0：平层，1：非平层
        int floorStatus = monitorMessage.getInteger("floorStatus");
        if (0 != floorStatus) {
            return;
        }
        // 1.2 非正常楼层舍弃
        var floorStr = monitorMessage.getString("floor");
        boolean isNumber = NumberUtil.isNumber(floorStr);
        if (!isNumber) {
            //这里直接丢弃掉不合法的楼层，例如B、B1等
            return;
        }
        //  1.3 一楼及以下不统计, 需要先记录， 用于电梯变化
        /*if (Integer.parseInt(floorStr) < VALID_MIN_FLOOR) {
            return;
        }*/

        // 2. 获取电梯信息
        var elevator = elevatorCacheService.getByElevatorCode(elevatorCode);
        // 2.1. 判断是否开启了群租识别
        if (0 == elevator.getGroupLeasingStatus()) {
            return;
        }
        // 2.2. 如果已经群租确认则不在处理
        if (GroupLeasingResultEnum.CONFIRMED.getStatus().equals(elevator.getGroupLeasingResult())) {
            return;
        }

        var map = new HashMap<String, Object>(JSON.parseObject(JSON.toJSONString(monitorMessage), Map.class));
        map.put("elevatorCode", elevatorCode);
        map.put("time", time);
        redisUtils.lSet(RedisKeyUtils.getElevatorGroupLeasingQueue(), JSON.toJSONString(map));
    }
}
