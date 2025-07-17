package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.dto.TblInstallingTaskDto;
import com.shmashine.pm.api.module.installingTask.input.SearchInstallingTaskListModule;
import com.shmashine.pm.api.module.installingTask.input.SearchInstallingTaskSelectListModule;

public interface BizInstallingTaskDao {

    /**
     * 安装单列表
     */
    List<Map> searchInstallingTaskList(SearchInstallingTaskListModule searchInstallingTaskListModule);

    /**
     * 获取小区下拉框
     */
    List<Map> searchInstallingTaskSelectList(SearchInstallingTaskSelectListModule searchInstallingTaskSelectListModule);

    TblInstallingTaskDto getTaskDetail(String vInstallingTaskId);

    List<Map> getElevatorsInfo(String vInstallingTaskId);

    List<Integer> getAllStatus(SearchInstallingTaskListModule searchInstallingTaskListModule);
}
