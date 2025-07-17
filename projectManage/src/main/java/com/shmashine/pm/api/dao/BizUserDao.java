package com.shmashine.pm.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.module.user.input.SearchUserListModule;

public interface BizUserDao {
    /**
     * 获取用户详情
     */
    List<Map<String, Object>> searchUserList(SearchUserListModule searchUserListModule);

    /**
     * 获取用户详情
     */
    HashMap getUserDetail(@Param("userId") String UserId);

    /**
     * 获取用户密码
     */
    String getUserPassword(@Param("userId") String UserId);

    /**
     * 删除用户的部门
     */
    int deleteDeptUser(@Param("userId") String UserId);

    /**
     * 查询用户所属部门下级部门编码
     */
    List<String> getUserDeptIds(@Param("deptId") String deptId);

    /**
     * 查询用户所属部门下级部门编码
     */
    List<Map> getUserDeptInfo(@Param("deptId") String deptId, @Param("deptType") String deptType);

    /**
     * 判断是否是 超级管理员
     */
    String isAdmin(String userId);

    /**
     * 判断是否是 超级管理员或项目经理
     */
    String isAdminOrPm(String userId);

    /**
     * 删除用户的 电梯权限数据
     *
     * @param userId
     * @return
     */
    int deleteUserResource(String userId);

    List<String> getUserResourceData(String userId);

    /**
     * 查询可见部门信息，name&id 用于校验批量添加电梯时维保公司
     *
     * @param deptId
     * @param deptType
     * @return
     */
    List<Map<String, String>> getUserDeptList(@Param("deptId") String deptId, @Param("deptType") String deptType);

    /**
     * 查询西子电梯用户负责人
     *
     * @param elevatorId
     * @return
     */
    List<Map<String, Object>> queryPrincipalByElevator(@Param("elevatorId") String elevatorId);

    /**
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
