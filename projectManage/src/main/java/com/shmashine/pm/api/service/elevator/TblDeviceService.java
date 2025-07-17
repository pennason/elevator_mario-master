package com.shmashine.pm.api.service.elevator;

import java.util.List;

import com.shmashine.pm.api.entity.TblDevice;

public interface TblDeviceService {

    int insertDevice(TblDevice tblDevice);

    List<TblDevice> selectByEntity(TblDevice tblDevice);

    TblDevice selectByDeviceId(String vDeviceId);
}
