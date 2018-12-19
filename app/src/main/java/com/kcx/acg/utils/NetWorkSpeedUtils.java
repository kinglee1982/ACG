package com.kcx.acg.utils;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;

import com.blankj.utilcode.util.LogUtils;

import java.util.Timer;
import java.util.TimerTask;

public class NetWorkSpeedUtils {
    private Context context;
    private Handler mHandler;
    private Handler timeHandler;
    private static long total_tdata = TrafficStats.getTotalTxBytes();
    private static int count = 1;
    private double fileSizeKb;

    public NetWorkSpeedUtils(Context context, Handler mHandler, double fileSizeKb) {
        this.context = context;
        this.mHandler = mHandler;
        this.fileSizeKb = fileSizeKb;
    }

    public NetWorkSpeedUtils(Context context, Handler timeHandler) {
        this.context = context;
        this.timeHandler = timeHandler;
    }

    public static int getNetSpeed() {
        long traffic_data;
        traffic_data = TrafficStats.getTotalTxBytes() - total_tdata;//总的发送的字节数
        total_tdata = TrafficStats.getTotalTxBytes();

        return (int) (traffic_data / count);
    }

    /**
     * 定义线程周期性的获取网速
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(mRunnable, count * 1000);
            Message msg = mHandler.obtainMessage();
            String seep;//网速
            int temp = getNetSpeed();
            if (temp > (1024 * 1024)) {
                seep = temp / 1024 / 1024 + "Mb/s";
            } else if (temp > 1024) {
                seep = temp / 1024 + "Kb/s";
            } else {
                seep = temp + "b/s";
            }

            double d = temp * 1024 / fileSizeKb;
            msg.arg1 = (int) d;
            msg.obj = seep;
            mHandler.sendMessage(msg);
        }
    };

    public void start() {
        mHandler.postDelayed(mRunnable, 0);

    }

    public void getKbSpeed() {
        timeHandler.postDelayed(timeRun, 0);
    }

    public void stopTime(){
        timeHandler.removeCallbacks(timeRun);
    }

    private Runnable timeRun = new Runnable() {
        @Override
        public void run() {
            timeHandler.postDelayed(timeRun, count * 1000);
            Message msg = timeHandler.obtainMessage();
            int temp = getNetSpeed();
            if (temp > 1024) {
                msg.arg1 = temp;
                timeHandler.sendMessage(msg);
            }
        }
    };
}
