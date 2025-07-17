package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.mashineService.MashineServiceBizElevatorModule;

public interface MashineServiceBizElevatorDao {
    List<Map<String, Object>> list(@Param("mashineServiceBizElevatorModule") MashineServiceBizElevatorModule mashineServiceBizElevatorModule);
}
