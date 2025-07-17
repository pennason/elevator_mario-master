package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblDeviceSensor;

public interface TblDeviceSensorDao {

    int deleteByPrimaryKey(String vDeviceSensorId);

    int insert(TblDeviceSensor record);

    int batchInsert(List<TblDeviceSensor> list);

    TblDeviceSensor selectByPrimaryKey(String vDeviceSensorId);

    int update(TblDeviceSensor record);

    List<TblDeviceSensor> selectByEntity(TblDeviceSensor record);

    List<Map> getBizList(TblDeviceSensor record);
}
