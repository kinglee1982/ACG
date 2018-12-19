package com.kcx.acg.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.SpannableStringBuilder;
import android.widget.Button;

import com.kcx.acg.R;


/**
 * Created by jb on 2018/2/12.
 * 倒计时
 */

public class CountTimer extends CountDownTimer {

    private  Context context;
    private SpannableStringBuilder sb;
//    private ForegroundColorSpan colorSpan;
    private Button button;

    public CountTimer(Context context,long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        this.context = context;
        this.button = button;
        sb = new SpannableStringBuilder();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //处理后的倒计时数值
        int time = (int) (Math.round((double) millisUntilFinished / 1000) - 1);
        //拼接要显示的字符串
        sb.clear(); //先把之前的字符串清除
        sb.append(String.valueOf(time));
        sb.append(context.getString(R.string.countTimer_timeHint));
        int index = String.valueOf(sb).indexOf(context.getString(R.string.countTimer_after));
        //给秒数和单位设置蓝色前景色
//        sb.setSpan(colorSpan, 0, index, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        button.setText(sb);
        //设置倒计时中的按钮外观
        button.setClickable(false);//倒计时过程中将按钮设置为不可点击

    }

    @Override
    public void onFinish() {
        //设置倒计时结束之后的按钮样式
        button.setText(R.string.countTimer_sendCode);
        button.setClickable(true);
        button.setTextColor(context.getResources().getColor(R.color.black_333));
    }
}
