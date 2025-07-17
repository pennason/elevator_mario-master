package com.shmashine.socket.nezha.service;


import java.util.List;
import java.util.Map;

import com.shmashine.socket.nezha.domain.FailureShieldDO;

/**
 * 故障上报规则表
 *
 * @author little.li
 */
public interface FailureShieldService {


    List<FailureShieldDO> list(Map<String, Object> map);

}
