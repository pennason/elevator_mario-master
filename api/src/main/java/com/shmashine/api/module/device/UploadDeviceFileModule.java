package com.shmashine.api.module.device;

import lombok.Data;

/**
 * 设备升级文件
 *
 * @author little.li
 */
@Data
public class UploadDeviceFileModule {

    /**
     * 电梯编号
     */
    String elevatorCode;

    /**
     * 设备类型
     */
    String sensorType;

    /**
     * 升级方式
     */
    String updateMethod;

    /**
     * 升级文件id
     */
    String deviceFileId;

    /**
     * frep 远程调试
     */
    String frep;

}
