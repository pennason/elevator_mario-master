package com.shmashine.pm.api.service.deviceSensor.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.shmashine.pm.api.dao.TblDeviceSensorDao;
import com.shmashine.pm.api.entity.TblDeviceSensor;
import com.shmashine.pm.api.service.deviceSensor.TblDeviceSensorService;

@Service
public class TblDeviceSensorServiceImpl implements TblDeviceSensorService {

    @Resource
    private TblDeviceSensorDao tblDeviceSensorDao;

    @Override
    public int deleteByPrimaryKey(String vDeviceSensorId) {
        return tblDeviceSensorDao.deleteByPrimaryKey(vDeviceSensorId);
    }

    @Override
    public int insert(TblDeviceSensor record) {

        return tblDeviceSensorDao.insert(record);
    }

    @Override
    public int batchInsert(List<TblDeviceSensor> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return tblDeviceSensorDao.batchInsert(list);
    }

    @Override
    public TblDeviceSensor selectByPrimaryKey(String vDeviceSensorId) {
        return tblDeviceSensorDao.selectByPrimaryKey(vDeviceSensorId);
    }

    @Override
    public int update(TblDeviceSensor record) {
        return tblDeviceSensorDao.update(record);
    }

    @Override
    public List<TblDeviceSensor> selectByEntity(TblDeviceSensor record) {
        return tblDeviceSensorDao.selectByEntity(record);
    }

    @Override
    public List<Map> getBizList(TblDeviceSensor record) {
        return tblDeviceSensorDao.getBizList(record);
    }

}
