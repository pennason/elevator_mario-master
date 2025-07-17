package com.shmashine.api.controller.mashineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.mashineService.MashineServiceBizElevatorModule;
import com.shmashine.api.service.mashineService.MashineServiceBizElevatorService;
import com.shmashine.api.service.user.BizUserService;

@RestController
@Controller
@RequestMapping("/mashineService/elevator")
public class BizElevatorController extends BaseRequestEntity {

    @Autowired
    private MashineServiceBizElevatorService mashineServiceBizElevatorService;

    @Autowired
    private BizUserService bizUserService;

    /**
     * 查询【请填写功能名称】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public Object list(@RequestBody MashineServiceBizElevatorModule mashineServiceBizElevatorModule) {

        mashineServiceBizElevatorModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        mashineServiceBizElevatorModule.setUserId(getUserId());

        PageListResultEntity menu = mashineServiceBizElevatorService.list(mashineServiceBizElevatorModule);
        return ResponseResult.successObj(menu);
    }
}
