package com.kcx.acg.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.kcx.acg.R;
import com.kcx.acg.views.activity.CreativeCenterActivity;
import com.kcx.acg.views.view.HomeDialog;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class PermissionManager {

    private static PermissionManager instance;
    int REQUEST_EXTERNAL_STORAGE = 1234;
    String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private PermissionManager() {

    }

    public static PermissionManager getInstances() {
        if (instance == null) {
            synchronized (PermissionManager.class) {
                if (instance == null) {
                    instance = new PermissionManager();
                    return instance;
                }
            }
        }
        return instance;
    }

    public void checkPermission(final Context context) {
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            HomeDialog.Builder builder = new HomeDialog.Builder(context);
            builder.setLayoutId(R.layout.dialog_premission_layout).builder().show();
            builder.setOnClickListener(new HomeDialog.Builder.OnClickListener() {
                @Override
                public void onConfrim() {
                    requestPermissions(
                            (Activity) context,
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                }
            });
        }else {

        }
    }
}
