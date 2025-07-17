package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblElevatorShield;

public interface TblElevatorShieldDao {

    TblElevatorShield getById(@NotNull String vElevatorShieldId);

    List<TblElevatorShield> listByEntity(TblElevatorShield tblElevatorShield);

    List<TblElevatorShield> getByEntity(TblElevatorShield tblElevatorShield);

    List<TblElevatorShield> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblElevatorShield tblElevatorShield);

    int insertBatch(@NotEmpty List<TblElevatorShield> list);

    int update(@NotNull TblElevatorShield tblElevatorShield);

    int updateByField(@NotNull @Param("where") TblElevatorShield where, @NotNull @Param("set") TblElevatorShield set);

    int updateBatch(@NotEmpty List<TblElevatorShield> list);

    int deleteById(@NotNull String vElevatorShieldId);

    int deleteByEntity(@NotNull TblElevatorShield tblElevatorShield);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblElevatorShield tblElevatorShield);

    TblElevatorShield getShieldByElevatorCode(@Param("elevatorCode") String elevatorCode);

    void deleteByElevatorCode(@Param("elevatorCode") String elevatorCode);

}