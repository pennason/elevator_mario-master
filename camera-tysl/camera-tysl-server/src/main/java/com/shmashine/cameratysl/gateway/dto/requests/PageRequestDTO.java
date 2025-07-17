// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto.requests;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 14:34
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO implements Serializable {

    /**
     * 每⻚记录数取值范围(1-1000)，不传默认100
     */
    @Schema(title = "每页大小", description = "每⻚记录数取值范围(1-1000)，不传默认100", minimum = "1", maximum = "1000",
            defaultValue = "100")
    private Integer pageSize;
    /**
     * 当前⻚，不传默认1
     */
    @Schema(title = "当前页", description = "当前⻚，不传默认1", example = "1", defaultValue = "1")
    private Integer pageNum;
}
