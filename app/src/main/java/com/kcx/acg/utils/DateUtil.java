package com.kcx.acg.utils;

import android.content.Context;

import com.kcx.acg.R;
import com.kcx.acg.base.SysApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jb on 2018/9/26.
 */
public class DateUtil {

    /*
    @param 要转换的毫秒数
    * @return 该毫秒数转换为
    * days
    *  hours
    *   minutes
    *   seconds 后的格式 
    *   @author jb  
    * */
    public static String formatDuring(Context context, long mss) {
        //系统返回的是秒
        long days = mss / (60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
//        return days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒";
        return days + context.getString(R.string.day);
    }

    /**
     * 获取系统当前日期 yyyy-MM-dd
     *
     * @return
     */
    public static String getSysDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new java.util.Date());
    }


    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16:09"）
     *
     * @param time
     * @return
     */
    public static String timet(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        long lcc = Long.valueOf(time);
//        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(lcc * 1000L));
        return times;

    }

    public static String getTime(int second) {
        if (second < 10) {
            return "00分钟0" + second + "秒";
        }
        if (second < 60) {
            return "00分钟" + second + "秒";
        }
        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "0" + minute + "分钟0" + second + "秒";
                }
                return "0" + minute + "分钟" + second + "秒";
            }
            if (second < 10) {
                return minute + "分钟0" + second + "秒";
            }
            return minute + "分钟" + second + "秒";
        }
        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + "小时0" + minute + "分钟0" + second + "秒";
                }
                return "0" + hour + "小时0" + minute + "分钟" + second + "秒";
            }
            if (second < 10) {
                return "0" + hour +"小时"+ minute + "分钟0" + second + "秒";
            }
            return "0" + hour +"小时"+ minute + "分钟" + second + "秒";
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + "小时0" + minute + "分钟0" + second + "秒";
            }
            return hour + "小时0" + minute + "分钟" + second + "秒";
        }
        if (second < 10) {
            return hour + "小时" + minute + "分钟0" + second + "秒";
        }
        return hour + "小时" + minute + "分钟" + second + "秒";
    }

    public static String getCurrentTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }


    public static String duration(Long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String hms = formatter.format(time);
        return hms;

    }



}




