package com.shmashine.api.service.elevator.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblSensorDao;
import com.shmashine.api.service.elevator.TblSensorServiceI;
import com.shmashine.common.entity.TblSensor;

/**
 * sensor 相关服务
 *
 * @author chenx
 */

@Service
public class TblSensorServiceImpl implements TblSensorServiceI {

    @Resource(type = TblSensorDao.class)
    private TblSensorDao tblSensorDao;

    @Override
    public TblSensorDao getTblSensorDao() {
        return tblSensorDao;
    }

    @Override
    public TblSensor getById(String vSensorId) {
        return tblSensorDao.getById(vSensorId);
    }

    @Override
    public List<TblSensor> getByEntity(TblSensor tblSensor) {
        return tblSensorDao.getByEntity(tblSensor);
    }

    @Override
    public List<TblSensor> listByEntity(TblSensor tblSensor) {
        return tblSensorDao.listByEntity(tblSensor);
    }

    @Override
    public List<TblSensor> listByIds(List<String> ids) {
        return tblSensorDao.listByIds(ids);
    }

    @Override
    public int insert(TblSensor tblSensor) {
        Date date = new Date();
        tblSensor.setDtCreateTime(date);
        tblSensor.setDtModifyTime(date);
        return tblSensorDao.insert(tblSensor);
    }

    @Override
    public int insertBatch(List<TblSensor> list) {
        return tblSensorDao.insertBatch(list);
    }

    @Override
    public int update(TblSensor tblSensor) {
        tblSensor.setDtModifyTime(new Date());
        return tblSensorDao.update(tblSensor);
    }

    @Override
    public int deleteById(String vSensorId) {
        return tblSensorDao.deleteById(vSensorId);
    }

    @Override
    public int deleteByEntity(TblSensor tblSensor) {
        return tblSensorDao.deleteByEntity(tblSensor);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblSensorDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblSensorDao.countAll();
    }

    @Override
    public int countByEntity(TblSensor tblSensor) {
        return tblSensorDao.countByEntity(tblSensor);
    }

}