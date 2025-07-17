// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/10 14:10
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ResponseFileEntityHaikang extends ResponseEntityHaikang<ResponseFileEntityHaikang.FileInfo> {

    /**
     * 文件信息
     */
    @Data
    public static class FileInfo {
        /**
         * 过期时间 1673610578230
         */
        private Long expire;
        private String[] urls;
    }
}
