// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 默认说明
 *
 * @param <T> 泛型
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 14:38
 * @since v1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PageResponseDTO<T> extends TyslResponseDTO<PageResponseDTO.PageDTO<T>> {

    /**
     * 分页
     *
     * @param <T> 泛型
     */
    @Data
    public static class PageDTO<T> implements Serializable {
        private Integer pageSize;
        private Integer pageNum;
        private Integer pages;
        private Integer total;
        private List<T> list;
    }
}
