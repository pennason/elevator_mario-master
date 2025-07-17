package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblSysMenuDao;
import com.shmashine.common.entity.TblSysMenu;

public interface TblSysMenuServiceI {

    TblSysMenuDao getTblSysMenuDao();

    TblSysMenu getById(String vMenuId);

    List<TblSysMenu> getByEntity(TblSysMenu tblSysMenu);

    List<TblSysMenu> listByEntity(TblSysMenu tblSysMenu);

    List<TblSysMenu> listByIds(List<String> ids);

    int insert(TblSysMenu tblSysMenu);

    int insertBatch(List<TblSysMenu> list);

    int update(TblSysMenu tblSysMenu);

    int updateBatch(List<TblSysMenu> list);

    int deleteById(String vMenuId);

    int deleteByEntity(TblSysMenu tblSysMenu);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysMenu tblSysMenu);
}