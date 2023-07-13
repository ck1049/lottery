package com.reward.lottery.utils;

import com.reward.lottery.common.enumeration.Week;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {


    /**
     * 解析日期字符串
     * @param date
     * @param pattern
     * @return
     */
    public static Date parse(String date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 日期格式统一修改成yyyy-MM-dd
     * @param date
     * @return
     */
    public static String formatDateString(String date){
        if (date.contains("-") && date.length() >= 10){
            return date;
        }
        return format(parse(date, "yyyyMMdd"), "yyyy-MM-dd");
    }

    /**
     * 根据日期获取星期几枚举
     * @param date
     * @return
     */
    public static Week week(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return Week.getByCode(calendar.get(Calendar.DAY_OF_WEEK) - 1);
    }

    public static void main(String[] args) {
        System.out.println(week(new Date()));
    }
}
