package com.shmashine.pm.api.service.elevator.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblDeviceDao;
import com.shmashine.pm.api.entity.TblDevice;
import com.shmashine.pm.api.service.elevator.TblDeviceService;

@Service
public class TblDeviceServiceImpl implements TblDeviceService {

    @Autowired
    private TblDeviceDao tblDeviceDao;

    @Override
    public int insertDevice(TblDevice tblDevice) {
        return tblDeviceDao.insert(tblDevice);
    }

    @Override
    public List<TblDevice> selectByEntity(TblDevice tblDevice) {
        return tblDeviceDao.selectByEntity(tblDevice);
    }

    @Override
    public TblDevice selectByDeviceId(String vDeviceId) {
        return tblDeviceDao.selectByDeviceId(vDeviceId);
    }
}
