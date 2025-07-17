package com.shmashine.api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 系统所需Dao
 */
public interface BizSystemDao {

    List<Map> getSelectList(@Param("mainId") String mainId);


    List<Map> getPlatformSelectList(@Param("mainId") String mainId);

    List<Map> getWorkOrderSelectList(@Param("mainId") String mainId);

    List<Map> getAreaSelectList(@Param("parentId") String parentId);

    /**
     * 获取故障标准
     */
    List<HashMap> getFaultDefinitionSelectList(@Param("isAdminFlag") Boolean isAdminFlag, @Param("userId") String userId, @Param("elevatorIds") List<String> elevatorIds);
}
