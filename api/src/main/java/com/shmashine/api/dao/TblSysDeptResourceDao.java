package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysDeptResource;

public interface TblSysDeptResourceDao {

    TblSysDeptResource getById(@NotNull String vDeptId);

    List<TblSysDeptResource> listByEntity(TblSysDeptResource tblSysDeptResource);

    List<TblSysDeptResource> getByEntity(TblSysDeptResource tblSysDeptResource);

    List<TblSysDeptResource> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysDeptResource tblSysDeptResource);

    int insertBatch(@NotEmpty List<TblSysDeptResource> list);

    int update(@NotNull TblSysDeptResource tblSysDeptResource);

    int updateByField(@NotNull @Param("where") TblSysDeptResource where, @NotNull @Param("set") TblSysDeptResource set);

    int updateBatch(@NotEmpty List<TblSysDeptResource> list);

    int deleteById(@NotNull String vDeptId);

    int deleteByEntity(@NotNull TblSysDeptResource tblSysDeptResource);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysDeptResource tblSysDeptResource);

}