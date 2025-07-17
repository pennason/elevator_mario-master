package com.shmashine.api.service.fault;

import java.util.Map;

/**
 * 故障统计接口
 *
 * @author little.li
 */
public interface FaultStatisticalService {


    Map<String, Object> getFaultStatisticalByDate(Map<String, Object> parms, String userId);


    Map<String, Object> getFaultStatisticalByElevator(Map<String, Object> parms, String userId);

}
