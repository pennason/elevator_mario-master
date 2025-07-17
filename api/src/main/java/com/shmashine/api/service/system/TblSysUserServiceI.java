package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblSysUserDao;
import com.shmashine.common.entity.TblSysUser;

public interface TblSysUserServiceI {

    TblSysUserDao getTblSysUserDao();

    TblSysUser getById(String vUserId);

    List<TblSysUser> getByEntity(TblSysUser tblSysUser);

    List<TblSysUser> listByEntity(TblSysUser tblSysUser);

    List<TblSysUser> listByIds(List<String> ids);

    int insert(TblSysUser tblSysUser);

    int insertBatch(List<TblSysUser> list);

    int update(TblSysUser tblSysUser);

    int updateBatch(List<TblSysUser> list);

    int deleteById(String vUserId);

    int deleteByEntity(TblSysUser tblSysUser);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysUser tblSysUser);
}