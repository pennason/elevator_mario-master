// Copyright (C) 2024 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.village;

import com.shmashine.common.entity.TblVillageDeviceBill;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/5/13 14:16
 * @since v1.0
 */

public interface TblVillageDeviceBillServiceI {

    TblVillageDeviceBill getByVillageId(String villageId);


    int addVillageDeviceBill(TblVillageDeviceBill tblVillageDeviceBill);

    /**
     * 根据小区id更新
     *
     * @param tblVillageDeviceBill 更新实体
     */
    void updateByVillageId(TblVillageDeviceBill tblVillageDeviceBill);

}
