package com.shmashine.api.service.user.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.controller.user.VO.SearchOutServiceUserListRespVO;
import com.shmashine.api.controller.user.VO.SearchUserListRespVO;
import com.shmashine.api.dao.BizUserDao;
import com.shmashine.api.dao.TblSysUserDao;
import com.shmashine.api.dao.WuyeElevatorDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.login.input.LoginInfoRequest;
import com.shmashine.api.module.user.input.SearchUserListModule;
import com.shmashine.api.module.user.input.UserElevatorPermissionModule;
import com.shmashine.api.module.user.input.UserElevatorPermissionUpdateModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.system.TblSysUserResourceServiceI;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblSysRole;
import com.shmashine.common.entity.TblSysUser;
import com.shmashine.common.entity.TblSysUserResource;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BizUserServiceImpl implements BizUserService {

    private TblSysUserDao tblSysUserDao;

    private BizUserDao userDao;

    private BizDeptService bizDeptService;

    private TblSysUserResourceServiceI tblSysUserResourceServiceI;

    private WuyeElevatorDao wuyeElevatorDao;


    @Autowired
    public BizUserServiceImpl(TblSysUserDao tblSysUserDao, BizUserDao userDao, BizDeptService bizDeptService, TblSysUserResourceServiceI tblSysUserResourceServiceI, WuyeElevatorDao wuyeElevatorDao) {
        this.tblSysUserDao = tblSysUserDao;
        this.userDao = userDao;
        this.bizDeptService = bizDeptService;
        this.tblSysUserResourceServiceI = tblSysUserResourceServiceI;
        this.wuyeElevatorDao = wuyeElevatorDao;
    }

    @Override
    public PageListResultEntity<SearchUserListRespVO> searchUserList(SearchUserListModule searchUserListModule) {

        Integer pageIndex = searchUserListModule.getPageIndex();
        Integer pageSize = searchUserListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        PageHelper.startPage(pageIndex, pageSize);

        SearchUserListRespVO user = userDao.getSearchUserListRespVO(searchUserListModule.userId);
        ArrayList<Object> objects = new ArrayList<>();
        //若没有下级部门
        if (searchUserListModule.getPermissionDeptIds().size() == 0) {
            objects.add(user);
            PageInfo<SearchUserListRespVO> hashMapPageInfo = new PageInfo(objects, pageSize);
            // 封装分页数据结构
            return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(),
                    hashMapPageInfo.getList());
        }
        List<SearchUserListRespVO> users = userDao.searchUserList(searchUserListModule);
        users.add(user);

        PageInfo<SearchUserListRespVO> hashMapPageInfo = new PageInfo(users, pageSize);
        // 封装分页数据结构

        int fromIndex = pageIndex - 1 == 0 ? 0 : (pageIndex - 1) * pageSize - 1;
        int toIndex = (fromIndex + pageSize - 1) <= hashMapPageInfo.getList().size() ? (fromIndex + pageSize - 1)
                : hashMapPageInfo.getList().size() - 1;

        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(),
                hashMapPageInfo.getList().subList(fromIndex, toIndex));
    }

    @Override
    public PageListResultEntity<SearchOutServiceUserListRespVO> searchOutServiceUserList(SearchUserListModule searchUserListModule) {
        Integer pageIndex = searchUserListModule.getPageIndex();
        Integer pageSize = searchUserListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        PageHelper.startPage(pageIndex, pageSize);

        SearchOutServiceUserListRespVO user = userDao.getOutServiceUser(searchUserListModule.userId);
        ArrayList<Object> objects = new ArrayList<>();
        //若没有下级部门
        if (searchUserListModule.getPermissionDeptIds().size() == 0) {
            objects.add(user);
            PageInfo<SearchOutServiceUserListRespVO> hashMapPageInfo = new PageInfo(objects, pageSize);
            // 封装分页数据结构
            return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(),
                    hashMapPageInfo.getList());
        }

        List<SearchOutServiceUserListRespVO> maps = userDao.searchOutServiceUserList(searchUserListModule);
        maps.add(user);

        PageInfo<SearchOutServiceUserListRespVO> hashMapPageInfo = new PageInfo(maps, pageSize);
        // 封装分页数据结构

        int fromIndex = pageIndex - 1 == 0 ? 0 : (pageIndex - 1) * pageSize - 1;

        int toIndex = (fromIndex + pageSize - 1) <= hashMapPageInfo.getList().size() ? (fromIndex + pageSize - 1)
                : hashMapPageInfo.getList().size() - 1;

        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(),
                hashMapPageInfo.getList().subList(fromIndex, toIndex));
    }


    @Override
    public List<SearchUserListRespVO> userList(SearchUserListModule searchUserListModule) {

        Integer pageIndex = searchUserListModule.getPageIndex();
        Integer pageSize = searchUserListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        PageHelper.startPage(pageIndex, pageSize);
        return userDao.searchUserList(searchUserListModule);

    }

    //    @Cacheable(value = "dept" ,key = "targetClass + methodName +#p0")
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
    @Cacheable(value = "dept-map", key = "targetClass + methodName +#p0 +#p1")
    public List<Map> getUserDeptSelectList(String deptId, String deptType) {
        return userDao.getUserDeptInfo(deptId, deptType);
    }

    @Override
    public TblSysUser getUserDetail(String userId) {
        return userDao.getUserDetail(userId);
    }

    @Override
    public HashMap getUserDetailByMobile(String mobileNum) {
        return userDao.getUserDetailByMobile(mobileNum);
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
        if (StringUtils.hasText(userId)) {
            String admin = userDao.isAdmin(userId);
            return admin != null;
        }
        return false;
    }

    /**
     * 授权电梯数据接口
     *
     * @param userElevatorPermissionModule
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean permissionData(UserElevatorPermissionModule userElevatorPermissionModule, String operationUserId) {

        // 1. 删除用户下的数据
        String userId = userElevatorPermissionModule.getUserId();
        try {
            userDao.deleteUserResource(userId);
        } catch (Exception e) {
            throw new BizException(ResponseResult.error());
        }
        // 2. 批量添加 授权数据
        List<String> resourceIds = userElevatorPermissionModule.getResourceIds();
        ArrayList<TblSysUserResource> tblSysUserResources = new ArrayList<>();

        Date date = new Date();
        resourceIds.forEach(item -> {
            TblSysUserResource tblSysUserResource = new TblSysUserResource();
            tblSysUserResource.setDtModifyTime(date);
            tblSysUserResource.setDtCreateTime(date);
            tblSysUserResource.setVCreateUserId(operationUserId);
            tblSysUserResource.setVModifyUserId(operationUserId);

            tblSysUserResource.setVUserId(userId);
            tblSysUserResource.setVResourceId(item);
            tblSysUserResources.add(tblSysUserResource);
        });

        if (tblSysUserResourceServiceI.insertBatch(tblSysUserResources) != tblSysUserResources.size()) {
            throw new BizException(ResponseResult.error());
        }
        // 3. 返回结果
        return true;
    }

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
    public List<SearchUserListRespVO> searchAllUserList(SearchUserListModule searchUserListModule) {
        Integer pageIndex = searchUserListModule.getPageIndex();
        Integer pageSize = searchUserListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }


        return userDao.searchUserList(searchUserListModule);
    }

    @Override
    public HashMap getUser(String userId) {
        return userDao.getUser(userId);
    }

    @Override
    public List<TblSysUser> getListByRoleEntity(TblSysRole tblSysRole) {
        return userDao.getListByRoleEntity(tblSysRole);
    }

    public PageListResultEntity getLoginInfo(LoginInfoRequest loginInfoRequest) {
        Integer pageIndex = loginInfoRequest.getPageIndex();
        Integer pageSize = loginInfoRequest.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<HashMap> hashMapPageInfo = new PageInfo(userDao.getLoginInfo(loginInfoRequest), pageSize);
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());

    }

    /**
     * 一键授权
     *
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult authorizeAllEle(String userId, String superUserId) {

        //删除已经授权电梯
        userDao.deleteUserResource(userId);

        boolean isAdmin = false;
        //获取所有电梯
        if (StringUtils.hasText(superUserId)) {
            isAdmin = userDao.isAdmin(superUserId) != null;
        }
        List<String> resourceIds = userDao.queryAllEle(isAdmin, userId);

        //添加所有电梯授权
        ArrayList<TblSysUserResource> tblSysUserResources = new ArrayList<>();

        resourceIds.forEach(item -> {
            TblSysUserResource tblSysUserResource = new TblSysUserResource();
            tblSysUserResource.setDtModifyTime(new Date());
            tblSysUserResource.setDtCreateTime(new Date());
            tblSysUserResource.setVCreateUserId(superUserId);
            tblSysUserResource.setVModifyUserId(superUserId);

            tblSysUserResource.setVUserId(userId);
            tblSysUserResource.setVResourceId(item);
            tblSysUserResources.add(tblSysUserResource);
        });

        if (tblSysUserResourceServiceI.insertBatch(tblSysUserResources) != tblSysUserResources.size()) {
            throw new BizException(ResponseResult.error());
        }

        return ResponseResult.success();
    }

    @Override
    public HashMap<String, String> getAppSecretByAppkey(String appkey) {
        return userDao.getAppSecretByAppkey(appkey);
    }

    @Override
    public boolean userElevatorPermissionUpdate(UserElevatorPermissionUpdateModule userElevatorPermissionUpdateModule, String operationUserId) {

        String userId = userElevatorPermissionUpdateModule.getUserId();
        log.info("用户电梯授权，requestInfo:{}, userId:{}", userElevatorPermissionUpdateModule, userId);

        //1 批量删除授权数据
        List<String> removedResourceIds = new ArrayList<>();
        List<String> removedData = userElevatorPermissionUpdateModule.getRemovedData().getElevatorIds();
        removedResourceIds.addAll(removedData);

        //根据项目列表获取电梯列表
        List<String> removedProjectIds = userElevatorPermissionUpdateModule.getRemovedData().getProjectIds();
        if (removedProjectIds != null && removedProjectIds.size() > 0) {
            removedResourceIds.addAll(wuyeElevatorDao.getElevatorIdsByProjectIds(removedProjectIds));
        }

        //根据小区列表获取电梯列表
        List<String> removedVillageIds = userElevatorPermissionUpdateModule.getRemovedData().getVillageIds();
        if (removedVillageIds != null && removedVillageIds.size() > 0) {
            removedResourceIds.addAll(wuyeElevatorDao.getElevatorIdsByVillageIds(removedVillageIds));
        }

        //删除授权
        if (removedResourceIds != null && removedResourceIds.size() > 0) {
            tblSysUserResourceServiceI.deleteByUserAndResourcesId(userId, removedResourceIds.stream().distinct().collect(Collectors.toList()));
        }

        List<String> addResourceIds = new ArrayList<>();
        //根据项目列表获取电梯列表
        List<String> addProjectIds = userElevatorPermissionUpdateModule.getAddedData().getProjectIds();
        if (addProjectIds != null && addProjectIds.size() > 0) {
            addResourceIds.addAll(wuyeElevatorDao.getElevatorIdsByProjectIds(addProjectIds));
        }

        //根据小区列表获取电梯列表
        List<String> addVillageIds = userElevatorPermissionUpdateModule.getAddedData().getVillageIds();
        if (addVillageIds != null && addVillageIds.size() > 0) {
            addResourceIds.addAll(wuyeElevatorDao.getElevatorIdsByVillageIds(addVillageIds));
        }

        //电梯列表
        addResourceIds.addAll(userElevatorPermissionUpdateModule.getAddedData().getElevatorIds());

        //获取用户已经授权所有电梯列表
        List<String> userResource = tblSysUserResourceServiceI.getElevatorIdListByUserId(userId);

        if (userResource != null && userResource.size() > 0) {
            addResourceIds.removeAll(userResource);
        }

        //排序
        addResourceIds = addResourceIds.stream().distinct().collect(Collectors.toList());

        // 2. 批量添加 授权数据
        if (addResourceIds != null && addResourceIds.size() > 0) {

            ArrayList<TblSysUserResource> tblSysUserResources = new ArrayList<>();

            Date date = new Date();
            addResourceIds.forEach(item -> {
                TblSysUserResource tblSysUserResource = new TblSysUserResource();
                tblSysUserResource.setDtModifyTime(date);
                tblSysUserResource.setDtCreateTime(date);
                tblSysUserResource.setVCreateUserId(operationUserId);
                tblSysUserResource.setVModifyUserId(operationUserId);

                tblSysUserResource.setVUserId(userId);
                tblSysUserResource.setVResourceId(item);
                tblSysUserResources.add(tblSysUserResource);

            });


            if (tblSysUserResourceServiceI.insertBatch(tblSysUserResources) != tblSysUserResources.size()) {
                throw new BizException(ResponseResult.error());
            }
        }


        // 3. 返回结果
        return true;
    }
}
