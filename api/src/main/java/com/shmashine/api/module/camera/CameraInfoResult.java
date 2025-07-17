package com.shmashine.api.module.camera;

import lombok.Data;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2022/6/6 15:06
 * <p>
 * 上海智慧电梯平台-摄像头基本信息
 */
@Data
public class CameraInfoResult {

    /**
     * 电梯注册码
     */
    private String registerNumber;

    /**
     * 摄像头安装状态
     * true:已安装
     * false:未安装
     */
    private Boolean cameraStatus;

    /**
     * 摄像头在线状态
     * true：在线
     * false：离线
     */
    private Boolean onlineStatus;
}
