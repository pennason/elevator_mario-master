package com.cn21.open.javaDemo;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import com.cn21.open.javaDemo.common.Constant;
import com.cn21.open.javaDemo.common.Variable;
import com.cn21.open.javaDemo.util.ByteFormat;
import com.cn21.open.javaDemo.util.HmacSHATool;
import com.cn21.open.javaDemo.util.OkHttpUtil;
import com.cn21.open.javaDemo.util.XXTea;

import java.util.*;

/**
 * @Author lyf15
 * @Description 基础方法常用，RSA，XXTea加解密，请求参数组装，加密签名等
 * @Date 2023/7/1210:25
 */
public class BaseDemo {

    /**
     * 本地 私钥 RSA 解密
     *
     * @param encodeData 待解密字符串
     * @param privateKey 本地私钥
     * @return 明文
     */
    public static String RSADec(String encodeData, String privateKey) throws Exception {
        RSA rsa1 = new RSA(privateKey, null);
        String source = rsa1.decryptStr(encodeData, KeyType.PrivateKey);
        return source;
    }

    /**
     * 本地公钥RSA加密  用于本地测试 密钥对 这个是用于对接云眼平台的RSA加密机制【不要用到看家的接口】
     *
     * @param sourceData
     * @param publicKey
     * @return
     */
    public static String RSAEce(String sourceData, String publicKey) {
        RSA rsa = new RSA(null, publicKey);
        String data = rsa.encryptHex(sourceData, KeyType.PublicKey);
        return data;
    }

    /**
     * 本地XXTea 加密
     *
     * @param encodeData 加密的字符串
     * @param APP_SECRET app_scret
     * @return 明文
     * @throws Exception 异常
     */
    public static String XXTeaEncrypt(String encodeData, String APP_SECRET) throws Exception {
        return XXTea.encrypt(encodeData, "UTF-8", ByteFormat.toHex(APP_SECRET.getBytes()));
    }

    /**
     * 本地XXTea 解密
     *
     * @param encodeData 加密的字符串
     * @param APP_SECRET app_scret
     * @return 明文
     * @throws Exception 异常
     */
    public static String XXTeaDecrypt(String encodeData, String APP_SECRET) throws Exception {
        return XXTea.decrypt(encodeData, "UTF-8", ByteFormat.toHex(APP_SECRET.getBytes()));
    }

    /**
     * 生成token方式的header参数map
     * 请检查请求头参数apiVersion：2.0参数要求必传，否则会提示:70012，应用不存在
     *
     * @return
     */
    protected static Map<String, String> genTokenHeader() {
        Map<String, String> headers = new HashMap<>(10);
        // 2.0获取accessToken，这个header参数固定写成这样
        headers.put("apiVersion", "2.0");
        return headers;
    }

    /**
     * 构建完整访问路径
     */
    protected static String getUrl(String baseUrl, String uri) {
        if (StrUtil.isBlank(baseUrl) || StrUtil.isBlank(uri)) {
            throw new RuntimeException("构建完整访问路径异常");
        }
        if (baseUrl.endsWith(Variable.SPLIT_FORWARD_SLASH)) {
            return baseUrl.substring(0, baseUrl.length() - 1) + uri;
        }
        return baseUrl + uri;
    }

    /**
     * 发送请求
     *
     * @param url     url
     * @param params  接口请求参数，
     * @param headers 接口请求header参数
     *                封装了公共参数，若报错，请检查是否将公共参数修改成自己应用的参数{@link Constant}
     * @throws Exception
     */
    public static String sendMessage(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        Map<String, Object> resultMap = apiGen(params);
        return OkHttpUtil.sendPost(url, resultMap, headers);
    }

    /**
     * 用于保持请求和回调的状态，登录请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），第三方需带上该参数，可设置为简单的随机数加session进行校验
     * 这里只是提供了一种方式，state具体生成规矩可由合作方自行制定
     *
     * @return
     */
    public static String genState() {
        return UUID.randomUUID().toString().replace(Variable.SPLIT_HYPHEN, Variable.SPLIT_BLANK);
    }

    /**
     * 对应文档：门户文档中心-接口规范
     * 请注意，这里的的公共参数请改成自己的应用对应的参数{@link Constant}
     * 生成参数，参数加密，添加签名等
     *
     * @param specialParam 接口具体入参
     * @return
     */
    public static Map<String, Object> apiGen(Map<String, Object> specialParam) throws Exception {
        // 公共参数
        String appId = Constant.APP_ID;
        String version = Constant.VERSION2;
        Integer clientType = Constant.CLINT_TYPE;
        Long timestamp = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();
        Set<String> keySet = specialParam.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = specialParam.get(key);
            sb.append(key).append("=").append(value).append("&");
        }
        String paramStr = "";
        if (sb.length() > 0) {
            paramStr = sb.substring(0, sb.length() - 1);
        }
        System.out.printf("请求参数(加密前):%s%n", paramStr);
        String params = XXTea.encrypt(paramStr, "UTF-8", ByteFormat.toHex(Constant.APP_SECRET.getBytes()));
        String signature = HmacSHATool.encodeHmacSHA256(appId + clientType + params + timestamp + version, Constant.APP_SECRET);
        // 返回全部参数
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("appId", appId);
        resultMap.put("version", version);
        resultMap.put("clientType", clientType);
        resultMap.put("timestamp", timestamp);
        resultMap.put("signature", signature);
        resultMap.put("params", params);
        return resultMap;
    }

}
