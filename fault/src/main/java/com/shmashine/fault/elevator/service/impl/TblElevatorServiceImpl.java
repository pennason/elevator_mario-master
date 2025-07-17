package com.shmashine.fault.elevator.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.fault.elevator.dao.TblElevatorDao;
import com.shmashine.fault.elevator.entity.TblElevator;
import com.shmashine.fault.elevator.service.TblElevatorService;
import com.shmashine.fault.user.entity.TblSysUser;

/**
 * 电梯表(TblElevator)表服务实现类
 *
 * @author little.li
 * @since 2020-06-14 15:17:40
 */
@Service("tblElevatorService")
public class TblElevatorServiceImpl implements TblElevatorService {


    @Resource
    private TblElevatorDao tblElevatorDao;


    /**
     * 获取正确的楼层信息
     *
     * @param elevatorCode 电梯编号
     * @param deviceFloor  设备楼层
     */
    @Override
    public String getRightFloor(String elevatorCode, String deviceFloor) {
        try {
            TblElevator elevator = tblElevatorDao.getByElevatorCode(elevatorCode);
            String floorDetail = elevator.getVFloorDetail();
            Integer minFloor = elevator.getIMinFloor();
            Integer maxFloor = elevator.getIMaxFloor();

            // 不存在特殊楼层，直接返回设备上报楼层
            if (StringUtils.isEmpty(floorDetail)) {
                return deviceFloor;
            }

            // 匹配设备上报楼层与真实楼层
            String[] floorSplit = floorDetail.split(",");
            List<String> deviceFloorDetail = new ArrayList<>();
            for (int i = minFloor; i <= maxFloor; i++) {
                deviceFloorDetail.add(String.valueOf(i));
            }
            int i = deviceFloorDetail.indexOf(deviceFloor);
            return floorSplit[i];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceFloor;
    }


    /**
     * 更新电梯服务模式
     *
     * @param elevatorCode 电梯编号
     * @param modeStatus   模式
     */
    @Override
    public void updateModeStatus(String elevatorCode, Integer modeStatus) {
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
    public void updateFloorSettingStatus(Map<String, Object> map) {
        tblElevatorDao.updateFloorSettingStatus(map);
    }


    /**
     * 根据电梯编号，修改统计数据
     *
     * @param messageJson  统计报文
     * @param elevatorCode 电梯编号
     */
    @Override
    public void updateStatisticalInformationByElevatorCode(JSONObject messageJson, String elevatorCode) {
        tblElevatorDao.updateStatisticalInformationByElevatorCode(messageJson, elevatorCode);
    }


    @Override
    public void updateFaultStatus(String elevatorCode, int status) {
        tblElevatorDao.updateFaultStatus(elevatorCode, status);
    }

    @Override
    public void updateStatisticalInformationByElevatorCode2(JSONObject messageJson, String elevatorCode) {
        tblElevatorDao.updateStatisticalInformationByElevatorCode2(messageJson, elevatorCode);
    }

    @Override
    public void updateStatisticalInformationByElevatorCode3(JSONObject messageJson, String elevatorCode) {
        tblElevatorDao.updateStatisticalInformationByElevatorCode3(messageJson, elevatorCode);
    }

    @Override
    public List<String> getFaultElevator(TblSysUser user) {
        return tblElevatorDao.getFaultElevator(user.getVUserId());
    }

    @Override
    public String getClient(String vElevatorId) {
        return tblElevatorDao.getClient(vElevatorId);
    }

    @Override
    public void updateInstallStatus(String elevatorCode, int status) {
        tblElevatorDao.updateInstallStatus(elevatorCode, status);
    }

    @Override
    public void updateDeviceConfStatusByCode(String elevatorCode, Integer deviceConfStatus) {
        tblElevatorDao.updateDeviceConfStatusByCode(elevatorCode, deviceConfStatus);
    }


}