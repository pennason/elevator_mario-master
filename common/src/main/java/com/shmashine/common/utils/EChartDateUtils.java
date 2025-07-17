package com.shmashine.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * echart图表统计  年/月/周 统计工具类
 */
public class EChartDateUtils {


    /**
     * 获取年
     *
     * @param list      图表的原始数据
     * @param nameKey   原始数据中 用来作为 y轴 name 的字段名称
     * @param dateKey   原始数据中 用来作为 y轴 匹配X轴时间 的字段名称
     * @param numberKey 原始数据中 用来作为 y轴 数值 的字段名称
     * @return 结果
     */
    public static Map<String, Object> getDataOnYear(List<Map> list, String nameKey,
                                                    String dateKey, String numberKey) {
        ArrayList<String> times = DateUtils.getPastMonthList(12);
        return getData(times, list, nameKey, dateKey, numberKey);
    }

    /**
     * 获取月
     *
     * @param list      图表的原始数据
     * @param nameKey   原始数据中 用来作为 y轴 name 的字段名称
     * @param dateKey   原始数据中 用来作为 y轴 匹配X轴时间 的字段名称
     * @param numberKey 原始数据中 用来作为 y轴 数值 的字段名称
     * @return 结果
     */
    public static Map<String, Object> getDataOnMonth(List<Map> list, String nameKey,
                                                     String dateKey, String numberKey) {
        ArrayList<String> times = DateUtils.getPastDateList(30);
        return getData(times, list, nameKey, dateKey, numberKey);
    }

    /**
     * 获取周
     *
     * @param list      图表的原始数据
     * @param nameKey   原始数据中 用来作为 y轴 name 的字段名称
     * @param dateKey   原始数据中 用来作为 y轴 匹配X轴时间 的字段名称
     * @param numberKey 原始数据中 用来作为 y轴 数值 的字段名称
     * @return 结果
     */
    public static Map<String, Object> getDataOnWeek(List<Map> list, String nameKey,
                                                    String dateKey, String numberKey) {
        ArrayList<String> times = DateUtils.getPastDateList(7);
        return getData(times, list, nameKey, dateKey, numberKey);
    }

    /**
     * 获取日期
     *
     * @param times     图标X轴显示的日期title
     * @param list      图表的原始数据
     * @param nameKey   原始数据中 用来作为 y轴 name 的字段名称
     * @param dateKey   原始数据中 用来作为 y轴 匹配X轴时间 的字段名称
     * @param numberKey 原始数据中 用来作为 y轴 数值 的字段名称
     * @return 结果
     */
    private static Map<String, Object> getData(List<String> times, List<Map> list, String nameKey,
                                               String dateKey, String numberKey) {

        Map<String, Integer> timesMap = Maps.newHashMap();
        for (int i = 0; i < times.size(); i++) {
            timesMap.put(times.get(i), i);
        }

        nameKey = !StringUtils.hasText(nameKey) ? "name" : nameKey;
        dateKey = !StringUtils.hasText(dateKey) ? "date" : dateKey;
        numberKey = !StringUtils.hasText(numberKey) ? "number" : numberKey;

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

        Map<String, Object> result = Maps.newHashMap();
        result.put("times", times);
        result.put("datas", datas);
        return result;
    }

    public static void main(String[] args) {
        HashMap temp = Maps.newHashMap();
        temp.put("i_fault_type", "09");
        temp.put("date", "2020-11-11");
        temp.put("number", 1);
        ArrayList<Map> list = Lists.newArrayList();
        list.add(temp);

        HashMap temp2 = Maps.newHashMap();
        temp2.put("i_fault_type", "08");
        temp2.put("date", "2020-11-10");
        temp2.put("number", 3);
        list.add(temp2);

        HashMap temp3 = Maps.newHashMap();
        temp3.put("i_fault_type", "09");
        temp3.put("date", "2020-11-1");
        temp3.put("number", 4);
        list.add(temp3);
        Map<String, Object> result = getDataOnMonth(list, "i_fault_type", null, null);

        System.out.println(JSON.toJSONString(result));
    }
}
