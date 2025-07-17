package com.shmashine.fault.service.PeopleFlowStatistics.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shmashine.common.entity.TblCameraImageIdentifyEntity;
import com.shmashine.common.message.PeopleFlowStatisticsMessage;
import com.shmashine.fault.dal.dao.PeopleFlowStatisticsDao;
import com.shmashine.fault.dal.dao.TblCameraImageIdentifyMapper;
import com.shmashine.fault.service.PeopleFlowStatistics.PeopleFlowStatisticsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 人流量统计服务实现类
 *
 * @author jiangheng
 * @version V1.0.0 - 2024/1/8 17:08
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PeopleFlowStatisticsServiceImpl implements PeopleFlowStatisticsService {

    private final PeopleFlowStatisticsDao peopleFlowStatisticsDao;

    private final TblCameraImageIdentifyMapper cameraImageIdentifyMapper;

    @Override
    public void insert(PeopleFlowStatisticsMessage peopleFlowStatisticsMessage) {
        peopleFlowStatisticsDao.insert(peopleFlowStatisticsMessage);
    }

    @Override
    public void insertCameraImageIdentify(TblCameraImageIdentifyEntity cameraImageIdentifyEntity) {
        cameraImageIdentifyMapper.insert(cameraImageIdentifyEntity);
    }
}
