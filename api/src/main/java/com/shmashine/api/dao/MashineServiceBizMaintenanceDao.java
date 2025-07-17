package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.mashineService.MashineServiceBizMaintenanceModule;

public interface MashineServiceBizMaintenanceDao {

    List<Map<String, Object>> list(@Param("mashineServiceBizMaintenanceModule") MashineServiceBizMaintenanceModule mashineServiceBizMaintenanceModule);
}
