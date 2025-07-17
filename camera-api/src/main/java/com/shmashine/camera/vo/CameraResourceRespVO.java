package com.shmashine.camera.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求参数
 *
 * @author  jiangheng
 * @version 2023/3/28 14:32
 * @description: com.shmashine.camera.vo
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CameraResourceRespVO {

    /**
     * 流地址
     */
    private String url;

    /**
     * 过期时间 秒
     */
    private Long expireTime;

    /**
     * 取流token
     */
    private String streamToken;
}
