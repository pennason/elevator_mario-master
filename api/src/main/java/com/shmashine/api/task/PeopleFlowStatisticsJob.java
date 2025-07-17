package com.shmashine.api.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import com.shmashine.api.dao.PeopleFlowStatisticsDao;
import com.shmashine.common.message.PeopleFlowStatisticsMessage;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.extern.slf4j.Slf4j;

/**
 * @author  jiangheng
 * @version 2024/1/25 09:59
 * @description: com.shmashine.api.task
 * <p>
 * 人流量统计计算
 */
@Slf4j
@Component
public class PeopleFlowStatisticsJob {

    @Resource
    private PeopleFlowStatisticsDao peopleFlowStatisticsDao;

    /**
     * 人流量统计计算
     * 每天凌晨执行前一天的计算
     * 分批拿取数据
     */
    @XxlJob("StatisticalCalculationOfHumanFlow")
    public void StatisticalCalculationOfHumanFlow() {

        //获取前一天起始时间
        DateTime date = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -1));

        for (int d = 1; d <= 12; d++) {

            DateTime startTime = DateUtil.offsetHour(date, (d - 1) * 2);
            DateTime endTime = DateUtil.offsetHour(date, d * 2);

            //获取当前时间段数据 按梯&时间排序
            List<PeopleFlowStatisticsMessage> peopleFlowStatisticsList = peopleFlowStatisticsDao.getListByTriggerTime(startTime, endTime);

            if (peopleFlowStatisticsList != null && peopleFlowStatisticsList.size() > 0) {

                //计算首条记录
                calculateFirstRecord(peopleFlowStatisticsList.get(0));

                //计算统计人流量
                for (int i = 1; i < peopleFlowStatisticsList.size(); i++) {

                    PeopleFlowStatisticsMessage thisRecord = peopleFlowStatisticsList.get(i);
                    PeopleFlowStatisticsMessage lastRecord = peopleFlowStatisticsList.get(i - 1);
                    Integer result = thisRecord.getIdentificationNumber() == null ? 0 : thisRecord.getIdentificationNumber();

                    //电梯编号不一致
                    if (!thisRecord.getElevatorCode().equals(lastRecord.getElevatorCode())) {
                        lastRecord = peopleFlowStatisticsDao.getPreviousRecord(thisRecord);
                        if (lastRecord == null) {
                            lastRecord = thisRecord;
                        }
                    }

                    //运行方向变动
                    if ((thisRecord.getDirection() + lastRecord.getDirection()) == 0) {

                        PeopleFlowStatisticsMessage newPeopleFlowStatisticsMessage = PeopleFlowStatisticsMessage.builder()
                                .id(IdUtil.getSnowflakeNextId()).elevatorCode(lastRecord.getElevatorCode()).triggerTime(thisRecord.getTriggerTime())
                                .floor(thisRecord.getFloor()).direction(0).identificationNumber(0)
                                .throughput(-(lastRecord.getIdentificationNumber() == null ? 0 : lastRecord.getIdentificationNumber())).build();

                        //新增一条折返记录
                        peopleFlowStatisticsDao.insert(newPeopleFlowStatisticsMessage);

                        thisRecord.setThroughput(result);

                    } else {
                        thisRecord.setThroughput(result - (lastRecord.getIdentificationNumber() == null ? 0 : lastRecord.getIdentificationNumber()));
                    }
                    peopleFlowStatisticsDao.updateById(thisRecord);

                }

            }

        }
    }

    /**
     * 计算首条记录
     *
     * @param peopleFlowStatistics
     */
    private void calculateFirstRecord(PeopleFlowStatisticsMessage peopleFlowStatistics) {
        //获取识别人数
        Integer result = peopleFlowStatistics.getIdentificationNumber() == null ? 0 : peopleFlowStatistics.getIdentificationNumber();
        //获取上一条记录
        PeopleFlowStatisticsMessage previousRecord = peopleFlowStatisticsDao.getPreviousRecord(peopleFlowStatistics);

        if (previousRecord != null) {

            //运行方向变动
            if (!peopleFlowStatistics.getDirection().equals(previousRecord.getDirection())) {

                PeopleFlowStatisticsMessage newPeopleFlowStatisticsMessage = PeopleFlowStatisticsMessage.builder()
                        .id(IdUtil.getSnowflakeNextId()).elevatorCode(previousRecord.getElevatorCode()).triggerTime(peopleFlowStatistics.getTriggerTime())
                        .floor(peopleFlowStatistics.getFloor()).direction(0).identificationNumber(0)
                        .throughput(-(previousRecord.getIdentificationNumber() == null ? 0 : previousRecord.getIdentificationNumber())).build();

                //新增一条折返记录
                peopleFlowStatisticsDao.insert(newPeopleFlowStatisticsMessage);

                peopleFlowStatistics.setThroughput(result);

            } else {
                peopleFlowStatistics.setThroughput(result - (previousRecord.getThroughput() == null ? 0 : previousRecord.getThroughput()));
            }

        } else {
            peopleFlowStatistics.setThroughput(result);
        }

        peopleFlowStatisticsDao.updateById(peopleFlowStatistics);
    }

}
