package com.shmashine.api.service.map;

import java.util.List;
import java.util.Map;

public interface BizMapService {

    List<Map> searchElevatorCountInfo(String userId, boolean isAdminFlag, List<String> projectIds, List<String> villageIds);

    List<Map> getVillageElevatorList(String villageId, String userId, boolean isAdminFlag);

    Map getElevatorPosition(String elevatorId);
}
