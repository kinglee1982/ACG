package com.kcx.acg.views.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.BottomDialogUtil;
import com.kcx.acg.utils.CacheUtil;
import com.kcx.acg.utils.CommonUtils;
import com.kcx.acg.utils.DialogUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.utils.SystemUtil;
import com.kcx.acg.utils.ToastUtil;
import com.kcx.acg.views.view.CustomToast;
import com.meituan.android.walle.WalleChannelReader;

import org.greenrobot.eventbus.EventBus;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 设置界面
 * Created by jb on 2018/9/4.
 */
public class SettingActivity extends BaseActivity implements CancelAdapt {
    public static SettingActivity instance;
    private TextView tv_title, tv_privacyHint, tv_cache, tv_versionAndChannel,tv_about,currentLanguageTv;
    private Button btn_exit;
    private ImageButton ib_privacyPw;
    private LinearLayout ll_back;
    private LinearLayout languageLayout;
    private RelativeLayout rl_cleanCache, rl_shits, rl_alertPW, rl_editPrivacyPW, rl_about;
    private DialogUtil dialogUtil;
    private CacheUtil cacheUtil;
    private boolean havePrivacyPw;

    @Override
    protected void onResume() {
        super.onResume();
        havePrivacyPw = (boolean) SPUtil.get(SettingActivity.this, Constants.HAVE_PRIVACY_PW, false);

        if (!havePrivacyPw) {
            //未开启隐私密码
            tv_privacyHint.setVisibility(View.VISIBLE);
            rl_editPrivacyPW.setVisibility(View.GONE);
            ib_privacyPw.setBackgroundResource(R.drawable.checkbox_normal);
        } else {
            tv_privacyHint.setVisibility(View.GONE);
            rl_editPrivacyPW.setVisibility(View.VISIBLE);
            ib_privacyPw.setBackgroundResource(R.drawable.checkbox_selected);
        }
    }

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_setting, null);
    }

    @Override
    public void initView() {
        instance = this;
        super.initView();
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.setting_title);
        tv_privacyHint = findViewById(R.id.tv_privacyHint);
        tv_cache = findViewById(R.id.tv_cache);
        ll_back = findViewById(R.id.ll_back);
        btn_exit = findViewById(R.id.btn_exit);
        ib_privacyPw = findViewById(R.id.ib_privacyPw);
        rl_cleanCache = findViewById(R.id.rl_cleanCache);
        rl_shits = findViewById(R.id.rl_shits);
        rl_alertPW = findViewById(R.id.rl_alertPW);
        rl_editPrivacyPW = findViewById(R.id.rl_editPrivacyPW);
        rl_about = findViewById(R.id.rl_about);
        languageLayout = findViewById(R.id.settings_language_layout);
        tv_versionAndChannel = findViewById(R.id.tv_versionAndChannel);
        tv_about = findViewById(R.id.tv_about);
        tv_about.setText(getString(R.string.setting_about)+AppUtil.getAppName(SettingActivity.this));
        currentLanguageTv = findViewById(R.id.setting_current_language_tv);
        currentLanguageTv.setText(AccountManager.getInstances().getLanguage());




    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        rl_cleanCache.setOnClickListener(this);
        rl_shits.setOnClickListener(this);
        rl_alertPW.setOnClickListener(this);
        rl_editPrivacyPW.setOnClickListener(this);
        ib_privacyPw.setOnClickListener(this);
        languageLayout.setOnClickListener(this);
        rl_about.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        cacheUtil = CacheUtil.getInstance(this);
        tv_cache.setText(cacheUtil.getCacheSize());

        tv_versionAndChannel.setText(getString(R.string.setting_versionAndChannel) + CommonUtils.getVersionName(SysApplication.getContext()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_exit:  //退出登录
                SPUtil.putString(SettingActivity.this, Constants.ACCESS_TOKEN, "");
                SPUtil.putString(SettingActivity.this, Constants.USER_INFO, "");
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                EventBus.getDefault().post(new BusEvent(BusEvent.LOGIN_OUT, true));
                finish();
                break;
            case R.id.rl_cleanCache:  //清除缓存
                new BottomDialogUtil(SettingActivity.this,
                        R.layout.dialog_bottom,
                        getString(R.string.setting_dialog_title),
                        getString(R.string.setting_dialog_ok),
                        getString(R.string.setting_dialog_no),
                        new BottomDialogUtil.BottonDialogListener() {
                            @Override
                            public void onItemListener() {
                                setCacheSize();
                            }
                        });

                break;

            case R.id.rl_shits:  //我要吐槽
                startActivity(new Intent(SettingActivity.this, ShitsActivity.class));
                break;
            case R.id.rl_alertPW:  //修改登录密码
                startActivity(new Intent(SettingActivity.this, EditPWActivity.class));
                break;

            case R.id.rl_editPrivacyPW:  //修改隐私密码
                Intent intent = new Intent(SettingActivity.this, SetPrivacyPWActivity.class);
                intent.putExtra("classType", 1);
                startActivity(intent);
                break;
            case R.id.ib_privacyPw: //隐私密码开关
                havePrivacyPw = (boolean) SPUtil.get(SettingActivity.this, Constants.HAVE_PRIVACY_PW, false);
                if (!havePrivacyPw) {
                    //未开启隐私密码
                    tv_privacyHint.setVisibility(View.GONE);
                    rl_editPrivacyPW.setVisibility(View.VISIBLE);
                    ib_privacyPw.setBackgroundResource(R.drawable.checkbox_selected);
                    startActivity(new Intent(SettingActivity.this, SetPrivacyPWActivity.class));
                } else {
                    tv_privacyHint.setVisibility(View.VISIBLE);
                    rl_editPrivacyPW.setVisibility(View.GONE);
                    ib_privacyPw.setBackgroundResource(R.drawable.checkbox_normal);
                    SPUtil.putString(SettingActivity.this, Constants.PRIVACY_PW, "");
                    SPUtil.put(SettingActivity.this, Constants.HAVE_PRIVACY_PW, false);
                }
                break;
            case R.id.settings_language_layout:
                startDDMActivity(LanguageActivity.class, true);
                break;
            case R.id.rl_about:
                startDDMActivity(AboutActivity.class, true);
                break;
        }
    }

    //清除缓存
    private void setCacheSize() {
        cacheUtil.clearAppCache();
        tv_cache.postDelayed(new Runnable() {
            @Override
            public void run() {
                CustomToast.showToast(getString(R.string.setting_clean_success));
                tv_cache.setText(cacheUtil.getCacheSize());
            }
        }, 1000);
    }

}
