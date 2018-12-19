package com.kcx.acg.views.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kcx.acg.R;
import com.kcx.acg.base.SysApplication;

import java.lang.reflect.Field;

/**
 */

public class CustomToast2 extends Toast {
    private static Toast mToast;

    public CustomToast2(Context context) {
        super(context);
    }

    public static void showToast(CharSequence text ){
        makeText(SysApplication.getContext(), text, LENGTH_SHORT).show();
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,
                ViewGroup.LayoutParams.MATCH_PARENT);

        TextView toastTextView = (TextView) view.findViewById(R.id.text);
        //设置TextView的宽度为 屏幕宽度
        toastTextView.setLayoutParams(layoutParams);
        toastTextView.setGravity(Gravity.CENTER);
        toastTextView.setText(text);

        mToast.setView(view);
        mToast.setGravity(Gravity.TOP, 0, 0);
        mToast.setDuration(duration);
        try {
            Object mTN = null;
            mTN = getField(mToast, "mTN");
            if (mTN != null) {
                Object mParams = getField(mTN, "mParams");
                if (mParams != null
                        && mParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
                    params.windowAnimations = R.style.anim_view;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

}
