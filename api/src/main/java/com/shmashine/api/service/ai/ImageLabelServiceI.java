// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.ai;

import java.util.List;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.common.dto.ImageLabelMarkRequestDTO;
import com.shmashine.common.dto.ImageLabelNotMarkDTO;
import com.shmashine.common.entity.ImageLabelsEntity;
import com.shmashine.common.enums.CameraImageIdentifyEnum;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/9 15:31
 * @since v1.0
 */

public interface ImageLabelServiceI {

    /**
     * 根据ID 获取相关记录
     *
     * @param id 主键
     * @return 结果
     */
    ImageLabelsEntity getImageLabelById(Long id);

    /**
     * 根据故障ID 获取标注信息
     *
     * @param faultId 故障ID
     * @return 结果
     */
    ImageLabelsEntity getElectricBikeImageLabelByFaultId(String faultId);

    /**
     * 获取未标注的图片信息 （电动车识别）
     *
     * @param markType 标注类型
     * @return 列表
     */
    List<ImageLabelNotMarkDTO> getNoLabelRoundImages(CameraImageIdentifyEnum markType);

    /**
     * 保存标注信息
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    ResponseResult saveImageLabel(ImageLabelMarkRequestDTO requestDTO);

    /**
     * 获取图像标注分类
     *
     * @return 列表
     */
    ResponseResult listLabelClassify();
}
