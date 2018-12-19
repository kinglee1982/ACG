package com.kcx.acg.presenter;

import android.os.Handler;


import com.kcx.acg.contract.UserContract;
import com.kcx.acg.model.UserInfoModel;

import io.reactivex.ObservableTransformer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 */

public class UserInfoPresenter implements UserContract.Presenter {
    private UserContract.View mUserView;

    public UserInfoPresenter(UserContract.View userView) {
        mUserView = checkNotNull(userView, "userView  不能为空");
        mUserView.setPresenter(this);
    }

    @Override
    public void start() {
        getUserInfo();
    }

    @Override
    public <T> ObservableTransformer<T, T> setThread() {
        return null;
    }

    @Override
    public void getUserInfo() {
        mUserView.showLoading();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserInfoModel user = new UserInfoModel();
                user.setName("anni");
                mUserView.showUserInfo(user);
                mUserView.closeLoading();
            }
        }, 5000);
    }
}
