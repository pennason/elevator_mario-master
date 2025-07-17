package com.shmashine.fault.redis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shmashine.common.constants.SocketConstants;
import com.shmashine.fault.elevator.service.TblElevatorService;
import com.shmashine.fault.redis.util.RedisKeyUtils;
import com.shmashine.fault.redis.util.RedisUtils;

/**
 * redis服务
 *
 * @author little.li
 */
@Component
public class RedisService {


    private final RedisUtils redisUtils;

    private final TblElevatorService elevatorService;

    @Autowired
    public RedisService(RedisUtils redisUtils, TblElevatorService elevatorService) {
        this.redisUtils = redisUtils;
        this.elevatorService = elevatorService;
    }


    /**
     * redis中记录设备上线状态
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @param serverIp     服务器ip
     * @param nowTime      格式化时间
     */
    public void updateOnLineStatus(String elevatorCode, String sensorType, String serverIp, String nowTime) {
        // 更新设备上线状态
        Map<String, String> map = new HashMap<>();
        map.put("online", "1");
        map.put("online_time", nowTime);
        map.put("server_ip", serverIp);

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
        // 根据不同设备类型，判断是否将电梯设置为离线
        boolean isUpdateElevatorOffLine = isUpdateElevatorOffLine(elevatorCode, sensorType);
        if (isUpdateElevatorOffLine) {
            // 将电梯设置为离线
            String elevatorStatus = RedisKeyUtils.getElevatorStatus(elevatorCode);
            redisUtils.hmSet(elevatorStatus, "online", "0");
        }
        return isUpdateElevatorOffLine;
    }


    /**
     * 获取Redis中电梯的服务模式
     *
     * @param elevatorCode     电梯编号
     * @param deviceModeStatus 设备上报的服务模式，服务模式 0 正常运行，1 检修服务，2 停止服务（检修由机房上传，停止由轿顶上传）
     * @return 服务模式 0 正常运行，1 检修服务，2 停止服务 (优先级越高，数字越大)
     */
    public Integer getAndUpdateModeStatus(String elevatorCode, Integer deviceModeStatus) {
        try {
            String statusKey = RedisKeyUtils.getElevatorStatus(elevatorCode);
            String status = redisUtils.hmGet(statusKey, "mode_status");
            if (status == null) {
                // redis中不存在服务模式，则将设备上报的服务模式存储
                redisUtils.hmSet(statusKey, "mode_status", String.valueOf(deviceModeStatus));
                elevatorService.updateModeStatus(elevatorCode, deviceModeStatus);
                return deviceModeStatus;
            }

            // 比较设备上报与Redis中的服务模式 更新为优先级高的服务模式 TODO 服务模式规范
            int modeStatus = Integer.parseInt(status);
            if (deviceModeStatus != modeStatus && deviceModeStatus > modeStatus) {
                redisUtils.hmSet(statusKey, "mode_status", String.valueOf(deviceModeStatus));
                elevatorService.updateModeStatus(elevatorCode, deviceModeStatus);
                return deviceModeStatus;
            }
            return modeStatus;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
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
            case SocketConstants.SENSOR_TYPE_CUBE -> {
                return true;
            }
            case SocketConstants.SENSOR_TYPE_CAR_ROOF -> {
                String deviceStatus = RedisKeyUtils.getDeviceStatus(elevatorCode,
                        SocketConstants.SENSOR_TYPE_MOTOR_ROOM);
                String onLine = redisUtils.hmGet(deviceStatus, "online");
                return "0".equals(onLine);
            }

            case SocketConstants.SENSOR_TYPE_MOTOR_ROOM -> {
                String deviceStatus = RedisKeyUtils.getDeviceStatus(elevatorCode, SocketConstants.SENSOR_TYPE_CAR_ROOF);
                String onLine = redisUtils.hmGet(deviceStatus, "online");
                return "0".equals(onLine);
            }
            default -> {
                return false;
            }
        }
    }


    /**
     * 更新Redis中电梯故障状态
     *
     * @param elevatorCode 电梯编号
     * @param i            故障状态 0：未故障，1：故障
     */
    public void updateFaultStatus(String elevatorCode, int i) {
        try {
            String statusKey = RedisKeyUtils.getElevatorStatus(elevatorCode);
            redisUtils.hmSet(statusKey, "fault_status", String.valueOf(i));
        } catch (Exception e) {
            //
        }
    }


}
