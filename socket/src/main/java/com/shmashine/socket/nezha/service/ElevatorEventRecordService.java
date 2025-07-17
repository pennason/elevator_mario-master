package com.shmashine.socket.nezha.service;


import java.util.List;
import java.util.Map;

import com.shmashine.socket.nezha.domain.ElevatorEventRecordDO;

/**
 * 设备上下线记录
 *
 * @author little.li
 */
public interface ElevatorEventRecordService {

    ElevatorEventRecordDO get(Long id);

    ElevatorEventRecordDO getLatest(String code);

    List<ElevatorEventRecordDO> list(Map<String, Object> map);

    List<Map<String, Object>> freqsortlist(Map<String, Object> map);

    List<Map<String, Object>> trendmap(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(ElevatorEventRecordDO elevatorEventRecord);


}
