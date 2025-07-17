package com.shmashine.api.controller.device.vo;

import java.util.List;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2023/12/18 17:46
 * @description: 设备配置同步请求
 */
@Data
public class DeviceParamSyncReqVO {

    private String elevatorCode;

    private List<String> sensorTypes;
}
