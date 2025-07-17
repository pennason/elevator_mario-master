// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.dto.TblCameraDTO;
import com.shmashine.hkcamerabyys.entity.HikCameraAlarmConfig;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 14:49
 * @since v1.0
 */

@Mapper
public interface TblCameraMapper {

    /**
     * 根据电梯编号 获取摄像头信息
     *
     * @param elevatorCode 电梯编号
     * @return 摄像头信息
     */
    TblCameraDTO getCameraInfoByElevatorCode(@Param("elevatorCode") String elevatorCode);

    /**
     * 获取所有摄像头 云台编号
     *
     * @return 云台编号
     */
    List<String> listCloudNumbers();

    /**
     * 更新摄像头在线状态
     *
     * @param cloudNumber  云台编号
     * @param onlineStatus 在线状态 1:在线 0:离线
     * @return 结果
     */
    Integer updateOnlineStatus(@Param("cloudNumber") String cloudNumber, @Param("onlineStatus") Integer onlineStatus);

    /**
     * 根据电梯编号 获取摄像头在线状态
     *
     * @param elevatorCode 电梯编号
     * @return 在线状态
     */
    Integer getCameraOnlineStatusByElevatorCode(String elevatorCode);

    /**
     * 获取摄像头故障告警配置
     *
     * @param devSerial 摄像头序列号
     * @param alarmType 萤石告警类型
     */
    HikCameraAlarmConfig getCameraAlarmConfig(@Param("devSerial") String devSerial,
                                              @Param("alarmType") String alarmType);

}
