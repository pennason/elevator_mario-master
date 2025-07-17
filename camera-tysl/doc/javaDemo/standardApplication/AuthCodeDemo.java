package com.cn21.open.javaDemo.standardApplication;

import cn.hutool.json.JSONUtil;

import com.cn21.open.javaDemo.common.Constant;
import com.cn21.open.javaDemo.common.Variable;
import com.cn21.open.javaDemo.BaseDemo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author lyf15
 * @Description 用户扫码登录获取令牌-标准应用获取访问令牌测试demo-天翼云眼用户授权（authCode用户授权）
 * 若有其他问题，请参考门户（示例了常见问题）：天翼视联-能力开放门户Q&A，地址：https://vcp.dlife.cn/portal/QA
 * @Date 2023/7/1210:32
 */
public class AuthCodeDemo extends BaseDemo {
    /**
     * 获取访问令牌授权码authCode的URI
     */
    private static final String GET_AUTH_PAGE_URL_URI = "/open/oauth/login/getAuthPageUrl";
    /**
     * 获取访问令牌accessToken的URI
     */
    private static final String GET_ACCESS_TOKEN_URI = "/open/oauth/getAccessToken";
    /**
     * 获取当前账号所分配的区域，及其所有下级区域列表
     */
    private static final String GET_REGIN_WITH_GROUP_LIST_URI = "/open/token/device/getReginWithGroupList";
    /**
     * 根据区域id分页查询当前区域下设备列表
     */
    private static final String GET_DEVICE_LIST_URI = "/open/token/device/getDeviceList";

    /**
     * 根据区域id分页查询当前区域及子区域下设备列表
     */
    private static final String GET_ALL_DEVICE_LIST_NEW_URI = "/open/token/device/getAllDeviceListNew";

    /**
     * 请对照文档，修改公共参数{@link Constant}并打开注释调用，
     *
     * @param args
     */
    public static void main(String[] args) {
//        //获取登录地址，
        String authPageUrl = getAuthPageUrl();
//        获取访问令牌accessToken
//        getAccessToken("ZyZwY6VXdTV2Y6Gn1wIf7IKt");
//        刷新token
//        getAccessTokenByRefreshToken("DuyDLLEB34VwmGn9s1IslsId1z4b9nBa");
//        定时刷新accessToken实现保活机制简单示例
//        refreshTokenByScheduled();
        //获取监控目录树
//        getReginWithGroupList("FhnZR2HVCN5FmPvjmmwYfxnKB5jbXEJW");
//        getDeviceList("FhnZR2HVCN5FmPvjmmwYfxnKB5jbXEJW","65881055");
//        getAllDeviceListNew("FhnZR2HVCN5FmPvjmmwYfxnKB5jbXEJW","65881055");
    }

    /**
     * 标准应用-PC网页获取访问令牌授权码authCode
     * 1、若报：（40105,"回调地址非法"），请检查门户应用配置中授权回调域名，此域名必须与 "getAuthPageUrl" 接口 “callbackUrl” 字段中域名匹配，服务端在返回授权码前，会校验应用配置的授权回调域名是否与实际回调地址域名一致（仅校验域名，域名后的资源地址不限）。
     * 2、授权域名可接受 “协议://域名” 和 “协议://IP+端口” 两种形式，其中无论何种形式记得必须填写 “协议” 关键信息。
     * 3，若门户未配置回调域名，会报：(40104, "回调域名为空")
     * 获取登录地址，获取authPageUrl在浏览器打开，用户授权登录成功后重定向到CALLBACK_URL地址，并携带authCode,authCode应用于getAccessToken通过授权码获取访问令牌
     */
    private static String getAuthPageUrl() {
        String url = getUrl(Constant.TEST_DOMAIN, GET_AUTH_PAGE_URL_URI);
        Map<String, Object> params = new HashMap<>(4);
        //最终授权登录成功的重定向URI地址，域名
        params.put(Variable.CALLBACK_URL, "http://loaclhost:8080");
        params.put(Variable.STATE, genState());
        //登录端类型 web (PC网页)： 10010
        //登录端类型 wap(H5)端： 20100
        params.put(Variable.LOGIN_CLIENT_TYPE, "10010");
        String response = null;
        try {
            response = sendMessage(url, params, genTokenHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String authPageUrl = JSONUtil.parseObj(response).get("data").toString();
        System.out.println("登录框地址为：" + authPageUrl);
        return authPageUrl;
    }

    /**
     * 标准应用-通过授权码换取访问令牌accessToken
     * 若返回：30040, "授权类型与app类型不匹配"，请检查grantType是否与应用类型匹配
     * authCode的缓存时效为5分钟，过期请重新获取
     */
    private static void getAccessToken(String authCode) {
        String url = getUrl(Constant.PRO_DOMAIN, GET_ACCESS_TOKEN_URI);
        try {
            Map<String, Object> params = new HashMap<>(2);
            // 选取下列2个值之一
            //"auth_code"：授权码获取访问令牌
            //"refresh_token"： 标准应用刷新令牌
            params.put(Variable.GRANT_TYPE, "auth_code");
            //通过getAuthPageUrl接口获取登录框，用户授权登录后返回的authCode(浏览器地址栏)
            params.put(Variable.AUTH_CODE, authCode);
            sendMessage(url, params, genTokenHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过getAccessToken接口刷新访问令牌，合作方可通过此接口实现自己的令牌保活机制
     * 标准应用accessToke有消息为一天，refreshToken有效期为7天，通过此接口，可获取一个全新生命周期的（新的）accessToken和refreshToken
     *
     * @param refreshToken
     */
    private static void getAccessTokenByRefreshToken(String refreshToken) {
        String url = getUrl(Constant.PRO_DOMAIN, GET_ACCESS_TOKEN_URI);
        try {
            Map<String, Object> params = new HashMap<>(2);
            //"refresh_token"： 标准应用刷新令牌
            params.put(Variable.GRANT_TYPE, "refresh_token");
            //通过getAccessToken接口获取的refreshToken
            params.put(Variable.REFRESH_TOKEN, refreshToken);
            sendMessage(url, params, genTokenHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 合作方可通过此接口实现自己的令牌保活机制示例，一次扫码登录，后期长时间维护
     * 此方法只是简单做一个示例，具体实现方式自行斟酌，主要是通过定时任务之类的定期刷新缓存从而实现保活
     */
    private static void refreshTokenByScheduled() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Runnable refreshTask = new Runnable() {
            @Override
            public void run() {
                //todo 从缓存（数据库）之类存储内的获取refreshToken，建议关联企业主账号，保障唯一主键关联
                String refreshToken = "DuyDLLEB34VwmGn9s1IslsId1z4b9nBa";
//                getAccessTokenByRefreshToken("DuyDLLEB34VwmGn9s1IslsId1z4b9nBa");
                System.out.println("111");
                //todo 保存/刷新应用信息，accessToken到缓存（数据库）
            }
        };
        //1天后开始执行，1天刷新一次，标准应用refreshToken有效期是7天，时间长度自行斟酌
        scheduledExecutorService.scheduleAtFixedRate(refreshTask, 1, 1, TimeUnit.DAYS);
    }

    /**
     * 示例接口，若返回错误，请参考门户：天翼视联-能力开放门户Q&A，地址：https://vcp.dlife.cn/portal/QA
     * 测试，查询监控目录-获取监控目录接口
     * 此接口只是获取监控目录，获取全量设备需要遍历目录，获取id（regionId）后调后getDeviceList获取当前区域下的设备，getAllDeviceListNew接口也可以获取级联设备，自行酌情调用
     *
     * @param accessToken
     */
    private static void getReginWithGroupList(String accessToken) {
        String url = getUrl(Constant.PRO_DOMAIN, GET_REGIN_WITH_GROUP_LIST_URI);
        try {
            Map<String, Object> params = new HashMap<>(2);
            params.put(Variable.ACCESS_TOKEN, accessToken);
            params.put(Variable.REGION_ID, "");
            sendMessage(url, params, genTokenHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据区域id分页查询当前区域下设备列表
     * 通过getReginWithGroupList获取的id（regionId）调此接口，获取当前区域下设备
     *
     * @param accessToken
     * @param regionId
     */
    private static void getDeviceList(String accessToken, String regionId) {
        String url = getUrl(Constant.PRO_DOMAIN, GET_DEVICE_LIST_URI);
        try {
            Map<String, Object> params = new HashMap<>(4);
            params.put(Variable.ACCESS_TOKEN, accessToken);
            params.put(Variable.REGION_ID, regionId);
            params.put(Variable.PAGE_NO, 1);
            params.put(Variable.PAGE_SIZE, 10);
            sendMessage(url, params, genTokenHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据区域id分页查询当前区域及子区域下设备列表
     *
     * @param accessToken
     * @param regionId
     */
    private static void getAllDeviceListNew(String accessToken, String regionId) {
        String url = getUrl(Constant.PRO_DOMAIN, GET_ALL_DEVICE_LIST_NEW_URI);
        try {
            Map<String, Object> params = new HashMap<>(4);
            params.put(Variable.ACCESS_TOKEN, accessToken);
            params.put(Variable.CUS_REGION_ID, regionId);
            params.put(Variable.LAST_ID, 0);
            params.put(Variable.PAGE_SIZE, 10);
            sendMessage(url, params, genTokenHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
