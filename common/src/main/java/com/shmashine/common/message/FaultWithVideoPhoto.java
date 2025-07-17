// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.message;

import lombok.Data;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/12 15:45
 * @since v1.0
 */

@Data
public class FaultWithVideoPhoto extends FaultMessage {
    /**
     * 取证照片URL
     */
    private String faultImageUrl;
    /**
     * 取证视频URL
     */
    private String faultVideoUrl;
}
