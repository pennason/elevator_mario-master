package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblSysUserDao;
import com.shmashine.api.service.system.TblSysUserServiceI;
import com.shmashine.common.entity.TblSysUser;

@Service
public class TblSysUserServiceImpl implements TblSysUserServiceI {

    @Resource(type = TblSysUserDao.class)
    private TblSysUserDao tblSysUserDao;

    @Override
    public TblSysUserDao getTblSysUserDao() {
        return tblSysUserDao;
    }

    public TblSysUser getById(String vUserId) {
        return tblSysUserDao.getById(vUserId);
    }

    public List<TblSysUser> getByEntity(TblSysUser tblSysUser) {
        return tblSysUserDao.getByEntity(tblSysUser);
    }

    public List<TblSysUser> listByEntity(TblSysUser tblSysUser) {
        return tblSysUserDao.listByEntity(tblSysUser);
    }

    public List<TblSysUser> listByIds(List<String> ids) {
        return tblSysUserDao.listByIds(ids);
    }

    public int insert(TblSysUser tblSysUser) {
        Date date = new Date();
        tblSysUser.setDtCreatetime(date);
        tblSysUser.setDtModifytime(date);
        return tblSysUserDao.insert(tblSysUser);
    }

    public int insertBatch(List<TblSysUser> list) {
        return tblSysUserDao.insertBatch(list);
    }

    public int update(TblSysUser tblSysUser) {
        tblSysUser.setDtModifytime(new Date());
        return tblSysUserDao.update(tblSysUser);
    }

    public int updateBatch(List<TblSysUser> list) {
        return tblSysUserDao.updateBatch(list);
    }

    public int deleteById(String vUserId) {
        return tblSysUserDao.deleteById(vUserId);
    }

    public int deleteByEntity(TblSysUser tblSysUser) {
        return tblSysUserDao.deleteByEntity(tblSysUser);
    }

    public int deleteByIds(List<String> list) {
        return tblSysUserDao.deleteByIds(list);
    }

    public int countAll() {
        return tblSysUserDao.countAll();
    }

    public int countByEntity(TblSysUser tblSysUser) {
        return tblSysUserDao.countByEntity(tblSysUser);
    }

}