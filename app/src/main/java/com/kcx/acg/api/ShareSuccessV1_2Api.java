package com.kcx.acg.api;

import com.kcx.acg.base.BaseApi;
import com.kcx.acg.https.HttpService;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

public class ShareSuccessV1_2Api extends BaseApi {
    private int type;
    private int shareID;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getShareID() {
        return shareID;
    }

    public void setShareID(int shareID) {
        this.shareID = shareID;
    }

    public ShareSuccessV1_2Api(RxAppCompatActivity rxAppCompatActivity) {
        super(rxAppCompatActivity);
        setShowProgress(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.shareSuccessV1_2(type, shareID);
    }
}
