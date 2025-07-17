package com.shmashine.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.utils.RequestUtil;

/**
 * 摄像头工具类
 *
 * @author little.li
 */
public class CameraUtils {


    /**
     * 获取appToken URL
     */
    public static String APP_TOKEN_URL = "https://open.ys7.com/api/lapp/token/get";

    public static String appKey = "8374cfb69acd473d8b4a65c8837c364a";
    public static String appSecret = "25f086df116c78899863c7fc0b8e24ae";

    private static final String FORMAT_SEC_EZOPEN = "yyyyMMddHHmmss";


    /**
     * 构建萤石云历史回放URL
     */
    public static String getEzOpenHistoryUrl(Date reportTime, Date endTime, String privateUrl) {
        if (StringUtils.isBlank(privateUrl)) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_SEC_EZOPEN);
        long beforeTime = 10 * 1000;
        long afterTime = 60 * 1000;

        privateUrl = privateUrl.split(".live")[0];
        String accessToken = getEzopenToken();
        Date beforeDate = new Date(reportTime.getTime() - beforeTime);

        String begin = simpleDateFormat.format(beforeDate);
        String end;
        if (endTime == null) {
            Date afterDate = new Date(reportTime.getTime() + afterTime);
            end = simpleDateFormat.format(afterDate);
        } else {
            end = simpleDateFormat.format(endTime);
        }

        return privateUrl
                + ".rec&autoplay=0&accessToken=" + accessToken
                + "&begin=" + begin
                + "&end=" + end + "&templete=2";
    }


    /**
     * 获取萤石云token
     */
    private static String getEzopenToken() {
        // 拼接请求参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(APP_TOKEN_URL)
                .queryParam("appKey", appKey)
                .queryParam("appSecret", appSecret);

        Map<String, Object> queryMap = new HashMap<>();

        ResponseEntity<String> map = RequestUtil.sendPost(builder, queryMap);
        JSONObject body = JSONObject.parseObject(map.getBody());
        return body.getJSONObject("data").getString("accessToken");
    }

    /**
     * 调用天翼平台生成直播流地址
     *
     * @param tenantNo  租户唯一识别码
     * @param tenantKey 租户密钥
     * @param deviceNo  设备编号
     */
    public static String getUrl(String tenantNo, String tenantKey, String deviceNo) {

        String url = "http://101.89.203.135:10035/viot-openapi/openapi/GBDevApi/V2/media/play";

        String time = DateUtil.format(DateTime.now(), DatePattern.PURE_DATETIME_PATTERN);
        StringBuilder signSb = new StringBuilder();
        String singStr = signSb.append(tenantNo + time + tenantKey).toString();
        String sign = DigestUtils.md5DigestAsHex(singStr.getBytes());
        RestTemplate restTemplate = new RestTemplate();

        Map reqMap = new HashMap();
        reqMap.put("deviceNo", deviceNo);

        JSONObject reqBody = new JSONObject();
        reqBody.put("tenantNo", tenantNo);
        reqBody.put("sign", sign);
        reqBody.put("time", time);
        reqBody.put("req", reqMap);


        System.out.println("reqParam>>>>>>>" + JSON.toJSONString(reqBody));

        String result = null;
        try {
            result = restTemplate.postForObject(url, reqBody, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return result;
    }


}
