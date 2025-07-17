package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysMenu;

public interface TblSysMenuDao {

    TblSysMenu getById(@NotNull String vMenuId);

    List<TblSysMenu> listByEntity(TblSysMenu tblSysMenu);

    List<TblSysMenu> getByEntity(TblSysMenu tblSysMenu);

    List<TblSysMenu> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysMenu tblSysMenu);

    int insertBatch(@NotEmpty List<TblSysMenu> list);

    int update(@NotNull TblSysMenu tblSysMenu);

    int updateByField(@NotNull @Param("where") TblSysMenu where, @NotNull @Param("set") TblSysMenu set);

    int updateBatch(@NotEmpty List<TblSysMenu> list);

    int deleteById(@NotNull String vMenuId);

    int deleteByEntity(@NotNull TblSysMenu tblSysMenu);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysMenu tblSysMenu);

}