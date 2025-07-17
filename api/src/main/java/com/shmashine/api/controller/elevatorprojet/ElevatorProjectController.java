package com.shmashine.api.controller.elevatorprojet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.elevatorProject.input.SearchElevatorProjectListModule;
import com.shmashine.api.module.elevatorProject.input.SearchElevatorProjectModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.elevatorproject.BizProjectService;
import com.shmashine.api.service.elevatorproject.TblProjectServiceI;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.common.entity.TblProject;
import com.shmashine.common.utils.SnowFlakeUtils;

/**
 * 项目(工程)接口
 */
@RestController
@RequestMapping("elevatorProject")
public class ElevatorProjectController extends BaseRequestEntity {


    private TblProjectServiceI tblProjectServiceI;
    private BizDeptService bizDeptService;
    private BizUserService bizUserService;
    private BizProjectService bizProjectService;

    @Autowired
    public ElevatorProjectController(TblProjectServiceI tblProjectServiceI, BizDeptService bizDeptService, BizUserService bizUserService, BizProjectService bizProjectService) {
        this.tblProjectServiceI = tblProjectServiceI;
        this.bizDeptService = bizDeptService;
        this.bizUserService = bizUserService;
        this.bizProjectService = bizProjectService;
    }

    /**
     * 所属项目列表  根据用户查询用户的数所部门
     *
     * @param searchElevatorProjectListModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.base.PageListResultEntity#
     */
    @PostMapping("/searchElevatorProjectList")
    public Object searchElevatorProjectList(@RequestBody SearchElevatorProjectListModule searchElevatorProjectListModule) {
        // 1. 查询用户部门
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String deptId = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list
        List<String> results = bizDeptService.getAllSubsByDeptId(deptId);
        searchElevatorProjectListModule.setPermissionDeptIds((ArrayList<String>) results);
        // 3. 权限查找 项目
        return ResponseResult.successObj(bizProjectService.searchProjectList(searchElevatorProjectListModule));
    }

    /**
     * 项目(工程)下拉框
     *
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @SaIgnore
    @GetMapping("/searchElevatorProjectSelectList")
    public Object searchElevatorProjectSelectList(
            @RequestParam(value = "projectIds", required = false) String projectIdsString,
            @RequestParam(value = "user_id", required = false) String userId) {
        // 不可使用 stream 的 collect, jdk 不兼容
        ArrayList<String> projectIds = new ArrayList<>();
        if (StringUtils.hasText(projectIdsString)) {
            Arrays.stream(projectIdsString.split(",")).map(String::trim)
                    .forEach(projectIds::add);
        }
        SearchElevatorProjectModule searchElevatorProjectModule = new SearchElevatorProjectModule();
        searchElevatorProjectModule.setProjectIds(projectIds);

        if (!StringUtils.hasText(userId)) {
            userId = getUserId();
        }

        // 1. 查询用户部门
        JSONObject userDept = bizDeptService.getUserDept(userId);
        String deptId = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list
        List<String> results = bizDeptService.getAllSubsByDeptId(deptId);
        searchElevatorProjectModule.setPermissionDeptIds(results);
        // 3. 权限查找 项目
        return ResponseResult.successObj(bizProjectService.searchProjectSelectList(searchElevatorProjectModule));
    }

    /**
     * 获取用户授权电梯关联的项目列表
     *
     * @return 项目列表
     */
    @PostMapping("/searchUserElevatorProjects")
    public Object searchUserElevatorProjects() {
        return ResponseResult.successObj(bizProjectService.searchUserElevatorProjects(getUserId(),
                bizUserService.isAdmin(getUserId())));
    }

    /**
     * 添加项目
     *
     * @param tblProject
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/insertElevatorProject")
    public Object insertElevatorProject(@RequestBody TblProject tblProject) {
        String projectId = SnowFlakeUtils.nextStrId();
        tblProject.setVProjectId(projectId);
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
     * @param projectId ID
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.module.elevatorProject.output.ResultProjectDetail#
     */
    @PostMapping("/getElevatorProjectInfo")
    public Object getElevatorProjectInfo(@RequestBody @Valid @NotNull(message = "请输入项目Id") String projectId) {
        return ResponseResult.successObj(bizProjectService.searchElevatorProjectInfo(projectId));
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
        int update = tblProjectServiceI.update(tblProject);
        if (update > 0) {
            return ResponseResult.success();
        } else {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 获取所有项目列表-用户电梯授权
     *
     * @param permissionUserId 被授权用户id
     * @return
     */
    @PostMapping("/searchAllProjects")
    public Object searchAllProjects(@RequestParam("permissionUserId") String permissionUserId,
                                    @RequestParam(value = "projectName", required = false) String projectName) {
        return ResponseResult.successObj(bizProjectService.searchAllProjectsAndPermissionStatus(getUserId(), permissionUserId, projectName));
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
