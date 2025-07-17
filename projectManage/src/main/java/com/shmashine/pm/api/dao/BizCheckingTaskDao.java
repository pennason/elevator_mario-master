package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.dto.TblCheckingTaskDto;
import com.shmashine.pm.api.module.checkingTask.input.SearchCheckingTaskListModule;
import com.shmashine.pm.api.module.checkingTask.input.SearchCheckingTaskSelectListModule;

public interface BizCheckingTaskDao {
    /**
     * 小区列表
     */
    List<Map> searchCheckingTaskList(SearchCheckingTaskListModule searchCheckingTaskListModule);

    /**
     * 获取小区下拉框
     */
    List<Map> searchCheckingTaskSelectList(SearchCheckingTaskSelectListModule searchCheckingTaskSelectListModule);

    TblCheckingTaskDto getTaskDetail(String vCheckingTaskId);
}
