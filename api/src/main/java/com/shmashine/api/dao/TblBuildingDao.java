package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblBuilding;

public interface TblBuildingDao {

    TblBuilding getById(@NotNull String vVillageId);

    List<TblBuilding> listByEntity(TblBuilding tblBuilding);

    List<TblBuilding> getByEntity(TblBuilding tblBuilding);

    List<TblBuilding> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblBuilding tblBuilding);

    int insertBatch(@NotEmpty List<TblBuilding> list);

    int update(@NotNull TblBuilding tblBuilding);

    int updateByField(@NotNull @Param("where") TblBuilding where, @NotNull @Param("set") TblBuilding set);

    int updateBatch(@NotEmpty List<TblBuilding> list);

    int deleteById(@NotNull String vVillageId, String vBuildingId);

    int deleteByEntity(@NotNull TblBuilding tblBuilding);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblBuilding tblBuilding);

}