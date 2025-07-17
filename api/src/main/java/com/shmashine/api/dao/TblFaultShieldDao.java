package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblFaultShield;

public interface TblFaultShieldDao {

    TblFaultShield getById(@NotNull String vFaultShieldId);

    List<TblFaultShield> listByEntity(TblFaultShield tblFaultShield);

    List<TblFaultShield> getByEntity(TblFaultShield tblFaultShield);

    List<TblFaultShield> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblFaultShield tblFaultShield);

    int insertBatch(@NotEmpty List<TblFaultShield> list);

    int update(@NotNull TblFaultShield tblFaultShield);

    int updateByField(@NotNull @Param("where") TblFaultShield where, @NotNull @Param("set") TblFaultShield set);

    int updateBatch(@NotEmpty List<TblFaultShield> list);

    int deleteById(@NotNull String vFaultShieldId);

    int deleteByEntity(@NotNull TblFaultShield tblFaultShield);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblFaultShield tblFaultShield);

    List<Map<String, Object>> getShieldInfo(@Param("elevatorCode") String elevatorCode, @Param("elevatorType") Integer elevatorType, @Param("eventType") int eventType);

    int deleteByElevatorCode(@Param("vElevatorCode") String vElevatorCode);
}