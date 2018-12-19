package com.kcx.acg.api;

import com.kcx.acg.base.BaseApi;
import com.kcx.acg.https.HttpService;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

public class GetMyBrowseHistoryApi extends BaseApi {

    private int pageIndex;
    private int pageSize;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public GetMyBrowseHistoryApi(RxAppCompatActivity rxAppCompatActivity) {
        super(rxAppCompatActivity);
        setShowProgress(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.getMyBrowseHistory(pageIndex, pageSize);
    }
}
