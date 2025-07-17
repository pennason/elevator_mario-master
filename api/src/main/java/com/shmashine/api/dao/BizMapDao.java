package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BizMapDao {

    /**
     * 加载地图一层接口
     *
     * @param userId
     * @param isAdminFlag
     * @return
     */
    List<Map> searchMapElevatorList(@Param("userId") String userId, @Param("isAdminFlag") boolean isAdminFlag, @Param("elevatorIds") List<String> elevatorIds);

    List<Map> getElevatorInfo(@Param("villageId") String villageId, @Param("userId") String userId, @Param("isAdminFlag") boolean isAdminFlag);

    Map getElevatorPosition(@Param("elevatorId") String elevatorId);

}
