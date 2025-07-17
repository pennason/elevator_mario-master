package com.shmashine.socket.nezha.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.socket.nezha.domain.ElevatorEventRecordDO;

/**
 * 设备上下线记录
 *
 * @author little.li
 */
@Mapper
public interface ElevatorEventRecordDao {

    ElevatorEventRecordDO get(Long id);

    ElevatorEventRecordDO getLatest(String code);

    List<ElevatorEventRecordDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(ElevatorEventRecordDO elevatorEventRecord);

    List<Map<String, Object>> freqsort();

    List<Map<String, Object>> trendmap();

}
