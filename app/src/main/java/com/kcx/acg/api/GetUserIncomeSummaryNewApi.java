package com.kcx.acg.api;

import com.kcx.acg.base.BaseApi;
import com.kcx.acg.https.HttpService;
import com.kcx.acg.impl.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by zjb on 2018/11/14.
 */
public class GetUserIncomeSummaryNewApi extends BaseApi {
    private int type;



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public GetUserIncomeSummaryNewApi(RxAppCompatActivity rxAppCompatActivity, HttpOnNextListener listener) {
        super(rxAppCompatActivity, listener);
        setShowProgress(false);
    }


    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.getUserIncomeSummaryNew(getType());
    }
}
