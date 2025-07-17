package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BizEnventNoticeDao {
    List<Map> searchEnoentNotice(@Param("vNoticeType") String vNoticeType, @Param("isAdminFlag") boolean isAdminFlag, @Param("userId") String userId,
                                 @Param("elevatorIds") List<String> elevatorIds);
}
