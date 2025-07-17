package com.shmashine.api.controller.verifyCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.entity.TblVerifyCode;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.redis.RedisService;
import com.shmashine.api.service.verifyCode.ITblVerifyCodeService;
import com.shmashine.common.utils.RegexUtils;
import com.shmashine.common.utils.VerifyCodeGenerateUtils;

/**
 * 验证码接口
 *
 * @author
 */
@SaIgnore
@RestController
@RequestMapping("/verifyCode")
public class TblVerifyCodeController extends BaseRequestEntity {

    @Autowired
    private ITblVerifyCodeService tblVerifyCodeService;

    @Autowired
    private RedisService redisService;

    /**
     * 获取登陆验证码
     *
     * @param tblVerifyCode
     * @return
     */
    @GetMapping("/fetchCode")
    public Object fetchCode(@RequestBody TblVerifyCode tblVerifyCode) {
        if (!RegexUtils.matchPhoneNum(tblVerifyCode.getvCode())) {
            throw new BizException(ResponseResult.error("电话号码不正确"));
        }

        if (null != redisService.getMobileFrequencyRequest(tblVerifyCode.getvMobile())) {
            throw new BizException(ResponseResult.error("该电话号码请求太频繁"));
        } else {
            redisService.setMobileFrequencyRequest(tblVerifyCode.getvMobile());
        }

        TblVerifyCode code = tblVerifyCodeService.findRecord(tblVerifyCode.getvMobile(), TblVerifyCode.LoginType);

        if (code != null && !code.isExpired() && code.isUseful()) {
            return ResponseResult.successObj(code);
        }
        code.setvMobile(tblVerifyCode.getvMobile());
        code.setvCode(VerifyCodeGenerateUtils.fetchLoginCode());

        int result = tblVerifyCodeService.insertRecord(code);

        if (result > 0) {
            return ResponseResult.successObj(code);
        }
        return ResponseResult.error("获取登陆验证码失败，稍后再试！");


    }


}
