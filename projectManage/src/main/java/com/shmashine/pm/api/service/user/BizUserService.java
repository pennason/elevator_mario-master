package com.shmashine.pm.api.service.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.module.user.input.SearchUserListModule;

public interface BizUserService {

    /**
     * 查询用户数据
     */
    PageListResultEntity<HashMap> searchUserList(SearchUserListModule searchUserListModule);

    /**
     * 查询用户数据
     */
    List<Map<String, Object>> userList(SearchUserListModule searchUserListModule);

    /**
     * 查询可见的子部门编号
     */
    List<String> getUserDeptIds(String deptId);

    /**
     * 查询可见的子部门信息
     */
    List<Map> getUserDeptSelectList(String deptId, String deptType);

    /**
     * 查看用户详情
     */
    HashMap getUserDetail(String userId);

    /**
     * 获取用户密码
     */
    String getUserPassword(String userId);

    /**
     * 删除部门下的用户
     */
    int deleteDeptUser(String userId);

    /**
     * 是否是管理员
     */
    boolean isAdmin(String userId);

    /**
     * 是否是管理员或项目经理
     */
    boolean isAdminOrPm(String userId);

    /** 授权电梯数据 */
//    boolean permissionData(UserElevatorPermissionModule userElevatorPermissionModule, String operationUserId);

    /**
     * 获取用户有哪些授权电梯
     */
    List<String> getUserPermissionData(String userId);

    /**
     * 查询可见部门信息，name&id 用于校验批量添加电梯时维保公司
     *
     * @param deptId
     * @param deptType
     * @return
     */
    List<Map<String, String>> getUserDeptList(String deptId, String deptType);

    /**
     * 查询用户负责人
     *
     * @param elevatorId
     * @return
     */
    List<Map<String, Object>> queryElevatorPrincipal(String elevatorId);

    /**
     * 获取所有用户
     *
     * @param searchUserListModule
     * @return
     */
    List<Map<String, Object>> searchAllUserList(SearchUserListModule searchUserListModule);

    /**
     * 查询用户
     *
     * @param userId
     * @return
     */
    HashMap getUser(String userId);

    /**
     * @param userId
     * @return
     */
    HashMap getNameAndMobile(String userId);
}
