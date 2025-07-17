package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblElevator;

public interface TblElevatorDao {
    TblElevator getById(@NotNull String vElevatorId);

    List<TblElevator> listByEntity(TblElevator tblElevator);

    List<TblElevator> getByEntity(TblElevator tblElevator);

    List<TblElevator> listByIds(@NotEmpty List<String> list);

    List<TblElevator> listByCodes(@NotEmpty List<String> list);

    int insert(@NotNull TblElevator tblElevator);

    int insertIsNotEmpty(@NotNull TblElevator tblElevator);

    int insertBatch(@NotEmpty List<TblElevator> list);

    int insertBatchParty(@NotEmpty List<TblElevator> list);

    int update(@NotNull TblElevator tblElevator);

    int updateByField(@NotNull @Param("where") TblElevator where, @NotNull @Param("set") TblElevator set);

    int updateBatch(@NotEmpty List<TblElevator> list);

    int deleteById(@NotNull String vElevatorId);

    int deleteByEntity(@NotNull TblElevator tblElevator);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblElevator tblElevator);

    List<String> checkExistsByCodes(@NotEmpty List<String> list);

    int updateElevatorCodeBatch(@NotEmpty List<TblElevator> list);

    List<Map> countWithPmStatus(@Param("vVillageId") @NotNull String vVillageId);

    Map getRelatedInfoById(@Param("vElevatorId") @NotNull String vElevatorId);

    TblElevator getByElevatorCode(@Param("vElevatorCode") @NotNull String vElevatorCode);
}
