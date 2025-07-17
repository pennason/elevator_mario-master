package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblElevatorCollect;

public interface TblElevatorCollectDao {

    TblElevatorCollect getById(@NotNull String vUserId);

    List<TblElevatorCollect> listByEntity(TblElevatorCollect tblElevatorCollect);

    List<TblElevatorCollect> getByEntity(TblElevatorCollect tblElevatorCollect);

    List<TblElevatorCollect> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblElevatorCollect tblElevatorCollect);

    int insertBatch(@NotEmpty List<TblElevatorCollect> list);

    int update(@NotNull TblElevatorCollect tblElevatorCollect);

    int updateByField(@NotNull @Param("where") TblElevatorCollect where, @NotNull @Param("set") TblElevatorCollect set);

    int updateBatch(@NotEmpty List<TblElevatorCollect> list);

    int deleteById(@NotNull @Param("vUserId") String vUserId, @Param("vElevatorId") String vElevatorId);

    int deleteByEntity(@NotNull TblElevatorCollect tblElevatorCollect);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblElevatorCollect tblElevatorCollect);

}