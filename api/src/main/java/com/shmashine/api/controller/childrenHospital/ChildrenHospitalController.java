package com.shmashine.api.controller.childrenHospital;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cn.dev33.satoken.annotation.SaIgnore;

import com.shmashine.api.controller.permission.LoginController;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.api.service.childrenHospital.ChildrenHospitalService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.satoken.client.SaTokenClientAppletsClient;
import com.shmashine.satoken.dto.SaResultDTO;

/**
 * 儿童医院相关
 *
 * @author jiangheng
 */
@RestController
@RequestMapping("/childrenHospital")
public class ChildrenHospitalController extends BaseRequestEntity {

    private final BizUserService bizUserService;

    private final ChildrenHospitalService childrenHospitalService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoginController loginController;

    @Resource
    private SaTokenClientAppletsClient saTokenClientAppletsClient;

    @Autowired
    public ChildrenHospitalController(BizUserService bizUserService, ChildrenHospitalService childrenHospitalService) {
        this.bizUserService = bizUserService;
        this.childrenHospitalService = childrenHospitalService;
    }

    /**
     * 儿童医院雷达图
     */
    @PostMapping("/radarChart")
    public ResponseResult radarChart(
            @RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(childrenHospitalService.getRadarChart(faultStatisticalQuantitySearchModule));
    }

    @SaIgnore
    @PostMapping("/getToken")
    public ResponseEntity getToken(@RequestParam("appkey") String appkey,
                                   @RequestParam("timestamp") String timestamp, @RequestParam("token") String token) {

        long millis = System.currentTimeMillis();

        if ((millis - 5400000) > Long.parseLong(timestamp)) {
            return new ResponseEntity("校验失败", HttpStatus.resolve(401));
        }
        String s = DigestUtils.sha1Hex(timestamp + appkey + "etyy01@Children");

        if (!s.equals(token)) {
            return new ResponseEntity("校验失败", HttpStatus.resolve(401));
        }


        SaResultDTO saResultDTO = saTokenClientAppletsClient.doLogin(appkey, "etyy01@Children");

        if (saResultDTO.getCode() == 200) {

            var authToken = Map.of("access_token", saResultDTO.getData(),
                    "token_type", "bearer",
                    "expires_in", 7200,
                    "scope", "all");
            return ResponseEntity.ok(authToken);
        }
        return ResponseEntity.ok(saResultDTO.getMsg());

    }
}
