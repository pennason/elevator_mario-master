package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.hutool.core.date.DateTime;

import com.shmashine.api.entity.TblElevatorHeatMap;

@Mapper
public interface TblElevatorHeatMapDao {

    void insertElevatorFloorNumberCountInfo(TblElevatorHeatMap tblElevatorHeatMap);

    void modifyElevatorFloorNumberCountInfo(TblElevatorHeatMap tblElevatorHeatMap);

    List<TblElevatorHeatMap> getTblElevatorHeatMap(TblElevatorHeatMap tblElevatorHeatMap);

    List<TblElevatorHeatMap> getTblElevatorHeatMapNew(TblElevatorHeatMap tblElevatorHeatMap);

    List<TblElevatorHeatMap> getAllElevatorCode(TblElevatorHeatMap elevatorHeatMapParam);

    /**
     * 根据电梯获取电梯楼层热力图次数统计
     *
     * @param countDate
     * @return
     */
    List<TblElevatorHeatMap> getElevatorHeatMapRunCount(DateTime countDate);

    /**
     * 获取今日该电梯楼层列表
     *
     * @param elevatorCode
     * @param today
     * @return
     */
    List<TblElevatorHeatMap> getListByElevatorCodeAndCountDate(@Param("elevatorCode") String elevatorCode, @Param("today") DateTime today);

    /**
     * 批量更新
     *
     * @param list
     */
    void updateList(List<TblElevatorHeatMap> list);
}
