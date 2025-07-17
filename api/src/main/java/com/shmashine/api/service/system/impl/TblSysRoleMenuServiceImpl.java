package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblSysRoleMenuDao;
import com.shmashine.api.service.system.TblSysRoleMenuServiceI;
import com.shmashine.common.entity.TblSysRoleMenu;

@Service
public class TblSysRoleMenuServiceImpl implements TblSysRoleMenuServiceI {

    @Resource(type = TblSysRoleMenuDao.class)
    private TblSysRoleMenuDao tblSysRoleMenuDao;

    @Override
    public TblSysRoleMenuDao getTblSysRoleMenuDao() {
        return tblSysRoleMenuDao;
    }

    public TblSysRoleMenu getById(String vRoleId) {
        return tblSysRoleMenuDao.getById(vRoleId);
    }

    public List<TblSysRoleMenu> getByEntity(TblSysRoleMenu tblSysRoleMenu) {
        return tblSysRoleMenuDao.getByEntity(tblSysRoleMenu);
    }

    public List<TblSysRoleMenu> listByEntity(TblSysRoleMenu tblSysRoleMenu) {
        return tblSysRoleMenuDao.listByEntity(tblSysRoleMenu);
    }

    public List<TblSysRoleMenu> listByIds(List<String> ids) {
        return tblSysRoleMenuDao.listByIds(ids);
    }

    public int insert(TblSysRoleMenu tblSysRoleMenu) {
        Date date = new Date();
        tblSysRoleMenu.setDtCreatetime(date);
        tblSysRoleMenu.setDtModifytime(date);
        return tblSysRoleMenuDao.insert(tblSysRoleMenu);
    }

    public int insertBatch(List<TblSysRoleMenu> list) {
        return tblSysRoleMenuDao.insertBatch(list);
    }

    public int update(TblSysRoleMenu tblSysRoleMenu) {
        tblSysRoleMenu.setDtModifytime(new Date());
        return tblSysRoleMenuDao.update(tblSysRoleMenu);
    }

    public int updateBatch(List<TblSysRoleMenu> list) {
        return tblSysRoleMenuDao.updateBatch(list);
    }

    public int deleteById(String vRoleId) {
        return tblSysRoleMenuDao.deleteById(vRoleId);
    }

    public int deleteByEntity(TblSysRoleMenu tblSysRoleMenu) {
        return tblSysRoleMenuDao.deleteByEntity(tblSysRoleMenu);
    }

    public int deleteByIds(List<String> list) {
        return tblSysRoleMenuDao.deleteByIds(list);
    }

    public int countAll() {
        return tblSysRoleMenuDao.countAll();
    }

    public int countByEntity(TblSysRoleMenu tblSysRoleMenu) {
        return tblSysRoleMenuDao.countByEntity(tblSysRoleMenu);
    }

}