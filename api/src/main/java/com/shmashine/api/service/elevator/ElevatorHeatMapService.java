package com.shmashine.api.service.elevator;

import java.util.List;

import com.shmashine.api.entity.TblElevatorHeatMap;

public interface ElevatorHeatMapService {

    List getElevatorHeatMap(TblElevatorHeatMap elevatorHeatMapParam);

    List getElevatorHeatMapNew(TblElevatorHeatMap elevatorHeatMapParam);
}
