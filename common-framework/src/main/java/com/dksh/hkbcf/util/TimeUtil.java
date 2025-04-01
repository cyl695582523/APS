package com.dksh.hkbcf.util;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeUtil {

    public static Instant parse(String dateTimeText, String formatText) {
        return LocalDateTime.parse(dateTimeText, DateTimeFormatter.ofPattern(formatText, Locale.US)).atZone(ZoneId.systemDefault()).toInstant();
    }

    public static Date parseDate(String dateTimeText, String formatText) {
        try {
            return new Date(new SimpleDateFormat(formatText).parse(dateTimeText).getTime());
        } catch (ParseException e) {
            e.getStackTrace();
            return null;
        }
    }

    public static Time parseTime(String dateTimeText, String formatText) {
        try {
            return new Time(new SimpleDateFormat(formatText).parse(dateTimeText).getTime());
        } catch (ParseException e) {
            e.getStackTrace();
            return null;
        }
    }

    public static Long parseMilli(String dateTimeText, String formatText) {
        return parse(dateTimeText, formatText).toEpochMilli();
    }

    public static String format(Date date, String formatText) {
        return new SimpleDateFormat(formatText).format(date);
    }

    public static String format(Time time, String formatText) {
        return new SimpleDateFormat(formatText).format(time);
    }

    public static String format(Instant instant, String formatText) {
        return DateTimeFormatter.ofPattern(formatText).withZone(ZoneId.systemDefault()).format(instant);
    }

    public static String formatMilli(Long milliseconds, String formatText) {
        return DateTimeFormatter.ofPattern(formatText).withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(milliseconds));
    }

    public static Instant toInstant(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time).atZone(ZoneId.systemDefault()).toInstant();
    }
}
