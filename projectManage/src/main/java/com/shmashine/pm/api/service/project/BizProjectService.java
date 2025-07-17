package com.shmashine.pm.api.service.project;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblProject;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.module.project.input.SearchElevatorProjectListModule;
import com.shmashine.pm.api.module.project.input.SearchElevatorProjectModule;
import com.shmashine.pm.api.module.project.output.ResultProjectDetail;

public interface BizProjectService {


    PageListResultEntity<Map> searchProjectList(SearchElevatorProjectListModule searchElevatorProjectListModule);

    ResultProjectDetail searchElevatorProjectInfo(TblProject tblProject);

    List<Map> searchProjectSelectList(SearchElevatorProjectModule searchElevatorProjectModule);

    /**
     * 根据部门ID获取部门下属的项目ID
     *
     * @return
     */
    List<String> getProjectIdsByDeptId(String ParentDeptId);

    /**
     * 根据部门ID查询部门及子部门下的 所有项目ID
     *
     * @return
     */
    List<String> getProjectIdsByParentDeptId(String ParentDeptId);

    List<Map> getVillagesStatus(String vProjectId);

    /**
     * 获取状态数量
     *
     * @return
     */
    List<Map> getCountByStatus();

    /**
     * 获取项目和相关任务的数量
     *
     * @return
     */
    List<Map> getStatistics(SearchElevatorProjectListModule searchElevatorProjectListModule);
}
