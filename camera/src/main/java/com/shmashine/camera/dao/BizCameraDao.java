package com.shmashine.camera.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.camera.model.CamerasResponseModule;
import com.shmashine.camera.model.SearchCamerasModule;
import com.shmashine.common.entity.TblCamera;
import com.shmashine.common.entity.TblCameraCascadePlatformEntity;

@Mapper
public interface BizCameraDao {

    /**
     * 基础服务：获取摄像头绑定分页列表
     *
     * @param searchCamerasModule
     * @return
     */
    List<Map<String, Object>> searchElevatorListByPage(@Param("searchCamerasModule") SearchCamerasModule searchCamerasModule);

    /**
     * 基础服务：获取摄像头分页列表
     *
     * @param searchCamerasModule
     * @return
     */
    List<Map<String, Object>> searchCamerasListByPage(@Param("searchCamerasModule") SearchCamerasModule searchCamerasModule);

    /**
     * 基础服务：获取摄像头关联视频
     *
     * @param searchCamerasModule
     * @return
     */
    List<CamerasResponseModule> searchCamerasVedioAndPicByPage(@Param("searchCamerasModule") SearchCamerasModule searchCamerasModule);


    /**
     * Socket服务调用：根据电梯编号 获取rtmp流地址
     *
     * @param elevatorCode 电梯编号
     * @return rtmp流地址
     */
    String getRtmpUrlByElevatorCode(@Param("elevatorCode") String elevatorCode);


    /**
     * Socket服务调用：通过电梯编号 获取hls流地址
     *
     * @param elevatorCode 电梯编号
     * @return hls流地址
     */
    String getHlsUrlByElevatorCode(@Param("elevatorCode") String elevatorCode);


    /**
     * Socket服务调用：截取摄像头当前一帧图片，返回图片存储路径
     *
     * @param vCloudNumber 云平台序列号
     * @return 图片存储路径
     */
    List<TblCamera> getByvCloudNumber(@Param("vCloudNumber") String vCloudNumber);

    /**
     * 根据电梯码查找摄像头
     *
     * @param vElevatorCode 电梯码
     * @return
     */
    TblCamera getByElevatorCode(@Param("vElevatorCode") String vElevatorCode);


    /**
     * 根据电梯id获取摄像头信息
     *
     * @param elevatorId
     * @return
     */
    TblCamera getByElevatorId(String elevatorId);

    TblCamera getByCameraId(String cameraId);


    int insertCamera(@NotNull TblCamera tblCamera);

    int updateCamera(@NotNull TblCamera tblCamera);

    int deleteCameraInfoById(String cameraId);

    /**
     * 电梯绑定修改
     *
     * @param tblCamera
     * @return
     */
    int updateElevotorBound(TblCamera tblCamera);

    /**
     * 根据电梯编码删除
     *
     * @param vElevatorCode
     * @return
     */
    int delByEleCode(String vElevatorCode);

    HashMap<String, String> getHkCameraInfo(String vCameraId);

    /**
     * 根据级联编码获取摄像头信息
     *
     * @param channelCode
     * @return
     */
    TblCameraCascadePlatformEntity queryCameraInfoByChannelCode(String channelCode);

    /**
     * 根据电梯编码获取摄像头报警配置ID
     *
     * @param elevatorCode 电梯编码
     * @return
     */
    String getCameraAlarmConfigByElevatorCode(String elevatorCode);

}
