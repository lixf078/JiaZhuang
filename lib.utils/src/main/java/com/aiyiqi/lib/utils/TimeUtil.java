package com.aiyiqi.lib.utils;

import com.aiyiqi.decoration.lib.constants.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    private static final String FORMAT_SECOND = "%d秒";
    private static final String FORMAT_MINUTE = "%d分钟";
    private static final String FORMAT_MINUTE_SECOND = "%d分%d秒";
    private static final String FORMAT_HOUR = "%d小时";
    private static final String FORMAT_HOUR_MINUTE = "%d小时%d分";
    private static final String FORMAT_HOUR_MINUTE_SECOND = "%d小时%d分%d秒";
    private static final String FORMAT_DAY = "%d天";
    private static final String FORMAT_DAY_HOUR = "%d天%d小时";
    private static final String FORMAT_DAY_HOUR_MINUTE = "%d天%d小时%d分";
    private static final String FORMAT_DAY_HOUR_MINUTE_SECOND = "%d天%d小时%d分%d秒";

    private TimeUtil() {
    }

    public static final long getCurrentMills() {
        final android.text.format.Time now = new android.text.format.Time();
        now.setToNow();
        return now.toMillis(false);
    }

    public static final String formatDate(int date) {
        String temp = String.valueOf(date);
        return new StringBuilder().append(temp.substring(0, 4)).append("-").append(temp.substring(4, 6)).append("-")
                .append(temp.substring(6)).toString();
    }

    public static final String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return format.format(date);
    }

    public static final String formatDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(time));
    }

    public static final String formatYearMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(new Date(time));
    }

    public static final String formatTimeForLabel(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        return format.format(new Date(time));
    }

    public static final String formatTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    public static final String formatTimeToHHmm(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }

    public static final String formatTimeToYMDHHmm(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static final String formatTimeToYMDHHmmByBackslash(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return format.format(new Date(time));
    }

    public static final String formatTimeWithUnderline(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return format.format(new Date(time));
    }

    public static final String formatSimpleTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM.dd HH:mm");
        return format.format(new Date(time));
    }

    public static final String formatFullTimeWithZone(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return format.format(new Date(time));
    }

    public static final String formatLength(int second) {
        if (second < 60) {
            return String.format(FORMAT_SECOND, second);
        } else if ((second >= 60) && (second < 60 * 60)) {
            final int min = second / 60;
            final int sec = second % 60;
            return (sec == 0) ? String.format(FORMAT_MINUTE, min) : String.format(FORMAT_MINUTE_SECOND, min, sec);
        } else if ((second >= 60 * 60) && (second < 24 * 60 * 60)) {
            final int hour = second / (60 * 60);
            final int mod = second % (60 * 60);
            if (mod == 0) {
                return String.format(FORMAT_HOUR, hour);
            } else {
                final int min = mod / 60;
                final int sec = mod % 60;
                return (sec == 0) ? String.format(FORMAT_HOUR_MINUTE, hour, min) : String.format(FORMAT_HOUR_MINUTE_SECOND, hour, min, sec);
            }
        } else {
            final int day = second / (24 * 60 * 60);
            final int mod = second % (24 * 60 * 60);
            if (mod == 0) {
                return String.format(FORMAT_DAY, day);
            } else {
                final int hour = mod / (60 * 60);
                final int mod2 = mod % (60 * 60);
                if (mod2 == 0) {
                    return String.format(FORMAT_DAY_HOUR, day, hour);
                } else {
                    final int min = mod2 / 60;
                    final int sec = mod2 % 60;
                    return (sec == 0) ? String.format(FORMAT_DAY_HOUR_MINUTE, day, hour, min) : String.format(FORMAT_DAY_HOUR_MINUTE_SECOND, day, hour, min, sec);
                }
            }
        }
    }

    public static final String formatSimpleLength(int second) {
        if (second < 60) {
            return String.format(FORMAT_SECOND, second);
        } else if ((second >= 60) && (second < 60 * 60)) {
            final int min = second / 60;
            return String.format(FORMAT_MINUTE, min);
        } else if ((second >= 60 * 60) && (second < 24 * 60 * 60)) {
            final int hour = second / (60 * 60);
            return String.format(FORMAT_HOUR, hour);
        } else {
            final int day = second / (24 * 60 * 60);
            return String.format(FORMAT_DAY, day);
        }
    }

    public static final int getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static final int getMonth(final long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        return cal.get(Calendar.MONTH);
    }

    public static final int getDay(final long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static final String getDayOfWeek(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
            default:
                return "";
        }
    }

    public static final String formatDuration(int duration) {
        if (duration <= 1800) {
            return "半小时";
        } else if (duration <= 3600 * 4) {
            return Integer.toString((int) Math.ceil((double) duration / 3600)) + "小时";
        } else {
            return Integer.toString((int) Math.ceil((double) duration / (3600 * 24))) + "天";
        }
    }

    public static final String formatTimeSequence(long startT, long targetT) {
        StringBuilder sb = new StringBuilder();
        int startDay = (int) startT / 86400;
        int targetDay = (int) targetT / 86400;
        if (targetDay >= startDay) {
            sb.append("第").append(targetDay - startDay + 1).append("天");
            sb.append('(');
        }
        sb.append(formatTime(targetT * 1000));
        if (targetDay >= startDay) {
            sb.append(')');
        }

        return sb.toString();
    }

    public static final long getLastWeekend() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.get(Calendar.DAY_OF_WEEK) > Calendar.SATURDAY) {
            calendar.set(Calendar.WEEK_OF_MONTH, 1 +
                    calendar.get(Calendar.WEEK_OF_MONTH));
        }
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return calendar.getTimeInMillis();
    }

    public static final String formatMessageTime(final long baseT, final int year, final long msgT) {
        long currentMills = getCurrentMills(); // 即时获取当前系统时间，不使用baseT, year
        int currentYear = 0;
        final long interval = (currentMills - msgT);
        final int intervalSec = (int) interval / 1000;
        if (interval < Constants.Time.MINUTE) {
            return "刚刚";
        } else if (interval <= Constants.Time.DAY) {
            return formatSimpleLength(intervalSec)+ "前";
        } else {
            Calendar calendar = Calendar.getInstance();
            currentYear = calendar.get(Calendar.YEAR);
            calendar.setTimeInMillis(msgT);
            final int msgYear = calendar.get(Calendar.YEAR);
            if (msgYear == currentYear) {
                return formatTimeForLabel(msgT);
            } else {
                return formatDate(msgT);
            }
        }
    }

    public static final long formatSpecialTimeToMillisecond(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try{
            return format.parse(time).getTime();

        } catch (ParseException e) {
           Logger.e("TimeUtil","format special time error.",e);
        }
        return Constants.INVALID;
    }

    // 11-12 20:52
    public static final long formUmengNotificationTime(String time){
        SimpleDateFormat format = new SimpleDateFormat("MM-dd hh:mm");
        try{

            Logger.e(Constants.TAG,"getYear : "+getYear());
            return formatSpecialYearTime(getYear())+format.parse(time).getTime();

        } catch (ParseException e) {
            Logger.e(Constants.TAG,"format special time error.",e);
        }
        return Constants.INVALID;
    }

    public static final long formatSpecialYearTime(int year){
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        try{
            return format.parse(String.valueOf(year)).getTime();

        } catch (ParseException e) {
            Logger.e(Constants.TAG,"format special time error.",e);
        }
        return Constants.INVALID;
    }
}
