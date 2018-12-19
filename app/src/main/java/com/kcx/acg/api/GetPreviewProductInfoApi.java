package com.kcx.acg.api;

import com.kcx.acg.base.BaseApi;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpService;
import com.kcx.acg.utils.SPUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

/**
 */

public class GetPreviewProductInfoApi extends BaseApi {

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    private int productId;

    public GetPreviewProductInfoApi(RxAppCompatActivity rxAppCompatActivity) {
        super(rxAppCompatActivity);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.getPreviewProductInfo(productId);
    }
}
