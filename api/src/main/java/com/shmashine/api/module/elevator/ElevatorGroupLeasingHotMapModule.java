// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.module.elevator;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/3/10 14:38
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ElevatorGroupLeasingHotMapModule implements Serializable {
    private String elevatorCode;
    private String statisticsType;
    private String floor;
    private Double dayCoefficient;
    private Double averageCoefficient;
    private Double percent;
    private Integer level;

    private Map<String, CameraDownloadTaskModule> evidences;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class CameraDownloadTaskModule implements Serializable {
        private String elevatorCode;
        private String taskCustomId;
        private String collectTime;
        private String startTime;
        private String endTime;
        private String floor;
        private String ossUrl;
    }
}
