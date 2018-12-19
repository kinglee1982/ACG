package com.kcx.acg.manager;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.kcx.acg.R;
import com.kcx.acg.api.GetAppUpgradeInfoApi;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.UpgradeInfoBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.CommonUtils;
import com.kcx.acg.views.activity.MainActivity;
import com.kcx.acg.views.view.NewVersionDialog;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.kcx.acg.base.SysApplication.getMainThreadId;


/**
 * Created by Administrator on 2016-9-11.
 */
public class UpdateManager {
    public static String TAG = "UpdateService";
    public static int NOTIFI_ID = 12345;
    private static File DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

    private NewVersionDialog.Builder builder;

    /**
     * 安全的执行一个任务
     */
    public static void postTaskSafely(Runnable task) {
        // 得到当前的线程
        long curThreadId = android.os.Process.myTid();
        // 得到主线程的线程id
        long mainThreadId = getMainThreadId();
        if (curThreadId == mainThreadId) {
            // 如果当前是在主线程-->直接执行
            task.run();
        } else {
            // 如果当前是在子线程-->通过消息机制,把任务发送到主线程执行
            getMainThreadHandler().post(task);
        }
    }

    /**
     * 得到主线程的一个handler
     */
    public static Handler getMainThreadHandler() {
        return SysApplication.getHandler();
    }

    public void showDialog(final Activity context, final UpgradeInfoBean.ReturnDataBean version, final DialogInterface.OnCancelListener onCancelListener) {
        postTaskSafely(new Runnable() {
            @Override
            public void run() {
                NewVersionDialog dialog;
                builder = new NewVersionDialog.Builder(context);
                dialog = builder.create(version);
                dialog.show();
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = (int) (SysApplication.mWidthPixels * 0.8); //设置宽度
                dialog.getWindow().setAttributes(lp);
                builder.setOnUpgradeListener(new NewVersionDialog.Builder.OnUpgradeListener() {
                    @Override
                    public void onUpgrade() {
                        startTask(context, version.getDownLoadUrl());
                    }
                });
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if(onCancelListener != null)
                        onCancelListener.onCancel(dialogInterface);
                    }
                });
            }
        });
    }


    private void startTask(final Context context, String url) {
//        AppDownloadTask task = new AppDownloadTask(url, notificationManager, builder);
        AppDownloadTask task = new AppDownloadTask(url, builder);
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            task.downloadApk(DOWNLOAD_DIR);
        }
    }


    private String getFileName(String url) {
        String filename = "";
        if (!TextUtils.isEmpty(url)) {
            String[] split = url.split("/");
            if (split.length > 0) {
                filename = split[split.length - 1];
            }
        }
        if (!filename.contains(".apk")) filename += ".apk";

        return DOWNLOAD_DIR + File.separator + filename;
    }

}
