package com.kcx.acg.api;

import com.kcx.acg.base.BaseApi;
import com.kcx.acg.base.SysApplication;
import com.kcx.acg.conf.Constants;
import com.kcx.acg.https.HttpService;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.SPUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by jb on 2018/9/21.
 */
public class AddSuggesttionApi extends BaseApi {
    private String content;
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public AddSuggesttionApi(RxAppCompatActivity rxAppCompatActivity, HttpOnNextListener listener) {
        super(rxAppCompatActivity, listener);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.addSuggesttion(getContent());
    }
}
