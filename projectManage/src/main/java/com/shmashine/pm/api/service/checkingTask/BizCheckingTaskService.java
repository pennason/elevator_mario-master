package com.shmashine.pm.api.service.checkingTask;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblCheckingTask;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.entity.dto.TblCheckingTaskDto;
import com.shmashine.pm.api.module.checkingTask.input.SearchCheckingTaskListModule;
import com.shmashine.pm.api.module.checkingTask.input.SearchCheckingTaskSelectListModule;

public interface BizCheckingTaskService {

    List<Map> searchCheckingTaskSelectList(SearchCheckingTaskSelectListModule searchCheckingTaskSelectListModule);

    PageListResultEntity<Map> searchCheckingTaskList(SearchCheckingTaskListModule searchCheckingTaskListModule);

    List<Map> searchAllCheckingTask(SearchCheckingTaskListModule searchCheckingTaskListModule);

    TblCheckingTask getByCheckingTaskId(String vCheckingTaskId);

    TblCheckingTaskDto getTaskDetail(String vCheckingTaskId);
}
