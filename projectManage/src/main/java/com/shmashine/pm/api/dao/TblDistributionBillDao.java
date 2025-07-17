package com.shmashine.pm.api.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.TblDistributionBill;
import com.shmashine.pm.api.module.distributionBill.DistributionBillModule;

public interface TblDistributionBillDao {

    int insert(TblDistributionBill record);

    int update(TblDistributionBill record);

    TblDistributionBill selectByBillId(@Param("vDistributionBillId") String vDistributionBillId);

    /**
     * 根据电梯id获取配货单
     *
     * @param vElevatorId 电梯id
     * @return 配货单
     */
    TblDistributionBill selectByElevatorId(@Param("vElevatorId") String vElevatorId);

    List<Map> selectByBillModule(DistributionBillModule module);

    Map getRelatedInfo(@Param("vDistributionBillId") String vDistributionBillId);

    List<Integer> getAllStatus(DistributionBillModule module);

    List<TblDistributionBill> selectByTaskId(@Param("vDistributionTaskId") String vDistributionTaskId);

    int existVerifyCode(@Param("vVerifyCode") String vVerifyCode, @Param("vDistributionTaskId") String vDistributionTaskId);

    int updateByTaskId(@Param("vDistributionTaskId") String vDistributionTaskId);
}
