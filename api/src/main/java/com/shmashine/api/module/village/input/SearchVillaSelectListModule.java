package com.shmashine.api.module.village.input;


import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 查找小区下拉框接口模型
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SearchVillaSelectListModule extends PageListParams {

    /**
     * 项目id集合  在这之前项目是有权限的
     */
    private String projectId;
}
