package com.shmashine.socket.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.shmashine.socket.dal.dao.DockingFloorDao;
import com.shmashine.socket.dal.dto.PeopleFlowStatisticsDO;
import com.shmashine.socket.mongo.utils.MongoTemplateUtil;

/**
 * 楼层停靠算法
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/12/11 15:26
 * @Since: 1.0.0
 */
@Component
public class FloorDockingAlgorithm {

    @Resource
    private GetHolidaysUtil getHolidaysUtil;

    @Resource
    private DockingFloorDao dockingFloorDao;

    @Resource(name = "mongoTemplateUtil")
    private MongoTemplateUtil mongoTemplate;

    /**
     * 获取预停靠楼层
     *
     * @return 楼层
     */
    public Integer getDockingFloor(String elevatorCode) {

        Integer dockingFloor;
        DateTime now = DateTime.now();
        DateTime startDate = DateUtil.offsetDay(now, -30);
        DateTime startTime = DateUtil.offsetMinute(now, -10);
        DateTime endTime = DateUtil.offsetMinute(now, 20);

        //获取最近一个月节假日数据
        List<Date> holidays = getHolidaysUtil.getHolidays(startDate, now);

        //获取楼层停靠人流量统计
        List<PeopleFlowStatisticsDO> peopleFlowStatistics =
                dockingFloorDao.peopleFlowStatistics(elevatorCode, startDate,
                        DateUtil.format(startTime, DatePattern.NORM_TIME_PATTERN),
                        DateUtil.format(endTime, DatePattern.NORM_TIME_PATTERN));

        Map<Integer, Integer> floorMap;
        //是否有人流量统计数据
        if (peopleFlowStatistics == null || peopleFlowStatistics.size() == 0) {
            //获取楼层停靠统计
            List<MongoTemplateUtil.AggregationResult> floorStopCountInfo =
                    mongoTemplate.getFloorStopCountInfo(elevatorCode,
                            DateUtil.format(DateUtil.offsetHour(startTime, -8), DatePattern.NORM_TIME_PATTERN),
                            DateUtil.format(DateUtil.offsetHour(endTime, -8), DatePattern.NORM_TIME_PATTERN));

            if (floorStopCountInfo == null || floorStopCountInfo.size() == 0) {
                return null;
            }

            List<String> holidayStr = holidays.stream().map(DateUtil::formatDate).collect(Collectors.toList());
            if (holidays.contains(DateUtil.beginOfDay(now))) {
                floorMap = floorStopCountInfo.stream()
                        .filter(it -> holidayStr.contains(it.getStatisticalDate()))
                        .collect(Collectors.groupingBy(
                                MongoTemplateUtil.AggregationResult::getFloor,
                                Collectors.summingInt(MongoTemplateUtil.AggregationResult::getFloorCount)
                        ));
            } else {
                floorMap = floorStopCountInfo.stream()
                        .filter(it -> !holidayStr.contains(it.getStatisticalDate()))
                        .collect(Collectors.groupingBy(
                                MongoTemplateUtil.AggregationResult::getFloor,
                                Collectors.summingInt(MongoTemplateUtil.AggregationResult::getFloorCount)
                        ));
            }

        } else {

            //是否为节假日
            if (holidays.contains(DateUtil.beginOfDay(now))) {
                floorMap = peopleFlowStatistics.stream()
                        .filter(it -> holidays.contains(it.getTriggerDate()))
                        .collect(Collectors.groupingBy(
                                PeopleFlowStatisticsDO::getFloor,
                                Collectors.summingInt(PeopleFlowStatisticsDO::getThroughput)
                        ));
            } else {
                floorMap = peopleFlowStatistics.stream()
                        .filter(it -> !holidays.contains(it.getTriggerDate()))
                        .collect(Collectors.groupingBy(
                                PeopleFlowStatisticsDO::getFloor,
                                Collectors.summingInt(PeopleFlowStatisticsDO::getThroughput)
                        ));
            }
        }

        // 找到总吞吐量最大的楼层
        dockingFloor = floorMap.entrySet().stream()
                .max(Map.Entry.comparingByValue()).get().getKey();
        return dockingFloor;
    }

}
