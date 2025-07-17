package com.shmashine.api.service.fault.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblFaultDefinition0902Dao;
import com.shmashine.api.service.fault.TblFaultDefinition0902ServiceI;
import com.shmashine.common.entity.TblFaultDefinition0902;

@Service
public class TblFaultDefinition0902ServiceImpl implements TblFaultDefinition0902ServiceI {

    @Resource(type = TblFaultDefinition0902Dao.class)
    private TblFaultDefinition0902Dao tblFaultDefinition0902Dao;

    @Override
    public TblFaultDefinition0902Dao getTblFaultDefinition0902Dao() {
        return tblFaultDefinition0902Dao;
    }

    public TblFaultDefinition0902 getById(String faultDefinitionId) {
        return tblFaultDefinition0902Dao.getById(faultDefinitionId);
    }

    public List<TblFaultDefinition0902> getByEntity(TblFaultDefinition0902 tblFaultDefinition0902) {
        return tblFaultDefinition0902Dao.getByEntity(tblFaultDefinition0902);
    }

    public List<TblFaultDefinition0902> listByEntity(TblFaultDefinition0902 tblFaultDefinition0902) {
        return tblFaultDefinition0902Dao.listByEntity(tblFaultDefinition0902);
    }

    public List<TblFaultDefinition0902> listByIds(List<String> ids) {
        return tblFaultDefinition0902Dao.listByIds(ids);
    }

    public int insert(TblFaultDefinition0902 tblFaultDefinition0902) {
        Date date = new Date();
        return tblFaultDefinition0902Dao.insert(tblFaultDefinition0902);
    }

    public int insertBatch(List<TblFaultDefinition0902> list) {
        return tblFaultDefinition0902Dao.insertBatch(list);
    }

    public int update(TblFaultDefinition0902 tblFaultDefinition0902) {
        return tblFaultDefinition0902Dao.update(tblFaultDefinition0902);
    }

    public int updateBatch(List<TblFaultDefinition0902> list) {
        return tblFaultDefinition0902Dao.updateBatch(list);
    }

    public int deleteById(String faultDefinitionId) {
        return tblFaultDefinition0902Dao.deleteById(faultDefinitionId);
    }

    public int deleteByEntity(TblFaultDefinition0902 tblFaultDefinition0902) {
        return tblFaultDefinition0902Dao.deleteByEntity(tblFaultDefinition0902);
    }

    public int deleteByIds(List<String> list) {
        return tblFaultDefinition0902Dao.deleteByIds(list);
    }

    public int countAll() {
        return tblFaultDefinition0902Dao.countAll();
    }

    public int countByEntity(TblFaultDefinition0902 tblFaultDefinition0902) {
        return tblFaultDefinition0902Dao.countByEntity(tblFaultDefinition0902);
    }

}