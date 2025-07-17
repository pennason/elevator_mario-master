package com.shmashine.api.service.elevatorproject.impl;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.hutool.core.util.NumberUtil;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.shmashine.api.dao.BizElevatorProjectDao;
import com.shmashine.api.dao.TblElevatorDao;
import com.shmashine.api.dao.TblSysUserDao;
import com.shmashine.api.entity.ProjectNameEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.elevatorProject.input.SearchElevatorProjectListModule;
import com.shmashine.api.module.elevatorProject.input.SearchElevatorProjectModule;
import com.shmashine.api.module.elevatorProject.output.ResultProjectDetail;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.elevatorproject.BizProjectService;
import com.shmashine.common.constants.SystemConstants;

/**
 * @PackgeName: com.shmashine.api.service.elevatorproject.impl
 * @ClassName: BizProjectServiceImpl
 * @Date: 2020/7/610:44
 * @Author: LiuLiFu
 * @Description: 项目业务方法
 */
@Service
public class BizProjectServiceImpl implements BizProjectService {

    @Autowired
    BizElevatorProjectDao bizElevatorProjectDao;


//    @Autowired
//    BizUserService bizUserService;

    @Autowired
    BizDeptService bizDeptService;

    @Autowired
    private TblSysUserDao tblSysUserDao;

    @Autowired
    private TblElevatorDao tblElevatorDao;

    /**
     * 获取项目列表
     *
     * @param searchElevatorProjectListModule
     * @return
     */
    @Override
    public PageListResultEntity<Map> searchProjectList(SearchElevatorProjectListModule searchElevatorProjectListModule) {
        Integer pageIndex = searchElevatorProjectListModule.getPageIndex();
        Integer pageSize = searchElevatorProjectListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> tPageInfo = new PageInfo<>(bizElevatorProjectDao.searchElevatorProjectList(searchElevatorProjectListModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) tPageInfo.getTotal(), tPageInfo.getList());
    }

    /**
     * 查看部门
     *
     * @param projectId
     * @return
     */
    @Override
    public ResultProjectDetail searchElevatorProjectInfo(String projectId) {
        return bizElevatorProjectDao.searchElevatorProjectInfo(projectId);
    }

    /**
     * 查找用户列表
     *
     * @param searchElevatorProjectModule 查询条件
     * @return 用户列表
     */
    @Override
    public List<Map> searchProjectSelectList(SearchElevatorProjectModule searchElevatorProjectModule) {
        return bizElevatorProjectDao.searchElevatorProjectSelectList(searchElevatorProjectModule);
    }

    /**
     * 根据部门ID获取部门下属的项目ID
     *
     * @return
     */
    @Override
//    @Cacheable(value = "project" ,key = "targetClass + methodName +#p0")
    public List<String> getProjectIdsByDeptId(String deptId) {
        // 根据部门ID获取所有的关联项目
        List<String> deptIds = Lists.newArrayList();
        deptIds.add(deptId);
        return bizElevatorProjectDao.getProjectIdsByDeptIds(deptIds);
    }

    /**
     * 根据部门ID查询部门及子部门下的 所有项目ID
     *
     * @return
     */
    @Override
//    @Cacheable(value = "project" ,key = "targetClass + methodName +#p0")
    public List<String> getProjectIdsByParentDeptId(String parentDeptId) {

        // 获取部门下属及自己的所有部门ids
        List<String> subDeptIds = bizDeptService.getAllSubsByDeptId(parentDeptId);

        // 根据部门ID获取所有的关联项目
        return bizElevatorProjectDao.getProjectIdsByDeptIds(subDeptIds);
    }

    @Override
    public List<Map<String, String>> searchAllProjectList(String userId) {

        // 1. 查询用户部门
        JSONObject userDept = bizDeptService.getUserDept(userId);
        String deptId = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list
        List<String> depts = bizDeptService.getAllSubsByDeptId(deptId);

        return bizElevatorProjectDao.searchAllProjectList(depts);
    }

    @Override
    public List<Map<String, String>> searchAllProjectsAndPermissionStatus(String userId, String permissionUserId, String projectName) {

        // 1. 查询用户部门
        JSONObject userDept = bizDeptService.getUserDept(userId);
        String deptId = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list
        List<String> depts = bizDeptService.getAllSubsByDeptId(deptId);

        List<Map<String, String>> maps = bizElevatorProjectDao.searchAllProjectListByName(depts, projectName);

        //返回电梯授权状态
        List<Map<String, String>> res = maps.stream().map(it -> {

            //项目id
            String projectId = it.get("projectId");

            //用户项目电梯授权状态 0:未授权;   大于0小于1:授权部分;  1:授权所有
            String permissionStatus = "0";
            //已经授权数
            Integer userCount = tblSysUserDao.countByProjectIdAndUser(projectId, permissionUserId);

            if (userCount > 0) {

                //电梯总数
                Integer count = tblElevatorDao.countByProjectId(projectId);

                permissionStatus = NumberUtil.div(userCount, count, 2).toPlainString();
            }

            //电梯授权情况
            it.put("permissionStatus", permissionStatus);

            return it;
        }).collect(Collectors.toList());

        //排序
        Comparator<Map<String, String>> mapComparator = Comparator.comparing((Map<String, String> o) -> o.get("permissionStatus")).reversed()
                .thenComparing((Map<String, String> o) -> o.get("projectName"), Collator.getInstance(Locale.CHINA));

        return res.stream().sorted(mapComparator).collect(Collectors.toList());
    }

    @Override
    public List<ProjectNameEntity> searchUserElevatorProjects(String userId, boolean isAdmin) {

        //管理员用户获取所有项目
        if (isAdmin) {
            return bizElevatorProjectDao.getAllProjects();
        }

        //获取用户授权电梯所属项目
        List<String> projectIds = tblElevatorDao.searchUserElevatorProjects(userId, isAdmin);
        if (CollectionUtils.isEmpty(projectIds)) {
            return null;
        }

        return bizElevatorProjectDao.getProjectByIds(projectIds);
    }

}
