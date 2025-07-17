// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 默认说明
 *
 * @param <T> 泛型
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 11:14
 * @since v1.0
 */

@Data
public class TyslResponseDTO<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;
}
