package com.shmashine.fault.dal.dao;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.common.entity.TblCameraImageIdentifyEntity;

/**
 * 图像识别记录
 *
 * @author jiangheng
 * @version V1.0.0 - 2024/1/9 14:00
 */
@Mapper
public interface TblCameraImageIdentifyMapper {

    /**
     * 插入图像识别记录
     *
     * @param cameraImageIdentifyEntity 图像识别记录
     */
    void insert(TblCameraImageIdentifyEntity cameraImageIdentifyEntity);

}
