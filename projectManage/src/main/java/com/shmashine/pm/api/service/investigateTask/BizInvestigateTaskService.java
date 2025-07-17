package com.shmashine.pm.api.service.investigateTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblInvestigateTask;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.module.investigateTask.input.SearchInvestigateTaskListModule;
import com.shmashine.pm.api.module.investigateTask.input.SearchInvestigateTaskSelectListModule;

public interface BizInvestigateTaskService {

    List<Map> searchInvestigateTaskSelectList(SearchInvestigateTaskSelectListModule searchInvestigateTaskSelectListModule);

    PageListResultEntity<Map> searchInvestigateTaskList(SearchInvestigateTaskListModule searchInvestigateTaskListModule);

    List<Map> searchAllInvestigateTask(SearchInvestigateTaskListModule searchInvestigateTaskListModule);

    TblInvestigateTask getByVillageId(String vVillageId);

    HashMap getRelativeInfo(String vInvestigateTaskId);

    Map getBizVillageInfo(String vInvestigateTaskId);

    List<Integer> getAllStatus(SearchInvestigateTaskListModule searchInvestigateTaskListModule);
}
