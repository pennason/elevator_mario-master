package com.shmashine.socket.fault.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.socket.fault.entity.TblFaultShield;

/**
 * 故障上报屏蔽规则表(TblFaultShield)表数据库访问层
 *
 * @author little.li
 * @since 2020-06-14 15:18:16
 */
@Mapper
public interface TblFaultShieldDao {


    /**
     * 获取所有故障屏蔽规则
     *
     * @param map 屏蔽规则缓存
     */
    List<TblFaultShield> list(Map<String, Object> map);


    void updateFaultShieldId(@Param("elevatorCode") String elevatorCode,
                             @Param("faultType") Integer faultType,
                             @Param("id") String id);
}