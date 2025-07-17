package com.shmashine.satoken.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 默认说明
 *
 * @author jiangheng
 * @version v1.0.0 - 2024/3/21 13:59
 * @since v1.0.0
 */
@Data
public class SaResultDTO implements Serializable {

    private Integer code;

    private String msg;

    private Object data;

    public SaResultDTO() {
    }

    public SaResultDTO(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
