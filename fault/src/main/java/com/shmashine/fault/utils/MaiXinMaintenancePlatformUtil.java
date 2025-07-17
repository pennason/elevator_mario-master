package com.shmashine.fault.utils;

import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.StringUtils;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.fault.entity.maiXinMaintenancePlatform.FaultMessageReqVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 麦信维保平台对接
 *
 * @author jiangheng
 * @version V1.0.0 - 2023/10/12 11:20
 */
@Slf4j
public class MaiXinMaintenancePlatformUtil {

    private static final String BASE_URL = "https://weibao.shmashine.com/admin-api/";

    //test url
    //private static final String BASE_URL = "http://nps.shmashine.com:49080/admin-api/";

    /**
     * 获取token地址
     */
    private static final String GET_TOKEN_URL = BASE_URL + "system/oauth2/token";

    /**
     * 推送故障
     */
    private static final String PUSH_FAULT_MESSAGE = BASE_URL + "shmashine-message/fault/device-message/push";

    /**
     * 推送待确认故障-困人工单
     */
    private static final String PUSH_EMERGENCY_RESCUE_MESSAGE = BASE_URL
            + "shmashine-message/fault/emergency-rescue/push";

    /**
     * 推送维保平台困人取消
     */
    private static final String PUSH_EMERGENCY_RESCUE_CANCEL = BASE_URL
            + "shmashine-maintenance/emergency-rescue/pushEmergencyRescueCancel";


    private static final Map GET_TOKEN_FORM = Map.of(
            "grant_type", "password",
            "username", "ShmashineDataPlatform",
            "password", "123456",
            "client_id", "ShmashineDataPlatform",
            "client_secret", "asbfdkjhsahgfdasfpodsjfo",
            "scope", "shmashine:contract-customer:query shmashine.message.fault.push"
    );

    private static TimedCache<String, String> tokenCache = CacheUtil.newTimedCache(1600);


    /// ///////////////////////////////////private////////////////////////////////////////////////

    private static String getToken() {

        String token = tokenCache.get(RedisConstants.MAIXIN_MAINTENANCE_PLATFORM_TOKEN);


        if (StrUtil.isBlank(token)) {

            String body = HttpRequest.post(GET_TOKEN_URL).form(GET_TOKEN_FORM).header("tenant-id", "1")
                    .timeout(6000).execute().body();

            token = JSON.parseObject(body, JSONObject.class).getJSONObject("data").getString("access_token");

            tokenCache.put(RedisConstants.MAIXIN_MAINTENANCE_PLATFORM_TOKEN, token, 1600);

            return token;

        }

        return token;
    }

    /**
     * 推送待确认故障-困人工单
     *
     * @param faultMessageReqVO 故障消息
     */
    public static void pushEmergencyRescue(FaultMessageReqVO faultMessageReqVO) {

        if (StringUtils.hasText(faultMessageReqVO.getRegisterNumber())) {

            try {
                String token = getToken();
                String body = HttpRequest.post(PUSH_EMERGENCY_RESCUE_MESSAGE)
                        .body(JSON.toJSONString(faultMessageReqVO)).bearerAuth(token)
                        .timeout(6000).execute().body();

                var code = JSON.parseObject(body, JSONObject.class).getInteger("code");

                if (code == 0) {
                    log.info("推送故障信息-成功-reqData：{}", faultMessageReqVO, token);
                } else {
                    log.info("推送故障信息-失败-reqData：{}，respData：{}，token：{}", faultMessageReqVO, body, token);
                }
            } catch (HttpException e) {
                log.info("推送故障信息-失败-reqData：{}，error：{}", faultMessageReqVO, ExceptionUtils.getStackTrace(e));
            }

        } else {
            log.info("无电梯注册码-不推送故障信息-reqData：{}", faultMessageReqVO);
        }

    }


    /**
     * 维保困人取消
     *
     * @param faultId 故障id
     */
    public static String pushEmergencyRescueCancel(String faultId) {

        String token = getToken();

        //通知维保确认
        String body = HttpRequest.post(PUSH_EMERGENCY_RESCUE_CANCEL).form(Map.of("alarmId", faultId)).bearerAuth(token)
                .timeout(6000).execute().body();
        var data = JSON.parseObject(body, JSONObject.class).get("data");
        if (data == null) {
            return null;
        }
        return data.toString();
    }

}
