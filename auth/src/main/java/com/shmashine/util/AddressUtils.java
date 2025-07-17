package com.shmashine.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/12/13 11:25
 */

/**
 * 根据IP地址获取详细的地域信息 第一个方法是传入ip获取真实地址 最后一个方法是获取访问者真实ip 即使通过Nginx多层代理也可以获取
 */
public class AddressUtils {

    public static JSONObject getAddresses(String ip) {
        String returnStr = getResult(ip);
        if (returnStr != null) {

            // 处理返回的省市区信息
            JSONObject data = JSONUtil.parseObj(returnStr).getJSONObject("data");

            return data;
        }
        return null;
    }

    /**
     * @return
     */
    private static String getResult(String ip) {

        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setReadTimeout(5000);//单位为ms
        simpleClientHttpRequestFactory.setConnectTimeout(5000);//单位为ms
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        MultiValueMap<String, String> mulmap = new LinkedMultiValueMap<>();
        mulmap.add("ip", ip);
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "ApiPOST Runtime +https://www.apipost.cn");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(mulmap, headers);
        ResponseEntity<String> exchange = restTemplate.exchange("https://www.bejson.com/Bejson/Api/Ip/getIp", HttpMethod.POST, request, String.class);
        String body = exchange.getBody();
        return body;
    }


    // 测试
    public static void main(String[] args) {
        AddressUtils addressUtils = new AddressUtils();

        /**
         *     测试IP：111.121.72.101  中国贵州省贵阳市 电信
         */
        JSONObject result = null;
        try {
            result = addressUtils.getAddresses("219.136.134.157");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //国家
        System.out.println(result.getStr("country"));
        //省份
        System.out.println(result.getStr("region"));
        //城市
        System.out.println(result.getStr("city"));
        //区/县
        System.out.println(result.getStr("county"));
        //互联网服务提供商
        System.out.println(result.getStr("isp"));
        //ipv6
        System.out.println(result.getStr("ipv6"));
    }
}