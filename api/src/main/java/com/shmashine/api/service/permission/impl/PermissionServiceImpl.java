package com.shmashine.api.service.permission.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.BizPermissionDao;
import com.shmashine.api.module.login.output.ResponsePermission;
import com.shmashine.api.module.role.output.RoleFunctionListModule;
import com.shmashine.api.service.permission.PermissionService;

import lombok.extern.slf4j.Slf4j;

/**
 * @PackageName org.sulotion.service.jurisdiction.impl
 * @ClassName PermissionServiceImpl
 * @Date 2020/3/12 14:49
 * @Author Liulifu
 * Version v1.0
 * @description 权限业务处理Service
 */
@SuppressWarnings("ALL")
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private BizPermissionDao permissionDao;


    @Autowired
    public PermissionServiceImpl(BizPermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    /**
     * @MethodName: getPermission
     * @Description: 根据用户编号获取功能树 前端路由信息
     * @Param: userId
     * @Return: List<LinkedHashMap>
     * @Author: Liulifu
     * @Date: 14:53
     **/
    @Override
    public List<ResponsePermission> getPermission(String userId) {
        List<ResponsePermission> permissions = permissionDao.getPermission(userId);
        List<ResponsePermission> menus = permissions.stream().filter(i -> i.getParentId() == null).collect(Collectors.toList());
        List<ResponsePermission> buttons = permissions.stream().filter(i -> i.getType().equals("button")).collect(Collectors.toList());
        menus.stream()
                .forEach(menu -> {
                    menu.setChildren(permissions.stream().filter(all -> menu.getFunctionId().equals(all.getParentId())).collect(Collectors.toList()));
                    menu.getChildren().stream().forEach(page -> {
                        page.setChildren(buttons.stream().filter(all -> page.getFunctionId().equals(all.getParentId())).collect(Collectors.toList()));
                    });
                });
        return menus;
    }


    /**
     * @MethodName: getPermissionV2 冗余代码
     * @Description: 根据用户编号获取功能树 前端路由信息
     * @Param: userId
     * @Return: List<LinkedHashMap>
     * @Author: depp
     * @Date: 14:53
     **/
    @Override
    public List<ResponsePermission> getPermissionV2(String userId) {

        List<ResponsePermission> permissions = permissionDao.getPermissionV2(userId);
        List<ResponsePermission> treeMenus = new ArrayList<>();

        return recurseTreeMenus(permissions, treeMenus);
    }


    /**
     * 获取角色权限树
     *
     * @param roleId
     * @return
     */
    @Override
    public List<RoleFunctionListModule> getRoleFunctionListCheck(String roleId) {

        List<RoleFunctionListModule> roleFunctionList = permissionDao.getRoleFunctionListCheck(roleId);
        List<RoleFunctionListModule> treeMenus = new ArrayList<>();

        return recurseTreeMenusForRole(roleFunctionList, treeMenus);
    }

    /**
     * 获取权限树
     *
     * @param roleId
     * @return
     */
    @Override
    public List<RoleFunctionListModule> getRoleFunctionList() {

        List<RoleFunctionListModule> roleFunctionList = permissionDao.getRoleFunctionList();
        List<RoleFunctionListModule> menus = new ArrayList<>();

        return recurseTreeMenusForRole(roleFunctionList, menus);
//        List<RoleFunctionListModule> menus = roleFunctionList.stream().filter(i -> i.getParentId() == null).collect(Collectors.toList());
//        List<RoleFunctionListModule> buttons = roleFunctionList.stream().filter(i -> i.getType().equals("button")).collect(Collectors.toList());
//
//        menus.stream()
//                .forEach(menu -> {
//                    menu.setChildren(roleFunctionList.stream().filter(all -> menu.getValue().equals(all.getParentId())).collect(Collectors.toList()));
//                    menu.getChildren().stream().forEach(page -> {
//                        page.setChildren(buttons.stream().filter(all -> page.getValue().equals(all.getParentId())).collect(Collectors.toList()));
//                    });
//                });
//        return menus;
    }

    /**
     * 递归查询树状菜单
     */
    private List<ResponsePermission> recurseTreeMenus(List<ResponsePermission> list, List<ResponsePermission> treeMenus) {
        if (null == treeMenus || treeMenus.size() == 0) {
            treeMenus = list.stream().filter(item -> item.getParentId() == null).collect(Collectors.toList());
        }

        for (ResponsePermission responsePermission : treeMenus) {
            List<ResponsePermission> permissionChildren = list.stream().filter(item ->
                    responsePermission.getFunctionId().equals(item.getParentId())
            ).collect(Collectors.toList());

            if (!permissionChildren.isEmpty()) {
                responsePermission.setChildren(permissionChildren);
                recurseTreeMenus(list, responsePermission.getChildren());
            }
        }

        return treeMenus;
    }

    /**
     * 递归查询树状菜单
     */
    private List<RoleFunctionListModule> recurseTreeMenusForRole(List<RoleFunctionListModule> list, List<RoleFunctionListModule> treeMenus) {
        if (null == treeMenus || treeMenus.size() == 0) {
            treeMenus = list.stream().filter(item -> item.getParentId() == null).collect(Collectors.toList());
        }

        for (RoleFunctionListModule responsePermission : treeMenus) {
            List<RoleFunctionListModule> permissionChildren = list.stream().filter(item ->
                    responsePermission.getValue().equals(item.getParentId())
            ).collect(Collectors.toList());

            if (!permissionChildren.isEmpty()) {
                responsePermission.setChildren(permissionChildren);
                recurseTreeMenusForRole(list, responsePermission.getChildren());
            }
        }

        return treeMenus;
    }
}
