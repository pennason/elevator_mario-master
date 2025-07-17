package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.workOrder.ResponseMaintenanceDetailModule;
import com.shmashine.common.entity.TblWorkOrderMaintenanceDetail;

public interface TblWorkOrderMaintenanceDetailDao {

    TblWorkOrderMaintenanceDetail getById(@NotNull String vWorkOrderMaintenanceDetailId);

    List<TblWorkOrderMaintenanceDetail> listByEntity(TblWorkOrderMaintenanceDetail tblWorkOrderMaintenanceDetail);

    List<TblWorkOrderMaintenanceDetail> getByEntity(TblWorkOrderMaintenanceDetail tblWorkOrderMaintenanceDetail);

    List<TblWorkOrderMaintenanceDetail> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblWorkOrderMaintenanceDetail tblWorkOrderMaintenanceDetail);

    int insertBatch(@NotEmpty List<TblWorkOrderMaintenanceDetail> list);

    int update(@NotNull TblWorkOrderMaintenanceDetail tblWorkOrderMaintenanceDetail);

    int updateByField(@NotNull @Param("where") TblWorkOrderMaintenanceDetail where, @NotNull @Param("set") TblWorkOrderMaintenanceDetail set);

    int updateBatch(@NotEmpty List<TblWorkOrderMaintenanceDetail> list);

    int deleteById(@NotNull String vWorkOrderMaintenanceDetailId);

    int deleteByEntity(@NotNull TblWorkOrderMaintenanceDetail tblWorkOrderMaintenanceDetail);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblWorkOrderMaintenanceDetail tblWorkOrderMaintenanceDetail);

    List<ResponseMaintenanceDetailModule> listByWorkOrderDetailId(@Param("workOrderDetailId") String workOrderDetailId);

    void updateHistoryFlagByWorkOrderId(@Param("workOrderId") String workOrderId);

    List<ResponseMaintenanceDetailModule> getMaintenanceDetail(@Param("workOrderId") String workOrderId);
}