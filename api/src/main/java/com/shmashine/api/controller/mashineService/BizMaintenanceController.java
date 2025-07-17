package com.shmashine.api.controller.mashineService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.shmashine.api.controller.village.VillageController;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.mashineService.MashineServiceBizMaintenanceModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.mashineService.MashineServiceBizMainteanceService;
import com.shmashine.api.service.user.BizUserService;

@RestController
@Controller
@RequestMapping("/mashineService/maintenance")
public class BizMaintenanceController extends BaseRequestEntity {

    @Autowired
    private MashineServiceBizMainteanceService mashineServiceBizMainteanceService;

    @Autowired
    private BizDeptService bizDeptService;

    @Autowired
    private VillageController villageController;

    @Autowired
    private BizUserService bizUserService;

    /**
     * 当前用户所有维保列表
     */
    @PostMapping("/list")
    @ResponseBody
    public Object list(@RequestBody MashineServiceBizMaintenanceModule mashineServiceBizMaintenanceModule) {
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        List<String> results = Lists.newArrayList();
        results.add(dept_id);
        villageController.recursion(dept_id, results);
        // 4. 查找小区 级联找 看起来是有点lo
        mashineServiceBizMaintenanceModule.setPermissionDeptIds((ArrayList<String>) results);
        mashineServiceBizMaintenanceModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        mashineServiceBizMaintenanceModule.setUserId(getUserId());

        PageListResultEntity menu = mashineServiceBizMainteanceService.list(mashineServiceBizMaintenanceModule);
        return ResponseResult.successObj(menu);
    }
}
