package com.mobileshop.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date utility class for formatting dates.
 */
public class DateUtil {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Formats a Date to a string in yyyy-MM-dd format.
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    /**
     * Formats a Date to a string in yyyy-MM-dd HH:mm:ss format.
     */
    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(DATETIME_FORMAT).format(date);
    }

    /**
     * Formats a timestamp (long) to a string in yyyy-MM-dd HH:mm:ss format.
     */
    public static String formatDateTime(long timestamp) {
        return formatDateTime(new Date(timestamp));
    }
}
