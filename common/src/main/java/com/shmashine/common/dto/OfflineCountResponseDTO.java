// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;
import java.util.Map;

import com.shmashine.common.model.Result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/1/17 10:52
 * @since v1.0
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OfflineCountResponseDTO extends Result<Map<String, OfflineCountResponseDTO.TotalAndOffline>>
        implements Serializable {

    @Data
    @ToString
    public static class TotalAndOffline implements Serializable {
        /**
         * 总数
         */
        private Integer total;

        /**
         * 离线数
         */
        private Integer offline;
    }
}
