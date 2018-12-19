package com.kcx.acg.utils;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kcx.acg.R;
import com.kcx.acg.conf.Constants;


/**
 * Created by zjb on 2018/3/14.
 * 配置环境地址
 */

public class EnvironmentDialog extends Dialog implements View.OnClickListener {

    private Boolean cancelable = false;
    private Boolean canceledOnTouchOutside = false;
    private SearchManager.OnCancelListener cancelListener;
    private Context mContext;
    private String content;
    private int layoutId;
    private OnCloseListener listener;

    private RadioGroup radioGroup;
    private RadioButton rb_debug, rb_dev1, rb_release, rb_dev2, rb_youtube,rb_dev3;
    private Button btn_ok;

    public EnvironmentDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public EnvironmentDialog(Context context, int themeResId, int layoutId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.layoutId = layoutId;
        this.listener = listener;
    }


    public EnvironmentDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public EnvironmentDialog setCanceledOnTouchOutside(Boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public EnvironmentDialog setCancelable(Boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        setCancelable(cancelable);
        initView();
    }

    private void initView() {

        /**
         * 设置宽度全屏，要设置在show的后面
         */
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.gravity = Gravity.CENTER;
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                getWindow().getDecorView().setPadding(0, 0, 0, 0);
                getWindow().setAttributes(layoutParams);



        setOnKeyListener(keylistener);

        radioGroup = findViewById(R.id.radioGroup);
        rb_debug = findViewById(R.id.rb_debug);
        rb_dev1 = findViewById(R.id.rb_dev1);
        rb_release = findViewById(R.id.rb_release);
        rb_dev2 = findViewById(R.id.rb_dev2);
        rb_youtube = findViewById(R.id.rb_youtube);
        rb_dev3 = findViewById(R.id.rb_dev3);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (rb_debug.isChecked()) {
                    Constants.URL.setUrl(mContext, Constants.DEBUG_URL);
                } else if (rb_dev1.isChecked()) {
                    Constants.URL.setUrl(mContext, Constants.DEV1_URL);
                } else if (rb_release.isChecked()) {
                    Constants.URL.setUrl(mContext, Constants.RELEASE_URL);
                } else if (rb_release.isChecked()) {
                    Constants.URL.setUrl(mContext, Constants.RELEASE_URL);
                } else if (rb_dev2.isChecked()) {
                    Constants.URL.setUrl(mContext, Constants.DEV2_URL);
                } else if (rb_youtube.isChecked()) {
                    Constants.URL.setUrl(mContext, Constants.YOUTUBE_URL);
                }else if (rb_dev3.isChecked()) {
                    Constants.URL.setUrl(mContext, Constants.DEV3_URL);
                }

                if (listener != null) {
                    listener.onClick(this);
                }
                this.dismiss();
                break;


        }
    }


    public interface OnCloseListener {
        void onClick(Dialog dialog);
    }

    OnKeyListener keylistener = new OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };
}
