package com.shmashine.socket.elevator.service;

import com.shmashine.socket.elevator.entity.TblRedirectElevatorMapping;

/**
 * 重定向电梯映射服务
 */
public interface TblRedirectElevatorMappingService {

    TblRedirectElevatorMapping getByElevatorCode(String elevatorCode);

    void clear();
}