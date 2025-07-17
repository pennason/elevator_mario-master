package com.shmashine.socket.camera.service;

/**
 * 摄像头
 *
 * @author little.li
 */
public interface TblCameraServiceI {


    /**
     * 根据电梯编号 获取rtmp流地址
     *
     * @param elevatorCode 电梯编号
     * @return rtmp流地址
     */
    String getRtmpUrlByElevatorCode(String elevatorCode);


    /**
     * 通过电梯编号 获取hls流地址
     *
     * @param elevatorCode 电梯编号
     * @return hls流地址
     */
    String getHlsUrlByElevatorCode(String elevatorCode);


    /**
     * 截取摄像头当前一帧图片，返回图片存储路径
     *
     * @param elevatorCode 电梯编号
     * @return 图片存储路径
     */
    String getCurrentImagePathByElevatorCode(String elevatorCode);

}