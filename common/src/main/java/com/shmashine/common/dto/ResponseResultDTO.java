// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回结果对象
 *
 * @param <T> 泛型
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/26 11:29
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResultDTO<T> implements Serializable {
    /**
     * 业务code
     */
    private String code;
    /**
     * 业务消息
     */
    private String message;
    /**
     * 返回数据对象
     */
    private T info;

    public static final String CODE_OK = "0000";
    public static final String CODE_ERROR = "1111";
    public static final String CODE_VALID = "0001";

    public static ResponseResultDTO<Object> resultValid(String msg) {
        return ResponseResultDTO.builder()
                .code(CODE_VALID)
                .message(msg)
                .build();
    }

    public static ResponseResultDTO<Object> success() {
        return ResponseResultDTO.builder()
                .code(CODE_OK)
                .message("处理成功!")
                .build();
    }

    public static <T> ResponseResultDTO<T> successObj(T object) {
        /*return (ResponseResultDTO<T>) ResponseResultDTO.builder()
                .code(CODE_OK)
                .message("处理成功!")
                .info(object)
                .build();*/
        ResponseResultDTO<T> response = new ResponseResultDTO<>();
        response.setCode(CODE_OK);
        response.setMessage("处理成功!");
        response.setInfo(object);
        return response;
    }

    public static ResponseResultDTO<Object> error() {
        return ResponseResultDTO.builder()
                .code(CODE_ERROR)
                .message("处理失败!")
                .build();
    }

    public static ResponseResultDTO error(String msg) {
        return ResponseResultDTO.builder()
                .code(CODE_ERROR)
                .message(msg)
                .build();
    }

    public static <T> ResponseResultDTO<T> error(String msg, Class<T> clazz) {
        ResponseResultDTO<T> response = new ResponseResultDTO<>();
        response.setCode(CODE_ERROR);
        response.setMessage(msg);
        return response;
    }
}
