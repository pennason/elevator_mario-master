// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.kafka.handler;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.api.service.elevator.ElevatorCacheService;
import com.shmashine.common.utils.RedisKeyUtils;

import lombok.RequiredArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/7 9:31
 * @since v1.0
 */

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class NightWatchHandler {
    private final ElevatorCacheService elevatorCacheService;
    private final RedisUtils redisUtils;

    public void checkAndSaveNeedWatchElevator(String elevatorCode, JSONObject monitorMessage, String time) {
        // 1. 获取电梯信息
        var elevator = elevatorCacheService.getByElevatorCode(elevatorCode);

        // 2. 判断是否开启了夜间守护 为1代表启用了
        if (elevator.getNightWatchStatus() <= 0) {
            return;
        }
        // 3. 判断是否在守护时间段内
        var collectTime = Time.valueOf(time.substring(11));
        var startTime = Time.valueOf(elevator.getNightWatchStartTime());
        var endTime = Time.valueOf(elevator.getNightWatchEndTime());
        // 3.1. 开始时间小于结束时间
        if (startTime.before(endTime)) {
            if (collectTime.before(startTime) || collectTime.after(endTime)) {
                return;
            }
        }
        // 3.2. 开始时间大于结束时间
        else {
            if (collectTime.before(startTime) && collectTime.after(endTime)) {
                return;
            }
        }

        /*// 4. 判断是否有人
        var hasPeople = monitorMessage.get("hasPeople");
        if (hasPeople == null || Integer.parseInt(hasPeople.toString()) == 0) {
            return;
        }*/

        // 5. 记录到队列， 用于后期获取监控视频
        var map = new HashMap<String, Object>(JSON.parseObject(JSON.toJSONString(monitorMessage), Map.class));
        map.put("elevatorCode", elevatorCode);
        map.put("time", time);
        redisUtils.lSet(RedisKeyUtils.getElevatorNightWatchQueue(), JSON.toJSONString(map));
    }

}
