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
import com.kcx.acg.api.AddBankApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.AddBankBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.ButtonUtils;
import com.kcx.acg.views.view.CustomToast;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 添加银行卡
 * Created by jb on 2018/9/18.
 */
public class AddCardActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title, tv_chooseBank;
    private Button btn_withdraw;
    private LinearLayout ll_back;
    private EditText et_bankCard, et_name, et_lastName;
    private String bankName, cardNum, name, lastName;
    private int bankID;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_add_card, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.income_addBank);
        ll_back = findViewById(R.id.ll_back);
        tv_chooseBank = findViewById(R.id.tv_chooseBank);
        et_bankCard = findViewById(R.id.et_bankCard);
        et_name = findViewById(R.id.et_name);
        et_lastName = findViewById(R.id.et_lastName);
        btn_withdraw = findViewById(R.id.btn_withdraw);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        tv_chooseBank.setOnClickListener(this);
        btn_withdraw.setOnClickListener(this);


        tv_chooseBank.addTextChangedListener(new TextWatcher() {
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


        tv_chooseBank.addTextChangedListener(new TextWatcher() {
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

        et_bankCard.addTextChangedListener(new TextWatcher() {
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

        et_name.addTextChangedListener(new TextWatcher() {
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

        et_lastName.addTextChangedListener(new TextWatcher() {
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

            case R.id.tv_chooseBank:  //选择银行
                Intent intent = new Intent(AddCardActivity.this, BankListActivity.class);
                intent.putExtra("bank", tv_chooseBank.getText().toString().trim());
                startActivityForResult(intent, 10086);
                break;

            case R.id.btn_withdraw:  //添加银行
                // 添加银行逻辑
                cardNum = et_bankCard.getText().toString().trim();
                name = et_name.getText().toString().trim();
                lastName = et_lastName.getText().toString().trim();
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_withdraw)) {
                    addBank(bankID, cardNum, lastName, name);
                }
                break;
        }
    }


    private void setBtnEnable() {
        bankName = tv_chooseBank.getText().toString().trim();
        cardNum = et_bankCard.getText().toString().trim();
        name = et_name.getText().toString().trim();
        lastName = et_lastName.getText().toString().trim();
        if (!bankName.equals(getString(R.string.income_selectBank))
                && !TextUtils.isEmpty(cardNum)
                && !TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(lastName)) {
            btn_withdraw.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
            btn_withdraw.setEnabled(true);

        } else {
            btn_withdraw.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
            btn_withdraw.setEnabled(false);
        }
    }

    /**
     * 添加银行接口
     *
     * @param bankID
     * @param cardNum
     * @param lastName
     * @param name
     */
    private void addBank(int bankID, String cardNum, String lastName, String name) {
        AddBankApi addBankApi = new AddBankApi(this, new HttpOnNextListener<AddBankBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AddBankBean addBankBean) {
                if (addBankBean.getErrorCode() == 200) {
                    Intent i = new Intent();
                    i.putExtra("bankName", bankName);
                    setResult(10010, i);
                    finish();
                } else {
                    CustomToast.showToast(addBankBean.getErrorMsg());
                }

                return null;
            }
        });

        addBankApi.setBankID(bankID);
        addBankApi.setAccountNo(cardNum);
        addBankApi.setAccountName(name);
        addBankApi.setSurname(lastName);
        HttpManager.getInstance().doHttpDeal(AddCardActivity.this, addBankApi);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086 && resultCode == 10010) {
            bankID = data.getIntExtra("bankID", 0);
            tv_chooseBank.setText(data.getStringExtra("bankName"));
        }
    }


}
