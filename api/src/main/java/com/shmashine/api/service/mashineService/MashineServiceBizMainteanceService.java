package com.shmashine.api.service.mashineService;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.mashineService.MashineServiceBizMaintenanceModule;

/**
 * 对外电梯服务
 */
public interface MashineServiceBizMainteanceService {

    /**
     * 返回当前用户所有维保列表
     *
     * @return
     */
    PageListResultEntity list(@Param("mashineServiceBizMaintenanceModule") MashineServiceBizMaintenanceModule mashineServiceBizMaintenanceModule);
}
