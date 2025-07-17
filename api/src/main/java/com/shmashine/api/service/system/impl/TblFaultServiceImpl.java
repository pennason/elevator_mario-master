package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblFaultDao;
import com.shmashine.api.service.system.TblFaultServiceI;
import com.shmashine.common.entity.TblFault;

@Service
public class TblFaultServiceImpl implements TblFaultServiceI {

    @Resource(type = TblFaultDao.class)
    private TblFaultDao tblFaultDao;

    @Override
    public TblFaultDao getTblFaultDao() {
        return tblFaultDao;
    }

    public TblFault getById(String vFaultId) {
        return tblFaultDao.getById(vFaultId);
    }

    public List<TblFault> getByEntity(TblFault tblFault) {
        return tblFaultDao.getByEntity(tblFault);
    }

    public List<TblFault> listByEntity(TblFault tblFault) {
        return tblFaultDao.listByEntity(tblFault);
    }

    public List<TblFault> listByIds(List<String> ids) {
        return tblFaultDao.listByIds(ids);
    }

    public int insert(TblFault tblFault) {
        Date date = new Date();
        tblFault.setDtCreateTime(date);
        tblFault.setDtModifyTime(date);
        return tblFaultDao.insert(tblFault);
    }

    public int insertBatch(List<TblFault> list) {
        return tblFaultDao.insertBatch(list);
    }

    public int update(TblFault tblFault) {
        tblFault.setDtModifyTime(new Date());
        return tblFaultDao.update(tblFault);
    }

    public int updateBatch(List<TblFault> list) {
        return tblFaultDao.updateBatch(list);
    }

    public int deleteById(String vFaultId) {
        return tblFaultDao.deleteById(vFaultId);
    }

    public int deleteByEntity(TblFault tblFault) {
        return tblFaultDao.deleteByEntity(tblFault);
    }

    public int deleteByIds(List<String> list) {
        return tblFaultDao.deleteByIds(list);
    }

    public int countAll() {
        return tblFaultDao.countAll();
    }

    public int countByEntity(TblFault tblFault) {
        return tblFaultDao.countByEntity(tblFault);
    }

}