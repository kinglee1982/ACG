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
import com.kcx.acg.api.ModifyNickNameApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.ModifyNickNameBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.view.CustomToast;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 修改昵称界面
 * Created by jb on 2018/9/4.
 */
public class AlertNicknameActivity  extends BaseActivity implements CancelAdapt{
    private TextView tv_title,tv_right;
    private Button btn_clean;
    private LinearLayout ll_back;
    private EditText et_alert;
    private String nickname;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_alert_nickname,null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title =findViewById(R.id.tv_title);
        tv_title.setText(R.string.alertNickname_title);
        tv_right =findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(R.string.alertNickname_save);
        ll_back=findViewById(R.id.ll_back);
        btn_clean=findViewById(R.id.btn_clean);
        et_alert= findViewById(R.id.et_alert);
        //光标放置文本末尾
//        et_alert.setSelection(et_alert.getText().length());

        et_alert.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)){
                    btn_clean.setVisibility(View.INVISIBLE);
                }else{
                    btn_clean.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        nickname =getIntent().getStringExtra("nickname");
        et_alert.setText(nickname);
        if (TextUtils.isEmpty(nickname)){
            btn_clean.setVisibility(View.GONE);
        }else{
            btn_clean.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_clean.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_right:// 保存
                String content = et_alert.getText().toString().trim();
                if (TextUtils.isEmpty(content)){
                    CustomToast.showToast(getString(R.string.alertNickname_hint));
                }else {
                alertNickName(content);
                }
                break;
            case R.id.btn_clean://清理昵称
                et_alert.setText("");
                break;
        }

    }

    /**
     * 修改昵称接口
     * @param content
     */
    private void alertNickName(final String content) {
        ModifyNickNameApi modifyNickNameApi = new ModifyNickNameApi(new HttpOnNextListener<ModifyNickNameBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(ModifyNickNameBean modifyNickNameBean) {
                if (modifyNickNameBean.getErrorCode()==200){
                    Intent i = new Intent();
                    i.putExtra("strData",content);
                    setResult(10010,i);
                    finish();
                }else {
                    CustomToast.showToast( modifyNickNameBean.getErrorMsg());
                }

                return null;
            }
        }, this);
        modifyNickNameApi.setUserName(content);
        HttpManager.getInstance().doHttpDeal(AlertNicknameActivity.this,modifyNickNameApi);

    }
}
