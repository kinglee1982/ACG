package com.kcx.acg.views.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.CreditCardPayBean;
import com.kcx.acg.bean.MessageBean;
import com.kcx.acg.utils.BottomDialogUtil;
import com.kcx.acg.utils.ToastUtil;
import com.kcx.acg.views.adapter.CreditCardPayAdapter;
import com.kcx.acg.views.adapter.MessageAdapter;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Created by jb on 2018/9/13.
 * 信用卡支付界面
 */
public class CreditCardPayActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title,tv_right,tv_hint;
    private LinearLayout ll_back;
    private RelativeLayout rl_addCredit;
    private RecyclerView mRecyclerView;
    private FullyLinearLayoutManager mLayoutManager;
    private CreditCardPayAdapter creditCardPayAdapter;
    private List<CreditCardPayBean> list;
    private boolean unBindCard = false;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_credit_card_pay, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("信用卡支付");
        tv_right = findViewById(R.id.tv_right);
        tv_right.setText("编辑");
        tv_right.setVisibility(View.VISIBLE);
        tv_hint =findViewById(R.id.tv_hint);
        ll_back = findViewById(R.id.ll_back);
        rl_addCredit = findViewById(R.id.rl_addCredit);
        ll_back = findViewById(R.id.ll_back);
        mRecyclerView = findViewById(R.id.mRecyclerView);



    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        rl_addCredit.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        //TODO  模拟数据
        list =new ArrayList<CreditCardPayBean>();
        for (int i = 0; i <3 ; i++) {
            CreditCardPayBean creditCardPayBean = new CreditCardPayBean();
            creditCardPayBean.setName("张 三丰"+i);
            list.add(creditCardPayBean);
        }
        initRecyclerView(list);

        if (list.size()==0){
            tv_hint.setVisibility(View.GONE);
        }else {
            tv_hint.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.rl_addCredit: //添加新信用卡
                startActivity(new Intent(CreditCardPayActivity.this,EnterCreditCardPayActivity.class));
                break;

            case R.id.tv_right: //编辑
                if (unBindCard){
                    unBindCard=false;
                    tv_right.setText("编辑");
                    tv_hint.setVisibility(View.VISIBLE);
                    rl_addCredit.setVisibility(View.VISIBLE);
                    creditCardPayAdapter.update(unBindCard,0);
                }else{
                    unBindCard=true;
                    tv_right.setText("完成");
                    tv_hint.setVisibility(View.GONE);
                    rl_addCredit.setVisibility(View.GONE);
                    creditCardPayAdapter.update(unBindCard,1);

                }
                break;
        }
    }

    private void initRecyclerView(final List<CreditCardPayBean> dataList) {
        mLayoutManager = new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        creditCardPayAdapter = new CreditCardPayAdapter(CreditCardPayActivity.this, dataList,unBindCard);
        // 设置adapter
        mRecyclerView.setAdapter(creditCardPayAdapter);



        creditCardPayAdapter.setOnItemClickListener(new CreditCardPayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                new BottomDialogUtil(CreditCardPayActivity.this,
                        R.layout.dialog_bottom,
                        "确定使用该卡进行支付？",
                        "Visa（1234）",
                        "确定", "取消",
                        new BottomDialogUtil.BottonDialogListener() {
                            @Override
                            public void onItemListener() {
                                CustomToast.showToast( "确定支付");
                            }
                        });


            }

            @Override
            public void onDelItemClick(int position) {
                   new BottomDialogUtil(CreditCardPayActivity.this,
                           R.layout.dialog_bottom,
                           "确定解除与该卡的绑定？",
                           "Visa（1234）",
                           "确定", "取消",
                           new BottomDialogUtil.BottonDialogListener() {
                               @Override
                               public void onItemListener() {
                                   CustomToast.showToast("解除绑定");
                               }
                           });
            }
        });
    }

}
