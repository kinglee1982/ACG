package com.kcx.acg.contract;


import com.kcx.acg.base.BasePresenter;
import com.kcx.acg.base.BaseView;
import com.kcx.acg.model.UserInfoModel;

/**
 */

public interface UserContract {

    interface View extends BaseView<Presenter> {
        void showLoading();
        void closeLoading();
        void showUserInfo(UserInfoModel userInfoModel);
    }

    interface Presenter extends BasePresenter {
        void getUserInfo();
    }
}
