package com.shmashine.socket.fault.service;

import java.util.List;
import java.util.Map;

import com.shmashine.socket.fault.entity.TblFaultShield;

/**
 * 故障上报屏蔽规则表(TblFaultShield)表服务接口
 *
 * @author little.li
 * @since 2020-06-14 15:18:16
 */
public interface TblFaultShieldService {


    /**
     * 获取所有故障屏蔽规则
     *
     * @param map 屏蔽规则缓存
     */
    List<TblFaultShield> list(Map<String, Object> map);


    void updateFaultShieldId(String elevatorCode, Integer faultType, long id);
}