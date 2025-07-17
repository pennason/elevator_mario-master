package com.shmashine.fault.service.PeopleFlowStatistics;

import com.shmashine.common.entity.TblCameraImageIdentifyEntity;
import com.shmashine.common.message.PeopleFlowStatisticsMessage;

/**
 * 人流量统计服务
 *
 * @author jiangheng
 * @version V1.0.0 - 2024/1/8 17:08
 */
public interface PeopleFlowStatisticsService {

    /**
     * 插入人流量统计记录
     *
     * @param messageData 人流统计消息
     */
    void insert(PeopleFlowStatisticsMessage messageData);

    /**
     * 添加图像识别记录
     *
     * @param cameraImageIdentifyEntity 图像识别记录
     */
    void insertCameraImageIdentify(TblCameraImageIdentifyEntity cameraImageIdentifyEntity);

}
