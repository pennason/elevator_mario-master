package com.shmashine.pm.api.service.elevator.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblElevatorBrandDao;
import com.shmashine.pm.api.service.elevator.TblElevatorBrandService;

@Service
public class TblElevatorBrandServiceImpl implements TblElevatorBrandService {

    @Autowired
    private TblElevatorBrandDao tblElevatorBrandDao;

    @Override
    public HashMap selectBrandByName(String brandName) {
        return tblElevatorBrandDao.selectBrandByName(brandName);
    }
}
