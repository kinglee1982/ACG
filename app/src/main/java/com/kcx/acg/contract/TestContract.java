package com.kcx.acg.contract;

import com.kcx.acg.base.BasePresenter;
import com.kcx.acg.base.BaseView;

/**
 */

public interface TestContract {
    interface View extends BaseView<Presenter>{
        void showLoading();
        void closeLoading();
    }

    interface Presenter extends BasePresenter{
        void test();
    }
}
