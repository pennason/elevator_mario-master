package com.shmashine.api.controller.maintenanceManagement;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.service.ruijin.BizThirdPartyRuijinEnventServiceI;
import com.shmashine.api.service.user.BizUserService;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/12/3 10:31
 * <p>
 * 维保记录查询
 */
@RestController
@RequestMapping("/maintenanceManagement")
public class MaintenanceManagementController extends BaseRequestEntity {

    @Resource
    private BizUserService bizUserService;

    @Resource
    private BizThirdPartyRuijinEnventServiceI bizThirdPartyRuijinEnventServiceI;

    /**
     * 获取维修记录
     *
     * @param searchFaultModule
     * @return
     */
    @PostMapping("/queryFaultList")
    public ResponseEntity queryFaultList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(bizThirdPartyRuijinEnventServiceI.searchFaultsListWithPage(searchFaultModule));
    }

    /**
     * 获取维修记录下载
     *
     * @param searchFaultModule
     * @return
     */
    @PostMapping("/queryFaultListDownload")
    public void queryFaultListDownload(@RequestBody SearchFaultModule searchFaultModule, HttpServletResponse response) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        bizThirdPartyRuijinEnventServiceI.searchFaultListDownload(searchFaultModule, response);
    }

    /**
     * 获取维保记录
     *
     * @param searchFaultModule
     * @return
     */
    @PostMapping("/queryMaintenanceList")
    public ResponseEntity queryMaintenanceList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(bizThirdPartyRuijinEnventServiceI.searchMaintenanceList(searchFaultModule));
    }

    /**
     * 获取维保记录下载
     *
     * @param searchFaultModule
     * @return
     */
    @PostMapping("/queryMaintenanceListDownload")
    public void queryMaintenanceListDownload(@RequestBody SearchFaultModule searchFaultModule, HttpServletResponse response) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        bizThirdPartyRuijinEnventServiceI.searchMaintenanceListDownload(searchFaultModule, response);
    }

    /**
     * 获取电梯年检信息
     *
     * @param searchFaultModule
     * @return
     */
    @PostMapping("/queryAnnualInspectionList")
    public ResponseEntity queryAnnualInspectionList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(bizThirdPartyRuijinEnventServiceI.queryAnnualInspectionList(searchFaultModule));
    }

    /**
     * 获取电梯年检记录
     *
     * @param searchFaultModule
     * @return
     */
    @PostMapping("/queryRuiJinAnnualCheckList")
    public ResponseEntity queryRuiJinAnnualCheckList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(bizThirdPartyRuijinEnventServiceI.queryRuiJinAnnualCheckList(searchFaultModule));
    }

}
