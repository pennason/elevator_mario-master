package com.shmashine.api.service.wuye;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.fault.input.SearchFaultModule;

public interface MaiXinMaintenanceService {

    /**
     * 获取维保统计
     *
     * @param searchFaultModule
     * @return
     */
    Integer getMaintenanceCount(SearchFaultModule searchFaultModule);

    /**
     * 超期维保
     *
     * @param searchFaultModule
     * @return
     */
    Integer getOverdueMaintenanceCount(SearchFaultModule searchFaultModule);

    /**
     * 获取维保记录列表
     *
     * @param searchFaultModule
     * @return
     */
    PageListResultEntity searchMaintenanceList(SearchFaultModule searchFaultModule);
}
