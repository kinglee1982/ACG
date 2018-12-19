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
import com.kcx.acg.api.ModifyPWApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.ModifyPWBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.view.CustomToast;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 修改密码
 * Created by jb on 2018/9/10.
 */
public class EditPWActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title, tv_hint;
    private Button  btn_complete;
    private LinearLayout ll_back;
    private EditText et_oldPW, et_newPW, et_newPWAgent;
    private String oldPW, newPW, newPWAgent;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_edit_pw, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_hint = findViewById(R.id.tv_hint);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.setting_et_pw);
        ll_back = findViewById(R.id.ll_back);
        btn_complete = findViewById(R.id.btn_complete);
        et_oldPW = findViewById(R.id.et_oldPW);
        et_newPW = findViewById(R.id.et_newPW);
        et_newPWAgent = findViewById(R.id.et_newPWAgent);

    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_complete.setOnClickListener(this);

        et_oldPW.addTextChangedListener(new TextWatcher() {
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

        et_newPW.addTextChangedListener(new TextWatcher() {
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

        et_newPWAgent.addTextChangedListener(new TextWatcher() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.btn_complete:  //完成
                oldPW = et_oldPW.getText().toString().trim();
                newPW = et_newPW.getText().toString().trim();
                newPWAgent = et_newPWAgent.getText().toString().trim();
                if (newPW.equals(newPWAgent)) {
                    commitPW(oldPW, newPWAgent);
                } else {
                    tv_hint.setVisibility(View.VISIBLE);
                }

                break;
        }
    }


    private void setBtnEnable() {
        oldPW = et_oldPW.getText().toString().trim();
        newPW = et_newPW.getText().toString().trim();
        newPWAgent = et_newPWAgent.getText().toString().trim();
        if (!TextUtils.isEmpty(oldPW) && !TextUtils.isEmpty(newPW) && !TextUtils.isEmpty(newPWAgent)) {
            btn_complete.setEnabled(true);
            btn_complete.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
        } else {
            btn_complete.setEnabled(false);
            btn_complete.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
        }
    }

    /**
     * 修改密码接口
     *
     * @param oldPW
     * @param newPWAgent
     */
    private void commitPW(String oldPW, String newPWAgent) {
        ModifyPWApi modifyPWApi = new ModifyPWApi(new HttpOnNextListener<ModifyPWBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(ModifyPWBean modifyPWBean) {
                if (modifyPWBean.getErrorCode() == 200) {
                    startActivity(new Intent(EditPWActivity.this, LoginActivity.class));

                    if (SettingActivity.instance != null) {
                        SettingActivity.instance.finish();
                    }

                    finish();
                } else {
                    CustomToast.showToast(modifyPWBean.getErrorMsg());
                }

                return null;
            }
        }, this);
        modifyPWApi.setPwd(oldPW);
        modifyPWApi.setSurePwd(newPWAgent);
        HttpManager.getInstance().doHttpDeal(EditPWActivity.this, modifyPWApi);
    }


}
