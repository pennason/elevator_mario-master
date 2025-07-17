package com.shmashine.api.service.elevatorconfig.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblElevatorConfigDao;
import com.shmashine.api.service.elevatorconfig.TblElevatorConfigServiceI;
import com.shmashine.common.entity.TblElevatorConfig;
import com.shmashine.common.utils.SnowFlakeUtils;


/**
 * @author little.li
 */
@Service
public class TblElevatorConfigServiceImpl implements TblElevatorConfigServiceI {

    @Resource(type = TblElevatorConfigDao.class)
    private TblElevatorConfigDao tblElevatorConfigDao;

    @Override
    public TblElevatorConfigDao getTblElevatorConfigDao() {
        return tblElevatorConfigDao;
    }

    @Override
    public TblElevatorConfig getById(String elevatorConfigId) {
        return tblElevatorConfigDao.getById(elevatorConfigId);
    }

    @Override
    public List<TblElevatorConfig> getByEntity(TblElevatorConfig tblElevatorConfig) {
        return tblElevatorConfigDao.getByEntity(tblElevatorConfig);
    }

    @Override
    public List<TblElevatorConfig> listByEntity(TblElevatorConfig tblElevatorConfig) {
        return tblElevatorConfigDao.listByEntity(tblElevatorConfig);
    }

    @Override
    public List<TblElevatorConfig> listByIds(List<String> ids) {
        return tblElevatorConfigDao.listByIds(ids);
    }

    @Override
    public int insert(TblElevatorConfig tblElevatorConfig) {
        Date date = new Date();
        tblElevatorConfig.setCreateTime(date);
        tblElevatorConfig.setModifyTime(date);
        return tblElevatorConfigDao.insert(tblElevatorConfig);
    }

    @Override
    public int insertBatch(List<TblElevatorConfig> list) {
        return tblElevatorConfigDao.insertBatch(list);
    }

    @Override
    public int update(TblElevatorConfig tblElevatorConfig) {
        tblElevatorConfig.setModifyTime(new Date());
        return tblElevatorConfigDao.update(tblElevatorConfig);
    }

    @Override
    public int updateBatch(List<TblElevatorConfig> list) {
        return tblElevatorConfigDao.updateBatch(list);
    }

    @Override
    public int deleteById(String elevatorConfigId) {
        return tblElevatorConfigDao.deleteById(elevatorConfigId);
    }

    @Override
    public int deleteByEntity(TblElevatorConfig tblElevatorConfig) {
        return tblElevatorConfigDao.deleteByEntity(tblElevatorConfig);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblElevatorConfigDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblElevatorConfigDao.countAll();
    }

    @Override
    public int countByEntity(TblElevatorConfig tblElevatorConfig) {
        return tblElevatorConfigDao.countByEntity(tblElevatorConfig);
    }

    @Override
    public TblElevatorConfig getByElevatorId(String elevatorId) {
        return tblElevatorConfigDao.getByElevatorId(elevatorId);
    }

    @Override
    public void updateConfig(String elevatorId, String elevatorCode, TblElevatorConfig elevatorConfig) {
        TblElevatorConfig elevatorConfigInDB = tblElevatorConfigDao.getByElevatorId(elevatorId);
        if (elevatorConfigInDB == null) {
            elevatorConfig.setElevatorConfigId(SnowFlakeUtils.nextStrId());
            elevatorConfig.setElevatorId(elevatorId);
            elevatorConfig.setElevatorCode(elevatorCode);
            tblElevatorConfigDao.insert(elevatorConfig);
        } else {
            tblElevatorConfigDao.update(elevatorConfig);
        }
    }

}