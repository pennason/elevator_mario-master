package com.shmashine.socket.dal.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.socket.dal.dto.HolidayDO;

/**
 * 节假日信息查询接口
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/12/11 10:08
 * @Since: 1.0.0
 */
@Mapper
public interface HolidayDao {

    /**
     * 根据日期查询节假日信息
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 节假日信息
     */
    List<HolidayDO> getByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 批量添加到表中
     *
     * @param holidays 节假日信息
     */
    void insertBatch(List<HolidayDO> holidays);
}
