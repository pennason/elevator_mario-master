package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.api.entity.CameraAlarmConfig;

/**
 * @author  jiangheng
 * @version 2024/3/5 14:29
 * @description: 海康摄像头告警配置
 */
@Mapper
public interface CameraAlarmConfigDao {

    /**
     * 根据电梯ID获取摄像头报警配置
     *
     * @param elevatorId 电梯id
     * @return
     */
    List<CameraAlarmConfig> getByElevatorId(String elevatorId);

    /**
     * 添加摄像头报警配置
     *
     * @param cameraAlarmConfig 摄像头报警配置
     * @return
     */
    int addCameraAlarmConfig(CameraAlarmConfig cameraAlarmConfig);

    /**
     * 摄像头告警配置删除
     *
     * @param id 主键id
     * @return
     */
    int delCameraAlarmConfig(String id);

    /**
     * 修改摄像头告警配置
     *
     * @param cameraAlarmConfig 摄像头报警配置
     */
    void updateCameraAlarmConfig(CameraAlarmConfig cameraAlarmConfig);
}
