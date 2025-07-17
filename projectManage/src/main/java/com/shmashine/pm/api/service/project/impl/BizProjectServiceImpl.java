package com.shmashine.pm.api.service.project.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.shmashine.pm.api.contants.SystemConstants;
import com.shmashine.pm.api.dao.BizElevatorProjectDao;
import com.shmashine.pm.api.entity.TblProject;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.module.project.input.SearchElevatorProjectListModule;
import com.shmashine.pm.api.module.project.input.SearchElevatorProjectModule;
import com.shmashine.pm.api.module.project.output.ResultProjectDetail;
import com.shmashine.pm.api.service.project.BizProjectService;

/**
 * 项目Service
 */
@Service
public class BizProjectServiceImpl implements BizProjectService {


    @Autowired
    BizElevatorProjectDao bizElevatorProjectDao;


//    @Autowired
//    BizUserService bizUserService;

    @Autowired
    com.shmashine.pm.api.service.dept.BizDeptService BizDeptService;

    /**
     * 获取项目列表
     *
     * @param searchElevatorProjectListModule 入参
     * @return 列表
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
     * @param tblProject 入参
     * @return object
     */
    @Override
    public ResultProjectDetail searchElevatorProjectInfo(TblProject tblProject) {
        return bizElevatorProjectDao.searchElevatorProjectInfo(tblProject);
    }

    /**
     * 查找用户列表
     *
     * @return
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
    @Cacheable(value = "project", key = "targetClass + methodName +#p0")
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
    @Cacheable(value = "project", key = "targetClass + methodName +#p0")
    public List<String> getProjectIdsByParentDeptId(String parentDeptId) {

        // 获取部门下属及自己的所有部门ids
        List<String> subDeptIds = BizDeptService.getAllSubsByDeptId(parentDeptId);

        // 根据部门ID获取所有的关联项目
        return bizElevatorProjectDao.getProjectIdsByDeptIds(subDeptIds);
    }

    public List<Map> getVillagesStatus(String vProjectId) {
        return bizElevatorProjectDao.getVillagesStatus(vProjectId);
    }

    @Override
    public List<Map> getCountByStatus() {
        return bizElevatorProjectDao.getCountByStatus();
    }

    @Override
    public List<Map> getStatistics(SearchElevatorProjectListModule searchElevatorProjectListModule) {
        return bizElevatorProjectDao.getStatistics(searchElevatorProjectListModule);
    }


}
