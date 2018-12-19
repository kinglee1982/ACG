package com.kcx.acg.api;

import com.kcx.acg.base.BaseApi;
import com.kcx.acg.https.HttpService;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by zjb on 2018/12/5.
 */
public class GetProductByIDApi extends BaseApi {

    private int productID;

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }


    public GetProductByIDApi(RxAppCompatActivity rxAppCompatActivity) {
        super(rxAppCompatActivity);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        return retrofit.create(HttpService.class).getProductByID(getProductID());
    }
}
