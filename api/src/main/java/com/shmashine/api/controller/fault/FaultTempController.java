package com.shmashine.api.controller.fault;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.fault.input.SearchFaultTempModule;
import com.shmashine.api.service.camera.TblCameraImageIdentifyServiceI;
import com.shmashine.api.service.fault.BizFaultTempService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.util.MaiXinMaintenancePlatformUtil;
import com.shmashine.common.entity.TblFaultTemp;

import lombok.extern.slf4j.Slf4j;

/**
 * 故障审核
 */

@Slf4j
@RestController
@RequestMapping("faultTemp")
public class FaultTempController extends BaseRequestEntity {
    /**
     * 服务对象
     */
    @Resource
    private BizFaultTempService faultTempService;

    @Resource
    private BizUserService bizUserService;

    @Resource
    private TblCameraImageIdentifyServiceI imageIdentifyService;

    @Resource
    private MaiXinMaintenancePlatformUtil maiXinMaintenancePlatformUtil;

    /**
     * 获取故障列表 / 不文明行为列表
     */
    @RequestMapping("/list")
    public Object searchFaultList(@RequestBody SearchFaultTempModule searchFaultTempModule) {
        var userId = getUserId();
        searchFaultTempModule.setAdminFlag(bizUserService.isAdmin(userId));
        searchFaultTempModule.setUserId(userId);
        var pageListResultEntity = faultTempService.searchFaultSList(searchFaultTempModule);
        return ResponseResult.successObj(pageListResultEntity);
    }

    /**
     * 获取故障详情
     *
     * @param vFaultId 故障Id
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @RequestMapping("/getDetail")
    public Object getFaultTempDetail(@RequestBody String vFaultId) {
        Map<String, Object> faultSDetail = faultTempService.getFaultTempDetail(vFaultId);
        return ResponseResult.successObj(faultSDetail);
    }

    /**
     * 确认故障
     */
    @RequestMapping("/confirm")
    public Object confirmFault(@RequestBody TblFaultTemp tblFaultTemp) {
        tblFaultTemp.setVModifyUserId(getUserId());
        faultTempService.confirmFault(tblFaultTemp);

        //通知维保确认
        try {
            maiXinMaintenancePlatformUtil.pushEmergencyRescueConfirm(tblFaultTemp.getVFaultId());
        } catch (Exception e) {
            log.error("通知维保确认失败，e：{}", ExceptionUtils.getStackTrace(e));
        }

        return ResponseResult.success();

    }

    /**
     * 维保平台确认
     */
    @RequestMapping("/maintenanceConfirm")
    public ResponseResult maintenanceConfirm(@RequestBody TblFaultTemp tblFaultTemp) {

        tblFaultTemp.setVModifyUserId(getUserId());
        faultTempService.confirmFault(tblFaultTemp);
        return ResponseResult.success();
    }

    /**
     * 取消故障
     */
    @RequestMapping("/cancel")
    public Object cancelFault(@RequestBody TblFaultTemp tblFaultTemp) {

        faultTempService.cancelFault(tblFaultTemp);

        //通知维保取消
        try {
            maiXinMaintenancePlatformUtil.pushEmergencyRescueCancel(tblFaultTemp.getVFaultId());
        } catch (Exception e) {
            log.error("通知维保确认失败，e：{}", ExceptionUtils.getStackTrace(e));
        }

        return ResponseResult.success();
    }

    /**
     * 维保平台取消故障
     */
    @RequestMapping("/maintenanceCancel")
    public ResponseResult maintenanceCancel(@RequestBody TblFaultTemp tblFaultTemp) {
        faultTempService.cancelFault(tblFaultTemp);
        return ResponseResult.success();
    }

    /**
     * 图像识别结果反馈
     *
     * @param faultId 故障id
     * @param result  识别结果
     */
    @SaIgnore
    @GetMapping("/electromobileInElevator/")
    public void faultConfirm(@RequestParam("faultId") String faultId, @RequestParam("result") Integer result,
                             @RequestParam("type") String type) {

        // 自研电动车反馈 暂无该类型 自研的故障ID为任务ID {yyyyMMddHHmmss-elevatorCode}
        if ("electricBike".equals(type) || faultId.contains("-")) {
            log.info("自研电动车乘梯识别结果为[{}],故障id为[{}]", result, faultId);
            imageIdentifyService.confirmElectricMobileIdentify(faultId, result);
            return;
        }

        //电动车识别反馈
        if ("motorcycle".equals(type)) {
            TblFaultTemp tblFaultTemp = faultTempService.queryElectricMobileById(faultId);
            if (tblFaultTemp == null) {
                return;
            }

            //识别结果为真
            if (result > 0) {
                //确认故障
                faultTempService.confirmElectricMobileFault(tblFaultTemp);
            } else {
                //取消故障
                faultTempService.cancelElectricMobileFault(tblFaultTemp);
            }
            log.info("电动车乘梯识别结果为[{}],故障id为[{}]", result, faultId);

            return;
        }

        //困人反馈
        if ("person".equals(type)) {
            faultTempService.faultConfirm(faultId, result);
            log.info("困人识别结果为[{}],故障id为[{}]", result, faultId);
            return;
        }
    }

    /**
     * 根据故障ID 发送非平层困人短信
     */
    @SaIgnore
    @GetMapping("/sendEntrap2Message/{faultId}")
    public ResponseResult sendEntrap2Message(@PathVariable("faultId") String faultId) {
        log.info("发送非平层困人短信，故障id为[{}]", faultId);
        var res = faultTempService.sendEntrap2Message(faultId);
        return ResponseResult.successObj(res);
    }

}