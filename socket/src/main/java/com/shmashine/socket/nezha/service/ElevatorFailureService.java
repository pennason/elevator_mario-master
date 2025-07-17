package com.shmashine.socket.nezha.service;

import java.util.List;

/**
 * 故障列表
 *
 * @author little.li
 */
public interface ElevatorFailureService {


    int manualRepairConfirm(String sensor_code, int fault_type, int manul_clean);


    List<String> getByCodeFailure(String elevatorCode);


}
