package com.kcx.acg.views.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.manager.H5LinkUrlManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.StringUtil;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 账户
 * Created by jb on 2018/9/6.
 */
public class AccountActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title,tv_whoAbi,tv_money,tv_freeze;
    private Button  btn_toUpAbi;
    private ImageView imageView;
    private LinearLayout ll_back;

    private String userInfo;
    private UserInfoBean userInfoBean;

    @Override
    protected void onResume() {
        super.onResume();
        userInfo = SPUtil.getString(AccountActivity.this, Constants.USER_INFO, "");
        userInfoBean = AppUtil.getGson().fromJson(userInfo, UserInfoBean.class);
        tv_money.setText(StringUtil.toDecimalFormat(userInfoBean.getReturnData().getTotalACoin()));
        //冻结
        int accountStatus = userInfoBean.getReturnData().getAccountStatus();
        if (accountStatus == 3||accountStatus == 6||accountStatus == 7|accountStatus == 9){
            imageView.setBackgroundResource(R.mipmap.icon_abi_gray);
            tv_money.setTextColor(getResources().getColor(R.color.black_ccc));
            tv_freeze.setVisibility(View.VISIBLE);
        }else {
            imageView.setBackgroundResource(R.mipmap.icon_abi_golden);
            tv_money.setTextColor(getResources().getColor(R.color.black_333));
            tv_freeze.setVisibility(View.INVISIBLE);
        }

        if (userInfoBean.getReturnData().isIsHaveCharged()){
            btn_toUpAbi.setText(R.string.account_btn1);
        }else {
            btn_toUpAbi.setText(R.string.account_btn2);
        }
    }

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_account, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.account_title);
        ll_back = findViewById(R.id.ll_back);
        tv_whoAbi = findViewById(R.id.tv_whoAbi);
        tv_money = findViewById(R.id.tv_money);
        btn_toUpAbi = findViewById(R.id.btn_toUpAbi);
        imageView = findViewById(R.id.imageView);
        tv_freeze = findViewById(R.id.tv_freeze);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        tv_whoAbi.setOnClickListener(this);
        btn_toUpAbi.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_whoAbi:  //什么是A币
//                Intent intent = new Intent(AccountActivity.this, WebViewActivity.class);
//                intent.putExtra("url",userInfoBean.getReturnData().getH5Link_AcoinDescription());
//                intent.putExtra("classType", 3);
//                startActivity(intent);
                H5LinkUrlManager.getInstances().getH5LinkUrl(AccountActivity.this,3);
                break;
            case R.id.btn_toUpAbi:  //A币充值
                startActivity(new Intent(AccountActivity.this,ToUpACoinActivity.class));
                break;
        }

    }
}
