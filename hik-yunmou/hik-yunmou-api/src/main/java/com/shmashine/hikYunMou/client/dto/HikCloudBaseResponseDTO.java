// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.client.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 默认说明
 *
 * @param <T>
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/16 17:59
 * @since v1.0
 */

@Data
@ToString
public class HikCloudBaseResponseDTO<T> implements Serializable {
    private Integer code;
    private String message;
    private Boolean success;
    private T data;
}
