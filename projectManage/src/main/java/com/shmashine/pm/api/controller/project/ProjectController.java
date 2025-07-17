package com.shmashine.pm.api.controller.project;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.pm.api.config.exception.BizException;
import com.shmashine.pm.api.entity.TblCheckingTask;
import com.shmashine.pm.api.entity.TblProject;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.enums.TblProjectStatusEnum;
import com.shmashine.pm.api.module.project.input.SearchElevatorProjectListModule;
import com.shmashine.pm.api.module.project.input.SearchElevatorProjectModule;
import com.shmashine.pm.api.service.checkingTask.TblCheckingTaskService;
import com.shmashine.pm.api.service.dept.BizDeptService;
import com.shmashine.pm.api.service.project.BizProjectService;
import com.shmashine.pm.api.service.project.TblProjectServiceI;
import com.shmashine.pm.api.service.user.BizUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/project")
@Tag(name = "项目接口", description = "项目接口")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class ProjectController extends BaseRequestEntity {

    @Autowired
    private TblProjectServiceI tblProjectServiceI;
    @Autowired
    private BizDeptService bizDeptService;
    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private BizProjectService bizProjectService;
    @Autowired
    private TblCheckingTaskService tblCheckingTaskService;

    /**
     * 所属项目列表  根据用户查询用户的数所部门
     *
     * @param searchElevatorProjectListModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.base.PageListResultEntity#
     */

    @Operation(summary = "获取项目列表", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/searchElevatorProjectList")
    public Object searchElevatorProjectList(@RequestBody SearchElevatorProjectListModule searchElevatorProjectListModule) {
        // 1. 查询用户部门
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String deptId = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list
        List<String> results = bizDeptService.getAllSubsByDeptId(deptId);
        searchElevatorProjectListModule.setPermissionDeptIds((ArrayList<String>) results);

        searchElevatorProjectListModule.setAdminFlag(bizUserService.isAdmin(getUserId()));

        searchElevatorProjectListModule.setUserId(getUserId());
        // 3. 权限查找 项目
        return ResponseResult.successObj(bizProjectService.searchProjectList(searchElevatorProjectListModule));
    }

    /**
     * 项目(工程)下拉框
     *
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @GetMapping("/searchElevatorProjectSelectList")
    public Object searchElevatorProjectSelectList() {
        // 1. 递归 查找 部门id list
        // 1.1 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list
        List<String> results = Lists.newArrayList();
        results.add(dept_id);
        recursion(dept_id, results);
        SearchElevatorProjectModule searchElevatorProjectModule = new SearchElevatorProjectModule();
        searchElevatorProjectModule.setPermissionDeptIds((ArrayList<String>) results);
        // 3. 权限查找 项目
        return ResponseResult.successObj(bizProjectService.searchProjectSelectList(searchElevatorProjectModule));
    }

    /**
     * 添加项目
     *
     * @param tblProject
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/insertElevatorProject")
    public Object insertElevatorProject(@RequestBody TblProject tblProject) {

        String existed = tblProjectServiceI.existsByName(tblProject.getVProjectName());

        if (StringUtils.hasText(existed)) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "项目名已存在"));
        }

        String projectId = SnowFlakeUtils.nextStrId();
        tblProject.setVProjectId(projectId);
        tblProject.setiStatus(TblProjectStatusEnum.Doing.getValue());
        tblProject.setIDelFlag(0);
        int insert = tblProjectServiceI.insert(tblProject);
        if (insert > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 获取项目详情
     *
     * @param
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.module.elevatorProject.output.ResultProjectDetail#
     */
    @PostMapping("/getElevatorProjectInfo")
    public Object getElevatorProjectInfo(@RequestBody TblProject tblProject) {
        return ResponseResult.successObj(bizProjectService.searchElevatorProjectInfo(tblProject));
    }

    /**
     * 修改项目
     *
     * @param tblProject
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/editElevatorProjectInfo")
    public Object editElevatorProjectInfo(@RequestBody TblProject tblProject) {

        String existed = tblProjectServiceI.existsByName(tblProject.getVProjectName());

        if (StringUtils.hasText(existed) && !existed.equals(tblProject.getVProjectId())) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "项目名已存在"));
        }

        int update = tblProjectServiceI.update(tblProject);
        if (update > 0) {
            if (tblProject.getdEndTime() != null) {
                TblCheckingTask taskModule = new TblCheckingTask();
                taskModule.setvProjectId(tblProject.getVProjectId());
                List<TblCheckingTask> tblCheckingTasks = tblCheckingTaskService.listByEntity(taskModule);
                tblCheckingTasks.forEach(item -> {
                    item.setdCheckDate(tblProject.getdEndTime());
                    tblCheckingTaskService.update(item);
                });
            }
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 删除项目
     *
     * @param projectId
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/deleteElevatorProjectInfo")
    public Object deleteElevatorProjectInfo(@RequestBody @Valid @NotNull(message = "请输入项目Id") String projectId) {
        TblProject tblProject = new TblProject();
        tblProject.setIDelFlag(1);
        tblProject.setVProjectId(projectId);
        int update = tblProjectServiceI.update(tblProject);
        if (update > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 项目下小区状态
     *
     * @param
     * @return
     */
    @PostMapping("/getVillagesStatus")
    public Object getVillagesStatus(@RequestBody TblProject tblProject) {
        return ResponseResult.successObj(bizProjectService.getVillagesStatus(tblProject.getVProjectId()));
    }

    @GetMapping("/projectStatusMap")
    public Object getProjectStatus() {
        Map<Integer, String> map = new HashMap<Integer, String>();
        for (TblProjectStatusEnum em : TblProjectStatusEnum.values()) {
            map.put(em.getValue(), em.getName());
        }
        return ResponseResult.successObj(map);
    }

    /**
     * 状态数目
     *
     * @return
     */
    @GetMapping("/getCountByStatus")
    public Object getCountByStatus() {
        return ResponseResult.successObj(bizProjectService.getCountByStatus());
    }


    /**
     * 项目管理-项目和任务数量
     */
    @PostMapping("/getStatistics")
    public Object getStatistics(@RequestBody SearchElevatorProjectListModule searchElevatorProjectListModule) {
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String deptId = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list
        List<String> results = bizDeptService.getAllSubsByDeptId(deptId);
        searchElevatorProjectListModule.setPermissionDeptIds((ArrayList<String>) results);

        searchElevatorProjectListModule.setAdminFlag(bizUserService.isAdmin(getUserId()));

        searchElevatorProjectListModule.setUserId(getUserId());

        return ResponseResult.successObj(bizProjectService.getStatistics(searchElevatorProjectListModule));
    }


    /**
     * 递归查询 下级部门的编号
     *
     * @param dept_id
     * @param strings
     */
    public void recursion(String dept_id, List<String> strings) {

        if (null != dept_id) {
            List<String> userDeptIds = bizUserService.getUserDeptIds(dept_id);
            if (null != userDeptIds && userDeptIds.size() > 0) {
                userDeptIds.forEach(id -> {
                    recursion(id, strings);
                });
            }
            strings.addAll(userDeptIds);
        }
    }
}
