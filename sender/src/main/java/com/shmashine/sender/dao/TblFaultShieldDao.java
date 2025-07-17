package com.shmashine.sender.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.sender.entity.TblFaultShield;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Mapper
public interface TblFaultShieldDao {
    List<TblFaultShield> getFaultShieldByElevatorCode(@Param("elevatorCode") String elevatorCode);
}
