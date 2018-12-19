package com.kcx.acg.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.bean.BankCardBean;
import com.kcx.acg.bean.MessageBean;
import com.kcx.acg.bean.SelectMemberBankBean;
import com.kcx.acg.views.activity.MessageActivity;
import com.kcx.acg.views.adapter.BankCarAdapter;
import com.kcx.acg.views.adapter.MessageAdapter;
import com.kcx.acg.views.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;


/**
 * 提现
 * Created by jb on 2018/2/11.
 */

public class BottomDialogUtil_withdrawal {

    private Context context;
    private int layout;
    private double pHeight;
    private Boolean cancelable = false;
    private BottonDialogListener mlistener = null;
    private Dialog bottomDialog;
    private FullyLinearLayoutManager mLayoutManager;
    private BankCarAdapter bankCarAdapter;
    private List<SelectMemberBankBean.ReturnDataBean> list;

    private ImageButton ib_close;
    private RecyclerView mRecyclerview;
    private TextView tv_addbank;


    public interface BottonDialogListener {
        void onItemListener(int i);
        void onDelItemListener(int i);
        void onAddBankListener();
    }

    public BottomDialogUtil_withdrawal(Context context, int layout, double pHeight, List<SelectMemberBankBean.ReturnDataBean> list, BottonDialogListener listener) {
        this.context = context;
        this.layout = layout;
        this.mlistener = listener;
        this.pHeight = pHeight;
        this.list = list;
        initView();
    }

    public void upData(List<SelectMemberBankBean.ReturnDataBean> list){
        this.list = list;
        bankCarAdapter.upData(list);
    }


    private void initView() {
        bottomDialog = new Dialog(context, R.style.dialog);
        View contentView = LayoutInflater.from(context).inflate(layout, null);
        bottomDialog.setContentView(contentView);
        setData(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);


                    WindowManager wm = (WindowManager) context
                            .getSystemService(Context.WINDOW_SERVICE); //为获取屏幕宽、高
                    android.view.WindowManager.LayoutParams p = bottomDialog.getWindow().getAttributes();  //获取对话框当前的参数值
                    p.height = (int) (wm.getDefaultDisplay().getHeight() * pHeight);   //高度设置为屏幕的0.75
                    p.width = (int) (wm.getDefaultDisplay().getWidth() * 1);    //宽度设置为屏幕的1
                    bottomDialog.getWindow().setAttributes(p);     //设置生效

        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCancelable(cancelable);
        bottomDialog.setCanceledOnTouchOutside(cancelable);
        bottomDialog.show();

    }


    private void setData(View contentView) {
        ib_close = contentView.findViewById(R.id.ib_close);
        mRecyclerview = contentView.findViewById(R.id.recyclerview);
        tv_addbank = contentView.findViewById(R.id.tv_addbank);
        tv_addbank.setText("+ "+context.getString(R.string.income_addBank));

        ib_close.setOnClickListener(new clickListener());
        tv_addbank.setOnClickListener(new clickListener());


        initRecyclerView(list);

    }

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_addbank:  //添加银行卡
                    bottomDialog.cancel();
                    if (mlistener != null) {
                        mlistener.onAddBankListener();
                    }
                    break;
                case R.id.ib_close:
                    bottomDialog.cancel();
                    break;

            }
        }
    }

    private void initRecyclerView(List<SelectMemberBankBean.ReturnDataBean> dataList) {
        mLayoutManager = new FullyLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        mRecyclerview.setLayoutManager(mLayoutManager);
        bankCarAdapter = new BankCarAdapter(context,dataList);
        // 设置adapter
        mRecyclerview.setAdapter(bankCarAdapter);


        bankCarAdapter.setOnItemClickListener(new BankCarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                bottomDialog.cancel();
                if (mlistener != null) {
                    mlistener.onItemListener(position);
                }
            }

            @Override
            public void onDelItemClick(View view, int position) {
                if (mlistener != null) {
                    mlistener.onDelItemListener(position);
                }
            }
        });
    }



}
