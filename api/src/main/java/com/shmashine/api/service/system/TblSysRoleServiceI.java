package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblSysRoleDao;
import com.shmashine.common.entity.TblSysRole;

public interface TblSysRoleServiceI {

    TblSysRoleDao getTblSysRoleDao();

    TblSysRole getById(String vRoleId);

    List<TblSysRole> getByEntity(TblSysRole tblSysRole);

    List<TblSysRole> listByEntity(TblSysRole tblSysRole);

    List<TblSysRole> listByIds(List<String> ids);

    int insert(TblSysRole tblSysRole);

    int insertBatch(List<TblSysRole> list);

    int update(TblSysRole tblSysRole);

    int updateBatch(List<TblSysRole> list);

    int deleteById(String vRoleId);

    int deleteByEntity(TblSysRole tblSysRole);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysRole tblSysRole);
}