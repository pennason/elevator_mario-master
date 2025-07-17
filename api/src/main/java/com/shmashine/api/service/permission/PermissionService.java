package com.shmashine.api.service.permission;

import java.util.List;

import com.shmashine.api.module.login.output.ResponsePermission;
import com.shmashine.api.module.role.output.RoleFunctionListModule;

/**
 * 权限业务处理接口 TODO 这里可以加缓存
 */
public interface PermissionService {
    /**
     * 获取用户权限
     */
    List<ResponsePermission> getPermission(String userId);

    /**
     * 获取用户权限
     */
    List<ResponsePermission> getPermissionV2(String userId);

    /**
     * 获取角色权限树
     */
    List<RoleFunctionListModule> getRoleFunctionListCheck(String roleId);

    /**
     * 获取权限树
     */
    List<RoleFunctionListModule> getRoleFunctionList();
}
