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
 * 获取用户收藏列表
 */
public class FavouriteProductListApi extends BaseApi {
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


    public FavouriteProductListApi(RxAppCompatActivity rxAppCompatActivity, HttpOnNextListener listener) {
        super(rxAppCompatActivity, listener);
        setShowProgress(false);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpService httpService = retrofit.create(HttpService.class);
        return httpService.getFavouriteProductList(getPageIndex(),getPageSize());
    }
}
