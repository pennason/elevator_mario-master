package com.shmashine.pm.api.service.testingTask;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblTestingTask;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.entity.dto.TblTestingTaskDto;
import com.shmashine.pm.api.module.testingTask.input.SearchTestingTaskListModule;

public interface BizTestingTaskService {

//    List<Map> searchTestingTaskSelectList(SearchTestingTaskSelectListModule searchTestingTaskSelectListModule);

    PageListResultEntity<Map> searchTestingTaskList(SearchTestingTaskListModule searchTestingTaskListModule);

    List<Map> searchAllTestingTask(SearchTestingTaskListModule searchTestingTaskListModule);

    TblTestingTask getByTestingTaskId(String vTestingTaskId);

    TblTestingTaskDto getTaskDetail(String vTestingTaskId);

    List<Map> getElevatorsInfo(String vTestingTaskId);
}
