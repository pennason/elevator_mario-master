package com.shmashine.socket.nezha.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.socket.nezha.dao.ElevatorFailureDao;
import com.shmashine.socket.nezha.service.ElevatorFailureService;

import lombok.extern.slf4j.Slf4j;

/**
 * 故障列表
 *
 * @author little.li
 */
@Slf4j
@Service
public class ElevatorFailureServiceImpl implements ElevatorFailureService {


    private final ElevatorFailureDao elevatorFailureDao;

    @Autowired
    public ElevatorFailureServiceImpl(ElevatorFailureDao elevatorFailureDao) {
        this.elevatorFailureDao = elevatorFailureDao;
    }


    /**
     * 手动消除故障反馈数据库
     *
     * @param sensor_code 电梯code
     * @param fault_type  故障类型
     * @param manul_clean 设备反馈状态
     */
    @Override
    public int manualRepairConfirm(String sensor_code, int fault_type, int manul_clean) {
        return elevatorFailureDao.manualRepairConfirm(sensor_code, fault_type, manul_clean);
    }

    @Override
    public List<String> getByCodeFailure(String elevatorCode) {
        return elevatorFailureDao.getByCodeFailure(elevatorCode);
    }


}
