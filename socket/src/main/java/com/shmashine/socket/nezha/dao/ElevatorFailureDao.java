package com.shmashine.socket.nezha.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * ElevatorFailureDao
 *
 * @author chglee
 * @version 2018-02-04 22:18:01
 */
@Mapper
public interface ElevatorFailureDao {


    int manualRepairConfirm(String sensor_code, int fault_type, int manul_clean);


    List<String> getByCodeFailure(String elevatorCode);


}
