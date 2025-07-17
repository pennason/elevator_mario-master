package com.shmashine.sender.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Mapper
public interface TblFaultDao {

    List<String> getFaultTypeByElevatorCode(String elevatorCode);

}