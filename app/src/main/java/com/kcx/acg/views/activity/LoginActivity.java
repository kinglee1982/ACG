package com.kcx.acg.views.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.api.GetUserInfoApi;
import com.kcx.acg.api.GetUserTokenApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.GetUserTokenBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.RefreshUserInfoManager;
import com.kcx.acg.utils.AESUtil;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.Base64;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.view.CustomToast;

import org.greenrobot.eventbus.EventBus;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 登陆界面
 * Created by jb on 2018/8/30.
 */
public class LoginActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_register, tv_forgetPW, tv_areaCode;
    private ImageView ib_back;
    private Button btn_login;
    private EditText et_phoneNum, et_pw;
    private String phoneNum, pw;
    private String areaCode;
    public static final String KEY_GO_HOME = "key_go_home";

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_login, null);

    }

    @Override
    public void initView() {
        super.initView();
        btn_login = findViewById(R.id.btn_login);
        ib_back = findViewById(R.id.ib_back);
        tv_register = findViewById(R.id.tv_register);
        tv_forgetPW = findViewById(R.id.tv_forgetPW);
        et_phoneNum = findViewById(R.id.et_phoneNum);
        et_phoneNum.setText(SPUtil.getString(LoginActivity.this, Constants.ACCOUNT, ""));
        et_pw = findViewById(R.id.et_pw);
        tv_areaCode = findViewById(R.id.tv_areaCode);
        tv_areaCode.setText("86");

    }

    @Override
    public void setListener() {
        super.setListener();
        ib_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forgetPW.setOnClickListener(this);
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
                setBtnEnable();
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
                setBtnEnable();
            }
        });

    }

    private void setBtnEnable() {
        phoneNum = et_phoneNum.getText().toString().trim();
        pw = et_pw.getText().toString().trim();
        if (!TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(pw)) {
            btn_login.setEnabled(true);
            btn_login.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
        } else {
            btn_login.setEnabled(false);
            btn_login.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:  //返回
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btn_login:  //登录
                // 登陆逻辑
                phoneNum = et_phoneNum.getText().toString().trim();
                pw = et_pw.getText().toString().trim();
                try {
                    //AES加密（加密模式：ECB ，填充：pkcs7padding，数据块：128位，输出：base64 ,字符集：utf8）
                    String phoneAes = AESUtil.Encrypt(phoneNum, Constants.AES_SECRET_KEY);
                    String pwdAes = AESUtil.Encrypt(pw, Constants.AES_SECRET_KEY);
                    byte[] phoneData = phoneAes.getBytes("UTF-8");
                    byte[] pwdData = pwdAes.getBytes("UTF-8");
                    //base64再加密一次
                    getUserToken(Base64.encode(phoneData), Base64.encode(pwdData));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.tv_register:  //注册
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                i.putExtra("isRegister", true);
                startActivity(i);
                finish();
                break;

            case R.id.tv_forgetPW:  //忘记密码
                Intent intent = new Intent(LoginActivity.this, FindPWActivity.class);
                intent.putExtra("isRegister", false);
                startActivity(intent);
                break;
            case R.id.tv_areaCode: //区号选择
                Intent intent1 = new Intent(LoginActivity.this, ChooseDataListActivity.class);
                intent1.putExtra("areaCode", tv_areaCode.getText().toString().trim());
                startActivityForResult(intent1, 100);
                break;
        }
    }

    /**
     * 根据手机号和密码获取用户AccessToken（登录：先获取token，再根据token获取用户信息)
     *
     * @param phoneNum
     * @param pw
     */
    private void getUserToken(String phoneNum, String pw) throws Exception {
        GetUserTokenApi getUserTokenApi = new GetUserTokenApi(httpOnNextListener, this);
        getUserTokenApi.setPhone(phoneNum);
        getUserTokenApi.setPwd(pw);
        getUserTokenApi.setAreaCode(tv_areaCode.getText().toString().trim());
        HttpManager httpManager = HttpManager.getInstance();
        httpManager.doHttpDeal(LoginActivity.this, getUserTokenApi);
    }

    HttpOnNextListener<GetUserTokenBean> httpOnNextListener = new HttpOnNextListener<GetUserTokenBean>() {
        @Override
        public RetryWhenNetworkException.Wrapper onNext(GetUserTokenBean getUserTokenBean) {
            if (200 == getUserTokenBean.getErrorCode()) {
                RefreshUserInfoManager.getInstances().refreshUserInfo(LoginActivity.this);
                SPUtil.putString(LoginActivity.this, Constants.AREA_CODE, tv_areaCode.getText().toString().trim());
                SPUtil.putString(LoginActivity.this, Constants.ACCESS_TOKEN, getUserTokenBean.getReturnData().getAccessToken());
                SPUtil.putString(LoginActivity.this, Constants.ACCOUNT, phoneNum);
                refreshUserInfo();
            } else {
                CustomToast.showToast(getUserTokenBean.getErrorMsg());
            }
            return null;
        }
    };

    /**
     * 刷新用户信息
     */
    private void refreshUserInfo() {
        GetUserInfoApi getUserInfoApi = new GetUserInfoApi(new HttpOnNextListener<UserInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UserInfoBean userInfoBean) {
                finish();
                if (userInfoBean.getErrorCode() == 200) {
                    SPUtil.putString(LoginActivity.this, Constants.USER_INFO, AppUtil.getGson().toJson(userInfoBean));
                    if (getIntent().getBooleanExtra(KEY_GO_HOME, false)) {
                        startDDMActivity(MainActivity.class, true);
                    }

                    EventBus.getDefault().post(new BusEvent(BusEvent.LOGIN_SUCCESS, true));
                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                finish();
                return null;
            }
        }, this);
        HttpManager httpManager = HttpManager.getInstance();
        httpManager.doHttpDeal(LoginActivity.this, getUserInfoApi);
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
