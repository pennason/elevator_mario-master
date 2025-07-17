package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.elevatorCollect.input.SearchElevatorCollectModule;

public interface BizElevatorCollectDao {

    /**
     * 获取收藏电梯列表
     */
    List<Map> searchElevatorCollectList(SearchElevatorCollectModule searchElevatorCollectModule);

    /**
     * 判断用户是否收藏电梯
     */
    Map searchUserCollectElevatorInfo(@Param("userId") String userId, @Param("elevatorId") String elevatorId);

}
