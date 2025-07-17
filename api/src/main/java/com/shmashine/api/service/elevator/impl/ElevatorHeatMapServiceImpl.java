package com.shmashine.api.service.elevator.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.entity.TblElevatorHeatMap;
import com.shmashine.api.service.elevator.ElevatorHeatMapService;
import com.shmashine.api.service.elevator.TblElevatorHeatMapService;

@Service
public class ElevatorHeatMapServiceImpl implements ElevatorHeatMapService {

    @Resource
    private TblElevatorHeatMapService tblElevatorHeatMapService;

    @Override
    public List getElevatorHeatMap(TblElevatorHeatMap elevatorHeatMapParam) {

        List<TblElevatorHeatMap> tblElevatorHeatMaps = tblElevatorHeatMapService.getTblElevatorHeatMap(elevatorHeatMapParam);

        Set<String> elevatorCodeSet = new TreeSet<>();

        //电梯编号去重
        for (TblElevatorHeatMap elevatorHeatMapEntity : tblElevatorHeatMaps) {
            String elevatorCode = elevatorHeatMapEntity.getElevatorCode();
            elevatorCodeSet.add(elevatorCode);
        }

        //电梯编号对应哪些楼层 1:n
        Map<String, Set<Integer>> elevatorFloorNumberMap = new HashMap<>();
        for (String elevatorCode : elevatorCodeSet) {

            Set<Integer> floorNumberSet = new TreeSet<>();
            for (TblElevatorHeatMap elevatorHeatMapEntity : tblElevatorHeatMaps) {

                String currElevatorCode = elevatorHeatMapEntity.getElevatorCode();
                Integer currFloorNumber = elevatorHeatMapEntity.getFloorNumber();

                if (elevatorCode.equals(currElevatorCode)) {
                    floorNumberSet.add(currFloorNumber);
                }
            }
            elevatorFloorNumberMap.put(elevatorCode, floorNumberSet);
        }

        List countElevatorHeatMapList = new ArrayList<>();
        for (Map.Entry<String, Set<Integer>> entry : elevatorFloorNumberMap.entrySet()) {
            Map countElevatorHeatMap = new HashMap<>();
            String elevatorCode = entry.getKey();
            Set<Integer> floorNumberSet = entry.getValue();

            List floorNumberCountInfoMapList = new ArrayList<>();
            Map floorNumberCountInfoMap = new HashMap<>();
            List floorNumberInfoMapList = new ArrayList<>();

            for (Integer floorNumber : floorNumberSet) {

                Map floorNumberInfoMap = new HashMap<>();
                List countInfoMapList = new ArrayList<>();

                for (TblElevatorHeatMap elevatorHeatMap : tblElevatorHeatMaps) {

                    String currElevatorCode = elevatorHeatMap.getElevatorCode();
                    Integer currFloorNumber = elevatorHeatMap.getFloorNumber();
                    Integer currCountStop = elevatorHeatMap.getCountStop();
                    String currCountDate = elevatorHeatMap.getCountDate();

                    if (elevatorCode.equals(currElevatorCode) && floorNumber.equals(currFloorNumber)) {

                        Map countInfoMap = new HashMap<>();
                        countInfoMap.put("countStop", currCountStop);
                        countInfoMap.put("countDate", currCountDate);

                        countInfoMapList.add(countInfoMap);
                    }
                }

                floorNumberInfoMap.put("floorNumber", floorNumber);
                floorNumberInfoMap.put("countDateInfo", countInfoMapList);

                floorNumberInfoMapList.add(floorNumberInfoMap);
            }
/*            floorNumberCountInfoMap.put("floorNumberInfo",floorNumberInfoMapList);
            floorNumberCountInfoMapList.add(floorNumberCountInfoMap);
            countElevatorHeatMap.put("elevatorCode",elevatorCode);
            countElevatorHeatMap.put("floorNumberCountInfo",floorNumberCountInfoMapList);*/

/*            floorNumberCountInfoMap.put("floorNumberInfo",floorNumberInfoMapList);
            floorNumberCountInfoMapList.add(floorNumberCountInfoMap);*/
            countElevatorHeatMap.put("elevatorCode", elevatorCode);
            countElevatorHeatMap.put("floorNumberCountInfo", floorNumberInfoMapList);

            countElevatorHeatMapList.add(countElevatorHeatMap);
        }

        return countElevatorHeatMapList;
    }

    @Override
    public List getElevatorHeatMapNew(TblElevatorHeatMap elevatorHeatMapParam) {

        List<TblElevatorHeatMap> multipleTblElevatorHeatMap = new ArrayList<>();

        String elevatorCodeParam = elevatorHeatMapParam.getElevatorCode();

        if (elevatorCodeParam.contains(",")) {
            String[] elevatorCodeArr = elevatorCodeParam.split(",");
            for (String elevatorCode : elevatorCodeArr) {

                TblElevatorHeatMap tblElevatorHeatMap = new TblElevatorHeatMap();
                tblElevatorHeatMap.setElevatorCode(elevatorCode);
                tblElevatorHeatMap.setFloorNumber(elevatorHeatMapParam.getFloorNumber());
                tblElevatorHeatMap.setStartDate(elevatorHeatMapParam.getStartDate());
                tblElevatorHeatMap.setEndDate(elevatorHeatMapParam.getEndDate());

                multipleTblElevatorHeatMap.add(tblElevatorHeatMap);
            }
        }

        List countElevatorHeatMapList = new ArrayList<>();


        if (multipleTblElevatorHeatMap.size() > 0) {
            for (TblElevatorHeatMap elevatorHeatMapParamTemp : multipleTblElevatorHeatMap) {
                Map singletonCountElevatorHeatMap = getSingletonCountElevatorHeatMap(elevatorHeatMapParamTemp);
                if (singletonCountElevatorHeatMap.size() > 0) {
                    countElevatorHeatMapList.add(singletonCountElevatorHeatMap);
                }
            }
        } else {
            Map singletonCountElevatorHeatMap = getSingletonCountElevatorHeatMap(elevatorHeatMapParam);
            if (singletonCountElevatorHeatMap.size() > 0) {
                countElevatorHeatMapList.add(singletonCountElevatorHeatMap);
            }
            countElevatorHeatMapList.add(singletonCountElevatorHeatMap);
        }

        return countElevatorHeatMapList;
    }

    private Map getSingletonCountElevatorHeatMap(TblElevatorHeatMap elevatorHeatMapParam) {

        List<TblElevatorHeatMap> tblElevatorHeatMaps = tblElevatorHeatMapService.getTblElevatorHeatMapNew(elevatorHeatMapParam);

        Set<String> elevatorCodeSet = new TreeSet<>();

        //电梯编号去重
        for (TblElevatorHeatMap elevatorHeatMapEntity : tblElevatorHeatMaps) {
            String elevatorCode = elevatorHeatMapEntity.getElevatorCode();
            elevatorCodeSet.add(elevatorCode);
        }

        //电梯编号对应哪些楼层 1:n
        Map<String, Set<Integer>> elevatorFloorNumberMap = new HashMap<>();
        for (String elevatorCode : elevatorCodeSet) {

            Set<Integer> floorNumberSet = new TreeSet<>();
            for (TblElevatorHeatMap elevatorHeatMapEntity : tblElevatorHeatMaps) {

                String currElevatorCode = elevatorHeatMapEntity.getElevatorCode();
                Integer currFloorNumber = elevatorHeatMapEntity.getFloorNumber();

                if (elevatorCode.equals(currElevatorCode)) {
                    floorNumberSet.add(currFloorNumber);
                }
            }
            elevatorFloorNumberMap.put(elevatorCode, floorNumberSet);
        }

        Map countElevatorHeatMap = new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : elevatorFloorNumberMap.entrySet()) {

            String elevatorCode = entry.getKey();
            Set<Integer> floorNumberSet = entry.getValue();

            List floorNumberInfoMapList = new ArrayList<>();

            for (Integer floorNumber : floorNumberSet) {

                Map floorNumberInfoMap = new HashMap<>();
                List countInfoMapList = new ArrayList<>();

                for (TblElevatorHeatMap elevatorHeatMap : tblElevatorHeatMaps) {

                    String currElevatorCode = elevatorHeatMap.getElevatorCode();
                    Integer currFloorNumber = elevatorHeatMap.getFloorNumber();
                    Integer currCountStop = elevatorHeatMap.getCountStop();
                    String currCountDate = elevatorHeatMap.getCountDate();

                    if (elevatorCode.equals(currElevatorCode) && floorNumber.equals(currFloorNumber)) {

                        Map countInfoMap = new HashMap<>();
                        countInfoMap.put("countStop", currCountStop);
                        countInfoMap.put("countDate", currCountDate);

                        countInfoMapList.add(countInfoMap);
                    }
                }

                floorNumberInfoMap.put("floorNumber", floorNumber);
                floorNumberInfoMap.put("countDateInfo", countInfoMapList);

                floorNumberInfoMapList.add(floorNumberInfoMap);
            }

            countElevatorHeatMap.put("elevatorCode", elevatorCode);
            countElevatorHeatMap.put("floorNumberCountInfo", floorNumberInfoMapList);

        }

        return countElevatorHeatMap;
    }
}
