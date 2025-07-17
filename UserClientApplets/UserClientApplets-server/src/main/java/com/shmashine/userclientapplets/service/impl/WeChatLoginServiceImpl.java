package com.shmashine.userclientapplets.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.satoken.client.SaTokenClientAppletsClient;
import com.shmashine.userclient.vo.WeChatUserUpdateReqVO;
import com.shmashine.userclientapplets.constants.RedisConstants;
import com.shmashine.userclientapplets.constants.RegexPatterns;
import com.shmashine.userclientapplets.dao.WeChatLoginDao;
import com.shmashine.userclientapplets.dto.WeChatUserDto;
import com.shmashine.userclientapplets.entity.PageListResultEntity;
import com.shmashine.userclientapplets.entity.ResponseResult;
import com.shmashine.userclientapplets.entity.User;
import com.shmashine.userclientapplets.entity.WeChatUser;
import com.shmashine.userclientapplets.service.UserService;
import com.shmashine.userclientapplets.service.WeChatLoginService;
import com.shmashine.userclientapplets.utils.ChangeToPinYin;
import com.shmashine.userclientapplets.utils.RegexUtils;
import com.shmashine.userclientapplets.utils.SmsUtils;

/**
 * 微信登录服务实现
 */
@Service
public class WeChatLoginServiceImpl extends ServiceImpl<WeChatLoginDao, WeChatUser> implements WeChatLoginService {

    private static final String KEY_PREFIX = "user:verify:code:";

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private ChangeToPinYin changeToPinYin;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Resource
    private SaTokenClientAppletsClient saTokenClientAppletsClient;

    @Override
    public ResponseEntity login(String jsCode, String appName) {

        //微信认证
        JSONObject fastJson = getOpenId(jsCode, appName);

        String errcode = fastJson.getString("errcode");

        if (errcode != null) {
            return new ResponseEntity("请重新授权登录", HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
        }

        String openid = fastJson.getString("openid");

        //获取用户信息
        WeChatUser weChatUser = getOne(new QueryWrapper<WeChatUser>()
                .select("user_id", "pass_word", "is_register", "role")
                .eq("open_id", openid).eq("is_deleted", 0));

        if (weChatUser == null) {
            return new ResponseEntity("用户不存在，请先注册", HttpStatus.PRECONDITION_REQUIRED);
        }

        if (weChatUser.getIsRegister() != 1) {
            return new ResponseEntity("请联系管理员确认权限", HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
        }

        //平台登录
        String token = StpUtil.login(weChatUser.getUserId());

        //刷新权限
        saTokenClientAppletsClient.reloadPermission(weChatUser.getUserId());

        /*JSONObject result = JSONUtil.createObj().set("access_token", token).set("refresh_token", null)
                .set("scope", "all").set("token_type", "bearer").set("expires_in", 7200);
        //用户id
        result.put("userId", weChatUser.getUserId());
        //角色
        result.put("role", weChatUser.getRole());*/

        var result = Map.of("access_token", token,
                "scope", "all",
                "token_type", "bearer",
                "expires_in", 7200,
                "userId", weChatUser.getUserId(),
                "role", weChatUser.getRole());
        return ResponseEntity.ok(result);
    }

    @Override
    @Deprecated
    public ResponseEntity loginForOutService(String jsCode, String appName) {
        JSONObject fastJson = getOpenId(jsCode, appName);

        String errcode = fastJson.getString("errcode");

        if (errcode != null) {
            return new ResponseEntity("请重新授权登录", HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
        }

        String openid = fastJson.getString("openid");

        //获取用户信息
        WeChatUser weChatUser = getOne(new QueryWrapper<WeChatUser>()
                .select("user_id", "pass_word", "is_register", "role")
                .eq("open_id", openid).eq("is_deleted", 0));

        if (weChatUser == null) {
            return new ResponseEntity("用户不存在，请先注册", HttpStatus.PRECONDITION_REQUIRED);
        }

        //平台登录
        String token = StpUtil.login(weChatUser.getUserId());

        //刷新权限
        saTokenClientAppletsClient.reloadPermission(weChatUser.getUserId());

        /*JSONObject result = JSONUtil.createObj().set("access_token", token).set("refresh_token", null)
                .set("scope", "all").set("token_type", "bearer").set("expires_in", 7200);

        //用户id
        result.set("userId", weChatUser.getUserId());
        //角色
        result.set("role", weChatUser.getRole());*/
        var result = Map.of("access_token", token,
                "scope", "all",
                "token_type", "bearer",
                "expires_in", 7200,
                "userId", weChatUser.getUserId(),
                "role", weChatUser.getRole());
        return ResponseEntity.ok(result);
    }

    /**
     * 获取openid
     *
     * @param jsCode  校验码
     * @param appName 小程序name
     */
    @Override
    public ResponseEntity getOpenid(String jsCode, String appName) {
        JSONObject fastJson = getOpenId(jsCode, appName);

        String errcode = fastJson.getString("errcode");

        if (errcode != null) {
            return new ResponseEntity("请重新授权登录", HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
        }

        String openid = fastJson.getString("openid");

        JSONObject result = new JSONObject();

        //openid
        result.put("openid", openid);

        return ResponseEntity.ok(result);
    }

    /**
     * 获取微信用户电话号码
     *
     * @param appName 小程序name
     * @param code    校验码
     */
    @Override
    public ResponseEntity getPhoneNumber(String appName, String code) {

        String tokenUrl = null;

        String appid;
        String secret;

        switch (appName) {
            case "weibao":      //维保小程序
                appid = "wx554845dcb4bed2b3";
                secret = "2d109c221b2ca42efa6a71b37470595a";
                break;
            case "pm":
                appid = "wx3498666fc2571a81";
                secret = "410c6c0d31a5f46723cc186d20e44f66";
                break;
            case "mansen":
                appid = "wx4cb4095f8b764a04";
                secret = "9f9112c7fcc5df0c3ec3be06c1847a86";
                break;
            case "property":
                appid = "wx3cbaa5053a5df140";
                secret = "5b85e0f4f10b0ed7fa680e994ec19eb9";
                break;
            default:        //默认物业小程序
                appid = "wx3c5622b60687279d";
                secret = "9e39a462c9ac0e2b521d0157c5edc2c3";
        }

        tokenUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type="
                + "client_credential&appid=%s&secret=%s", appid, secret);
        JSONObject token = JSONObject.parseObject(HttpUtil.createGet(tokenUrl).execute().body());
        if (token == null) {
            return new ResponseEntity("获取token失败", HttpStatus.PRECONDITION_REQUIRED);
        }
        String accessToken = token.getString("access_token");
        if (accessToken == null) {
            return new ResponseEntity("获取token失败", HttpStatus.PRECONDITION_REQUIRED);
        }

        String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken;

        HashMap<String, Object> param = new HashMap<>();
        param.put("code", code);
        String jsonString = JSONObject.toJSONString(param);
        String wxResult = HttpUtil.createPost(url).contentType("application/json").body(jsonString).execute().body();

        JSONObject result = JSONObject.parseObject(wxResult);

        return ResponseEntity.ok(result);
    }

    private JSONObject getOpenId(String jsCode, String appName) {

        String appid;
        String secret;

        switch (appName) {
            case "weibao":      //维保小程序
                appid = "wx554845dcb4bed2b3";
                secret = "2d109c221b2ca42efa6a71b37470595a";
                break;
            case "pm":
                appid = "wx3498666fc2571a81";
                secret = "410c6c0d31a5f46723cc186d20e44f66";
                break;
            case "mansen":
                appid = "wx4cb4095f8b764a04";
                secret = "9f9112c7fcc5df0c3ec3be06c1847a86";
                break;
            case "property":
                appid = "wx3cbaa5053a5df140";
                secret = "5b85e0f4f10b0ed7fa680e994ec19eb9";
                break;
            default:        //默认物业小程序
                appid = "wx3c5622b60687279d";
                secret = "9e39a462c9ac0e2b521d0157c5edc2c3";
        }

        //请求微信认证服务
        String url = "https://api.weixin.qq.com/sns/jscode2session"
                + "?appid=" + appid
                + "&secret=" + secret
                + "&js_code=" + jsCode
                + "&grant_type=authorization_code";

        //发送get请求并接收响应数据
        String body = HttpUtil.createGet(url).execute().body();

        return JSONObject.parseObject(body);
    }

    @Override
    public ResponseEntity sendVerifyCode(String phoneNum) {
        //判断请求参数是否正确
        if (!RegexUtils.isPhone(phoneNum)) {
            return new ResponseEntity("手机号格式不正确", HttpStatus.valueOf(400));
        }
        try {
            //生成随机6位数
            String code = String.valueOf((int) (((Math.random() * 9) + 1) * 100000));
            //存入redis
            redisTemplate.opsForValue().set(KEY_PREFIX + phoneNum, code, 90L, TimeUnit.SECONDS);

            //发送短信
            smsUtils.sendVerifyCode(phoneNum, code);
        } catch (Exception e) {
            return new ResponseEntity("请重新获取", HttpStatus.valueOf(400));
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity register(WeChatUserDto weChatUserDto) {

        //从redis取出验证码，判断验证码是否正确
        String key = KEY_PREFIX + weChatUserDto.getPhoneNumber();

        if (!redisTemplate.hasKey(key) || !redisTemplate.opsForValue().get(key).equals(weChatUserDto.getCode())) {
            return new ResponseEntity("验证码错误", HttpStatus.valueOf(400));
        }
        if (!RegexUtils.isPhone(weChatUserDto.getPhoneNumber())) {
            return new ResponseEntity("手机号格式不正确", HttpStatus.valueOf(400));
        }
        //判断用户名是否符合注册要求
        if (!weChatUserDto.getUserName().matches(RegexPatterns.USERNAME_REGEX)) {
            return new ResponseEntity("用户名为汉字、字母、数字、-、_，长度为1~16，且不能为纯数", HttpStatus.valueOf(400));
        }

        JSONObject fastJson = getOpenId(weChatUserDto.getJsCode(), weChatUserDto.getAppName());

        String errcode = fastJson.getString("errcode");

        if (errcode != null) {
            return new ResponseEntity("用户校验失败", HttpStatus.valueOf(400));
        }

        String openid = fastJson.getString("openid");

        WeChatUser exist = getOne(new QueryWrapper<WeChatUser>().select("open_id")
                .eq("open_id", openid)
                .eq("is_deleted", 0));

        if (exist != null) {
            return new ResponseEntity("微信号已注册", HttpStatus.valueOf(400));
        }

        WeChatUser weChatUser = Convert.convert(WeChatUser.class, weChatUserDto);
        weChatUser.setUserId(changeToPinYin.getStringPinYin(weChatUser.getUserName()) + "_" + openid);
        weChatUser.setId(IdUtil.createSnowflake(1, 1).nextIdStr());
        weChatUser.setOpenId(openid);
        weChatUser.setAppName(weChatUserDto.getAppName());
        weChatUser.setCreateTime(new Date());

        //存入数据库
        try {
            save(weChatUser);
        } catch (Exception e) {
            /**正常情况下前端会先做一次异步校验*/
            return new ResponseEntity("注册失败请重试", HttpStatus.valueOf(400));
        }

        return ResponseEntity.ok().build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity registerAndBindUser(WeChatUserDto weChatUserDto) {
        //从redis取出验证码，判断验证码是否正确
        String key = KEY_PREFIX + weChatUserDto.getPhoneNumber();

        if (!redisTemplate.hasKey(key) || !redisTemplate.opsForValue().get(key).equals(weChatUserDto.getCode())) {
            return new ResponseEntity("验证码错误", HttpStatus.valueOf(400));
        }
        if (!RegexUtils.isPhone(weChatUserDto.getPhoneNumber())) {
            return new ResponseEntity("手机号格式不正确", HttpStatus.valueOf(400));
        }
        //判断用户名是否符合注册要求
        if (!weChatUserDto.getUserName().matches(RegexPatterns.USERNAME_REGEX)) {
            return new ResponseEntity("用户名为汉字、字母、数字、-、_，长度为1~16，且不能为纯数", HttpStatus.valueOf(400));
        }

        User user = userService.findByMobile(weChatUserDto.getPhoneNumber());

        if (user == null) {
            return new ResponseEntity("该号码未在系统导入，请联系管理员", HttpStatus.NO_CONTENT);
        }

        JSONObject fastJson = getOpenId(weChatUserDto.getJsCode(), weChatUserDto.getAppName());

        String errcode = fastJson.getString("errcode");

        if (errcode != null) {
            return new ResponseEntity("用户校验失败", HttpStatus.valueOf(400));
        }

        String openid = fastJson.getString("openid");

        WeChatUser exist = getOne(new QueryWrapper<WeChatUser>().select("open_id")
                .eq("open_id", openid)
                .eq("is_deleted", 0));

        if (exist != null) {
            return new ResponseEntity("微信号已注册", HttpStatus.valueOf(400));
        }

        WeChatUser weChatUser = Convert.convert(WeChatUser.class, weChatUserDto);
        weChatUser.setUserId(changeToPinYin.getStringPinYin(weChatUser.getUserName()) + "_" + openid);
        weChatUser.setId(IdUtil.createSnowflake(1, 1).nextIdStr());
        weChatUser.setOpenId(openid);
        weChatUser.setAppName(weChatUserDto.getAppName());
        weChatUser.setCreateTime(new Date());
        weChatUser.setIsRegister(1);

        //存入数据库
        try {
            save(weChatUser);
            userService.updateUser(weChatUser.getOpenId(), weChatUserDto.getPhoneNumber(), weChatUser.getUserId());
        } catch (Exception e) {
            /**正常情况下前端会先做一次异步校验*/
            return new ResponseEntity("注册失败请重试", HttpStatus.valueOf(400));
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseResult queryWeChatUser(Integer pageIndex, Integer pageSize, String userId, String userName,
                                          String phoneNumber, String role, String appName, Integer isRegister) {

        var eq = new QueryWrapper<WeChatUser>()
                .select("id", "user_id", "user_name", "phone_number", "gender", "role", "is_register", "village",
                        "create_time", "app_name", "comment", "role_id", "dept_id",
                        "push_battery_car", "push_trapped_people", "push_battery_car_time")
                .like(StringUtils.isNoneEmpty(userId), "user_id", userId)
                .like(StringUtils.isNoneEmpty(userName), "user_name", userName)
                .like(StringUtils.isNoneEmpty(appName), "app_name", appName)
                .eq(StringUtils.isNoneEmpty(phoneNumber), "phone_number", phoneNumber)
                .eq(StringUtils.isNoneEmpty(role), "role", role)
                .eq(isRegister != null, "is_register", isRegister)
                .eq("is_deleted", 0)
                .orderByDesc("create_time");
        PageHelper.startPage(pageIndex, pageSize);
        var info = new PageInfo<>(list(eq), pageSize);
        return new ResponseResult("200", "success",
                new PageListResultEntity<>(pageIndex, pageSize, info.getTotal(), info.getList()));

    }

    //CHECKSTYLE:OFF
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult updateWeChatUserInfo(WeChatUserUpdateReqVO user, String requestUserId) {

        WeChatUser weChatUser = getById(user.getId());

        if (StringUtils.isNotBlank(user.getUserId())) {
            weChatUser.setUserId(user.getUserId());
        }
        if (StringUtils.isNotBlank(user.getUserName())) {
            weChatUser.setUserName(user.getUserName());
        }
        if (user.getGender() != null) {
            weChatUser.setGender(user.getGender());
        }
        if (StringUtils.isNotBlank(user.getRole())) {
            weChatUser.setRole(user.getRole());
        }
        if (StringUtils.isNotBlank(user.getComment())) {
            weChatUser.setComment(user.getComment());
        }
        if (StringUtils.isNotBlank(user.getPhoneNumber())) {
            weChatUser.setPhoneNumber(user.getPhoneNumber());
        }
        if (user.getIsRegister() != null) {
            weChatUser.setIsRegister(user.getIsRegister());
        }
        if (user.getIsDeleted() != null) {
            weChatUser.setIsDeleted(user.getIsDeleted());
        }
        if (StringUtils.isNotBlank(user.getRoleId())) {
            weChatUser.setRoleId(user.getRoleId());
        }
        if (StringUtils.isNotBlank(user.getDeptId())) {
            weChatUser.setDeptId(user.getDeptId());
        }
        if (user.getPushBatteryCar() != null) {
            weChatUser.setPushBatteryCar(user.getPushBatteryCar());
        }
        if (user.getPushBatteryCarTime() != null) {
            weChatUser.setPushBatteryCarTime(user.getPushBatteryCarTime());
        }
        if (user.getPushTrappedPeople() != null) {
            weChatUser.setPushTrappedPeople(user.getPushTrappedPeople());
        }

        //用户注册授权
        if (user.getIsRegister() != null && user.getIsRegister() == 1) {

            // 1. 查找用户是否存在
            if (baseMapper.getUserById(weChatUser.getUserId()) > 0) {
                return new ResponseResult("400", "用户已存在，请先确认用户id！");
            }

            // 2. 增加记录到 用户表
//            String password = StringUtils.isBlank(weChatUser.getPassWord())
//                    ? String.valueOf((int) (((Math.random() * 9) + 1) * 100000))
//                    : weChatUser.getPassWord();

//            weChatUser.setPassWord(EncodeUtil.cryptPasswordEncoder(password));
            Date date = new Date();
            weChatUser.setCreateTime(date);
            weChatUser.setModifyTime(date);
            if (baseMapper.insertUser(weChatUser) == 0) {
                return new ResponseResult("400", "添加用户失败！");
            }

//            weChatUser.setPassWord(password);
            weChatUser.setIsRegister(1);
        }

        //删除用户
        if (user.getIsDeleted() != null && user.getIsDeleted() == 1) {
            //移除授权电梯
            baseMapper.removeElevatorByuserId(weChatUser.getUserId());
            //删除用户
            baseMapper.removeUserById(weChatUser.getUserId());
            //退出登录
            StpUtil.logout(weChatUser.getUserId());

            //删除收藏电梯
            String key = RedisConstants.MAIXIN_WUYE_MINI_PROGRAM_USER_COLLECTION_ELEVATOR + weChatUser.getUserId();
            redisTemplate.delete(key);
        }

        //用户电梯授权
        if (user.getElevatorIds() != null && user.getElevatorIds().size() > 0) {

            //用户是否授权
            if (baseMapper.getUserById(weChatUser.getUserId()) == 0) {
                return new ResponseResult("400", "请先授权用户！");
            }

            //移除授权电梯
            baseMapper.removeElevatorByuserId(weChatUser.getUserId());

            //授权电梯
            baseMapper.addUserResource(weChatUser.getUserId(), user.getElevatorIds(), requestUserId);

        }

        //修改微信用户状态
        updateById(weChatUser);

        // 插入部门用户
        if (weChatUser.getDeptId() != null) {
            if (baseMapper.getDeptByUser(weChatUser) > 0) {
                baseMapper.updateDeptInfo(weChatUser);
            } else {
                baseMapper.insertDeptUser(weChatUser);
            }
        }


        // 插入角色用户
        if (weChatUser.getRoleId() != null) {
            if (baseMapper.getRoleByUser(weChatUser) > 0) {
                baseMapper.updateRoleInfo(weChatUser);
            } else {
                baseMapper.insertUserRole(weChatUser);
            }
        }


        return new ResponseResult("0000", "修改成功！");
    }  //CHECKSTYLE:ON

    @Override
    public String getOpenIdByPhoneNumAndType(String phoneNumber, String type) {

        WeChatUser weChatUser = getOne(new QueryWrapper<WeChatUser>()
                .select("open_id")
                .eq("phone_number", phoneNumber).eq("app_name", type).eq("is_deleted", 0));

        if (weChatUser == null) {
            return null;
        }

        return weChatUser.getOpenId();
    }

    @Override
    public WeChatUser getUnionIdByPhoneNum(String phoneNumber) {

        return getOne(new QueryWrapper<WeChatUser>()
                .eq("phone_number", phoneNumber).eq("is_deleted", 0)
                .orderByDesc("union_id").last("LIMIT 1"));
    }

    @Override
    public Boolean insert(WeChatUser user) {
        return save(user);
    }

    @Override
    public List<WeChatUser> getPushBatteryCarUsers(int hour) {
        return list(new QueryWrapper<WeChatUser>()
                .eq("push_battery_car_time", hour)
                .eq("push_battery_car", 1)
                .eq("is_deleted", 0)
                .groupBy("phone_number"));
    }

}
