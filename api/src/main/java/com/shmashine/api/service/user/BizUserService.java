package com.shmashine.api.service.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.api.controller.user.VO.SearchOutServiceUserListRespVO;
import com.shmashine.api.controller.user.VO.SearchUserListRespVO;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.login.input.LoginInfoRequest;
import com.shmashine.api.module.user.input.SearchUserListModule;
import com.shmashine.api.module.user.input.UserElevatorPermissionModule;
import com.shmashine.api.module.user.input.UserElevatorPermissionUpdateModule;
import com.shmashine.common.entity.TblSysRole;
import com.shmashine.common.entity.TblSysUser;

public interface BizUserService {

    /**
     * 查询用户数据
     */
    PageListResultEntity<SearchUserListRespVO> searchUserList(SearchUserListModule searchUserListModule);

    /**
     * 查询外协用户数据
     */
    PageListResultEntity<SearchOutServiceUserListRespVO> searchOutServiceUserList(SearchUserListModule searchUserListModule);

    /**
     * 查询用户数据
     */
    List<SearchUserListRespVO> userList(SearchUserListModule searchUserListModule);

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
    TblSysUser getUserDetail(String userId);

    /**
     * 根据手机号查看用户详情
     */
    HashMap getUserDetailByMobile(String mobileNum);

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
     * 授权电梯数据
     */
    boolean permissionData(UserElevatorPermissionModule userElevatorPermissionModule, String operationUserId);

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
    List<SearchUserListRespVO> searchAllUserList(SearchUserListModule searchUserListModule);

    /**
     * 查询用户
     *
     * @param userId
     * @return
     */
    HashMap getUser(String userId);

    List<TblSysUser> getListByRoleEntity(TblSysRole tblSysRole);

    /**
     * 获取历史用户登录信息
     *
     * @param loginInfoRequest
     * @return
     */
    PageListResultEntity getLoginInfo(LoginInfoRequest loginInfoRequest);

    ResponseResult authorizeAllEle(String userId, String superUserId);

    /**
     * 根据用户appkey获取用户登录信息
     *
     * @param appkey
     * @return
     */
    HashMap<String, String> getAppSecretByAppkey(String appkey);

    /**
     * 用户电梯数据授权更新
     *
     * @param userElevatorPermissionUpdateModule
     * @param userId
     * @return
     */
    boolean userElevatorPermissionUpdate(UserElevatorPermissionUpdateModule userElevatorPermissionUpdateModule, String userId);
}
