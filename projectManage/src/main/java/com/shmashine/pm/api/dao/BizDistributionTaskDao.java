package com.shmashine.pm.api.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblDistributionTask;
import com.shmashine.pm.api.module.distributionTask.input.SearchDistributionTaskListModule;

public interface BizDistributionTaskDao {

    /**
     * 小区列表
     */
    List<Map> searchDistributionTaskList(SearchDistributionTaskListModule searchDistributionTaskListModule);

    /**
     * 获取小区下拉框
     */
//    List<Map> searchInvestigateTaskSelectList(SearchInvestigateTaskSelectListModule searchInvestigateTaskSelectListModule);

    TblDistributionTask getByVillageId(String vVillageId);

    List<HashMap> getRelativeInfo(String vDistributionTaskId);

    Map getBizInfoById(String vDistributionTaskId);

    Integer existsElevatorCode(List<String> elevatorCodes);
}
