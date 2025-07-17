package com.shmashine.api.controller.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.controller.permission.LoginController;
import com.shmashine.api.controller.user.VO.SearchUserListRespVO;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.login.input.ReqestLoginModule;
import com.shmashine.api.module.user.input.AddUserModule;
import com.shmashine.api.module.user.input.ChangePasswordModule;
import com.shmashine.api.module.user.input.DeleteBatchUserModule;
import com.shmashine.api.module.user.input.DeleteUserModule;
import com.shmashine.api.module.user.input.SearchUserListModule;
import com.shmashine.api.module.user.input.UserElevatorPermissionModule;
import com.shmashine.api.module.user.input.UserElevatorPermissionUpdateModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.system.TblSysDeptUserServiceI;
import com.shmashine.api.service.system.TblSysUserRoleServiceI;
import com.shmashine.api.service.system.TblSysUserServiceI;
import com.shmashine.api.service.system.TblSysUserWecomServiceI;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.util.EncodeUtil;
import com.shmashine.api.util.PojoConvertUtil;
import com.shmashine.common.constants.BusinessConstants;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.entity.TblSysDeptUser;
import com.shmashine.common.entity.TblSysRole;
import com.shmashine.common.entity.TblSysUser;
import com.shmashine.common.entity.TblSysUserRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController extends BaseRequestEntity {
    private final BizUserService bizUserService;
    private final TblSysUserServiceI tblSysUserServiceI;
    private final TblSysUserRoleServiceI tblSysUserRoleServiceI;
    private final TblSysDeptUserServiceI tblSysDeptUserServiceI;
    private final TblSysUserWecomServiceI userWecomService;
    private final BizDeptService bizDeptService;
    private final RestTemplate restTemplate;
    private final LoginController loginController;
    private final RedisTemplate redisTemplate;


    /**
     * 获取用户列表
     */
    @PostMapping("SearchUser")
    public ResponseResult searchUser(@RequestBody SearchUserListModule searchUserListModule) {

        searchUserListModule.setUserId(super.getUserId());
        // 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String deptId = userDept.getString("dept_id");
        // 1. 找到当前用户所属部门编号 查询 所有子部门

        //获取所有部门缓存
        String key = RedisConstants.USER_DEPT_INFO + deptId;

        List<String> results = (List<String>) redisTemplate.opsForValue().get(key);

        if (results == null) {
            results = new ArrayList<>();
            recursion(deptId, results);

            if (!results.contains(deptId)) {
                results.add(deptId);
            }
            redisTemplate.opsForValue().set(key, results);
        }

        //没有下级部门则返回当前用户对象
        searchUserListModule.setPermissionDeptIds((ArrayList<String>) results);
        var res = bizUserService.searchUserList(searchUserListModule);

        // 补充用户对应的企业微信信息
        userWecomService.extendWecomInfo1(res.getList());


        return ResponseResult.successObj(res);
    }

    /**
     * 获取用户列表
     */
    @PostMapping("SearchOutServiceUserList")
    public ResponseResult searchOutServiceUserList(@RequestBody SearchUserListModule searchUserListModule) {

        searchUserListModule.setUserId(super.getUserId());
        // 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String deptId = userDept.getString("dept_id");
        // 1. 找到当前用户所属部门编号 查询 所有子部门

        //获取所有部门缓存
        String key = RedisConstants.USER_DEPT_INFO + deptId;

        List<String> results = (List<String>) redisTemplate.opsForValue().get(key);

        if (results == null) {
            results = new ArrayList<>();
            recursion(deptId, results);

            if (!results.contains(deptId)) {
                results.add(deptId);
            }
            redisTemplate.opsForValue().set(key, results);
        }

        //没有下级部门则返回当前用户对象
        searchUserListModule.setPermissionDeptIds((ArrayList<String>) results);
        var res = bizUserService.searchOutServiceUserList(searchUserListModule);

        // 补充用户对应的企业微信信息
        userWecomService.extendWecomInfo2(res.getList());


        return ResponseResult.successObj(res);
    }

    private PageListResultEntity<SearchUserListRespVO> loadFormDB(SearchUserListModule searchUserListModule) {


        searchUserListModule.setUserId(super.getUserId());
        // 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String deptId = userDept.getString("dept_id");
        // 1. 找到当前用户所属部门编号 查询 所有子部门

        //获取所有部门缓存
        String key = RedisConstants.USER_DEPT_INFO + deptId;

        List<String> results = (List<String>) redisTemplate.opsForValue().get(key);

        if (results == null) {

            results = new ArrayList<>();
            recursion(deptId, results);

            if (!results.contains(deptId)) {
                results.add(deptId);
            }
            redisTemplate.opsForValue().set(key, results);

        }

        //没有下级部门则返回当前用户对象
        searchUserListModule.setPermissionDeptIds((ArrayList<String>) results);
        // 2. 判断有没有下级部门
        // 3. 拿出所有部门编号 去 查询 符合条件的用户列表
        return bizUserService.searchUserList(searchUserListModule);
    }

    /**
     * 获取用户列表——不分页
     */
    @PostMapping("SearchAllUser")
    public Object searchAllUser(@RequestBody SearchUserListModule searchUserListModule) {
        // 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String deptId = userDept.getString("dept_id");
        // 1. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list
        List<String> results = Lists.newArrayList();
        results.add(deptId);
        recursion(deptId, results);
        searchUserListModule.setPermissionDeptIds((ArrayList<String>) results);
        // 2. 判断有没有下级部门 有添加到list
        // 3. 拿出所有部门编号 去 查询 符合条件的用户列表
        var hashMapPageListResultEntity = bizUserService.searchAllUserList(searchUserListModule);
        return ResponseResult.successObj(hashMapPageListResultEntity);
    }

    /**
     * 递归查询 下级部门的用户
     */
    public void recursion(String deptId, List<String> strings) {

        if (null != deptId) {
            List<String> userDeptIds = bizUserService.getUserDeptIds(deptId);
            if (null != userDeptIds && userDeptIds.size() > 0) {
                userDeptIds.forEach(id -> {
                    recursion(id, strings);
                });
            }
            strings.addAll(userDeptIds);
        }
    }


    /**
     * 添加用户
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("addUser")
    public ResponseResult addUser(@RequestBody @Valid AddUserModule addUserModule) {

        // 1. 查找用户是否存在
        TblSysUser userTemp = tblSysUserServiceI.getById(addUserModule.getvUserId());
        if (userTemp != null) {
            log.info("用户已经存在！");
            throw new BizException(new ResponseResult(ResponseResult.CODE_VALID, "msg5_05"));
        }

        // 2. 增加记录到 用户表
        TblSysUser tblSysUser = PojoConvertUtil.convertPojo(addUserModule, TblSysUser.class);
        tblSysUser.setVPassword(EncodeUtil.BCryptPasswordEncoder(BusinessConstants.INITIAL_PASSWORD));
        tblSysUser.setIStatus(BusinessConstants.USER_NORMAL);
        if (tblSysUserServiceI.insert(tblSysUser) == 0) {
            log.info("添加用户失败！");
            throw new BizException(ResponseResult.error());
        }

        String deptId = addUserModule.getDeptId();
        String userId = addUserModule.getvUserId();

        // 3. 增加用户到用户部门表
        TblSysDeptUser tblSysDeptUser = new TblSysDeptUser();
        tblSysDeptUser.setVUserId(userId);
        tblSysDeptUser.setVDeptId(deptId);
        tblSysDeptUser.setVCreateid(super.getUserId());
        tblSysDeptUser.setVModifyid(super.getUserId());
        if (tblSysDeptUserServiceI.insert(tblSysDeptUser) == 0) {
            log.info("添加与用户到部门用户表失败！");
            throw new BizException(ResponseResult.error());
        }
        // 4. 增加一条记录到用户角色表
        TblSysUserRole tblSysUserRole = new TblSysUserRole();
        String roleId = addUserModule.getRoleId();
        tblSysUserRole.setVRoleId(roleId);
        tblSysUserRole.setVUserId(userId);
        tblSysUserRole.setVCreateid(super.getUserId());
        tblSysUserRole.setVModifyid(super.getUserId());
        if (tblSysUserRoleServiceI.insert(tblSysUserRole) == 0) {
            log.info("添加用户到角色用户表失败！");
            throw new BizException(ResponseResult.error());
        }

        //删除缓存
        Set<String> keys = redisTemplate.keys(RedisConstants.USER_INFO + "*");
        redisTemplate.delete(keys);

        redisTemplate.delete(RedisConstants.USER_DEPT_INFO + "*");

        // 5. 返回处理结果
        return ResponseResult.success();
    }


    /**
     * 编辑用户
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("editUser")
    public Object editUser(@RequestBody @Valid AddUserModule addUserModule) {
        String userId = addUserModule.getvUserId();
        // 1. 判断用户是否存在
        TblSysUser byId = tblSysUserServiceI.getById(userId);

        if (byId == null) {
            log.info("该编辑的用户不存在");
            throw new BizException(ResponseResult.error());
        }

        // 2. 删除用户角色表
        tblSysUserRoleServiceI.deleteById(userId);

        // 3. 删除部门用户表
        bizUserService.deleteDeptUser(userId);

        // 4. 添加用户角色表
        TblSysUserRole tblSysUserRole = new TblSysUserRole();
        tblSysUserRole.setVRoleId(addUserModule.getRoleId());
        tblSysUserRole.setVUserId(userId);
        tblSysUserRole.setVCreateid(super.getUserId());
        tblSysUserRole.setVModifyid(super.getUserId());

        if (tblSysUserRoleServiceI.insert(tblSysUserRole) == 0) {
            log.info("添加用户到角色用户表失败！");
            throw new BizException(ResponseResult.error());
        }

        // 5. 添加部门用户表
        TblSysDeptUser tblSysDeptUser = new TblSysDeptUser();
        tblSysDeptUser.setVUserId(userId);
        tblSysDeptUser.setVDeptId(addUserModule.getDeptId());
        tblSysDeptUser.setVCreateid(super.getUserId());
        tblSysDeptUser.setVModifyid(super.getUserId());
        if (tblSysDeptUserServiceI.insert(tblSysDeptUser) == 0) {
            log.info("添加与用户到部门用户表失败！");
            throw new BizException(ResponseResult.error());
        }

        // 6. 修改用户信息
        TblSysUser tblSysUser = PojoConvertUtil.convertPojo(addUserModule, TblSysUser.class);
        if (tblSysUserServiceI.update(tblSysUser) == 0) {
            log.info("修改用户信息失败！");
            throw new BizException(ResponseResult.error());
        }

        //清除缓存
        Set<String> keys = redisTemplate.keys(RedisConstants.USER_INFO + "*");
        redisTemplate.delete(keys);

        redisTemplate.delete(RedisConstants.USER_DEPT_INFO + "*");

        // 7. 返回结果
        return ResponseResult.success();
    }

    /**
     * 删除用户
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("deleteUser")
    public Object deleteUser(@RequestBody @Valid DeleteUserModule deleteUserModule) {
        // 1. 判断用户是否存在
        TblSysUserRole byId = tblSysUserRoleServiceI.getById(deleteUserModule.getUserId());
        if (byId == null) {
            log.info("该删除的用户不存在");
            throw new BizException(ResponseResult.error());
        }

        // 2. 修改用户为失效
        TblSysUser tblSysUser = new TblSysUser();
        tblSysUser.setIStatus(BusinessConstants.USER_INVALID);
        tblSysUser.setVUserId(deleteUserModule.getUserId());
        tblSysUser.setVModifyid(super.getUserId());

        if (tblSysUserServiceI.update(tblSysUser) == 0) {
            log.info("修改用户状态为失效失败！");
            throw new BizException(ResponseResult.error());
        }

        //删除缓存
        Set<String> keys = redisTemplate.keys(RedisConstants.USER_INFO + "*");
        redisTemplate.delete(keys);

        redisTemplate.delete(RedisConstants.USER_DEPT_INFO + "*");

        //退出登录
        StpUtil.logout(deleteUserModule.getUserId());

        // 3. 返回结果
        return ResponseResult.success();
    }

    /**
     * 批量删除用户
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("batchDeleteUser")
    public Object batchDeleteUser(@RequestBody DeleteBatchUserModule deleteBatchUserModule) {
        List<String> arr = deleteBatchUserModule.getArr();
        // 1. 修改用户为失效
        for (int i = 0; i < arr.size(); i++) {
            // 1. 判断用户是否存在
            TblSysUserRole byId = tblSysUserRoleServiceI.getById(arr.get(i));
            if (byId == null) {
                log.info("该删除的用户不存在" + arr.get(i));
                throw new BizException(ResponseResult.error());
            }

            // 2. 修改用户为失效
            TblSysUser tblSysUser = new TblSysUser();
            tblSysUser.setIStatus(BusinessConstants.USER_INVALID);
            tblSysUser.setVUserId(arr.get(i));
            tblSysUser.setVModifyid(super.getUserId());

            if (tblSysUserServiceI.update(tblSysUser) == 0) {
                log.info("修改用户状态为失效失败！");
                throw new BizException(ResponseResult.error());
            }
        }
        // 2. 返回结果
        return ResponseResult.success();
    }

    /**
     * 获取用户信息 详情
     *
     * @param deleteUserModule 公用删除参数类
     * @return Json
     */
    @PostMapping("getUserDetail")
    public ResponseResult getUserDetail(@RequestBody @Valid DeleteUserModule deleteUserModule) {
        // 1. 获取用户信息
        TblSysUser user = bizUserService.getUserDetail(deleteUserModule.getUserId());
        // 4. 返回结果
        return ResponseResult.successObj(user);
    }

    /**
     * 修改密码
     */
    @PostMapping("changeUserPassword")
    public Object changeUserPassword(@RequestBody @Valid ChangePasswordModule changePasswordModule) {
        // 1. 获取初始密码 是否正确
        String dbUserPassword = bizUserService.getUserPassword(changePasswordModule.getvUserId());
        String oldPassword = changePasswordModule.getOldPassword();

        if (!EncodeUtil.CheckEncoderContent(oldPassword, dbUserPassword)) {
            log.info("旧密码不正确！");
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg5_07");
        }

        // 2. 两次密码是否一致
        String newPassword = changePasswordModule.getNewPassword();
        String changePassword = changePasswordModule.getChangePassword();
        if (!newPassword.equals(changePassword)) {
            log.info("两次密码输入 不一致！");
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg5_08");
        }
        // 3. 修改更新密码
        TblSysUser tblSysUser = new TblSysUser();
        tblSysUser.setVUserId(changePasswordModule.getvUserId());
        tblSysUser.setVPassword(EncodeUtil.BCryptPasswordEncoder(newPassword));
        tblSysUser.setVModifyid(super.getUserId());
        if (tblSysUserServiceI.update(tblSysUser) == 0) {
            log.info("修改密码失败！");
            return ResponseResult.error();
        }
        // 4. 返回结果
        return ResponseResult.success();
    }


    /**
     * 用户电梯数据授权
     */
    @PostMapping("userElevatorPermission")
    public Object userElevatorPermission(
            @RequestBody @Valid UserElevatorPermissionModule userElevatorPermissionModule) {
        return bizUserService.permissionData(userElevatorPermissionModule, getUserId())
                ? ResponseResult.success() : ResponseResult.error();
    }


    /**
     * 用户电梯数据授权更新
     */
    @PostMapping("userElevatorPermissionUpdate")
    public Object userElevatorPermissionUpdate(
            @RequestBody @Valid UserElevatorPermissionUpdateModule userElevatorPermissionUpdateModule) {

        return bizUserService.userElevatorPermissionUpdate(userElevatorPermissionUpdateModule, getUserId())
                ? ResponseResult.success() : ResponseResult.error();
    }


    /**
     * 获取用户有哪些电梯权限
     */
    @PostMapping("getUserPermissionData")
    public Object getUserPermissionData(@RequestBody String userId) {
        return ResponseResult.successObj(bizUserService.getUserPermissionData(userId));
    }

    /**
     * 获取用户
     */
    @PostMapping("getListByRole")
    public Object getListByRoleEntity(@RequestBody TblSysRole tblSysRole) {
        return ResponseResult.successObj(bizUserService.getListByRoleEntity(tblSysRole));
    }

    @PostMapping("/authorizeAllEle/{userId}")
    public ResponseResult authorizeAllEle(@PathVariable @Valid @NotNull(message = "用户不能为空！") String userId) {

        //用户不存在
        TblSysUserRole byId = tblSysUserRoleServiceI.getById(userId);
        if (byId == null) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg1_012");
        }

        return bizUserService.authorizeAllEle(userId, super.getUserId());

    }


    @SaIgnore
    @PostMapping("/getToken")
    public ResponseEntity getToken(@RequestParam("appkey") String appkey, @RequestParam("timestamp") String timestamp,
                                   @RequestParam("token") String token) {

        long millis = System.currentTimeMillis();

        if ((millis - 5400000) > Long.parseLong(timestamp)) {
            return new ResponseEntity("校验失败", HttpStatus.resolve(401));
        }

        //获取登录用户信息
        HashMap<String, String> userInfo = bizUserService.getAppSecretByAppkey(appkey);

        String s = DigestUtils.sha1Hex(timestamp + appkey + userInfo.get("appsecret"));

        if (!s.equals(token)) {
            return new ResponseEntity("校验失败", HttpStatus.resolve(401));
        }


        MultiValueMap<String, String> mulmap = new LinkedMultiValueMap<>();

        mulmap.add("username", userInfo.get("username"));
        mulmap.add("password", userInfo.get("password"));
        mulmap.add("grant_type", "password");
        mulmap.add("scope", "all");
        mulmap.add("client_id", "client");
        mulmap.add("client_secret", "secret");

        ReqestLoginModule reqestLoginModule = new ReqestLoginModule();
        reqestLoginModule.setUserName(userInfo.get("username"));
        reqestLoginModule.setPassWord(userInfo.get("password"));

        ResponseResult responseResult = (ResponseResult) loginController.loginV2(reqestLoginModule);
        JSONObject info = (JSONObject) responseResult.getInfo();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(mulmap, headers);

        String url = "https://iot.shmashine.com/auth/oauth/token";
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        var jsonObject = JSON.parseObject(JSON.toJSONString(exchange.getBody()));
        info.put("token", jsonObject);

        return ResponseEntity.ok(info);

    }

}