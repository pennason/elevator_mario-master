// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/26 10:39
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class HikCloudPageResponseDTO extends ResponseCustom<HikCloudPageResponseDTO.PageInfo> {

    @Data
    public static class PageInfo {
        /**
         * 当前页数
         */
        private Integer pageNo;
        /**
         * 分页量
         */
        private Integer pageSize;
        /**
         * 总页数
         */
        private Integer totalPage;
        /**
         * 总条数
         */
        private Integer total;
        /**
         * 是否有下一页（true：是）
         */
        private Boolean hasNextPage;
        /**
         * 是否有上一页（true：是）
         */
        private Boolean hasPreviousPage;
        /**
         * 是否为首页（true：是）
         */
        private Boolean firstPage;
        /**
         * 是否为尾页（true：是）
         */
        private Boolean lastPage;
        /**
         * 资源列表（见下方）
         */
        private List<Map<String, Object>> rows;
    }
}
