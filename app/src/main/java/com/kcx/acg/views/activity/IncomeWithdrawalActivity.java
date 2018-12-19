package com.kcx.acg.views.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.api.DeleteBankApi;
import com.kcx.acg.api.GetH5LinkUrlApi;
import com.kcx.acg.api.SelectMemberBankApi;
import com.kcx.acg.api.SendIdentifyCodeApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.DeleteBankBean;
import com.kcx.acg.bean.H5LinkUrlBean;
import com.kcx.acg.bean.SelectMemberBankBean;
import com.kcx.acg.bean.SendIdentifyCodeBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.H5LinkUrlManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.BottomDialogUtil;
import com.kcx.acg.utils.BottomDialogUtil_withdrawal;
import com.kcx.acg.utils.ButtonUtils;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.StringUtil;
import com.kcx.acg.utils.ToastUtil;
import com.kcx.acg.views.view.CustomToast;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 收益提现
 * Created by jb on 2018/9/18.
 */
public class IncomeWithdrawalActivity extends BaseActivity implements CancelAdapt {
    private static final int MSG_CODE = 0;
    private TextView tv_title, tv_bank, tv_totalIncome, tv_maxWithdrawal, tv_withdraw_all, tv_hint, tv_explain;
    private Button btn_withdraw;
    private RelativeLayout rl_selectBank;
    private EditText et_money;
    private LinearLayout ll_hint, ll_back;

    private String totalIncome;
    private List<SelectMemberBankBean.ReturnDataBean> selectMemberBankList;
    private BottomDialogUtil_withdrawal bottomDialogUtil_withdrawal;
    private int memberBankID;
    private String money;

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CODE:
                    money = et_money.getText().toString().trim();
                    if (!TextUtils.isEmpty(money)) {
                        int newMoney = Integer.parseInt(money);
                        if (newMoney < 100) { //余额不足，无法体现
                            ll_hint.setVisibility(View.GONE);
                            tv_hint.setVisibility(View.VISIBLE);
                            tv_hint.setText(getString(R.string.bottomDialogUtil_Pay_less) + "￥100.00" + getString(R.string.income_no));
                            tv_hint.setTextColor(getResources().getColor(R.color.black_999));
                            btn_withdraw.setEnabled(false);
                            btn_withdraw.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
                        } else if (newMoney > StringUtil.roundNumbers(Double.parseDouble(totalIncome))) {  //余额超出可体现金额
                            ll_hint.setVisibility(View.GONE);
                            tv_hint.setVisibility(View.VISIBLE);
                            tv_hint.setText(getString(R.string.income_hint1));
                            tv_hint.setTextColor(getResources().getColor(R.color.pink_hint));
                            btn_withdraw.setEnabled(false);
                            btn_withdraw.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
                        } else if (newMoney % 100 != 0) {  //输入金额需为100的整数倍，最小100
                            ll_hint.setVisibility(View.GONE);
                            tv_hint.setVisibility(View.VISIBLE);
                            tv_hint.setText(getString(R.string.income_hint2) + "100" + getString(R.string.income_hint3) + "100");
                            tv_hint.setTextColor(getResources().getColor(R.color.pink_hint));
                            btn_withdraw.setEnabled(false);
                            btn_withdraw.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
                        } else {
                            ll_hint.setVisibility(View.GONE);
                            tv_hint.setVisibility(View.VISIBLE);
                            int iMoney = newMoney / 100;
                            tv_hint.setText(getString(R.string.income_hint4) + "￥" + StringUtil.toDecimalFormat(String.valueOf(iMoney * 9)) + getString(R.string.income_hint5) + "（" + getString(R.string.income_hint6) + "费率9%）");
                            tv_hint.setTextColor(getResources().getColor(R.color.black_999));
                            if (!getString(R.string.income_addBank).equals(tv_bank.getText().toString().trim())) {
                                btn_withdraw.setEnabled(true);
                                btn_withdraw.setBackgroundResource(R.drawable.shape_pink_bg_5dp);
                            } else {
                                btn_withdraw.setEnabled(false);
                                btn_withdraw.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
                            }
                        }
                    } else {
                        ll_hint.setVisibility(View.VISIBLE);
                        tv_hint.setVisibility(View.GONE);
                        btn_withdraw.setEnabled(false);
                        btn_withdraw.setBackgroundResource(R.drawable.shape_pink_bg_undefined_5dp);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_income_withdrawal, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.income_btn);
        ll_back = findViewById(R.id.ll_back);
        btn_withdraw = findViewById(R.id.btn_withdraw);

        rl_selectBank = findViewById(R.id.rl_selectBank);
        tv_bank = findViewById(R.id.tv_bank);
        btn_withdraw = findViewById(R.id.btn_withdraw);
        tv_totalIncome = findViewById(R.id.tv_totalIncome);
        tv_maxWithdrawal = findViewById(R.id.tv_maxWithdrawal);
        tv_withdraw_all = findViewById(R.id.tv_withdraw_all);
        ll_hint = findViewById(R.id.ll_hint);
        tv_hint = findViewById(R.id.tv_hint);
        et_money = findViewById(R.id.et_money);
        tv_explain = findViewById(R.id.tv_explain);


    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        rl_selectBank.setOnClickListener(this);
        btn_withdraw.setOnClickListener(this);
        tv_withdraw_all.setOnClickListener(this);
        tv_explain.setOnClickListener(this);

        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et_money.setSelection(s.toString().length());//设置光标在文本后面
                Message msg = new Message();
                msg.what = MSG_CODE;
                myHandler.sendMessageDelayed(msg, 500);

            }
        });

        tv_bank.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Message msg = new Message();
                msg.what = MSG_CODE;
                myHandler.sendMessageDelayed(msg, 500);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        selectMemberBankList = new ArrayList<SelectMemberBankBean.ReturnDataBean>();
        totalIncome = getIntent().getStringExtra("totalIncome");
        tv_totalIncome.setText("￥" + totalIncome);
        tv_maxWithdrawal.setText("￥" + StringUtil.toDecimalFormat(StringUtil.roundNumbers(Double.parseDouble(totalIncome))));
        if (Double.parseDouble(totalIncome) < 100) {
            ll_hint.setVisibility(View.GONE);
            tv_hint.setVisibility(View.VISIBLE);
            tv_hint.setText(getString(R.string.bottomDialogUtil_Pay_less) + "￥100.00" + getString(R.string.income_no));
            tv_hint.setTextColor(getResources().getColor(R.color.black_999));
        }
        selectMemberBank();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_explain: //提现说明
                H5LinkUrlManager.getInstances().getH5LinkUrl(IncomeWithdrawalActivity.this, 6);
                break;
            case R.id.rl_selectBank:  //选择银行or添加银行卡
                if (getString(R.string.income_addBank).equals(tv_bank.getText())) {
                    Intent i = new Intent(IncomeWithdrawalActivity.this, AddCardActivity.class);
                    startActivityForResult(i, 10086);
                } else {
                    bottomDialogUtil_withdrawal = new BottomDialogUtil_withdrawal(IncomeWithdrawalActivity.this,
                            R.layout.dialog_withdrawal,
                            0.4,
                            selectMemberBankList,
                            new BottomDialogUtil_withdrawal.BottonDialogListener() {
                                @Override
                                public void onItemListener(int i) {
                                    tv_bank.setText(selectMemberBankList.get(i).getBankName());
                                }

                                @Override
                                public void onDelItemListener(final int i) {

                                    new BottomDialogUtil(IncomeWithdrawalActivity.this,
                                            R.layout.dialog_bottom,
                                            getString(R.string.income_del_bank),
                                            selectMemberBankList.get(i).getBankName() + " (" + selectMemberBankList.get(i).getAccountNo() + ")",
                                            getString(R.string.confirm_msg),
                                            getString(R.string.cancel_msg),
                                            new BottomDialogUtil.BottonDialogListener() {
                                                @Override
                                                public void onItemListener() {
                                                    deleteBank(selectMemberBankList.get(i).getMemberBankID(), i);
                                                }
                                            });


                                }

                                @Override
                                public void onAddBankListener() {
                                    Intent i = new Intent(IncomeWithdrawalActivity.this, AddCardActivity.class);
                                    startActivityForResult(i, 10086);
                                }
                            }
                    );
                }

                break;


            case R.id.btn_withdraw: //提现
                String userInfo = SPUtil.getString(IncomeWithdrawalActivity.this, Constants.USER_INFO, "");
                UserInfoBean userInfoBean = AppUtil.getGson().fromJson(userInfo, UserInfoBean.class);
                if (!ButtonUtils.isFastDoubleClick(R.id.btn_withdraw)) {
                    sendSmsCode(userInfoBean.getReturnData().getPhone());
                }
                break;

            case R.id.tv_withdraw_all: //全部提取
                et_money.setText(String.valueOf(StringUtil.roundNumbers(Double.parseDouble(totalIncome))));
                break;
        }
    }


    /**
     * 删除银行接口
     *
     * @param memberBankID
     */
    private void deleteBank(int memberBankID, final int position) {

        DeleteBankApi deleteBankApi = new DeleteBankApi(this, new HttpOnNextListener<DeleteBankBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(DeleteBankBean deleteBankBean) {
                if (deleteBankBean.getErrorCode() == 200) {
                    selectMemberBankList.remove(position);
                    bottomDialogUtil_withdrawal.upData(selectMemberBankList);
                    if (selectMemberBankList.size() != 0) {
                        //有银行卡
                        tv_bank.setText(selectMemberBankList.get(0).getBankName());
                    } else {
                        tv_bank.setText(R.string.income_addBank);
                    }
                } else {
                    ToastUtil.showShort(IncomeWithdrawalActivity.this, deleteBankBean.getErrorMsg());
                }

                return null;
            }
        });
        deleteBankApi.setMemberBankID(memberBankID);
        HttpManager.getInstance().doHttpDeal(IncomeWithdrawalActivity.this, deleteBankApi);


    }

    /**
     * 选择提现银行卡接口
     */
    private void selectMemberBank() {
        SelectMemberBankApi selectMemberBankApi = new SelectMemberBankApi(this, new HttpOnNextListener<SelectMemberBankBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(SelectMemberBankBean selectMemberBankBean) {

                if (selectMemberBankBean.getErrorCode() == 200) {
                    selectMemberBankList = selectMemberBankBean.getReturnData();
                    if (selectMemberBankList != null && selectMemberBankList.size() != 0) {
                        //有银行卡
                        tv_bank.setText(selectMemberBankList.get(0).getBankName());
                    }
                } else {
                    CustomToast.showToast(selectMemberBankBean.getErrorMsg());
                }
                return null;
            }
        });
        HttpManager.getInstance().doHttpDeal(IncomeWithdrawalActivity.this, selectMemberBankApi);
    }

    /**
     * 发送短信验证码
     *
     * @param phoneNum
     */
    private void sendSmsCode(final String phoneNum) {
        SendIdentifyCodeApi sendIdentifyCodeApi = new SendIdentifyCodeApi(new HttpOnNextListener<SendIdentifyCodeBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(SendIdentifyCodeBean sendIdentifyCodeBean) {
                if (sendIdentifyCodeBean.getErrorCode() == 200) {
                    for (int i = 0; i < selectMemberBankList.size(); i++) {
                        if (tv_bank.getText().toString().trim().equals(selectMemberBankList.get(i).getBankName())) {
                            memberBankID = selectMemberBankList.get(i).getMemberBankID();
                        }
                    }

                    Intent intent = new Intent(IncomeWithdrawalActivity.this, VerifyActivity.class);
                    intent.putExtra("classType", "3");
                    intent.putExtra("phone", phoneNum);
                    intent.putExtra("money", Integer.parseInt(et_money.getText().toString().trim()));
                    intent.putExtra("memberBankID", memberBankID);
                    startActivity(intent);
                    finish();
                } else {
                    CustomToast.showToast(sendIdentifyCodeBean.getErrorMsg());
                }
                return null;
            }
        }, this);
        sendIdentifyCodeApi.setPhone(phoneNum);
        sendIdentifyCodeApi.setSmsType("3");
        sendIdentifyCodeApi.setAreaCode(SPUtil.getString(IncomeWithdrawalActivity.this, Constants.AREA_CODE, ""));
        HttpManager manager = HttpManager.getInstance();
        manager.doHttpDeal(IncomeWithdrawalActivity.this, sendIdentifyCodeApi);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10010) {  //添加银行返回
            tv_bank.setText(data.getStringExtra("bankName"));
            selectMemberBank();
        }

    }

}
