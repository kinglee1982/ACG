package com.kcx.acg.views.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kcx.acg.R;

/**
 * 自定义多种状态的按钮
 * * Created by jb.
 */
public class MyStateButton extends RelativeLayout {
    private Context context;
    private Button button;
    private ProgressBar progressBar;
    private ButtonClickListener buttonClickListener;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 5:  //加载完成
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
//                    button.setVisibility(View.VISIBLE);
                    button.setEnabled(true);
                    break;

                case 0:  //+关注
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    button.setVisibility(View.VISIBLE);
                    button.setEnabled(true);
                    button.setText(msg.obj.toString());
                    button.setBackgroundResource(R.drawable.shape_attention_pink_bg);

                    break;
                case 1:  //已关注
                    progressBar.clearAnimation();
                    progressBar.setVisibility(View.GONE);
                    button.setVisibility(View.VISIBLE);
                    button.setEnabled(true);
                    button.setText(msg.obj.toString());
                    button.setBackgroundResource(R.drawable.shape_be_attention_bg);
                    break;
                case 2:                    //设定按钮颜色以及进度条可见性
                    progressBar.setVisibility(View.VISIBLE);
//                    button.setVisibility(View.GONE);
                    button.setText(msg.obj.toString());
                    button.setEnabled(false);
                    break;
            }
        }
    };

    public interface ButtonClickListener {
        void innerClick();
    }

    //点击监听
    public void setOnInnerClickeListener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }
    public void setOnInnerStart() {
        Message message = Message.obtain();
        message.obj = "";
        message.what = 2;
        handler.sendMessage(message);
    }

    public void setOnInnerResult() {
        Message message = Message.obtain();
        message.obj = "";
        message.what = 5;
        handler.sendMessage(message);
    }

    //完成方法
    public void setOnInnerFinish(String text) {
        Message message = Message.obtain();
        message.obj = text;
        message.what = 1;
        handler.sendMessage(message);
    }

    //失败完成方法
    public void setOnInnerUnFinish(final String text) {
        Message message = Message.obtain();
        message.obj = text;
        message.what = 0;
        handler.sendMessage(message);
    }

    public MyStateButton(Context context) {
        super(context);
        this.context = context;
    }

    public MyStateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyStateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 初始化布局
     */
    private void initView() {
        //获取外布局的宽高
        int totalWidth = getMeasuredWidth();
        int totalHeight = getMeasuredHeight();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof RelativeLayout) {
                button = (Button) ((RelativeLayout) child).getChildAt(0);
                //                button = (Button) getChildAt(i);
                //摆放子View，参数分别是子View矩形区域的左、上、右、下边
                child.layout(0, 0, totalWidth, totalHeight);
            }

//            if (getChildAt(i) instanceof ProgressBar) {
//                progressBar = (ProgressBar) getChildAt(i);
//                //progressBar linearParams.height = 20;// 控件的高强制设成20
//                //摆放子View，参数分别是子View矩形区域的左、上、右、下边
//                child.layout(0, 0, totalWidth, totalHeight);
//
//            }

            if (child instanceof LinearLayout) {
                progressBar = (ProgressBar) ((LinearLayout) child).getChildAt(0);
                //摆放子View，参数分别是子View矩形区域的左、上、右、下边
                child.layout(0, 0, totalWidth, totalHeight);
            }

        }
         button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonClickListener != null) {
                    buttonClickListener.innerClick();
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int a = childView.getMeasuredWidth();
            int b = childView.getMeasuredHeight();
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initView();
    }
}



