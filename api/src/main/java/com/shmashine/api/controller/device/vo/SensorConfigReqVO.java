package com.shmashine.api.controller.device.vo;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2023/5/29 15:59
 * @description: com.shmashine.api.controller.device.vo
 */
@Data
public class SensorConfigReqVO {

    @NotBlank(message = "eid不能为空")
    private String eid;

    @NotBlank(message = "传感器类型不能为空")
    private String sensorType;

    @NotBlank(message = "etype不能为空")
    private String etype;

    @NotBlank(message = "ST不能为空")
    @JsonProperty("ST")
    private String ST;

    @NotBlank(message = "properties不能为空")
    private Map<String, Object> properties;

}
