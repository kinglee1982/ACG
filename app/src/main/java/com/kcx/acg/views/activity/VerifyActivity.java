package com.kcx.acg.views.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.api.SendIdentifyCodeApi;
import com.kcx.acg.api.ValidateIdentifyCodeApi;
import com.kcx.acg.api.WithdrawCashApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.SendIdentifyCodeBean;
import com.kcx.acg.bean.ValidateIdentifyCodeBean;
import com.kcx.acg.bean.WithdrawCashBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.CountTimer;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.StringUtil;
import com.kcx.acg.utils.ToastUtil;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 验证界面{注册/找回密码}
 * Created by jb on 2018/8/30.
 */
public class VerifyActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title, tv_hint, tv_phone;
    private Button btn_next, btn_timeHint;
    private EditText et_code1, et_code2, et_code3, et_code4;
    private LinearLayout ll_phoneHint, ll_back;

    private CountTimer countTimer;
    private String classType;//（1：注册 2：找回密码 3：提现）

    private String codeNum1, codeNum2, codeNum3, codeNum4;
    private String phoneNum;
    private int money, memberBankID;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_verify, null);
    }

    @Override
    public void initView() {
        super.initView();

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.verify_title);
        ll_back = findViewById(R.id.ll_back);
        btn_next = findViewById(R.id.btn_next);
        btn_timeHint = findViewById(R.id.btn_timeHint);
        et_code1 = findViewById(R.id.et_code1);
        et_code2 = findViewById(R.id.et_code2);
        et_code3 = findViewById(R.id.et_code3);
        et_code4 = findViewById(R.id.et_code4);
        tv_hint = findViewById(R.id.tv_hint);
        tv_phone = findViewById(R.id.tv_phone);
        ll_phoneHint = findViewById(R.id.ll_phoneHint);

        //倒计时
        countTimer = new CountTimer(VerifyActivity.this, 60000, 1000, btn_timeHint);
        countTimer.start();
        setFocusable(et_code1);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_timeHint.setOnClickListener(this);

        et_code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                codeNum1 = et_code1.getText().toString().trim();
                codeNum2 = et_code2.getText().toString().trim();
                codeNum3 = et_code3.getText().toString().trim();
                codeNum4 = et_code4.getText().toString().trim();
                setNexEnable(codeNum1, codeNum2, codeNum3, codeNum4);
                if (!TextUtils.isEmpty(codeNum1)) {
                    setFocusable(et_code2);
                }

            }
        });

        et_code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                codeNum1 = et_code1.getText().toString().trim();
                codeNum2 = et_code2.getText().toString().trim();
                codeNum3 = et_code3.getText().toString().trim();
                codeNum4 = et_code4.getText().toString().trim();
                setNexEnable(codeNum1, codeNum2, codeNum3, codeNum4);
                if (!TextUtils.isEmpty(codeNum1)) {
                    setFocusable(et_code3);
                }
            }
        });

        et_code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                codeNum1 = et_code1.getText().toString().trim();
                codeNum2 = et_code2.getText().toString().trim();
                codeNum3 = et_code3.getText().toString().trim();
                codeNum4 = et_code4.getText().toString().trim();
                setNexEnable(codeNum1, codeNum2, codeNum3, codeNum4);
                if (!TextUtils.isEmpty(codeNum1)) {
                    setFocusable(et_code4);
                }
            }
        });

        et_code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                codeNum1 = et_code1.getText().toString().trim();
                codeNum2 = et_code2.getText().toString().trim();
                codeNum3 = et_code3.getText().toString().trim();
                codeNum4 = et_code4.getText().toString().trim();
                setNexEnable(codeNum1, codeNum2, codeNum3, codeNum4);
            }
        });

        et_code4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_code4.setText("");
                            setFocusable(et_code3);
                        }
                    }, 50);
                }
                return false;
            }
        });

        et_code3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_code3.setText("");
                            setFocusable(et_code2);
                        }
                    }, 50);
                }
                return false;
            }
        });

        et_code2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_code2.setText("");
                            setFocusable(et_code1);
                        }
                    }, 50);
                }
                return false;
            }
        });

        et_code1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_code1.setText("");
                            setFocusable(et_code1);
                        }
                    }, 50);
                }
                return false;
            }
        });


    }


    @Override
    public void initData() {
        super.initData();
        phoneNum = getIntent().getStringExtra("phone");
        classType = getIntent().getStringExtra("classType");
        if ("3".equals(classType)) {
            ll_phoneHint.setVisibility(View.VISIBLE);
            money = getIntent().getIntExtra("money", 0);
            memberBankID = getIntent().getIntExtra("memberBankID", 0);
            tv_phone.setText(StringUtil.replaceStr(3, phoneNum.length(), phoneNum, "********") + getString(R.string.verify_toRegister));
            //            tv_phone.setText(phoneNum+getString(R.string.verify_toRegister));

        } else {
            ll_phoneHint.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:  //返回
                finish();
                break;

            case R.id.btn_next: //下一步
                String code = codeNum1 + codeNum2 + codeNum3 + codeNum4;
                    verify(code, phoneNum);

                break;

            case R.id.btn_timeHint: //重新发送验证码
                // 获取验证码逻辑
                sendSmsCode(phoneNum);
                countTimer.start();
                break;
        }
    }

    /**
     * 校验短信验证码
     *
     * @param code
     * @param phone
     */
    private void verify(String code, String phone) {
        ValidateIdentifyCodeApi validateIdentifyCodeApi = new ValidateIdentifyCodeApi(httpOnNextListener, this);
        validateIdentifyCodeApi.setCode(code);
        validateIdentifyCodeApi.setPhone(phone);
        validateIdentifyCodeApi.setAreaCode(SPUtil.getString(VerifyActivity.this, Constants.AREA_CODE, ""));
        HttpManager httpManager = HttpManager.getInstance();
        httpManager.doHttpDeal(VerifyActivity.this, validateIdentifyCodeApi);

    }

    HttpOnNextListener<ValidateIdentifyCodeBean> httpOnNextListener = new HttpOnNextListener<ValidateIdentifyCodeBean>() {
        @Override
        public RetryWhenNetworkException.Wrapper onNext(ValidateIdentifyCodeBean validateIdentifyCodeBean) {
            if (200 == validateIdentifyCodeBean.getErrorCode()) {
                if ("1".equals(classType)) {
                    //注册设置
                    Intent intent = new Intent(VerifyActivity.this, RegisterSetActivity.class);
                    intent.putExtra("phone", phoneNum);
                    intent.putExtra("guid", validateIdentifyCodeBean.getReturnData());
                    startActivity(intent);
                    finish();

                } else if ("2".equals(classType)) {
                    //找回密码设置
                    Intent intent = new Intent(VerifyActivity.this, FindPWSetActivity.class);
                    intent.putExtra("phone", phoneNum);
                    intent.putExtra("guid", validateIdentifyCodeBean.getReturnData());
                    startActivity(intent);
                    finish();
                } else if ("3".equals(classType)) {
                    //提现
                    withdrawCash(validateIdentifyCodeBean.getReturnData(), money, memberBankID);

                }

            } else {
                ToastUtil.showShort(VerifyActivity.this, validateIdentifyCodeBean.getErrorMsg());
                tv_hint.setVisibility(View.VISIBLE);
            }
            return null;
        }
    };

    /**
     * 会员提现
     *
     * @param returnData
     * @param money
     * @param memberBankID
     */
    private void withdrawCash(String returnData, int money, int memberBankID) {
        WithdrawCashApi withdrawCashApi = new WithdrawCashApi(this, new HttpOnNextListener<WithdrawCashBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(WithdrawCashBean withdrawCashBean) {
                if (withdrawCashBean.getErrorCode() == 200) {
                    startActivity(new Intent(VerifyActivity.this, WithdrawalStatusActivity.class));
                    finish();
                } else {
                    ToastUtil.showShort(VerifyActivity.this, withdrawCashBean.getErrorMsg());
                }
                return null;
            }
        });
        withdrawCashApi.setGuid(returnData);
        withdrawCashApi.setMemberBankID(memberBankID);
        withdrawCashApi.setMoney(money);
        HttpManager.getInstance().doHttpDeal(VerifyActivity.this, withdrawCashApi);

    }

    /**
     * 发送短信验证码
     *
     * @param phoneNum
     */
    private void sendSmsCode(String phoneNum) {
        SendIdentifyCodeApi sendIdentifyCodeApi = new SendIdentifyCodeApi(smsHttpOnNextListener, this);
        sendIdentifyCodeApi.setPhone(phoneNum);
        sendIdentifyCodeApi.setSmsType(classType);
        HttpManager manager = HttpManager.getInstance();
        manager.doHttpDeal(VerifyActivity.this, sendIdentifyCodeApi);
    }

    HttpOnNextListener<SendIdentifyCodeBean> smsHttpOnNextListener = new HttpOnNextListener<SendIdentifyCodeBean>() {
        @Override
        public RetryWhenNetworkException.Wrapper onNext(SendIdentifyCodeBean sendIdentifyCodeBean) {
            tv_hint.setVisibility(View.INVISIBLE);
            ToastUtil.showShort(VerifyActivity.this, sendIdentifyCodeBean.getErrorMsg());
            return null;
        }

    };


    //设置“下一步”状态
    private void setNexEnable(String codeNum1, String codeNum2, String codeNum3, String codeNum4) {
        if (!TextUtils.isEmpty(codeNum1) &&
                !TextUtils.isEmpty(codeNum2) &&
                !TextUtils.isEmpty(codeNum3) &&
                !TextUtils.isEmpty(codeNum4)) {
            btn_next.setEnabled(true);
            btn_next.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
        } else {
            btn_next.setEnabled(false);
            btn_next.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
        }
    }

    private void setFocusable(EditText et_pre) {
        et_pre.setFocusable(true);
        et_pre.setFocusableInTouchMode(true);
        et_pre.requestFocus();
        //强制打开软键盘
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countTimer.cancel();
    }
}
