package com.shmashine.pm.api.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Param;

public interface TblElevatorBrandDao {

    HashMap selectBrandByName(@Param("brandName") String brandName);
}
