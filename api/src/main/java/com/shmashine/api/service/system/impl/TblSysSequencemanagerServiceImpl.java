package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblSysSequencemanagerDao;
import com.shmashine.api.service.system.TblSysSequencemanagerServiceI;
import com.shmashine.common.entity.TblSysSequencemanager;

@Service
public class TblSysSequencemanagerServiceImpl implements TblSysSequencemanagerServiceI {

    @Resource(type = TblSysSequencemanagerDao.class)
    private TblSysSequencemanagerDao tblSysSequencemanagerDao;

    @Override
    public TblSysSequencemanagerDao getTblSysSequencemanagerDao() {
        return tblSysSequencemanagerDao;
    }

    public TblSysSequencemanager getById(String vSeqId) {
        return tblSysSequencemanagerDao.getById(vSeqId);
    }

    public List<TblSysSequencemanager> getByEntity(TblSysSequencemanager tblSysSequencemanager) {
        return tblSysSequencemanagerDao.getByEntity(tblSysSequencemanager);
    }

    public List<TblSysSequencemanager> listByEntity(TblSysSequencemanager tblSysSequencemanager) {
        return tblSysSequencemanagerDao.listByEntity(tblSysSequencemanager);
    }

    public List<TblSysSequencemanager> listByIds(List<String> ids) {
        return tblSysSequencemanagerDao.listByIds(ids);
    }

    public int insert(TblSysSequencemanager tblSysSequencemanager) {
        Date date = new Date();
        tblSysSequencemanager.setDtCreatetime(date);
        tblSysSequencemanager.setDtModifytime(date);
        return tblSysSequencemanagerDao.insert(tblSysSequencemanager);
    }

    public int insertBatch(List<TblSysSequencemanager> list) {
        return tblSysSequencemanagerDao.insertBatch(list);
    }

    public int update(TblSysSequencemanager tblSysSequencemanager) {
        tblSysSequencemanager.setDtModifytime(new Date());
        return tblSysSequencemanagerDao.update(tblSysSequencemanager);
    }

    public int updateBatch(List<TblSysSequencemanager> list) {
        return tblSysSequencemanagerDao.updateBatch(list);
    }

    public int deleteById(String vSeqId) {
        return tblSysSequencemanagerDao.deleteById(vSeqId);
    }

    public int deleteByEntity(TblSysSequencemanager tblSysSequencemanager) {
        return tblSysSequencemanagerDao.deleteByEntity(tblSysSequencemanager);
    }

    public int deleteByIds(List<String> list) {
        return tblSysSequencemanagerDao.deleteByIds(list);
    }

    public int countAll() {
        return tblSysSequencemanagerDao.countAll();
    }

    public int countByEntity(TblSysSequencemanager tblSysSequencemanager) {
        return tblSysSequencemanagerDao.countByEntity(tblSysSequencemanager);
    }

}