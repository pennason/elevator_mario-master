package com.shmashine.api.service.monitor.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblElevatorShieldDao;
import com.shmashine.api.service.monitor.TblElevatorShieldServiceI;
import com.shmashine.common.entity.TblElevatorShield;

@Service
public class TblElevatorShieldServiceImpl implements TblElevatorShieldServiceI {

    @Resource(type = TblElevatorShieldDao.class)
    private TblElevatorShieldDao tblElevatorShieldDao;

    @Override
    public TblElevatorShieldDao getTblElevatorShieldDao() {
        return tblElevatorShieldDao;
    }

    @Override
    public TblElevatorShield getById(String vElevatorShieldId) {
        return tblElevatorShieldDao.getById(vElevatorShieldId);
    }

    @Override
    public List<TblElevatorShield> getByEntity(TblElevatorShield tblElevatorShield) {
        return tblElevatorShieldDao.getByEntity(tblElevatorShield);
    }

    @Override
    public List<TblElevatorShield> listByEntity(TblElevatorShield tblElevatorShield) {
        return tblElevatorShieldDao.listByEntity(tblElevatorShield);
    }

    @Override
    public List<TblElevatorShield> listByIds(List<String> ids) {
        return tblElevatorShieldDao.listByIds(ids);
    }

    @Override
    public int insert(TblElevatorShield tblElevatorShield) {
        Date date = new Date();
        tblElevatorShield.setDtCreateTime(date);
        tblElevatorShield.setDtModifyTime(date);
        return tblElevatorShieldDao.insert(tblElevatorShield);
    }

    @Override
    public int insertBatch(List<TblElevatorShield> list) {
        return tblElevatorShieldDao.insertBatch(list);
    }

    @Override
    public int update(TblElevatorShield tblElevatorShield) {
        tblElevatorShield.setDtModifyTime(new Date());
        return tblElevatorShieldDao.update(tblElevatorShield);
    }

    @Override
    public int updateBatch(List<TblElevatorShield> list) {
        return tblElevatorShieldDao.updateBatch(list);
    }

    @Override
    public int deleteById(String vElevatorShieldId) {
        return tblElevatorShieldDao.deleteById(vElevatorShieldId);
    }

    @Override
    public int deleteByEntity(TblElevatorShield tblElevatorShield) {
        return tblElevatorShieldDao.deleteByEntity(tblElevatorShield);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblElevatorShieldDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblElevatorShieldDao.countAll();
    }

    @Override
    public int countByEntity(TblElevatorShield tblElevatorShield) {
        return tblElevatorShieldDao.countByEntity(tblElevatorShield);
    }

}