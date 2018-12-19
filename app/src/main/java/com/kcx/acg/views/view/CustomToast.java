package com.kcx.acg.views.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.devspark.appmsg.AppMsg;
import com.kcx.acg.R;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.utils.ToastUtil;
import com.kcx.acg.views.activity.WorkDetailsActivity;

import java.lang.reflect.Field;

import static android.widget.Toast.LENGTH_SHORT;

/**
 */

public class CustomToast {
    private static Toast mToast;

    public static void showToast( CharSequence text) {
        showToast(SysApplication.getContext(), text);
    }
    public static void showToast(Context context, CharSequence text) {
        makeText(context, text, LENGTH_SHORT).show();
    }

    private static Toast makeText(Context context, CharSequence text, int duration) {
        if (mToast == null) {
            mToast = new Toast(context);
        } else {
            mToast.cancel();
            mToast = new Toast(context);
        }

        //获取LayoutInflater对象
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //获得屏幕的宽度
        int width = SysApplication.mWidthPixels;

        //由layout文件创建一个View对象
        View view = inflater.inflate(R.layout.toast_xml, null);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,
//                ViewGroup.LayoutParams.MATCH_PARENT);

        TextView toastTextView = view.findViewById(R.id.message);
        //设置TextView的宽度为 屏幕宽度
//        toastTextView.setLayoutParams(layoutParams);
//        toastTextView.setGravity(Gravity.CENTER);
        toastTextView.setText(text);

        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(duration);

        return mToast;
    }

    /**
     * 反射字段
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getField(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(object);
        }
        return null;
    }

    /**
     public static void showToast(Context context, CharSequence text){
     ToastUtils.setBgResource(R.color.black_60);
     ToastUtils.setGravity(Gravity.CENTER, 0 ,0);
     ToastUtils.showShort(text);
     ToastUtils.showCustomShort().getAlpha()
     Toast.makeText(context, "", Toast.LENGTH_SHORT).show();


     //        makeText(context , text, 2000);
     }

     public static void makeText(Context context, CharSequence text, int duration) {
     int result = 0;
     int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
     if (resourceId > 0) {
     result = context.getResources().getDimensionPixelSize(resourceId);
     }
     AppMsg.Style style = new AppMsg.Style(duration, R.color.blue_6bb);
     LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
     layoutParams.setMargins(0, result,0,0);


     AppMsg.makeText((Activity) context, text, style).setLayoutParams(layoutParams).show();
     }*/


}
