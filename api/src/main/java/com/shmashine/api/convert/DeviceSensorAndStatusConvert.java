package com.shmashine.api.convert;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.shmashine.api.controller.device.vo.DeviceSensorAndStatusRespVO;
import com.shmashine.api.entity.DeviceSensorAndStatusDTO;

/**
 * 默认说明
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/6/11 16:01
 * @Since: 1.0.0
 */
@Mapper
public interface DeviceSensorAndStatusConvert {

    DeviceSensorAndStatusConvert INSTANCE = Mappers.getMapper(DeviceSensorAndStatusConvert.class);

    DeviceSensorAndStatusRespVO convert(DeviceSensorAndStatusDTO bean);

    List<DeviceSensorAndStatusRespVO> convertList(List<DeviceSensorAndStatusDTO> list);
}
