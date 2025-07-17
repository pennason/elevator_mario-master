package com.shmashine.pm.api.service.sensorConfig.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblSensorConfigDao;
import com.shmashine.pm.api.entity.TblSensorConfig;
import com.shmashine.pm.api.service.sensorConfig.TblSensorConfigService;

@Service
public class TblSensorConfigServiceImpl implements TblSensorConfigService {

    @Autowired
    private TblSensorConfigDao tblSensorConfigDao;

    @Override
    public List<TblSensorConfig> selectBySensorType(String sensorType) {
        return tblSensorConfigDao.selectBySensorType(sensorType);
    }
}
