package com.shmashine.api.util;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSON;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.common.constants.RedisConstants;

/**
 * 仪电平台请求工具类
 *
 * @author  jiangheng
 * @version 2023/4/26 17:49
 */
@Component
public class YiDianPlatformRequestUtils {


    private static final String BASE_URL = "http://www.smartelevator.net";
    private static final String NAME = "sys_shmx";
    private static final String PASSWORD = "i*esa-t0013C";
    private static final String DEFAULT_TOKEN_URL = BASE_URL + "/iot/api/v1/login";

    @Resource
    private RedisUtils redisUtils;

    /**
     * 基本信息接URL
     */
    private static final String DEFAULT_QUERY_PLACE_NAME_URL = BASE_URL
            + "/platform/api/v1/liftBaseInfos/queryPlaceName/latest?placeName=ruijin";
    /**
     * 检验信息接URL
     */
    private static final String DEFAULT_LIFT_INSPECTS_URL = BASE_URL
            + "/platform/api/v1/liftInspects/queryPlaceName/latest?lastModifiedDate=0&placeName=ruijin";
    /**
     * 维保信息接URL
     */
    private static final String DEFAULT_LIFT_MAIN_PENANCE_USES_URL = BASE_URL
            + "/platform/api/v1/liftMaintenanceUses/queryPlaceName/latest?lastModifiedDate=0&placeName=ruijin";
    /**
     * 工单信息接URL
     */
    private static final String DEFAULT_MAINTENANCE_RECORDS_URL = BASE_URL
            + "/platform/api/v1/maintenanceRecords/queryPlaceName/latest?lastModifiedDate={}&placeName=ruijin";

    private static final String EVENT_REPORTS_EXCEL_URL = BASE_URL + "/platform/api/v1/eventReports/repair/excel";

    private static final String MAINTENANCE_REPORTS_EXCEL_URL = BASE_URL
            + "/platform/api/v1/maintenanceRecords/maintenance/excel";


    /**
     * 获取急修工单excel
     *
     * @param registerNumber 电梯唯一注册码
     * @param startTime      - 2020-11-01
     * @param endTime        -  2020-11-02
     */
    public String getRepairOrderExcel(String registerNumber, String startTime, String endTime) {

        //构建请求参数
        String reqBody = JSON.toJSONString(Map.of("registerNumberList", Arrays.asList(registerNumber),
                "startTime", startTime,
                "endTime", endTime));

        String resp = HttpRequest.post(EVENT_REPORTS_EXCEL_URL).body(reqBody)
                .header("authorization", getToken()).execute().body();

        return JSON.parseObject(resp).getJSONObject("result").getString("fileUrl");

    }


    public String getMaintenanceOrderExcel(String registerNumber, String startTime, String endTime) {

        //构建请求参数
        String reqBody = JSON.toJSONString(Map.of("registerNumberList", Arrays.asList(registerNumber),
                "startTime", startTime,
                "endTime", endTime));

        String resp = HttpRequest.post(MAINTENANCE_REPORTS_EXCEL_URL).body(reqBody)
                .header("authorization", getToken())
                .execute().body();

        return JSON.parseObject(resp).getJSONObject("result").getString("fileUrl");
    }


    /**
     * 获取token
     */
    private String getToken() {

        String token = redisUtils.get(RedisConstants.SH_YIDIAN_PLATFORM_TOKEN);

        if (StringUtils.hasText(token)) {
            return token;
        }

        String body = JSON.toJSONString(Map.of("username", NAME,
                "password", PASSWORD));
        String resp = HttpUtil.post(DEFAULT_TOKEN_URL, body);
        var respObject = JSON.parseObject(resp);
        var result = respObject.getJSONObject("result");
        token = result.getString("token");
        Long expirationTime = result.getLong("expirationTime");

        redisUtils.set(RedisConstants.SH_YIDIAN_PLATFORM_TOKEN,
                token, (expirationTime - System.currentTimeMillis()));

        return token;
    }

}
