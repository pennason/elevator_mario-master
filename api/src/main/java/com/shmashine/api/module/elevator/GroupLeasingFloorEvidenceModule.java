// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.module.elevator;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/3/14 11:09
 * @since v1.0
 */

@Data
@ToString
public class GroupLeasingFloorEvidenceModule implements Serializable {
    @Schema(title = "电梯编号", description = "电梯编号")
    private String elevatorCode;
    @Schema(title = "楼层列表", description = "楼层列表", example = "[]")
    private List<String> floors;
    @Schema(title = "日期 yyyy-MM-dd", description = "指定日期，不指定则为第二天，格式yyyy-MM-dd")
    private String date;
}
