package com.shmashine.pm.api.service.sensorConfig;

import java.util.List;

import com.shmashine.pm.api.entity.TblSensorConfig;

public interface TblSensorConfigService {

    /**
     * 查找类型
     *
     * @param sensorType
     * @return
     */
    List<TblSensorConfig> selectBySensorType(String sensorType);
}
