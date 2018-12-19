package com.kcx.acg.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kcx.acg.R;


/**
 * Created by jb on 2018/2/11.
 */

public class BottomDialogUtil {
    private  String title,hint,item1Str,item2Str;
    private Context context;
    private int layout;
    private double pHeight;
    private Boolean cancelable = true;
    private BottonDialogListener mlistener = null;
    private Dialog bottomDialog;
    private TextView tv_title,tv_hint,tv_item1, tv_item2;


    public interface BottonDialogListener {
        void onItemListener();
    }

    public BottomDialogUtil(Context context, int layout,String titile,String item1Str,String item2Str, BottonDialogListener listener) {
        this.context = context;
        this.layout = layout;
        this.mlistener = listener;
        this.title = titile;
        this.item1Str = item1Str;
        this.item2Str = item2Str;
        initView();
    }

    public BottomDialogUtil(Context context, int layout,String titile,String hint,String item1Str,String item2Str, BottonDialogListener listener) {
        this.context = context;
        this.layout = layout;
        this.mlistener = listener;
        this.title = titile;
        this.hint = hint;
        this.item1Str = item1Str;
        this.item2Str = item2Str;
        initView();
    }


    private void initView() {
        bottomDialog = new Dialog(context, R.style.dialog);
        View contentView = LayoutInflater.from(context).inflate(layout, null);
        bottomDialog.setContentView(contentView);
        setData(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);

        //        if (type != 3 && type != 4) {
        //            WindowManager wm = (WindowManager) context
        //                    .getSystemService(Context.WINDOW_SERVICE); //为获取屏幕宽、高
        //            android.view.WindowManager.LayoutParams p = bottomDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        //            p.height = (int) (wm.getDefaultDisplay().getHeight() * pHeight);   //高度设置为屏幕的0.75
        //            p.width = (int) (wm.getDefaultDisplay().getWidth() * 1);    //宽度设置为屏幕的1
        //            bottomDialog.getWindow().setAttributes(p);     //设置生效
        //        }

        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCancelable(cancelable);
        bottomDialog.setCanceledOnTouchOutside(cancelable);
        bottomDialog.show();

    }


    private void setData(View contentView) {
        tv_title = contentView.findViewById(R.id.tv_title);
        tv_hint = contentView.findViewById(R.id.tv_hint);
        if (TextUtils.isEmpty(hint)){
            tv_hint.setVisibility(View.GONE);
        }else {
            tv_hint.setVisibility(View.VISIBLE);
            tv_hint.setText(hint);
        }

        tv_item1 = contentView.findViewById(R.id.tv_item1);
        tv_item2 = contentView.findViewById(R.id.tv_item2);

        tv_item1.setOnClickListener(new clickListener());
        tv_item2.setOnClickListener(new clickListener());

        if (!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }

        if (!TextUtils.isEmpty(hint)){
            tv_hint.setText(hint);
            tv_hint.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(item1Str)){
            tv_item1.setText(item1Str);
        }

        if (!TextUtils.isEmpty(item2Str)){
            tv_item2.setText(item2Str);
        }


    }

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_item1:  //取消关注
                    bottomDialog.cancel();
                    if (mlistener != null) {
                        mlistener.onItemListener();
                    }
                    break;
                case R.id.tv_item2:
                    bottomDialog.cancel();
                    break;


            }
        }
    }


}
