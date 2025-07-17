package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblSysRoleMenuDao;
import com.shmashine.common.entity.TblSysRoleMenu;


public interface TblSysRoleMenuServiceI {

    TblSysRoleMenuDao getTblSysRoleMenuDao();

    TblSysRoleMenu getById(String vRoleId);

    List<TblSysRoleMenu> getByEntity(TblSysRoleMenu tblSysRoleMenu);

    List<TblSysRoleMenu> listByEntity(TblSysRoleMenu tblSysRoleMenu);

    List<TblSysRoleMenu> listByIds(List<String> ids);

    int insert(TblSysRoleMenu tblSysRoleMenu);

    int insertBatch(List<TblSysRoleMenu> list);

    int update(TblSysRoleMenu tblSysRoleMenu);

    int updateBatch(List<TblSysRoleMenu> list);

    int deleteById(String vRoleId);

    int deleteByEntity(TblSysRoleMenu tblSysRoleMenu);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysRoleMenu tblSysRoleMenu);
}