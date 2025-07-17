package com.shmashine.api.service.elevator.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblElevatorExtInfoDao;
import com.shmashine.api.entity.TblElevatorExtInfo;
import com.shmashine.api.service.elevator.TblElevatorExtInfoService;

@Service
public class TblElevatorExtInfoServiceImpl implements TblElevatorExtInfoService {

    @Autowired
    private TblElevatorExtInfoDao tblElevatorExtInfoDao;


    @Override
    public int insert(TblElevatorExtInfo tblElevatorExtInfo) {
        return tblElevatorExtInfoDao.insert(tblElevatorExtInfo);
    }

    @Override
    public int update(TblElevatorExtInfo tblElevatorExtInfo) {
        return tblElevatorExtInfoDao.update(tblElevatorExtInfo);
    }

    @Override
    public TblElevatorExtInfo getInfoByEntity(TblElevatorExtInfo tblElevatorExtInfo) {
        return tblElevatorExtInfoDao.getInfoByEntity(tblElevatorExtInfo);
    }
}
