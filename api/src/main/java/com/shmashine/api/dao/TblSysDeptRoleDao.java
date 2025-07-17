package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysDeptRole;

public interface TblSysDeptRoleDao {

    TblSysDeptRole getById(@NotNull String vDeptId);

    List<TblSysDeptRole> listByEntity(TblSysDeptRole tblSysDeptRole);

    List<TblSysDeptRole> getByEntity(TblSysDeptRole tblSysDeptRole);

    List<TblSysDeptRole> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysDeptRole tblSysDeptRole);

    int insertBatch(@NotEmpty List<TblSysDeptRole> list);

    int update(@NotNull TblSysDeptRole tblSysDeptRole);

    int updateByField(@NotNull @Param("where") TblSysDeptRole where, @NotNull @Param("set") TblSysDeptRole set);

    int updateBatch(@NotEmpty List<TblSysDeptRole> list);

    int deleteById(@NotNull String vDeptId);

    int deleteByEntity(@NotNull TblSysDeptRole tblSysDeptRole);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysDeptRole tblSysDeptRole);

}