package com.shmashine.api.controller.role;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.role.input.AddRoleModule;
import com.shmashine.api.module.role.input.DeleteRoleModule;
import com.shmashine.api.module.role.input.EditRoleModule;
import com.shmashine.api.module.role.input.SearchRoleListModule;
import com.shmashine.api.module.role.output.RoleFunctionListModule;
import com.shmashine.api.service.permission.PermissionService;
import com.shmashine.api.service.role.RoleService;
import com.shmashine.api.service.system.BizSequenceService;
import com.shmashine.api.service.system.TblSysRoleMenuServiceI;
import com.shmashine.api.service.system.TblSysRoleServiceI;
import com.shmashine.api.service.system.TblSysUserRoleServiceI;
import com.shmashine.api.util.PojoConvertUtil;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.entity.TblSysRole;
import com.shmashine.common.entity.TblSysRoleMenu;
import com.shmashine.common.entity.TblSysUserRole;
import com.shmashine.common.utils.SnowFlakeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 角色接口
 */
@RestController
@Slf4j
@RequestMapping("/role")
public class RoleController extends BaseRequestEntity {

    private RoleService roleService;

    private TblSysUserRoleServiceI tblSysUserRoleServiceI;

    private TblSysRoleMenuServiceI tblSysRoleMenuServiceI;

    private TblSysRoleServiceI tblSysRoleServiceI;

    private BizSequenceService bizSequenceService;

    private PermissionService permissionService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    public RoleController(RoleService roleService, TblSysUserRoleServiceI tblSysUserRoleServiceI, TblSysRoleMenuServiceI tblSysRoleMenuServiceI, TblSysRoleServiceI tblSysRoleServiceI, BizSequenceService bizSequenceService, PermissionService permissionService) {
        this.roleService = roleService;
        this.tblSysUserRoleServiceI = tblSysUserRoleServiceI;
        this.tblSysRoleMenuServiceI = tblSysRoleMenuServiceI;
        this.tblSysRoleServiceI = tblSysRoleServiceI;
        this.bizSequenceService = bizSequenceService;
        this.permissionService = permissionService;
    }

    /**
     * 获取角色列表
     *
     * @param searchRoleListModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.base.PageListResultEntity,com.shmashine.api.entity.TblSysRole#
     */
    @PostMapping("SearchRole")
    public Object SearchRole(@RequestBody SearchRoleListModule searchRoleListModule) {
        PageListResultEntity<TblSysRole> tblSysRolePageListResultEntity = roleService.searchRoleList(searchRoleListModule);
        return ResponseResult.successObj(tblSysRolePageListResultEntity);
    }

    /**
     * 获取角色（添加用户可选角色列表）
     *
     * @param searchRoleListModule
     * @return
     */
    @PostMapping("/searchRoleByvRoleId")
    public Object searchRoleByvRoleId(@RequestBody SearchRoleListModule searchRoleListModule) {
        return ResponseResult.successObj(roleService.searchRoleByvRoleId(searchRoleListModule));
    }

    /**
     * 添加角色接口
     *
     * @param addRoleModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("addRole")
    public Object addRole(@RequestBody @Valid AddRoleModule addRoleModule) {
        // 采番 部门编号

        //这里使用了事务的嵌套 warring
        String sequenceId = SnowFlakeUtils.nextStrId();
        if (!StringUtils.hasText(sequenceId)) {
            return ResponseResult.error();
        }

        // 转换类
        TblSysRole tblSysRole = PojoConvertUtil.convertPojo(addRoleModule, TblSysRole.class);
        tblSysRole.setVRoleId(sequenceId);
        tblSysRole.setVCreateid(getUserId());
        tblSysRole.setVModifyid(getUserId());
        if (tblSysRoleServiceI.insert(tblSysRole) == 0) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg4_01");
        }

        // 添加权限
        List<String> menu = addRoleModule.getMenu();
        Date date = new Date();
        List<TblSysRoleMenu> tblSysRoleMenus = new ArrayList<>();
        menu.forEach(item -> {
            TblSysRoleMenu tblSysRoleMenu = new TblSysRoleMenu();
            tblSysRoleMenu.setVRoleId(sequenceId);
            tblSysRoleMenu.setVMenuId(item);
            tblSysRoleMenu.setDtModifytime(date);
            tblSysRoleMenu.setDtCreatetime(date);
            tblSysRoleMenu.setVCreateid(super.getUserId());
            tblSysRoleMenu.setVModifyid(super.getUserId());
            tblSysRoleMenus.add(tblSysRoleMenu);
        });

        if (tblSysRoleMenuServiceI.insertBatch(tblSysRoleMenus) != tblSysRoleMenus.size()) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg4_01");
        }

        //删除缓存
        Set<String> keys = redisTemplate.keys(RedisConstants.USER_DEPT_INFO + "*");
        redisTemplate.delete(keys);

        return ResponseResult.success();
    }

    /**
     * 编辑角色接口
     *
     * @param editRoleModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("editRole")
    public Object editRole(@RequestBody @Valid EditRoleModule editRoleModule) {
        String roleId = editRoleModule.getvRoleId();
        TblSysRole tblSysRole = PojoConvertUtil.convertPojo(editRoleModule, TblSysRole.class);
        if (tblSysRoleServiceI.update(tblSysRole) == 0) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg4_02");
        }
        // 全删
        tblSysRoleMenuServiceI.deleteById(roleId);
//        if (tblSysRoleMenuServiceI.deleteById(roleId) == 0) {
//            return new ResponseResult(ResponseResult.CODE_ERROR, "msg4_02");
//        }
        // 全增
        List<String> menu = editRoleModule.getMenu();
        Date date = new Date();
        List<TblSysRoleMenu> tblSysRoleMenus = new ArrayList<>();
        menu.forEach(item -> {
            TblSysRoleMenu tblSysRoleMenu = new TblSysRoleMenu();
            tblSysRoleMenu.setVRoleId(roleId);
            tblSysRoleMenu.setVMenuId(item);
            tblSysRoleMenu.setDtModifytime(date);
            tblSysRoleMenu.setDtCreatetime(date);
            tblSysRoleMenu.setVCreateid(getUserId());
            tblSysRoleMenu.setVModifyid(getUserId());
            tblSysRoleMenus.add(tblSysRoleMenu);
        });

        if (tblSysRoleMenuServiceI.insertBatch(tblSysRoleMenus) != tblSysRoleMenus.size()) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg4_02");
        }

        return ResponseResult.success();
    }

    /**
     * 删除角色接口
     *
     * @param deleteRoleModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("deleteRole")
    public Object deleteRole(@RequestBody @Valid DeleteRoleModule deleteRoleModule) {
        String roleId = deleteRoleModule.getvRoleId();

        // 查询用户角色表 如果还有用户绑定了次角色 则不能删除
        TblSysUserRole tblSysUserRole = new TblSysUserRole();
        tblSysUserRole.setVRoleId(roleId);
        if (tblSysUserRoleServiceI.getByEntity(tblSysUserRole).size() != 0) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg4_04"));
        }

        if (tblSysRoleServiceI.deleteById(roleId) == 0) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg4_03"));
        }
        // 全删
        if (tblSysRoleMenuServiceI.deleteById(roleId) == 0) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg4_03"));
        }

        return ResponseResult.success();
    }

    /**
     * 查看角色
     *
     * @param deleteRoleModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("getRoleDetail")
    public Object getRoleDetail(@RequestBody @Valid DeleteRoleModule deleteRoleModule) {
        String roleId = deleteRoleModule.getvRoleId();
        TblSysRole roleDetail = roleService.getRoleDetail(roleId);
        List<RoleFunctionListModule> roleFunctionList = permissionService.getRoleFunctionListCheck(roleId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("functionList", roleFunctionList);
        jsonObject.put("roleInfo", roleDetail);
        return ResponseResult.successObj(jsonObject);
    }

    /**
     * 获取功能权限树
     *
     * @param
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.module.role.output.RoleFunctionListModule#
     */
    @PostMapping("getMenuList")
    public Object getMenuList() {
        List<RoleFunctionListModule> roleFunctionList = permissionService.getRoleFunctionList();
        return ResponseResult.successObj(roleFunctionList);
    }


}
