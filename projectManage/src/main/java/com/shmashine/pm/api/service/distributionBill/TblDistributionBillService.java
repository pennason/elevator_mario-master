package com.shmashine.pm.api.service.distributionBill;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblDistributionBill;
import com.shmashine.pm.api.module.distributionBill.DistributionBillModule;

public interface TblDistributionBillService {

    int insert(TblDistributionBill record);

    int update(TblDistributionBill record);

    TblDistributionBill selectByBillId(String vDistributionBillId);

    TblDistributionBill selectByElevatorId(String vElevatorId);

    List<TblDistributionBill> selectByTaskId(String vDistributionTaskId);

    List<Map> selectByBillModule(DistributionBillModule module);

    Map getRelatedInfo(String vDistributionBillId);

    List<Integer> getAllStatus(DistributionBillModule module);

    int existVerifyCode(String vVerifyCode, String vDistributionTaskId);

    int updateByTaskId(String vDistributionTaskId);


    /**
     * 获取传感器配置子选项
     *
     * @param elevatorId 电梯id
     * @return 1:烟杆 2:小平层 3:U型光电 4:门磁
     */
    Integer getFloorSensorRemark(String elevatorId);
}
