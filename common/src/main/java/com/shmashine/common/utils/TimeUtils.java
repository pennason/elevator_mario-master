/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */

package com.shmashine.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间计算工具类
 */
public class TimeUtils {

    private static final String FORMAT_SEC_FULL = "yyyy-MM-dd HH:mm:ss";

    public static String toTimeString(long time) {
        TimeUtils t = new TimeUtils(time);
        int day = t.get(TimeUtils.DAY);
        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append("天");
        }
        int hour = t.get(TimeUtils.HOUR);
        if (hour > 0) {
            sb.append(hour).append("时");
        }
        int minute = t.get(TimeUtils.MINUTE);
        if (minute > 0) {
            sb.append(minute).append("分");
        }
        int second = t.get(TimeUtils.SECOND);
        if (second > 0) {
            sb.append(second).append("秒");
        }
        return sb.toString();
    }

    /**
     * 时间字段常量，表示“秒”
     */
    public static final int SECOND = 0;

    /**
     * 时间字段常量，表示“分”
     */
    public static final int MINUTE = 1;

    /**
     * 时间字段常量，表示“时”
     */
    public static final int HOUR = 2;

    /**
     * 时间字段常量，表示“天”
     */
    public static final int DAY = 3;

    /**
     * 各常量允许的最大值
     */
    private final int[] maxFields = {59, 59, 23, Integer.MAX_VALUE - 1};

    /**
     * 各常量允许的最小值
     */
    private final int[] minFields = {0, 0, 0, Integer.MIN_VALUE};

    /**
     * 默认的字符串格式时间分隔符
     */
    private String timeSeparator = ":";

    /**
     * 时间数据容器
     */
    private int[] fields = new int[4];

    /**
     * 无参构造，将各字段置为 0
     */
    public TimeUtils() {
        this(0, 0, 0, 0);
    }

    /**
     * 使用时、分构造一个时间
     *
     * @param hour   小时
     * @param minute 分钟
     */
    public TimeUtils(int hour, int minute) {
        this(0, hour, minute, 0);
    }

    /**
     * 使用时、分、秒构造一个时间
     *
     * @param hour   小时
     * @param minute 分钟
     * @param second 秒
     */
    public TimeUtils(int hour, int minute, int second) {
        this(0, hour, minute, second);
    }

    /**
     * 使用一个字符串构造时间<br/>
     * Time time = new Time("14:22:23");
     *
     * @param time 字符串格式的时间，默认采用“:”作为分隔符
     */
    public TimeUtils(String time) {
        this(time, null);
    }

    /**
     * 使用时间毫秒构建时间
     *
     * @param time 日期
     */
    public TimeUtils(long time) {
        this(new Date(time));
    }

    /**
     * 使用日期对象构造时间
     *
     * @param date 日期
     */
    public TimeUtils(Date date) {
        this(DateFormatUtils.formatUTC(date, "HH:mm:ss"));
    }

    /**
     * 使用天、时、分、秒构造时间，进行全字符的构造
     *
     * @param day    天
     * @param hour   时
     * @param minute 分
     * @param second 秒
     */
    public TimeUtils(int day, int hour, int minute, int second) {
        initialize(day, hour, minute, second);
    }

    /**
     * 使用一个字符串构造时间，指定分隔符<br/>
     * Time time = new Time("14-22-23", "-");
     *
     * @param time 字符串格式的时间
     */
    public TimeUtils(String time, String timeSeparator) {
        if (timeSeparator != null) {
            setTimeSeparator(timeSeparator);
        }
        parseTime(time);
    }

    /**
     * 格式化时间转date对象
     */
    public static Date stringToDate(String nowTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return fmt.parse(nowTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateFormat(Date date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(date);
    }


    /**
     * 设置时间字段的值
     *
     * @param field 时间字段常量
     * @param value 时间字段的值
     */
    public void set(int field, int value) {
        if (value < minFields[field]) {
            throw new IllegalArgumentException(value + ", time value must be positive.");
        }
        fields[field] = value % (maxFields[field] + 1);
        // 进行进位计算
        int carry = value / (maxFields[field] + 1);
        if (carry > 0) {
            int upFieldValue = get(field + 1);
            set(field + 1, upFieldValue + carry);
        }
    }

    /**
     * 获得时间字段的值
     *
     * @param field 时间字段常量
     * @return 该时间字段的值
     */
    public int get(int field) {
        if (field < 0 || field > fields.length - 1) {
            throw new IllegalArgumentException(field + ", field value is error.");
        }
        return fields[field];
    }

    /**
     * 将时间进行“加”运算，即加上一个时间
     *
     * @param time 需要加的时间
     * @return 运算后的时间
     */
    public TimeUtils addTime(TimeUtils time) {
        TimeUtils result = new TimeUtils();
        int up = 0;     // 进位标志
        for (int i = 0; i < fields.length; i++) {
            int sum = fields[i] + time.fields[i] + up;
            up = sum / (maxFields[i] + 1);
            result.fields[i] = sum % (maxFields[i] + 1);
        }
        return result;
    }

    /**
     * 将时间进行“减”运算，即减去一个时间
     *
     * @param time 需要减的时间
     * @return 运算后的时间
     */
    public TimeUtils subtractTime(TimeUtils time) {
        TimeUtils result = new TimeUtils();
        int down = 0;       // 退位标志
        for (int i = 0, k = fields.length - 1; i < k; i++) {
            int difference = fields[i] + down;
            if (difference >= time.fields[i]) {
                difference -= time.fields[i];
                down = 0;
            } else {
                difference += maxFields[i] + 1 - time.fields[i];
                down = -1;
            }
            result.fields[i] = difference;
        }
        result.fields[DAY] = fields[DAY] - time.fields[DAY] + down;
        return result;
    }

    /**
     * 获得时间字段的分隔符
     *
     * @return 结果
     */
    public String getTimeSeparator() {
        return timeSeparator;
    }

    /**
     * 设置时间字段的分隔符（用于字符串格式的时间）
     *
     * @param timeSeparator 分隔符字符串
     */
    public void setTimeSeparator(String timeSeparator) {
        this.timeSeparator = timeSeparator;
    }

    private void initialize(int day, int hour, int minute, int second) {
        set(DAY, day);
        set(HOUR, hour);
        set(MINUTE, minute);
        set(SECOND, second);
    }

    private void parseTime(String time) {
        if (time == null) {
            initialize(0, 0, 0, 0);
            return;
        }
        String t = time;
        int field = DAY;
        set(field--, 0);
        int p = -1;
        while ((p = t.indexOf(timeSeparator)) > -1) {
            parseTimeField(time, t.substring(0, p), field--);
            t = t.substring(p + timeSeparator.length());
        }
        parseTimeField(time, t, field--);
    }

    private void parseTimeField(String time, String t, int field) {
        if (field < SECOND || t.length() < 1) {
            parseTimeException(time);
        }
        char[] chs = t.toCharArray();
        int n = 0;
        for (int i = 0; i < chs.length; i++) {
            if (chs[i] <= ' ') {
                continue;
            }
            if (chs[i] >= '0' && chs[i] <= '9') {
                n = n * 10 + chs[i] - '0';
                continue;
            }
            parseTimeException(time);
        }
        set(field, n);
    }

    private void parseTimeException(String time) {
        throw new IllegalArgumentException(time + ", time format error, HH"
                + this.timeSeparator + "mm" + this.timeSeparator + "ss");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(16);
        sb.append(fields[DAY]).append(',').append(' ');
        buildString(sb, HOUR).append(timeSeparator);
        buildString(sb, MINUTE).append(timeSeparator);
        buildString(sb, SECOND);
        return sb.toString();
    }

    private StringBuilder buildString(StringBuilder sb, int field) {
        if (fields[field] < 10) {
            sb.append('0');
        }
        return sb.append(fields[field]);
    }


    /**
     * 时间转10位时间戳
     *
     * @param time 时间
     * @return 时间戳
     */
    public static long dateToTimestamp(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_SEC_FULL);
        try {
            Date date = simpleDateFormat.parse(time);
            return date.getTime() / 1000;
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 获取当前时间
     *
     * @return 时间
     */
    public static String nowTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(currentTime);
    }

    /**
     * 获取当前时间
     *
     * @return 时间
     */
    public static String nowDate() {
        // 获取当前时间
        Date currentTime = new Date();
        // 格式化时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(currentTime);
    }

    /**
     * 获取当前时间的10位时间戳
     *
     * @return 结果
     */
    public static long getTenBitTimestamp() {
        String s = nowTime();
        return dateToTimestamp(s);
    }


    /**
     * UnixTimeStamp 秒级时间戳转换为date，单位为秒
     * NB设备上传的时间戳为12小时制，需根据当前时间将其转为24小时制
     *
     * @param timestampString 转换前的时间戳
     * @return 转换完成的时间
     */
    public static Date timeStamp2Date(String timestampString) {
        long timestamp;
        // 支持秒级和毫秒级时间戳
        if (timestampString.length() == 10) {
            timestamp = Long.parseLong(timestampString) * 1000;
        } else {
            timestamp = Long.parseLong(timestampString);
        }

        return new Date(timestamp);
    }


    /**
     * 计算时间差，以秒为单位
     *
     * @param minDate 最小日期
     * @param maxDate 最大日期
     * @return 相差的秒数
     */
    public static long getDistanceTime(Date minDate, Date maxDate) {
        long second = 0;
        long time1 = minDate.getTime();
        long time2 = maxDate.getTime();

        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        second = diff / 1000;
        return second;
    }

    /**
     * 小时减去8，获取格林威治时间
     *
     * @param hour 源小时，如24、13
     * @return 处理后结果，如16、5
     */
    public static String hourSubtractEight(String hour) {
        int value = Integer.parseInt(hour);
        int result = 0;
        if (value >= 8) {
            result = value - 8;
        } else {
            result = 24 - (8 - value);
        }
        return String.valueOf(result);
    }


    /**
     * 小时加上8，获取格林威治对应的北京时间
     *
     * @param hour 源小时，如20、05
     * @return 处理后结果，如04、13
     */
    public static String hourAddEight(String hour) {
        int value = Integer.parseInt(hour);
        int result = 0;
        if (value < 16) {
            result = value + 8;
        } else {
            result = (8 + value) - 24;
        }
        return String.valueOf(result);
    }


}