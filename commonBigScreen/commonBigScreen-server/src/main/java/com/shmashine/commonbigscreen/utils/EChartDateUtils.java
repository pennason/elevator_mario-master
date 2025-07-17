package com.shmashine.commonbigscreen.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

/**
 * echart图表统计  年/月/周 统计工具类
 */
public class EChartDateUtils {


    /**
     * 获取一年的数据
     *
     * @param list      图表的原始数据
     * @param nameKey   原始数据中 用来作为 y轴 name 的字段名称
     * @param dateKey   原始数据中 用来作为 y轴 匹配X轴时间 的字段名称
     * @param numberKey 原始数据中 用来作为 y轴 数值 的字段名称
     */
    public static HashMap<String, Object> getDataOnYear(List<HashMap<String, Object>> list, String nameKey,
                                                        String dateKey, String numberKey) {
        ArrayList<String> times = DateUtils.getPastMonthList(12);
        return getData(times, list, nameKey, dateKey, numberKey);
    }

    /**
     * 获取一个月数据
     *
     * @param list      图表的原始数据
     * @param nameKey   原始数据中 用来作为 y轴 name 的字段名称
     * @param dateKey   原始数据中 用来作为 y轴 匹配X轴时间 的字段名称
     * @param numberKey 原始数据中 用来作为 y轴 数值 的字段名称
     */
    public static HashMap<String, Object> getDataOnMonth(List<HashMap<String, Object>> list, String nameKey,
                                                         String dateKey, String numberKey) {
        ArrayList<String> times = DateUtils.getPastDateList(30);
        return getData(times, list, nameKey, dateKey, numberKey);
    }

    /**
     * 获取一周的数据
     *
     * @param list      图表的原始数据
     * @param nameKey   原始数据中 用来作为 y轴 name 的字段名称
     * @param dateKey   原始数据中 用来作为 y轴 匹配X轴时间 的字段名称
     * @param numberKey 原始数据中 用来作为 y轴 数值 的字段名称
     */
    public static HashMap<String, Object> getDataOnWeek(List<HashMap<String, Object>> list, String nameKey,
                                                        String dateKey, String numberKey) {
        ArrayList<String> times = DateUtils.getPastDateList(7);
        return getData(times, list, nameKey, dateKey, numberKey);
    }

    /**
     * 获取指定日期的数据
     *
     * @param times     图标X轴显示的日期title
     * @param list      图表的原始数据
     * @param nameKey   原始数据中 用来作为 y轴 name 的字段名称
     * @param dateKey   原始数据中 用来作为 y轴 匹配X轴时间 的字段名称
     * @param numberKey 原始数据中 用来作为 y轴 数值 的字段名称
     */
    private static HashMap<String, Object> getData(List<String> times, List<HashMap<String, Object>> list,
                                                   String nameKey, String dateKey, String numberKey) {

        Map<String, Integer> timesMap = Maps.newHashMap();
        for (int i = 0; i < times.size(); i++) {
            timesMap.put(times.get(i), i);
        }

        nameKey = StringUtils.isEmpty(nameKey) ? "name" : nameKey;
        dateKey = StringUtils.isEmpty(dateKey) ? "date" : dateKey;
        numberKey = StringUtils.isEmpty(numberKey) ? "number" : numberKey;

        // 获取 name : date-number list
        HashMap<String, long[]> datasMap = Maps.newHashMap();
        for (Map map : list) {
            // 取得 对应时间的记录
            String name = (String) map.get(nameKey);
            String date = (String) map.get(dateKey);
            Object key = map.get(numberKey);
            Long number = Long.parseLong(key.toString());

            // 匹配赋值
            datasMap.putIfAbsent(name, new long[times.size()]);
            long[] dateNumbers = datasMap.get(name);
            Integer index = timesMap.get(date);
            if (null != index && null != number) {
                dateNumbers[index] = number;
            }
        }

        // 转为 对象结构
        ArrayList<Object> datas = new ArrayList<>();
        for (String name : datasMap.keySet()) {
            HashMap temp = new HashMap<>();
            temp.put("title", name);
            temp.put("values", datasMap.get(name));
            datas.add(temp);
        }

        HashMap<String, Object> result = Maps.newHashMap();
        result.put("times", times);
        result.put("datas", datas);
        return result;
    }
}
