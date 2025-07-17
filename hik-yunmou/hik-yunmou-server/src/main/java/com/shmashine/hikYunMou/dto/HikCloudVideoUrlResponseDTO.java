// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/9 13:40
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class HikCloudVideoUrlResponseDTO extends ResponseCustom<HikCloudVideoUrlResponseDTO.VideoUrlDTO> {

    @Data
    public static class VideoUrlDTO implements Serializable {
        private String id;
        private String url;
        private String expireTime;
    }

}
