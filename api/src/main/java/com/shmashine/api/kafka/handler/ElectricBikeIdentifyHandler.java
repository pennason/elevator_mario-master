// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.kafka.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.common.utils.RedisKeyUtils;

import lombok.RequiredArgsConstructor;

/**
 * 麦信平台自研电动车识别 消息处理
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/13 10:52
 * @since v1.0
 */

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ElectricBikeIdentifyHandler {
    private final RedisUtils redisUtils;

    public void checkAndSaveByElevator(String elevatorCode, JSONObject monitorMessage, String time) {
        // 1 判断所在楼层是否是1层
        var floorStr = monitorMessage.getString("floor");
        if (!"1".equals(floorStr)) {
            //这里直接丢弃非1层的记录
            return;
        }
        // 2. 判断是否关门状态 0 关门 1 开门 2 关门中 3 开门中
        int droopClose = monitorMessage.getInteger("droopClose");
        if (0 != droopClose) {
            //这里直接丢弃不是关门状态的记录
            return;
        }
        // 3. 轿厢运行方向是否为上行 0：停留，1：上行，2：下行
        int direction = monitorMessage.getInteger("direction");
        if (1 != direction) {
            //这里直接丢弃不是上行状态的记录
            return;
        }

        // 4. 记录到队列， 用于后期获取监控视频
        var map = new HashMap<String, Object>(JSON.parseObject(JSON.toJSONString(monitorMessage), Map.class));
        map.put("elevatorCode", elevatorCode);
        map.put("time", time);
        redisUtils.lSet(RedisKeyUtils.getElevatorElectricBikeIdentifyQueue(), JSON.toJSONString(map));
    }
}
