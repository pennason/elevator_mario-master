package com.shmashine.api.service.monitor.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblMonitorDefinitionDao;
import com.shmashine.api.service.monitor.TblMonitorDefinitionServiceI;
import com.shmashine.common.entity.TblMonitorDefinition;

@Service
public class TblMonitorDefinitionServiceImpl implements TblMonitorDefinitionServiceI {

    @Resource(type = TblMonitorDefinitionDao.class)
    private TblMonitorDefinitionDao tblMonitorDefinitionDao;

    @Override
    public TblMonitorDefinitionDao getTblMonitorDefinitionDao() {
        return tblMonitorDefinitionDao;
    }

    @Override
    public TblMonitorDefinition getById(String vMonitorDefinitionId) {
        return tblMonitorDefinitionDao.getById(vMonitorDefinitionId);
    }

    @Override
    public List<TblMonitorDefinition> getByEntity(TblMonitorDefinition tblMonitorDefinition) {
        return tblMonitorDefinitionDao.getByEntity(tblMonitorDefinition);
    }

    @Override
    public List<TblMonitorDefinition> listByEntity(TblMonitorDefinition tblMonitorDefinition) {
        return tblMonitorDefinitionDao.listByEntity(tblMonitorDefinition);
    }

    @Override
    public List<TblMonitorDefinition> listByIds(List<String> ids) {
        return tblMonitorDefinitionDao.listByIds(ids);
    }

    @Override
    public int insert(TblMonitorDefinition tblMonitorDefinition) {
        Date date = new Date();
        tblMonitorDefinition.setDtCreateTime(date);
        tblMonitorDefinition.setDtModifyTime(date);
        return tblMonitorDefinitionDao.insert(tblMonitorDefinition);
    }

    @Override
    public int insertBatch(List<TblMonitorDefinition> list) {
        return tblMonitorDefinitionDao.insertBatch(list);
    }

    @Override
    public int update(TblMonitorDefinition tblMonitorDefinition) {
        tblMonitorDefinition.setDtModifyTime(new Date());
        return tblMonitorDefinitionDao.update(tblMonitorDefinition);
    }

    @Override
    public int updateBatch(List<TblMonitorDefinition> list) {
        return tblMonitorDefinitionDao.updateBatch(list);
    }

    @Override
    public int deleteById(String vMonitorDefinitionId) {
        return tblMonitorDefinitionDao.deleteById(vMonitorDefinitionId);
    }

    @Override
    public int deleteByEntity(TblMonitorDefinition tblMonitorDefinition) {
        return tblMonitorDefinitionDao.deleteByEntity(tblMonitorDefinition);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblMonitorDefinitionDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblMonitorDefinitionDao.countAll();
    }

    @Override
    public int countByEntity(TblMonitorDefinition tblMonitorDefinition) {
        return tblMonitorDefinitionDao.countByEntity(tblMonitorDefinition);
    }

}