package com.shmashine.pm.api.controller.dept;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.pm.api.contants.BusinessConstants;
import com.shmashine.pm.api.contants.SystemConstants;
import com.shmashine.pm.api.entity.TblSysDept;
import com.shmashine.pm.api.entity.TblSysDeptLogo;
import com.shmashine.pm.api.entity.base.BaseRequestEntity;
import com.shmashine.pm.api.entity.base.ResponseResult;
import com.shmashine.pm.api.module.dept.input.SearchDetpListModule;
import com.shmashine.pm.api.redis.utils.RedisUtils;
import com.shmashine.pm.api.service.dept.BizDeptService;
import com.shmashine.pm.api.service.dept.TblSysDeptLogoServiceI;
import com.shmashine.pm.api.service.system.TblSysDeptServiceI;
import com.shmashine.pm.api.service.user.BizUserService;
import com.shmashine.pm.api.util.JSONUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 客户信息接口（延用dept表）
 */
@RestController
@Slf4j
@RequestMapping("/customer")
public class CustomerController extends BaseRequestEntity {

    @Autowired
    private BizDeptService bizDeptService;
    @Autowired
    private BizUserService bizUserService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TblSysDeptLogoServiceI sysDeptLogoServiceI;
    @Autowired
    private TblSysDeptServiceI tblSysDeptServiceI;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 获取用户当前所在部门
     *
     * @param
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/getUserDept")
    public Object getUserDept() {
        return ResponseResult.successObj(bizDeptService.getUserDept(getUserId()));
    }

    /**
     * 获取部门列表
     *
     * @param searchDetpListModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.common.entity.TblSysDept#
     */
    @PostMapping("/searchCustomer")
    public Object SearchUser(@RequestBody @Valid SearchDetpListModule searchDetpListModule) {
        if (searchDetpListModule.getvParentId() == null) {
            JSONObject userDept = bizDeptService.getUserDept(getUserId());
            searchDetpListModule.setvParentId(userDept.getString("dept_id"));
        }
        return ResponseResult.successObj(bizDeptService.searchDeptList(searchDetpListModule));
    }

    /**
     * 获取下级部门
     *
     * @param searchDetpListModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.common.entity.TblSysDept#
     */
    @PostMapping("/searchCustomerParent")
    public Object searchDeptParent(@RequestBody @Valid SearchDetpListModule searchDetpListModule) {
        if (searchDetpListModule.getvParentId() == null) {
            JSONObject userDept = bizDeptService.getUserDept(getUserId());
            searchDetpListModule.setvParentId(userDept.getString("dept_id"));
        }

        searchDetpListModule.setAdminFlag(bizUserService.isAdmin(getUserId()));

        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        //获取所有部门缓存
        String key = RedisConstants.USER_DEPT_INFO + dept_id;
        List<String> results = (List<String>) redisTemplate.opsForValue().get(key);

        if (results == null) {
            results = new ArrayList<>();
            recursion(dept_id, results);
            if (!results.contains(dept_id))
                results.add(dept_id);
            redisTemplate.opsForValue().set(key, results);

        }
        searchDetpListModule.setPermissionDeptIds((ArrayList<String>) results);

        return ResponseResult.successObj(bizDeptService.searchDeptParent(searchDetpListModule));
    }

    /**
     * 添加部门
     *
     * @param tblSysDept
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/addCustomer")
    public Object addDept(@RequestBody @Valid TblSysDept tblSysDept) {

        int existed = bizDeptService.existsDeptByName(tblSysDept.getVDeptName());

        if (existed > 0) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "客户名已存在！");
        }

        String v_user_id = super.getUserId();
        // 采番 部门编号
        String dept = SnowFlakeUtils.nextStrId();

        // 创建默认部门Log
        TblSysDeptLogo tblSysDeptLog = new TblSysDeptLogo();
        tblSysDeptLog.setVLogoId(SnowFlakeUtils.nextStrId());
        tblSysDeptLog.setVDeptId(dept);
        tblSysDeptLog.setVSystemTitle(BusinessConstants.DEFAULT_SYSTEM_NAME);
        tblSysDeptLog.setVLogFileUrl(BusinessConstants.DEFAULT_SYSTEM_LOG_FILE_URL);
        sysDeptLogoServiceI.insert(tblSysDeptLog);

        // 设置唯一标识，创建人,修改人，创建时间，修改时间
        tblSysDept.setVDeptId(dept);
        tblSysDept.setVModifyid(v_user_id);
        tblSysDept.setVCreateid(v_user_id);
        TblSysDept tbsSaysDeptResult = bizDeptService.settingDeptUserAndParent(tblSysDept);

        if (tblSysDeptServiceI.insert(tbsSaysDeptResult) > 0) {
            redisUtils.deleteByPrex(RedisConstants.USER_DEPT_INFO);
            return ResponseResult.success();
        } else {
            return new ResponseResult(ResponseResult.CODE_ERROR, "msg3_01");
        }
    }

    /**
     * 编辑部门
     *
     * @param tblSysDept
     * @return #type:com.shmashine.api.entity.base.ResponseResult,com.shmashine.common.entity.TblSysDept#
     */
    @PostMapping("/editCustomer")
    public Object editDept(@RequestBody @Valid TblSysDept tblSysDept) {

        int existed = bizDeptService.existsDeptByName(tblSysDept.getVDeptName());

        if (existed > 1) {
            return new ResponseResult(ResponseResult.CODE_ERROR, "客户名已存在！");
        }

        // 设置更新人
        tblSysDept.setVModifyid(getUserId());
        if (tblSysDeptServiceI.update(tblSysDept) > 0) {
            redisUtils.deleteByPrex(RedisConstants.USER_DEPT_INFO);
            redisUtils.deleteByPrex("dept");
            return ResponseResult.success();
        }
        return new ResponseResult(ResponseResult.CODE_ERROR, "msg3_02");
    }

    /**
     * 删除部门
     *
     * @param params 部门编号
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/deleteCustomer")
    public Object deleteDept(@RequestBody String params) {
        Map<String, Object> prams = JSONUtils.jsonToMap(params);
        TblSysDept tblSysDept = new TblSysDept();
        tblSysDept.setVDeptId((String) prams.get("vDeptId"));
        tblSysDept.setIStatus(SystemConstants.DATA_ABNORMAL);
        if (tblSysDeptServiceI.update(tblSysDept) > 0) {
            redisUtils.deleteByPrex("dept");
            redisUtils.deleteByPrex(RedisConstants.USER_DEPT_INFO);
            return ResponseResult.success();
        }
        return new ResponseResult(ResponseResult.CODE_ERROR, "msg3_03");
    }

    /**
     * 批量删除部门
     *
     * @param arr 部门编号List
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/batchDeleteCustomer")
    public Object batchDeleteDept(@RequestBody @Valid @NotBlank List<String> arr) {
        // 逻辑删除
        int num = 0;
        for (int i = 0; i < arr.size(); i++) {
            TblSysDept tblSysDept = new TblSysDept();
            tblSysDept.setVDeptId(arr.get(i));
            tblSysDept.setIStatus(SystemConstants.DATA_ABNORMAL);
            if (tblSysDeptServiceI.update(tblSysDept) > 0) {
                num++;
            }
            ;
        }
        if (num == arr.size()) {
            return ResponseResult.success();
        }
        return new ResponseResult(ResponseResult.CODE_ERROR, "msg3_04");
    }

    /**
     * 获取部门详情
     *
     * @param params 部门编号
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/getCustomerDetail")
    public Object getDeptDetail(@RequestBody String params) {
        Map<String, Object> prams = JSONUtils.jsonToMap(params);
        return ResponseResult.successObj(bizDeptService.getDeptDetail((String) prams.get("vDeptId")));
    }


    /**
     * 过时接口，可用api/dept/searchDeptParent替代，删除前需与前端确认
     * 获取部门下拉框(权限限制)
     *
     * @param deptType 部门类型
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @Deprecated
    @GetMapping("/getCustomertDetailSelectList/{deptType}")
    public Object getDeptDetailSelectList(@PathVariable("deptType") String deptType) {
        // 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 1. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list
        List<Map> results = Lists.newArrayList();
        results.add(bizDeptService.getDeptInfo(dept_id));
        recursion(dept_id, results, deptType);
        return ResponseResult.successObj(results);
    }

    /**
     * 递归查询 下级部门的用户
     *
     * @param dept_id
     * @param strings
     */
    public void recursion(String dept_id, List<Map> strings, String deptType) {

        if (null != dept_id) {
            List<Map> userDeptSelectList = bizUserService.getUserDeptSelectList(dept_id, deptType);
            if (null != userDeptSelectList && userDeptSelectList.size() > 0) {
                userDeptSelectList.forEach(id -> {
                    recursion((String) id.get("v_dept_id"), strings, deptType);
                });
            }
            strings.addAll(userDeptSelectList);
        }
    }

    /**
     * 递归查询 下级部门的用户
     *
     * @param dept_id
     * @param strings
     */
    public void recursion(String dept_id, List<String> strings) {

        if (null != dept_id) {
            List<String> userDeptIds = bizUserService.getUserDeptIds(dept_id);
            if (null != userDeptIds && userDeptIds.size() > 0) {
                userDeptIds.forEach(id -> {
                    recursion(id, strings);
                });
            }
            strings.addAll(userDeptIds);
        }
    }
}
