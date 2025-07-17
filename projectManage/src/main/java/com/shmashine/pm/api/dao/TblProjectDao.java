package com.shmashine.pm.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.TblProject;

public interface TblProjectDao {

    TblProject getById(@NotNull String vProjectId);

    List<TblProject> listByEntity(TblProject tblProject);

    List<TblProject> getByEntity(TblProject tblProject);

    List<TblProject> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblProject tblProject);

    int insertBatch(@NotEmpty List<TblProject> list);

    int update(@NotNull TblProject tblProject);

    int updateByField(@NotNull @Param("where") TblProject where, @NotNull @Param("set") TblProject set);

    int updateBatch(@NotEmpty List<TblProject> list);

    int deleteById(@NotNull String vProjectId);

    int deleteByEntity(@NotNull TblProject tblProject);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblProject tblProject);

    String existsByName(@Param("vProjectName") String existsByName);
}
