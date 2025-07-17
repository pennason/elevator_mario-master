package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysUserRole;

public interface TblSysUserRoleDao {

    TblSysUserRole getById(@NotNull String vUserId);

    List<TblSysUserRole> listByEntity(TblSysUserRole tblSysUserRole);

    List<TblSysUserRole> getByEntity(TblSysUserRole tblSysUserRole);

    List<TblSysUserRole> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysUserRole tblSysUserRole);

    int insertBatch(@NotEmpty List<TblSysUserRole> list);

    int update(@NotNull TblSysUserRole tblSysUserRole);

    int updateByField(@NotNull @Param("where") TblSysUserRole where, @NotNull @Param("set") TblSysUserRole set);

    int updateBatch(@NotEmpty List<TblSysUserRole> list);

    int deleteById(@NotNull String vUserId);

    int deleteByEntity(@NotNull TblSysUserRole tblSysUserRole);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysUserRole tblSysUserRole);

}