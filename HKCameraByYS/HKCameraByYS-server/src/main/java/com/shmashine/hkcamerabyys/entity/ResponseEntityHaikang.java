// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.entity;

import java.io.Serializable;
import java.util.HashMap;

import lombok.Data;
import lombok.ToString;

/**
 * 默认说明
 *
 * @param <T> 返回数据
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/10 9:59
 * @since v1.0
 */

@Data
@ToString
public class ResponseEntityHaikang<T> implements Serializable {

    private DataMeta meta;
    private T data;

    /**
     * Meta 信息
     */
    @Data
    public static class DataMeta {
        private Integer code;
        private String message;
        private HashMap<String, Object> moreInfo;
    }


}
