package com.shmashine.socket.elevator.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.socket.elevator.dao.TblElevatorDao;
import com.shmashine.socket.elevator.entity.TblElevator;
import com.shmashine.socket.elevator.service.TblElevatorService;
import com.shmashine.socket.message.bean.ElevatorCache;
import com.shmashine.socket.redis.utils.RedisKeyUtils;
import com.shmashine.socket.redis.utils.RedisUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 电梯表(TblElevator)表服务实现类
 *
 * @author little.li
 * @since 2020-06-14 15:17:40
 */
@Slf4j
@Service("tblElevatorService")
public class TblElevatorServiceImpl implements TblElevatorService {


    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private TblElevatorDao tblElevatorDao;


    /**
     * 获取正确的楼层信息
     *
     * @param elevatorCode 电梯编号
     * @param deviceFloor  设备上传楼层
     * @return 真实楼层数据
     */
    @Override
    public String getRightFloor(String elevatorCode, String deviceFloor, JSONObject messageJson) {
        try {
            // 获取楼层
            TblElevator elevator = ElevatorCache.getByElevatorCode(elevatorCode);
            if (elevator == null) {
                return deviceFloor;
            }
            String floorDetail = elevator.getVFloorDetail();
            Integer minFloor = elevator.getIMinFloor();
            Integer maxFloor = elevator.getIMaxFloor();

            // 不存在特殊楼层，直接返回设备上报楼层
            if (StringUtils.isBlank(floorDetail)) {
                return deviceFloor;
            }

            // 匹配设备上报楼层与真实楼层
            String[] floorSplit = floorDetail.split(",");
            List<String> deviceFloorDetail = new ArrayList<>();
            for (int i = minFloor; i <= maxFloor; i++) {
                if (i == 0) {
                    continue;
                }
                deviceFloorDetail.add(String.valueOf(i));
            }
            int i = deviceFloorDetail.indexOf(deviceFloor);
            return floorSplit[i];
        } catch (Exception e) {
            log.error("{}，获取楼层信息失败，elevatorCode：{}，deviceFloor：{} ---> error:{}",
                    TimeUtils.nowTime(), elevatorCode, deviceFloor, e.getMessage());
        }
        return deviceFloor;
    }


    /**
     * 更新电梯服务模式
     *
     * @param elevatorCode 电梯编号
     * @param modeStatus   服务模式
     */
    @Override
    public void updateModeStatus(String elevatorCode, String modeStatus) {
        tblElevatorDao.updateModeStatus(elevatorCode, modeStatus);
    }


    /**
     * 更新在线离线状态
     *
     * @param elevatorCode 电梯编号
     * @param onLine       在线状态
     */
    @Override
    public void updateOnlineStatus(String elevatorCode, int onLine) {
        tblElevatorDao.updateOnlineStatus(elevatorCode, onLine);
    }

    @Override
    public List<TblElevator> list() {
        return tblElevatorDao.list();
    }

    @Override
    public void updateElevatorId(String elevatorCode, long nextId) {
        tblElevatorDao.updateElevatorId(elevatorCode, String.valueOf(nextId));
    }

    @Override
    public TblElevator getByElevatorCode(String elevatorCode) {
        return tblElevatorDao.getByElevatorCode(elevatorCode);
    }

    @Override
    public void updateFloorSettingStatus(String elevatorCode, String settingFloorStatus) {
        tblElevatorDao.updateFloorSettingStatus(elevatorCode, settingFloorStatus);
    }

    @Override
    public void updateFaultStatus(String elevatorCode, int status) {
        tblElevatorDao.updateFaultStatus(elevatorCode, status);
    }

    @Override
    public List<HashMap<String, Object>> getAllNettyDeviceStatus() {
        return tblElevatorDao.getAllNettyDeviceStatus();
    }

    @Override
    public void changeDeviceStatus(String elevatorCode, String sensorType, int status) {

        // 更新Redis中设备状态
        String statusKey = RedisKeyUtils.getDeviceStatus(elevatorCode, sensorType);
        Map<String, String> map = new HashMap<>();
        map.put("online", String.valueOf(status));
        redisUtils.hmSet(statusKey, map);

        // 更新设备离线状态
        tblElevatorDao.changeDeviceStatus(elevatorCode, sensorType, status);

    }

    @Override
    public void changeElevatorStatus(String elevatorCode, int status) {

        //更新Redis中电梯状态
        String elevatorStatus = RedisKeyUtils.getElevatorStatus(elevatorCode);
        redisUtils.hmSet(elevatorStatus, "online", String.valueOf(status));

        // 更新设备离线状态
        tblElevatorDao.changeElevatorStatus(elevatorCode, status);
    }

    @Override
    public boolean getDetectedPeopleNumsIsOpen(String elevatorCode) {
        Boolean detectedPeopleNumsIsOpen = tblElevatorDao.getDetectedPeopleNumsIsOpen(elevatorCode);
        if (detectedPeopleNumsIsOpen == null) {
            return false;
        }
        return detectedPeopleNumsIsOpen;
    }


}