package com.crm.utils;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author crm
 * localdateTime
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式(yyyyMMdd)
     */
    public final static String DATE_NUMBER_PATTERN = "yyyyMMdd";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(LocalDateTime date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(LocalDateTime date, String pattern) {
        if (date != null) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 日期解析
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回Date
     */
    public static LocalDateTime parse(String date, String pattern) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
    }


    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param temporal 日期
     * @param pattern  格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(TemporalAccessor temporal, String pattern) {
        if (temporal != null) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            return df.format(temporal);
        }
        return null;
    }

    /**
     * 获取当前时间小时数组
     *
     * @param timeList
     * @return
     */
    public static List<String> getHourData(List<String> timeList) {
        //            获取当前时间
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i <= now.getHour(); i++) {
            timeList.add(String.format("%02d:00", i));
        }
        return timeList;
    }

    /**
     * 获取两个日期之间的所有日期
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getDatesInRange(String startDate, String endDate) {
        List<String> dates = new ArrayList<>();
        // 计算两个日期之间的天数
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        long daysBetween = ChronoUnit.DAYS.between(LocalDateTime.parse(startDate, formatter), LocalDateTime.parse(endDate, formatter));

        // 遍历每一天并添加到列表中
        for (int i = 0; i <= daysBetween; i++) {
            LocalDateTime date = LocalDateTime.parse(startDate, formatter).plusDays(i);
            DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = date.format(customFormatter);
            dates.add(formattedDate);
        }
        return dates;
    }


    public static List<String> getMonthInRange(String startDate, String endDate) {
        List<String> monthList = new ArrayList<>();
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        long monthsBetween = ChronoUnit.MONTHS.between(LocalDateTime.parse(startDate, formatter), LocalDateTime.parse(endDate, formatter));
        for (int i = 0; i <= monthsBetween; i++) {
            LocalDateTime currentDate = LocalDateTime.parse(startDate, formatter).plusMonths(i);
            String formattedDate = currentDate.format(customFormatter);
            monthList.add(formattedDate);
        }
        return monthList;
    }


    public static List<String> getYearInRange(String startDate, String endDate) {
        List<String> yearList = new ArrayList<>();
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        long yearsBetween = ChronoUnit.YEARS.between(LocalDateTime.parse(startDate, formatter), LocalDateTime.parse(endDate, formatter));
        for (int i = 0; i <= yearsBetween; i++) {
            LocalDateTime currentDate = LocalDateTime.parse(startDate, formatter).plusYears(i);
            String formattedDate = currentDate.format(customFormatter);
            yearList.add(formattedDate);
        }
        return yearList;
    }


    public static List<String> getWeekInRange(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);

        // 使用中国地区规则（周一为一周的开始）
        WeekFields weekFields = WeekFields.of(Locale.CHINA);

        int startWeek = startDateTime.get(weekFields.weekOfYear());
        int endWeek = endDateTime.get(weekFields.weekOfYear());
        List<String> weekList = new ArrayList<>();
        for (int i = startWeek; i <= endWeek; i++) {
            weekList.add(String.valueOf(i));
        }

        return weekList;
    }
}