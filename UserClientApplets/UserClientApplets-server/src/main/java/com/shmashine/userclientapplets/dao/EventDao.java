package com.shmashine.userclientapplets.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.userclientapplets.entity.Event;

/**
 * EventDao
 *
 * @author jiangheng
 * @version V1.0 - 2022/2/15 14:34
 */
public interface EventDao extends BaseMapper<Event> {

    @Select("SELECT trd.status_time, trd.status, trd.HANDLER, trd.handler_tel, trd.rescue_company_name,"
            + "CASE trd.status "
            + "WHEN 1 THEN '渠道上报' "
            + "WHEN 2 THEN '新建工单' "
            + "WHEN 3 THEN '已接单' "
            + "WHEN 4 THEN '已签到' "
            + "WHEN 5 THEN '已完成' "
            + "WHEN 6 THEN '已确认' "
            + "END AS COMMENT "
            + "FROM "
            + "tbl_third_party_ruijin_envent_detail trd "
            + "INNER JOIN tbl_third_party_ruijin_envent tr ON trd.event_id = tr.event_id "
            + "WHERE tr.reporter like CONCAT('%',#{faultId},'%') "
            + "ORDER BY trd.status_time DESC")
    List<Map> getEventStatusByfaultId(String faultId);

}
