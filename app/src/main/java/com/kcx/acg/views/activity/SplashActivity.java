package com.kcx.acg.views.activity;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.apkfuns.logutils.LogUtils;
import com.kcx.acg.BuildConfig;
import com.kcx.acg.R;
import com.kcx.acg.api.AmazonS3ConfigApi;
import com.kcx.acg.api.MySettingApi;
import com.kcx.acg.aws.AWSUtil;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.AmazonS3ConfigBean;
import com.kcx.acg.bean.MySettingBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.manager.SettingManager;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.EnvironmentDialog;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.utils.SPUtil;

import java.util.List;
import java.util.Locale;

import me.jessyan.autosize.internal.CancelAdapt;


/**
 * 启动页
 * Created by jb on 2018/8/30.
 */
public class SplashActivity extends BaseActivity implements CancelAdapt {

    private View rootView;
    private ImageView mImageView;
    private AnimationSet animationSet;
    private RelativeLayout splashLayout;
    private EnvironmentDialog environmentDialog;
    private TransferUtility transferUtility;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_splash, null);
        mImageView = rootView.findViewById(R.id.mImageView);
        splashLayout = rootView.findViewById(R.id.splash_layout);
        return rootView;
    }

    @Override
    public void initData() {
        super.initData();
        Constants.isShowDialog = true;
        if (BuildConfig.DEBUG) {
            initEnvironment();
        } else {
            init();
        }
        transferUtility = AWSUtil.getTransferUtility(this);
        List<TransferObserver> observers = transferUtility.getTransfersWithType(TransferType.UPLOAD);
        for (TransferObserver observer : observers) {
            transferUtility.deleteTransferRecord(observer.getId());
        }
    }

    //初始化测试环境
    private void initEnvironment() {
        environmentDialog = new EnvironmentDialog(SplashActivity.this, R.style.TopDialog_Animation, R.layout.dialog_environment, new EnvironmentDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog) {
                init();
            }
        });
        environmentDialog.setCancelable(false);
        environmentDialog.setCanceledOnTouchOutside(false);
        environmentDialog.show();
    }

    private void init() {
        getLanguage();
        mySetting();
        getAmazonS3Config();
        // 从无到有
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        animationSet = new AnimationSet(false);// 是否共享加速器
        animationSet.addAnimation(alphaAnimation);
        // 启动动画集
        splashLayout.startAnimation(animationSet);
        animationSet.setAnimationListener(new MyAnimationListener());
    }

    private void getAmazonS3Config() {
        AmazonS3ConfigApi amazonS3ConfigApi = new AmazonS3ConfigApi(this);
        amazonS3ConfigApi.setListener(new HttpOnNextListener<AmazonS3ConfigBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AmazonS3ConfigBean amazonS3ConfigBean) {
                if (amazonS3ConfigBean.getErrorCode() == 200) {
                    try {
                        String accessKey = AppUtil.decrypt(amazonS3ConfigBean.getReturnData().getAccessKey());
                        String secretKey = AppUtil.decrypt(amazonS3ConfigBean.getReturnData().getSecretKey());
                        AmazonS3ConfigBean.ReturnDataBean returnData = new AmazonS3ConfigBean.ReturnDataBean();
                        returnData.setAccessKey(accessKey);
                        returnData.setSecretKey(secretKey);
                        returnData.setEndpointURL(amazonS3ConfigBean.getReturnData().getEndpointURL());
                        returnData.setEndpoint(amazonS3ConfigBean.getReturnData().getEndpoint());
                        returnData.setConfigName(amazonS3ConfigBean.getReturnData().getConfigName());
                        returnData.setBucketName(amazonS3ConfigBean.getReturnData().getBucketName());

                        AccountManager.getInstances().setAmazonS3Config(returnData);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });

        HttpManager.getInstance().doHttpDeal(this, amazonS3ConfigApi);

    }

    @Override
    public void onClick(View view) {

    }

    public void getLanguage() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String curLanguage;
        if (language.equals("en")) {
            curLanguage = getString(R.string.setting_language_en_us);
            language = language + "_US";
        } else if (language.equals("zh")) {
            String country = locale.getCountry();
            if (country.equals("HK") || country.equals("TW") || country.equals("MO")) {
                language = language + "_TW";
                curLanguage = getString(R.string.setting_language_zh_tw);
            } else {
                language = language + "_CN";
                curLanguage = getString(R.string.setting_language_zh_cn);
            }
        } else if (language.equals("ko")) {
            language = language + "_KR";
            curLanguage = getString(R.string.setting_language_ko_kr);
        } else if (language.equals("ja")) {
            language = language + "_JP";
            curLanguage = getString(R.string.setting_language_ja_jp);
        } else {
            //SysApplication.isDeafultLangague = true;
            AccountManager.getInstances().setIsDefaultLanguage(true);
            language = language + "_US";
            curLanguage = getString(R.string.setting_language_en_us);
            SPUtil.put(this, Constants.UserInfo.USER_IS_DEFAULT_LANGUAGE, true);
        }
        LogUtils.e(language);
        SysApplication.DEFAULT_LANGUAGE = language;
        AccountManager.getInstances().setLocaleCode(language);
        AccountManager.getInstances().setLanguage(curLanguage);
    }


    class MyAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            //            if (!TextUtils.isEmpty(AccountManager.getInstances().getLocaleCode())) {
            //                String[] language = AccountManager.getInstances().getLocaleCode().split("_");
            //                Resources res = getResources();
            //                Configuration config = res.getConfiguration();
            //                config.locale = new Locale(language[0], language[1]);
            //                ConfigManager.getInstance(SplashActivity.this).updateConfig(config);
            //            } else {
                    /*Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);*/
            startDDMActivity(MainActivity.class, true);
            //            }
            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private void mySetting() {
        MySettingApi mySettingApi = new MySettingApi(this);
        mySettingApi.setListener(new HttpOnNextListener<MySettingBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(MySettingBean mySettingBean) {
                if (mySettingBean.getErrorCode() == 200) {
                    SettingManager.getInstances().setSettingBean(mySettingBean);
                }
                return null;
            }

        });

        HttpManager.getInstance().doHttpDeal(this, mySettingApi);
    }

}
