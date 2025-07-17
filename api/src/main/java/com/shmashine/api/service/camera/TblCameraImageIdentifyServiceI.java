// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.camera;

import java.util.List;

import com.shmashine.api.service.elevator.DO.ImageRecognitionMattingConfigDO;
import com.shmashine.common.entity.TblCameraImageIdentifyEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/13 16:06
 * @since v1.0
 */

public interface TblCameraImageIdentifyServiceI {

    /**
     * 根据id获取记录
     *
     * @param id id
     * @return 结果
     */
    TblCameraImageIdentifyEntity getById(Long id);

    /**
     * 获取初始状态的记录
     *
     * @return 结果
     */
    List<TblCameraImageIdentifyEntity> listInitRecordHoursAgo();

    /**
     * 保存记录
     *
     * @param entity 记录
     */
    void saveImageIdentifyRecord(TblCameraImageIdentifyEntity entity);

    /**
     * 获取最近待识别的记录
     *
     * @param date 日期 yyyy-MM-dd HH:mm:ss
     * @return 结果
     */
    List<TblCameraImageIdentifyEntity> listLatestUnIdentifyRecords(String date);

    /**
     * 更新记录 识别中
     *
     * @param ids 记录id
     */
    void updateStatusToIdentifying(List<Long> ids);

    /**
     * 确认电动车识别结果
     *
     * @param taskCustomId 任务id
     * @param result       识别结果
     */
    void confirmElectricMobileIdentify(String taskCustomId, Integer result);

    /**
     * 调用二次识别服务, 自研电动车识别
     *
     * @param taskCustomId 任务id
     * @param ossUrl       oss地址
     * @param identifyType 识别类型
     */
    // void restTemplateSendMessage(String taskCustomId, String ossUrl, String identifyType);

    /**
     * 根据自定义id获取记录
     *
     * @param customId
     * @return
     */
    TblCameraImageIdentifyEntity getByCustomId(String customId);

    /**
     * 根据主键更新记录
     *
     * @param imageIdentifyEntity
     * @return
     */
    Integer update(TblCameraImageIdentifyEntity imageIdentifyEntity);

    /**
     * 根据故障类型获取图片抠图配置
     *
     * @param elevatorCode
     * @param faultType
     * @return
     */
    ImageRecognitionMattingConfigDO getImageMattingConfigByFaultId(String elevatorCode, String faultType);

    /**
     * 更新配置实际坐标点
     *
     * @param toJsonStr
     * @param id
     */
    void updateImageMattingConfig(String toJsonStr, Long id);
}
