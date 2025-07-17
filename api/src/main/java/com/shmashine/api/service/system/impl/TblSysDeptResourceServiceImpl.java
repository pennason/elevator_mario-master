package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblSysDeptResourceDao;
import com.shmashine.api.service.system.TblSysDeptResourceServiceI;
import com.shmashine.common.entity.TblSysDeptResource;

@Service
public class TblSysDeptResourceServiceImpl implements TblSysDeptResourceServiceI {

    @Resource(type = TblSysDeptResourceDao.class)
    private TblSysDeptResourceDao tblSysDeptResourceDao;

    @Override
    public TblSysDeptResourceDao getTblSysDeptResourceDao() {
        return tblSysDeptResourceDao;
    }

    public TblSysDeptResource getById(String vDeptId) {
        return tblSysDeptResourceDao.getById(vDeptId);
    }

    public List<TblSysDeptResource> getByEntity(TblSysDeptResource tblSysDeptResource) {
        return tblSysDeptResourceDao.getByEntity(tblSysDeptResource);
    }

    public List<TblSysDeptResource> listByEntity(TblSysDeptResource tblSysDeptResource) {
        return tblSysDeptResourceDao.listByEntity(tblSysDeptResource);
    }

    public List<TblSysDeptResource> listByIds(List<String> ids) {
        return tblSysDeptResourceDao.listByIds(ids);
    }

    public int insert(TblSysDeptResource tblSysDeptResource) {
        Date date = new Date();
        tblSysDeptResource.setDtCreatetime(date);
        tblSysDeptResource.setDtModifytime(date);
        return tblSysDeptResourceDao.insert(tblSysDeptResource);
    }

    public int insertBatch(List<TblSysDeptResource> list) {
        return tblSysDeptResourceDao.insertBatch(list);
    }

    public int update(TblSysDeptResource tblSysDeptResource) {
        tblSysDeptResource.setDtModifytime(new Date());
        return tblSysDeptResourceDao.update(tblSysDeptResource);
    }

    public int updateBatch(List<TblSysDeptResource> list) {
        return tblSysDeptResourceDao.updateBatch(list);
    }

    public int deleteById(String vDeptId) {
        return tblSysDeptResourceDao.deleteById(vDeptId);
    }

    public int deleteByEntity(TblSysDeptResource tblSysDeptResource) {
        return tblSysDeptResourceDao.deleteByEntity(tblSysDeptResource);
    }

    public int deleteByIds(List<String> list) {
        return tblSysDeptResourceDao.deleteByIds(list);
    }

    public int countAll() {
        return tblSysDeptResourceDao.countAll();
    }

    public int countByEntity(TblSysDeptResource tblSysDeptResource) {
        return tblSysDeptResourceDao.countByEntity(tblSysDeptResource);
    }

}