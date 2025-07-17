package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BizFileDao {


    int deleteFile(@Param("fileId") String fileId, @Param("businessId") String businessId, @Param("businessType") Integer businessType);

    int deleteFileAll(@Param("businessId") String businessId, @Param("businessType") Integer businessType);

    List<Map> getElevatorImg(@Param("businessId") String businessId, @Param("businessType") Integer businessType);
}
