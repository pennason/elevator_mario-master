package com.shmashine.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.api.controller.user.VO.SearchOutServiceUserListRespVO;
import com.shmashine.api.controller.user.VO.SearchUserListRespVO;
import com.shmashine.api.module.login.input.LoginInfoRequest;
import com.shmashine.api.module.login.output.LoginInfo;
import com.shmashine.api.module.user.input.SearchUserListModule;
import com.shmashine.common.entity.TblSysRole;
import com.shmashine.common.entity.TblSysUser;

@Mapper
public interface BizUserDao {

    /**
     * 获取用户详情
     */
    List<SearchUserListRespVO> searchUserList(SearchUserListModule searchUserListModule);

    /**
     * 获取外协用户详情
     */
    List<SearchOutServiceUserListRespVO> searchOutServiceUserList(SearchUserListModule searchUserListModule);

    /**
     * 获取用户详情
     */
    TblSysUser getUserDetail(@Param("userId") String UserId);

    /**
     * 根据用户手机号获取用户详情
     */
    HashMap getUserDetailByMobile(@Param("mobileNum") String mobileNum);

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
     * 获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    SearchUserListRespVO getSearchUserListRespVO(String userId);

    /**
     * @param userId
     * @return
     */
    SearchOutServiceUserListRespVO getOutServiceUser(String userId);

    List<TblSysUser> getListByRoleEntity(TblSysRole tblSysRole);

    /**
     * 获取历史用户登录信息
     *
     * @param loginInfoRequest
     * @return
     */
    List<LoginInfo> getLoginInfo(LoginInfoRequest loginInfoRequest);

    /**
     * 获取所有授权电梯id
     *
     * @param isAdmin
     * @param userId
     * @return
     */
    List<String> queryAllEle(@Param("isAdmin") boolean isAdmin, @Param("userId") String userId);

    /**
     * 根据用户appkey获取用户登录信息
     *
     * @param appkey
     * @return
     */
    HashMap<String, String> getAppSecretByAppkey(String appkey);
}
