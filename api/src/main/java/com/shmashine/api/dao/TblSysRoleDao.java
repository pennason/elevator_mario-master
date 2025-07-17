package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysRole;

public interface TblSysRoleDao {

    TblSysRole getById(@NotNull String vRoleId);

    List<TblSysRole> listByEntity(TblSysRole tblSysRole);

    List<TblSysRole> getByEntity(TblSysRole tblSysRole);

    List<TblSysRole> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysRole tblSysRole);

    int insertBatch(@NotEmpty List<TblSysRole> list);

    int update(@NotNull TblSysRole tblSysRole);

    int updateByField(@NotNull @Param("where") TblSysRole where, @NotNull @Param("set") TblSysRole set);

    int updateBatch(@NotEmpty List<TblSysRole> list);

    int deleteById(@NotNull String vRoleId);

    int deleteByEntity(@NotNull TblSysRole tblSysRole);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysRole tblSysRole);

}