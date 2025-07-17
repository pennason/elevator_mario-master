// Copyright (C) 2024 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.village.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblVillageDeviceBillMapper;
import com.shmashine.api.service.village.TblVillageDeviceBillServiceI;
import com.shmashine.common.entity.TblVillageDeviceBill;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/5/13 14:16
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TblVillageDeviceBillServiceImpl implements TblVillageDeviceBillServiceI {
    private final TblVillageDeviceBillMapper villageDeviceBillMapper;

    @Override
    public TblVillageDeviceBill getByVillageId(String villageId) {
        return villageDeviceBillMapper.getByVillageId(villageId);
    }

    @Override
    public int addVillageDeviceBill(TblVillageDeviceBill tblVillageDeviceBill) {
        return villageDeviceBillMapper.insert(tblVillageDeviceBill);
    }

    @Override
    public void updateByVillageId(TblVillageDeviceBill tblVillageDeviceBill) {
        villageDeviceBillMapper.updateByVillageId(tblVillageDeviceBill);
    }
}
