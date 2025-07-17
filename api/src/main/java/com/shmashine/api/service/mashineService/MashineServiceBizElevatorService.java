package com.shmashine.api.service.mashineService;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.mashineService.MashineServiceBizElevatorModule;

/**
 * 对外电梯服务
 */
public interface MashineServiceBizElevatorService {
    /**
     * 返回当前用户所有电梯列表
     *
     * @return
     */
    PageListResultEntity list(@Param("mashineServiceBizElevatorModule") MashineServiceBizElevatorModule mashineServiceBizElevatorModule);
}
