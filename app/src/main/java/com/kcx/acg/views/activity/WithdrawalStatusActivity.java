package com.kcx.acg.views.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;

import java.io.Serializable;

/**
 * 提现状态
 * Created by jb on 2018/9/18.
 */
public class WithdrawalStatusActivity extends BaseActivity implements Serializable {
    private ImageButton ib_back;
    private ImageView iv_status;
    private TextView tv_status1, tv_status2;
    private Button btn_confirm;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_withdrawal_status, null);
    }

    @Override
    public void initView() {
        super.initView();
        ib_back = findViewById(R.id.ib_back);
        iv_status = findViewById(R.id.iv_status);
        tv_status1 = findViewById(R.id.tv_status1);
        tv_status2 = findViewById(R.id.tv_status2);
        btn_confirm = findViewById(R.id.btn_confirm);
    }

    @Override
    public void setListener() {
        super.setListener();
        ib_back.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
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
