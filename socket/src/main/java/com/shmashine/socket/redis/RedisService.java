package com.shmashine.socket.redis;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;

import com.google.common.collect.ImmutableMap;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.utils.IpUtils;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.socket.elevator.entity.TblElevator;
import com.shmashine.socket.elevator.service.TblElevatorService;
import com.shmashine.socket.nezha.service.ElevatorNewService;
import com.shmashine.socket.properties.FaultMaskingProperties;
import com.shmashine.socket.redis.utils.RedisKeyUtils;
import com.shmashine.socket.redis.utils.RedisUtils;

import lombok.RequiredArgsConstructor;

/**
 * redis服务
 *
 * @author little.li
 */
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RedisService {
    private final RedisUtils redisUtils;
    private final TblElevatorService elevatorService;
    private final ElevatorNewService elevatorNewService;
    private final FaultMaskingProperties faultMaskingProperties;

    @Value("${server.port}")
    public String serverPort;


    /**
     * redis中记录设备上线状态
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @param serverIp     服务器ip
     * @param sensorIp     设备内网ip
     * @param nowTime      格式化时间
     */
    public void updateOnLineStatus(String elevatorCode, String sensorType, String serverIp,
                                   String sensorIp, String nowTime) {

        Map<String, String> map = new HashMap<>();
        map.put("online", "1");
        map.put("online_time", nowTime);
        map.put("server_ip", serverIp);
        var localPort = IpUtils.getLocalPort();
        map.put("server_port", org.springframework.util.StringUtils.hasText(localPort) ? localPort : serverPort);
        /** 2020年9月30日15:30:56 添加设备内网ip */
        map.put("sensor_ip", sensorIp);
        // 更新设备上线状态
        String deviceStatus = RedisKeyUtils.getDeviceStatus(elevatorCode, sensorType);
        redisUtils.hmSet(deviceStatus, map);

        // 更新电梯上线状态
        String elevatorStatus = RedisKeyUtils.getElevatorStatus(elevatorCode);
        Map<String, String> elevatorMap = new HashMap<>();
        elevatorMap.put("online", "1");
        redisUtils.hmSet(elevatorStatus, elevatorMap);

    }


    /**
     * Redis中记录设备离线状态
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 是否将电梯设置为离线
     */
    public boolean updateOffLineStatus(String elevatorCode, String sensorType, String nowTime) {
        // 记录离线状态
        String statusKey = RedisKeyUtils.getDeviceStatus(elevatorCode, sensorType);
        Map<String, String> map = new HashMap<>();
        map.put("online", "0");
        map.put("offline_time", nowTime);
        redisUtils.hmSet(statusKey, map);
        // 将电梯设置为离线
        /**
         * 有一台离线则置为离线
         */
        String elevatorStatus = RedisKeyUtils.getElevatorStatus(elevatorCode);
        redisUtils.hmSet(elevatorStatus, "online", "0");

        ///////////////////////////////////////////////////////更新nezha_web使用的Redis数据
        //updateOffLineStatus(elevatorCode, sensorType);

        return true;
    }


    /**
     * Redis中记录设备离线状态
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     */
    public void updateOffLineStatus(String elevatorCode, String sensorType) {
        Map<String, String> systemMap = new HashMap<>();

        boolean isUpdateOffLine = false;

        // 记录离线状态
        if (SocketConstants.SENSOR_TYPE_MOTOR_ROOM.equals(sensorType)) {
            // 机房设备离线
            systemMap.put("machineyRoomOnLine", "0");
            systemMap.put("machineyRoomOffLineDate", TimeUtils.nowTime());
            String onlineKey = String.format("%s:%s:%s:%s:%s", "elevator", "info",
                    elevatorCode, "machineyRoom", "onLine");

            redisUtils.del(onlineKey);

            // 如果另一个设备离线，则更新数据库
            String key = String.format("%s:%s:%s:%s:%s", "elevator", "info", elevatorCode, "carRoof", "onLine");
            String status = redisUtils.getStr(key);
            if (status == null) {
                isUpdateOffLine = true;
            }

        } else {
            // 轿顶设备离线
            systemMap.put("onLine", "0");
            systemMap.put("offLineDate", TimeUtils.nowTime());
            String onlineKey = String.format("%s:%s:%s:%s:%s", "elevator", "info", elevatorCode, "carRoof", "onLine");
            redisUtils.del(onlineKey);

            // 如果另一个设备离线，则更新数据库
            String key = String.format("%s:%s:%s:%s:%s", "elevator", "info", elevatorCode, "machineyRoom", "onLine");
            String status = redisUtils.getStr(key);
            if (status == null) {
                isUpdateOffLine = true;
            }
        }

        // 记录离线时间
        String systemKey = RedisKeyUtils.systemKey(elevatorCode);
        redisUtils.hmSet(systemKey, systemMap);

        if (isUpdateOffLine) {
            // 更新设备状态为离线
            elevatorNewService.updateOffline(elevatorCode, sensorType);
        }

    }


    /**
     * 轿顶设备更新服务模式 - 停止服务
     *
     * @param elevatorCode     电梯编号
     * @param deviceModeStatus 设备上报的服务模式，服务模式 0 正常运行，1 检修服务，2 停止服务
     * @return 服务模式 0 正常运行，1 检修服务，2 停止服务 (优先级越高，数字越大)
     */
    public Integer singleBoxUpdateModeStatus(String elevatorCode, Integer deviceModeStatus) {
        try {
            String statusKey = RedisKeyUtils.getElevatorStatus(elevatorCode);
            String status = redisUtils.hmGet(statusKey, "mode_status");

            //设备软检修状态
            String softStatus = redisUtils.getStr("elevator:status:" + elevatorCode);
            if (softStatus != null && deviceModeStatus == 0) {
                deviceModeStatus = 1;
            }

            if (status == null) {
                // redis中不存在服务模式，则将设备上报的服务模式存储
                redisUtils.hmSet(statusKey, "mode_status", String.valueOf(deviceModeStatus));
                elevatorService.updateModeStatus(elevatorCode, String.valueOf(deviceModeStatus));
                return deviceModeStatus;
            }

            // 比较设备上报与Redis中的服务模式 更新为优先级高的服务模式
            int modeStatus = Integer.parseInt(status);
            if (modeStatus == deviceModeStatus) {
                return modeStatus;
            }

            if (modeStatus == 2 && deviceModeStatus == 1) {
                return modeStatus;
            }
            /**
             * 消除检修模式: (deviceModeStatus == 0 && modeStatus == 1)
             * 置为检修模式: (deviceModeStatus == 1 && modeStatus == 0)
             * 停止服务优先级高于其他模式: (deviceModeStatus == 2)
             * 消除停止服务: (deviceModeStatus == 0 && modeStatus == 2)
             */
            redisUtils.hmSet(statusKey, "mode_status", String.valueOf(deviceModeStatus));
            elevatorService.updateModeStatus(elevatorCode, String.valueOf(deviceModeStatus));
            return deviceModeStatus;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 机房设备更新服务模式 - 检修模式
     *
     * @param elevatorCode     电梯编号
     * @param deviceModeStatus 设备上报的服务模式，服务模式 0 正常运行，1 检修服务，2 停止服务（检修由机房上传，停止由轿顶上传）
     * @return 服务模式 0 正常运行，1 检修服务，2 停止服务 (优先级越高，数字越大)
     */
    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    public Integer motorRoomUpdateModeStatus(String elevatorCode, Integer deviceModeStatus) {
        try {
            String statusKey = RedisKeyUtils.getElevatorStatus(elevatorCode);

            //设备软检修状态
            String softStatus = redisUtils.getStr("elevator:status:" + elevatorCode);
            if (softStatus != null && deviceModeStatus == 0) {
                deviceModeStatus = 1;
            }

            //设置机房模式缓存
            if (deviceModeStatus < 3) {
                redisUtils.hmSet(statusKey, "motorRoom_mode_status", String.valueOf(deviceModeStatus));
                //获取轿顶模式缓存
                String carRoofModeStatus = redisUtils.hmGet(statusKey, "carRoof_mode_status");
                if (StrUtil.isNotEmpty(carRoofModeStatus)) {
                    deviceModeStatus = Math.max(deviceModeStatus, Integer.parseInt(carRoofModeStatus));
                }
            }

            String status = redisUtils.hmGet(statusKey, "mode_status");
            if (status == null) {
                // redis中不存在服务模式，则将设备上报的服务模式存储
                redisUtils.hmSet(statusKey, "mode_status", String.valueOf(deviceModeStatus));
                elevatorService.updateModeStatus(elevatorCode, String.valueOf(deviceModeStatus));
                return deviceModeStatus;
            }

            // 比较设备上报与Redis中的服务模式 更新为优先级高的服务模式
            int modeStatus = Integer.parseInt(status);
            if (modeStatus == deviceModeStatus) {
                return modeStatus;
            }
            //消除检修模式
            if (deviceModeStatus == 0 && modeStatus == 1) {
                redisUtils.hmSet(statusKey, "mode_status", String.valueOf(deviceModeStatus));
                elevatorService.updateModeStatus(elevatorCode, String.valueOf(deviceModeStatus));
                return deviceModeStatus;
            }
            //置为检修模式
            if (deviceModeStatus == 1 && modeStatus == 0) {
                redisUtils.hmSet(statusKey, "mode_status", String.valueOf(deviceModeStatus));
                elevatorService.updateModeStatus(elevatorCode, String.valueOf(deviceModeStatus));
                return deviceModeStatus;
            }
            //置为停止模式
            if (modeStatus != 2 && deviceModeStatus == 2) {
                redisUtils.hmSet(statusKey, "mode_status", String.valueOf(deviceModeStatus));
                elevatorService.updateModeStatus(elevatorCode, String.valueOf(deviceModeStatus));
                return deviceModeStatus;
            }
            //消除停止模式
            if (deviceModeStatus == 3 && modeStatus == 2) {
                redisUtils.hmSet(statusKey, "mode_status", String.valueOf(0));
                elevatorService.updateModeStatus(elevatorCode, String.valueOf(0));
                return 0;
            }
            return modeStatus;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取设备运行模式
     *
     * @param elevatorCode 电梯编号
     * @return 服务模式 0 正常运行，1 检修服务，2 停止服务 (优先级越高，数字越大)
     */
    public Integer getElevatorModeStatus(String elevatorCode) {
        try {
            String statusKey = RedisKeyUtils.getElevatorStatus(elevatorCode);
            String status = redisUtils.hmGet(statusKey, "mode_status");
            if (status == null) {
                // redis中不存在服务模式，则将设备上报的服务模式存储
                TblElevator elevator = elevatorService.getByElevatorCode(elevatorCode);
                Integer modeStatus = elevator.getIModeStatus();
                redisUtils.hmSet(statusKey, "mode_status", modeStatus.toString());
                return modeStatus;
            } else {
                return Integer.valueOf(status);
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取手动检修状态
     *
     * @param elevatorId 电梯id
     */
    public String getElevatorStatus(String elevatorId) {
        String key = "elevator:status:" + elevatorId;
        return redisUtils.getStr(key);
    }


    /**
     * 根据不同设备类型，判断是否将电梯设置为离线
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 是否将电梯设置为离线
     */
    private boolean isUpdateElevatorOffLine(String elevatorCode, String sensorType) {
        switch (sensorType) {
            case SocketConstants.SENSOR_TYPE_CUBE, SocketConstants.SENSOR_TYPE_ESCALATOR,
                 SocketConstants.SENSOR_TYPE_SINGLEBOX, SocketConstants.SENSOR_TYPE_FRONT,
                 SocketConstants.SENSOR_TYPE_CAR_DOOR -> {
                return true;
            }
            case SocketConstants.SENSOR_TYPE_CAR_ROOF -> {
                String deviceStatus = RedisKeyUtils.getDeviceStatus(elevatorCode,
                        SocketConstants.SENSOR_TYPE_MOTOR_ROOM);

                String onLine = redisUtils.hmGet(deviceStatus, "online");
                if ("0".equals(onLine) || StringUtils.isEmpty(onLine)) {
                    return true;
                }
                break;
            }
            case SocketConstants.SENSOR_TYPE_MOTOR_ROOM -> {
                String deviceStatus = RedisKeyUtils.getDeviceStatus(elevatorCode, SocketConstants.SENSOR_TYPE_CAR_ROOF);
                String onLine = redisUtils.hmGet(deviceStatus, "online");
                if ("0".equals(onLine) || StringUtils.isEmpty(onLine)) {
                    return true;
                }
                break;
            }
            default -> {
                return false;
            }

        }
        return false;
    }


    /**
     * 更新Redis中电梯故障状态
     *
     * @param elevatorCode 电梯编号
     * @param i            故障状态 0：未故障，1：故障
     */
    public void updateFaultStatus(String elevatorCode, int i) {
        String statusKey = RedisKeyUtils.getElevatorStatus(elevatorCode);
        redisUtils.hmSet(statusKey, "fault_status", String.valueOf(i));
    }


    /**
     * 更新Redis中当前电梯的故障状态和故障编号
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @param stype        add OR disappear
     */
    public void updateFaultDetail(String elevatorCode, String sensorType, String faultType, String stype) {
        String faultKey = RedisKeyUtils.getElevatorFaultStatus(sensorType, elevatorCode);

        // Redis故障状态记录
        try {
            if (MessageConstants.STYPE_ADD.equals(stype)) {
                redisUtils.setAdd(faultKey, faultType);
            }
            if (MessageConstants.STYPE_DISAPPEAR.equals(stype)) {
                redisUtils.setRemove(faultKey, faultType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取Redis中电梯故障状态
     *
     * @param elevatorCode 电梯编号
     */
    public Set<String> getFaultStatusByCode(String elevatorCode) {
        String faultKey = RedisKeyUtils.getElevatorFaultStatus(SocketConstants.SENSOR_TYPE_CAR_ROOF, elevatorCode);
        return redisUtils.setMembers(faultKey);
    }

    /**
     * 获取Redis中电梯故障状态
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     */
    public Set<String> getFaultStatusByCode(String sensorType, String elevatorCode) {
        String faultKey = RedisKeyUtils.getElevatorFaultStatus(sensorType, elevatorCode);
        return redisUtils.setMembers(faultKey);
    }


    /**
     * 获取redis中记录电梯设备所在的ip
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 操作结果
     */
    public String getDeviceServerIp(String elevatorCode, String sensorType) {
        String deviceStatus = RedisKeyUtils.getDeviceStatus(elevatorCode, sensorType);
        return redisUtils.hmGet(deviceStatus, "server_ip");
    }


    public void putElevatorRegister(String registerNum, String elevatorCode, String serverIp) {
        String registerKey = RedisKeyUtils.getRegisterKey(registerNum);
        Map<String, String> map = ImmutableMap.<String, String>builder()
                .put("elevator_code", elevatorCode)
                .put("server_ip", serverIp)
                .build();
        redisUtils.hmSet(registerKey, map);
    }

    public String getDeviceSensorIp(String elevatorCode, String sensorType) {
        /** 2020年9月30日15:30:56 添加设备内网ip */
        String deviceStatus = RedisKeyUtils.getDeviceStatus(elevatorCode, sensorType);
        return redisUtils.hmGet(deviceStatus, "sensor_ip");
    }

    public String getXiongMaiHlsUrl(String elevatorCode) {
        String xiongMaiHlsKey = RedisKeyUtils.getXiongMaiHlsUrl(elevatorCode);
        return redisUtils.getStr(xiongMaiHlsKey);
    }

    public void setXiongMaiHlsUrl(String elevatorCode, String hlsUrl) {
        String xiongMaiHlsKey = RedisKeyUtils.getXiongMaiHlsUrl(elevatorCode);
        redisUtils.setStr(xiongMaiHlsKey, hlsUrl, 5500, TimeUnit.SECONDS);
    }

    public Set<String> getFaultStatusByKey(String key) {
        return redisUtils.setMembers(key);
    }

    public String getDeviceStatus(String deviceStatus, String online) {
        return redisUtils.hmGet(deviceStatus, online);
    }

    /**
     * 检查故障时候过滤
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @return 0:不屏蔽，1，北向屏蔽，2：平台屏蔽
     */
    public int checkFaultFilter(String elevatorCode, String faultType) {

        String southKey = "ELEVATOR:FAULT:SOUTH_COUNT:" + elevatorCode + ":" + faultType;
        String northKey = "ELEVATOR:FAULT:NORTH_COUNT:" + elevatorCode + ":" + faultType;

        //南向（平台）每小时故障数（梯+故障类型） > 3 ?屏蔽故障 ； 次小时故障数（梯+故障类型） < 3 ?恢复故障
        Integer southCount = redisUtils.getCacheObject(southKey);
        if (southCount == null) {
            redisUtils.setCacheObject(southKey, 1, 3600L, TimeUnit.SECONDS);
        } else {
            if (southCount % faultMaskingProperties.getPlatformFaultCountHour() == 2) {
                redisUtils.setCacheObject(southKey, southCount + 1, 3600L, TimeUnit.SECONDS);
            } else {
                long expire = redisUtils.getExpire(southKey, TimeUnit.SECONDS);
                redisUtils.setCacheObject(southKey, southCount + 1, expire, TimeUnit.SECONDS);
            }

            if (southCount >= faultMaskingProperties.getPlatformFaultCountHour()) {
                return 2;
            }

        }

        //北向（第三方）每天推送不超过5次
        Integer northCount = redisUtils.getCacheObject(northKey);
        if (northCount == null) {
            redisUtils.setCacheObject(northKey, 1, getDayRemainingTime(), TimeUnit.SECONDS);
        } else {
            redisUtils.setCacheObject(northKey, northCount + 1, getDayRemainingTime(), TimeUnit.SECONDS);

            if (northCount > faultMaskingProperties.getNorthFaultCountDay()) {
                return 1;
            }

        }

        return 0;
    }


    /**
     * 获取今日剩余秒数
     */
    public static long getDayRemainingTime() {
        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), midnight);
    }

    /**
     * 轿顶检修模式处理
     *
     * @param elevatorCode 电梯编号
     * @param repair       检修状态
     */
    public int carRoofModeStatusHandle(String elevatorCode, Integer repair) {

        String statusKey = RedisKeyUtils.getElevatorStatus(elevatorCode);
        //设置轿顶模式缓存
        repair = repair == null ? 0 : repair;
        redisUtils.hmSet(statusKey, "carRoof_mode_status", String.valueOf(repair));
        //机房模式缓存
        String motorRoomModeStatus = redisUtils.hmGet(statusKey, "motorRoom_mode_status");
        int modeStatus = repair;
        if (StrUtil.isNotEmpty(motorRoomModeStatus)) {
            modeStatus = Math.max(Integer.parseInt(motorRoomModeStatus), repair);
        }

        //设置检修模式缓存
        redisUtils.hmSet(statusKey, "mode_status", String.valueOf(modeStatus));

        return modeStatus;
    }
}
