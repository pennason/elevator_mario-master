package com.shmashine.api.service.elevatorproject;

import java.util.List;
import java.util.Map;

import com.shmashine.api.entity.ProjectNameEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.elevatorProject.input.SearchElevatorProjectListModule;
import com.shmashine.api.module.elevatorProject.input.SearchElevatorProjectModule;
import com.shmashine.api.module.elevatorProject.output.ResultProjectDetail;

public interface BizProjectService {

    PageListResultEntity<Map> searchProjectList(SearchElevatorProjectListModule searchElevatorProjectListModule);

    ResultProjectDetail searchElevatorProjectInfo(String projectId);

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

    /**
     * 获取所有项目
     *
     * @return
     */
    List<Map<String, String>> searchAllProjectList(String userId);

    /**
     * 获取项目并且返回项目电梯授权情况
     *
     * @param userId           登录用户id
     * @param permissionUserId 被授权用户id
     * @param projectName      项目名
     * @return
     */
    List<Map<String, String>> searchAllProjectsAndPermissionStatus(String userId, String permissionUserId, String projectName);

    /**
     * 获取用户授权电梯关联的项目列表
     *
     * @param userId  用户id
     * @param isAdmin 是否为管理员
     * @return 项目列表
     */
    List<ProjectNameEntity> searchUserElevatorProjects(String userId, boolean isAdmin);
}
