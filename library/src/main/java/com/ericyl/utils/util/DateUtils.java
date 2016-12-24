package com.ericyl.utils.util;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * G 年代标志符
 * y 年
 * M 月
 * d 日
 * h 时 在上午或下午 (1~12)
 * H 时 在一天中 (0~23)
 * m 分
 * s 秒
 * S 毫秒
 * E 星期
 * D 一年中的第几天
 * F 一月中第几个星期几
 * w 一年中第几个星期
 * W 一月中第几个星期
 * a 上午 / 下午 标记符
 * k 时 在一天中 (1~24)
 * K 时 在上午或下午 (0~11)
 * Z 时区
 */
public class DateUtils {

    public static final String DATE_YMD_OTHER = "yyyyMMdd";
    public static final String DATE_DEFAULT = "yyyy-MM-dd";
    public static final String DATE_YMD = "yyyy/MM/dd";
    public static final String DATE_MDY = "MM/dd/yyyy";

    public static final String TIME_DEFAULT_12 = "hh:mm:ss";
    public static final String TIME_DEFAULT_24 = "HH:mm:ss";

    public static final String DATETIME_YMD_OTHER = "yyyyMMddHHmmss";
    public static final String DATETIME_DEFAULT_12 = "yyyy-MM-dd hh:mm:ss a";
    public static final String DATETIME_DEFAULT_24 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_YMD_12 = "yyyy/MM/dd hh:mm:ss a";
    public static final String DATETIME_YMD_24 = "yyyy/MM/dd HH:mm:ss";
    public static final String DATETIME_MDY_12 = "MM/dd/yyyy hh:mm:ss a";
    public static final String DATETIME_MDY_24 = "MM/dd/yyyy HH:mm:ss";

    public static final String DATETIME_ALL_OTHER = "yyyyMMddHHmmssSSS";
    public static final String DATETIME_ALL = "yyyy-MM-dd HH:mm:ss.SSSZ E";

    @SuppressLint("SimpleDateFormat")
    public static String getString(@NonNull Date date, @NonNull String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static Date getDate(@NonNull String dateTime, @NonNull String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
