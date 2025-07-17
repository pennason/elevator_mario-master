package com.shmashine.pm.api.service.user.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.pm.api.contants.SystemConstants;
import com.shmashine.pm.api.dao.BizUserDao;
import com.shmashine.pm.api.dao.TblSysUserDao;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.module.user.input.SearchUserListModule;
import com.shmashine.pm.api.service.dept.BizDeptService;
import com.shmashine.pm.api.service.user.BizUserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BizUserServiceImpl implements BizUserService {

    @Autowired
    private TblSysUserDao tblSysUserDao;

    @Autowired
    private BizUserDao userDao;

    @Autowired
    private BizDeptService bizDeptService;

//    private TblSysUserResourceServiceI tblSysUserResourceServiceI;

    @Override
    public PageListResultEntity<HashMap> searchUserList(SearchUserListModule searchUserListModule) {

        Integer pageIndex = searchUserListModule.getPageIndex();
        Integer pageSize = searchUserListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        PageHelper.startPage(pageIndex, pageSize);

        HashMap user = userDao.getUser(searchUserListModule.userId);
        ArrayList<Object> objects = new ArrayList<>();
        //若没有下级部门
        if (searchUserListModule.getPermissionDeptIds().size() == 0) {
            objects.add(user);
            PageInfo<HashMap> hashMapPageInfo = new PageInfo(objects, pageSize);
            // 封装分页数据结构
            return new PageListResultEntity<HashMap>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
        }
        List<Map<String, Object>> maps = userDao.searchUserList(searchUserListModule);
        maps.add(user);

        PageInfo<HashMap> hashMapPageInfo = new PageInfo(maps, pageSize);
        // 封装分页数据结构
        return new PageListResultEntity<HashMap>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());

    }


    @Override
    public List<Map<String, Object>> userList(SearchUserListModule searchUserListModule) {

        Integer pageIndex = searchUserListModule.getPageIndex();
        Integer pageSize = searchUserListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        PageHelper.startPage(pageIndex, pageSize);
        return userDao.searchUserList(searchUserListModule);

    }

    @Override
    public List<String> getUserDeptIds(String deptId) {
        return userDao.getUserDeptIds(deptId);
    }

    /**
     * 获取有权限的所有部门
     *
     * @param deptId
     * @param deptType
     * @return
     */
    @Override
    public List<Map> getUserDeptSelectList(String deptId, String deptType) {
        return userDao.getUserDeptInfo(deptId, deptType);
    }

    @Override
    public HashMap getUserDetail(String userId) {
        return userDao.getUserDetail(userId);
    }

    @Override
    public String getUserPassword(String userId) {
        return userDao.getUserPassword(userId);
    }

    @Override
    public int deleteDeptUser(String userId) {
        return userDao.deleteDeptUser(userId);
    }

    /**
     * 判断用户是否是管理员
     *
     * @param userId
     * @return
     */
    @Override
    public boolean isAdmin(String userId) {
        // 1. 获取用户部门
//        JSONObject userDept = bizDeptService.getUserDept(userId);
//        String dept_id = userDept.getString("dept_id");
        String admin = userDao.isAdmin(userId);
        return admin != null ? true : false;
    }

    /**
     * 判断用户是否是管理员
     *
     * @param userId
     * @return
     */
    @Override
    public boolean isAdminOrPm(String userId) {
        // 1. 获取用户部门
//        JSONObject userDept = bizDeptService.getUserDept(userId);
//        String dept_id = userDept.getString("dept_id");
        String admin = userDao.isAdminOrPm(userId);
        return admin != null ? true : false;
    }

//    /**
//     * 授权电梯数据接口
//     * @param userElevatorPermissionModule
//     * @return
//     */
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public boolean permissionData(UserElevatorPermissionModule userElevatorPermissionModule,String operationUserId) {
//
//        // 1. 删除用户下的数据
//        String userId = userElevatorPermissionModule.getUserId();
//        try{
//            userDao.deleteUserResource(userId);
//        }catch (Exception e) {
//            throw new BizException(ResponseResult.error());
//        }
//        // 2. 批量添加 授权数据
//        List<String> resourceIds = userElevatorPermissionModule.getResourceIds();
//        ArrayList<TblSysUserResource> tblSysUserResources = new ArrayList<>();
//
//        Date date = new Date();
//        resourceIds.forEach(item -> {
//            TblSysUserResource tblSysUserResource = new TblSysUserResource();
//            tblSysUserResource.setDtModifyTime(date);
//            tblSysUserResource.setDtCreateTime(date);
//            tblSysUserResource.setVCreateUserId(operationUserId);
//            tblSysUserResource.setVModifyUserId(operationUserId);
//
//            tblSysUserResource.setVUserId(userId);
//            tblSysUserResource.setVResourceId(item);
//            tblSysUserResources.add(tblSysUserResource);
//        });
//
//        if(tblSysUserResourceServiceI.insertBatch(tblSysUserResources) != tblSysUserResources.size()) {
//            throw new BizException(ResponseResult.error());
//        }
//        // 3. 返回结果
//        return true;
//    }

    /**
     * 获取被授权电梯数据
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getUserPermissionData(String userId) {
        // 1. 查询用户 有哪些 电梯权限返回 数据
        return userDao.getUserResourceData(userId);
    }

    /**
     * 查询可见部门信息，name&id 用于校验批量添加电梯时维保公司
     *
     * @param deptId
     * @param deptType
     * @return
     */
    @Override
    public List<Map<String, String>> getUserDeptList(String deptId, String deptType) {
        return userDao.getUserDeptList(deptId, deptType);
    }

    @Override
    public List<Map<String, Object>> queryElevatorPrincipal(String elevatorId) {
        //拿到用户责任表
        return userDao.queryPrincipalByElevator(elevatorId);

    }

    @Override
    public List<Map<String, Object>> searchAllUserList(SearchUserListModule searchUserListModule) {
        Integer pageIndex = searchUserListModule.getPageIndex();
        Integer pageSize = searchUserListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

//        PageHelper.startPage(pageIndex, pageSize);
//        PageInfo<HashMap> hashMapPageInfo = new PageInfo(userDao.searchUserList(searchUserListModule), pageSize);
        return userDao.searchUserList(searchUserListModule);
    }

    @Override
    public HashMap getUser(String userId) {
        return userDao.getUser(userId);
    }

    @Override
    public HashMap getNameAndMobile(String userId) {
        return userDao.getNameAndMobile(userId);
    }
}
