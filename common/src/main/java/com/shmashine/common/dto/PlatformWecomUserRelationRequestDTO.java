// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/26 10:27
 * @since v1.0
 */

@Data
@ToString
@Schema(name = "PlatformWecomUserRelationRequestDTO", description = "平台用户与企业微信用户关系请求DTO")
public class PlatformWecomUserRelationRequestDTO implements Serializable {

    /**
     * 用户ID
     */
    @Schema(description = "平台用户ID", required = true)
    private String userId;
    /**
     * 企业微信成员userid
     */
    @Schema(description = "企业微信成员userid", required = true)
    private String wecomUserId;
    /**
     * 企业微信成员姓名
     */
    @Schema(description = "企业微信成员姓名")
    private String wecomUserName;
}
