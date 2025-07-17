package com.shmashine.pm.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.TblSysUser;

public interface TblSysUserDao {
    TblSysUser getById(@NotNull String vUserId);

    List<TblSysUser> listByEntity(TblSysUser tblSysUser);

    List<TblSysUser> getByEntity(TblSysUser tblSysUser);

    List<TblSysUser> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysUser tblSysUser);

    int insertBatch(@NotEmpty List<TblSysUser> list);

    int update(@NotNull TblSysUser tblSysUser);

    int updateByField(@NotNull @Param("where") TblSysUser where, @NotNull @Param("set") TblSysUser set);

    int updateBatch(@NotEmpty List<TblSysUser> list);

    int deleteById(@NotNull String vUserId);

    int deleteByEntity(@NotNull TblSysUser tblSysUser);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysUser tblSysUser);
}
