package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblSysUserRoleDao;
import com.shmashine.common.entity.TblSysUserRole;

public interface TblSysUserRoleServiceI {

    TblSysUserRoleDao getTblSysUserRoleDao();

    TblSysUserRole getById(String vUserId);

    List<TblSysUserRole> getByEntity(TblSysUserRole tblSysUserRole);

    List<TblSysUserRole> listByEntity(TblSysUserRole tblSysUserRole);

    List<TblSysUserRole> listByIds(List<String> ids);

    int insert(TblSysUserRole tblSysUserRole);

    int insertBatch(List<TblSysUserRole> list);

    int update(TblSysUserRole tblSysUserRole);

    int updateBatch(List<TblSysUserRole> list);

    int deleteById(String vUserId);

    int deleteByEntity(TblSysUserRole tblSysUserRole);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysUserRole tblSysUserRole);
}