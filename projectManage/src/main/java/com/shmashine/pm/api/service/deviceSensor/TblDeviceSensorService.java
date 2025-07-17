package com.shmashine.pm.api.service.deviceSensor;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblDeviceSensor;

public interface TblDeviceSensorService {

    int deleteByPrimaryKey(String vDeviceSensorId);

    int insert(TblDeviceSensor record);

    int batchInsert(List<TblDeviceSensor> list);

    TblDeviceSensor selectByPrimaryKey(String vDeviceSensorId);

    int update(TblDeviceSensor record);

    List<TblDeviceSensor> selectByEntity(TblDeviceSensor record);

    List<Map> getBizList(TblDeviceSensor record);
}
