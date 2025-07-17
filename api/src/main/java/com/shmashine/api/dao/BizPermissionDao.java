package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.login.output.ResponsePermission;
import com.shmashine.api.module.role.output.RoleFunctionListModule;

/**
 * 权限dao类
 */
public interface BizPermissionDao {

    /**
     * 根据用户id获取功能列表
     */
    List<ResponsePermission> getPermission(@Param("userId") String userId);


    /**
     * 根据用户id获取功能列表
     */
    List<ResponsePermission> getPermissionV2(@Param("userId") String userId);

    /**
     * 根据用户获取路由权限 0代表有权限 1代表没有权限
     */
    List<RoleFunctionListModule> getRoleFunctionListCheck(@Param("roleId") String roleId);


    /**
     * 获取所有菜单
     */
    List<RoleFunctionListModule> getRoleFunctionList();

}
