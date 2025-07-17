package com.shmashine.socket.nezha.service.impl;


import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.socket.nezha.dao.ElevatorNewDao;
import com.shmashine.socket.nezha.domain.ElevatorEventRecordDO;
import com.shmashine.socket.nezha.service.ElevatorEventRecordService;
import com.shmashine.socket.nezha.service.ElevatorNewService;

/**
 * ElevatorNewServiceImpl
 */
@Service
public class ElevatorNewServiceImpl implements ElevatorNewService {


    @Resource
    private ElevatorNewDao elevatorNewDao;

    @Resource
    private ElevatorEventRecordService elevatorEventRecordService;


    @Override
    public int updateDevice(Map<String, Object> map) {
        return elevatorNewDao.updateDevice(map);
    }

    @Override
    public int updateVersion(Map<String, Object> map) {
        return elevatorNewDao.updateVersion(map);
    }


    /**
     * 更新设备上线状态
     */
    @Override
    public int updateOnline(String code, String sensorType) {

        ElevatorEventRecordDO elevatorEventRecord = new ElevatorEventRecordDO();
        elevatorEventRecord.setElevatorCode(code);
        elevatorEventRecord.setSensorType(sensorType);

        // 获取设备上次的登录状态
        ElevatorEventRecordDO elevatorEventRecordDOLatest = elevatorEventRecordService.getLatest(code);
        if (elevatorEventRecordDOLatest == null
                || elevatorEventRecordDOLatest.getTypeId() == null
                || elevatorEventRecordDOLatest.getTypeId() == 2) {
            // 上次为登出状态，添加上线记录
            elevatorEventRecord.setTypeId(1);
        } else {
            // 上次的为上线状态，添加重建记录
            elevatorEventRecord.setTypeId(3);
        }
        elevatorEventRecordService.save(elevatorEventRecord);

        return elevatorNewDao.updateOnline(code);
    }

    @Override
    public int updateOffline(String code, String sensorType) {
        if (null != sensorType) {
            ElevatorEventRecordDO elevatorEventRecord = new ElevatorEventRecordDO();
            elevatorEventRecord.setElevatorCode(code);
            elevatorEventRecord.setTypeId(2);
            elevatorEventRecord.setSensorType(sensorType);
            elevatorEventRecordService.save(elevatorEventRecord);
        }
        return elevatorNewDao.updateOffline(code);
    }


}
