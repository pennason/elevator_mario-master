package com.shmashine.api.task;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.TblElevatorHeatMapDao;
import com.shmashine.api.entity.TblElevatorHeatMap;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.extern.slf4j.Slf4j;

/**
 * @author  jiangheng
 * @version 2024/2/6 14:49
 * @description: 楼层热力图统计数据更正:
 * 已设备上报统计运行次数数据为准，
 * （运行次数 - 热力图统计次数总和} 均摊到每层
 * <p>
 * 注：后期需要更可靠的设备上报楼层热力图消息，不需此算法
 */
@Slf4j
@Component
public class FloorStatisticalCorrection {

    @Resource
    private TblElevatorHeatMapDao tblElevatorHeatMapDao;

    @Resource
    private BizElevatorDao bizElevatorDao;

    /**
     * 12:30  18:30  23:30各执行更正一次
     */
    @XxlJob("floorStatisticalCorrection")
    public void floorStatisticalCorrection() {

        log.info("楼层热力图统计数据更正开始");

        //获取已经统计热力图电梯列表及运行次数
        DateTime today = DateUtil.beginOfDay(DateUtil.date());
        List<TblElevatorHeatMap> elevatorHeatMapRunCount = tblElevatorHeatMapDao.getElevatorHeatMapRunCount(today);

        for (TblElevatorHeatMap heatMapCount : elevatorHeatMapRunCount) {

            //获取该电梯当日运行次数
            Integer runCount = bizElevatorDao.getTodayRunCount(heatMapCount.getElevatorCode());
            if (runCount == null) {
                runCount = 0;
            }

            //获取今日该电梯楼层列表
            List<TblElevatorHeatMap> elevatorHeatMap = tblElevatorHeatMapDao.getListByElevatorCodeAndCountDate(heatMapCount.getElevatorCode(), today);

            //均摊到每层
            int dif = (runCount - heatMapCount.getCountStop()) / elevatorHeatMap.size();

            for (TblElevatorHeatMap tblElevatorHeatMap : elevatorHeatMap) {
                tblElevatorHeatMap.setCountStop(tblElevatorHeatMap.getCountStop() + dif);
            }

            //批量更新
            tblElevatorHeatMapDao.updateList(elevatorHeatMap);

        }

        log.info("楼层热力图统计数据更正完成");

    }
}
