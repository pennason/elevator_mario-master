package com.shmashine.api.service.ruijin.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.hutool.core.bean.BeanUtil;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.BizRuiJinDao;
import com.shmashine.api.dao.BizThirdPartyRuijinEnventDao;
import com.shmashine.api.dao.WuyeFaultDao;
import com.shmashine.api.entity.ExportRunCountInfo;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.fault.output.QueryFaultExportModule;
import com.shmashine.api.module.fault.output.QueryMaintenanceExportModule;
import com.shmashine.api.module.ruijin.EventDownloadModuleMap;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.api.service.ruijin.BizThirdPartyRuijinEnventServiceI;
import com.shmashine.api.service.village.TblVillageServiceI;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.utils.DateUtils;
import com.shmashine.common.utils.EChartDateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BizThirdPartyRuijinEnventServiceImpl implements BizThirdPartyRuijinEnventServiceI {

    private final BizThirdPartyRuijinEnventDao bizThirdPartyRuijinEnventDao;
    private final BizRuiJinDao bizRuiJinDao;
    private final BizElevatorDao bizElevatorDao;
    private final TblVillageServiceI tblVillageServiceI;
    private final WuyeFaultDao wuyeFaultDao;

    @Override
    public List<Map> downLoadSearchMaintenanceList(SearchFaultModule searchFaultModule) {
        return bizRuiJinDao.searchMaintenanceList(searchFaultModule);
    }

    @Override
    public Map countElevator(String villageId, String userId, boolean isAdminFlag) {
        HashMap<Object, Object> map = new HashMap<>();
        List<Map> maps = bizRuiJinDao.countElevator(villageId, userId, isAdminFlag);
        maps.forEach(item -> {
            map.put(item.get("key"), item.get("number"));
        });
        return map;
    }

    @Override
    public Map countElevatorV1(String villageId, String userId, boolean isAdminFlag) {
        HashMap<Object, Object> map = new HashMap<>();
        List<Map> maps = bizRuiJinDao.countElevatorV1(villageId, userId, isAdminFlag);
        maps.forEach(item -> {
            map.put(item.get("key"), item.get("number"));
        });
        return map;
    }

    /**
     * 删除瑞金医院数据
     */
    @Override
    public void deleteThirdPartyRuijinEnventData(String eventNumber) {
        bizThirdPartyRuijinEnventDao.deleteThirdPartyRuijinEnventData(eventNumber);
        // TODO 这里先做增量
//        bizThirdPartyRuijinEnventDao.deleteThirdPartyRuijinEnventDataDetail(eventNumber);
    }

    /**
     * 电梯分类占比
     *
     * @return
     */
    @Override
    public List<Map> elevatorClassification(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        List<Map> maps = bizRuiJinDao.countElevatorClassificationInfo(
                faultStatisticalQuantitySearchModule.getVillageId(),
                faultStatisticalQuantitySearchModule.getvProjectId(),
                faultStatisticalQuantitySearchModule.getUserId(),
                faultStatisticalQuantitySearchModule.isAdminFlag);
        BigDecimal sum = new BigDecimal("0");
        for (int i = 0; i < maps.size(); i++) {
            Map map = maps.get(i);
            sum = (new BigDecimal((Long) map.get("number"))).add(sum);
        }
        for (int i = 0; i < maps.size(); i++) {
            Map map = maps.get(i);
            BigDecimal temp = (new BigDecimal((Long) map.get("number"))).divide(sum, 0, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
            map.put("percentageName", temp + "%");
            map.put("percentage", temp);
        }
        return maps;
    }


    /**
     * 电梯分类数量列表
     *
     * @return
     */
    @Override
    public List<Map> elevatorClassificationQuantityList(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.countElevatorClassificationInfo(
                faultStatisticalQuantitySearchModule.getVillageId(),
                faultStatisticalQuantitySearchModule.getvProjectId(),
                faultStatisticalQuantitySearchModule.getUserId(),
                faultStatisticalQuantitySearchModule.isAdminFlag);
    }

    /**
     * 电梯分类数量列表  通用
     *
     * @return
     */
    @Override
    public List<Map> elevatorClassificationQuantityListV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.countElevatorClassificationInfoV1(faultStatisticalQuantitySearchModule);
    }

    /**
     * 统计故障次数信息
     *
     * @return
     */
    @Override
    public List<Map> statisticsOfFailureTimes(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.faultStatisticalQuantity(faultStatisticalQuantitySearchModule);
    }

    /**
     * 统计故障次数信息 骋隆
     *
     * @return
     */
    @Override
    public List<Map> statisticsOfFailureTimesForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.faultStatisticalQuantityForCl(faultStatisticalQuantitySearchModule);
    }

    /**
     * 困人来源排行
     *
     * @return
     */
    @Override
    public List<Map> rankingOfPoorPeople(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.statisticsOnTheSourceOfPoverty(faultStatisticalQuantitySearchModule);
    }

    /**
     * 困人来源排行 骋隆
     *
     * @return
     */
    @Override
    public List<Map> rankingOfPoorPeopleForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.statisticsOnTheSourceOfPovertyForCl(faultStatisticalQuantitySearchModule);
    }

    /**
     * 电梯故障占比
     *
     * @return
     */
    @Override
    public List<Map> proportionOfElevatorFailures(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        //儿童医院项目只统计平台故障
        if ("8115537080661639168".equals(faultStatisticalQuantitySearchModule.getvProjectId())) {
            return bizRuiJinDao.statisticsFaultPovertyByMX(faultStatisticalQuantitySearchModule);
        } else {
            //其他项目统计仪电反推故障
            List<Map> maps = bizRuiJinDao.statisticsFaultPoverty(faultStatisticalQuantitySearchModule);

            if (maps.size() > 5) {

                long num = 0;
                for (int i = 5; i < maps.size(); i++) {
                    Map map = maps.get(i);
                    Long n = (Long) map.get("number");
                    num = num + n;
                }

                HashMap<Object, Object> otherFault = new HashMap<>();

                otherFault.put("number", num);
                otherFault.put("v_fault_name", "其他故障");
                List<Map> res = maps.subList(0, 5);
                res.add(otherFault);
                return res;
            }

            return maps;
        }

    }

    @Override
    public List<Map> proportionOfElevatorFailuresWithMXData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.statisticsFaultPovertyByMX(faultStatisticalQuantitySearchModule);
    }

    /**
     * 电梯故障排名
     *
     * @return
     */
    @Override
    public List<Map> elevatorFailureRanking(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.statisticsFaultPoverty(faultStatisticalQuantitySearchModule);
    }

    @Override
    public List<Map> elevatorFailureRankingWithMXData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.statisticsFaultPovertyByMX(faultStatisticalQuantitySearchModule);
    }

    /**
     * 不文明行为统计 不文明种类次数
     *
     * @return
     */
    @Override
    public List<Map> frequencyOfUncivilizedSpecies(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        List<Map> maps = bizRuiJinDao.uncivilizedBehavior(faultStatisticalQuantitySearchModule);
        if (maps != null && maps.size() != 0) {
            return maps;
        } else {
            return bizRuiJinDao.searchUncivilizedBehavior();
        }
    }

    /**
     * 不文明行为统计 不文明种类次数 通用
     *
     * @return
     */
    @Override
    public List<Map> frequencyOfUncivilizedSpeciesV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        List<Map> maps = bizRuiJinDao.uncivilizedBehaviorV1(faultStatisticalQuantitySearchModule);
        if (maps != null && maps.size() != 0) {
            return maps;
        } else {
            return bizRuiJinDao.searchUncivilizedBehavior();
        }
    }

    /**
     * 不文明行为统计 不文明种类次数 骋隆
     *
     * @return
     */
    @Override
    public List<Map> frequencyOfUncivilizedSpeciesForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        List<Map> maps = bizRuiJinDao.uncivilizedBehaviorForCl(faultStatisticalQuantitySearchModule);
        if (maps != null && maps.size() != 0) {
            return maps;
        } else {
            return bizRuiJinDao.searchUncivilizedBehavior();
        }
    }

    /**
     * 不文明行为前三电梯排行ruijin
     *
     * @return
     */
    @Override
    public List<Map> frequencyOfUncivilizedSpeciesTop3(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        /*运行次数和不文明次数*/
        return bizRuiJinDao.uncivilizedBehaviorByTopThree(faultStatisticalQuantitySearchModule, faultStatisticalQuantitySearchModule.getFaultType());
    }

    /**
     * 不文明行为前三电梯排行 通用
     *
     * @return
     */
    @Override
    public List<Map> frequencyOfUncivilizedSpeciesTop3V1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        String faultType = faultStatisticalQuantitySearchModule.getFaultType();
        if (faultType.equals("0")) {
            return bizRuiJinDao.uncivilizedBehaviorByTopThreeV1RunCount(faultStatisticalQuantitySearchModule);
        } else {
            return bizRuiJinDao.uncivilizedBehaviorByTopThreeV1(faultStatisticalQuantitySearchModule, faultStatisticalQuantitySearchModule.getFaultType());
        }
    }

    /**
     * 不文明行为走势图ruijin
     *
     * @return
     */
    @Override
    public Map<String, Object> trendOfUncivilizedBehavior(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        String timeFlag = faultStatisticalQuantitySearchModule.getTimeFlag();
        List<Map> resTemp;
        if (timeFlag.equals("22")) {
            resTemp = bizRuiJinDao.trendOfUncivilizedBehaviorInThisYear(faultStatisticalQuantitySearchModule);
            return EChartDateUtils.getDataOnYear(resTemp, null, null, null);
        } else if (timeFlag.equals("11")) {
            resTemp = bizRuiJinDao.trendOfUncivilizedBehaviorInThePreviousMonth(faultStatisticalQuantitySearchModule);
            return EChartDateUtils.getDataOnMonth(resTemp, null, null, null);
        } else {
            resTemp = bizRuiJinDao.trendOfUncivilizedBehaviorInThePreviousWeek(faultStatisticalQuantitySearchModule);
            return EChartDateUtils.getDataOnWeek(resTemp, null, null, null);
        }
    }

    /**
     * 不文明行为走势图 通用
     *
     * @return
     */
    @Override
    public Map<String, Object> trendOfUncivilizedBehaviorV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        String timeFlag = faultStatisticalQuantitySearchModule.getTimeFlag();
        List<Map> resTemp;
        if (timeFlag.equals("22")) {
            resTemp = bizRuiJinDao.trendOfUncivilizedBehaviorInThisYearV1(faultStatisticalQuantitySearchModule);
            return EChartDateUtils.getDataOnYear(resTemp, null, null, null);
        } else if (timeFlag.equals("11")) {
            resTemp = bizRuiJinDao.trendOfUncivilizedBehaviorInThePreviousMonthV1(faultStatisticalQuantitySearchModule);
            return EChartDateUtils.getDataOnMonth(resTemp, null, null, null);
        } else {
            resTemp = bizRuiJinDao.trendOfUncivilizedBehaviorInThePreviousWeekV1(faultStatisticalQuantitySearchModule);
            return EChartDateUtils.getDataOnWeek(resTemp, null, null, null);
        }
    }
//    /**
//     * 不文明行为走势图
//     *
//     * @return
//     */
//    @Override
//    public HashMap<Object, Object> trendOfUncivilizedBehavior(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
//        String timeFlag = faultStatisticalQuantitySearchModule.getTimeFlag();
//        ArrayList<Object> nameLS = new ArrayList<>();
//        ArrayList<String> times;
//
//        ArrayList<LinkedHashMap> resTemp;
//        if (timeFlag.equals("22")) {
//            resTemp = bizRuiJinDao.trendOfUncivilizedBehaviorInThisYear(faultStatisticalQuantitySearchModule);
//            times = DateUtils.getPastMonthList(12);
//        } else if (timeFlag.equals("11")) {
//            resTemp = bizRuiJinDao.trendOfUncivilizedBehaviorInThePreviousMonth(faultStatisticalQuantitySearchModule);
//            times = DateUtils.getPastDateList(30);
//        } else {
//            resTemp = bizRuiJinDao.trendOfUncivilizedBehaviorInThePreviousWeek(faultStatisticalQuantitySearchModule);
//            times = DateUtils.getPastDateList(7);
//        }
//
//        for (int i = 0; i < resTemp.size(); i++) {
//            Map map = resTemp.get(i);
//            Object fault_name = map.get("fault_name");
//            HashMap names = new HashMap<>();
//            names.put("title", fault_name);
//            ArrayList<Object> values = new ArrayList<>();
//            for (Object key : map.keySet()) {
//                if (!key.equals("fault_name")) {
//                    values.add(map.get(key));
//                }
//            }
//            Collections.reverse(values);
//            names.put("values", values);
//            nameLS.add(names);
//        }
//        HashMap<Object, Object> result = new HashMap<>();
//        result.put("times", times);
//        result.put("datas", nameLS);
//        return result;
//    }

    /**
     * 不文明行为走势图 骋隆
     *
     * @return
     */
    @Override
    public Map<String, Object> trendOfUncivilizedBehaviorForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        String timeFlag = faultStatisticalQuantitySearchModule.getTimeFlag();
        List<Map> resTemp;
        if (timeFlag.equals("22")) {
            resTemp = bizRuiJinDao.trendOfUncivilizedBehaviorInThisYearForCl(faultStatisticalQuantitySearchModule);
            return EChartDateUtils.getDataOnYear(resTemp, null, null, null);
        } else if (timeFlag.equals("11")) {
            resTemp = bizRuiJinDao.trendOfUncivilizedBehaviorInThePreviousMonthForCl(faultStatisticalQuantitySearchModule);
            return EChartDateUtils.getDataOnMonth(resTemp, null, null, null);
        } else {
            resTemp = bizRuiJinDao.trendOfUncivilizedBehaviorInThePreviousWeekForCl(faultStatisticalQuantitySearchModule);
            return EChartDateUtils.getDataOnWeek(resTemp, null, null, null);
        }
    }

    /**
     * 智能监管
     *
     * @return
     */
    @Override
    public List<Map> intelligentSupervision(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.intelligentSupervision(faultStatisticalQuantitySearchModule);
    }

    /**
     * 智能监管
     *
     * @return
     */
    @Override
    public List<Map> intelligentSupervisionV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.intelligentSupervisionV1(faultStatisticalQuantitySearchModule);
    }


    /**
     * 智能监管 骋隆
     *
     * @return
     */
    @Override
    public List<Map> intelligentSupervisionForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.intelligentSupervisionForCl(faultStatisticalQuantitySearchModule);
    }

    /**
     * 获取电梯基本信息
     *
     * @return
     */
    @Override
    public Map getElevatorInfo(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        Map elevatorInfo = bizRuiJinDao.getElevatorInfo(faultStatisticalQuantitySearchModule.getRegister_number());

        return getelevatorInfo(elevatorInfo);
    }

    @Override
    public Map getElevatorInfoWithMXData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        Map elevatorInfo = bizRuiJinDao.getElevatorInfoWithMXData(faultStatisticalQuantitySearchModule.getRegister_number());
        return getelevatorInfo(elevatorInfo);

    }

    /**
     * 电梯历史信息
     * <p>
     * 困人记录 故障记录 不文明行为
     *
     * @return
     */
    @Override
    public LinkedHashMap getElevatorHlsInfo(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        String register_number = faultStatisticalQuantitySearchModule.getRegister_number();
        List<Map> elevatorHlsInfoSleepy = bizRuiJinDao.getElevatorHlsInfoSleepy(register_number);
        List<Map> elevatorHlsInfoFault = bizRuiJinDao.getElevatorHlsInfoFault(register_number);
//        List<Map> elevatorHlsInfoUncivilizedBehavior = bizRuiJinDao.getElevatorHlsInfoUncivilizedBehavior(register_number);
        LinkedHashMap result = new LinkedHashMap<>();
        result.put("elevatorHlsInfoSleepy", elevatorHlsInfoSleepy);
        result.put("elevatorHlsInfoFault", elevatorHlsInfoFault);
//        result.put("elevatorHlsInfoUncivilizedBehavior", elevatorHlsInfoUncivilizedBehavior);
        return result;
    }

    @Override
    public Map getElevatorHlsInfoWithMXData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        String register_number = faultStatisticalQuantitySearchModule.getRegister_number();

        //困人
        List<Map> elevatorHlsInfoSleepy = bizRuiJinDao.getElevatorHlsInfoTrappedWithMXData(register_number);
        //故障
        List<Map> elevatorHlsInfoFault = bizRuiJinDao.getElevatorHlsInfoFaultWithMXData(register_number);
        //不文明行为
//        List<Map> elevatorHlsInfoUncivilizedBehavior = bizRuiJinDao.getElevatorHlsInfoUncivilizedBehavior(register_number);

        LinkedHashMap result = new LinkedHashMap<>();
        result.put("elevatorHlsInfoSleepy", elevatorHlsInfoSleepy);
        result.put("elevatorHlsInfoFault", elevatorHlsInfoFault);
//        result.put("elevatorHlsInfoUncivilizedBehavior", elevatorHlsInfoUncivilizedBehavior);
        return result;
    }

    /**
     * 电梯历史信息 骋隆
     * <p>
     * 困人记录 故障记录 不文明行为
     *
     * @return
     */
    @Override
    public LinkedHashMap getElevatorHlsInfoForCl(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        String register_number = faultStatisticalQuantitySearchModule.getRegister_number();
        List<Map> elevatorHlsInfoSleepy = bizRuiJinDao.getElevatorHlsInfoSleepy(register_number);
        List<Map> elevatorHlsInfoFault = bizRuiJinDao.getElevatorHlsInfoFault(register_number);
        List<Map> elevatorHlsInfoUncivilizedBehavior = bizRuiJinDao.getElevatorHlsInfoUncivilizedBehaviorForCl(register_number);
        LinkedHashMap result = new LinkedHashMap<>();
        result.put("elevatorHlsInfoSleepy", elevatorHlsInfoSleepy);
        result.put("elevatorHlsInfoFault", elevatorHlsInfoFault);
        result.put("elevatorHlsInfoUncivilizedBehavior", elevatorHlsInfoUncivilizedBehavior);
        return result;
    }

    /**
     * 困人实时信息
     */
    @Override
    public List<Map> getEnventDetailById(String eventId) {
        return bizRuiJinDao.getEnventDetailById(eventId);
    }

    @Override
    public List<Map> numberOfMaintenanceWorkOrders(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.numberOfMaintenanceWorkOrders(faultStatisticalQuantitySearchModule);
    }

    @Override
    public List<Map> statisticsWorkOrderStatusProportion(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        List<Map> workOrderCount = bizRuiJinDao.queryMaintenanceWorkOrders(faultStatisticalQuantitySearchModule);

        Map<Integer, Integer> collect = workOrderCount.stream()
                .collect(Collectors.toMap(it -> ((Number) it.get("overdue")).intValue(), it -> ((Number) it.get("num")).intValue()));


        // 超期维保数量
        int overdueNum = collect.get(1) == null ? 0 : collect.get(1);
        // 正常维保数量
        int normalNum = collect.get(0) == null ? 0 : collect.get(0);
        // 总维保数量
        int total = overdueNum + normalNum;

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);

        // 统计
        List<Map> list = Lists.newArrayList();

        // 正常维保
        Map<String, Object> normal = Maps.newHashMap();
        normal.put("key", "normal_maintenance");
        normal.put("proportion", total == 0 ? 0 : numberFormat.format((double) normalNum / total * 100));
        normal.put("number", normalNum);
        list.add(normal);

        // 超期维保
        Map<String, Object> overdue = Maps.newHashMap();
        overdue.put("key", "be_overdue_maintenance");
        overdue.put("proportion", total == 0 ? 0 : numberFormat.format((double) overdueNum / total * 100));
        overdue.put("number", overdueNum);
        list.add(overdue);

        // 缺失维保
        Map<String, Object> defect = Maps.newHashMap();
        defect.put("key", "defect_maintenance");
        defect.put("proportion", 0);
        defect.put("number", 0);
        list.add(defect);
        return list;
    }

    @Override
    public List<Map> statisticsOfTrappedPeopleRescue(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        List<Map> map;
        //历史困人救援统计——儿童医院项目，统计麦信平台故障
        if ("8115537080661639168".equals(faultStatisticalQuantitySearchModule.getvProjectId())) {
            map = bizRuiJinDao.statisticsOfTrappedPeopleRescueForMX(faultStatisticalQuantitySearchModule);
        } else {    //其他项目统计仪电反推故障
            map = bizRuiJinDao.statisticsOfTrappedPeopleRescue(faultStatisticalQuantitySearchModule);
        }
        for (Map temp : map) {
            if ("救援解困平均时间".equals(temp.get("key")) || "救援到场平均时间".equals(temp.get("key"))) {
                BigDecimal number = (BigDecimal) (temp.get("number"));
                int num = (number != null) ? number.intValue() : 0;
                int h = num / 3600;
                int m = (num % 3600) / 60;
                int s = num % 60;
                String time;
                if (h > 0) {
                    time = String.format("%s小时%s分%s秒", h, m, s);
                } else {
                    time = String.format("%s分%s秒", m, s);
                }
                temp.put("number", time);
            }
        }
        return map;
    }

    @Override
    public List<Map> theHistoryOfSingleLabor(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        //儿童医院项目——历史困人工单统计平台故障
        if ("8115537080661639168".equals(faultStatisticalQuantitySearchModule.getvProjectId())) {
            return bizRuiJinDao.theHistoryOfSingleLaborForMX(faultStatisticalQuantitySearchModule);
        } else {     //其他项目——历史困人工单统计仪电反推
            return bizRuiJinDao.theHistoryOfSingleLabor(faultStatisticalQuantitySearchModule);
        }
    }

    /**
     * 电梯故障工单走势图
     */
    @Override
    public Map<String, Object> failureWorkOrderTrendChart(FaultStatisticalQuantitySearchModule
                                                                  faultStatisticalQuantitySearchModule) {

        String timeFlag = faultStatisticalQuantitySearchModule.getTimeFlag();
        List<Map> resTemp;

        //儿童医院项目统计平台数据
        if ("8115537080661639168".equals(faultStatisticalQuantitySearchModule.getvProjectId())) {
            resTemp = bizRuiJinDao.failureWorkOrderTrendChartByMX(faultStatisticalQuantitySearchModule);
        } else {    //其他项目统计仪电反推数据
            resTemp = bizRuiJinDao.failureWorkOrderTrendChart(faultStatisticalQuantitySearchModule);
        }
        if (timeFlag.equals("22")) {
            return EChartDateUtils.getDataOnYear(resTemp, null, null, null);
        } else if (timeFlag.equals("11")) {
            return EChartDateUtils.getDataOnMonth(resTemp, null, null, null);
        } else {
            return EChartDateUtils.getDataOnWeek(resTemp, null, null, null);
        }
    }

    @Override
    public Map trendYearOfHistoricalMaintenance(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        String timeFlag = faultStatisticalQuantitySearchModule.getTimeFlag();

        ArrayList<String> times;
        List<Map> resTemp;

        if (timeFlag.equals("22")) {
            resTemp = bizRuiJinDao.trendYearOfHistoricalMaintenanceData(faultStatisticalQuantitySearchModule);
            times = DateUtils.getPastMonthList(12);
        } else if (timeFlag.equals("11")) {
            resTemp = bizRuiJinDao.historicalMaintenanceDataTrendMonth(faultStatisticalQuantitySearchModule);
            times = DateUtils.getPastDateList(30);
        } else {
            resTemp = bizRuiJinDao.trendWeekOfHistoricalMaintenanceData(faultStatisticalQuantitySearchModule);
            times = DateUtils.getPastDateList(7);
        }

        // 根据时间范围对数据进行补零操作
        Map<String, int[]> data = dataZeroPadding(times, resTemp);

        ArrayList<Object> dataLs = new ArrayList<>();
        for (String key : data.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", key);
            map.put("values", data.get(key));
            dataLs.add(map);
        }

        LinkedHashMap<Object, Object> result = new LinkedHashMap<>();
        result.put("times", times);
        result.put("datas", dataLs);
        return result;
    }


    /**
     * 数据补零操作
     *
     * @param times 日期数组
     * @param input 需要补零的数据
     * @return
     */
    public static Map<String, int[]> dataZeroPadding(ArrayList<String> times, List<Map> input) {

        Map<String, int[]> result = new HashMap<>();
        input.forEach(value -> {
            String orderTypeName = String.valueOf(value.get("order_type_name"));
            result.putIfAbsent(orderTypeName, new int[times.size()]);
            int index = times.indexOf(value.get("date").toString());
            if (index != -1) {
                result.get(orderTypeName)[index] = Integer.parseInt(String.valueOf(value.get("number")));
            }
        });
        return result;
    }

    /**
     * 电梯列表不分页
     *
     * @param searchElevatorModule
     * @return
     */
    @Override
    public List<Map<String, Object>> searchElevatorListNoPage(SearchElevatorModule searchElevatorModule) {
        return bizRuiJinDao.searchElevatorList(searchElevatorModule);
    }

    /**
     * 获取故障列表
     *
     * @param searchFaultModule 查询条件
     * @return 分页信息
     */
    @Override
    public PageListResultEntity searchFaultsListWithPage(SearchFaultModule searchFaultModule) {
        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> mapPageInfo = new PageInfo<>(bizRuiJinDao.searchFaultList(searchFaultModule), pageSize);

        // 扩展小区信息
        tblVillageServiceI.extendVillageInfo(mapPageInfo.getList());

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(), mapPageInfo.getList());
    }

    @Override
    public PageListResultEntity searchFaultsListWithMXDataByPage(SearchFaultModule searchFaultModule) {
        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> mapPageInfo = new PageInfo<>(bizRuiJinDao.searchFaultListWithMXData(searchFaultModule), pageSize);

        // 扩展小区信息
        tblVillageServiceI.extendVillageInfo(mapPageInfo.getList());

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(), mapPageInfo.getList());
    }

    /**
     * 维修工单下载
     *
     * @param searchFaultModule
     * @param response
     */
    @Override
    public void searchFaultListDownload(SearchFaultModule searchFaultModule, HttpServletResponse response) {
        // 查询故障统计列表
        List<QueryFaultExportModule> data = bizRuiJinDao.searchFaultListDownload(searchFaultModule);

        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String name = "维修工单";
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), QueryFaultExportModule.class).sheet(name).doWrite(data);

        } catch (Exception e) {

            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            map.put("code", "500");
            map.put("data", JSONObject.toJSONString(data));
            try {
                response.getWriter().println(JSONObject.toJSONString(map));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 维保记录下载
     *
     * @param searchFaultModule
     * @param response
     */
    @Override
    public void searchMaintenanceListDownload(SearchFaultModule searchFaultModule, HttpServletResponse response) {
        // 查询故障统计列表
        List<QueryMaintenanceExportModule> data = bizRuiJinDao.queryMaintenanceWorkOrdersListDownload(searchFaultModule);

        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String name = "维保工单";
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), QueryMaintenanceExportModule.class).sheet(name).doWrite(data);

        } catch (Exception e) {

            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            map.put("code", "500");
            map.put("data", JSONObject.toJSONString(data));
            try {
                response.getWriter().println(JSONObject.toJSONString(map));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public PageListResultEntity queryRuiJinAnnualCheckList(SearchFaultModule searchFaultModule) {

        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> mapPageInfo = new PageInfo<>(bizRuiJinDao.queryRuiJinAnnualCheckList(searchFaultModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(), mapPageInfo.getList());
    }

    /**
     * 获取维保记录列表
     *
     * @param searchFaultModule
     * @return
     */
    @Override
    public PageListResultEntity searchMaintenanceList(SearchFaultModule searchFaultModule) {

        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> mapPageInfo = new PageInfo<>(bizRuiJinDao.queryMaintenanceWorkOrdersList(searchFaultModule), pageSize);

        // 扩展小区信息
        tblVillageServiceI.extendVillageInfo(mapPageInfo.getList());

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(), mapPageInfo.getList());

    }

    /**
     * 最新的上报困人(仪电推送故障， 困人中的记录)
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    @Override
    public Map getPersonShuttingLately(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.getPersonShuttingLately(faultStatisticalQuantitySearchModule);
    }

    /**
     * 获取故障列表
     *
     * @param searchFaultModule
     * @return
     */
    @Override
    public List<EventDownloadModuleMap> searchEventForDownload(SearchFaultModule searchFaultModule) {

        //1：渠道上报 2：新建工单 3：已接单、4：已签到 5：已完成 6：已确认 7： 误报 8：事故
        String[] statusNames = new String[]{"", "渠道上报", "新建工单", "已接单", "已签到", "已完成", "已确认", "误报", "事故"};
        List<EventDownloadModuleMap> list = bizRuiJinDao.searchEventsForDownload(searchFaultModule);
        if (list != null && list.size() > 0) {
            for (EventDownloadModuleMap eventDownloadModuleMap : list) {
                if (StringUtils.hasText(eventDownloadModuleMap.getStatus())) {
                    Integer status = Integer.valueOf(eventDownloadModuleMap.getStatus());
                    if (status > 0 && status < 9) {
                        eventDownloadModuleMap.setStatus(statusNames[status]);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<String> getTodayTrappedPeopleEleName(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.getTodayTrappedPeopleEleName(faultStatisticalQuantitySearchModule);
    }

    /**
     * 瑞金运行统计数据
     *
     * @param searchFaultModule
     * @return
     */
    @Override
    public PageListResultEntity getRunCountInfo(SearchFaultModule searchFaultModule) {

        List<Map> list = getRunCountList(searchFaultModule);

        ////////////////////////数据分页////////////////////////////

        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        int s = pageSize * (pageIndex - 1);
        int e = pageSize * pageIndex;
        List<Map> maps;
        if (list.size() < pageSize) {
            maps = list;
        } else if (list.size() < e) {
            maps = list.subList(s, list.size());
        } else {
            maps = list.subList(s, e);
        }
        PageInfo<Map> mapPageInfo = new PageInfo<>(maps, pageSize);
        // 封装分页数据结构
        PageListResultEntity<Map> mapPageListResultEntity = new PageListResultEntity<>(pageIndex, pageSize, list.size(), mapPageInfo.getList());
        return mapPageListResultEntity;

    }

    @Override
    public String exportRunCountInfo(SearchFaultModule searchFaultModule, HttpServletResponse response) {

        List<Map> list = getRunCountList(searchFaultModule);

        ExcelWriter excelWriter = null;

        try {

            String fileName = URLEncoder.encode("运行统计数据.xlsx", "utf-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            //此处指定了文件类型为xls，如果是xlsx的，请自行替换修改
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");

            List<ExportRunCountInfo> exportRunCountInfos = listMapToListBean(list, ExportRunCountInfo.class);

            excelWriter = EasyExcel.write(response.getOutputStream(), ExportRunCountInfo.class).build();

            WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();
            excelWriter.write(exportRunCountInfos, writeSheet);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

        return null;
    }

    @Override
    public List<Map> getElevatorsStatus(SearchFaultModule searchFaultModule) {
        return bizRuiJinDao.getElevatorsStatus(searchFaultModule);
    }

    @Override
    public List<Map> queryBuilding(SearchFaultModule searchFaultModule) {
        return bizRuiJinDao.queryBuilding(searchFaultModule);
    }

    @Override
    public PageListResultEntity queryAnnualInspectionList(SearchFaultModule searchFaultModule) {

        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> mapPageInfo = new PageInfo<>(bizRuiJinDao.queryAnnualInspectionList(searchFaultModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(), mapPageInfo.getList());
    }

    @Override
    public List<Map> statisticsOfFailureTimesForClWithMXData(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizRuiJinDao.statisticsOfFailureTimesForClWithMXData(faultStatisticalQuantitySearchModule);
    }

    private List<ExportRunCountInfo> listMapToListBean(List<Map> list, Class<ExportRunCountInfo> exportRunCountInfoClass) {

        List<ExportRunCountInfo> exportRunCountInfoList = new ArrayList<>();

        list.stream().forEach(it -> {
            ExportRunCountInfo exportRunCountInfo = BeanUtil.toBean(it, exportRunCountInfoClass);
            exportRunCountInfoList.add(exportRunCountInfo);
        });

        return exportRunCountInfoList;
    }

    private List<Map> getRunCountList(SearchFaultModule searchFaultModule) {

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Map> list;
        try {
            List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5, 6);
            HashMap<Integer, List<Map>> resMap = new HashMap<>();

            CompletableFuture[] completableFutures = ids.stream().map(id -> CompletableFuture.supplyAsync(() -> {

                List<Map> resList = getRunCountInfos(id, searchFaultModule);

                return resList;

            }, executorService).whenComplete((resList, e) -> {
                resMap.put(id, resList);
            })).toArray(CompletableFuture[]::new);

            try {
                CompletableFuture.allOf(completableFutures).get(20, TimeUnit.MINUTES);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            ////////////////////////整合数据////////////////////////

            list = resMap.get(1);

            CompletableFuture[] completableFutures1 = list.stream().map(l -> CompletableFuture.supplyAsync(() -> {

                resMap.get(2).stream().forEach(r2 -> {
                    if (l.get("v_elevator_id").equals(r2.get("v_elevator_id"))) {
                        l.putAll(r2);
                    }
                });
                resMap.get(3).stream().forEach(r3 -> {
                    if (l.get("v_elevator_id").equals(r3.get("v_elevator_id"))) {
                        l.putAll(r3);
                    }
                });
                resMap.get(4).stream().forEach(r4 -> {
                    if (l.get("v_elevator_id").equals(r4.get("v_elevator_id"))) {
                        l.putAll(r4);
                    }
                });
                resMap.get(5).stream().forEach(r5 -> {
                    if (l.get("v_elevator_id").equals(r5.get("v_elevator_id"))) {
                        l.putAll(r5);
                    }
                });

                resMap.get(6).stream().forEach(r6 -> {
                    if (l.get("v_elevator_id").equals(r6.get("v_elevator_id"))) {
                        l.putAll(r6);
                    }
                });

                return l;

            }, executorService).whenComplete((resList, e) -> {

            })).toArray(CompletableFuture[]::new);

            try {
                CompletableFuture.allOf(completableFutures1).get(20, TimeUnit.MINUTES);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } finally {
            executorService.shutdown();
        }
        return list;
    }

    private List<Map> getRunCountInfos(Integer id, SearchFaultModule searchFaultModule) {
        List<String> elevator = wuyeFaultDao.getElevator(searchFaultModule);
        searchFaultModule.setElevatorCode(elevator);
        //电梯基础数据
        if (id == 1) {
            return bizRuiJinDao.getRunCountInfo1(searchFaultModule);
        }

        //基础运行数据统计
        if (id == 2) {
            return bizRuiJinDao.getRunCountInfo2(searchFaultModule);
        }

        //困人故障统计急修次数统计
        if (id == 3) {
            if (searchFaultModule.getWuyePlatform() == 2) {
                return bizRuiJinDao.getRunCountInfo3Mx(searchFaultModule);
            } else {
                return bizRuiJinDao.getRunCountInfo3(searchFaultModule);
            }

        }

        //维保次数统计
        if (id == 4) {
            return bizRuiJinDao.getRunCountInfo4(searchFaultModule);
        }

        //关门受阻挡次数统计
        if (id == 5) {
            return bizRuiJinDao.getRunCountInfo5(searchFaultModule);
        }

        //电动车乘梯次数统计
        if (id == 6) {
            return bizRuiJinDao.getRunCountInfo6(searchFaultModule);
        }

        return null;
    }


    private Map getelevatorInfo(Map elevatorInfo) {
        if (elevatorInfo == null) {
            return null;
        }

        LocalDateTime dt_install_time = (LocalDateTime) elevatorInfo.get("dt_install_time");

        if (dt_install_time != null) {
            // 获取日期
            String now = DateUtils.dateFormate(new Date(), "yyyy-MM-dd HH:mm:ss");
            Date date1 = DateUtils.parseDate(dt_install_time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "yyyy-MM-dd HH:mm:ss");
            Date date2 = DateUtils.parseDate(now, "yyyy-MM-dd HH:mm:ss");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            long timeInMillis1 = calendar.getTimeInMillis();
            calendar.setTime(date2);
            long timeInMillis2 = calendar.getTimeInMillis();

            long betweenDays = (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);
            elevatorInfo.put("cumulative_running_time", betweenDays * 24);
            elevatorInfo.put("cumulative_days", betweenDays);
        } else {
            elevatorInfo.put("cumulative_running_time", "--");
            elevatorInfo.put("cumulative_days", "--");
        }
        return elevatorInfo;
    }

}
