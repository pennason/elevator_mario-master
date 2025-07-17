package com.shmashine.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsRequest;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 电话通知工具列表
 *
 * @author little.li
 */
public class AlarmTaskUtils {


    /**
     * 产品名称:云通信语音API产品,开发者无需替换
     */
    static final String PRODUCT = "Dyvmsapi";
    /**
     * 产品域名,开发者无需替换
     */
    static final String DOMAIN = "dyvmsapi.aliyuncs.com";

    /**
     * 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
     */
    static final String ACCESS_KEY_ID = "LTAIY9LXaiBxEli4";
    static final String ACCESS_KEY_SECRET = "EpCz2cLScD1J9tARmdgP3I6xx1N8jL";


    /**
     * 文本转语音外呼
     */
    public static SingleCallByTtsResponse singleCallByTts(String tel, String param, String outId) {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SingleCallByTtsRequest request = new SingleCallByTtsRequest();
        //必填-被叫显号,可在语音控制台中找到所购买的显号
        request.setCalledShowNumber("02160662622");
        //必填-被叫号码
        request.setCalledNumber(tel);
        //必填-Tts模板ID
        request.setTtsCode("TTS_182541631");
        //可选-当模板中存在变量时需要设置此值
        request.setTtsParam(param);
        //可选-外部扩展字段,此ID将在回执消息中带回给调用方
        request.setOutId(outId);


        SingleCallByTtsResponse singleCallByTtsResponse = null;
        try {
            singleCallByTtsResponse = acsClient.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return singleCallByTtsResponse;

    }


    /**
     * test
     */
    public static void main(String[] args) throws ClientException, InterruptedException {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("code", "MX3390");
        // 语音模板里有故障两字了，可以去掉
        map.put("level", "你好 玛卡巴卡，宁吃了吗?");
        map.put("local", "天安门白宫");

        String message = JSONObject.toJSONString(map);
        SingleCallByTtsResponse singleCallByTtsResponse = singleCallByTts("17621046137", message, "aaaaaaa");
        //SingleCallByTtsResponse singleCallByTtsResponse = singleCallByTts("15116056591", message, "aaaaaaa");
        //SingleCallByTtsResponse singleCallByTtsResponse = singleCallByTts("13564919965", message, "aaaaaaa");
        System.out.println("文本转语音外呼---------------");
        System.out.println("RequestId=" + singleCallByTtsResponse.getRequestId());
        System.out.println("Code=" + singleCallByTtsResponse.getCode());
        System.out.println("Message=" + singleCallByTtsResponse.getMessage());
        System.out.println("CallId=" + singleCallByTtsResponse.getCallId());
    }


}
