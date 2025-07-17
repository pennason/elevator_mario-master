package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysRoleMenu;

public interface TblSysRoleMenuDao {

    TblSysRoleMenu getById(@NotNull String vRoleId);

    List<TblSysRoleMenu> listByEntity(TblSysRoleMenu tblSysRoleMenu);

    List<TblSysRoleMenu> getByEntity(TblSysRoleMenu tblSysRoleMenu);

    List<TblSysRoleMenu> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysRoleMenu tblSysRoleMenu);

    int insertBatch(@NotEmpty List<TblSysRoleMenu> list);

    int update(@NotNull TblSysRoleMenu tblSysRoleMenu);

    int updateByField(@NotNull @Param("where") TblSysRoleMenu where, @NotNull @Param("set") TblSysRoleMenu set);

    int updateBatch(@NotEmpty List<TblSysRoleMenu> list);

    int deleteById(@NotNull String vRoleId);

    int deleteByEntity(@NotNull TblSysRoleMenu tblSysRoleMenu);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysRoleMenu tblSysRoleMenu);

}