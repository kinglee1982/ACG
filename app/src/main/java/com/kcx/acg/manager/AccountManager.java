package com.kcx.acg.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.kcx.acg.api.GetUserInfoApi;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.bean.AmazonS3ConfigBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.activity.LoginActivity;
import com.kcx.acg.views.activity.SettingActivity;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.zhihu.matisse.internal.utils.UIUtils;

import retrofit2.http.HEAD;

/**
 */

public class AccountManager {

    private static AccountManager accountManager = new AccountManager();
    private boolean isLogin = false;
    private String token;
    //用户地区码
    public String mLocaleCode;
    private String language;


    private AccountManager() {

    }

    public static AccountManager getInstances() {
        synchronized (AccountManager.class) {
            return accountManager;
        }
    }

    public boolean getSession() {
        token = SPUtil.getString(SysApplication.getContext(), Constants.ACCESS_TOKEN, "");
        if (!TextUtils.isEmpty(token)) {
            isLogin = true;
        } else {
            isLogin = false;
        }
        return true;
    }

    public boolean isLogin(Context context, boolean toLogin) {
        if (context == null) {
            return false;
        }
        getSession();
        if (!isLogin && toLogin) {
            //添加是否调整到登录界面
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
        return isLogin;
    }

    public void clearUserInfo() {
        SPUtil.putString(SysApplication.getContext(), Constants.ACCESS_TOKEN, "");
        SPUtil.putString(SysApplication.getContext(), Constants.USER_INFO, "");
    }

    public UserInfoBean getUserInfo(Context context) {
        UserInfoBean userInfoBean = null;
        String userInfoData = SPUtil.getString(context, Constants.USER_INFO, "");
        if (!TextUtils.isEmpty(userInfoData)) {
            userInfoBean = AppUtil.getGson().fromJson(userInfoData, UserInfoBean.class);
        }
        return userInfoBean;
    }

    public void refreshUserInfo(final Context context) {
        GetUserInfoApi getUserInfoApi = new GetUserInfoApi(new HttpOnNextListener<UserInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UserInfoBean userInfoBean) {
                if (userInfoBean.getErrorCode() == 200) {
                    SPUtil.putString(context, Constants.USER_INFO, AppUtil.getGson().toJson(userInfoBean));
                }
//                if(userInfoBean.getErrorCode() == 402){
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                }
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                return null;
            }
        }, (RxAppCompatActivity) context);
        HttpManager httpManager = HttpManager.getInstance();
        httpManager.doHttpDeal(context, getUserInfoApi);
    }

    private boolean mIsDefaultLanguage;

    public void setIsDefaultLanguage(boolean isDeafultLangague) {
        mIsDefaultLanguage = isDeafultLangague;
        SPUtil.put(SysApplication.getContext(), Constants.UserInfo.USER_IS_DEFAULT_LANGUAGE, isDeafultLangague);
    }

    public boolean getIsDefaultLanguage() {
        return mIsDefaultLanguage;
    }


    public void setLocaleCode(String localeCode) {
        if (!TextUtils.isEmpty(localeCode)) {
            SPUtil.put(SysApplication.getContext(), Constants.UserInfo.USER_LOCELA_CODE, localeCode);
//            mCurrencyCode=localeCode;
            mLocaleCode = localeCode;
        }
    }

    public void setAmazonS3Config(AmazonS3ConfigBean.ReturnDataBean AmazonS3ConfigBean) {
        SPUtil.saveObject(SysApplication.getContext(), Constants.UserInfo.KEY_AMAZON_S3_CONFIG, AmazonS3ConfigBean);

    }

    public AmazonS3ConfigBean.ReturnDataBean getAmazonS3Config() {
        AmazonS3ConfigBean.ReturnDataBean returnDataBean = new AmazonS3ConfigBean.ReturnDataBean();
        returnDataBean.setAccessKey(Constants.ACCESS_KEY);
        returnDataBean.setSecretKey(Constants.SECRET_KEY);
        returnDataBean.setBucketName(Constants.BUCKET_NAME);
        returnDataBean.setEndpointURL(Constants.ENDPOINT);
        returnDataBean.setEndpoint(Constants.BUCKET_REGION);
        returnDataBean.setConfigName(Constants.CONFIG_NAME);
        AmazonS3ConfigBean.ReturnDataBean bean = (AmazonS3ConfigBean.ReturnDataBean) SPUtil.readObject(SysApplication.getContext(), Constants.UserInfo.KEY_AMAZON_S3_CONFIG,returnDataBean);
        return bean;
    }

    public String getLocaleCode() {

        if (!TextUtils.isEmpty(mLocaleCode)) {
            return mLocaleCode;
        } else {
            return "zh_CN";
        }
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
