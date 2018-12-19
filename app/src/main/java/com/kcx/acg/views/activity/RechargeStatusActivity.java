package com.kcx.acg.views.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.manager.RefreshUserInfoManager;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * vip开通状态
 * Created by jb on 2018/9/13.
 */
public class RechargeStatusActivity extends BaseActivity implements CancelAdapt {
    private ImageButton ib_back;
    private ImageView iv_status;
    private TextView tv_status;
    private Button btn_confirm;
    private String msg;
    private boolean isSuccess;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_vip_status, null);
    }

    @Override
    public void initView() {
        super.initView();
        ib_back = findViewById(R.id.ib_back);
        iv_status = findViewById(R.id.iv_status);
        tv_status = findViewById(R.id.tv_status);
        btn_confirm = findViewById(R.id.btn_confirm);


    }

    @Override
    public void setListener() {
        super.setListener();
        ib_back.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        isSuccess = getIntent().getBooleanExtra("isSuccess", false);
        if (isSuccess) {
            iv_status.setBackgroundResource(R.mipmap.icon_vip_succeed);
            RefreshUserInfoManager.getInstances().refreshUserInfo(RechargeStatusActivity.this);
        } else {
            iv_status.setBackgroundResource(R.mipmap.icon_vip_loser);
        }
        msg = getIntent().getStringExtra("msg");
        tv_status.setText(msg);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.btn_confirm:
                finish();
                break;
        }
    }

}
