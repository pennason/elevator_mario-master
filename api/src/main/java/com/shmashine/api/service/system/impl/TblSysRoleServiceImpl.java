package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shmashine.api.dao.TblSysRoleDao;
import com.shmashine.api.service.system.TblSysRoleServiceI;
import com.shmashine.common.entity.TblSysRole;

@Service
public class TblSysRoleServiceImpl implements TblSysRoleServiceI {

    @Resource(type = TblSysRoleDao.class)
    private TblSysRoleDao tblSysRoleDao;

    @Override
    public TblSysRoleDao getTblSysRoleDao() {
        return tblSysRoleDao;
    }

    public TblSysRole getById(String vRoleId) {
        return tblSysRoleDao.getById(vRoleId);
    }

    public List<TblSysRole> getByEntity(TblSysRole tblSysRole) {
        return tblSysRoleDao.getByEntity(tblSysRole);
    }

    public List<TblSysRole> listByEntity(TblSysRole tblSysRole) {
        return tblSysRoleDao.listByEntity(tblSysRole);
    }

    public List<TblSysRole> listByIds(List<String> ids) {
        return tblSysRoleDao.listByIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insert(TblSysRole tblSysRole) {
        Date date = new Date();
        tblSysRole.setDtCreatetime(date);
        tblSysRole.setDtModifytime(date);
        return tblSysRoleDao.insert(tblSysRole);
    }

    public int insertBatch(List<TblSysRole> list) {
        return tblSysRoleDao.insertBatch(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public int update(TblSysRole tblSysRole) {
        tblSysRole.setDtModifytime(new Date());
        return tblSysRoleDao.update(tblSysRole);
    }

    public int updateBatch(List<TblSysRole> list) {
        return tblSysRoleDao.updateBatch(list);
    }

    public int deleteById(String vRoleId) {
        return tblSysRoleDao.deleteById(vRoleId);
    }

    public int deleteByEntity(TblSysRole tblSysRole) {
        return tblSysRoleDao.deleteByEntity(tblSysRole);
    }

    public int deleteByIds(List<String> list) {
        return tblSysRoleDao.deleteByIds(list);
    }

    public int countAll() {
        return tblSysRoleDao.countAll();
    }

    public int countByEntity(TblSysRole tblSysRole) {
        return tblSysRoleDao.countByEntity(tblSysRole);
    }

}