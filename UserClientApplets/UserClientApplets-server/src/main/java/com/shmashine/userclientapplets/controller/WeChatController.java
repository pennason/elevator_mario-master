package com.shmashine.userclientapplets.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.shmashine.userclient.vo.WeChatUserUpdateReqVO;
import com.shmashine.userclientapplets.dto.WeChatUserDto;
import com.shmashine.userclientapplets.entity.BaseRequestEntity;
import com.shmashine.userclientapplets.entity.ResponseResult;
import com.shmashine.userclientapplets.service.WeChatLoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 小程序接口
 */
@RestController
@RequestMapping("/weChatLogin")
@Tag(name = "小程序登录接口", description = "小程序登录接口")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class WeChatController extends BaseRequestEntity {

    @Autowired
    private WeChatLoginService weChatLoginService;

    /**
     * 微信小程序登录
     *
     * @param jsCode 校验码
     */
    @SaIgnore
    @PostMapping("/login")
    public ResponseEntity login(@RequestParam("jsCode") String jsCode, @RequestParam("appName") String appName) {
        return weChatLoginService.login(jsCode, appName);
    }

    /**
     * 微信外协小程序登录
     *
     * @param jsCode 校验码
     */
    @SaIgnore
    @PostMapping("/loginForOutService")
    public ResponseEntity loginForOutService(@RequestParam("jsCode") String jsCode,
                                             @RequestParam("appName") String appName) {
        return weChatLoginService.login(jsCode, appName);
    }

    /**
     * 发送验证码
     *
     * @param phoneNum 手机号
     */
    @SaIgnore
    @PostMapping("/code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phoneNum) {
        return weChatLoginService.sendVerifyCode(phoneNum);
    }

    /**
     * 小程序注册
     *
     * @param weChatUser 请求体
     */
    @SaIgnore
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid WeChatUserDto weChatUser) {
        return weChatLoginService.register(weChatUser);
    }

    /**
     * 外协小程序注册
     *
     * @param weChatUser 请求体
     */
    @SaIgnore
    @PostMapping("/registerAndBindUser")
    public ResponseEntity registerAndBindUser(@RequestBody @Valid WeChatUserDto weChatUser) {
        return weChatLoginService.registerAndBindUser(weChatUser);
    }

    /**
     * 获取手机号
     *
     * @param appName 小程序名
     * @param code    校验码
     */
    @Operation(summary = "获取手机号", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getPhoneNumber")
    public ResponseEntity getPhoneNumber(@RequestParam("appName") String appName,
                                         @RequestParam("code") @NotNull String code) {
        return weChatLoginService.getPhoneNumber(appName, code);
    }

    /**
     * 搜索用户
     *
     * @param pageIndex   页码
     * @param pageSize    每页条数
     * @param userId      用户名称
     * @param userName    用户姓名
     * @param phoneNumber 手机号
     * @param role        身份
     * @param appName     小程序名称
     * @param isRegister  状态 （1：通过，2：拒绝）
     */
    @SaIgnore
    @PostMapping("/queryWeChatUser")
    public ResponseResult queryWeChatUser(@RequestParam(value = "pageIndex") Integer pageIndex,
                                          @RequestParam(value = "pageSize") Integer pageSize,
                                          @RequestParam(value = "userId", required = false) String userId,
                                          @RequestParam(value = "userName", required = false) String userName,
                                          @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                          @RequestParam(value = "role", required = false) String role,
                                          @RequestParam(value = "appName", required = false) String appName,
                                          @RequestParam(value = "isRegister", required = false) Integer isRegister) {

        return weChatLoginService.queryWeChatUser(pageIndex, pageSize, userId, userName, phoneNumber,
                role, appName, isRegister);
    }

    /**
     * 微信用户 通过|拒绝|修改|删除
     *
     * @param reqVO 请求体
     */
    @Operation(summary = "微信用户 通过|拒绝|修改|删除", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/updateWeChatUserInfo")
    public ResponseResult updateWeChatUserInfo(@RequestBody WeChatUserUpdateReqVO reqVO) {
        return weChatLoginService.updateWeChatUserInfo(reqVO, getUserId());
    }

}
