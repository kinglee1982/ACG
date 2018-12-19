package com.kcx.acg.views.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.api.ResetPWApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.ResetPasswordBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.StringUtil;
import com.kcx.acg.views.view.CustomToast;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 找回密码设置界面
 * Created by jb on 2018/8/31.
 */
public class FindPWSetActivity extends BaseActivity implements CancelAdapt {
    private String phone, guid, newPWStr, newPWAgentStr;
    private TextView tv_title, tv_hint;
    private Button  btn_completeSet;
    private EditText et_setNewPW, et_setNewPWAgent;
    private LinearLayout ll_back;

    @Override
    public View setInitView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_find_set, null);
        return view;
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.findPWSet_title);

        ll_back = findViewById(R.id.ll_back);
        et_setNewPW = findViewById(R.id.et_setNewPW);
        et_setNewPWAgent = findViewById(R.id.et_setNewPWAgent);

        tv_hint = findViewById(R.id.tv_hint);
        btn_completeSet = findViewById(R.id.btn_completeSet);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_completeSet.setOnClickListener(this);

        et_setNewPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                newPWStr = et_setNewPW.getText().toString().trim();
                newPWAgentStr = et_setNewPWAgent.getText().toString().trim();
                setBtnEnable(newPWStr, newPWAgentStr);
            }
        });

        et_setNewPWAgent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                newPWStr = et_setNewPW.getText().toString().trim();
                newPWAgentStr = et_setNewPWAgent.getText().toString().trim();
                setBtnEnable(newPWStr, newPWAgentStr);
            }
        });


    }

    @Override
    public void initData() {
        super.initData();
        phone = getIntent().getStringExtra("phone");
        guid = getIntent().getStringExtra("guid");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_completeSet://完成设置
                newPWStr = et_setNewPW.getText().toString().trim();
                newPWAgentStr = et_setNewPWAgent.getText().toString().trim();
                if (!StringUtil.regexText(newPWAgentStr)) {
                    CustomToast.showToast(getString(R.string.registerSet_errorStr));
                    break;
                }
                if (newPWStr.equals(newPWAgentStr)) {
                    resetPW(newPWAgentStr,phone,guid);

                } else {
                    tv_hint.setVisibility(View.VISIBLE);
                }

                break;
        }
    }



    private void setBtnEnable(String newPW1, String newPW2) {
        if (!TextUtils.isEmpty(newPW1) && !TextUtils.isEmpty(newPW2)) {
            btn_completeSet.setEnabled(true);
            btn_completeSet.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
        } else {
            btn_completeSet.setEnabled(false);
            btn_completeSet.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
        }
    }

    /**
     * 重置密码
     * @param newPWAgentStr
     * @param phone
     * @param guid
     */
    private void resetPW(String newPWAgentStr, String phone, String guid) {
        ResetPWApi resetPWApi = new ResetPWApi(new HttpOnNextListener<ResetPasswordBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(ResetPasswordBean resetPasswordBean) {
                if (200==resetPasswordBean.getErrorCode()){
                    CustomToast.showToast( resetPasswordBean.getErrorMsg());
                    startActivity(new Intent(FindPWSetActivity.this,LoginActivity.class));
                    finish();

                }else {
                    CustomToast.showToast( resetPasswordBean.getErrorMsg());
                }
                return null;
            }
        }, this);
        resetPWApi.setPhone(phone);
        resetPWApi.setPwd(newPWAgentStr);
        resetPWApi.setGuid(guid);
        resetPWApi.setAreaCode(SPUtil.getString(FindPWSetActivity.this, Constants.AREA_CODE,""));
        HttpManager httpManager = HttpManager.getInstance();
        httpManager.doHttpDeal(FindPWSetActivity.this,resetPWApi);

    }
}
