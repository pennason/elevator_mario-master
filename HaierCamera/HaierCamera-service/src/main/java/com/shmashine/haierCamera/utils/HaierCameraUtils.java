package com.shmashine.haierCamera.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cn.hutool.json.JSONObject;

import com.shmashine.haierCamera.constants.HaierCameraConstants;
import com.shmashine.haierCamera.constants.RedisConstants;
import com.shmashine.haierCamera.redis.RedisService;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/18 18:04
 */
@Component
public class HaierCameraUtils {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RestTemplate restTemplate;

    private static final String clientId = "maixin";

    private static final String clientSecret = "ba4b0f6c581bb583a51d0a71586f6f20";

    /**
     * 获取token
     */
    public String getToken() {

        String token = redisService.getCacheObject(RedisConstants.HAIERCAMERA_TOKEN);

        if (token == null) {

            JSONObject reqBody = new JSONObject();
            reqBody.set("clientId", clientId);
            reqBody.set("clientSecret", clientSecret);

            HashMap<String, Object> result = restTemplate.postForObject(HaierCameraConstants.TOKEN_URL, reqBody, HashMap.class);

            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");

            token = (String) data.get("token");
            long timeout = (int) data.get("expires") - 60;

            redisService.setCacheObject(RedisConstants.HAIERCAMERA_TOKEN, token, timeout, TimeUnit.SECONDS);
        }

        return token;
    }


    /**
     * 刷新token
     *
     * @return
     */
    public String refreshToken() {

        JSONObject reqBody = new JSONObject();
        reqBody.set("clientId", clientId);
        reqBody.set("clientSecret", clientSecret);

        HashMap<String, Object> result = restTemplate.postForObject(HaierCameraConstants.TOKEN_URL, reqBody, HashMap.class);
        HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");

        String token = (String) data.get("token");
        Long timeout = (Long) data.get("expires") - 60;

        redisService.setCacheObject(RedisConstants.HAIERCAMERA_TOKEN, token, timeout, TimeUnit.SECONDS);

        return token;
    }


    /**
     * 调用二次识别
     *
     * @param workOrderId 故障id
     * @param fileUrl     待识别图片url
     * @param type        识别类型
     */
    private void restTemplateSendMessage(String workOrderId, String fileUrl, String type) {
        //拼接请求参数
        String url = "http://172.31.183.101:10087/?type=" + type + "&url=" + fileUrl + "&faultId=" + workOrderId;

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            HttpGet httpGet = new HttpGet(url);
            try {
                httpclient.execute(httpGet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).exceptionally(throwable -> null);
        try {
            Thread.sleep(100);
            httpclient.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
