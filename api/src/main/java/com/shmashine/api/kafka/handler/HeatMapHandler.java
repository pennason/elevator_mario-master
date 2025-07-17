package com.shmashine.api.kafka.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSONObject;

@Component
public class HeatMapHandler {

    @Resource
    private RedisTemplate redisTemplate;

    public void heatMapCount(String message) {
        JSONObject messageJSON = JSONObject.parseObject(message);

        String elevatorCode = messageJSON.getString("elevatorCode");

        JSONObject monitorMessage = (JSONObject) messageJSON.get("monitorMessage");

        String sensorType = monitorMessage.getString("sensorType");

        Map<String, Object> countElevatorFloorInfo = new HashMap<>();

        Map<String, Object> FloorInfo = new HashMap<>();

        if ("CarRoof".equals(sensorType) || "SINGLEBOX".equals(sensorType)) {
            //0 关门 1 开门 2 关门中 3 开门中
            int droopClose = monitorMessage.getInteger("droopClose");
            int floorNumber = monitorMessage.getInteger("floor");
            String elevatorKey = "countHeatMap:" + elevatorCode;

            //redis中上次关门楼层信息的key
            String doorCloseFloorKey = "elevator:floorInfo:" + elevatorCode;

            boolean isExist = redisTemplate.hasKey(elevatorKey);

            if (isExist) {
                Map<String, Object> redisCountElevatorFloorInfo = (Map<String, Object>) redisTemplate.boundValueOps(elevatorKey).get();

                int redisDroopClose = (Integer) redisCountElevatorFloorInfo.get("droopClose");

                if (redisDroopClose == 0 && droopClose == 1) {

                    int lastDoorCloseFloor = (int) redisTemplate.opsForValue().get(doorCloseFloorKey);

                    Map<String, Object> countElevatorFloorInfoMap = (Map<String, Object>) redisCountElevatorFloorInfo.get("countElevatorFloorInfo");

                    boolean isExistFloorNumber = countElevatorFloorInfoMap.containsKey(String.valueOf(floorNumber));

                    if (floorNumber != lastDoorCloseFloor) {
                        if (isExistFloorNumber) {
                            int currfloorNumberCount = (Integer) countElevatorFloorInfoMap.get(String.valueOf(floorNumber));

                            currfloorNumberCount++;

                            countElevatorFloorInfoMap.put(String.valueOf(floorNumber), currfloorNumberCount);

                            redisCountElevatorFloorInfo.put("droopClose", 1);

                            redisCountElevatorFloorInfo.put("countElevatorFloorInfo", countElevatorFloorInfoMap);

                            //设置key过期时间
                            long expireTime = getExpireTime();

                            redisTemplate.opsForValue().set(elevatorKey, redisCountElevatorFloorInfo, expireTime, TimeUnit.SECONDS);

                        } else {

                            countElevatorFloorInfoMap.put(String.valueOf(floorNumber), 1);

                            redisCountElevatorFloorInfo.put("droopClose", 1);

                            redisCountElevatorFloorInfo.put("countElevatorFloorInfo", countElevatorFloorInfoMap);

                            //设置key过期时间
                            long expireTime = getExpireTime();

                            redisTemplate.opsForValue().set(elevatorKey, redisCountElevatorFloorInfo, expireTime, TimeUnit.SECONDS);
                        }

                        //更新上一次关门的楼层信息
                        redisTemplate.opsForValue().set(doorCloseFloorKey, floorNumber);
                    }

                } else if (redisDroopClose == 1 && droopClose == 0) {
                    redisCountElevatorFloorInfo.put("droopClose", 0);

                    //设置key过期时间
                    long expireTime = getExpireTime();

                    redisTemplate.opsForValue().set(elevatorKey, redisCountElevatorFloorInfo, expireTime, TimeUnit.SECONDS);
                }
            } else {
                if (0 == droopClose) {
                    //设置key过期时间
                    long expireTime = getExpireTime();

                    countElevatorFloorInfo.put("droopClose", 0);

                    //FloorInfo.put(String.valueOf(floorNumber),0);
                    countElevatorFloorInfo.put("countElevatorFloorInfo", FloorInfo);
                    redisTemplate.opsForValue().set(elevatorKey, countElevatorFloorInfo, expireTime, TimeUnit.SECONDS);

                    //将本次关门所在楼层信息记录在redis中
                    redisTemplate.opsForValue().set(doorCloseFloorKey, floorNumber);
                }
            }
        }
    }

    private long getExpireTime() {
        //设置key过期时间
        String currDateTimeStr = DateUtil.format(DateTime.now(), DatePattern.NORM_DATETIME_PATTERN);

        Date currDateTime = DateUtil.parse(currDateTimeStr);

        String endDateTimeStr = DateUtil.format(DateTime.now(), "yyyy-MM-dd 23:59:59");
        Date endDateTime = DateUtil.parse(endDateTimeStr);

        long expireTime = DateUtil.between(currDateTime, endDateTime, DateUnit.SECOND);

        return expireTime;
    }
}
