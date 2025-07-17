package com.shmashine.api.service.peopleFlowStatistics.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.shmashine.api.controller.peopleFlowStatistics.vo.PedestrianFlowStatisticsByVillageReqVO;
import com.shmashine.api.controller.peopleFlowStatistics.vo.PedestrianFlowStatisticsReqVO;
import com.shmashine.api.controller.peopleFlowStatistics.vo.PedestrianFlowStatisticsRespVO;
import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.PeopleFlowStatisticsDao;
import com.shmashine.api.service.peopleFlowStatistics.PeopleFlowStatisticsService;
import com.shmashine.api.service.peopleFlowStatistics.dto.PedestrianFlowStatisticsDTO;
import com.shmashine.common.entity.TblElevator;

/**
 * @author  jiangheng
 * @version 2024/1/16 11:30
 * @description: com.shmashine.api.service.peopleFlowStatistics.impl
 */
@Service
public class PeopleFlowStatisticsServiceImpl implements PeopleFlowStatisticsService {

    private final PeopleFlowStatisticsDao peopleFlowStatisticsDao;

    private final BizElevatorDao elevatorDao;


    @Autowired
    public PeopleFlowStatisticsServiceImpl(PeopleFlowStatisticsDao peopleFlowStatisticsDao, BizElevatorDao elevatorDao) {
        this.peopleFlowStatisticsDao = peopleFlowStatisticsDao;
        this.elevatorDao = elevatorDao;
    }

    @Override
    public PedestrianFlowStatisticsRespVO pedestrianFlowStatistics(PedestrianFlowStatisticsReqVO reqVO) {

        List<PedestrianFlowStatisticsDTO> result = peopleFlowStatisticsDao.pedestrianFlowStatistics(reqVO);

        //获取两个日期之间的时间列表
        List<String> xAxisDataList = getDateTimesBetweenTwoDate(reqVO.getSelectStartTime(), reqVO.getSelectEndTime());

        return getPedestrianFlowStatistics(result, xAxisDataList);
    }

    @Override
    public PedestrianFlowStatisticsRespVO pedestrianFlowStatisticsGroupByFloor(PedestrianFlowStatisticsReqVO reqVO) {

        List<PedestrianFlowStatisticsDTO> result = peopleFlowStatisticsDao.pedestrianFlowStatisticsGroupByFloor(reqVO, 1);
        result.addAll(peopleFlowStatisticsDao.pedestrianFlowStatisticsGroupByFloor(reqVO, -1));

        //获取该梯的楼层列表
        TblElevator elevator = elevatorDao.getElevatorByElevatorIdOrCode(reqVO.getElevatorCode());
        List<String> xAxisDataList = getElevatorFloorList(elevator);

        return getPedestrianFlowStatistics(result, xAxisDataList);

    }

    @Override
    public PedestrianFlowStatisticsRespVO pedestrianFlowStatisticsByVillage(PedestrianFlowStatisticsByVillageReqVO reqVO) {

        List<PedestrianFlowStatisticsDTO> result = peopleFlowStatisticsDao.pedestrianFlowStatisticsByVillage(reqVO);

        //获取两个日期之间的时间列表
        List<String> xAxisDataList = getDateTimesBetweenTwoDate(reqVO.getSelectStartTime(), reqVO.getSelectEndTime());

        return getPedestrianFlowStatistics(result, xAxisDataList);
    }

    /**
     * 人流量折线图数据封装
     *
     * @param result
     * @param xAxisDataList
     * @return
     */
    private static PedestrianFlowStatisticsRespVO getPedestrianFlowStatistics(List<PedestrianFlowStatisticsDTO> result, List<String> xAxisDataList) {

        PedestrianFlowStatisticsRespVO.XAxis xAxis = PedestrianFlowStatisticsRespVO.XAxis.builder().type("category").build();
        PedestrianFlowStatisticsRespVO.Series series = PedestrianFlowStatisticsRespVO.Series.builder().build();
        PedestrianFlowStatisticsRespVO.Rest up = PedestrianFlowStatisticsRespVO.Rest.builder().type("up").build();
        PedestrianFlowStatisticsRespVO.Rest down = PedestrianFlowStatisticsRespVO.Rest.builder().type("down").build();
        series.setRestList(Arrays.asList(up, down));
        ArrayList<Integer> upData = new ArrayList<>();
        ArrayList<Integer> downData = new ArrayList<>();

        //数据格式化
        HashMap<String, List<PedestrianFlowStatisticsDTO>> mapResult = new HashMap<>(result.size());
        result.forEach(it -> {

            List<PedestrianFlowStatisticsDTO> pedestrianFlowStatisticsDTOS = mapResult.get(it.getGroupData());
            if (pedestrianFlowStatisticsDTOS == null) {
                pedestrianFlowStatisticsDTOS = new ArrayList<>();
                pedestrianFlowStatisticsDTOS.add(it);
                mapResult.put(it.getGroupData(), pedestrianFlowStatisticsDTOS);
            }
            pedestrianFlowStatisticsDTOS.add(it);

        });

        //数据填充
        xAxisDataList.forEach(it -> {

            List<PedestrianFlowStatisticsDTO> statistics = mapResult.get(it);
            Integer upNum = 0;
            Integer downNum = 0;

            if (statistics != null) {
                for (PedestrianFlowStatisticsDTO s : statistics) {

                    //上行|进入
                    if (s.getGroupBy() == 1) {
                        upNum = s.getThroughput();
                    }
                    //下行|流出
                    if (s.getGroupBy() == -1) {
                        downNum = s.getThroughput();
                    }
                }
            }

            //填充数据
            upData.add(upNum);
            downData.add(downNum);

        });

        xAxis.setData(xAxisDataList);
        up.setData(upData);
        down.setData(downData);

        return PedestrianFlowStatisticsRespVO.builder().xAxis(xAxis).series(series).build();
    }

    /**
     * 获取电梯楼层列表
     *
     * @param elevator
     * @return
     */
    private static List<String> getElevatorFloorList(TblElevator elevator) {

        String vFloorDetail = elevator.getVFloorDetail();
        if (StringUtils.hasText(vFloorDetail)) {
            return Arrays.stream(vFloorDetail.split(",")).collect(Collectors.toList());
        } else {
            ArrayList<String> xAxisDataList = new ArrayList<>();
            Integer iMaxFloor = elevator.getIMaxFloor();
            Integer iMinFloor = elevator.getIMinFloor();
            for (int i = iMinFloor; i <= iMaxFloor; i++) {
                xAxisDataList.add(String.valueOf(i));
            }
            xAxisDataList.removeIf(e -> "0".equals(e));
            return xAxisDataList;
        }
    }

    /**
     * 获取两个日期之间的时间列表  yyyy-MM-dd HH:00:00
     *
     * @param selectStartTime
     * @param selectEndTime
     * @return
     */
    public static List<String> getDateTimesBetweenTwoDate(Date selectStartTime, Date selectEndTime) {

        List<String> resultDateTimeList = new ArrayList<>();
        //把开始时间加入集合
        String beginDate = DateUtil.format(selectStartTime, "yyyy-MM-dd HH:00:00");
        resultDateTimeList.add(beginDate);

        DateTime dateTime = DateUtil.beginOfHour(selectStartTime);

        while (true) {

            dateTime = DateUtil.offsetHour(dateTime, 1);

            if (dateTime.after(selectEndTime)) {
                break;
            }
            resultDateTimeList.add(DateUtil.format(dateTime, "yyyy-MM-dd HH:00:00"));
        }

        return resultDateTimeList;
    }

}
