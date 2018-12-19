package com.kcx.acg.manager;

import android.content.Context;
import android.content.Intent;

import com.kcx.acg.api.GetH5PayLinkApi;
import com.kcx.acg.bean.GetMyCouponListBean;
import com.kcx.acg.bean.H5PayLinkBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.views.activity.PayActivity;
import com.kcx.acg.views.view.CustomToast;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by  o on 2018/10/17.
 */

public class PayManager {

    private PayManager() {

    }

    public static PayManager getInstances() {
        synchronized (PayManager.class) {
            return new PayManager();
        }
    }

    public void goPay(Context context, int goodsType, int packageID) {
        getH5PayLink(context, goodsType, packageID, null);
    }
    public void goPay(Context context, int goodsType, int packageID, GetMyCouponListBean.ReturnDataBean.ListBean couponBean) {
        getH5PayLink(context, goodsType, packageID, couponBean);
    }

    private void getH5PayLink(final Context context, int goodsType, int packageID, GetMyCouponListBean.ReturnDataBean.ListBean couponBean) {
        GetH5PayLinkApi getH5PayLinkApi = new GetH5PayLinkApi((RxAppCompatActivity) context, new HttpOnNextListener<H5PayLinkBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(H5PayLinkBean h5PayLinkBean) {
                if (h5PayLinkBean.getErrorCode() == 200) {
                    Intent intent = new Intent(context, PayActivity.class);
                    intent.putExtra("url", h5PayLinkBean.getReturnData().getPayLink());
                    intent.putExtra("classType", 6);
                    context.startActivity(intent);

                    //Uri uri = Uri.parse(h5PayLinkBean.getReturnData().getPayLink());
//                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                    ((BaseActivity) context).startDDMActivity(it, false);
                } else {
                    CustomToast.showToast(h5PayLinkBean.getErrorMsg());
                }
                return null;
            }
        });
        getH5PayLinkApi.setBuyAmount(1);
        getH5PayLinkApi.setGoodsType(goodsType);
        getH5PayLinkApi.setPayType(2);//支付宝
        getH5PayLinkApi.setPackageID(packageID);
        getH5PayLinkApi.setMemberCouponID(couponBean != null ? couponBean.getMemberCouponID() : 0);
        HttpManager.getInstance().doHttpDeal(context, getH5PayLinkApi);
    }


}



