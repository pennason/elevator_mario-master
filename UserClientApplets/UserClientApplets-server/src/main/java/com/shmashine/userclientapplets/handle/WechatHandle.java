package com.shmashine.userclientapplets.handle;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.userclientapplets.config.WechatOfficialAccountProperties;
import com.shmashine.userclientapplets.entity.WeChatUser;
import com.shmashine.userclientapplets.enums.WechatAppNameEnum;
import com.shmashine.userclientapplets.service.WeChatLoginService;
import com.shmashine.userclientapplets.utils.RedisUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信开发handle
 *
 * @author jiangheng
 */

@Slf4j
@Component
public class WechatHandle {

    /**
     * 获取公众号token URL
     */
    private static final String WECHAT_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    /**
     * 微信公众号关注列表
     */
    public static final String WECHAT_OFFICIAL_ACCOUNT_ATTENTIONS = "WECHAT:OFFICIAL_ACCOUNT:ATTENTIONS:";

    /**
     * 微信公众号ACCESS_TOKEN
     */
    public static final String WECHAT_OFFICIAL_ACCOUNT_ACCESS_TOKEN = "WECHAT:OFFICIAL_ACCOUNT:ACCESS_TOKEN";

    @Resource
    private WechatOfficialAccountProperties wechatOfficialAccountProperties;

    @Resource
    private WeChatLoginService weChatService;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 获取微信公众号openId
     *
     * @param phoneNumber 手机号
     * @return openId
     */
    public String getWechatOfficialAccountOpenId(String phoneNumber) {

        //获取openId
        String openId = weChatService.getOpenIdByPhoneNumAndType(phoneNumber,
                WechatAppNameEnum.GONG_ZHONG_HAO.getType());

        //小程序绑定获取
        if (StrUtil.isEmpty(openId)) {

            WeChatUser user = weChatService.getUnionIdByPhoneNum(phoneNumber);

            if (user == null) {
                return null;
            }
            String unionId = user.getUnionId();

            //根据UnionID获取 SocialUser
            openId = redisUtils.getCacheObject(WECHAT_OFFICIAL_ACCOUNT_ATTENTIONS + unionId);

            if (StrUtil.isEmpty(openId)) {

                //同步关注列表
                openId = subscriberSynchronization(unionId);

                if (StrUtil.isEmpty(openId)) {
                    return null;
                }
            }

            //添加到绑定关系中
            user.setId(IdUtil.getSnowflakeNextIdStr());
            user.setUserId(user.getId() + "_gongzhonghao");
            user.setUnionId(unionId);
            user.setOpenId(openId);
            user.setPhoneNumber(phoneNumber);
            user.setAppName(WechatAppNameEnum.GONG_ZHONG_HAO.getType());
            weChatService.insert(user);

        }

        return openId;
    }

    public String subscriberSynchronization(String unionId) {

        //获取订阅列表
        List<String> subscriberList = getSubscriberList();

        int size = redisUtils.keys(WECHAT_OFFICIAL_ACCOUNT_ATTENTIONS + "*").size();

        if (subscriberList.size() <= size) {
            return null;
        }

        //获取用户信息 新增社交用户记录
        for (String openid : subscriberList) {

            // 获取用户信息
            String id = getSocialUserUnionId(openid);

            if (unionId.equals(id)) {
                // 新增社交用户记录列表redis缓存
                redisUtils.setCacheObject(WECHAT_OFFICIAL_ACCOUNT_ATTENTIONS + id, openid);
                return openid;
            }

        }

        return null;

    }

    /**
     * 获取微信公众号关注列表
     *
     * @return 关注列表
     */
    private List<String> getSubscriberList() {

        String token = getToken();

        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + token;

        String body = HttpRequest.post(url).timeout(6000).execute().body();
        var jsonBody = JSON.parseObject(body, JSONObject.class);

        try {
            List<String> openId = jsonBody.getJSONObject("data").getList("openid", String.class);
            return openId;
        } catch (Exception e) {
            if (jsonBody.getInteger("errcode") != null && jsonBody.getInteger("errcode") == 40001) {
                redisUtils.deleteObject(WECHAT_OFFICIAL_ACCOUNT_ACCESS_TOKEN);
            }
            log.error("获取微信公众号关注列表失败，err[{}]", body);
        }

        return null;
    }

    /**
     * 获取微信公众号Access token
     *
     * @return token
     */
    private String getToken() {

        String accessToken = redisUtils.getCacheObject(WECHAT_OFFICIAL_ACCOUNT_ACCESS_TOKEN);

        if (StrUtil.isNotEmpty(accessToken)) {
            return accessToken;
        }

        String urlString = WECHAT_TOKEN_URL + "?grant_type=client_credential&appid="
                + wechatOfficialAccountProperties.getAppId()
                + "&secret=" + wechatOfficialAccountProperties.getSecret();

        String body = HttpRequest.post(urlString).timeout(6000).execute().body();

        try {
            accessToken = JSON.parseObject(body, JSONObject.class).getString("access_token");
        } catch (Exception e) {
            log.error("获取微信公众号token失败,error[{}],body[{}]", e.getMessage(), body);
        }

        redisUtils.setCacheObject(WECHAT_OFFICIAL_ACCOUNT_ACCESS_TOKEN, accessToken, 3600L, TimeUnit.SECONDS);

        return accessToken;

    }

    private String getSocialUserUnionId(String openid) {

        String token = getToken();

        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token
                + "&openid=" + openid + "&lang=zh_CN";

        String body = HttpRequest.post(url).timeout(6000).execute().body();

        String unionid = JSON.parseObject(body, JSONObject.class).getString("unionid");

        return unionid;
    }

}
