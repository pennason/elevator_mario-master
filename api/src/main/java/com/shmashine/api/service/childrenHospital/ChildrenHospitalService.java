package com.shmashine.api.service.childrenHospital;

import java.util.Map;

import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/9/26 10:52
 */
public interface ChildrenHospitalService {

    /**
     * 儿童医院雷达图
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    Map<String, Integer> getRadarChart(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule);
}
