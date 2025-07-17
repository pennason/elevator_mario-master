package com.shmashine.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;

import org.springframework.util.StringUtils;


/**
 * 日期处理工具类
 */
public class DateUtils {

    /**
     * 时间格式(yyyy-MM-dd)
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 一天的秒数
     */
    public static final long SECOND_PER_DAY = 86400L;

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    public static String formatWithTime(Date date) {
        return format(date, DATE_TIME_PATTERN);
    }


    public static Date simpleFormatDate(String date) {
        if (StringUtils.hasText(date)) {
            SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
            try {
                return df.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String formatDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String result = format.format(new Date());
        return result;
    }

    public static Date formatDate(String date) {
        if (StringUtils.hasText(date)) {
            SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
            try {
                return df.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 计算距离现在多久，非精确
     *
     * @param date 日期
     * @return 结果
     */
    public static String getTimeBefore(Date date) {
        Date now = new Date();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String r = "";
        if (day > 0) {
            r += day + "天";
        } else if (hour > 0) {
            r += hour + "小时";
        } else if (min > 0) {
            r += min + "分";
        } else if (s > 0) {
            r += s + "秒";
        }
        r += "前";
        return r;
    }


    /**
     * 计算距离现在多久，精确
     *
     * @param date 日期
     * @return 结果
     */
    public static String getTimeBeforeAccurate(Date date) {
        Date now = new Date();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        String r = "";
        if (day > 0) {
            r += day + "天";
        }
        long hour = (l / (60 * 60 * 1000) - day * 24);
        if (hour > 0) {
            r += hour + "小时";
        }
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        if (min > 0) {
            r += min + "分";
        }
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (s > 0) {
            r += s + "秒";
        }
        r += "前";
        return r;
    }


    /**
     * 根据时间范围，获取日期列表
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 日期列表
     */
    public static List<String> getDatesBetweenTwoDate(String beginDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> resultDateList = new ArrayList<>();
        //把开始时间加入集合
        resultDateList.add(beginDate);
        Calendar cal = Calendar.getInstance();
        //使用给定的 Date 设置此 Calendar 的时间
        try {
            cal.setTime(sdf.parse(beginDate));
            while (true) {
                //根据日历的规则，为给定的日历字段添加或减去指定的时间量
                cal.add(Calendar.DAY_OF_MONTH, 1);
                // 测试此日期是否在指定日期之后
                if (sdf.parse(endDate).after(cal.getTime())) {
                    resultDateList.add(sdf.format(cal.getTime()));
                } else {
                    break;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (resultDateList.size() > 0 && resultDateList.get(resultDateList.size() - 1).equals(endDate)) {
            return resultDateList;
        }
        //把结束时间加入集合
        resultDateList.add(endDate);
        return resultDateList;
    }

    /**
     * 将指定的日期字符串转换成日期
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return 日期对象
     */
    public static Date parseDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("日期转化错误");
        }

        return date;
    }

    /**
     * 将指定的日期格式化成指定的日期字符串
     *
     * @param date    日期对象
     * @param pattern 格式
     * @return 格式化后的日期字符串
     */
    public static String dateFormate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateStr;
        if (date == null) {
            return "";
        }
        dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * 查询指定日期前后指定的天数
     *
     * @param date 日期对象
     * @param days 天数
     * @return 日期对象
     */
    public static Date incr(Date date, int days) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 将LocalDate日期转化成Date
     *
     * @param localDate LocalDate对象
     * @return Date对象
     */
    public static Date localDateToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
        Date date = Date.from(zonedDateTime.toInstant());

        return date;
    }

    /**
     * 将Date转成LocalDate对象
     *
     * @param date Date对象
     * @return LocalDate对象
     */
    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();

        return localDate;
    }

    /**
     * 获取某一时间段特定星期几的日期
     *
     * @param dateFrom 开始时间
     * @param dateEnd  结束时间
     * @param weekDays 星期
     * @return 返回时间数组
     */
    public static List<String> getDates(String dateFrom, String dateEnd, String weekDays) {
        long time = 1L;
        long perDayMilSec = 24 * 60 * 60 * 1000;
        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //需要查询的星期系数  
        String strWeekNumber = weekForNum(weekDays);
        try {
            dateFrom = sdf.format(sdf.parse(dateFrom).getTime() - perDayMilSec);
            while (true) {
                time = sdf.parse(dateFrom).getTime();
                time = time + perDayMilSec;
                Date date = new Date(time);
                dateFrom = sdf.format(date);
                if (dateFrom.compareTo(dateEnd) <= 0) {
                    //查询的某一时间的星期系数
                    Integer weekDay = dayForWeek(date);
                    //判断当期日期的星期系数是否是需要查询的
                    if (strWeekNumber.indexOf(weekDay.toString()) != -1) {
                        dateList.add(dateFrom);
                    }
                } else {
                    break;
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }  
        /*String[] dateArray = new String[dateList.size()];  
        dateList.toArray(dateArray); */
        return dateList;
    }

    //等到当期时间的周系数。星期日：1，星期一：2，星期二：3，星期三：4，星期四：5，星期五：6，星期六：7
    public static Integer dayForWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 得到对应星期的系数  星期日：1，星期一：2，星期二：3，星期三：4，星期四：5，星期五：6，星期六：7
     *
     * @param weekDays 星期格式  星期一|星期二
     */
    public static String weekForNum(String weekDays) {
        //返回结果为组合的星期系数  
        StringBuilder weekNumber = new StringBuilder();
        //解析传入的星期  
        if (weekDays.contains("|")) {
            //多个星期数
            String[] strWeeks = weekDays.split("\\|");
            for (String strWeek : strWeeks) {
                weekNumber.append(getWeekNum(strWeek).toString());
            }
        } else {
            // 一个星期数
            weekNumber = new StringBuilder(getWeekNum(weekDays).toString());
        }

        return weekNumber.toString();

    }

    //将星期转换为对应的系数  星期日：1，星期一：2，星期二：3，星期三：4，星期四：5，星期五：6，星期六：7  
    public static Integer getWeekNum(String strWeek) {
        //默认为星期日
        int number = 1;
        if ("星期一".equals(strWeek)) {
            number = 2;
        } else if ("星期二".equals(strWeek)) {
            number = 3;
        } else if ("星期三".equals(strWeek)) {
            number = 4;
        } else if ("星期四".equals(strWeek)) {
            number = 5;
        } else if ("星期五".equals(strWeek)) {
            number = 6;
        } else if ("星期六".equals(strWeek)) {
            number = 7;
        }
        return number;
    }

    /**
     * 获取当前时间
     *
     * @param sdf 格式
     */
    public static String getStringDate(String sdf) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sdf);
        return formatter.format(currentTime);
    }

    public static String getTimeBySet(Date date1, String intervalType, int timeInteral) {
        String r;
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        c.add(Calendar.DAY_OF_MONTH, timeInteral);
        Date retDate = c.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.DATE_TIME_PATTERN);
        r = formatter.format(retDate);
        return r;
    }

    /**
     * 获取过去任意天内的日期数组
     *
     * @param intervals intervals天内
     * @return 日期数组 [2019-09-14, 2019-09-15, 2019-09-16, 2019-09-17, 2019-09-18, 2019-09-19, 2019-09-20]
     */
    public static ArrayList<String> getPastDateList(int intervals) {
        ArrayList<String> pastDaysList = new ArrayList<>();
        for (int i = intervals - 1; i >= 0; i--) {
            pastDaysList.add(getPastDate(i));
        }
        return pastDaysList;
    }

    /**
     * 获取过去任意天内的月份数组
     *
     * @param intervals intervals月内
     * @return 月份数组 [2019-09, 2019-08]
     */
    public static ArrayList<String> getPastMonthList(int intervals) {
        ArrayList<String> pastMonthsList = new ArrayList<>();
        for (int i = intervals - 1; i >= 0; i--) {
            pastMonthsList.add(getPastMonth(i));
        }
        return pastMonthsList;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past 天数
     * @return 结果
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    /**
     * 获取过去第几月的月份
     *
     * @param past 月份数
     * @return 结果
     */
    public static String getPastMonth(int past) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -past);
        Date m = c.getTime();
        return sdf.format(m);
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param past 天数
     * @return 结果
     */
    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    /**
     * 获取后 几分钟的日期
     *
     * @param minute 分钟数
     * @return 结果
     */
    public static String getMinuteDate(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + minute);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
        String result = format.format(today);
        return result;
    }

    /**
     * 指定时间增加指定小时
     *
     * @param day  指定时间
     * @param hour 指定小时
     * @return 处理后的时间
     */
    public static String dateAddHour(String day, int hour) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        date = cal.getTime();
        return format.format(date);
    }

    /**
     * 指定时间减少指定小时
     *
     * @param day  指定时间
     * @param hour 指定小时
     * @return 处理后的时间
     */
    public static String dateSubtractHour(String day, int hour) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, -hour);
        date = cal.getTime();
        return format.format(date);
    }


    /**
     * 指定时间减少指定小时
     *
     * @param date 指定时间
     * @param hour 指定小时
     * @return 处理后的时间
     */
    public static Date dateSubtractHour(Date date, int hour) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, -hour);
        date = cal.getTime();
        try {
            date = sf.parse(sf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * mongo 日期查询isodate
     *
     * @param dateStr 日期
     * @return 结果
     */
    public static Date dateToISODate(String dateStr) {
        //T代表后面跟着时间，Z代表UTC统一时间
        Date date = formatD(dateStr);
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
        String isoDate = format.format(date);
        try {
            return format.parse(isoDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Date formatD(String dateStr) {
        return formatD(dateStr, DATE_TIME_PATTERN);
    }

    public static Date formatD(String dateStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date ret = null;
        try {
            ret = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            //
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 两个日期相减得出相差天数
     */
    public static int getDiscrepantDays(Date dateStart, Date dateEnd) {
        if (null == dateStart || null == dateEnd) {
            return 0;
        }
        return (int) ((dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24);
    }


}
