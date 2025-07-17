package com.shmashine.api.controller.permission;

import java.security.InvalidParameterException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.entity.TblVerifyCode;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.login.input.LoginInfoRequest;
import com.shmashine.api.module.login.input.ReqestLoginModule;
import com.shmashine.api.module.login.output.ResponsePermission;
import com.shmashine.api.redis.RedisService;
import com.shmashine.api.service.dept.BizDeptLogoService;
import com.shmashine.api.service.permission.PermissionService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.user.TblSysUserAliyunServiceI;
import com.shmashine.api.service.verifyCode.ITblVerifyCodeService;
import com.shmashine.api.util.EncodeUtil;
import com.shmashine.common.constants.BusinessConstants;
import com.shmashine.common.entity.TblSysUser;
import com.shmashine.common.entity.TblSysUserAliyun;
import com.shmashine.common.utils.RegexUtils;
import com.shmashine.common.utils.SendMessageUtil;
import com.shmashine.common.utils.VerifyCodeGenerateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录接口，权限（路由信息）接口
 */
@SaIgnore
@RestController
@Slf4j
public class LoginController extends BaseRequestEntity {


    private static final String REGION = "cn-shanghai";
    private static final String PRODUCT = "CCC";
    private static final String ENDPOINT = "CCC";
    private static final String DOMAIN = "ccc.cn-shanghai.aliyuncs.com";
    private static final String VERSION = "2017-07-05";


    private PermissionService permissionService;

    private BizUserService bizUserService;

    private TblSysUserAliyunServiceI userAliyunService;

    private BizDeptLogoService bizDeptLogoService;

    private ITblVerifyCodeService tblVerifyCodeService;

    private RedisService redisService;

    @Autowired
    public LoginController(PermissionService permissionService, BizUserService bizUserService, TblSysUserAliyunServiceI userAliyunService, BizDeptLogoService bizDeptLogoService, RedisService redisService, ITblVerifyCodeService tblVerifyCodeService) {
        this.permissionService = permissionService;
        this.bizUserService = bizUserService;
        this.userAliyunService = userAliyunService;
        this.bizDeptLogoService = bizDeptLogoService;
        this.redisService = redisService;
        this.tblVerifyCodeService = tblVerifyCodeService;
    }

    /**
     * 用户获取菜单路由按钮资源数据
     *
     * @param reqestLoginModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/login")
    public Object test(@RequestBody @Valid ReqestLoginModule reqestLoginModule) {
        log.info("用户" + reqestLoginModule.getUserName() + "登录系统");
        // 1. 检验密码
        TblSysUser userDetail = bizUserService.getUserDetail(reqestLoginModule.getUserName());
        if (userDetail == null) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_03");
        }

        if (userDetail.getIStatus() == null) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_04");
        }

        String userPassword = bizUserService.getUserPassword(reqestLoginModule.getUserName());
        if (!StringUtils.hasText(userPassword)) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_04");
        }

        if (!EncodeUtil.CheckEncoderContent(reqestLoginModule.getPassWord(), userPassword)) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_02");
        }

        if (userDetail.getIStatus().equals(BusinessConstants.USER_INVALID)) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_04");
        }

        // 1. 获取菜单权限
        List<ResponsePermission> permission = permissionService.getPermission(reqestLoginModule.getUserName());
        if (permission.size() == 0) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_04");
        }
        // 2. 获取系统信息
        String deptId = userDetail.getDeptId();
        Map systemLogInfo = bizDeptLogoService.getSystemLogInfo(deptId);

        // 3. 获取用户坐席悬浮球权限
        TblSysUserAliyun userAliyun = userAliyunService.getByUserName(reqestLoginModule.getUserName());

        JSONObject resultData = new JSONObject();
        resultData.put("systemLogInfo", systemLogInfo);
        resultData.put("functionList", permission);
        resultData.put("userDetail", userDetail);
        resultData.put("userAliyun", userAliyun);
        return ResponseResult.successObj(resultData);
    }

    /**
     * 用户获取菜单路由按钮资源数据（升级版本 冗余代码）
     *
     * @param reqestLoginModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/loginV2")
    public Object loginV2(@RequestBody @Valid ReqestLoginModule reqestLoginModule) {
        log.info("用户" + reqestLoginModule.getUserName() + "登录系统");
        // 1. 检验密码
        TblSysUser userDetail = bizUserService.getUserDetail(reqestLoginModule.getUserName());
        if (userDetail == null) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_03");
        }

        if (userDetail.getIStatus() == null) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_04");
        }

        String userPassword = bizUserService.getUserPassword(reqestLoginModule.getUserName());
        if (!StringUtils.hasText(userPassword)) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_04");
        }

        if (!EncodeUtil.CheckEncoderContent(reqestLoginModule.getPassWord(), userPassword)) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_02");
        }

        if (userDetail.getIStatus().equals(BusinessConstants.USER_INVALID)) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_04");
        }

        // 1. 获取菜单权限
        List<ResponsePermission> permission = permissionService.getPermissionV2(reqestLoginModule.getUserName());
        if (permission.size() == 0) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_04");
        }
        // 2. 获取系统信息
        String deptId = userDetail.getDeptId();
        Map systemLogInfo = bizDeptLogoService.getSystemLogInfo(deptId);


        // 3. 获取用户坐席悬浮球权限
        TblSysUserAliyun userAliyun = userAliyunService.getByUserName(reqestLoginModule.getUserName());

        JSONObject resultData = new JSONObject();
        resultData.put("systemLogInfo", systemLogInfo);
        resultData.put("functionList", permission);
        resultData.put("userDetail", userDetail);
        resultData.put("userAliyun", userAliyun);
        return ResponseResult.successObj(resultData);
    }

    /**
     * 用户获取菜单路由按钮资源数据（升级版本 冗余代码）
     *
     * @param reqestLoginModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/loginByMobile")
    public Object loginByMobile(@RequestBody @Valid ReqestLoginModule reqestLoginModule) {
        log.info("用户" + reqestLoginModule.getUserName() + "登录系统");
        // 1. 检验密码
        TblSysUser userDetail = bizUserService.getUserDetail(reqestLoginModule.getUserName());

        if (userDetail == null) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_03");
        }

        if (userDetail.getIStatus() == null) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_04");
        }

        String userPassword = bizUserService.getUserPassword(reqestLoginModule.getUserName());
        if (!StringUtils.hasText(userPassword)) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_04");
        }

        if (!EncodeUtil.CheckEncoderContent(reqestLoginModule.getPassWord(), userPassword)) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_02");
        }

        if (userDetail.getIStatus().equals(BusinessConstants.USER_INVALID)) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_04");
        }

        if (userDetail.getbLoginVerifyPhone() != null && userDetail.getbLoginVerifyPhone()) {

            if (reqestLoginModule.getVerifyCode() == null) {
                if (!redisService.getMobileFrequencyRequest(userDetail.getVMobile()).equals("null")) {
                    return new ResponseResult(ResponseResult.CODE_VALID, "登陆过于频繁，请稍后再登陆");
                }
            }

            if (reqestLoginModule.getVerifyCode() == null) {
                if (userDetail.getVMobile() == null) {
                    throw new BizException(ResponseResult.error("该用户没有绑定手机"));
                }
                if (!RegexUtils.matchPhoneNum(userDetail.getVMobile())) {
                    throw new BizException(ResponseResult.error("绑定电话号码不正确"));
                }

                TblVerifyCode code = tblVerifyCodeService.findRecord(userDetail.getVMobile(), TblVerifyCode.LoginType);

                if (code != null && !code.isExpired() && code.isUseful()) {
                    redisService.setMobileFrequencyRequest(userDetail.getVMobile());
                    SendMessageUtil.sendVerifyCodeMessage(userDetail.getVMobile(), code.getvCode());

                } else {
                    code = new TblVerifyCode();

                    code.setvMobile(userDetail.getVMobile());
                    code.setvCode(VerifyCodeGenerateUtils.fetchLoginCode());
                    tblVerifyCodeService.insertRecord(code);
                    redisService.setMobileFrequencyRequest(userDetail.getVMobile());

                    SendMessageUtil.sendVerifyCodeMessage(userDetail.getVMobile(), code.getvCode());
                }

                Map<String, Object> res = new HashMap<>();
                res.put("needVerifyPhone", true);
                res.put("msg", "需要验证手机");
                return ResponseResult.successObj(res);
            } else {
                TblVerifyCode code = tblVerifyCodeService.findRecord(userDetail.getVMobile(), TblVerifyCode.LoginType);

                if (code == null) {
                    throw new BizException(ResponseResult.error("验证码错误"));
                }

                if (code.isExpired() || !code.isUseful()) {
                    throw new BizException(ResponseResult.error("验证码错误"));
                }

                if (!code.getvCode().equals(reqestLoginModule.getVerifyCode())) {
                    return new ResponseResult(ResponseResult.CODE_VALID, "验证码错误");
                } else {
                    code.setiStatus(TblVerifyCode.used);
                    tblVerifyCodeService.updateRecord(code);
                }
            }
        }

        // 1. 获取菜单权限
        List<ResponsePermission> permission = permissionService.getPermissionV2(reqestLoginModule.getUserName());
        if (permission.size() == 0) {
            return new ResponseResult(ResponseResult.CODE_VALID, "msg5_04");
        }
        // 2. 获取系统信息
        String deptId = userDetail.getDeptId();
        Map systemLogInfo = bizDeptLogoService.getSystemLogInfo(deptId);


        // 3. 获取用户坐席悬浮球权限
        TblSysUserAliyun userAliyun = userAliyunService.getByUserName(reqestLoginModule.getUserName());

        JSONObject resultData = new JSONObject();
        resultData.put("systemLogInfo", systemLogInfo);
        resultData.put("functionList", permission);
        resultData.put("userDetail", userDetail);
        resultData.put("userAliyun", userAliyun);
        return ResponseResult.successObj(resultData);
    }

    /**
     * 获取用户历史登录信息
     *
     * @param loginInfoRequest
     * @return
     */
    @SaIgnore
    @PostMapping("/getLoginInfo")
    public Object getLoginInfo(@RequestBody LoginInfoRequest loginInfoRequest) {
        return ResponseResult.successObj(bizUserService.getLoginInfo(loginInfoRequest));
    }

    /**
     * 阿里云cell center接口
     *
     * @param principal
     * @param httpServletRequest
     * @return #type: java.lang.String#
     */
    @RequestMapping(value = "/aliyun/ccc/api", method = RequestMethod.POST)
    public String call(Principal principal, HttpServletRequest httpServletRequest) {
        System.out.println(super.getUserId());
        TblSysUserAliyun user = userAliyunService.getByUserName(super.getUserId());
        String action = httpServletRequest.getParameter("action");
        String request = httpServletRequest.getParameter("request");
        return invokeApiByAk(user.getVUserName(), action, request);
    }


    /**
     * AK方式调用API
     *
     * @param loginName 登录名
     * @param action    接口名
     * @param request   请求参数
     * @return 结果
     */
    private String invokeApiByAk(String loginName, String action, String request) {
        // 使用CommonAPI调用POP API时，和使用传统产品SDK相比，请求和返回参数的格式都有所不同，因此需要进行一定的格式转换。
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setDomain(DOMAIN);
        commonRequest.setVersion(VERSION);
        commonRequest.setAction(action);
        JSONObject jsonObject = JSONObject.parseObject(request);

        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String key = entry.getKey().trim();
            if (key.length() > 1) {
                key = key.substring(0, 1).toUpperCase() + key.substring(1);
            } else if (key.length() == 1) {
                key = key.toUpperCase();
            } else {
                continue;
            }
            commonRequest.putQueryParameter(key, entry.getValue().toString());
        }

        CommonResponse response = null;
        try {
            response = getCccClient(loginName).getCommonResponse(commonRequest);
            System.out.println(JSONObject.toJSONString(response.getData()));
        } catch (ClientException e) {
            e.printStackTrace();
        }

        JSONObject jsonResult = JSONObject.parseObject(response.getData());
        JSONObject newJsonResult = new JSONObject();
        copyObject(newJsonResult, jsonResult);
        return JSONObject.toJSONString(newJsonResult);
    }


    private static void copyObject(JSONObject destination, JSONObject source) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = entry.getKey().trim();
            if (key.length() > 1) {
                key = key.substring(0, 1).toLowerCase() + key.substring(1);
            } else if (key.length() == 1) {
                key = key.toUpperCase();
            } else {
                continue;
            }
            Object object = entry.getValue();
            if (object instanceof JSONObject) {
                JSONObject tempObject = (JSONObject) object;
                if (tempObject.entrySet().size() == 1) {
                    Object theOne = tempObject.entrySet().iterator().next().getValue();
                    if (theOne instanceof JSONArray) {
                        JSONArray newArray = new JSONArray();
                        destination.put(key, newArray);
                        copyArray(newArray, (JSONArray) theOne);
                        continue;
                    }
                }
                JSONObject newObject = new JSONObject();
                destination.put(key, newObject);
                copyObject(newObject, (JSONObject) object);
            } else if (object instanceof JSONArray) {
                JSONArray newArray = new JSONArray();
                destination.put(key, newArray);
                copyArray(newArray, (JSONArray) object);
            } else {
                destination.put(key, object);
            }
        }
    }

    private static void copyArray(JSONArray destination, JSONArray source) {
        for (Object object : source) {
            if (object instanceof JSONObject) {
                JSONObject newObject = new JSONObject();
                destination.add(newObject);
                copyObject(newObject, (JSONObject) object);
            } else if (object instanceof JSONArray) {
                JSONArray newArray = new JSONArray();
                destination.add(newArray);
                copyArray(newArray, (JSONArray) object);
            } else {
                destination.add(object);
            }
        }
    }

    private synchronized IAcsClient getCccClient(String loginName) {
        DefaultAcsClient client;
        TblSysUserAliyun user = userAliyunService.getByUserName(loginName);
        client = createClient(user.getVAccessKeyId(), user.getVAccessKeySecret());
        return client;
    }

    private DefaultAcsClient createClient(String accessKeyId, String secret) {
        try {
            DefaultProfile.addEndpoint(ENDPOINT, REGION, PRODUCT, DOMAIN);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if (accessKeyId == null || secret == null) {
            throw new InvalidParameterException("ak, sk should not be null.");
        }
        IClientProfile profile = DefaultProfile.getProfile(REGION, accessKeyId, secret);
        return new DefaultAcsClient(profile);
    }

}
