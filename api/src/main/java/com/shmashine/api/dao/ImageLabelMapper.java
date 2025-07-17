// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.dto.ImageLabelNotMarkDTO;
import com.shmashine.common.entity.ImageLabelsEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/9 15:33
 * @since v1.0
 */

@Mapper
public interface ImageLabelMapper {
    /**
     * 根据ID 获取相关记录
     *
     * @param id 主键
     * @return 结果
     */
    ImageLabelsEntity getById(Long id);

    /**
     * 根据故障id获取标注信息
     *
     * @param faultId 故障id
     * @return 结果
     */
    ImageLabelsEntity getByFaultId(String faultId);

    /**
     * 新增
     *
     * @param entity 实体
     * @return 结果
     */
    Integer insertEntity(@Param("entity") ImageLabelsEntity entity);

    /**
     * 更新
     *
     * @param entity 实体
     * @return 结果
     */
    Integer updateEntity(@Param("entity") ImageLabelsEntity entity);


    // 非 image_labels 表的查询

    /**
     * 从助动车故障表 tbl_fault_uncivilized_behavior37 与 tbl_sys_file 中 获取 故障信息明细
     *
     * @param faultId 故障id
     * @return 结果
     */
    ImageLabelsEntity getSourceByFaultId(@Param("faultId") String faultId);


    /**
     * 从助动车记录中获取指定条数的记录
     *
     * @param pageSize 条数
     * @return 结果
     */
    List<ImageLabelNotMarkDTO> getNoLabelRoundImagesForElectricBike(@Param("pageSize") Integer pageSize);
}
