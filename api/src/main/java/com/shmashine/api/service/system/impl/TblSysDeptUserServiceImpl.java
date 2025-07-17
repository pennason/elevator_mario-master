package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblSysDeptUserDao;
import com.shmashine.api.service.system.TblSysDeptUserServiceI;
import com.shmashine.common.entity.TblSysDeptUser;

@Service
public class TblSysDeptUserServiceImpl implements TblSysDeptUserServiceI {

    @Resource(type = TblSysDeptUserDao.class)
    private TblSysDeptUserDao tblSysDeptUserDao;

    @Override
    public TblSysDeptUserDao getTblSysDeptUserDao() {
        return tblSysDeptUserDao;
    }

    public TblSysDeptUser getById(String vDeptId) {
        return tblSysDeptUserDao.getById(vDeptId);
    }

    public List<TblSysDeptUser> getByEntity(TblSysDeptUser tblSysDeptUser) {
        return tblSysDeptUserDao.getByEntity(tblSysDeptUser);
    }

    public List<TblSysDeptUser> listByEntity(TblSysDeptUser tblSysDeptUser) {
        return tblSysDeptUserDao.listByEntity(tblSysDeptUser);
    }

    public List<TblSysDeptUser> listByIds(List<String> ids) {
        return tblSysDeptUserDao.listByIds(ids);
    }

    public int insert(TblSysDeptUser tblSysDeptUser) {
        Date date = new Date();
        tblSysDeptUser.setDtCreatetime(date);
        tblSysDeptUser.setDtModifytime(date);
        return tblSysDeptUserDao.insert(tblSysDeptUser);
    }

    public int insertBatch(List<TblSysDeptUser> list) {
        return tblSysDeptUserDao.insertBatch(list);
    }

    public int update(TblSysDeptUser tblSysDeptUser) {
        tblSysDeptUser.setDtModifytime(new Date());
        return tblSysDeptUserDao.update(tblSysDeptUser);
    }

    public int updateBatch(List<TblSysDeptUser> list) {
        return tblSysDeptUserDao.updateBatch(list);
    }

    public int deleteById(String vDeptId, String vUserId) {
        return tblSysDeptUserDao.deleteById(vDeptId, vUserId);
    }

    public int deleteByEntity(TblSysDeptUser tblSysDeptUser) {
        return tblSysDeptUserDao.deleteByEntity(tblSysDeptUser);
    }

    public int deleteByIds(List<String> list) {
        return tblSysDeptUserDao.deleteByIds(list);
    }

    public int countAll() {
        return tblSysDeptUserDao.countAll();
    }

    public int countByEntity(TblSysDeptUser tblSysDeptUser) {
        return tblSysDeptUserDao.countByEntity(tblSysDeptUser);
    }

}