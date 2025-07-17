package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblSysMenuDao;
import com.shmashine.api.service.system.TblSysMenuServiceI;
import com.shmashine.common.entity.TblSysMenu;

@Service
public class TblSysMenuServiceImpl implements TblSysMenuServiceI {

    @Resource(type = TblSysMenuDao.class)
    private TblSysMenuDao tblSysMenuDao;

    @Override
    public TblSysMenuDao getTblSysMenuDao() {
        return tblSysMenuDao;
    }

    public TblSysMenu getById(String vMenuId) {
        return tblSysMenuDao.getById(vMenuId);
    }

    public List<TblSysMenu> getByEntity(TblSysMenu tblSysMenu) {
        return tblSysMenuDao.getByEntity(tblSysMenu);
    }

    public List<TblSysMenu> listByEntity(TblSysMenu tblSysMenu) {
        return tblSysMenuDao.listByEntity(tblSysMenu);
    }

    public List<TblSysMenu> listByIds(List<String> ids) {
        return tblSysMenuDao.listByIds(ids);
    }

    public int insert(TblSysMenu tblSysMenu) {
        Date date = new Date();
        tblSysMenu.setDtCreatetime(date);
        tblSysMenu.setDtModifytime(date);
        return tblSysMenuDao.insert(tblSysMenu);
    }

    public int insertBatch(List<TblSysMenu> list) {
        return tblSysMenuDao.insertBatch(list);
    }

    public int update(TblSysMenu tblSysMenu) {
        tblSysMenu.setDtModifytime(new Date());
        return tblSysMenuDao.update(tblSysMenu);
    }

    public int updateBatch(List<TblSysMenu> list) {
        return tblSysMenuDao.updateBatch(list);
    }

    public int deleteById(String vMenuId) {
        return tblSysMenuDao.deleteById(vMenuId);
    }

    public int deleteByEntity(TblSysMenu tblSysMenu) {
        return tblSysMenuDao.deleteByEntity(tblSysMenu);
    }

    public int deleteByIds(List<String> list) {
        return tblSysMenuDao.deleteByIds(list);
    }

    public int countAll() {
        return tblSysMenuDao.countAll();
    }

    public int countByEntity(TblSysMenu tblSysMenu) {
        return tblSysMenuDao.countByEntity(tblSysMenu);
    }

}