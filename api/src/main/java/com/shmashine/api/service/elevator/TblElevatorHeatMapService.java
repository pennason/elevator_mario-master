package com.shmashine.api.service.elevator;

import java.util.List;

import com.shmashine.api.entity.TblElevatorHeatMap;

public interface TblElevatorHeatMapService {
    void insertElevatorFloorNumberCountInfo(TblElevatorHeatMap tblElevatorHeatMap);

    void modifyElevatorFloorNumberCountInfo(TblElevatorHeatMap tblElevatorHeatMap);

    List<TblElevatorHeatMap> getTblElevatorHeatMap(TblElevatorHeatMap tblElevatorHeatMap);

    List<TblElevatorHeatMap> getTblElevatorHeatMapNew(TblElevatorHeatMap tblElevatorHeatMap);

    List<TblElevatorHeatMap> getAllElevatorCode(TblElevatorHeatMap elevatorHeatMapParam);

}
