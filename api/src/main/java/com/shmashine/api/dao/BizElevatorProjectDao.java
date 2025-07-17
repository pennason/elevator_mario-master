package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.entity.ProjectNameEntity;
import com.shmashine.api.module.elevatorProject.input.SearchElevatorProjectListModule;
import com.shmashine.api.module.elevatorProject.input.SearchElevatorProjectModule;
import com.shmashine.api.module.elevatorProject.output.ResultProjectDetail;

public interface BizElevatorProjectDao {


    List<Map> searchElevatorProjectList(SearchElevatorProjectListModule searchElevatorProjectListModule);

    ResultProjectDetail searchElevatorProjectInfo(@Param("projectId") String projectId);

    List<Map> searchElevatorProjectSelectList(SearchElevatorProjectModule searchElevatorProjectModule);


    List<String> getProjectIdsByDeptIds(@Param("deptIds") List<String> deptIds);

    /**
     * 获取用户所有项目
     *
     * @return
     */
    List<Map<String, String>> searchAllProjectList(@Param("deptIds") List<String> deptIds);

    /**
     * 获取所有项目
     *
     * @param deptIds
     * @param projectName
     * @return
     */
    List<Map<String, String>> searchAllProjectListByName(@Param("deptIds") List<String> deptIds, @Param("projectName") String projectName);

    /**
     * 根据项目id获取项目列表
     *
     * @param projectIds 项目id
     * @return 项目列表
     */
    List<ProjectNameEntity> getProjectByIds(List<String> projectIds);

    /**
     * 获取所有项目
     *
     * @return 项目列表
     */
    List<ProjectNameEntity> getAllProjects();
}
