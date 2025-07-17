package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblSysSysteminfoDao;
import com.shmashine.api.service.system.TblSysSysteminfoServiceI;
import com.shmashine.common.entity.TblSysSysteminfo;

@Service
public class TblSysSysteminfoServiceImpl implements TblSysSysteminfoServiceI {

    @Resource(type = TblSysSysteminfoDao.class)
    private TblSysSysteminfoDao tblSysSysteminfoDao;

    @Override
    public TblSysSysteminfoDao getTblSysSysteminfoDao() {
        return tblSysSysteminfoDao;
    }

    public TblSysSysteminfo getById(Integer vSysid) {
        return tblSysSysteminfoDao.getById(vSysid);
    }

    public List<TblSysSysteminfo> getByEntity(TblSysSysteminfo tblSysSysteminfo) {
        return tblSysSysteminfoDao.getByEntity(tblSysSysteminfo);
    }

    public List<TblSysSysteminfo> listByEntity(TblSysSysteminfo tblSysSysteminfo) {
        return tblSysSysteminfoDao.listByEntity(tblSysSysteminfo);
    }

    public List<TblSysSysteminfo> listByIds(List<Integer> ids) {
        return tblSysSysteminfoDao.listByIds(ids);
    }

    public int insert(TblSysSysteminfo tblSysSysteminfo) {
        Date date = new Date();
        tblSysSysteminfo.setDtCreatetime(date);
        tblSysSysteminfo.setDtModifytime(date);
        return tblSysSysteminfoDao.insert(tblSysSysteminfo);
    }

    public int insertBatch(List<TblSysSysteminfo> list) {
        return tblSysSysteminfoDao.insertBatch(list);
    }

    public int update(TblSysSysteminfo tblSysSysteminfo) {
        tblSysSysteminfo.setDtModifytime(new Date());
        return tblSysSysteminfoDao.update(tblSysSysteminfo);
    }

    public int updateBatch(List<TblSysSysteminfo> list) {
        return tblSysSysteminfoDao.updateBatch(list);
    }

    public int deleteById(Integer vSysid) {
        return tblSysSysteminfoDao.deleteById(vSysid);
    }

    public int deleteByEntity(TblSysSysteminfo tblSysSysteminfo) {
        return tblSysSysteminfoDao.deleteByEntity(tblSysSysteminfo);
    }

    public int deleteByIds(List<Integer> list) {
        return tblSysSysteminfoDao.deleteByIds(list);
    }

    public int countAll() {
        return tblSysSysteminfoDao.countAll();
    }

    public int countByEntity(TblSysSysteminfo tblSysSysteminfo) {
        return tblSysSysteminfoDao.countByEntity(tblSysSysteminfo);
    }

}