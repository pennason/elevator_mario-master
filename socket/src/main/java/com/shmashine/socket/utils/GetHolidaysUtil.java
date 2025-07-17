package com.shmashine.socket.utils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.socket.dal.dao.HolidayDao;
import com.shmashine.socket.dal.dto.HolidayDO;

/**
 * 获取节假日信息
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/12/10 17:15
 * @Since: 1.0.0
 */
@Component
public class GetHolidaysUtil {

    private static final String HOST = "timor.tech";

    @Resource
    private HolidayDao holidayDao;

    /**
     * 获取年度的节假日信息
     *
     * @param year 年度
     * @return 节假日信息
     */
    public static String getHolidaysByYear(Integer year) {
        //获取api数据
        String url = StrUtil.format(HOST + "/api/holiday/year/{}?week=Y", year);
        return HttpUtil.get(url, 10000);
    }

    /**
     * 获取节假日信息
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 节假日信息
     */
    public List<Date> getHolidays(Date startDate, Date endDate) {

        //年份不一致
        if (DateUtil.year(startDate) != DateUtil.year(endDate)) {
            getHolidaysInTable(startDate, null);
            getHolidaysInTable(endDate, null);
        }

        return getHolidaysInTable(startDate, endDate);
    }

    /**
     * 获取表中数据节假日信息
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 节假日信息
     */
    public List<Date> getHolidaysInTable(Date startDate, Date endDate) {

        List<HolidayDO> holidays;

        if (endDate == null) {
            holidays = holidayDao.getByDate(DateUtil.beginOfYear(startDate), endDate);
            if (holidays == null || holidays.size() == 0) {
                //获取api数据
                String holidaysByYear = getHolidaysByYear(DateUtil.year(startDate));

                holidays = JSON.parseObject(holidaysByYear, JSONObject.class).getJSONObject("holiday")
                        .values().stream().map(item -> JSON.parseObject(JSON.toJSONString(item), HolidayDO.class))
                        .collect(Collectors.toList());

                //api数据更新到表中
                holidayDao.insertBatch(holidays);
            }
        } else {
            holidays = holidayDao.getByDate(startDate, endDate);

            return holidays.stream().filter(d -> (d.getDate().after(startDate) && d.getDate().before(endDate))
                            || d.getDate().equals(startDate) || d.getDate().equals(endDate))
                    .map(HolidayDO::getDate).collect(Collectors.toList());
        }

        return null;
    }

}
