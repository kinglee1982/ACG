package com.kcx.acg.api;

import com.kcx.acg.base.BaseApi;
import com.kcx.acg.https.HttpService;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

public class SaveMemberAuthenticateUrlApi extends BaseApi {

    private String authenticateVideoUrl;

    public String getAuthenticateVideoUrl() {
        return authenticateVideoUrl;
    }

    public void setAuthenticateVideoUrl(String authenticateVideoUrl) {
        this.authenticateVideoUrl = authenticateVideoUrl;
    }

    public SaveMemberAuthenticateUrlApi(RxAppCompatActivity rxAppCompatActivity) {
        super(rxAppCompatActivity);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.saveMemberAuthenticateUrl(authenticateVideoUrl);
    }
}
