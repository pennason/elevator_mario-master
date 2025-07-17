package com.shmashine.pm.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblInvestigateTask;
import com.shmashine.pm.api.module.investigateTask.input.SearchInvestigateTaskListModule;
import com.shmashine.pm.api.module.investigateTask.input.SearchInvestigateTaskSelectListModule;

public interface BizInvestigateTaskDao {

    /**
     * 小区列表
     */
    List<Map> searchInvestigateTaskList(SearchInvestigateTaskListModule searchInvestigateTaskListModule);

    /**
     * 获取小区下拉框
     */
    List<Map> searchInvestigateTaskSelectList(SearchInvestigateTaskSelectListModule searchInvestigateTaskSelectListModule);

    TblInvestigateTask getByVillageId(String vVillageId);

    HashMap getRelativeInfo(String vInvestigateTaskId);

    Map getBizVillageInfo(String vInvestigateTaskId);

    List<Integer> getAllStatus(SearchInvestigateTaskListModule searchInvestigateTaskListModule);
}
