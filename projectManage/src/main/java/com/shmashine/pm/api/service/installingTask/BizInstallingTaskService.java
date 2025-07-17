package com.shmashine.pm.api.service.installingTask;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblInstallingTask;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.entity.dto.TblInstallingTaskDto;
import com.shmashine.pm.api.module.installingTask.input.SearchInstallingTaskListModule;
import com.shmashine.pm.api.module.installingTask.input.SearchInstallingTaskSelectListModule;

public interface BizInstallingTaskService {

    List<Map> searchInstallingTaskSelectList(SearchInstallingTaskSelectListModule searchInstallingTaskSelectListModule);

    PageListResultEntity<Map> searchInstallingTaskList(SearchInstallingTaskListModule searchInstallingTaskListModule);

    List<Map> searchAllInstallingTask(SearchInstallingTaskListModule searchInstallingTaskListModule);

    TblInstallingTask getByInstallingTaskId(String vInstallingTaskId);

    TblInstallingTaskDto getTaskDetail(String vInstallingTaskId);

    List<Map> getElevatorsInfo(String vInstallingTaskId);

    List<Integer> getAllStatus(SearchInstallingTaskListModule searchInstallingTaskListModule);
}
