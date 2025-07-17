// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.camera.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import com.shmashine.api.dao.TblCameraImageIdentifyMapper;
import com.shmashine.api.service.camera.TblCameraImageIdentifyServiceI;
import com.shmashine.api.service.elevator.DO.ImageRecognitionMattingConfigDO;
import com.shmashine.common.entity.TblCameraImageIdentifyEntity;

import lombok.RequiredArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/13 16:07
 * @since v1.0
 */

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TblCameraImageIdentifyServiceImpl implements TblCameraImageIdentifyServiceI {
    private final TblCameraImageIdentifyMapper imageIdentifyMapper;

    @Override
    public TblCameraImageIdentifyEntity getById(Long id) {
        return imageIdentifyMapper.getById(id);
    }

    @Override
    public List<TblCameraImageIdentifyEntity> listInitRecordHoursAgo() {
        var hoursAgoDate = DateUtil.format(DateUtil.offsetHour(DateUtil.date(), -1), DatePattern.NORM_DATETIME_PATTERN);
        return imageIdentifyMapper.listInitRecordHoursAgo(hoursAgoDate);
    }

    @Override
    public void saveImageIdentifyRecord(TblCameraImageIdentifyEntity entity) {
        imageIdentifyMapper.save(entity);
    }

    @Override
    public List<TblCameraImageIdentifyEntity> listLatestUnIdentifyRecords(String date) {
        return imageIdentifyMapper.listRecentUnIdentify(date);
    }

    @Override
    public void updateStatusToIdentifying(List<Long> ids) {
        imageIdentifyMapper.updateStatusToIdentifying(ids);
    }

    @Override
    public void confirmElectricMobileIdentify(String taskCustomId, Integer result) {
        imageIdentifyMapper.update(new TblCameraImageIdentifyEntity(taskCustomId)
                // 记录执行状态 0:初始状态，1：待识别，2：识别中， 3：已识别
                .setStatus(3)
                // 识别状态 0:无结果，1：成功，2：失败
                .setIdentifyStatus(1)
                .setIdentifyResult(String.valueOf(result)));
    }

    @Override
    public TblCameraImageIdentifyEntity getByCustomId(String customId) {
        return imageIdentifyMapper.getByCustomId(customId);
    }

    @Override
    public Integer update(TblCameraImageIdentifyEntity imageIdentifyEntity) {
        return imageIdentifyMapper.update(imageIdentifyEntity);
    }

    @Override
    public ImageRecognitionMattingConfigDO getImageMattingConfigByFaultId(String elevatorCode, String faultType) {
        return imageIdentifyMapper.getImageMattingConfigByFaultType(elevatorCode, faultType);
    }

    @Override
    public void updateImageMattingConfig(String realCoordinates, Long id) {
        imageIdentifyMapper.updateImageMattingConfig(realCoordinates, id);
    }


    /*@Override
    public void restTemplateSendMessage(String taskCustomId, String ossUrl, String identifyType) {

        ossUrl = OssInternalUtils.changeToInternalUrl(ossUrl);

        //拼接请求参数
        String url = "http://47.105.214.0:10089/?type=" + identifyType + "&url=" + ossUrl + "&faultId=" + taskCustomId;

        //异步请求
        try {
            HttpRequest.get(url).timeout(200).executeAsync();
        } catch (Exception e) {

        }
    }*/
}
