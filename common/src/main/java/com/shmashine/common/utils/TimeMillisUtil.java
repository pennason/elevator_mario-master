package com.shmashine.common.utils;


import java.sql.Timestamp;
import java.util.Date;

/**
 * 时间戳处理工具类 - 雄迈摄像头使用
 *
 * @author little.li
 */
public class TimeMillisUtil {


    /**
     * 计数器
     */
    private static long counter = 0L;


    /**
     * 获取计数器
     *
     * @return 计数器
     */
    private static synchronized String getCounter() {
        ++counter;
        if (counter < 10L) {
            return "000000" + counter;
        } else if (counter < 100L) {
            return "00000" + counter;
        } else if (counter < 1000L) {
            return "0000" + counter;
        } else if (counter < 10000L) {
            return "000" + counter;
        } else if (counter < 100000L) {
            return "00" + counter;
        } else if (counter < 1000000L) {
            return "0" + counter;
        } else if (counter < 10000000L) {
            return String.valueOf(counter);
        } else {
            counter = 1L;
            return "000000" + counter;
        }
    }


    /**
     * 获取组合时间戳
     *
     * @return 组合后的时间戳
     */
    public static String getTimeMillis() {
        long timeMillis = System.currentTimeMillis();
        return getCounter() + timeMillis;
    }

    /**
     * 时间戳返回日期
     *
     * @param stamp 时间错
     * @return 日期
     */
    public static Date exchangeStampToDate(Long stamp) {
        Timestamp timestamp = new Timestamp(stamp);
        return new Date(timestamp.getTime());
    }

    /**
     * 日期返回时间戳
     *
     * @param date 日期
     * @return 结果
     */
    public static Long exchangeDateToStamp(Date date) {
        if (date != null) {
            return date.getTime();
        } else {
            return System.currentTimeMillis();
        }
    }
}