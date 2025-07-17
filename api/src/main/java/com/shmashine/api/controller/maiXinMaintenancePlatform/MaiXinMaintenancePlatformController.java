package com.shmashine.api.controller.maiXinMaintenancePlatform;

import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.EmergencyRescueOrderPageResultDTO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.EmergencyRescueOrderReqVO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.MaintenanceOrderPageReqVO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.MaintenancePageResultDTO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.RepairOrderPageResultDTO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.RepairOrderReqVO;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.util.MaiXinMaintenancePlatformUtil;

/**
 * @author  jiangheng
 * @version 2024/1/17 10:36
 * @description: 获取麦信维保平台数据接口
 */
@RestController
@RequestMapping("/maintenancePlatform")
public class MaiXinMaintenancePlatformController extends BaseRequestEntity {

    private final BizUserService bizUserService;

    private final MaiXinMaintenancePlatformUtil maiXinMaintenancePlatformUtil;

    @Autowired
    public MaiXinMaintenancePlatformController(BizUserService bizUserService, MaiXinMaintenancePlatformUtil maiXinMaintenancePlatformUtil) {
        this.bizUserService = bizUserService;
        this.maiXinMaintenancePlatformUtil = maiXinMaintenancePlatformUtil;
    }

    /**
     * 获取麦信维保平台维保单列表
     *
     * @param reqVO
     * @return
     */
    @PostMapping("/getMaintenanceOrderPage")
    public ResponseResult getMaintenanceOrderPage(@Valid @RequestBody MaintenanceOrderPageReqVO reqVO) {

        //获取用户手机号
        HashMap user = bizUserService.getUser(super.getUserId());

        //根据手机号获取客户维保单列表
        MaintenancePageResultDTO maintenanceOrders = maiXinMaintenancePlatformUtil.getMaintenanceOrderPage((String) user.get("vMobile"),
                reqVO.getStartTime(), reqVO.getEndTime(), reqVO.getOrderStatus(), reqVO.getCommunityId(), reqVO.getPageNo(), reqVO.getPageSize());

        return ResponseResult.successObj(maintenanceOrders);
    }


    /**
     * 获取维保平台维修列表
     *
     * @param reqVO
     * @return
     */
    @PostMapping("/getRepairOrderPage")
    public ResponseResult getRepairOrderPage(@Valid @RequestBody RepairOrderReqVO reqVO) {

        //获取用户手机号
        HashMap user = bizUserService.getUser(super.getUserId());

        //根据手机号获取客户维保单列表
        RepairOrderPageResultDTO repairOrders = maiXinMaintenancePlatformUtil.getRepairOrderPage((String) user.get("vMobile"),
                reqVO.getStartTime(), reqVO.getEndTime(), reqVO.getOrderStatus(), reqVO.getPageNo(), reqVO.getPageSize());

        return ResponseResult.successObj(repairOrders);
    }


    /**
     * 获取维保平台急救列表
     *
     * @param reqVO
     * @return
     */
    @PostMapping("/getEmergencyRescueOrderByPhone")
    public ResponseResult getEmergencyRescueOrderByPhone(@Valid @RequestBody EmergencyRescueOrderReqVO reqVO) {

        //获取用户手机号
        HashMap user = bizUserService.getUser(super.getUserId());

        //根据手机号获取客户维保单列表
        EmergencyRescueOrderPageResultDTO emergencyRescueOrders = maiXinMaintenancePlatformUtil.getEmergencyRescueOrderByPhone((String) user.get("vMobile"),
                reqVO.getStartTime(), reqVO.getEndTime(), reqVO.getOrderStatus(), reqVO.getPageNo(), reqVO.getPageSize());

        return ResponseResult.successObj(emergencyRescueOrders);
    }

    /**
     * 获取维保平台维保详情
     *
     * @param id
     * @return
     */
    @PostMapping("/getMaintenanceDetailById")
    public ResponseResult getMaintenanceDetailById(@RequestParam("id") String id) {

        return ResponseResult.successObj(maiXinMaintenancePlatformUtil.getMaintenanceDetailById(id));
    }

    /**
     * 根据ID获取维修单详情
     *
     * @param id
     * @return
     */
    @PostMapping("/getRepairDetailById")
    public ResponseResult getRepairDetailById(@RequestParam("id") String id) {

        return ResponseResult.successObj(maiXinMaintenancePlatformUtil.getRepairDetailById(id));
    }

    /**
     * 根据ID获取救援单详情
     *
     * @param id
     * @return
     */
    @PostMapping("/getEmergencyRescueDetailById")
    public ResponseResult getEmergencyRescueDetailById(@RequestParam("id") String id) {

        return ResponseResult.successObj(maiXinMaintenancePlatformUtil.getEmergencyRescueDetailById(id));
    }


}
