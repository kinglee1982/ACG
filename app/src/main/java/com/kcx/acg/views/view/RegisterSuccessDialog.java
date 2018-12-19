package com.kcx.acg.views.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.bean.UserInfoBean;


/**
 * Created by jb on 2018/2/11.
 */

public class RegisterSuccessDialog {
    private Context context;
    private int layout;
    private Boolean cancelable = false;
    private OnClickListener mlistener = null;
    private Dialog dialog;
    private Button btn_close;


    public interface OnClickListener {
        void onItemListener();
    }

    public RegisterSuccessDialog(Context context, int layout, OnClickListener listener) {
        this.context = context;
        this.layout = layout;
        this.mlistener = listener;
        initView();
    }


    private void initView() {
        dialog = new Dialog(context, R.style.dialog);
        View contentView = LayoutInflater.from(context).inflate(layout, null);
        dialog.setContentView(contentView);
        setData(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);


        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE); //为获取屏幕宽、高
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (wm.getDefaultDisplay().getHeight() * 1);   //高度设置为屏幕的1
        p.width = (int) (wm.getDefaultDisplay().getWidth() * 1);    //宽度设置为屏幕的1
        dialog.getWindow().setAttributes(p);     //设置生效

        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.TopDialog_Animation);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.show();

    }


    private void setData(View contentView) {
        btn_close = contentView.findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new clickListener());
    }

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //                case R.id.btn_close:
                //                    if (mlistener !=null){
                //                    mlistener.onItemListener();
                //                    }
                //                    dialog.cancel();
                //                    break;
                case R.id.btn_close:
                    dialog.cancel();
                    break;
            }
        }
    }


}
