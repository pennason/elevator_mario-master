package com.shmashine.socket.nezha.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.socket.nezha.domain.FailureShieldDO;

/**
 * 故障上报规则表
 *
 * @author little.li
 */
@Mapper
public interface FailureShieldDao {


    List<FailureShieldDO> list(Map<String, Object> map);


}
