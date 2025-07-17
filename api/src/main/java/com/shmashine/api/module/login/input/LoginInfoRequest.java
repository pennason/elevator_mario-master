package com.shmashine.api.module.login.input;

import java.io.Serializable;

import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/12/13 17:05
 * <p>
 * 获取用户历史登录信息——请求参数
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginInfoRequest extends PageListParams implements Serializable {

    private static final Long serialVersionUID = -246535478952357L;

    private String username;

    private String startTime;

    private String endTime;
}
