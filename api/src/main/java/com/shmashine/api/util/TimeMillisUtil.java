package com.shmashine.api.util;


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


}