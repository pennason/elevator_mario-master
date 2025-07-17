package com.shmashine.sender.platform.city.shanghai;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.http.HttpRequest;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.sender.log.SenderLog;
import com.shmashine.sender.redis.utils.RedisUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 统一推送入口 临港
 *
 * @author jiangheng
 * @since 2022/11/7 16:28
 */

@Slf4j
@Component
public class LinGangHttpUtil {

    //生产环境
    private static final String BASE_URL = "http://220.196.247.249:16001/gateway/http/";

    private static final String PRODUCT_KEY = "005b3548375b17c6";

    private static final String PRODUCT_SECRET = "ec3cdbf74323c457";

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 统一推送入口 便于统计计数
     */
    @SenderLog(groupId = "LinGang")
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 2))
    public String send(@NotNull String registerNumber, @NotNull String topic, @NotNull String url,
                       @NotNull String body) {
        return post(url, body, registerNumber);
    }

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 2))
    public String post(String url, String body, String registerNumber) {

        if (StringUtils.isBlank(url) || StringUtils.isBlank(body)) {
            return null;
        }

        url = BASE_URL + url;

        String token = getToken(registerNumber);

        String response = HttpRequest.post(url)
                .body(body).header("password", token)
                .execute().body();

        // post成功日志
        log.info("post: url[{}] body[{}] success !  response: [{}]", url, body, JSON.toJSONString(response));
        return response;
    }

    private String getToken(String registerNumber) {

        String rediskey = "SENDER:SERVICE:TOKEN:LINGANG:" + registerNumber;

        String token = redisUtils.get(rediskey);

        if (StrUtil.isEmpty(token) || "null".equals(token)) {

            String url = BASE_URL + "http/auth";

            String timestamp = StrUtil.toString(System.currentTimeMillis());

            HMac mac = new HMac(HmacAlgorithm.HmacMD5, PRODUCT_SECRET.getBytes());

            String sign = mac.digestHex(PRODUCT_KEY + registerNumber);

            String json = JSON.toJSONString(Map.of("productKey", PRODUCT_KEY,
                    "deviceName", registerNumber,
                    "sign", sign,
                    "timestamp", timestamp));

            String body = HttpRequest.post(url)
                    .body(json)
                    .execute().body();

            token = JSON.parseObject(body, JSONObject.class).getString("data");

            redisUtils.set(rediskey, token, 24 * 60 * 60);

        }

        return token;
    }
}
