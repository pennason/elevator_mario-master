package com.shmashine.pm.api.service.distributionTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblDistributionTask;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.module.distributionTask.input.SearchDistributionTaskListModule;

/**
 * 分配任务接口实现
 */
public interface BizDistributionTaskService {

    /**
     * 获取配货单任务列表
     *
     * @param searchDistributionTaskListModule 查询条件
     * @return 配货单任务列表
     */
    PageListResultEntity<Map> searchDistributionTaskList(SearchDistributionTaskListModule
                                                                 searchDistributionTaskListModule);

    TblDistributionTask getByVillageId(String vVillageId);

    List<HashMap> getRelativeInfo(String vDistributionTaskId);

    Map getBizInfoById(String vDistributionTaskId);

    Integer existsElevatorCode(List<String> elevatorCodes);


}
