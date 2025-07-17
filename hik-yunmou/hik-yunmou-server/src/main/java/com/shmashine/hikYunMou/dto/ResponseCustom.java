// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 默认说明
 *
 * @param <T> 返回数据
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/10 14:44
 * @since v1.0
 */

@Data
@ToString
public class ResponseCustom<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public static ResponseCustom buildFailed(Integer code, String message) {
        var responseCustom = new ResponseCustom();
        responseCustom.setCode(code);
        responseCustom.setMessage(message);
        return responseCustom;
    }
}
