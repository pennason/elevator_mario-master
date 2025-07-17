package com.shmashine.fault.fault.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.fault.fault.dao.TblElevatorEventDao;
import com.shmashine.fault.fault.entity.TblElevatorEvent;
import com.shmashine.fault.fault.service.EventService;

/**
 * EventServiceImpl
 *
 * @author jiangheng
 * @version V1.0.0 - 2021/5/6 14:09
 */
@Service
public class EventServiceImpl implements EventService {

    @Resource
    private TblElevatorEventDao tblElevatorEventDao;

    @Override
    public void insert(TblElevatorEvent tblElevatorEvent) {
        tblElevatorEventDao.insert(tblElevatorEvent);
    }
}
