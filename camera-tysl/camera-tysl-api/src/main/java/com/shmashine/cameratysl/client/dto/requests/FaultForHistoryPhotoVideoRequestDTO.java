// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.client.dto.requests;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/9/7 14:31
 * @since v1.0
 */

@Data
@ToString
@Accessors(chain = true)
public class FaultForHistoryPhotoVideoRequestDTO implements Serializable {
    private final String cloudNumber;
    private final String elevatorCode;
    private final String faultType;
    private final String faultId;
    private final Integer cameraType;
    private String occurTime;
}
