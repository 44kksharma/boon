package org.boon.utils;

import java.util.Calendar;
import java.util.TimeZone;

public class Dates {

    private static TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
    private static volatile long lastNow;
    private static long MILLI_SECOND = 1;
    private static long SECOND = MILLI_SECOND * 1000;
    private static long MINUTE = 60 * SECOND;
    private static long HOUR = 60 * MINUTE;
    private static long DAY = 24 * HOUR;
    private static long WEEK = 7 * DAY;
    private static long MONTH = (long) (30.4167 * DAY);
    private static long YEAR = (long) (365.2425 * DAY);


    public static long utcNow() {
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.setTimeZone(UTC_TIME_ZONE);
        long utcNow = calendar.getTime().getTime();
        lastNow = now;
        return utcNow;
    }

    /**
     * For testing only, avoids potential timing issue.
     */
    static long lastNow() {
        return lastNow;
    }


    public static long fromUtcTimeToTimeZone(long utcTime, TimeZone timeZone) {

        Calendar calendar = Calendar.getInstance(UTC_TIME_ZONE);
        calendar.setTimeInMillis(utcTime);
        calendar.setTimeZone(timeZone);
        return calendar.getTime().getTime();
    }


    public static boolean before(long isThis, long beforeThis) {
        return isThis < beforeThis;
    }


    public static boolean after(long isThis, long afterThis) {
        return isThis > afterThis;
    }


    public static long secondsFrom(long time, int seconds) {
        return time + (seconds * SECOND);
    }

    public static long minutesFrom(long time, int minutes) {
        return time + (minutes * MINUTE);
    }

    public static long hoursFrom(long time, int hours) {
        return time + (hours * HOUR);
    }

    public static long daysFrom(long time, int days) {
        return time + (days * DAY);
    }

    public static long weeksFrom(long time, int weeks) {
        return time + (weeks * WEEK);
    }

    public static long monthsFrom(long time, int months) {
        return time + (months * MONTH);
    }

    public static long yearsFrom(long time, int years) {
        return time + (years * YEAR);
    }

    public static long utcDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();

        /* Set to midnight. */
        midnight(calendar);

        /* This might change the date, but when you convert it
        back to the clocktime timezone, it will be correct.
         */
        calendar.setTimeZone(UTC_TIME_ZONE);


        return internalDate(year, month, day, calendar);
    }

    public static long utcDate(int year, int month, int day,
                               int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        midnight(calendar);

        /* This might change the date, but when you convert it
        back to the clocktime timezone, it will be correct.
         */
        calendar.setTimeZone(UTC_TIME_ZONE);

        return internalDate(year, month, day, hour, minute, calendar);
    }

    private static long internalDate(int year, int month, int day, int hour, int minute, Calendar calendar) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        return calendar.getTime().getTime();
    }


    public static long wallTimeDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();

        /* Set to midnight. */
        midnight(calendar);


        return internalDate(year, month, day, calendar);
    }


    public static long date(int year, int month, int day) {
        return utcDate( year, month, day );
    }

    public static long date(int year, int month, int day,
                            int hour, int minute) {
        return utcDate( year, month, day, hour, minute );

    }


    public static long date(TimeZone tz, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();

        /* Set to midnight. */
        midnight(calendar);

        calendar.setTimeZone(tz);

        return internalDate(year, month, day, calendar);
    }

    private static long internalDate(int year, int month, int day, Calendar calendar) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        return calendar.getTime().getTime();
    }

    public static long wallTimeDate(int year, int month, int day,
                                     int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        midnight(calendar);


        return internalDate(year, month, day, hour, minute, calendar);
    }

    public static long date(TimeZone tz, int year, int month, int day,
                            int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        midnight(calendar);
        calendar.setTimeZone(tz);

        return internalDate(year, month, day, hour, minute, calendar);
    }

    private static void midnight(Calendar calendar) {
        /* Set to midnight. */
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


}
