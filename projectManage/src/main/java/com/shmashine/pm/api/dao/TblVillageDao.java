package com.shmashine.pm.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblVillage;

public interface TblVillageDao {
    TblVillage getById(@NotNull @Param("vVillageId") String vVillageId);

    List<TblVillage> listByEntity(TblVillage tblVillage);

    List<TblVillage> getByEntity(TblVillage tblVillage);

    List<TblVillage> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblVillage tblVillage);

    int insertBatch(@NotEmpty List<TblVillage> list);

    int update(@NotNull TblVillage TblVillage);

    int updateByField(@NotNull @Param("where") TblVillage where, @NotNull @Param("set") TblVillage set);

    int updateBatch(@NotEmpty List<TblVillage> list);

    int deleteById(@NotNull String vVillageId);

    int deleteByEntity(@NotNull TblVillage tblVillage);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblVillage tblVillage);

    int existsByName(@Param("vVillageName") String vVillageName, @Param("vProjectId") String vProjectId);
}
