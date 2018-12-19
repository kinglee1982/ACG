package com.kcx.acg.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.apkfuns.logutils.Constant;
import com.kcx.acg.R;
import com.kcx.acg.bean.SelectMemberBankBean;
import com.kcx.acg.conf.Constants;

import java.util.List;

/**
 * Created by zjb on 2018/11/15.
 */
public class PopUtil {
    private final Context context;
    private final int layout;
    private final View parent;
    private final int type;
    private OnItemClickListener mlistener = null;

    public interface OnItemClickListener {
        void onItemListener(int i);
    }

    public PopUtil(Context context, int layout, View parent, int type, OnItemClickListener listener) {
        this.context = context;
        this.layout = layout;
        this.mlistener = listener;
        this.parent = parent;
        this.type = type;

    }

    public void  showPop() {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(context).inflate(layout, null);
        //        View contentView = getLayoutInflater().inflate(R.layout.pop_income, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //        setBackgroundAlpha(0.5f);//设置屏幕透明度
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.color.white));
        popupWindow.setTouchable(true);

        // 设置按钮的点击事件
        final Button btn_totalIncome = (Button) contentView.findViewById(R.id.btn_totalIncome);
        final Button btn_generalizeIncome = (Button) contentView.findViewById(R.id.btn_generalizeIncome);
        final Button btn_productionIncome = (Button) contentView.findViewById(R.id.btn_productionIncome);
        if (type == 1) {
            btn_totalIncome.setTextColor(context.getResources().getColor(R.color.pink_hint));
            btn_totalIncome.setBackgroundResource(R.drawable.shape_pink_bg_5dp_income);
            btn_generalizeIncome.setTextColor(context.getResources().getColor(R.color.black_666));
            btn_generalizeIncome.setBackgroundResource(R.drawable.shape_gary_bg_5dp);
            btn_productionIncome.setTextColor(context.getResources().getColor(R.color.black_666));
            btn_productionIncome.setBackgroundResource(R.drawable.shape_gary_bg_5dp);
        } else if (type == 2) {
            btn_generalizeIncome.setTextColor(context.getResources().getColor(R.color.pink_hint));
            btn_generalizeIncome.setBackgroundResource(R.drawable.shape_pink_bg_5dp_income);
            btn_totalIncome.setTextColor(context.getResources().getColor(R.color.black_666));
            btn_totalIncome.setBackgroundResource(R.drawable.shape_gary_bg_5dp);
            btn_productionIncome.setTextColor(context.getResources().getColor(R.color.black_666));
            btn_productionIncome.setBackgroundResource(R.drawable.shape_gary_bg_5dp);
        } else if (type == 3) {
            btn_productionIncome.setTextColor(context.getResources().getColor(R.color.pink_hint));
            btn_productionIncome.setBackgroundResource(R.drawable.shape_pink_bg_5dp_income);
            btn_totalIncome.setTextColor(context.getResources().getColor(R.color.black_666));
            btn_totalIncome.setBackgroundResource(R.drawable.shape_gary_bg_5dp);
            btn_generalizeIncome.setTextColor(context.getResources().getColor(R.color.black_666));
            btn_generalizeIncome.setBackgroundResource(R.drawable.shape_gary_bg_5dp);
        }

        btn_totalIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mlistener.onItemListener(1);
               popupWindow.dismiss();
            }
        });
        btn_generalizeIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onItemListener(2);
                popupWindow.dismiss();
            }
        });
        btn_productionIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onItemListener(3);
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //                setBackgroundAlpha(1f);
            }
        });
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });

        //        popupWindow.showAtLocation(parent, Gravity.TOP, 0, 0);
        popupWindow.showAsDropDown(parent);

    }

}
