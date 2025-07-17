package com.cn21.open.javaDemo;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import com.cn21.open.javaDemo.common.Constant;
import com.cn21.open.javaDemo.util.ByteFormat;
import com.cn21.open.javaDemo.util.GenReqParam;
import com.cn21.open.javaDemo.util.OkHttpUtil;
import com.cn21.open.javaDemo.util.XXTea;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pansicong
 * @version 1.1.2.1 平台对接版本DEMO
 * @date 2022/7/1
 **/
public class BaseTest {

    /**
     * 待测试的正式接口url
     */
    private static final String TEST_URL = "https://vcp.21cn.com/open/p/device/getReginWithGroupList";
    /**
     * 测试环境，获取能力开放accessToken
     */
    private static final String GET_ACCESS_TOKEN_URL_TEST = "https://beta-vcp.21cn.com/open/oauth/getAccessToken";
    /**
     * 生产环境，获取能力开放accessToken
     */
    private static final String GET_ACCESS_TOKEN_URL_PRO = "https://vcp.21cn.com/open/oauth/getAccessToken";
    /**
     * 测试环境，token方式访问原子能力接口
     */
    private static final String REGIN_URL_TOKEN_TEST = "https://beta-vcp.21cn.com/open/token/device/getReginWithGroupList";
    /**
     * 生产环境,token方式访问原子能力接口
     */
    private static final String REGIN_URL_TOKEN_PRO = "https://vcp.21cn.com/open/token/device/getReginWithGroupList";

    public static void main(String[] args) throws Exception {
//        /** 接口地址测试 */
//        System.out.println("接口地址测试 ");
//        test_interface();
//
//        /** 云眼/视频云网 RSA 加密 公钥 私钥 生成 */
//        System.out.println("云眼/视频云网 RSA 加密 公钥 私钥 生成");
//        CreatRSAKey();
//
//        /** RSA 密钥 加解密 测试 */
//        System.out.println("RSA 密钥 加解密 测试");
//        test_RSA();
//
        /** XXTea 加解密 测试 */
        System.out.println("XXTea 加解密 测试");
        test_XXTea();
//
//        /** ======================accessToken相关接口============================================ */
//        /** 平台应用，获取能力开放accessToken测试 */
//        System.out.println("平台应用，获取能力开放accessToken测试");
//        test_getAccessToken_platform();

//        /** 平台应用，accessToken方式访问原子能力接口 */
//        System.out.println("平台应用，accessToken方式访问原子能力接口");
//        test_interfaceByAccessToken_platform();
//
//        /** saas应用，获取能力开放accessToken测试 */
//        System.out.println("saas应用，获取能力开放accessToken测试");
//        test_getAccessToken_saas();
//
//        /** saas应用，accessToken方式访问原子能力接口 */
//        System.out.println("saas应用，accessToken方式访问原子能力接口");
//        test_interfaceByAccessToken_saas();

    }

    /**
     * 测试接口地址测试
     */
    private static void test_interface() {
        try {
            // 接口定义具体入参
            Map<String, Object> params = new HashMap<String, Object>(2);
            params.put("enterpriseUser", "189300000000"); // 这里输入企业主手机号enterpriseUser
            params.put("regionId", "");
            String json = sendMessage(TEST_URL, params, null);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 平台应用，获取能力开放的accessToken
     */
    private static void test_getAccessToken_platform() {
        try {
            Map<String, Object> params = new HashMap<>(10);
            // saas应用参数
            params.put("grantType", "vcp_189");
            sendMessage(GET_ACCESS_TOKEN_URL_TEST, params, genTokenHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * saas应用，获取能力开放的accessToken
     * 1.登录天翼账号，获取天翼账号的accessToken
     * 2.通过天翼账号的accessToken，换取天翼账号的tokenCode
     * 3.通过天翼账号的tokenCode，获取能力开放的accessToken（根据应用授权类型不同，又分为以下两种情况）
     * 3.1 应用授权类型为免企业主授权，则直接返回accessToken
     * 3.2 应用授权类型为企业主授权
     * - 如果未授权过，会返回错误码和授权地址shortUrl，需通过授权地址进行授权后，重新走步骤2和步骤3来获取accessToken
     * - 如果授权过，则直接返回accessToken
     */
    private static void test_getAccessToken_saas() {
        try {
            Map<String, Object> params = new HashMap<>(10);
            // 平台应用参数
            params.put("grantType", "189_code");
            params.put("tokenCode", "7f84d7f57bac40768bbd8c618875b35d");
            sendMessage(GET_ACCESS_TOKEN_URL_TEST, params, genTokenHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成token方式的header参数map
     *
     * @return
     */
    private static Map<String, String> genTokenHeader() {
        Map<String, String> headers = new HashMap<>(10);
        // 2.0获取accessToken，这个header参数固定写成这样
        headers.put("apiVersion", "2.0");
        return headers;
    }

    /**
     * 平台应用，通过accessToken方式访问原子能力接口
     * enterpriseUser参数为必传
     */
    private static void test_interfaceByAccessToken_platform() {
        try {
            // 接口定义具体入参
            Map<String, Object> params = new HashMap<String, Object>(2);
            // 平台应用
            params.put("enterpriseUser", "13148952730");
            params.put("regionId", "");
            params.put("accessToken", "6vyBgyDBz2shAWkaFwuMK3G40v6HAnuC");
            sendMessage(REGIN_URL_TOKEN_TEST, params, genTokenHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * saas应用，通过accessToken方式访问原子能力接口
     * 不需要传enterpriseUser参数，能力开放内部会使用accessToken关联的手机号
     */
    private static void test_interfaceByAccessToken_saas() {
        try {
            // 接口定义具体入参
            Map<String, Object> params = new HashMap<String, Object>(2);
            // 平台应用
            params.put("regionId", "");
            params.put("accessToken", "nK8EcNk4aECsAe1KLhcJK5R9s37dv1Nn");
            sendMessage(REGIN_URL_TOKEN_TEST, params, genTokenHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * RSA 密钥 加解密 测试
     */
    private static void test_RSA() {
        /** 模拟的 测试的 json 字符串 */
        String json = "{\"deviceCode\":\"3ZWCA5550200HXM\", messageType=AI_ALARM, jsonBody={\"algType\":\"1011008\",\"code\":0,\"data\":{\"alarmTime\":\"1654406601028\",\"algCode\":\"open189-1011008-1.0.0\",\"deviceSn\":\"3ZWCA5550200HXM\",\"sceneId\":100006,\"srcToken\":\"63CE796887CEF4FBB705A5B5ACBE04AD8B0C4EE890598C4B41CBBF01D0C3667147C284F041CCBB73B0CE8FDA004F1BAEEDA2C4D0FCDAF39A2A6A728139E7234D2C298D78280BC33CE88A5C8562F8BB39A8F2461B9F42668BDF9D39BA7115EAC7059343187C3A5CD52AD951F10DE58010\",\"srcUrl\":\"http%3A%2F%2Fai-data-manage.gd3oss.xstore.ctyun.cn%2FLF10%2FAIMODEL%2Fai-platform%2F2f3391f18652bb4d7c184eef30e38519%3Fresponse-content-disposition%3Dattachment%253Bfilename%253D5cc7f29784044aa79d686a4d161f3ffb%26Signature%3DufW6hPBie43nn5s1hcsrjZk38kU%253D%26AWSAccessKeyId%3DAVLFT0M0P1CGGFX0U6X7%26Expires%3D1654494065\",\"targets\":[{\"bbox\":[{\"class\":\"pedestrain\",\"conf\":0.5098426,\"point\":[140,126,241,438]}],\"fileType\":0,\"frameNum\":-1}]},\"msgId\":\"375cda29-bdf1-42d5-8d4e-1cc55f0fd922\"}";
        System.out.println("原始的字符串 是:" + json);

        RSA rsa = new RSA(null, Constant.publicKey);
        String data = rsa.encryptHex(json + json + json, KeyType.PublicKey);
        System.out.println("加密后字符串 是:" + data);

//        data = "89eea65ab81c50845ad7a92ead114d0c2e2afa94bcdffba3e99ec47efa4be60d77c563cafc8feb5d0c0f53d841290551c8da4531aa6ee796b25eff5692b7dee42a741c3cc9b90e93cabeb2bd0c8d4578b467519a150e57e904f61888e7569a49807be3be9cb47893c8161b4425132470d3bf0f5e6ed1630f736abc3c964ab8c96de1adc2a072ae39ea2b9fe371a9194f0ff9cdc0cd8c8057b5b3d1c6b90af51a6dc1837bf71d4dce04aef9cc52e33630a72da71439fc813fa40762ae60cff800329450e15383d74252e5a5978aa602af2304fbf9a0a7f55ed3bbfa15f5cd9d68b39076a3be018cd0ab167f7a58eb15ad24376489d10d720f58151501ae085abc018156ed5edf2d26081891cde7b6c76ac51951e714225b9199c7a3a5d7047be018af9134fe9f1273c140173bfe8b1d34351c98433163c5efc9da7352f8efde0bf6bc40247946900e5e67abc01ea013437fdc787fa0454ac5b9a4ca591bc3d3eb9a2fe832412239602892408de18ea45ee9494bad475e9ed77f96e312169a0dc381deaeaa80bfb05ddfaa8c928b82b4ced560f5099ac51f9fab39e20dad965e3aaceffa424bacb4a8ede9dc89e5e12bd59429abb8dc4558192868c20cdba26b756905317b102c301fb84b150505d17dbab3cb8bca0d3e66447161ef14bad666ac81be523ac53390da04963fc30c9fa716082ad0e8858728dca8a0fb1821d830f46d1a944a3942efc6d77da3b243a5127a6e05b85a991502ec78ae362386051e3891ddc2e766dd22802e64189648fc42e0b080bad592468a86fe5f3c615989dd03fefb99c997d5c513a0d810899568477a578683135cd932029f61759b51bd564e186dad621f7180a874e4584c99f559c73a702e36e6ee3035d7d63fb7b6499e2a780721c0c827305f4f59acf892a6e1f259a48f28236aae05ef5afafca46a9ebc2ce2b633ae7fbf671b43e642120e1699acfedd8e619a045f95aac22b93e0cbe73607f0f01cbd0757083c298e774dd5571e24c93d2c6dc3cdba9a52370055bac5142b986e00e5e6d6413b33ac49b0b3379a7cf6b90c517ff7c1ffa04078268526a7beff1245c745309272a73f125d1d280c4d2ab98cf3dc4be4b6933200e773043766037907f73fff35ccde00cfeb638bb9d5dca3bab9466ab7655d801474ba15561b2ee910d66e2ddf6b60fd48d8b52a13f6b775398eeae92d2fa75e2cd48b8f58e3435a1e9a5c063f6cc5f219cc0dd840d55edfd8283b69049dbe3fcdfb8036451c05c837d418c9a95aee5fe5634697108d053e5d8da208df273fba064c28b6ee7c9b83de5ee95003e86ccc1c7ec4248a9abd8d6a1c6bf7f8046f9fc22a0335c1bef6433c922ddd29e0600f867875bd1db7a17fee4cb49eab56a4a424a78a1a2e3544738d8b4274859e01eef0eeb9682bf4b34469e5f02ca5ac10ecec8f11cd8247a36edd776df1fb18dc258eeb3d91c5dd9ba8553128005222fbe276e4970b466f0e5811d55d2b58d812651771580dfbb6a9390b8378e71be376db673aac97f56c0ebcec92187a90be072798b09261160a084f78401efdc1076f42f15336d1dbaf228aff60f199e535e34d678c9c9f3d3fcfa6cc338cfa37cc574da733824360861e9eecc04d1745dc7e360f8aca18f9b474b7cfaea5a041a2b562e8509d9ba49ee44ceb73202ffcbe151e0a773540c16d56525e67dcc8a9b8ab232b7dd3251ee70034b928d81ed737a10ee51f7ba40ec0c54ddb2e982ed5e41dfbea035b783197811cccb8f1a5f2ec807908481695d24c3d4fd894f77f7e209af4b0ca041829594cce656ade9202367d36d840b90cbdbbdda288c4e7c5ed4d76d436074576a784df6d700b218d5325e37c53c19a665750c315c58ec5e72c3b58eff27ce3802c0433f82151257b2ea2b64f9835306d4e5c1ecbe9b8cba4cab513365e7cc67ca17e067c2a8d2ddd839817491a388fc838086100f4b0f2d53c1a69be5d42e5668d7011cc2189a2d50892bd110d58f9efe6da9b8dc432869d3e6a013d9464856aa79f24de5b36e93932623c2cd09dd30372d61ba27974a0ac7d965c12fb0635b40886a7c96d73960bf5acde72d5b6c3d55a7b595e74e4f1c3a580ce01ba021eabbc379eb23432323f4628a2ad4407c4528230a83967472895c4d71e97f625dfdf";

        RSA rsa1 = new RSA(Constant.privateKey, null);
        String source = rsa1.decryptStr(data, KeyType.PrivateKey);
        System.out.println("解密后字符串 是:" + source);

    }


    public static void test_XXTea() {
        String result = "clientType=3&appId=690182581109&enterpriseUser=19313578062&version=v1.0&timestamp=1690427366525";
        System.out.println("原始的字符串 是:" + result);
        try {
            /** XXTEA 加密 */
            String params = XXTea.encrypt(result, "UTF-8", ByteFormat.toHex(Constant.APP_SECRET.getBytes()));
            System.out.println("加密后字符串 是:" + params);
            String decrpte = XXTeaDEC(params, Constant.APP_SECRET);
            System.out.println("解密后字符串 是:" + decrpte);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
     * 本地公私钥匙生成
     */
    public static void CreatRSAKey() {
        RSA rsa = new RSA("RSA");
        String privateKey = rsa.getPrivateKeyBase64();
        String publicKey = rsa.getPublicKeyBase64();
        System.out.println("publicKey: " + publicKey);
        System.out.println("============================================");
        System.out.println("privateKey: " + privateKey);
    }


    /**
     * 本地XXTea 解密
     *
     * @param encodeData 加密的字符串
     * @param APP_SECRET app_scret
     * @return 明文
     * @throws Exception 异常
     */
    public static String XXTeaDEC(String encodeData, String APP_SECRET) throws Exception {
        return XXTea.decrypt(encodeData, "UTF-8", ByteFormat.toHex(APP_SECRET.getBytes()));
    }


    /**
     * 发送请求
     *
     * @param url     url
     * @param params  接口请求参数
     * @param headers 接口请求header参数
     * @throws Exception
     */
    public static String sendMessage(String url, Map<String, Object> params, Map<String, String> headers) throws Exception {
        Map<String, Object> resultMap = GenReqParam.apiGen(params);
        return OkHttpUtil.sendPost(url, resultMap, headers);
    }


}
