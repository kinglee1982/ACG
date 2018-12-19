package com.kcx.acg.manager;

import android.content.Context;

import com.kcx.acg.api.GetUserInfoApi;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.SPUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public class RefreshUserInfoManager {

    private RefreshUserInfoManager() {

    }

    public static RefreshUserInfoManager getInstances() {
        synchronized (RefreshUserInfoManager.class) {
            return new RefreshUserInfoManager();
        }
    }

    public void refreshUserInfo(final Context context) {
        GetUserInfoApi getUserInfoApi = new GetUserInfoApi(new HttpOnNextListener<UserInfoBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(UserInfoBean userInfoBean) {
                if (userInfoBean.getErrorCode() == 200) {
                    SPUtil.putString(context, Constants.USER_INFO, AppUtil.getGson().toJson(userInfoBean));
                }
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


}



