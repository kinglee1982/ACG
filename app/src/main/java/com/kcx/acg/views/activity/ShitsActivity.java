package com.kcx.acg.views.activity;

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
import com.kcx.acg.api.AddSuggesttionApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.CommonBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.view.CustomToast;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 吐槽界面
 * Created by jb on 2018/9/10.
 */
public class ShitsActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title;
    private Button btn_send;
    private EditText et_shits;
    private LinearLayout ll_back;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_shits, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.setting_shits);
        ll_back = findViewById(R.id.ll_back);
        btn_send = findViewById(R.id.btn_send);
        et_shits = findViewById(R.id.et_shits);

    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_send.setOnClickListener(this);

        et_shits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    btn_send.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
                    btn_send.setEnabled(false);
                } else {
                    btn_send.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
                    btn_send.setEnabled(true);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.btn_send:  //发送
                    addSuggesttion(et_shits.getText().toString().trim());
                break;
        }
    }

    private void addSuggesttion(String ss) {
        AddSuggesttionApi addSuggesttionApi = new AddSuggesttionApi(this, new HttpOnNextListener<CommonBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(CommonBean commonBean) {
                if (commonBean.getErrorCode() == 200) {
                    CustomToast.showToast(getText(R.string.setting_shits_result));
                    finish();
                } else {
                    CustomToast.showToast(commonBean.getErrorMsg());
                }

                return null;
            }
        });

        addSuggesttionApi.setContent(ss);
        HttpManager.getInstance().doHttpDeal(ShitsActivity.this, addSuggesttionApi);

    }
}
