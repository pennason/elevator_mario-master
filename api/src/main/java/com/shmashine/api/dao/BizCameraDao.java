package com.shmashine.api.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.api.entity.KpiProjectElevatorCountEntity;
import com.shmashine.api.module.camera.CameraInfoResult;
import com.shmashine.common.entity.TblCamera;

@Mapper
public interface BizCameraDao {
    TblCamera getByElevatorId(String elevatorId);

    TblCamera getByElevatorCode(String elevatorCode);

    HashMap<String, String> getHkCameraInfo(String vCameraId);

    /**
     * 上海智慧电梯平台-摄像头基本信息
     *
     * @param registerNumbers
     * @return
     */
    List<CameraInfoResult> getCameraInfoByRegisterNumbers(List<String> registerNumbers);


    /**
     * 按项目统计摄像头安装总数
     *
     * @return 结果
     */
    List<KpiProjectElevatorCountEntity> countCameraGroupByProject();

    /**
     * 按项目统计摄像头离线总数
     *
     * @return 结果
     */
    List<KpiProjectElevatorCountEntity> countOfflineCameraGroupByProject();
}
