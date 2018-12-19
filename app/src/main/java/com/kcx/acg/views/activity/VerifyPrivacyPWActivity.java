package com.kcx.acg.views.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.ToastUtil;
import com.kcx.acg.views.adapter.PrivacyPWAdapter;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 校验隐私密码
 * Created by jb on 2018/9/10.
 */
public class VerifyPrivacyPWActivity extends BaseActivity implements CancelAdapt {
    private String[] stringArra, pws;
    private String privacyPw;
    private Boolean havePrivacyPw;
    private PrivacyPWAdapter privacyPWAdapter;
    private RecyclerView mRecyclerView;
    private TextView tv_title, tv_hint;
    private EditText et_privacyPW1, et_privacyPW2, et_privacyPW3, et_privacyPW4;
    private View view_line_botto1, view_line_botto2, view_line_botto3, view_line_botto4;
    private int count = 0;
    private int chance = 5; //五次机会
    private String pwd, pw;


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
                if (TextUtils.isEmpty(s.toString())){
                    privacyPWAdapter.update(false);
                }else {
                    privacyPWAdapter.update(true);
                }

            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        stringArra = getResources().getStringArray(R.array.privacyPW);
        initRecyclerView(stringArra);

        pws = new String[4];
        privacyPw = SPUtil.getString(VerifyPrivacyPWActivity.this, Constants.PRIVACY_PW, "");
        tv_title.setText(R.string.setting_inputPrivacyPW);

    }

    @Override
    public void onClick(View v) {

    }

    /***
     * 初始列表
     */
    private void initRecyclerView(String[] stringArra) {
        GridLayoutManager mgr = new GridLayoutManager(VerifyPrivacyPWActivity.this, 3);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mgr);
        privacyPWAdapter = new PrivacyPWAdapter(VerifyPrivacyPWActivity.this, stringArra,1);
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
                    pws[0] = pwd;
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
                    pws[1] = pwd;
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
                    if (unDel) {
                        pws[2] = pwd;
                    }
                }
                et_privacyPW3.setText("*");
                view_line_botto3.setBackgroundResource(R.color.pink_hint);

                break;

            case 4:
                chance--;
                if (unDel) {
                    if (unDel) {
                        pws[3] = pwd;
                    }
                }

                pw = pws[0] + pws[1] + pws[2] + pws[3];
                LogUtil.e("隐私密码", "pw=" + pw + "        /      privacyPw=  " + privacyPw);
                if (pw.equals(privacyPw)) {
                    //密码一致
                    finish();
                } else {
                    if (chance > 0) {
                        count = 0;
                        String pwd = "";
                        et_privacyPW1.setText("");
                        view_line_botto1.setBackgroundResource(R.color.black_ccc);
                        et_privacyPW2.setText("");
                        view_line_botto2.setBackgroundResource(R.color.black_ccc);
                        et_privacyPW3.setText("");
                        view_line_botto3.setBackgroundResource(R.color.black_ccc);
                        tv_hint.setVisibility(View.VISIBLE);
                        tv_hint.setText(getString(R.string.setting_inputPrivacyPW_hint1) + chance + getString(R.string.setting_inputPrivacyPW_hint2));
                    }else {
                        startActivity(new Intent(VerifyPrivacyPWActivity.this,LoginActivity.class));
                        finish();
                    }
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
