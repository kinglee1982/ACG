package com.kcx.acg.manager;

import android.content.Context;
import android.content.Intent;

import com.kcx.acg.api.GetH5LinkUrlApi;
import com.kcx.acg.api.GetUserInfoApi;
import com.kcx.acg.bean.H5LinkUrlBean;
import com.kcx.acg.bean.UserInfoBean;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.AppUtil;
import com.kcx.acg.utils.SPUtil;
import com.kcx.acg.views.activity.ContributeActivity;
import com.kcx.acg.views.activity.WebViewActivity;
import com.kcx.acg.views.view.CustomToast;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public class H5LinkUrlManager {

    private H5LinkUrlManager() {

    }

    public static H5LinkUrlManager getInstances() {
        synchronized (H5LinkUrlManager.class) {
            return new H5LinkUrlManager();
        }
    }

    public void getH5LinkUrl(final Context context, int i) {
        GetH5LinkUrlApi getH5LinkUrlApi = new GetH5LinkUrlApi((RxAppCompatActivity) context, new HttpOnNextListener<H5LinkUrlBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(H5LinkUrlBean h5LinkUrlBean) {
                if (h5LinkUrlBean.getErrorCode() == 200){
                    Intent i = new Intent(context, WebViewActivity.class);
                    i.putExtra("url", h5LinkUrlBean.getReturnData().getH5Link());
                    context.startActivity(i);
                }else {
                    CustomToast.showToast(h5LinkUrlBean.getErrorMsg());
                }
                return null;
            }
        });
        getH5LinkUrlApi.setType(i);
        HttpManager.getInstance().doHttpDeal(context,getH5LinkUrlApi);
    }

}



