package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblSysUserRoleDao;
import com.shmashine.api.service.system.TblSysUserRoleServiceI;
import com.shmashine.common.entity.TblSysUserRole;

@Service
public class TblSysUserRoleServiceImpl implements TblSysUserRoleServiceI {

    @Resource(type = TblSysUserRoleDao.class)
    private TblSysUserRoleDao tblSysUserRoleDao;

    @Override
    public TblSysUserRoleDao getTblSysUserRoleDao() {
        return tblSysUserRoleDao;
    }

    public TblSysUserRole getById(String vUserId) {
        return tblSysUserRoleDao.getById(vUserId);
    }

    public List<TblSysUserRole> getByEntity(TblSysUserRole tblSysUserRole) {
        return tblSysUserRoleDao.getByEntity(tblSysUserRole);
    }

    public List<TblSysUserRole> listByEntity(TblSysUserRole tblSysUserRole) {
        return tblSysUserRoleDao.listByEntity(tblSysUserRole);
    }

    public List<TblSysUserRole> listByIds(List<String> ids) {
        return tblSysUserRoleDao.listByIds(ids);
    }


    public int insert(TblSysUserRole tblSysUserRole) {
        Date date = new Date();
        tblSysUserRole.setDtCreatetime(date);
        tblSysUserRole.setDtModifytime(date);
        return tblSysUserRoleDao.insert(tblSysUserRole);
    }

    public int insertBatch(List<TblSysUserRole> list) {
        return tblSysUserRoleDao.insertBatch(list);
    }

    public int update(TblSysUserRole tblSysUserRole) {
        tblSysUserRole.setDtModifytime(new Date());
        return tblSysUserRoleDao.update(tblSysUserRole);
    }

    public int updateBatch(List<TblSysUserRole> list) {
        return tblSysUserRoleDao.updateBatch(list);
    }

    public int deleteById(String vUserId) {
        return tblSysUserRoleDao.deleteById(vUserId);
    }

    public int deleteByEntity(TblSysUserRole tblSysUserRole) {
        return tblSysUserRoleDao.deleteByEntity(tblSysUserRole);
    }

    public int deleteByIds(List<String> list) {
        return tblSysUserRoleDao.deleteByIds(list);
    }

    public int countAll() {
        return tblSysUserRoleDao.countAll();
    }

    public int countByEntity(TblSysUserRole tblSysUserRole) {
        return tblSysUserRoleDao.countByEntity(tblSysUserRole);
    }

}