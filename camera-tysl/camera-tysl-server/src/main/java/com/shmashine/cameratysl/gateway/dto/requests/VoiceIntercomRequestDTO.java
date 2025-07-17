// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto.requests;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/23 14:26
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoiceIntercomRequestDTO implements Serializable {

    /**
     * 统⼀设备管理平台中设备通道唯⼀标识
     */
    @Schema(title = "统⼀设备管理平台中设备通道唯⼀标识", description = "统⼀设备管理平台中设备通道唯⼀标识", required = true)
    private String guid;

    /**
     * 语音文件 语⾳数据格式：g711a,PCMA, 8000，1，16bit
     */
    @Schema(title = "语音文件", description = "语⾳数据格式：g711a,PCMA, 8000，1，16bit", required = true)
    private MultipartFile voiceData;
}
