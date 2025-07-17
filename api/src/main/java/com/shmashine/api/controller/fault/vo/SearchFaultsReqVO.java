package com.shmashine.api.controller.fault.vo;

import java.util.List;

import com.shmashine.api.module.fault.input.SearchFaultModule;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 故障列表查询-请求参数
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/12/6 14:11
 * @Since: 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchFaultsReqVO extends SearchFaultModule {

    private List<String> villageIds;

}
