package com.shmashine.api.controller.device.vo;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  jiangheng
 * @version 2023/12/18 17:25
 * @description: com.shmashine.api.controller.device.vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetDeviceParamConfRespVO {

    private List<DeviceParamConfRespVO> confList;

    private Integer deviceConfStatus;

    /**
     * 【平层传感器】备注信息, 1:烟杆 2:小平层 3:U型光电 4:门磁
     */
    private Integer floorSensorRemark;

    private Date lastConfTime;

}
