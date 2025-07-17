package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.dto.TblTestingTaskDto;
import com.shmashine.pm.api.module.testingTask.input.SearchTestingTaskListModule;

public interface BizTestingTaskDao {

    /**
     * 小区列表
     */
    List<Map> searchTestingTaskList(SearchTestingTaskListModule searchTestingTaskListModule);


    TblTestingTaskDto getTaskDetail(String vTestingTaskId);

    List<Map> getElevatorsInfo(String vTestingTaskId);
}
