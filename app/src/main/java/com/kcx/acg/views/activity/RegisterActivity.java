package com.kcx.acg.views.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.api.GetH5LinkUrlApi;
import com.kcx.acg.api.SendIdentifyCodeApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.H5LinkUrlBean;
import com.kcx.acg.bean.SendIdentifyCodeBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.H5LinkUrlManager;
import com.kcx.acg.utils.CodeUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.view.CustomToast;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 注册界面
 * Created by jb on 2018/8/30.
 */
public class RegisterActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title, tv_protocol, tv_areaCode;
    private Button btn_sendSmsCode;
    private LinearLayout ll_back;
    private ImageButton ib_verifyCode;
    private EditText et_phoneNum, et_pw;
    private CodeUtil mCodeUtile;
    private Bitmap bitmap;
    private String phoneNum, verifyCode;
    private String areaCode;


    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_register, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.title_register);
        tv_protocol = findViewById(R.id.tv_protocol);
        ll_back = findViewById(R.id.ll_back);
        btn_sendSmsCode = findViewById(R.id.btn_sendSmsCode);
        ib_verifyCode = findViewById(R.id.ib_verifyCode);
        et_phoneNum = findViewById(R.id.et_phoneNum);
        et_pw = findViewById(R.id.et_pw);
        tv_areaCode = findViewById(R.id.tv_areaCode);
        tv_areaCode.setText("86");
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        tv_protocol.setOnClickListener(this);
        btn_sendSmsCode.setOnClickListener(this);
        ib_verifyCode.setOnClickListener(this);
        tv_areaCode.setOnClickListener(this);

        et_phoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                verifyCode = et_pw.getText().toString().trim();

                if (!TextUtils.isEmpty(s)) {
                    if (TextUtils.isEmpty(verifyCode)) {
                        btn_sendSmsCode.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
                        btn_sendSmsCode.setEnabled(false);
                    } else {
                        btn_sendSmsCode.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
                        btn_sendSmsCode.setEnabled(true);
                    }
                }

            }
        });


        et_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNum = et_phoneNum.getText().toString().trim();

                if (TextUtils.isEmpty(s)) {
                    btn_sendSmsCode.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
                    btn_sendSmsCode.setEnabled(false);
                } else {
                    if (TextUtils.isEmpty(phoneNum)) {
                        btn_sendSmsCode.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
                        btn_sendSmsCode.setEnabled(false);
                    } else {
                        btn_sendSmsCode.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
                        btn_sendSmsCode.setEnabled(true);
                    }
                }

            }
        });


    }

    @Override
    public void initData() {
        super.initData();
        mCodeUtile = CodeUtil.getInstance();
        refreshCode();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back: //返回
                finish();
                break;
            case R.id.ib_verifyCode: //刷新验证码
                refreshCode();
                break;
            case R.id.btn_sendSmsCode: //发送验证码
                phoneNum = et_phoneNum.getText().toString().trim();
                verifyCode = et_pw.getText().toString().trim();

                if (verifyCode.equalsIgnoreCase(mCodeUtile.getCode())) {
                    sendSmsCode(phoneNum);
                } else {
                    CustomToast.showToast(getResources().getString(R.string.register_errorStr));
                    refreshCode();
                }
                break;

            case R.id.tv_protocol: //用户协议
                H5LinkUrlManager.getInstances().getH5LinkUrl(RegisterActivity.this, 5);
                break;
            case R.id.tv_areaCode: //区号选择
                Intent intent1 = new Intent(RegisterActivity.this, ChooseDataListActivity.class);
                intent1.putExtra("areaCode", tv_areaCode.getText().toString().trim());
                startActivityForResult(intent1, 100);
                break;
        }
    }


    private void refreshCode() {
        bitmap = mCodeUtile.createBitmap();
        ib_verifyCode.setImageBitmap(bitmap);
    }

    /**
     * 发送短信验证码
     *
     * @param phoneNum
     */
    private void sendSmsCode(String phoneNum) {
        areaCode = tv_areaCode.getText().toString().trim();
        SendIdentifyCodeApi sendIdentifyCodeApi = new SendIdentifyCodeApi(httpOnNextListener, this);
        sendIdentifyCodeApi.setPhone(phoneNum);
        sendIdentifyCodeApi.setSmsType("1");
        sendIdentifyCodeApi.setAreaCode(areaCode);
        HttpManager manager = HttpManager.getInstance();
        manager.doHttpDeal(RegisterActivity.this, sendIdentifyCodeApi);
    }

    HttpOnNextListener<SendIdentifyCodeBean> httpOnNextListener = new HttpOnNextListener<SendIdentifyCodeBean>() {
        @Override
        public RetryWhenNetworkException.Wrapper onNext(SendIdentifyCodeBean sendIdentifyCodeBean) {
            if (sendIdentifyCodeBean.getErrorCode() == 200) {
                SPUtil.putString(RegisterActivity.this, Constants.AREA_CODE, areaCode);
                Intent intent = new Intent(RegisterActivity.this, VerifyActivity.class);
                intent.putExtra("classType", "1");
                intent.putExtra("phone", et_phoneNum.getText().toString().trim());
                startActivity(intent);
                finish();
            } else {
                CustomToast.showToast(sendIdentifyCodeBean.getErrorMsg());
                refreshCode();
            }
            return null;
        }


        @Override
        public RetryWhenNetworkException.Wrapper onError(Throwable e) {
            super.onError(e);
            btn_sendSmsCode.setEnabled(true);
            return null;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10086) {
            tv_areaCode.setText(data.getStringExtra("areaCode"));
        }
    }

}
