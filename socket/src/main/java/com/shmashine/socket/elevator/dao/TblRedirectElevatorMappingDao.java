package com.shmashine.socket.elevator.dao;


import org.apache.ibatis.annotations.Mapper;

import com.shmashine.socket.elevator.entity.TblRedirectElevatorMapping;

/**
 * 电梯表(TblElevator)表数据库访问层
 *
 * @author little.li
 * @since 2020-06-14 15:17:39
 */
@Mapper
public interface TblRedirectElevatorMappingDao {

    /**
     * 根据电梯编号获取坏电梯列表
     *
     * @param elevatorCode 电梯编号
     */
    TblRedirectElevatorMapping getByElevatorCode(String elevatorCode);

}