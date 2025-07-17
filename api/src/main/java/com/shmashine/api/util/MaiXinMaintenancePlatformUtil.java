package com.shmashine.api.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.hutool.http.HttpRequest;

import com.alibaba.fastjson2.JSON;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.EmergencyRescueDetailRespDTO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.EmergencyRescueOrderPageResultDTO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.FaultMessageReqVO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.MaintenanceDetailRespDTO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.MaintenancePageResultDTO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.MaintenancePlatformElevatorRespDTO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.RepairDetailRespDTO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.RepairOrderPageResultDTO;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.entity.TblVillage;

/**
 * 麦信维保平台对接
 *
 * @author  jiangheng
 * @version 2023/10/12 11:20
 */
@Component
public class MaiXinMaintenancePlatformUtil {

    private static final String BASE_URL = "https://weibao.shmashine.com/admin-api/";

    /**
     * 获取token地址
     */
    private static final String GET_TOKEN_URL = BASE_URL + "system/oauth2/token";

    /**
     * 获取维保系统客户信息
     */
    private static final String GET_CUSTOMER_INFO_URL = BASE_URL + "shmashine-contract/contract-customer/getByPhone";

    /**
     * 获取客户电梯列表
     */
    private static final String GET_CUSTOMER_ELEVATORS_URL = BASE_URL
            + "shmashine-contract/contract-customer/getElevatorsByPhone";

    /**
     * 获取客户小区电梯列表
     */
    private static final String GET_CUSTOMER_VILLAGE_ELEVATORS_URL = BASE_URL
            + "shmashine-contract/contract-customer/getElevatorsByPhoneAndVillageName";

    /**
     * 获取客户维保单列表
     */
    private static final String GET_CUSTOMER_MAINTENANCE_ORDERS_URL = BASE_URL
            + "shmashine-contract/contract-customer/getMaintenanceOrdersByPhone";

    /**
     * 获取客户维修单列表
     */
    private static final String GET_CUSTOMER_REPAIR_ORDERS_URL = BASE_URL
            + "shmashine-contract/contract-customer/getRepairOrdersByPhone";

    /**
     * 获取客户紧急救援单列表
     */
    private static final String GET_CUSTOMER_EMERGENCY_RESCUE_ORDERS_URL = BASE_URL
            + "shmashine-contract/contract-customer/getEmergencyRescueOrderByPhone";

    /**
     * 根据ID获取维保单详情
     */
    private static final String GET_CUSTOMER_MAINTENANCE_DETAIL_URL = BASE_URL
            + "shmashine-contract/contract-customer/getMaintenanceDetailById";

    /**
     * 根据ID获取维修单详情
     */
    private static final String GET_CUSTOMER_REPAIR_DETAIL_URL = BASE_URL
            + "shmashine-contract/contract-customer/getRepairDetailById";

    /**
     * 根据ID获取救援单详情
     */
    private static final String GET_CUSTOMER_EMERGENCY_RESCUE_DETAIL_URL = BASE_URL
            + "shmashine-contract/contract-customer/getEmergencyRescueDetailById";

    /**
     * 获取客户小区列表
     */
    private static final String GET_CUSTOMER_VILLAGES_URL = BASE_URL
            + "shmashine-contract/contract-customer/getVillagesByPhone";

    /**
     * 根据电梯id获取电梯列表
     */
    private static final String GET_CUSTOMER_ELEVATORS_BYIDS_URL = BASE_URL + "shmashine-elevator/elevator/list";

    /**
     * 推送维保平台困人确认
     */
    private static final String PUSH_EMERGENCY_RESCUE_CONFIRM = BASE_URL
            + "shmashine-maintenance/emergency-rescue/pushEmergencyRescueConfirm";

    /**
     * 推送维保平台困人取消
     */
    private static final String PUSH_EMERGENCY_RESCUE_CANCEL = BASE_URL
            + "shmashine-maintenance/emergency-rescue/pushEmergencyRescueCancel";

    /**
     * 故障推送
     */
    private static final String PUSH_FAULT_MESSAGE = BASE_URL + "shmashine-message/fault/device-message/push";


    /**
     * 推送待确认故障-困人工单
     */
    private static final String PUSH_EMERGENCY_RESCUE_MESSAGE = BASE_URL
            + "shmashine-message/fault/emergency-rescue/push";

    private static final Map GET_TOKEN_FORM = Map.of(
            "grant_type", "password",
            "username", "ShmashineDataPlatform",
            "password", "123456",
            "client_id", "ShmashineDataPlatform",
            "client_secret", "asbfdkjhsahgfdasfpodsjfo",
            "scope", "shmashine:contract-customer:query"
    );

    @Resource
    private StringRedisTemplate redisTemplateStr;

    /**
     * 维保困人确认
     *
     * @param faultId 故障id
     */
    public String pushEmergencyRescueConfirm(String faultId) {

        String token = getToken();

        //通知维保确认
        String body = HttpRequest.post(PUSH_EMERGENCY_RESCUE_CONFIRM).form(Map.of("alarmId", faultId)).bearerAuth(token)
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONObject("data");

        if (data == null) {
            return null;
        }

        return data.toString();
    }

    /**
     * 维保困人取消
     *
     * @param faultId 故障id
     */
    public String pushEmergencyRescueCancel(String faultId) {

        String token = getToken();

        //通知维保确认
        String body = HttpRequest.post(PUSH_EMERGENCY_RESCUE_CANCEL).form(Map.of("alarmId", faultId)).bearerAuth(token)
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONObject("data");

        if (data == null) {
            return null;
        }

        return data.toString();
    }


    /**
     * 根据用户手机号获取客户信息
     *
     * @param phone 手机号
     */
    public String getCustomerByPhone(String phone) {

        String token = getToken();

        String body = HttpRequest.get(GET_CUSTOMER_INFO_URL).form(Map.of("phone", phone)).bearerAuth(token)
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONObject("data");

        if (data == null) {
            return null;
        }

        return data.toString();
    }


    /**
     * 获取电梯列表
     *
     * @param phone 手机号
     */
    public List<MaintenancePlatformElevatorRespDTO> getElevatorList(String phone) {


        String body = HttpRequest.get(GET_CUSTOMER_ELEVATORS_URL).form(Map.of("phone", phone)).bearerAuth(getToken())
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONArray("data");

        if (data == null) {
            return null;
        }

        var maintenanceElevators = JSON.parseArray(JSON.toJSONString(data), MaintenancePlatformElevatorRespDTO.class);

        return maintenanceElevators;
    }

    /**
     * 获取电梯列表-根据小区name
     */
    public List<MaintenancePlatformElevatorRespDTO> getElevatorListByVillageName(String phone, String villageName) {

        String body = HttpRequest.get(GET_CUSTOMER_VILLAGE_ELEVATORS_URL)
                .form(Map.of("phone", phone, "villageName", villageName))
                .bearerAuth(getToken())
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONObject("data");

        if (data == null) {
            return null;
        }

        var maintenanceElevators = JSON.parseArray(JSON.toJSONString(data), MaintenancePlatformElevatorRespDTO.class);

        return maintenanceElevators;
    }

    /**
     * 获取麦信维保平台维保单列表
     *
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param orderStatus 工单状态 [0：正常，1：超期]
     * @param communityId 小区id
     * @param phone       手机号
     * @param pageNo      页码
     * @param pageSize    每页条数
     */
    public MaintenancePageResultDTO getMaintenanceOrderPage(String phone, String startTime, String endTime,
                                                            Integer orderStatus, String communityId, Integer pageNo,
                                                            Integer pageSize) {

        HashMap<String, Object> reqData = new HashMap<>();
        reqData.put("phone", phone);
        if (StringUtils.hasText(startTime)) {
            reqData.put("startTime", startTime);
        }
        if (StringUtils.hasText(endTime)) {
            reqData.put("endTime", endTime);
        }
        reqData.put("pageNo", pageNo);
        reqData.put("pageSize", pageSize);
        reqData.put("orderStatus", orderStatus);
        reqData.put("communityId", communityId);

        String body = HttpRequest.get(GET_CUSTOMER_MAINTENANCE_ORDERS_URL).form(reqData).bearerAuth(getToken())
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONObject("data");

        if (data == null) {
            return null;
        }

        var maintenanceOrders = JSON.parseObject(JSON.toJSONString(data), MaintenancePageResultDTO.class);

        return maintenanceOrders;
    }

    /**
     * 获取麦信维保平台维修单列表
     *
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param orderStatus 工单状态 [0：正常，1：超期]
     * @param phone       手机号
     * @param pageNo      页码
     * @param pageSize    每页条数
     */
    public RepairOrderPageResultDTO getRepairOrderPage(String phone, String startTime, String endTime,
                                                       Integer orderStatus, Integer pageNo, Integer pageSize) {

        HashMap<String, Object> reqData = new HashMap<>();
        reqData.put("phone", phone);
        if (StringUtils.hasText(startTime)) {
            reqData.put("startTime", startTime);
        }
        if (StringUtils.hasText(endTime)) {
            reqData.put("endTime", endTime);
        }
        reqData.put("pageNo", pageNo);
        reqData.put("pageSize", pageSize);
        reqData.put("orderStatus", orderStatus);

        String body = HttpRequest.get(GET_CUSTOMER_REPAIR_ORDERS_URL).form(reqData).bearerAuth(getToken())
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONObject("data");

        if (data == null) {
            return null;
        }

        var repairOrderPageOrders = JSON.parseObject(JSON.toJSONString(data), RepairOrderPageResultDTO.class);

        return repairOrderPageOrders;
    }

    /**
     * 获取麦信维保平台急救单列表
     *
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param orderStatus 工单状态 [0：正常，1：超期]
     * @param phone       手机号
     * @param pageNo      页码
     * @param pageSize    每页条数
     */
    public EmergencyRescueOrderPageResultDTO getEmergencyRescueOrderByPhone(String phone, String startTime,
                                                                            String endTime,
                                                                            Integer orderStatus,
                                                                            Integer pageNo, Integer pageSize) {

        HashMap<String, Object> reqData = new HashMap<>();
        reqData.put("phone", phone);
        if (StringUtils.hasText(startTime)) {
            reqData.put("startTime", startTime);
        }
        if (StringUtils.hasText(endTime)) {
            reqData.put("endTime", endTime);
        }
        reqData.put("pageNo", pageNo);
        reqData.put("pageSize", pageSize);
        reqData.put("orderStatus", orderStatus);

        String body = HttpRequest.get(GET_CUSTOMER_EMERGENCY_RESCUE_ORDERS_URL).form(reqData).bearerAuth(getToken())
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONObject("data");

        if (data == null) {
            return null;
        }

        var emergencyRescueOrders = JSON.parseObject(JSON.toJSONString(data), EmergencyRescueOrderPageResultDTO.class);

        return emergencyRescueOrders;
    }

    /**
     * 获取客户小区列表
     */
    public List<TblVillage> getVillageList(String phone, String villageName) {

        HashMap<String, Object> reqData = new HashMap<>();
        reqData.put("phone", phone);
        reqData.put("villageName", villageName);


        String body = HttpRequest.get(GET_CUSTOMER_VILLAGES_URL).form(reqData).bearerAuth(getToken())
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONArray("data");

        if (data == null) {
            return null;
        }

        List<TblVillage> villages = data.stream().map(d -> {

            var entries = JSON.parseObject(JSON.toJSONString(d));
            return TblVillage.builder()
                    .vVillageId(entries.getString("id"))
                    .vVillageName(entries.getString("communityName"))
                    .build();
        }).collect(Collectors.toList());

        return villages;

    }

    /**
     * 根据电梯id获取电梯列表
     */
    public List<MaintenancePlatformElevatorRespDTO> getElevatorListByIds(List<String> elevatorIds) {

        String body = HttpRequest.get(GET_CUSTOMER_ELEVATORS_BYIDS_URL)
                .form(Map.of("ids", elevatorIds))
                .bearerAuth(getToken())
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONObject("data");

        if (data == null) {
            return null;
        }

        var maintenanceElevators = JSON.parseArray(JSON.toJSONString(data), MaintenancePlatformElevatorRespDTO.class);

        return maintenanceElevators;
    }

    /**
     * 根据维保单id获取维保详情
     */
    public MaintenanceDetailRespDTO getMaintenanceDetailById(String id) {

        String body = HttpRequest.get(GET_CUSTOMER_MAINTENANCE_DETAIL_URL).form(Map.of("id", id)).bearerAuth(getToken())
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONObject("data");

        if (data == null) {
            return null;
        }

        var maintenanceDetail = JSON.parseObject(JSON.toJSONString(data), MaintenanceDetailRespDTO.class);

        return maintenanceDetail;
    }

    /**
     * 根据ID获取维修单详情
     */
    public RepairDetailRespDTO getRepairDetailById(String id) {

        String body = HttpRequest.get(GET_CUSTOMER_REPAIR_DETAIL_URL).form(Map.of("id", id)).bearerAuth(getToken())
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONObject("data");

        if (data == null) {
            return null;
        }

        var maintenanceDetail = JSON.parseObject(JSON.toJSONString(data), RepairDetailRespDTO.class);

        return maintenanceDetail;
    }

    /**
     * 根据ID获取救援单详情
     */
    public EmergencyRescueDetailRespDTO getEmergencyRescueDetailById(String id) {

        String body = HttpRequest.get(GET_CUSTOMER_EMERGENCY_RESCUE_DETAIL_URL).form(Map.of("id", id))
                .bearerAuth(getToken())
                .timeout(6000).execute().body();

        var data = JSON.parseObject(body).getJSONObject("data");

        if (data == null) {
            return null;
        }

        var maintenanceDetail = JSON.parseObject(JSON.toJSONString(data), EmergencyRescueDetailRespDTO.class);

        return maintenanceDetail;
    }


    /**
     * 推送待确认故障-困人工单
     *
     * @param faultMessageReqVO 故障消息
     */
    public void pushEmergencyRescue(FaultMessageReqVO faultMessageReqVO) {

        if (StringUtils.hasText(faultMessageReqVO.getRegisterNumber())) {

            String token = getToken();
            String body = HttpRequest.post(PUSH_EMERGENCY_RESCUE_MESSAGE)
                    .body(JSON.toJSONString(faultMessageReqVO)).bearerAuth(token)
                    .timeout(6000).execute().body();

            Integer code = JSON.parseObject(body).getInteger("code");

        }

    }

    /// //////////////////////////////////////private//////////////////////////////////////////////////////

    private String getToken() {

        String token = redisTemplateStr.opsForValue().get(RedisConstants.MAIXIN_MAINTENANCE_PLATFORM_TOKEN);

        if (!StringUtils.hasText(token)) {

            String body = HttpRequest.post(GET_TOKEN_URL).form(GET_TOKEN_FORM).header("tenant-id", "1")
                    .timeout(6000).execute().body();

            token = JSON.parseObject(body).getJSONObject("data").getString("access_token");

            redisTemplateStr.opsForValue()
                    .set(RedisConstants.MAIXIN_MAINTENANCE_PLATFORM_TOKEN, token, 1600L, TimeUnit.SECONDS);

        }

        return token;
    }
}
