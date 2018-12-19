package com.kcx.acg.manager;

import android.content.Context;
import android.content.Intent;

import com.kcx.acg.R;
import com.kcx.acg.api.GetCanViewTypeApi;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.bean.GetCanViewTypeBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.activity.PreviewActivity;
import com.kcx.acg.views.activity.WorkDetailsActivity;
import com.kcx.acg.views.view.CustomToast;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by  o on 2018/10/17.
 */

public class WorkManager {

    private WorkManager() {

    }

    public static WorkManager getInstances() {
        synchronized (WorkManager.class) {
            return new WorkManager();
        }
    }

    public void goToWhere(Context context, int productId) {
        getCanViewType(context, productId);
    }
    public void goToWhere(Context context, int canViewType, int productId) {
        getCanViewType(context, productId);
    }

    public void getCanViewType(final Context context, final int productId) {
        GetCanViewTypeApi getCanViewTypeApi = new GetCanViewTypeApi((RxAppCompatActivity) context);
        getCanViewTypeApi.setProductID(productId);
        getCanViewTypeApi.setListener(new HttpOnNextListener<GetCanViewTypeBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(GetCanViewTypeBean getCanViewTypeBean) {
                if (getCanViewTypeBean.getErrorCode() == 607) {
                    CustomToast.showToast(context.getString(R.string.home_work_removed_msg));
                    return null;
                }
                if (getCanViewTypeBean.getErrorCode() == 200) {
                    Intent intent = null;
                    UserInfoBean userInfo = AccountManager.getInstances().getUserInfo(context);
                    if (userInfo == null) {
                        switch (getCanViewTypeBean.getReturnData().getCanViewType()) {
                            case Constants.Works.CANT_WATCH:
                                intent = new Intent(context, PreviewActivity.class);
                                break;
                            case Constants.Works.FREE_WATCH:
                            case Constants.Works.PAY_WATCH:
                                intent = new Intent(context, WorkDetailsActivity.class);
                                break;
                        }
                    } else {
                        if (userInfo.getReturnData().getAccountStatus() == Constants.UserInfo.AccountStatus.SUSPEND) {
                            CustomToast.showToast(context.getString(R.string.account_disable_2));
                            return null;
                        }
                        switch (userInfo.getReturnData().getUserLevel()) {
                            case Constants.UserInfo.VIP_EXPIRED:
                            case Constants.UserInfo.ORDINARY_MEMBER:
                                switch (getCanViewTypeBean.getReturnData().getCanViewType()) {
                                    case Constants.Works.CANT_WATCH:
                                        intent = new Intent(context, PreviewActivity.class);
                                        break;
                                    case Constants.Works.FREE_WATCH:
                                    case Constants.Works.PAY_WATCH:
                                        intent = new Intent(context, WorkDetailsActivity.class);
                                        break;
                                    case Constants.Works.CANT_WATCH_OF_ORIGINAL:
                                        intent = new Intent(context, WorkDetailsActivity.class);
                                        intent.putExtra(WorkDetailsActivity.KEY_SHOW_FOLLOW, true);
                                        break;
                                }
                                break;
                            case Constants.UserInfo.VIP_MEMBER:
                                switch (getCanViewTypeBean.getReturnData().getCanViewType()) {
                                    case Constants.Works.CANT_WATCH:
                                        intent = new Intent(context, PreviewActivity.class);
                                        break;
                                    case Constants.Works.VIP_WATCH:
                                        intent = new Intent(context, WorkDetailsActivity.class);
                                        intent.putExtra(WorkDetailsActivity.KEY_SHOW_VIP_TOAST, context.getString(R.string.work_details_toast_msg));
//                                        CustomToast.showToast(context, context.getString(R.string.work_details_toast_msg));
                                        break;
                                    case Constants.Works.FREE_WATCH:
                                    case Constants.Works.PAY_WATCH:
                                        intent = new Intent(context, WorkDetailsActivity.class);
                                        break;
                                    case Constants.Works.CANT_WATCH_OF_ORIGINAL:
                                        intent = new Intent(context, WorkDetailsActivity.class);
                                        intent.putExtra(WorkDetailsActivity.KEY_SHOW_FOLLOW, true);
                                        break;
                                }
                                break;

                        }
                    }

                    intent.putExtra(PreviewActivity.KEY_PRODUCT_ID, productId);
                    ((BaseActivity) context).startDDMActivity(intent, true);
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(context, getCanViewTypeApi);
    }
}
