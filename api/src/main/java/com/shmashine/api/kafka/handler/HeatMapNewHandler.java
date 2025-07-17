package com.shmashine.api.kafka.handler;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.utils.RedisKeyUtils;

@Component
public class HeatMapNewHandler {

    private static Logger heatMapLogger = LoggerFactory.getLogger("heatMapLogger");

    private final String recentFloorSuffix = ":recent:floor";

    @Resource
    private RedisTemplate redisTemplate;

    /*public void heatMapCount(String message){
        JSONObject messageJSON = JSONObject.parseObject(message);

        String elevatorCode = messageJSON.getString("elevatorCode");

        JSONObject monitorMessage= (JSONObject)messageJSON.get("monitorMessage");

        String sensorType = monitorMessage.getString ("sensorType");

        if("CarRoof".equals(sensorType)  || "SINGLEBOX".equals(sensorType)){
            //floorStatus 平层状态 0：平层，1：非平层
            int floorStatus=monitorMessage.getInteger("floorStatus");

            String floorStr=monitorMessage.getString("floor");
            boolean isNumber = NumberUtil.isNumber(floorStr);
            if(!isNumber){
                //这里直接丢弃掉不合法的楼层，例如B、B1等
                //System.out.println("floorstr is :" + floorStr);
                return;
            }
            //floor 楼层
            int currentFloorNum = Integer.valueOf(floorStr);

//            heatMapLogger.info("----[{}]电梯楼层信息统计，电梯code：[{}],floor：[{}],floorStatus:[{}]", messageJSON.get("time"), elevatorCode, currentFloorNum,floorStatus);

            if(0 == floorStatus){

                //设置key过期时间
                long expireTime = getExpireTime();

                //先判断该楼层作为最近停靠楼层是否存在于Redis
                String recentFloorInfo =  elevatorCode + recentFloorSuffix;
                Boolean hasKey = redisTemplate.hasKey(recentFloorInfo);
                if(hasKey){
                    Integer recentFloorNum = (Integer)redisTemplate.opsForValue().get(recentFloorInfo);
                    countFloorStopInfo(elevatorCode, currentFloorNum, recentFloorNum,expireTime,recentFloorInfo);
                }else{
                    fristCountFloorStopInfo( elevatorCode, currentFloorNum,expireTime,recentFloorInfo);
                }
            }
        }
    }*/

    public void heatMapCount(String elevatorCode, JSONObject monitorMessage) {
        //floorStatus 平层状态 0：平层，1：非平层
        int floorStatus = monitorMessage.getInteger("floorStatus");
        String floorStr = monitorMessage.getString("floor");
        boolean isNumber = NumberUtil.isNumber(floorStr);
        if (!isNumber) {
            //这里直接丢弃掉不合法的楼层，例如B、B1等
            //System.out.println("floorstr is :" + floorStr);
            return;
        }
        //floor 楼层
        int currentFloorNum = Integer.parseInt(floorStr);
//            heatMapLogger.info("----[{}]电梯楼层信息统计，电梯code：[{}],floor：[{}],floorStatus:[{}]", messageJSON.get("time"), elevatorCode, currentFloorNum,floorStatus);
        if (0 == floorStatus) {
            //设置key过期时间
            long expireTime = getExpireTime();
            //先判断该楼层作为最近停靠楼层是否存在于Redis
            String recentFloorInfo = elevatorCode + recentFloorSuffix;
            boolean hasKey = redisTemplate.hasKey(recentFloorInfo);
            if (hasKey) {
                Integer recentFloorNum = (Integer) redisTemplate.opsForValue().get(recentFloorInfo);
                countFloorStopInfo(elevatorCode, currentFloorNum, recentFloorNum, expireTime, recentFloorInfo);
            } else {
                fristCountFloorStopInfo(elevatorCode, currentFloorNum, expireTime, recentFloorInfo);
            }
        }
    }

    private void fristCountFloorStopInfo(String elevatorCode, Integer currentFloorNum, long expireTime, String recentFloorInfo) {
        String key = RedisConstants.ELEVATOR_COUNT_HEARTMAP + elevatorCode;
        //将统计的电梯编号存入redis
        // redisTemplate.opsForSet().add(elevatorInfo,elevatorCode);
        redisTemplate.opsForSet().add(RedisKeyUtils.getElevatorMembers(), elevatorCode);

        //统计电梯楼层停靠信息
        redisTemplate.opsForHash().put(key, String.valueOf(currentFloorNum), 1);

        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);

        redisTemplate.opsForValue().set(recentFloorInfo, currentFloorNum, expireTime, TimeUnit.SECONDS);
    }


    private void countFloorStopInfo(String elevatorCode, Integer currentFloorNum, Integer recentFloorNum, long expireTime, String recentFloorInfo) {
        String key = RedisConstants.ELEVATOR_COUNT_HEARTMAP + elevatorCode;
        if (!currentFloorNum.equals(recentFloorNum)) {
            //将统计的电梯编号存入redis
            // redisTemplate.opsForSet().add(elevatorInfo,elevatorCode);
            redisTemplate.opsForSet().add(RedisKeyUtils.getElevatorMembers(), elevatorCode);
            //该电梯的楼层统计信息是否存在
            Boolean floorInfo = redisTemplate.opsForHash().hasKey(key, String.valueOf(currentFloorNum));
            if (floorInfo) {
                Integer floorNum = (Integer) redisTemplate.opsForHash().get(key, String.valueOf(currentFloorNum));
                floorNum++;
                //统计电梯楼层停靠信息
                redisTemplate.opsForHash().put(key, String.valueOf(currentFloorNum), floorNum);
            } else {
                //统计电梯楼层停靠信息
                redisTemplate.opsForHash().put(key, String.valueOf(currentFloorNum), 1);
                //设置统计电梯停靠统计信息Key的过期时间
                redisTemplate.expire(key, getExpireTime(), TimeUnit.SECONDS);
            }

            //更新最近停靠的楼层信息
            redisTemplate.opsForValue().set(recentFloorInfo, currentFloorNum, expireTime, TimeUnit.SECONDS);
        }
    }

    private long getExpireTime() {
        //设置key过期时间
        DateTime now = DateTime.now();
        return DateUtil.between(now, DateUtil.endOfDay(now), DateUnit.SECOND);
    }
}
