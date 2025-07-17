package com.shmashine.userclientapplets.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.userclient.vo.WeChatUserUpdateReqVO;
import com.shmashine.userclientapplets.dto.WeChatUserDto;
import com.shmashine.userclientapplets.entity.ResponseResult;
import com.shmashine.userclientapplets.entity.WeChatUser;

/**
 * 用户登录服务
 */
public interface WeChatLoginService extends IService<WeChatUser> {

    /**
     * 微信用户登录
     *
     * @param jsCode  登录时获取的 code
     * @param appName 小程序name
     */
    ResponseEntity login(String jsCode, String appName);


    /**
     * 登录
     *
     * @param jsCode  校验码
     * @param appName 小程序name
     */
    @Deprecated
    ResponseEntity loginForOutService(String jsCode, String appName);

    /**
     * 获取openId
     *
     * @param jsCode  校验码
     * @param appName 小程序name
     */
    ResponseEntity getOpenid(String jsCode, String appName);

    /**
     * 获取手机号
     *
     * @param appName 小程序name
     * @param code    校验码
     */
    ResponseEntity getPhoneNumber(String appName, String code);

    /**
     * 发送验证码
     *
     * @param phoneNum 手机号
     */
    ResponseEntity sendVerifyCode(String phoneNum);

    /**
     * 小程序用户注册
     */
    ResponseEntity register(WeChatUserDto weChatUser);

    /**
     * 注册绑定用户
     */
    ResponseEntity registerAndBindUser(WeChatUserDto weChatUser);

    /**
     * 搜索微信注册用户
     *
     * @param userId      用户名
     * @param phoneNumber 手机号
     * @param role        身份
     * @param appName     小程序名称
     * @param isRegister  状态
     */
    ResponseResult queryWeChatUser(Integer pageIndex, Integer pageSize, String userId, String userName,
                                   String phoneNumber, String role, String appName, Integer isRegister);

    //CHECKSTYLE:OFF

    /**
     * 微信用户 通过|拒绝|修改|删除
     */
    ResponseResult updateWeChatUserInfo(WeChatUserUpdateReqVO reqVO, String requestUserId);
    //CHECKSTYLE:ON

    /**
     * 获取openId
     *
     * @param phoneNumber 手机号
     * @param type        微信用户类型 app_name
     * @return openId
     */
    String getOpenIdByPhoneNumAndType(String phoneNumber, String type);

    /**
     * 获取unionId
     *
     * @param phoneNumber 手机号
     * @return unionId
     */
    WeChatUser getUnionIdByPhoneNum(String phoneNumber);

    /**
     * 新增用户
     *
     * @param user 用户
     */
    Boolean insert(WeChatUser user);

    /**
     * 获取需推送电瓶车统计用户
     *
     * @param hour 推送时间
     * @return 用户id列表
     */
    List<WeChatUser> getPushBatteryCarUsers(int hour);
}
