// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dto;

import java.io.Serializable;
import java.util.List;

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
public class HikCloudFileDownloadResponseDTO extends ResponseCustom<HikCloudFileDownloadResponseDTO.FileInfoDTO> {

    @Data
    public static class FileInfoDTO implements Serializable {
        /**
         * 过期时间：格式为时间戳, 毫秒
         */
        private Long expire;
        /**
         * 文件列表
         */
        private List<String> urls;
    }

}
