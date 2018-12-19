package com.kcx.acg.manager;

import android.content.Context;

import com.kcx.acg.R;
import com.kcx.acg.api.AcceptActivityCouponApi;
import com.kcx.acg.api.IsAcceptActivityCouponApi;
import com.kcx.acg.bean.AcceptActivityCouponBean;
import com.kcx.acg.bean.IsAcceptActivityCouponBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.view.HomeDialog;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public class DialogManager {
    public static final String KEY_ACCEPT_RED_PACKET = "key_accept_red_packet";
    private static DialogManager dialogManager;
    private Context context;

    private DialogManager() {

    }

    public static DialogManager getInstances() {
        if (dialogManager == null) {
            synchronized (DialogManager.class) {
                if (dialogManager == null) {
                    return new DialogManager();
                }
            }
        }
        return dialogManager;
    }

    public void isAcceptActivityCouponApi(final Context context) {
        IsAcceptActivityCouponApi isAcceptActivityCouponApi = new IsAcceptActivityCouponApi((RxAppCompatActivity) context);
        isAcceptActivityCouponApi.setListener(new HttpOnNextListener<IsAcceptActivityCouponBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(IsAcceptActivityCouponBean isAcceptActivityCouponBean) {
                if (isAcceptActivityCouponBean.getErrorCode() == 200) {
                    if (!isAcceptActivityCouponBean.getReturnData().isIsAccepted()) {
                        final HomeDialog.Builder builder = new HomeDialog.Builder(context);

                        final HomeDialog dialog = builder.setLayoutId(R.layout.dialog_red_packet_layout).builder();

                        builder.setRedPacketData(isAcceptActivityCouponBean.getReturnData());
                        if (!dialog.isShowing())
                            dialog.show();
                        builder.setOnClickListener(new HomeDialog.Builder.OnClickListener() {
                            @Override
                            public void onConfrim() {
                                dialog.dismiss();
                                boolean isLogin = AccountManager.getInstances().isLogin( context, true);
                                if (isLogin) {
                                    acceptActivityCoupon(context);
                                }

                            }
                        });

                    }
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(context, isAcceptActivityCouponApi);
    }

    public void acceptActivityCoupon(final Context context) {
        AcceptActivityCouponApi acceptActivityCouponApi = new AcceptActivityCouponApi((RxAppCompatActivity) context);
        acceptActivityCouponApi.setListener(new HttpOnNextListener<AcceptActivityCouponBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AcceptActivityCouponBean acceptActivityCouponBean) {
                if (acceptActivityCouponBean.getErrorCode() == 200) {
                    AcceptActivityCouponBean.ReturnDataBean bean = acceptActivityCouponBean.getReturnData();
                    HomeDialog.Builder couponBuilder = new HomeDialog.Builder(context);
                    couponBuilder
                            .setLayoutId(R.layout.dialog_coupon_layout)
                            .setCouponData(bean).builder().show();
                }
                return null;
            }

        });
        HttpManager.getInstance().doHttpDeal(context, acceptActivityCouponApi);
    }
}
