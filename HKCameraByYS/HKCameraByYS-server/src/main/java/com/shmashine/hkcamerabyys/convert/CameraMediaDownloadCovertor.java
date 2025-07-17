// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.convert;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.convert.BaseEntityDtoConvertor;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 15:07
 * @since v1.0
 */

@Component
public class CameraMediaDownloadCovertor
        extends BaseEntityDtoConvertor<TblCameraDownloadTaskEntity, CamareMediaDownloadRequestDTO> {

    //CHECKSTYLE:OFF
    @Override
    public TblCameraDownloadTaskEntity dto2Entity(CamareMediaDownloadRequestDTO source) {

        var target = TblCameraDownloadTaskEntity.builder()
                .elevatorCode(source.getElevatorCode())
                .taskType(source.getTaskType() == null ? 0 : source.getTaskType().getCode())
                .taskCustomId(source.getTaskCustomId())
                .mediaType(source.getMediaType() == null ? "jpg" : source.getMediaType().getMediaType())
                .cameraType(0)
                .cloudNumber("")
                .taskCustomType(source.getTaskCustomType() == null ? 0 : source.getTaskCustomType())
                .collectTime(StringUtils.hasText(source.getCollectTime())
                        ? source.getCollectTime() : source.getStartTime())
                .startTime(StringUtils.hasText(source.getStartTime())
                        ? dateFormatChangeNormalToPure(source.getStartTime())
                        : null)
                .endTime(StringUtils.hasText(source.getEndTime())
                        ? dateFormatChangeNormalToPure(source.getEndTime())
                        : null)
                .floor(source.getFloor())
                .fileStatus(0)
                .requestFailedCount(0)
                .uploadFailedCount(0)
                .extendInfo((source.getExtendInfo() == null || source.getExtendInfo().isEmpty())
                        ? null : JSON.toJSONString(source.getExtendInfo()))
                .build();
        // 开始和结束时间逻辑补充
        if (source.getDuringSeconds() != null && source.getDuringSeconds() > 0L) {
            if (StringUtils.hasText(source.getStartTime()) && !StringUtils.hasText(source.getEndTime())) {
                target.setEndTime(getDateTimeAfter(source.getStartTime(), source.getDuringSeconds()));
            }
            if (!StringUtils.hasText(source.getStartTime()) && StringUtils.hasText(source.getEndTime())) {
                target.setStartTime(getDateTimeAfter(source.getStartTime(), -1 * source.getDuringSeconds()));
            }
        }
        // 如果没有自定义故障编号，则自定义生成一个
        if (!StringUtils.hasText(source.getTaskCustomId()) && StringUtils.hasText(source.getElevatorCode())) {
            target.setTaskCustomId(target.getElevatorCode() + (StringUtils.hasText(source.getCollectTime())
                    ? dateFormatChangeNormalToPure(source.getCollectTime())
                    : (StringUtils.hasText(target.getStartTime())
                    ? target.getStartTime()
                    : new SimpleDateFormat(DatePattern.PURE_DATETIME_PATTERN)
                    .format(DateTime.now().setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))))));
        }

        return target;
    } //CHECKSTYLE:ON

    public TblCameraDownloadTaskEntity dto2Entity(CamareMediaDownloadRequestDTO source, Integer cameraType,
                                                  String cloudNumber) {
        var target = dto2Entity(source);
        target.setCameraType(cameraType);
        target.setCloudNumber(cloudNumber);
        return target;
    }

    @Override
    public CamareMediaDownloadRequestDTO entity2Dto(TblCameraDownloadTaskEntity source) {
        return null;
    }

    /**
     * 日期变更
     *
     * @param time    原值 yyyy-MM-dd HH:mm:ss
     * @param seconds 时间 负数为向前
     * @return yyyyMMddHHmmss
     */
    private String getDateTimeAfter(String time, Long seconds) {
        return new SimpleDateFormat(DatePattern.PURE_DATETIME_PATTERN).format(
                DateTime.of(
                        DateTime.of(time, DatePattern.NORM_DATETIME_PATTERN).getTime() + seconds * 1000L));
    }

    /**
     * 日期时间格式转换
     *
     * @param date 原值 yyyy-MM-dd HH:mm:ss
     * @return yyyyMMddHHmmss
     */
    private String dateFormatChangeNormalToPure(String date) {
        return date.replace("-", "").replace(" ", "").replace(":", "");
        /*return new SimpleDateFormat(DatePattern.PURE_DATETIME_PATTERN)
                .format(DateTime.of(date, DatePattern.NORM_DATETIME_PATTERN));*/

    }
}
