package com.shmashine.satoken.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dev33.satoken.dao.SaTokenDaoRedisFastjson2;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;

import com.shmashine.satoken.dal.dto.AdminUserDO;
import com.shmashine.satoken.dal.dto.RoleMenuDO;
import com.shmashine.satoken.dal.dto.UserRoleDO;
import com.shmashine.satoken.dal.mysql.AdminUserMapper;
import com.shmashine.satoken.dal.mysql.RoleMenuMapper;
import com.shmashine.satoken.dal.mysql.UserRoleMapper;
import com.shmashine.satoken.dal.query.LambdaQueryWrapperX;
import com.shmashine.satoken.service.UserService;

/**
 * 用户服务
 *
 * @author jiangheng
 * @version v1.0.0 - 2024/3/18 16:27
 * @description: com.pj.service.impl
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private SaTokenDaoRedisFastjson2 saTokenDao;

    @Override
    public String login(String name, String pwd) {

        // 校验账号是否存在
        AdminUserDO user = adminUserMapper.selectOne(new LambdaQueryWrapperX<AdminUserDO>()
                .eq(AdminUserDO::getId, name));

        if (user == null) {
            throw new SaTokenException("用户不存在!");
        }

        if (!BCrypt.checkpw(pwd, user.getPassword().replace("{bcrypt}", ""))) {
            throw new SaTokenException("用户密码不正确!");
        }

        // 校验是否禁用
        if (user.getStatus() == 1) {
            throw new SaTokenException("用户已禁用!");
        }

        String token = StpUtil.login(user.getId());

        addUserPermission(user.getId());

        return token;
    }

    @Override
    public void reloadPermission(String userId) {
        addUserPermission(userId);
    }

    /**
     * 添加用户权限缓存
     *
     * @param userId 用户id
     */
    private void addUserPermission(String userId) {
        //获取角色列表
        List<UserRoleDO> userRoles = userRoleMapper.selectList(new LambdaQueryWrapperX<UserRoleDO>()
                .eq(UserRoleDO::getUserId, userId));
        //格式化
        List<String> roles = userRoles.stream().map(r -> r.getRoleId()).collect(Collectors.toList());
        //刷新角色列表
        saTokenDao.reloadRoleList(userId, roles);

        //获取权限列表
        List<RoleMenuDO> roleMenuDOS = roleMenuMapper.selectList(new LambdaQueryWrapperX<RoleMenuDO>()
                .inIfPresent(RoleMenuDO::getRoleId, roles));
        //格式化
        List<String> menuIds = roleMenuDOS.stream().map(r -> r.getMenuId()).collect(Collectors.toList());
        //刷新权限列表
        saTokenDao.reloadPermissionList(userId, menuIds);

    }

}
