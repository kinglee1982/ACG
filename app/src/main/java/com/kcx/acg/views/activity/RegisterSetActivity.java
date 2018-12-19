package com.kcx.acg.views.activity;

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
import com.kcx.acg.api.RegisterUserApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.RegisterUserBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.StringUtil;
import com.kcx.acg.views.view.CustomToast;

import org.greenrobot.eventbus.EventBus;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 注册设置界面
 * Created by jb on 2018/8/31.
 */
public class RegisterSetActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title, tv_hint;
    private Button btn_completeRegister;
    private ImageButton ib_clean;
    private EditText et_setPW, et_setPWAgent, et_nickname, et_invitationCode;
    private LinearLayout ll_back;
    private String phoneNum, guid, invitationCode, androidID, setPW, setPWAgent, nickname;

    @Override
    public View setInitView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_register_set, null);
        return view;
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.registerSet_title);

        ll_back = findViewById(R.id.ll_back);
        et_setPW = findViewById(R.id.et_setPW);
        et_setPWAgent = findViewById(R.id.et_setPWAgent);
        et_nickname = findViewById(R.id.et_nickname);
        et_invitationCode = findViewById(R.id.et_invitationCode);
        tv_hint = findViewById(R.id.tv_hint);
        btn_completeRegister = findViewById(R.id.btn_completeRegister);
        ib_clean = findViewById(R.id.ib_clean);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_completeRegister.setOnClickListener(this);
        ib_clean.setOnClickListener(this);

        et_setPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setPW = et_setPW.getText().toString().trim();
                setPWAgent = et_setPWAgent.getText().toString().trim();
                nickname = et_nickname.getText().toString().trim();
                setBtnEnable(setPW, setPWAgent, nickname);

            }
        });

        et_setPWAgent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setPW = et_setPW.getText().toString().trim();
                setPWAgent = et_setPWAgent.getText().toString().trim();
                nickname = et_nickname.getText().toString().trim();
                setBtnEnable(setPW, setPWAgent, nickname);

            }
        });

        et_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setPW = et_setPW.getText().toString().trim();
                setPWAgent = et_setPWAgent.getText().toString().trim();
                nickname = et_nickname.getText().toString().trim();
                setBtnEnable(setPW, setPWAgent, nickname);

            }
        });

        et_invitationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if (TextUtils.isEmpty(s)){
                   ib_clean.setVisibility(View.INVISIBLE);
               }else {
                   ib_clean.setVisibility(View.VISIBLE);
               }
            }
        });




    }


    @Override
    public void initData() {
        super.initData();
        phoneNum = getIntent().getStringExtra("phone");
        guid = getIntent().getStringExtra("guid");
        androidID = AppUtil.getAndroidID(RegisterSetActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ib_clean: //清除邀请码
                et_invitationCode.setText("");
                break;
            case R.id.btn_completeRegister://完成注册
                setPW = et_setPW.getText().toString().trim();
                setPWAgent = et_setPWAgent.getText().toString().trim();
                if (!StringUtil.regexText(setPW)) {
                    CustomToast.showToast(getString(R.string.registerSet_errorStr));
                    break;
                } else if (!setPW.equals(setPWAgent)) {
                    tv_hint.setVisibility(View.VISIBLE);
                    break;
                }
                invitationCode = et_invitationCode.getText().toString().trim();
                nickname = et_nickname.getText().toString().trim();
                registerUser(phoneNum, guid, androidID, setPWAgent, nickname, invitationCode);
                break;
        }
    }


    private void setBtnEnable(String setPW, String setPWAgent, String nickname) {
        if (!TextUtils.isEmpty(setPW) && !TextUtils.isEmpty(setPWAgent) && !TextUtils.isEmpty(nickname)) {
            btn_completeRegister.setEnabled(true);
            btn_completeRegister.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
        } else {
            btn_completeRegister.setEnabled(false);
            btn_completeRegister.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
        }

    }

    /**
     * 注册接口
     *
     * @param phoneNum
     * @param guid
     * @param androidID
     * @param setPWAgent
     * @param nickname
     * @param invitationCode
     */
    private void registerUser(String phoneNum, String guid, String androidID, String setPWAgent, String nickname, String invitationCode) {
        RegisterUserApi registerUserApi = new RegisterUserApi(httpOnNextListener, this);
        registerUserApi.setDeviceID(androidID);
        registerUserApi.setGuid(guid);
        registerUserApi.setInviteCode(invitationCode);
        registerUserApi.setPhone(phoneNum);
        registerUserApi.setUserName(nickname);
        registerUserApi.setPwd(setPWAgent);
        registerUserApi.setAreaCode(SPUtil.getString(RegisterSetActivity.this, Constants.AREA_CODE, ""));
        HttpManager httpManager = HttpManager.getInstance();
        httpManager.doHttpDeal(RegisterSetActivity.this, registerUserApi);
    }

    HttpOnNextListener<RegisterUserBean> httpOnNextListener = new HttpOnNextListener<RegisterUserBean>() {
        @Override
        public RetryWhenNetworkException.Wrapper onNext(RegisterUserBean registerUserBean) {
//            CustomToast.showToast(getString(R.string.registerSet_registerStr));
            if (registerUserBean.getErrorCode() == 200) {
                finish();
                SPUtil.put(RegisterSetActivity.this, Constants.IS_REGISTER, true);
                SPUtil.putString(RegisterSetActivity.this, Constants.ACCESS_TOKEN, registerUserBean.getReturnData().getAccessToken());
                EventBus.getDefault().post(new BusEvent(BusEvent.REGISTER_SUCCESS, true));
            } else {
                CustomToast.showToast(registerUserBean.getErrorMsg());
            }

            return null;
        }
    };
}
