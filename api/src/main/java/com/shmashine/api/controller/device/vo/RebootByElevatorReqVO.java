package com.shmashine.api.controller.device.vo;

import java.util.List;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2023/12/18 18:02
 * @description: 电梯重启请求
 */
@Data
public class RebootByElevatorReqVO {

    private List<String> sensorTypes;

    private String elevatorCode;

    private String updateMethod;

}
