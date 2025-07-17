package com.cn21.open.javaDemo.standardApplication;

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
 * @Description 天翼账号鉴权兑换令牌-标准API应用获取能力开放accessToken接入天翼账号）测试demo
 * 前提：合作方已对接天翼账号或要求并准备接入天翼账号,如果要接入天翼账号，请参考门户文档接入指南（一）
 * 此demo只是示例已对接了天翼账号的应用获取能力开放的访问令牌流程，通过天翼账号accessToken获取天翼账号tokenCode,再通过tokenCode置换能力开放的accessToken
 * 若有其他问题，请参考门户（示例了常见问题）：天翼视联-能力开放门户Q&A，地址：https://vcp.dlife.cn/portal/QA
 * @Date 2023/7/1317:31
 */
public class TokenCodeDemo extends BaseDemo {
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
        getAccessToken("Byey5drGKBATo0NbQf3vddzl");
//        getAccessTokenByRefreshToken("AwlUHTinavRldiSY7VAUOdbS");
//        getReginWithGroupList("ZyZwY6VXdTV2Y6Gn1wIf7IKt");
//        getDeviceList("ZyZwY6VXdTV2Y6Gn1wIf7IKt","65881057");
//        refreshTokenByScheduled();
//        getAllDeviceListNew("ZyZwY6VXdTV2Y6Gn1wIf7IKt","65881057");
    }

    /**
     * 通过天翼账号tokenCode换取能力开放accessToken
     *
     * @param tokenCode
     */
    private static void getAccessToken(String tokenCode) {
        String url = getUrl(Constant.PRO_DOMAIN, GET_ACCESS_TOKEN_URI);
        try {
            Map<String, Object> params = new HashMap<>(2);
            //根据获取令牌或者刷新令牌场景选择以下两个值之一：
            //"189_code" ：标准应用获取访问令牌
            //"refresh_token"： 标准应用刷新令牌
            params.put(Variable.GRANT_TYPE, "189_code");
            //通过天翼账号accessToken获取天翼账号tokenCode
            params.put(Variable.TOKEN_CODE, tokenCode);
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
            //通过getAccessToken接口获取的refreshToken)
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
