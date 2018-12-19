package com.kcx.acg.views.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blankj.utilcode.util.BarUtils;
import com.kcx.acg.R;

import java.lang.reflect.Field;

/**
 */

public class LottieLoadingDialog extends Dialog {
    private Context context;

    public LottieLoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LottieLoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected LottieLoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
    }

    public static class Builder {
        private LottieLoadingDialog dialog;
        private View view;
        private Context context;
        private ImageView loadingIv;

        public Builder(Context context) {
            this.context = context;
            dialog = new LottieLoadingDialog(context, R.style.LoadingDialog);
            dialog.setCancelable(false);
            create();
        }

        public LottieLoadingDialog create() {
            view = LayoutInflater.from(context).inflate(R.layout.lottie_loading_layout, null);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(false);

            loadingIv = view.findViewById(R.id.loading_iv);
            loadingIv.setImageResource(R.drawable.loading);
            AnimationDrawable animationDrawable = (AnimationDrawable) loadingIv.getDrawable();
            animationDrawable.start();
            return dialog;
        }

        public LottieLoadingDialog setTimeOut() {
            loadingIv.setImageResource(R.mipmap.image_failed);
            return dialog;
        }

        public LottieLoadingDialog setNetworkError() {
            loadingIv.setImageResource(R.mipmap.image_networkout);
            return dialog;
        }

        public LottieLoadingDialog setServerError() {
            loadingIv.setImageResource(R.mipmap.image_failed);
            return dialog;
        }
    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);
        setTranslucentStatus();
    }

    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
        } else {//4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
