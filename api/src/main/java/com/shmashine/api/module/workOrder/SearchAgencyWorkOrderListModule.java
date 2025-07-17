package com.shmashine.api.module.workOrder;

import java.util.List;

import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取用户代办工单列表 供首页展示
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchAgencyWorkOrderListModule extends PageListParams {

    private List<String> projectIds;

    private List<String> elevatorIds;

}
