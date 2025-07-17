// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/10 14:44
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCustom implements Serializable {
    private Integer code;
    private String message;
    private Object data;
}
