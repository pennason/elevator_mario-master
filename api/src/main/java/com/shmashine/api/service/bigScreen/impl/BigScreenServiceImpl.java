package com.shmashine.api.service.bigScreen.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.BizBigScreenDao;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.api.service.bigScreen.BigScreenService;
import com.shmashine.common.utils.EChartDateUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BigScreenServiceImpl implements BigScreenService {

    @Autowired
    private final BizBigScreenDao bizBigScreenDao;

    public BigScreenServiceImpl(BizBigScreenDao bizBigScreenDao) {
        this.bizBigScreenDao = bizBigScreenDao;
    }

    @Override
    public Map countElevatorV1(String villageId, String userId, boolean isAdminFlag) {
        HashMap<Object, Object> map = new HashMap<>();
        List<Map> maps = bizBigScreenDao.countElevatorV1(villageId, userId, isAdminFlag);
        maps.forEach(item -> {
            map.put(item.get("key"), item.get("number"));
        });
        return map;
    }

    /**
     * 电梯分类数量列表  通用
     *
     * @return
     */
    @Override
    public List<Map> elevatorClassificationQuantityListV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizBigScreenDao.countElevatorClassificationInfoV1(faultStatisticalQuantitySearchModule);
    }

    /**
     * 不文明行为统计 不文明种类次数 通用
     *
     * @return
     */
    @Override
    public List<Map> frequencyOfUncivilizedSpeciesV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        List<Map> maps = bizBigScreenDao.uncivilizedBehaviorV1(faultStatisticalQuantitySearchModule);
        if (maps != null && maps.size() != 0) {
            return maps;
        } else {
            return bizBigScreenDao.searchUncivilizedBehavior();
        }
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
            return bizBigScreenDao.uncivilizedBehaviorByTopThreeV1RunCount(faultStatisticalQuantitySearchModule);
        } else {
            return bizBigScreenDao.uncivilizedBehaviorByTopThreeV1(faultStatisticalQuantitySearchModule, faultStatisticalQuantitySearchModule.getFaultType());
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
            resTemp = bizBigScreenDao.trendOfUncivilizedBehaviorInThisYearV1(faultStatisticalQuantitySearchModule);
            return EChartDateUtils.getDataOnYear(resTemp, null, null, null);
        } else if (timeFlag.equals("11")) {
            resTemp = bizBigScreenDao.trendOfUncivilizedBehaviorInThePreviousMonthV1(faultStatisticalQuantitySearchModule);
            return EChartDateUtils.getDataOnMonth(resTemp, null, null, null);
        } else {
            resTemp = bizBigScreenDao.trendOfUncivilizedBehaviorInThePreviousWeekV1(faultStatisticalQuantitySearchModule);
            return EChartDateUtils.getDataOnWeek(resTemp, null, null, null);
        }
    }

    /**
     * 智能监管
     *
     * @return
     */
    @Override
    public List<Map> intelligentSupervisionV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizBigScreenDao.intelligentSupervisionV1(faultStatisticalQuantitySearchModule);
    }

    /**
     * 困人来源排行
     *
     * @return
     */
    @Override
    public List<Map> rankingOfPoorPeopleV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizBigScreenDao.statisticsOnTheSourceOfPovertyV1(faultStatisticalQuantitySearchModule);
    }

    /**
     * 统计故障次数信息
     *
     * @return
     */
    @Override
    public List<Map> statisticsOfFailureTimesV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        //儿童医院项目统计平台故障
        if ("8115537080661639168".equals(faultStatisticalQuantitySearchModule.getvProjectId())) {
            return bizBigScreenDao.faultStatisticalQuantityByMX(faultStatisticalQuantitySearchModule);
        }
        //其他项目统计仪电反推故障
        return bizBigScreenDao.faultStatisticalQuantityV1(faultStatisticalQuantitySearchModule);
    }

    /**
     * 电梯故障排名
     *
     * @return
     */
    @Override
    public List<Map> elevatorFailureRankingV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizBigScreenDao.statisticsFaultPovertyV1(faultStatisticalQuantitySearchModule);
    }

    @Override
    public Map getElevatorInfoV1(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return bizBigScreenDao.getElevatorInfoV1(faultStatisticalQuantitySearchModule);
    }
}
