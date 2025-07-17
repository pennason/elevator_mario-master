package com.shmashine.api.controller.device.vo;

import com.shmashine.api.module.elevator.SearchElevatorModule;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/6/21 11:25
 * @Since: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchDeviceConfPageReqVO extends SearchElevatorModule {

    /**
     * 设备配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer deviceConfStatus;

}
