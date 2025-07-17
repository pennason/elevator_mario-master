package com.shmashine.api.service.childrenHospital.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shmashine.api.dao.BizRuiJinDao;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.api.service.childrenHospital.ChildrenHospitalService;
import com.shmashine.common.utils.DateUtils;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/9/26 10:56
 */
@Service
public class ChildrenHospitalServiceImpl implements ChildrenHospitalService {

    @Autowired
    private BizRuiJinDao bizRuiJinDao;

    @Override
    public Map<String, Integer> getRadarChart(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        HashMap<String, Integer> rest = new HashMap<>();

        //本年度维保逾期次数-按期维保
        int overdueOrders = getOverdueOrders(faultStatisticalQuantitySearchModule);
        rest.put("overdueOrders", 4 - overdueOrders >= 0 ? 4 - overdueOrders : 0);

        //反复阻挡门次数-文明用梯
        int stopCount = bizRuiJinDao.queryStopCount(faultStatisticalQuantitySearchModule);
        rest.put("stopCount", 4 - (stopCount / 4) >= 0 ? 4 - (stopCount / 4) : 0);

        /*电梯故障次数-故障频次*/
        String projectId = faultStatisticalQuantitySearchModule.getvProjectId();
        int faultCount;
        //儿童医院项目——故障频次统计麦信平台故障
        if ("8115537080661639168".equals(projectId)) {
            faultCount = bizRuiJinDao.searchFaultCountForMX(faultStatisticalQuantitySearchModule);
        } else {    //其他项目——故障频次统计仪电反推故障
            faultCount = bizRuiJinDao.searchFaultCount(faultStatisticalQuantitySearchModule);
        }
        rest.put("faultCount", 4 - (faultCount / 4) >= 0 ? 4 - (faultCount / 4) : 0);

        /*电梯困人故障次数（非误报，非系统测试）-困人频次*/
        int trappedPeopleCount;
        //儿童医院项目——困人频次统计麦信平台故障
        if ("8115537080661639168".equals(projectId)) {
            trappedPeopleCount = bizRuiJinDao.searchtrappedPeopleCountForMX(faultStatisticalQuantitySearchModule);
        } else {     //其他项目——困人频次统计仪电反推故障
            trappedPeopleCount = bizRuiJinDao.searchtrappedPeopleCount(faultStatisticalQuantitySearchModule);
        }
        rest.put("trappedPeopleCount", 4 - trappedPeopleCount >= 0 ? 4 - trappedPeopleCount : 0);

        //单梯所有困人时长取平均-救援时长
        int trappedPeopleGrade = getTrappedPeopleGrade(faultStatisticalQuantitySearchModule);
        rest.put("trappedPeopleGrade", trappedPeopleGrade);

        //每月平均每小时运行次数-运行频度
        int runCount = getCount(faultStatisticalQuantitySearchModule);
        rest.put("runCount", runCount);

        return rest;
    }

    //平均每小时运行次数
    private int getCount(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        Integer runCount = bizRuiJinDao.getRunCount(faultStatisticalQuantitySearchModule);
        int count;

        if (runCount < 5) {
            count = 0;
        } else if (5 <= runCount && runCount < 20) {
            count = 1;
        } else if (20 <= runCount && runCount < 35) {
            count = 2;
        } else if (35 <= runCount && runCount < 50) {
            count = 3;
        } else {
            count = 4;
        }
        /*--------------------------------------------------*/
//        switch (runCount/7200) {
//            case 0:
//            case 1:
//                count = 0;
//                break;
//            case 2:
//                count = 1;
//                break;
//            case 3:
//                count = 2;
//                break;
//            case 4:
//                count = 3;
//                break;
//            default:
//                count = 4;
//                break;
//        }
        return count;
    }

    //困人平均时长
    private int getTrappedPeopleGrade(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setTimeFlag("22");

        //儿童医院项目——救援时长统计麦信平台故障时长
        Integer num;
        if ("8115537080661639168".equals(faultStatisticalQuantitySearchModule.getvProjectId())) {
            //平均救人时长（s）
            num = bizRuiJinDao.getTrappedPeopleTimeForMX(faultStatisticalQuantitySearchModule) / 1000;
        } else {    //其他项目——救援时长统计仪电反推困人时长
            num = bizRuiJinDao.getTrappedPeopleTime(faultStatisticalQuantitySearchModule);
        }

        num = (num != null) ? num : 0;
        int trappedPeopleGrade;
        if (num <= 600) {
            trappedPeopleGrade = 4;
        } else if (600 < num && num <= 900) {
            trappedPeopleGrade = 3;
        } else if (900 < num && num <= 1200) {
            trappedPeopleGrade = 2;
        } else if (1200 < num && num <= 1500) {
            trappedPeopleGrade = 1;
        } else {
            trappedPeopleGrade = 0;
        }
        return trappedPeopleGrade;
    }

    //维保逾期
    private int getOverdueOrders(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        List<Map> list = bizRuiJinDao.getOverdueOrders(faultStatisticalQuantitySearchModule);
        // 按电梯分组 每个电梯list存自己的维保工单记录
        Map<String, List<Map>> workOrders = Maps.newHashMap();
        if (null != list && list.size() > 0) {
            for (Map map : list) {
                String registerNumber = (String) map.get("register_number");
                List<Map> elevatorWorkOrder = workOrders.getOrDefault(registerNumber, Lists.newArrayList());
                elevatorWorkOrder.add(map);
                workOrders.put(registerNumber, elevatorWorkOrder);
            }
        }

        int overdueOrders = 0;

        // 统计每个电梯的维保记录中 是否存在超期
        for (String registerNumber : workOrders.keySet()) {
            List<Map> elevatorWorkOrder = workOrders.get(registerNumber);
            if (null == elevatorWorkOrder || elevatorWorkOrder.size() < 1) {
                continue;
            }
            for (int i = 1; i < elevatorWorkOrder.size(); i++) {
                Date nextMaintenanceDate = (Date) elevatorWorkOrder.get(i - 1).get("next_maintenance_date");
                Date completeTime = (Date) elevatorWorkOrder.get(i).get("complete_time");
                if (completeTime.after(nextMaintenanceDate)) {
                    // 超期维保记录
                    overdueOrders++;
                }
            }
            // 最后一条记录的下次维保时间与当前时间比较
            Map lastOrder = elevatorWorkOrder.get(elevatorWorkOrder.size() - 1);
            Date currentDate = DateUtils.localDateToDate(LocalDate.now());
            if (currentDate.after((Date) lastOrder.get("next_maintenance_date"))) {// 此记录的完成时间与上一条记录的下次维保日期比较
                overdueOrders++;
            }
        }
        return overdueOrders;
    }
}
