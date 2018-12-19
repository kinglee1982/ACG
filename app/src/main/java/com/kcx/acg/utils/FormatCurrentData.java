package com.kcx.acg.utils;

import com.kcx.acg.R;
import com.kcx.acg.base.SysApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by  o on 2018/10/24.
 */

public class FormatCurrentData {

    /**
     * 设置每个阶段时间
     */
    private static final int seconds_of_1minute = 60;

    private static final int seconds_of_30minutes = 30 * 60;

    private static final int seconds_of_1hour = 60 * 60;

    private static final int seconds_of_1day = 24 * 60 * 60;

    private static final int seconds_of_2days = seconds_of_1day * 2;

    private static final int seconds_of_15days = seconds_of_1day * 15;

    private static final int seconds_of_30days = seconds_of_1day * 30;

    private static final int seconds_of_6months = seconds_of_30days * 6;

    private static final int seconds_of_1year = seconds_of_30days * 12;

    public static final int POSITION_1DAY = 1;
    public static final int POSITION_2DAY = 2;
    public static final int POSITION_MONTH = 3;

    /**
     * 格式化时间
     *
     * @param mTime
     * @return
     */
    public static String getTimeRange(String mTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**获取当前时间*/
        Date curDate = new Date(System.currentTimeMillis());
        String dataStrNew = sdf.format(curDate);
        Date startTime = null;
        try {
            /**将时间转化成Date*/
            curDate = sdf.parse(dataStrNew);
            startTime = sdf.parse(mTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**除以1000是为了转换成秒*/
        if (startTime == null) {
            return SysApplication.getContext().getString(R.string.format_data_advertis);
        }
        long between = (curDate.getTime() - startTime.getTime()) / 1000;
        int elapsedTime = (int) (between);
        if (elapsedTime < seconds_of_1minute) {
            return SysApplication.getContext().getString(R.string.format_data_after);
        }
//        if (elapsedTime < seconds_of_30minutes) {
//
//            return elapsedTime / seconds_of_1minute + "分钟前";
//        }
        if (elapsedTime < seconds_of_1hour) {
            return elapsedTime / seconds_of_1minute + SysApplication.getContext().getString(R.string.format_data_minute);
        }
        if (elapsedTime < seconds_of_1day) {
            return elapsedTime / seconds_of_1hour + SysApplication.getContext().getString(R.string.format_data_hour);
        }
//        if (elapsedTime < seconds_of_15days) {
//            return elapsedTime / seconds_of_1day + "天前";
//        }
        if (elapsedTime < seconds_of_2days) {
            return SysApplication.getContext().getString(R.string.format_data_yestoday);
        }
//        if (elapsedTime < seconds_of_30days) {
//
//            return "半个月前";
//        }
//        if (elapsedTime < seconds_of_6months) {
//
//            return elapsedTime / seconds_of_30days + "月前";
//        }
//        if (elapsedTime < seconds_of_1year) {
//
//            return "半年前";
//        }
        if (elapsedTime < seconds_of_1year) {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            return format.format(startTime);
//            return elapsedTime / seconds_of_1year + "年前";
        }
        if (elapsedTime >= seconds_of_1year) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(startTime);
//            return elapsedTime / seconds_of_1year+SysApplication.getContext().getString(R.string.format_data_year);
        }
        return "";
    }


    /**
     * 格式化时间
     *
     * @param mTime
     * @return
     */
    public static String getCommentTime(String mTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**获取当前时间*/
        Date curDate = new Date(System.currentTimeMillis());
        String dataStrNew = sdf.format(curDate);
        Date startTime = null;
        try {
            /**将时间转化成Date*/
            curDate = sdf.parse(dataStrNew);
            startTime = sdf.parse(mTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**除以1000是为了转换成秒*/
        if (startTime == null) {
            return SysApplication.getContext().getString(R.string.format_data_advertis);

        }
        long between = (curDate.getTime() - startTime.getTime()) / 1000;
        int elapsedTime = (int) (between);
        if (elapsedTime < seconds_of_1minute) {
            return SysApplication.getContext().getString(R.string.format_data_after);
        }
//        if (elapsedTime < seconds_of_30minutes) {
//
//            return elapsedTime / seconds_of_1minute + "分钟前";
//        }
        if (elapsedTime < seconds_of_1hour) {
            return elapsedTime / seconds_of_1minute + SysApplication.getContext().getString(R.string.format_data_minute);
        }
        if (elapsedTime < seconds_of_1day) {
            return elapsedTime / seconds_of_1hour + SysApplication.getContext().getString(R.string.format_data_hour);
        }
//        if (elapsedTime < seconds_of_15days) {
//            return elapsedTime / seconds_of_1day + "天前";
//        }
        if (elapsedTime < seconds_of_2days) {
            return SysApplication.getContext().getString(R.string.format_data_yestoday);
        }
        if (elapsedTime < seconds_of_30days) {
            return elapsedTime / seconds_of_1day + SysApplication.getContext().getString(R.string.format_data_day);
        }
//        if (elapsedTime < seconds_of_6months) {
//
//            return elapsedTime / seconds_of_30days + "月前";
//        }
//        if (elapsedTime < seconds_of_1year) {
//
//            return "半年前";
//        }
        if (elapsedTime < seconds_of_1year) {
            return elapsedTime / seconds_of_30days + SysApplication.getContext().getString(R.string.format_data_month);
        }
        if (elapsedTime >= seconds_of_1year) {
            return elapsedTime / seconds_of_1year + SysApplication.getContext().getString(R.string.format_data_year);
        }
        return "";
    }


    public static String getTimePosition(String mTime) {
        try {
            if (IsToday(mTime)) {
                return SysApplication.getContext().getString(R.string.history_today_msg);
            }

            if (IsYesterday(mTime)) {
                return SysApplication.getContext().getString(R.string.history_yestoday_msg);

            }
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            return format.format(getDateFormat().parse(mTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws Exception
     */
    public static boolean IsToday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat().parse(day);
            cal.setTime(date);

            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);

                if (diffDay == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws Exception
     */
    public static boolean IsYesterday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = getDateFormat().parse(day);
            cal.setTime(date);
            if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
                int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                        - pre.get(Calendar.DAY_OF_YEAR);

                if (diffDay == -1) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
