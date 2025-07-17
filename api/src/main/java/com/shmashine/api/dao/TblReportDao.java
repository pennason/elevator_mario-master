package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.api.entity.TblRequestXmReport;

/**
 * 雄迈报告
 *
 * @author jiangheng
 * @since 2020/12/28 —— 11:38
 */

@Mapper
public interface TblReportDao {

    /**
     * 分页查询雄迈报告
     *
     * @param tblRequestXmReport
     * @return
     */
    List<Map<String, Object>> queryXmReportBypage(@Param("tblRequestXmReport") TblRequestXmReport tblRequestXmReport, @Param("eleCodeList") List<String> eleCodeList);

    /**
     * 手动获取视频
     *
     * @param faultId
     * @return
     */
    String getUrlByHand(String faultId);
}
