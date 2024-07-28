/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author Admin
 */
public class XDate {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // Method to convert String to Date with default format
    public static Date toDate(String dateStr) {
        return toDate(dateStr, DEFAULT_DATE_FORMAT);
    }

    // Method to convert String to Date with custom format
    public static Date toDate(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to convert Date to String with default format
    public static String toString(Date date) {
        return toString(date, DEFAULT_DATE_FORMAT);
    }

    // Method to convert Date to String with custom format
    public static String toString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    // Method to get the current date and time
    public static Date now() {
        return new Date();
    }

    // Method to add days to a Date
    public static Date addDays(Date date, int days) {
        long millis = date.getTime();
        millis += days * 24 * 60 * 60 * 1000L;
        return new Date(millis);
    }

    // Method to subtract days from a Date
    public static Date subtractDays(Date date, int days) {
        return addDays(date, -days);
    }

    // Method to calculate the difference in hours between two dates
    public static BigDecimal getDifferenceInHours(Date date1, Date date2) {
        long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
        BigDecimal diffInHours = BigDecimal.valueOf(diffInMillies).divide(BigDecimal.valueOf(1000 * 60 * 60), 2, BigDecimal.ROUND_HALF_UP);
        return diffInHours;
    }
}
