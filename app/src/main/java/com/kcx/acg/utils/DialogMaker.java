package com.kcx.acg.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.kcx.acg.R;

/**
 * 对app的所有对话框进行管理
 *
 * @author jb
 */
@SuppressLint("InflateParams")
public class DialogMaker {

    private String msg;

    public void setmsg(String msg) {
        this.msg = msg;
    }

    /**
     * 显示统一风格的等待对话框。没有标题(加载提示框)
     *
     * @param msg             对话框内容
     * @param isCanCancelabel 是否可以取消
     */
    public static Dialog showCommenWaitDialog(Context context, String msg, boolean isCanCancelabel) {
        final Dialog progressDialog = new Dialog(context, R.style.progress_dialog);
        progressDialog.setOwnerActivity((Activity) context);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        TextView contentTv = (TextView) contentView.findViewById(R.id.id_tv_loadingmsg);
        if (TextUtils.isEmpty(msg)){
            contentTv.setVisibility(View.GONE);
        }else {
        contentTv.setText(msg);
        }
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(isCanCancelabel);
        progressDialog.setContentView(contentView);
        progressDialog.show();
        return progressDialog;

    }


}
