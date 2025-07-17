package com.shmashine.socket.message.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.socket.device.entity.TblDevice;
import com.shmashine.socket.device.service.TblDeviceService;
import com.shmashine.socket.elevator.entity.TblElevator;
import com.shmashine.socket.elevator.service.TblElevatorService;
import com.shmashine.socket.fault.entity.TblFaultShield;
import com.shmashine.socket.fault.service.TblFaultShieldService;
import com.shmashine.socket.message.bean.ElevatorCache;
import com.shmashine.socket.message.bean.FaultShieldCache;
import com.shmashine.socket.message.bean.SensorFaultShieldCache;
import com.shmashine.socket.netty.ChannelManager;
import com.shmashine.socket.redis.RedisService;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务处理
 *
 * @author little.li
 */
@Slf4j
@Profile({"prod"})
@Component
public class TimeTaskHandle {

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(128, 256,
            1L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "TimeTaskHandle");


    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final TblFaultShieldService faultShieldService;

    private final TblElevatorService elevatorService;

    private final TblDeviceService deviceService;

    private final RedisService redisService;

    //加载传感器故障屏蔽
    @PostConstruct
    public void init() {

        //获取传感器故障屏蔽列表
        List<HashMap<String, String>> sensorFaultShields = deviceService.getSensorFaultShields();
        //获取传感器关联故障屏蔽列表
        List<HashMap<String, String>> faultShields = deviceService.getFaultShields();

        Map<String, List<Integer>> sensorCollect = sensorFaultShields.stream()
                .filter(it -> it.get("sensorFaultType") != null)
                .collect(Collectors.toMap(it -> it.get("elevatorCode"),
                        it -> Arrays.stream(it.get("sensorFaultType").split(","))
                                .map(i -> Integer.parseInt(i)).collect(Collectors.toList()))
                );

        Map<String, List<Integer>> faultCollect = faultShields.stream().filter(it -> it.get("faultType") != null)
                .collect(Collectors.toMap(it -> it.get("elevatorCode"),
                        it -> Arrays.stream(it.get("faultType").split(","))
                                .map(i -> Integer.parseInt(i)).collect(Collectors.toList()))
                );

        //刷新屏蔽缓存
        SensorFaultShieldCache.loadSensorFaultShieldCache(sensorCollect);
        SensorFaultShieldCache.loadFaultShieldCache(faultCollect);
    }

    @Autowired
    public TimeTaskHandle(TblFaultShieldService faultShieldService, TblElevatorService elevatorService,
                          TblDeviceService deviceService, RedisService redisService) {
        this.faultShieldService = faultShieldService;
        this.elevatorService = elevatorService;
        this.deviceService = deviceService;
        this.redisService = redisService;
    }


    /**
     * 每天00:00:30下发时间校准指令
     */
    @Scheduled(cron = "30 0 0 * * ?")
    @Async
    public void taskSendRtc() {

        executorService.submit(() -> {
            Map<String, Channel> channelMap = ChannelManager.getAllChannel();
            channelMap.forEach((key, value) -> {
                String[] split = key.split("_");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("TY", "Update");
                jsonObject.put("ST", "rtc");
                jsonObject.put("daytime", SIMPLE_DATE_FORMAT.format(new Date()));

                String sensorType = split[0];
                String elevatorCode = split[1];
                ChannelManager.sendMessageToChannel(elevatorCode, sensorType, jsonObject.toJSONString());
            });
        });

    }


    /**
     * 30秒触发一次 ping心跳消息
     */
    @Scheduled(cron = "0/30 * * * * ? ")
    @Async
    public void taskSendPing() {

        executorService.submit(() -> {

            Map<String, Channel> channelMap = ChannelManager.getAllChannel();

            channelMap.forEach((key, value) -> executorService.submit(() -> {

                String[] split = key.split("_");
                String sensorType = split[0];
                String elevatorCode = split[1];
                ChannelManager.sendMessageToChannel(elevatorCode, sensorType, SocketConstants.PING_MESSAGE);

            }));
        });

    }


    /**
     * 故障屏蔽缓存
     * fixedRate 10分钟触发一次 刷新故障屏蔽规则
     * initialDelay 程序启动10秒后开始第一次执行
     */
    @Scheduled(fixedRate = 600000, initialDelay = 10000)
    @Async
    public void taskReloadFailureShield() {

        executorService.submit(() -> {
            Map<String, List<TblFaultShield>> cacheMap = new HashMap<>();
            List<TblFaultShield> list = faultShieldService.list(new HashMap<>());

            if (CollectionUtils.isEmpty(list)) {
                return;
            }

            for (TblFaultShield shield : list) {
                String elevatorCode = shield.getVElevatorCode();
                List<TblFaultShield> shieldList = cacheMap.get(elevatorCode);

                if (shieldList == null) {
                    List<TblFaultShield> newShieldList = new ArrayList<>();
                    newShieldList.add(shield);
                    cacheMap.put(elevatorCode, newShieldList);
                } else {
                    shieldList.add(shield);
                }
            }
            FaultShieldCache.reloadCache(cacheMap);
        });

    }

    /**
     * 设备状态重置,6分钟一次
     */
    @Scheduled(fixedRate = 360000, initialDelay = 10000)
    @Async
    public void taskReloadDeviceStatus() {

        executorService.submit(() -> {

            Set<String> allChannelStatus = ChannelManager.getAllChannelStatus();

            //校验所有netty连接电梯及设备状态
            List<HashMap<String, Object>> allDeviceStatus = elevatorService.getAllNettyDeviceStatus();

            HashMap<String, HashMap<String, Integer>> elevatorStatus = new HashMap<>();

            allDeviceStatus.forEach(it -> {

                String elevatorCode = (String) it.get("elevatorCode");
                String sensorType = (String) it.get("sensorType");

                HashMap<String, Integer> elevator = elevatorStatus.get(elevatorCode);

                if (elevator != null) {

                    elevator.put(sensorType, (Integer) it.get("deviceStatus"));
                    return;
                }

                elevator = new HashMap<>();

                elevator.put("elevatorStatus", (Integer) it.get("elevatorStatus"));
                elevator.put(sensorType, (Integer) it.get("deviceStatus"));

                elevatorStatus.put(elevatorCode, elevator);

            });

            Set<String> elevators = elevatorStatus.keySet();


            elevators.forEach(

                    it -> {

                        HashMap<String, Integer> hashMap = elevatorStatus.get(it);

                        Set<String> keys = hashMap.keySet();

                        Integer es = 1;

                        for (String key : keys) {

                            //获取设备状态
                            if (!key.equals("elevatorStatus")) {

                                int status = allChannelStatus.contains(key + "_" + it) ? 1 : 0;

                                //修改设备状态
                                if (status != hashMap.get(key)) {
                                    elevatorService.changeDeviceStatus(it, key, status);
                                }

                                //有一台设备离线则为离线状态
                                if (status == 0) {
                                    es = 0;
                                }
                            }
                        }

                        //修改电梯状态
                        if (es != hashMap.get("elevatorStatus")) {
                            elevatorService.changeElevatorStatus(it, es);
                        }
                    }
            );

        });


    }

    /**
     * 电梯信息缓存
     * fixedRate 3分钟触发一次 刷新电梯楼层信息
     * initialDelay 程序启动10秒后开始第一次执行
     */
    @Scheduled(fixedRate = 180000, initialDelay = 10000)
    @Async
    public void taskReloadElevatorFloor() {

        executorService.submit(() -> {
            Map<String, TblElevator> cacheMap = new HashMap<>();
            List<TblElevator> list = elevatorService.list();

            if (CollectionUtils.isEmpty(list)) {
                return;
            }

            for (TblElevator elevator : list) {
                String elevatorCode = elevator.getVElevatorCode();
                cacheMap.put(elevatorCode, elevator);

            }
            ElevatorCache.reloadCache(cacheMap);
        });

    }


    /**
     * 电梯注册码信息Redis缓存
     * fixedRate 6分钟触发一次 刷新电梯注册码信息
     * initialDelay 程序启动10秒后开始第一次执行
     */
    @Scheduled(fixedRate = 360000, initialDelay = 10000)
    @Async
    public void taskReloadElevatorRegister() {

        executorService.submit(() -> {
            Map<String, String> map = new HashMap<>();
            List<TblElevator> list = elevatorService.list();

            if (CollectionUtils.isEmpty(list)) {
                return;
            }

            for (TblElevator elevator : list) {
                String registerNum = elevator.getVEquipmentCode();
                if (StringUtils.isEmpty(registerNum)) {
                    continue;
                }
                String elevatorCode = elevator.getVElevatorCode();
                List<TblDevice> deviceList = deviceService.getDeviceListByElevatorCode(elevatorCode);
                if (deviceList.isEmpty() || deviceList.get(0) == null || deviceList.get(0).getVServerIp() == null) {
                    continue;
                }
                redisService.putElevatorRegister(registerNum, elevatorCode, deviceList.get(0).getVServerIp());
                map.put(elevatorCode, registerNum);
            }
        });

    }

}
