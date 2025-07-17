package com.shmashine.pm.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.TblSysDept;

public interface TblSysDeptDao {

    TblSysDept getById(@NotNull String vDeptId);

    List<TblSysDept> listByEntity(TblSysDept tblSysDept);

    List<TblSysDept> getByEntity(TblSysDept tblSysDept);

    List<TblSysDept> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysDept tblSysDept);

    int insertBatch(@NotEmpty List<TblSysDept> list);

    int update(@NotNull TblSysDept tblSysDept);

    int updateByField(@NotNull @Param("where") TblSysDept where, @NotNull @Param("set") TblSysDept set);

    int updateBatch(@NotEmpty List<TblSysDept> list);

    int deleteById(@NotNull String vDeptId);

    int deleteByEntity(@NotNull TblSysDept tblSysDept);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysDept tblSysDept);
}
