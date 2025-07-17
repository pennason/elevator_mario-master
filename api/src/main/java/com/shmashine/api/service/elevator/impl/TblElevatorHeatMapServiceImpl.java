package com.shmashine.api.service.elevator.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblElevatorHeatMapDao;
import com.shmashine.api.entity.TblElevatorHeatMap;
import com.shmashine.api.service.elevator.TblElevatorHeatMapService;

@Service
public class TblElevatorHeatMapServiceImpl implements TblElevatorHeatMapService {

    @Resource
    private TblElevatorHeatMapDao tblElevatorHeatMapDao;

    @Override
    public void insertElevatorFloorNumberCountInfo(TblElevatorHeatMap tblElevatorHeatMap) {
        tblElevatorHeatMapDao.insertElevatorFloorNumberCountInfo(tblElevatorHeatMap);
    }

    @Override
    public void modifyElevatorFloorNumberCountInfo(TblElevatorHeatMap tblElevatorHeatMap) {
        tblElevatorHeatMapDao.modifyElevatorFloorNumberCountInfo(tblElevatorHeatMap);
    }

    @Override
    public List<TblElevatorHeatMap> getTblElevatorHeatMap(TblElevatorHeatMap tblElevatorHeatMap) {
        return tblElevatorHeatMapDao.getTblElevatorHeatMap(tblElevatorHeatMap);
    }

    @Override
    public List<TblElevatorHeatMap> getTblElevatorHeatMapNew(TblElevatorHeatMap tblElevatorHeatMap) {
        return tblElevatorHeatMapDao.getTblElevatorHeatMapNew(tblElevatorHeatMap);
    }

    @Override
    public List<TblElevatorHeatMap> getAllElevatorCode(TblElevatorHeatMap elevatorHeatMapParam) {
        return tblElevatorHeatMapDao.getAllElevatorCode(elevatorHeatMapParam);
    }

}
