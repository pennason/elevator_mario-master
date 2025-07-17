package com.shmashine.api.controller.thirdparty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.config.exception.BizException;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.entity.yidian.TokenResponse;
import com.shmashine.api.entity.yidian.TokenResult;
import com.shmashine.api.module.elevator.ElevatorDetailModule;
import com.shmashine.api.module.thirdparty.ruijin.RuiJinEventModule;
import com.shmashine.api.module.thirdparty.ruijin.RuiJinEventReceiveDetailModule;
import com.shmashine.api.module.thirdparty.ruijin.RuiJinEventReceiveModule;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.fault.BizFaultDefinition0902ServiceI;
import com.shmashine.api.service.ruijin.BizThirdPartyRuijinEnventServiceI;
import com.shmashine.api.service.system.TblFaultServiceI;
import com.shmashine.api.service.system.TblThirdPartyRuijinEnventDetailServiceI;
import com.shmashine.api.service.system.TblThirdPartyRuijinEnventServiceI;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.util.JSONUtils;
import com.shmashine.api.util.PojoConvertUtil;
import com.shmashine.common.constants.BusinessConstants;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.entity.TblFault;
import com.shmashine.common.entity.TblSysUser;
import com.shmashine.common.entity.TblThirdPartyRuijinEnvent;
import com.shmashine.common.entity.TblThirdPartyRuijinEnventDetail;
import com.shmashine.common.utils.SendMessageUtil;
import com.shmashine.common.utils.SnowFlakeUtils;

/**
 * 对外接口
 */
@RestController
@RequestMapping("/RuiJin")
public class RuiJinController extends BaseRequestEntity {

    protected static final Logger mylogger = LoggerFactory.getLogger("yidianLogger");

    private TblThirdPartyRuijinEnventServiceI tblThirdPartyRuijinEnventServiceI;
    private TblThirdPartyRuijinEnventDetailServiceI tblThirdPartyRuijinEnventDetailServiceI;
    private BizElevatorService elevatorService;
    private TblFaultServiceI tblFaultServiceI;
    private BizFaultDefinition0902ServiceI bizFaultDefinition0902ServiceI;
    private BizThirdPartyRuijinEnventServiceI bizThirdPartyRuijinEnventServiceI;

    private static final String BASE_URL = "http://www.smartelevator.net";
    private static final String DEFAULT_TOKEN_URL = BASE_URL + "/iot/api/v1/login";
    private static final String name = "sys_shmx";
    private static final String password = "i*esa-t0013C";


    @Resource
    private BizUserService bizUserService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Resource
    private RedisUtils redisUtils;

    @Autowired
    public RuiJinController(TblThirdPartyRuijinEnventServiceI tblThirdPartyRuijinEnventServiceI, TblThirdPartyRuijinEnventDetailServiceI tblThirdPartyRuijinEnventDetailServiceI, BizElevatorService elevatorService, TblFaultServiceI tblFaultServiceI, BizFaultDefinition0902ServiceI bizFaultDefinition0902ServiceI, BizThirdPartyRuijinEnventServiceI bizThirdPartyRuijinEnventServiceI) {
        this.tblThirdPartyRuijinEnventServiceI = tblThirdPartyRuijinEnventServiceI;
        this.tblThirdPartyRuijinEnventDetailServiceI = tblThirdPartyRuijinEnventDetailServiceI;
        this.elevatorService = elevatorService;
        this.tblFaultServiceI = tblFaultServiceI;
        this.bizFaultDefinition0902ServiceI = bizFaultDefinition0902ServiceI;
        this.bizThirdPartyRuijinEnventServiceI = bizThirdPartyRuijinEnventServiceI;
    }

    /**
     * 瑞金医院故障事件数据接受
     *
     * @param ruiJinEventModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("receiveEventInfo")
    public Object receiveEventInfo(@RequestBody @Valid RuiJinEventModule ruiJinEventModule) {
        RuiJinEventReceiveModule value = ruiJinEventModule.getValue();
        mylogger.info("/RuiJin/receiveEventInfo:" + JSONUtils.beanToJson(value));

        RLock lock = redissonClient.getFairLock(RedisConstants.YIDIAN_EVENT_RECEIVE_LOCK
                + value.getEventNumber());

        try {

            //尝试加锁，最多等待30s，上锁以后1分钟自动解锁
            if (lock.tryLock(30, 60, TimeUnit.SECONDS)) {

                try {

                    Date date = new Date();
                    String userId = super.getUserId();

                    // 1. 读取参数
                    TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent = PojoConvertUtil.convertPojo(value, TblThirdPartyRuijinEnvent.class);
                    ArrayList<RuiJinEventReceiveDetailModule> statusLogs = value.getStatusLogs();
                    tblThirdPartyRuijinEnvent.setEventId(SnowFlakeUtils.nextStrId());
                    tblThirdPartyRuijinEnvent.setOccurTime(value.getOccurTime());
                    tblThirdPartyRuijinEnvent.setVCreateUserId(userId);
                    tblThirdPartyRuijinEnvent.setVModifyUserId(userId);
                    // 2. 转化参数
                    List<TblThirdPartyRuijinEnventDetail> tblThirdPartyRuijinEnventDetails =
                            PojoConvertUtil.convertPojos(statusLogs, TblThirdPartyRuijinEnventDetail.class);

                    for (TblThirdPartyRuijinEnventDetail item : tblThirdPartyRuijinEnventDetails) {
                        item.setDtCreateTime(date);
                        item.setDtModifyTime(date);
                        item.setVCreateUserId(userId);
                        item.setVModifyUserId(userId);
                        item.setEventId(tblThirdPartyRuijinEnvent.getEventId());
                        item.setEventDetailId(SnowFlakeUtils.nextStrId());
                    }

                    // 检查是否存在事件
                    TblThirdPartyRuijinEnvent param = new TblThirdPartyRuijinEnvent();
                    param.setEventNumber(tblThirdPartyRuijinEnvent.getEventNumber());
                    List<TblThirdPartyRuijinEnvent> byEntity = tblThirdPartyRuijinEnventServiceI.getByEntity(param);
                    if (byEntity != null && byEntity.size() != 0) { // 存在事件 - 更新操作
                        // 更新故障 全量更新
                        TblThirdPartyRuijinEnvent temp = byEntity.get(0);
                        bizThirdPartyRuijinEnventServiceI.deleteThirdPartyRuijinEnventData(temp.getEventNumber());
                        saveEvent(tblThirdPartyRuijinEnvent, tblThirdPartyRuijinEnventDetails);

                        /**
                         * 每一次平层或非平层困人时，给小刘推送的短信增加一条，逻辑如下：当困人出现后，麦信平台收到仪电反推的“已接单”状态时，
                         * 给小刘发送短信，格式为： 6-1电梯 10:20发生平层关人故障，维保人员严振已经接到报修并赶往现场救援维修。
                         * 其中时间为新建工单的时间，维保人名为仪电反推的维保工姓名。
                         */
                        if (tblThirdPartyRuijinEnvent.getCurrentStatus() == 3 && ("09".equals(tblThirdPartyRuijinEnvent.getFailureCode()))) {
                            // 推送短信到ruijin1账号
                            sendFaultMessage(tblThirdPartyRuijinEnvent, 3);
                        }

                    } else {

                        //查询仪电电梯故障报修信息，判断是否重复提交
                        if (queryYiDianEventReports(tblThirdPartyRuijinEnvent)) {

                            mylogger.info(tblThirdPartyRuijinEnvent.getEventNumber() + " ========> 新增工单,推送短信");

                            // 新增操作
                            saveEvent(tblThirdPartyRuijinEnvent, tblThirdPartyRuijinEnventDetails);

                            // 推送短信到ruijin1账号
                            sendFaultMessage(tblThirdPartyRuijinEnvent, 2);

                        } else {
                            mylogger.info("==============>查询仪电故障保修信息重复，新增工单信息为" + tblThirdPartyRuijinEnvent.toString());
                            mylogger.info("==============>查询仪电故障保修信息重复，新增工单详细信息为" + tblThirdPartyRuijinEnventDetails.toString());
                        }

                    }

                } finally {
                    lock.unlock();
                }

            }

        } catch (InterruptedException e) {
            mylogger.error("瑞金医院故障事件数据接受处理失败，error：{}", ExceptionUtils.getStackTrace(e));
        }

        return ResponseResult.success();

    }

    /**
     * 获取维保工单pdf
     *
     * @param workOrderNumber
     * @return
     */
    @PostMapping("/generateMaintenancePdf")
    public Object generateMaintenancePdf(@PathParam("workOrderNumber") String workOrderNumber) {

        String url = BASE_URL + "/platform/api/v1/maintenanceRecords/maintenance/generatePdf?workOrderNumber=" + workOrderNumber;

        return generatePDF(url);

    }

    /**
     * 获取救援工单
     *
     * @param eventNumber
     * @return
     */
    @PostMapping("/generateRescuePdf")
    public ResponseResult generateRescuePdf(@PathParam("eventNumber") String eventNumber) {

        String url = BASE_URL + "/platform/api/v1/eventReports/rescue/generatePdf?eventNumber=" + eventNumber;

        return generatePDF(url);
    }

    /**
     * 获取急修工单
     *
     * @param eventNumber
     * @return
     */
    @PostMapping("/generateRepairPdf")
    public ResponseResult generateRepairPdf(@PathParam("eventNumber") String eventNumber) {

        String url = BASE_URL + "/platform/api/v1/eventReports/repair/generatePdf?eventNumber=" + eventNumber;

        return generatePDF(url);
    }

    private ResponseResult generatePDF(String url) {
        // 请求头 设置格式为：application/json
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getToken());

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, request, HashMap.class);

        Integer status = (Integer) response.getBody().get("status");

        if (200 != status) {
            return new ResponseResult(String.valueOf(status), (String) response.getBody().get("message"));
        }

        return ResponseResult.successObj(response.getBody().get("result"));

    }

    private boolean queryYiDianEventReports(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent) {

        boolean res = true;
        try {
            String url = BASE_URL + "/platform/api/v1/eventReports/" + tblThirdPartyRuijinEnvent.getEventNumber();
            // 请求头 设置格式为：application/json
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", getToken());

            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, request, HashMap.class);

            HashMap<String, Object> result = (HashMap<String, Object>) response.getBody().get("result");

            if (result.get("masterEventNumber") != null) {
                res = false;
            }

        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return res;
    }


    public void saveEvent(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent, List<TblThirdPartyRuijinEnventDetail> tblThirdPartyRuijinEnventDetails) {
        // 1. 添加瑞金医院事件表
        int insert = tblThirdPartyRuijinEnventServiceI.insert(tblThirdPartyRuijinEnvent);
        if (insert == 0) {
            throw new BizException(ResponseResult.error());
        }
        saveEventDetail(tblThirdPartyRuijinEnventDetails);
    }

    public void saveEventDetail(List<TblThirdPartyRuijinEnventDetail> tblThirdPartyRuijinEnventDetails) {
        int i = tblThirdPartyRuijinEnventDetailServiceI.insertBatch(tblThirdPartyRuijinEnventDetails);
        if (i != tblThirdPartyRuijinEnventDetails.size()) {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 添加到故障表
     *
     * @param tblThirdPartyRuijinEnvent
     */
    public void saveFault(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent) throws Exception {
        String faultName = bizFaultDefinition0902ServiceI.getFaultDefinitionByCodeAndPlatformType(BusinessConstants.EVENT_CHANNEL, tblThirdPartyRuijinEnvent.getFailureCode());
        ElevatorDetailModule elevatorInfo = elevatorService.getElevatorByEquipmentCode(tblThirdPartyRuijinEnvent.getRegisterNumber());
        if (elevatorInfo == null) {
            throw new BizException(new ResponseResult(ResponseResult.CODE_ERROR, "msg7_01"));
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date occurTime = simpleDateFormat.parse((String) tblThirdPartyRuijinEnvent.getOccurTime());
        TblFault tblFault = new TblFault();
        tblFault.setVFaultId(tblThirdPartyRuijinEnvent.getEventNumber());
        tblFault.setVElevatorCode(elevatorInfo.getElevatorCode());
        tblFault.setVElevatorId(elevatorInfo.getElevatorId());
        tblFault.setVAddress(elevatorInfo.getAddress());
        tblFault.setDtReportTime(occurTime);
        tblFault.setDReportDate(tblThirdPartyRuijinEnvent.getOccurTime());
        tblFault.setIFaultType(tblThirdPartyRuijinEnvent.getFailureCode());
        tblFault.setVFaultName(faultName);
        tblFault.setILevel(BusinessConstants.FAULT_LEVEL_4);
        tblFault.setILevelName(BusinessConstants.FAULT_LEVEL_4_NAME);
        tblFault.setIFaultNum(BusinessConstants.FAULT_NUMBER);
        tblFault.setIStatus(tblThirdPartyRuijinEnvent.getCurrentStatus().equals(BusinessConstants.EVENT_CURRENT_FINISH) ? BusinessConstants.FAULT_STATUS_NO : BusinessConstants.FAULT_STATUS_YES);
        tblFault.setIUncivilizedBehaviorFlag(BusinessConstants.UNCIVILIZED_BEHAVIOR_FLAG_0);
        tblFault.setIModeStatus(BusinessConstants.MODULE_STATUS);
        tblFault.setIManualClear(BusinessConstants.MANUAL_CLEAR_NONE);
        tblFault.setIIsUserVisible(BusinessConstants.FAULT_PUSH_NO);
        tblFault.setVCreateUserId(getUserId());
        tblFault.setVModifyUserId(getUserId());
        tblFault.setIDelFlag(BusinessConstants.DELETE_FLAG_YES);
        tblFault.setVEventChannel(BusinessConstants.EVENT_CHANNEL);

        int insert1 = tblFaultServiceI.insert(tblFault);
        if (insert1 == 0) {
            throw new BizException(ResponseResult.error());
        }
    }

    /**
     * 更新故障表
     */
    public void updateFault(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent) {

        try {
            Date date1 = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date occurTime = simpleDateFormat.parse((String) tblThirdPartyRuijinEnvent.getOccurTime());
            Long l = date1.getTime() - occurTime.getTime() > 0 ? date1.getTime() - occurTime.getTime() : occurTime.getTime() - date1.getTime();
            TblFault tblFault = new TblFault();
            tblFault.setVFaultId(tblThirdPartyRuijinEnvent.getEventNumber());
            tblFault.setDtEndTime(date1);
            tblFault.setIStatus(BusinessConstants.FAULT_STATUS_NO);
            tblFault.setIDurationTime((l.intValue() / 1000));
            tblFaultServiceI.update(tblFault);
        } catch (ParseException e) {
            throw new BizException(ResponseResult.error());
        }

    }

    /**
     * 推送短信到ruijin1账号
     */
    private void sendFaultMessage(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent, int currentStatus) {

        try {

            //获取用户信息
            TblSysUser userInfo = bizUserService.getUserDetail("ruijin1");

            //获取电梯信息
            ElevatorDetailModule elevator = elevatorService.getElevatorByEquipmentCodeS(tblThirdPartyRuijinEnvent.getRegisterNumber());

            //获取故障信息 ——> 故障楼层
            String floor = "null";
            String reporter = tblThirdPartyRuijinEnvent.getReporter();
            String faultId = reporter.substring(5);
            TblFault fault = tblFaultServiceI.getById(faultId);
            if (fault != null) {
                floor = String.valueOf(fault.getiFloor());
            }


            if (userInfo != null && elevator != null) {
                String mobile = userInfo.getVMobile();
                String elevatorName = elevator.getElevatorName();
                String local = elevator.getAddress();
                String faultName = tblThirdPartyRuijinEnvent.getEventDesc();
                String time = (String) tblThirdPartyRuijinEnvent.getOccurTime();
                String handler = tblThirdPartyRuijinEnvent.getHandler();
                if (currentStatus == 2) {
                    //发送故障短信
                    SendMessageUtil.sendFaultMessageWithFloor(mobile, elevatorName, local, faultName, floor, time);
                }
                if (currentStatus == 3) {

                    //发送困人故障接单短信
                    //格式为： 6-1电梯 10:20发生关人故障，维保人员严振已经接到报修并赶往现场救援维修。
                    SendMessageUtil.sendGetOrderMessageWithFloor(mobile, elevatorName, handler, faultName, floor, time);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取token
     *
     * @return
     */
    private synchronized String getToken() {

        String token = redisUtils.get(RedisConstants.SH_YIDIAN_PLATFORM_TOKEN);

        if (StringUtils.hasText(token)) {
            return token;
        }

        // 请求头 设置格式为：application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 请求体 用户名/密码
        Map<String, String> bodyMap = new LinkedHashMap();
        bodyMap.put("username", name);
        bodyMap.put("password", password);
        String body = JSONObject.toJSONString(bodyMap);

        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        TokenResponse tokenResponse = restTemplate.postForObject(DEFAULT_TOKEN_URL, request, TokenResponse.class);
        if (tokenResponse != null && tokenResponse.getStatus() == 200) {
            TokenResult tokenResult = tokenResponse.getResult();
            if (tokenResult != null) {
                token = tokenResult.getToken();
                long expirationTime = tokenResult.getExpirationTime();
                redisUtils.set(RedisConstants.SH_YIDIAN_PLATFORM_TOKEN,
                        token, (expirationTime - System.currentTimeMillis()));
            }
        }
        return token;
    }
}
