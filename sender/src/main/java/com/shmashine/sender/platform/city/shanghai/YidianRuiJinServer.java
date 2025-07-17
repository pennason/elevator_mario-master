package com.shmashine.sender.platform.city.shanghai;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblThirdPartyRuijinElevator;
import com.shmashine.common.entity.TblThirdPartyRuijinMaintenance;
import com.shmashine.common.entity.TblThirdPartyRuijinWorkOrder;
import com.shmashine.common.entity.TblThridPartyRuijinCheck;
import com.shmashine.common.utils.HttpTools;
import com.shmashine.sender.mapstruct.TblThirdPartyRuijinElevatorMapStruct;
import com.shmashine.sender.mapstruct.TblThirdPartyRuijinMaintenanceMapStruct;
import com.shmashine.sender.server.elevator.BizElevatorService;
import com.shmashine.sender.server.ruijin.TblThirdPartyRuijinElevatorService;
import com.shmashine.sender.server.ruijin.TblThirdPartyRuijinMaintenanceService;
import com.shmashine.sender.server.ruijin.TblThirdPartyRuijinWorkOrderService;
import com.shmashine.sender.server.ruijin.TblThridPartyRuijinCheckService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 瑞金医院数据 迁移服务 （来自仪电）
 *
 * @author LiuLiFu
 * @version v1.0  -  2020/7/24 15:30
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class YidianRuiJinServer {
    private final TblThirdPartyRuijinElevatorService tblThirdPartyRuijinElevatorService;
    private final TblThridPartyRuijinCheckService tblThridPartyRuijinCheckService;
    private final TblThirdPartyRuijinMaintenanceService tblThirdPartyRuijinMaintenanceService;
    private final TblThirdPartyRuijinWorkOrderService tblThirdPartyRuijinWorkOrderService;
    private final BizElevatorService bizElevatorService;
    private final YidianHttpUtil yidianHttpUtil;

    private static final String BASE_URL = "http://www.smartelevator.net";
    //    private static final String BASE_URL = "http://test.smartelevator.net";

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


    public void updateThirdPartyRuiJinElevator() {
        var list = getList(DEFAULT_QUERY_PLACE_NAME_URL, TblThirdPartyRuijinElevator.class);
        log.info("updateThirdPartyRuiJinElevatorTask>>>>>电梯基本信息获取 大小: {}", list.size());
        if (!list.isEmpty()) {
            for (var elevator : list) {
                var registerNumber = elevator.getRegisterNumber();
                var rjElevatorInfo = tblThirdPartyRuijinElevatorService.getByRegisterNumber(registerNumber);
                if (null == rjElevatorInfo) {
                    log.info("updateThirdPartyRuiJinElevatorTask>>>>>无结果时处理");
                    tblThirdPartyRuijinElevatorService.insert(elevator);
                } else {
                    elevator.setId(rjElevatorInfo.getId());
                    tblThirdPartyRuijinElevatorService.updateById(TblThirdPartyRuijinElevatorMapStruct.INSTANCE
                            .entityToDo(elevator));
                }
            }
        }
    }


    public void updateThridPartyRuiJinCheck() {
        // 电梯检验信息
        long totalPage = getTotalPage(DEFAULT_LIFT_INSPECTS_URL);
        for (int i = 0; i < totalPage; i++) {
            updateThridPartyRuiJinCheckByPage(i);
        }
    }

    public void updateThridPartyRuiJinCheckByPage(int page) {
        List<TblThridPartyRuijinCheck> list = getListForPage(DEFAULT_LIFT_INSPECTS_URL, page,
                TblThridPartyRuijinCheck.class);
        if (!list.isEmpty()) {
            // 更新年检信息
            for (TblThridPartyRuijinCheck check : list) {
                try {
                    String reportNumber = check.getReportNumber();
                    var checkInfo = tblThridPartyRuijinCheckService.getElevatorCheckInfo(reportNumber);
                    if (checkInfo == null) {

                        //维保是否超期
                        var overdue = 0;
                        Date nextMaintainDate = (Date) bizElevatorService.getByElevatorCode(check.getRegisterNumber())
                                .getDNextInspectDate();
                        Date completeTime = check.getInspectDate();
                        if (completeTime.after(nextMaintainDate)) {
                            overdue = 1;
                        }

                        check.setOverdue(overdue);

                        tblThridPartyRuijinCheckService.insert(check);
                    }
                } catch (Exception e) {
                    log.error("updateThridPartyRuiJinCheckByPage>>>>>电梯检验信息处理异常: {} {}",
                            check.getRegisterNumber(), ExceptionUtil.stacktraceToString(e));
                }
            }
        }
    }


    public void updateThirdPartyRuiJinMaintenance() {
        // 电梯维保信息
        long totalPage = getTotalPage(DEFAULT_LIFT_MAIN_PENANCE_USES_URL);
        for (int i = 0; i < totalPage; i++) {
            updateThirdPartyRuiJinMaintenanceByPage(i);
        }
    }

    public void updateThirdPartyRuiJinMaintenanceByPage(int page) {
        var list = getListForPage(DEFAULT_LIFT_MAIN_PENANCE_USES_URL, page, TblThirdPartyRuijinMaintenance.class);
        if (!list.isEmpty()) {
            //保养
            for (var maintenance : list) {
                try {
                    String registerNumber = maintenance.getRegisterNumber();
                    var maintenanceInfo = tblThirdPartyRuijinMaintenanceService
                            .getMaintenanceInfoByRegisternumber(registerNumber);
                    if (null == maintenanceInfo) {
                        tblThirdPartyRuijinMaintenanceService.insert(maintenance);
                    } else {
                        maintenance.setId(maintenanceInfo.getId());
                        tblThirdPartyRuijinMaintenanceService.updateById(
                                TblThirdPartyRuijinMaintenanceMapStruct.INSTANCE.entityToDo(maintenance));
                    }
                } catch (Exception e) {
                    log.error("维保信息更新异常: {} {}", e.getMessage(), ExceptionUtil.stacktraceToString(e));
                }

            }
        }
    }


    public void updateThirdPartyRuiJinWorkOrder() {
        long totalPage = getTotalPage(StrUtil.format(DEFAULT_MAINTENANCE_RECORDS_URL,
                DateUtil.lastMonth().getTime()));
        if (totalPage > 0) {
            for (int page = 0; page < totalPage; page++) {
                updateThirdPartyRuiJinWorkOrderByPage(page);
            }
        }
    }

    public void updateThirdPartyRuiJinWorkOrderByPage(int page) {

        List<TblThirdPartyRuijinWorkOrder> list = getListForPage(StrUtil.format(DEFAULT_MAINTENANCE_RECORDS_URL,
                DateUtil.lastMonth().getTime()), page, TblThirdPartyRuijinWorkOrder.class);

        if (!list.isEmpty()) {

            List<String> regsNumber = new ArrayList<>();
            HashMap<String, TblThirdPartyRuijinWorkOrder> workOrderHashMap = new HashMap<>();

            for (TblThirdPartyRuijinWorkOrder order : list) {
                regsNumber.add(order.getWorkOrderNumber());
                workOrderHashMap.put(order.getWorkOrderNumber(), order);
            }

            //已存在工单
            var workOrderNumbers = tblThirdPartyRuijinWorkOrderService
                    .searchWorkOrderNumberByWorkOrderNumbers(regsNumber);

            //已存在工单不需要更新&添加
            regsNumber.removeAll(workOrderNumbers);

            for (String item : regsNumber) {
                try {
                    updateThirdPartyRuiJinWorkOrderInfo(workOrderHashMap.get(item));
                } catch (Exception e) {
                    log.error("维保信息更新异常: {} {}", e.getMessage(), ExceptionUtil.stacktraceToString(e));
                }
            }

        }
    }

    public void updateThirdPartyRuiJinWorkOrderInfo(TblThirdPartyRuijinWorkOrder order) {

        //维保是否超期
        var overdue = 0;

        TblElevator byElevatorCode = bizElevatorService.getByElevatorCode(order.getRegisterNumber());

        if (byElevatorCode == null) {
            return;
        }

        Date nextMaintainDate = (Date) byElevatorCode.getDNextMaintainDate();
        Date completeTime = order.getCompleteTime();
        if (nextMaintainDate != null && completeTime.after(DateUtil.endOfDay(nextMaintainDate))) {
            overdue = 1;
        }

        order.setOverdue(overdue);

        //新的维保工单才需要更新时间
        if (null != order.getCompleteTime()) {
            bizElevatorService.updateMaintainDate(order.getRegisterNumber(), order.getCompleteTime());
        }

        tblThirdPartyRuijinWorkOrderService.insert(order);

    }


    public <T> List<T> getList(String url, Class<T> clazz) {
        int size = 40;
        int page = 0;
        var list = getListForPage(url, size, page, clazz);
        if (!CollectionUtils.isEmpty(list)) {
            return list;
        }
        //        int retry = 3;
        //        while (retry-- > 0) {
        //            List<T> list = getListForPage(url, size, page, clazz);
        //            if (null != list) {
        //                return list;
        //            }
        //        }
        return new ArrayList<T>();
    }

    /**
     * 获取分页数据
     */
    private <T> List<T> getListForPage(String url, int size, int page, Class<T> clazz) {
        var list = new ArrayList<T>();
        JSONObject response = getInfoList(url, size, page);
        JSONObject result = null;
        if (response != null) {
            result = response.getJSONObject("result");
        }
        Integer totalElements = 0;
        if (result != null) {
            totalElements = result.getInteger("totalElements");
        }
        if (result != null) {
            list.addAll(JSON.parseArray(result.getString("content"), clazz));
        }

        //获取全部电梯基本信息信息
        if (totalElements > size) {
            if (totalElements % size == 0) {
                page = totalElements / size;
            } else {
                page = (totalElements / size) + 1;
            }
        }

        if (page > 1) {
            for (int i = 1; i <= page; i++) {
                response = getInfoList(url, size, i);
                if (response != null) {
                    result = response.getJSONObject("result");
                }
                if (result != null) {
                    list.addAll(JSON.parseArray(result.getString("content"), clazz));
                }
            }
        }
        return list;
    }

    private <T> List<T> getListForPage(String url, int page, Class<T> clazz) {
        JSONObject response = getInfoList(url, 50, page);
        JSONObject result = null;
        if (response != null) {
            result = response.getJSONObject("result");
        }
        ArrayList<T> list = null;
        if (result != null) {
            list = new ArrayList<T>(JSON.parseArray(result.getString("content"), clazz));
        }
        return list;
    }

    private JSONObject getInfoList(String url, int size, int page) {
        HttpTools httpTool = new HttpTools();
        Object[] objects = httpTool.get(url + "&size=" + size + "&page=" + page, yidianHttpUtil.getHeadMap());
        // 200表示请求成功
        int statusCode = (int) objects[0];
        if (statusCode != 200) {
            // TODO token失效重试
            return null;
        }
        List<T> list = new ArrayList<>();
        // 返回报文body
        String responseBody = (String) objects[1];
        // 判断是否还有数据
        return JSONObject.parseObject(responseBody);
    }

    public Long getTotalPage(String url) {
        HttpTools httpTool = new HttpTools();
        Object[] objects = httpTool.get(url + "&size=" + 1 + "&page=" + 1, yidianHttpUtil.getHeadMap());
        // 200表示请求成功
        int statusCode = (int) objects[0];
        if (statusCode != 200) {
            // TODO token失效重试
            return 0L;
        }
        var list = new ArrayList<T>();
        // 返回报文body
        String responseBody = (String) objects[1];
        // 判断是否还有数据
        JSONObject response = JSONObject.parseObject(responseBody);

        JSONObject result = response.getJSONObject("result");
        Integer totalElements = result.getInteger("totalElements");

        long isZero = totalElements % 50;
        long page = 0;
        if (isZero == 0) {
            page = totalElements / 50;
        } else {
            page = totalElements / 50 + 1;
        }

        return page;
    }

}
