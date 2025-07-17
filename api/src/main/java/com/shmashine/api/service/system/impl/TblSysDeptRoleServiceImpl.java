package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblSysDeptRoleDao;
import com.shmashine.api.service.system.TblSysDeptRoleServiceI;
import com.shmashine.common.entity.TblSysDeptRole;

@Service
public class TblSysDeptRoleServiceImpl implements TblSysDeptRoleServiceI {

    @Resource(type = TblSysDeptRoleDao.class)
    private TblSysDeptRoleDao tblSysDeptRoleDao;

    @Override
    public TblSysDeptRoleDao getTblSysDeptRoleDao() {
        return tblSysDeptRoleDao;
    }

    public TblSysDeptRole getById(String vDeptId) {
        return tblSysDeptRoleDao.getById(vDeptId);
    }

    public List<TblSysDeptRole> getByEntity(TblSysDeptRole tblSysDeptRole) {
        return tblSysDeptRoleDao.getByEntity(tblSysDeptRole);
    }

    public List<TblSysDeptRole> listByEntity(TblSysDeptRole tblSysDeptRole) {
        return tblSysDeptRoleDao.listByEntity(tblSysDeptRole);
    }

    public List<TblSysDeptRole> listByIds(List<String> ids) {
        return tblSysDeptRoleDao.listByIds(ids);
    }

    public int insert(TblSysDeptRole tblSysDeptRole) {
        Date date = new Date();
        tblSysDeptRole.setDtCreatetime(date);
        tblSysDeptRole.setDtModifytime(date);
        return tblSysDeptRoleDao.insert(tblSysDeptRole);
    }

    public int insertBatch(List<TblSysDeptRole> list) {
        return tblSysDeptRoleDao.insertBatch(list);
    }

    public int update(TblSysDeptRole tblSysDeptRole) {
        tblSysDeptRole.setDtModifytime(new Date());
        return tblSysDeptRoleDao.update(tblSysDeptRole);
    }

    public int updateBatch(List<TblSysDeptRole> list) {
        return tblSysDeptRoleDao.updateBatch(list);
    }

    public int deleteById(String vDeptId) {
        return tblSysDeptRoleDao.deleteById(vDeptId);
    }

    public int deleteByEntity(TblSysDeptRole tblSysDeptRole) {
        return tblSysDeptRoleDao.deleteByEntity(tblSysDeptRole);
    }

    public int deleteByIds(List<String> list) {
        return tblSysDeptRoleDao.deleteByIds(list);
    }

    public int countAll() {
        return tblSysDeptRoleDao.countAll();
    }

    public int countByEntity(TblSysDeptRole tblSysDeptRole) {
        return tblSysDeptRoleDao.countByEntity(tblSysDeptRole);
    }

}