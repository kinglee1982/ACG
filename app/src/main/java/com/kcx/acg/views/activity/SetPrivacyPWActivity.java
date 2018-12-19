package com.kcx.acg.views.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.ToastUtil;
import com.kcx.acg.views.adapter.PrivacyPWAdapter;
import com.kcx.acg.views.view.CustomToast;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 设置隐私密码
 * Created by jb on 2018/9/10.
 */
public class SetPrivacyPWActivity extends BaseActivity implements CancelAdapt {
    private String[] stringArra, pws1, pws2;
    private String privacyPw;
    private Boolean havePrivacyPw;
    private PrivacyPWAdapter privacyPWAdapter;
    private RecyclerView mRecyclerView;
    private TextView tv_title, tv_hint;
    private EditText et_privacyPW1, et_privacyPW2, et_privacyPW3, et_privacyPW4;
    private View view_line_botto1, view_line_botto2, view_line_botto3, view_line_botto4;
    private int count = 0;
    private String pwd, pw1, pw2;
    private boolean isfrist = true;
    private int classType;


    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_privacy_pw, null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        et_privacyPW1 = findViewById(R.id.et_privacyPW1);
        et_privacyPW2 = findViewById(R.id.et_privacyPW2);
        et_privacyPW3 = findViewById(R.id.et_privacyPW3);
        et_privacyPW4 = findViewById(R.id.et_privacyPW4);
        view_line_botto1 = findViewById(R.id.view_line_botto1);
        view_line_botto2 = findViewById(R.id.view_line_botto2);
        view_line_botto3 = findViewById(R.id.view_line_botto3);
        view_line_botto4 = findViewById(R.id.view_line_botto4);
        tv_hint = findViewById(R.id.tv_hint);
    }

    @Override
    public void setListener() {
        super.setListener();
        et_privacyPW1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)){
                    privacyPWAdapter.update(false);
                }else{
                    privacyPWAdapter.update(true);
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        classType = getIntent().getIntExtra("classType", 0);
        stringArra = getResources().getStringArray(R.array.privacyPW);
        initRecyclerView(stringArra);

        pws1 = new String[4];
        pws2 = new String[4];
        privacyPw = SPUtil.getString(SetPrivacyPWActivity.this, Constants.PRIVACY_PW, "");
        if (TextUtils.isEmpty(privacyPw)) {
            tv_title.setText(R.string.setting_setPrivacyPW);
        } else {
            tv_title.setText(R.string.setting_setNewPrivacyPW);
        }
    }

    @Override
    public void onClick(View v) {
    }

    /***
     * 初始列表
     */
    private void initRecyclerView(String[] stringArra) {
        GridLayoutManager mgr = new GridLayoutManager(SetPrivacyPWActivity.this, 3);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mgr);
        privacyPWAdapter = new PrivacyPWAdapter(SetPrivacyPWActivity.this, stringArra, 0);
        // 设置adapter
        mRecyclerView.setAdapter(privacyPWAdapter);

        privacyPWAdapter.setOnItemClickListener(new PrivacyPWAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 1:
                        count++;
                        if (count < 5) {
                            pwd = "1";
                        }
                        count(true);
                        break;
                    case 2:
                        count++;
                        if (count < 5) {
                            pwd = "2";
                        }
                        count(true);
                        break;
                    case 3:
                        count++;
                        if (count < 5) {
                            pwd = "3";
                        }
                        count(true);
                        break;
                    case 4:
                        count++;
                        if (count < 5) {
                            pwd = "4";
                        }
                        count(true);
                        break;
                    case 5:
                        count++;
                        if (count < 5) {
                            pwd = "5";
                        }
                        count(true);
                        break;
                    case 6:
                        count++;
                        if (count < 5) {
                            pwd = "6";
                        }
                        count(true);
                        break;

                    case 7:
                        count++;
                        if (count < 5) {
                            pwd = "7";
                        }
                        count(true);
                        break;

                    case 8:
                        count++;
                        if (count < 5) {
                            pwd = "8";
                        }
                        count(true);
                        break;
                    case 9:
                        count++;
                        if (count < 5) {
                            pwd = "9";
                        }
                        count(true);
                        break;

                    case 11:
                        count++;
                        if (count < 5) {
                            pwd = "0";
                        }
                        count(true);
                        break;
                    case 10:  //取消
                        finish();
                        break;
                    case 12:  //删除
                        if (count > 0) {
                            count--;
                        }
                        count(false);
                        break;
                }


            }
        });
    }

    private void count(boolean unDel) {
        if (count > 4) {
            count--;
            return;
        }
        switch (count) {
            case 0:
                et_privacyPW1.setText("");
                view_line_botto1.setBackgroundResource(R.color.black_ccc);
                et_privacyPW2.setText("");
                view_line_botto2.setBackgroundResource(R.color.black_ccc);
                et_privacyPW3.setText("");
                view_line_botto3.setBackgroundResource(R.color.black_ccc);
                et_privacyPW4.setText("");
                view_line_botto4.setBackgroundResource(R.color.black_ccc);
                break;
            case 1:
                if (unDel) {
                    if (isfrist) {
                        pws1[0] = pwd;
                    } else {
                        pws2[0] = pwd;
                    }
                }
                et_privacyPW1.setText("*");
                view_line_botto1.setBackgroundResource(R.color.pink_hint);
                et_privacyPW2.setText("");
                view_line_botto2.setBackgroundResource(R.color.black_ccc);
                et_privacyPW3.setText("");
                view_line_botto3.setBackgroundResource(R.color.black_ccc);
                et_privacyPW4.setText("");
                view_line_botto4.setBackgroundResource(R.color.black_ccc);
                break;

            case 2:
                if (unDel) {
                    if (isfrist) {
                        pws1[1] = pwd;
                    } else {
                        pws2[1] = pwd;
                    }
                }

                et_privacyPW2.setText("*");
                view_line_botto2.setBackgroundResource(R.color.pink_hint);
                et_privacyPW3.setText("");
                view_line_botto3.setBackgroundResource(R.color.black_ccc);
                et_privacyPW4.setText("");
                view_line_botto4.setBackgroundResource(R.color.black_ccc);
                break;

            case 3:
                if (unDel) {
                    if (isfrist) {
                        pws1[2] = pwd;
                    } else {
                        pws2[2] = pwd;
                    }
                }
                et_privacyPW3.setText("*");
                view_line_botto3.setBackgroundResource(R.color.pink_hint);
                break;

            case 4:
                if (unDel) {
                    if (isfrist) {
                        pws1[3] = pwd;
                    } else {
                        pws2[3] = pwd;
                    }
                }
                if (isfrist) {
                    et_privacyPW1.setText("");
                    view_line_botto1.setBackgroundResource(R.color.black_ccc);
                    et_privacyPW2.setText("");
                    view_line_botto2.setBackgroundResource(R.color.black_ccc);
                    et_privacyPW3.setText("");
                    view_line_botto3.setBackgroundResource(R.color.black_ccc);
                    isfrist = false;
                    count = 0;
                    tv_title.setText(R.string.setting_setAgentPrivacyPW);
                    break;
                }
                pw1 = pws1[0] + pws1[1] + pws1[2] + pws1[3];
                pw2 = pws2[0] + pws2[1] + pws2[2] + pws2[3];
                LogUtil.e("隐私密码", "pw1=" + pw1 + "        /        pw2" + pw2);
                if (pw1.equals(pw2)) {
                    //两次密码一致
                    if (classType == 1) {
                        CustomToast.showToast(getString(R.string.setting_setPrivacyPW_success));
                    }
                    SPUtil.putString(SetPrivacyPWActivity.this, Constants.PRIVACY_PW, pw2);
                    SPUtil.put(SetPrivacyPWActivity.this, Constants.HAVE_PRIVACY_PW, true);
                    finish();
                } else {
                    count = 0;
                    String pwd = "";
                    et_privacyPW1.setText("");
                    view_line_botto1.setBackgroundResource(R.color.black_ccc);
                    et_privacyPW2.setText("");
                    view_line_botto2.setBackgroundResource(R.color.black_ccc);
                    et_privacyPW3.setText("");
                    view_line_botto3.setBackgroundResource(R.color.black_ccc);
                    tv_hint.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }
}
