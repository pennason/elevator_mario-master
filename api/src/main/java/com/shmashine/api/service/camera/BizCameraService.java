package com.shmashine.api.service.camera;

import java.util.HashMap;
import java.util.List;

import com.shmashine.api.entity.CameraAlarmConfig;
import com.shmashine.api.module.auth.Result;
import com.shmashine.common.entity.TblCamera;

/**
 * 摄像头相关接口
 *
 * @author little.li
 */
public interface BizCameraService {

    TblCamera getByElevatorId(String elevatorId);

    TblCamera getByElevatorCode(String code);

    /**
     * 获取萤石云访问token
     */
    String getEzopenToken();

    //获取海康连接天翼物联平台信息
    HashMap<String, String> getHkCameraInfo(String vCameraId);

    /**
     * 上海智慧电梯平台-摄像头基本信息
     *
     * @param registerNumbers 电梯注册号
     * @return
     */
    Result getCameraInfoByRegisterNumber(List<String> registerNumbers);

    /**
     * 根据电梯ID获取摄像头报警配置
     *
     * @param elevatorId 电梯id
     * @return
     */
    List<CameraAlarmConfig> getCameraAlarmConfig(String elevatorId);

    /**
     * 摄像头报警配置新增
     *
     * @param cameraAlarmConfig 摄像头报警配置
     * @return
     */
    String addCameraAlarmConfig(CameraAlarmConfig cameraAlarmConfig);

    /**
     * 摄像头报警配置删除
     *
     * @param id 摄像头报警配置ID
     * @return
     */
    String delCameraAlarmConfig(String id);

    /**
     * 摄像头报警配置更新
     *
     * @param cameraAlarmConfig 摄像头报警配置
     * @return
     */
    String updateCameraAlarmConfig(CameraAlarmConfig cameraAlarmConfig);
}