package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.camera.SearchCamerasModule;
import com.shmashine.common.dto.TblCameraDTO;
import com.shmashine.common.entity.TblCamera;

public interface TblCameraDao {

    TblCamera getById(@NotNull String vCameraId);

    List<TblCamera> listByEntity(TblCamera tblCamera);

    List<TblCamera> getByEntity(TblCamera tblCamera);

    List<TblCamera> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblCamera tblCamera);

    int insertBatch(@NotEmpty List<TblCamera> list);

    int update(@NotNull TblCamera tblCamera);

    int updateByField(@NotNull @Param("where") TblCamera where, @NotNull @Param("set") TblCamera set);

    int updateBatch(@NotEmpty List<TblCamera> list);

    int deleteById(@NotNull String vCameraId);

    int deleteByEntity(@NotNull TblCamera tblCamera);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblCamera tblCamera);

    List<Map<String, Object>> searchElevatorListByPage(@Param("searchCamerasModule") SearchCamerasModule searchCamerasModule);

    List<Map> electricBicycleConfirm(@Param("elevatorCode") String elevatorCode, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据电梯编号 获取摄像头信息
     *
     * @param elevatorCode 电梯编号
     * @return 摄像头信息
     */
    TblCameraDTO getCameraInfoByElevatorCode(String elevatorCode);
}