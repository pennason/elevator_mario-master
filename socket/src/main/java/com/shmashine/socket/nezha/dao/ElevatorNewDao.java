package com.shmashine.socket.nezha.dao;


import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 电梯表接口
 *
 * @author little.li
 */
@Mapper
public interface ElevatorNewDao {


    int updateDevice(Map<String, Object> map);

    int updateVersion(Map<String, Object> map);

    int updateOnline(String code);

    int updateOffline(String code);


}
