package com.kcx.acg.views.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.utils.ToastUtil;
import com.kcx.acg.views.view.CustomToast;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Created by jb on 2018/9/13.
 * 信用卡支付录入界面
 */
public class EnterCreditCardPayActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title, tv_areaCode,tv_country,tv_province;
    private Button  btn_pay;
    private EditText et_cardNum, et_month, et_year, et_securityCode, et_name, et_familyName, et_street1, et_street2, et_district, et_postalCode, et_phone;
    private RelativeLayout rl_province, rl_country;
    private LinearLayout ll_back;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_enter_credit_card_pay, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("信用卡支付");
        ll_back = findViewById(R.id.ll_back);
        btn_pay = findViewById(R.id.btn_pay);

        rl_province = findViewById(R.id.rl_province);
        rl_country = findViewById(R.id.rl_country);

        tv_areaCode = findViewById(R.id.tv_areaCode);
        et_cardNum = findViewById(R.id.et_cardNum);
        et_month = findViewById(R.id.et_month);
        et_year = findViewById(R.id.et_year);
        et_securityCode = findViewById(R.id.et_securityCode);
        et_name = findViewById(R.id.et_name);
        et_familyName = findViewById(R.id.et_familyName);
        et_street1 = findViewById(R.id.et_street1);
        et_street2 = findViewById(R.id.et_street2);
        et_district = findViewById(R.id.et_district);
        et_postalCode = findViewById(R.id.et_postalCode);
        et_phone = findViewById(R.id.et_phone);
        tv_country = findViewById(R.id.tv_country);
        tv_province = findViewById(R.id.tv_province);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_pay.setOnClickListener(this);
        tv_areaCode.setOnClickListener(this);
        rl_country.setOnClickListener(this);
        rl_province.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.tv_areaCode: //区号选择
                Intent intent = new Intent(EnterCreditCardPayActivity.this, ChooseDataListActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_province: //省 选择
                Intent i = new Intent(EnterCreditCardPayActivity.this, ChooseDataListActivity.class);
                startActivity(i);
                break;

            case R.id.rl_country: //国家 选择
                Intent i1 = new Intent(EnterCreditCardPayActivity.this, ChooseDataListActivity.class);
                startActivity(i1);
                break;

            case R.id.btn_pay: //完成支付
                CustomToast.showToast("完成支付");
                break;
        }

    }
}
