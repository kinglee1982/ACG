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
import com.kcx.acg.api.SendIdentifyCodeApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.SendIdentifyCodeBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.CodeUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.view.CustomToast;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 找回密码
 * Created by jb on 2018/9/10.
 */
public class FindPWActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title, tv_areaCode;
    private Button btn_sendSmsCode;
    private EditText et_phoneNum, et_pw;
    private LinearLayout ll_back;
    private ImageButton ib_code;
    private Bitmap bitmap;
    private String phoneNum, verifyCode, areaCode;


    private CodeUtil codeUtil;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_find_pw, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.findPW_title);
        ll_back = findViewById(R.id.ll_back);
        et_phoneNum = findViewById(R.id.et_phoneNum);
        et_pw = findViewById(R.id.et_pw);
        btn_sendSmsCode = findViewById(R.id.btn_sendSmsCode);
        ib_code = findViewById(R.id.ib_code);
        tv_areaCode = findViewById(R.id.tv_areaCode);
        tv_areaCode.setText("86");


    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_sendSmsCode.setOnClickListener(this);
        ib_code.setOnClickListener(this);
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
        codeUtil = CodeUtil.getInstance();
        refreshCode();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_areaCode:  //区号选择
                Intent intent1 = new Intent(FindPWActivity.this, ChooseDataListActivity.class);
                intent1.putExtra("areaCode", tv_areaCode.getText().toString().trim());
                startActivityForResult(intent1, 100);
                break;

            case R.id.ib_code: //刷新验证码
                refreshCode();
                break;

            case R.id.btn_sendSmsCode:  //发送验证码
                phoneNum = et_phoneNum.getText().toString().trim();
                verifyCode = et_pw.getText().toString().trim();
                if (verifyCode.equalsIgnoreCase(codeUtil.getCode())) {
                    sendSmsCode(phoneNum);
                } else {
                    CustomToast.showToast(getString(R.string.findPW_errorStr));
                }
                break;
        }
    }


    /**
     * 发送短信验证码
     *
     * @param phoneNum
     */
    private void sendSmsCode(String phoneNum) {
        SendIdentifyCodeApi sendIdentifyCodeApi = new SendIdentifyCodeApi(httpOnNextListener, this);
        sendIdentifyCodeApi.setPhone(phoneNum);
        sendIdentifyCodeApi.setSmsType("2");
        sendIdentifyCodeApi.setAreaCode(tv_areaCode.getText().toString().trim());
        HttpManager manager = HttpManager.getInstance();
        manager.doHttpDeal(FindPWActivity.this, sendIdentifyCodeApi);
    }

    HttpOnNextListener<SendIdentifyCodeBean> httpOnNextListener = new HttpOnNextListener<SendIdentifyCodeBean>() {
        @Override
        public RetryWhenNetworkException.Wrapper onNext(SendIdentifyCodeBean sendIdentifyCodeBean) {
            if (200 == sendIdentifyCodeBean.getErrorCode()) {
                SPUtil.putString(FindPWActivity.this, Constants.AREA_CODE, tv_areaCode.getText().toString().trim());
                Intent intent = new Intent(FindPWActivity.this, VerifyActivity.class);
                intent.putExtra("classType", "2");
                intent.putExtra("phone", et_phoneNum.getText().toString().trim());
                startActivity(intent);
                finish();
            } else {
                CustomToast.showToast(sendIdentifyCodeBean.getErrorMsg());
                refreshCode();
            }

            return null;
        }
    };

    //刷新验证码
    private void refreshCode() {
        bitmap = codeUtil.createBitmap();
        ib_code.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10086) {
            areaCode = data.getStringExtra("areaCode");
            tv_areaCode.setText(areaCode);
        }
    }
}
