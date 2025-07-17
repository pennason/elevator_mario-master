package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.shmashine.common.entity.TblVillageDeviceBill;

/**
 * 查询 小区配货表
 */

public interface TblVillageDeviceBillMapper {
    TblVillageDeviceBill getById(@NotNull String vVillageDeviceBillId);

    TblVillageDeviceBill getByVillageId(@NotNull String vVillageId);

    List<TblVillageDeviceBill> getByEntity(TblVillageDeviceBill tblVillageDeviceBill);

    int insert(@NotNull TblVillageDeviceBill tblVillageDeviceBill);

    int update(@NotNull TblVillageDeviceBill tblVillageDeviceBill);

    /**
     * 根据小区id更新
     *
     * @param tblVillageDeviceBill 更新实体
     */
    void updateByVillageId(TblVillageDeviceBill tblVillageDeviceBill);
}
