package com.shmashine.api.service.elevator;

import java.util.List;
import java.util.Map;

import com.shmashine.api.dao.TblElevatorSensorDao;
import com.shmashine.common.entity.TblElevatorSensor;


public interface TblElevatorSensorServiceI {

    TblElevatorSensorDao getTblElevatorSensorDao();

    TblElevatorSensor getById(String vElevatorSensorId);

    List<TblElevatorSensor> getByEntity(TblElevatorSensor tblElevatorSensor);

    List<TblElevatorSensor> listByEntity(TblElevatorSensor tblElevatorSensor);

    List<TblElevatorSensor> listByIds(List<String> ids);

    int insert(TblElevatorSensor tblElevatorSensor);

    int insertBatch(List<TblElevatorSensor> list);

    int update(TblElevatorSensor tblElevatorSensor);

    int updateBatch(List<TblElevatorSensor> list);

    int deleteById(String vElevatorSensorId);

    int deleteByEntity(TblElevatorSensor tblElevatorSensor);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblElevatorSensor tblElevatorSensor);

    /**
     * 根据电梯编号获取传感器列表
     *
     * @param elevatorCode 电梯编号
     */
    List<Map<String, Object>> getSensorListByElevatorCode(String elevatorCode);

    /**
     * 清除电梯对应的传感器列表
     *
     * @param elevatorCode 电梯编号
     */
    void batchRemoveByElevatorCode(String elevatorCode);

    /**
     * 新增电梯对应的传感器列表
     *
     * @param elevatorCode 电梯编号
     * @param arr          传感器列表
     */
    void batchSave(String elevatorCode, List<String> arr);

    /**
     * 根据电梯编号获取页面展示屏蔽表
     *
     * @param elevatorCode 电梯编号
     */
    Map<String, Map<String, Object>> getPageDisplayByElevatorCode(String elevatorCode);

    void insertElevatorSensor(String elevatorCode, List<String> arr);
}