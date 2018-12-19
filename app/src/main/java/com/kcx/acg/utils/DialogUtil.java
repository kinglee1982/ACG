package com.kcx.acg.utils;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.conf.Constants;


/**
 * Created by jb
 * 配置环境地址
 */

public class DialogUtil extends Dialog implements View.OnClickListener {
    private Boolean cancelable = false;
    private Boolean canceledOnTouchOutside = false;
    private SearchManager.OnCancelListener cancelListener;
    private Context mContext;
    private String content;
    private int layoutId;
    private OnCloseListener listener;
    private Button btn_ok, btn_cancel;
    private TextView tv_cache;

    public DialogUtil(Context context) {
        super(context);
        this.mContext = context;
    }

    public DialogUtil(Context context, int themeResId, int layoutId, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.layoutId = layoutId;
        this.listener = listener;
    }


    public DialogUtil setContent(String content) {
        this.content = content;
        return this;
    }

    public DialogUtil setCanceledOnTouchOutside(Boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public DialogUtil setCancelable(Boolean cancelable) {
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
        setOnKeyListener(keylistener);
        tv_cache = findViewById(R.id.tv_cache);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (listener != null) {
                    listener.onClick(this);
                }
                this.dismiss();
                break;
            case R.id.btn_cancel:
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
