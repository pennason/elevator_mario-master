package com.shmashine.pm.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.TblSensorConfig;

public interface TblSensorConfigDao {

    List<TblSensorConfig> selectBySensorType(@Param("sensorType") String sensorType);
}
