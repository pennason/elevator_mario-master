package com.cn21.open.javaDemo.platformApplication;

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
 * @Description 用户无感知获取令牌-平台应用获取访问令牌测试demo
 * 1、平台应用在访问接口前，必须通过门户中“应用管理-成员管理”模块添加企业主。
 * 2、平台应用未打开企业主免授权的场景下，并且要求企业主通过下发的授权提醒“短信”或“邮件”点开授权界面完成授权行为。
 * 3、平台应用打开企业主免授权的场景，需要由平台应用所有单位上传“企业主免授权”申请函件加盖单位公章，代替设备所有者（企业主）授权，此时企业主将不会收到授权提醒“短信”或“邮件”。
 * 4、以上两种场景添加企业主成功后，请求能力开放服务接口时，必须携带 enterpriseUser 参数（授权通过的企业主手机号）查询设备清单、拉取设备视频等能力。否则报：30045，企业主应用授权为空
 * 若有其他问题，请参考门户（示例了常见问题）：天翼视联-能力开放门户Q&A，地址：https://vcp.dlife.cn/portal/QA
 * @Date 2023/7/1415:22
 */
public class PlatformDemo extends BaseDemo {
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
        //获取访问令牌accessToken
        getAccessToken();
        //刷新token
//        getAccessTokenByRefreshToken("DuyDLLEB34VwmGn9s1IslsId1z4b9nBa");
        //定时刷新accessToken实现保活机制简单示例
//        refreshTokenByScheduled();
        //获取监控目录树
//        getReginWithGroupList("FhnZR2HVCN5FmPvjmmwYfxnKB5jbXEJW");
//        getDeviceList("FhnZR2HVCN5FmPvjmmwYfxnKB5jbXEJW","65881055");
//        getAllDeviceListNew("FhnZR2HVCN5FmPvjmmwYfxnKB5jbXEJW","65881055");
    }

    /**
     * 平台应用，获取能力开放的accessToken
     *
     * @param
     */
    private static void getAccessToken() {
        String url = getUrl(Constant.PRO_DOMAIN, GET_ACCESS_TOKEN_URI);
        try {
            Map<String, Object> params = new HashMap<>(2);
            //根据获取令牌或者刷新令牌场景选择以下两个值之一：
            //"vcp_189"：平台应用获取访问令牌
            //"refresh_token"：平台应用刷新令牌
            params.put(Variable.GRANT_TYPE, "vcp_189");
            sendMessage(url, params, genTokenHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过getAccessToken接口刷新访问令牌，合作方可通过此接口实现自己的令牌保活机制
     * 平台应用accessToke有消息为7天，refreshToken有效期为30天，通过此接口，可获取一个全新生命周期的（新的）accessToken和refreshToken
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
     * 合作方可通过此接口实现自己的令牌保活机制示例
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
        //1天后开始执行，1天刷新一次，平台应用refreshToken有效期是30天，时间长度自行斟酌
        scheduledExecutorService.scheduleAtFixedRate(refreshTask, 1, 1, TimeUnit.DAYS);
    }

    /**
     * 示例接口，若返回错误，请参考门户：天翼视联-能力开放门户Q&A，地址：https://vcp.dlife.cn/portal/QA
     * 测试，查询监控目录-获取监控目录接口
     * 此接口只是获取监控目录，获取全量设备设备需要遍历目录，获取id（regionId）后调后getDeviceList获取级联设备，getAllDeviceListNew接口也可以获取级联设备，自行酌情调用
     *
     * @param accessToken
     */
    private static void getReginWithGroupList(String accessToken) {
        String url = getUrl(Constant.PRO_DOMAIN, GET_REGIN_WITH_GROUP_LIST_URI);
        try {
            // 接口定义具体入参
            Map<String, Object> params = new HashMap<String, Object>(2);
            // 平台应用,授权的企业主账号
            params.put(Variable.ENTERPRISE_USER, Constant.ENTERPRISE_USER);
            params.put("regionId", "");
            params.put(Variable.ACCESS_TOKEN, accessToken);
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
            // 平台应用,授权的企业主账号
            params.put(Variable.ENTERPRISE_USER, Constant.ENTERPRISE_USER);
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
            // 平台应用,授权的企业主账号
            params.put(Variable.ENTERPRISE_USER, Constant.ENTERPRISE_USER);
            params.put(Variable.CUS_REGION_ID, regionId);
            params.put(Variable.LAST_ID, 0);
            params.put(Variable.PAGE_SIZE, 10);
            sendMessage(url, params, genTokenHeader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
