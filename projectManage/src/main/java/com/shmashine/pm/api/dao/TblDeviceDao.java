package com.shmashine.pm.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.TblDevice;

public interface TblDeviceDao {

    int insert(TblDevice tblDevice);

    List<TblDevice> selectByEntity(TblDevice tblDevice);

    TblDevice selectByDeviceId(@Param("vDeviceId") String vDeviceId);
}
