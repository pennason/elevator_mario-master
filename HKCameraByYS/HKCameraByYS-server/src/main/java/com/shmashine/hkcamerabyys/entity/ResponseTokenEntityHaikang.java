// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/10 10:03
 * @since v1.0
 */

@Data
@ToString
public class ResponseTokenEntityHaikang implements Serializable {

    private String code;
    private String msg;
    private TokenHaikang data;

    /**
     * Token 信息
     */
    @Data
    @ToString
    public static class TokenHaikang {
        private String accessToken;

        /**
         * 过期时间点 毫秒  1676595885624
         */
        private Long expireTime;
    }
}
